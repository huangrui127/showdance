package com.android.app.showdance.adapter;

import java.util.List;
import java.util.Set;

import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.DownloadMediaService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.DownloadFrameInfo;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.widget.CircleProgressBar;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: ShowDanceRecommendAdapter
 * @Description: 推荐舞曲适配器
 * @author maminghua
 * @date 2015-5-13 下午03:45:40
 * 
 */
public class FrameInfoListAdapter extends BaseAdapter implements ContentValue {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<DownloadFrameInfo> DownloadList;

    protected String filePathForSDCard;

    public FrameInfoListAdapter(Context mContext, List<DownloadFrameInfo> downloadInfoList, String filePathForSDCard) {
        this.mContext = mContext;

        this.mInflater = LayoutInflater.from(mContext);
        // 引用数据
        this.DownloadList = downloadInfoList;

        this.filePathForSDCard = filePathForSDCard;
    }

    /**
     * 获取数据集长
     */
    @Override
    public int getCount() {
        return DownloadList.size();
    }

    /**
     * 根据位置获取当前数据
     */
    @Override
    public Object getItem(int position) {
        return DownloadList.get(position);
    }

    /**
     * 获取位置
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 代表某一个样式 的 数值(由position返回view type id)
     */
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    /**
     * 返回你有多少个不同的布局样式
     */
    @Override
    public int getViewTypeCount() {
        return 1;// recommendList.size();
    }

    /**
     * 
     * @Description:设置当前下载完成后界面更新
     * @param pos
     *            Item位置
     * @return void
     */

    protected class MyViewHolder {
        // public ImageView fg_img;
        public SimpleDraweeView sdvFgImg;
        public TextView frame_name_tv; // 舞曲名
        public ImageView pay_type; // 歌曲演唱者
        public boolean bfree;
        // public TextView music_size_tv; // 音乐大小
        // public TextView download_count_tv; // 下载量

        public ImageView Img_download;
        public CircleProgressBar mCircleProgressBar;// 下载进度

    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DownloadFrameInfo listItem = DownloadList.get(position);
        if (listItem == null) {
            return null;
        }

        View view = null;
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            view = mInflater.inflate(R.layout.camera_more_frame, null);

            // holder.fg_img = (ImageView) view.findViewById(R.id.fg_img);
            holder.sdvFgImg = (SimpleDraweeView) view.findViewById(R.id.fg_img);
            holder.frame_name_tv = (TextView) view.findViewById(R.id.frame_name_tv);
            holder.pay_type = (ImageView) view.findViewById(R.id.pay_type);
            // holder.music_size_tv = (TextView)
            // view.findViewById(R.id.music_size_tv);
            // holder.download_count_tv = (TextView)
            // view.findViewById(R.id.download_count_tv);
            holder.mCircleProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgressbar);
            holder.Img_download = (ImageView) view.findViewById(R.id.Img_download);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (MyViewHolder) view.getTag();
        }
        updateMyItemView(listItem, holder);
        setMyState(listItem, holder);
        return view;
    }

    protected void updateMyItemView(DownloadFrameInfo item, MyViewHolder holder) {
        holder.frame_name_tv.setText(item.getName());
        int type = item.getFrame().gettype();
        holder.pay_type.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        // if(item.getFrame().getframe_type() == 0) {
        // holder.music_size_tv.setText("个人");
        // }else if(item.getFrame().getframe_type() == 1)
        // holder.music_size_tv.setText("舞队");

        // DisplayImageOptions opt = new
        // DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build();
        // ImageLoader.getInstance().displayImage(VolleyManager.SERVER_URL +
        // item.getFrame().getimg(), holder.fg_img, opt);
        holder.sdvFgImg.setImageURI(VolleyManager.SERVER_URL + item.getFrame().getimg());
        holder.Img_download.setOnClickListener(new FrameOnClick(item));
    }

    private class FrameOnClick implements OnClickListener {
        protected DownloadFrameInfo dmi;

        public FrameOnClick(DownloadFrameInfo dmi) {
            this.dmi = dmi;
        }

        @Override
        public void onClick(View v) {
            L.d("guolei", "onClick " + dmi.getName());
            if (FileUtil.isFileExist(filePathForSDCard, dmi.getName())) {
                return;
            }
            User user = InitApplication.mSpUtil.getUser();
            if (user == null) {
                mContext.sendBroadcast(new Intent(ConstantsUtil.ACTION_SHOW_PAY_INFO));
                return;
            }

            // if(dmi.getFrame().gettype() == 0) {
            downloadFileSelect(dmi);
            // return;
            // }

            // String deadline = user.getendtime();
            //
            // long time = System.currentTimeMillis();
            // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // Date d1 = new Date(time);
            // String now = format.format(d1);
            // Log.d("guolei","deadline "+deadline+" now "+now);
            // if (deadline != null && now.compareTo(deadline) > 0) {
            // mContext.startActivity(new Intent(mContext,
            // BuyInfoActivity.class));
            // return;
            // }
            // SharedPreferences.Editor editor = InitApplication.mSpUtil
            // .getEditor();
            // Set<String> value = InitApplication.mSpUtil.getSp()
            // .getStringSet(user.getPhone(), new HashSet<String>());
            // value.add(dmi.getName());
            // editor.putStringSet(user.getPhone(), value).commit();
            // downloadFileSelect(dmi);
        }

    }

    /**
     * 下载选中的MP3
     * 
     * @param position
     * @param dmi
     * @param holder
     */
    protected void downloadFileSelect(DownloadFrameInfo dmi) {

        int code = dmi.getDownloadState();
        L.d("guolei", "downloadFileSelect code " + code);
        switch (code) {
        case DOWNLOAD_STATE_SUSPEND:
            dmi.setDownloadState(DOWNLOAD_STATE_WATTING);
            Intent i = new Intent(mContext, DownloadMediaService.class);
            i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_FRAME);
            i.putExtra(DOWNLOAD_TAG_BY_INTENT, dmi);
            i.putExtra("des", filePathForSDCard);
            mContext.startService(i);
            break;
        default:
            return;
        }
    }

    protected void setMyState(DownloadFrameInfo info, MyViewHolder holder) {
        switch (info.getDownloadState()) {
        case DOWNLOAD_STATE_SUCCESS:
            // 如果下载完成,可以播放
            SharedPreferences sp = InitApplication.mSpUtil.getSp();
            User user = InitApplication.mSpUtil.getUser();
            if (info.getFrame().gettype() == 1) {
                if (user == null)
                    break;
                Set<String> sets = sp.getStringSet(user.getPhone(), null);
                if (sets == null)
                    break;
                if (!sets.contains(info.getName()))
                    break;
            }
            holder.Img_download.setVisibility(View.VISIBLE);
            holder.Img_download.setImageLevel(1);
            holder.mCircleProgressBar.setVisibility(View.GONE);
            // holder.download_ll.setBackgroundColor(mContext.getResources().getColor(R.color.title_bg));
            // holder.download_ll.setAlpha(0.4f);
            break;
        case DOWNLOAD_STATE_DOWNLOADING:
            holder.mCircleProgressBar.setVisibility(View.VISIBLE);
            holder.mCircleProgressBar.setProgress(info.getProgress());
            holder.Img_download.setVisibility(View.GONE);
            break;
        case DOWNLOAD_STATE_SUSPEND:
            // 如果已经停止,可以开始
            // holder.tv_download.setBackgroundResource(R.drawable.start);
            holder.Img_download.setVisibility(View.VISIBLE);
            holder.Img_download.setImageLevel(0);
            holder.mCircleProgressBar.setVisibility(View.GONE);
            // holder.download_ll.setBackgroundColor(Color.parseColor("#dedede"));
            // holder.current_progress.setText(listItem.getPercentage());
            // holder.current_progress.setTextColor(Color.parseColor("#23b5bc"));
            break;
        case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
            break;
        case DOWNLOAD_STATE_FAIL:
            // 如果是下载失败状态
            // holder.tv_download.setBackgroundResource(R.drawable.button_bg_retry);//
            // 重试
            holder.mCircleProgressBar.setVisibility(View.GONE);
            holder.Img_download.setVisibility(View.VISIBLE);
            holder.Img_download.setImageLevel(0);
            // holder.current_progress.setText("下载失败");
            // holder.current_progress.setTextColor(Color.parseColor("#f39801"));
            break;
        default:
            break;
        }
    }
}
