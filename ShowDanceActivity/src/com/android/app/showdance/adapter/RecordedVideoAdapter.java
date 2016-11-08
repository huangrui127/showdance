package com.android.app.showdance.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: RecordedVideoAdapter
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-14 下午03:51:19
 * 
 */
public class RecordedVideoAdapter extends BaseAdapter {

    private static final String TAG = "RecordedVideoAdapter";

    private LayoutInflater listInflater;
    private Context context;

    private ListView recordedlv;

    private List<UploadVideoInfo> recordedVideoList = new ArrayList<UploadVideoInfo>();

    private int videoType;
    @SuppressWarnings("unused")
    private MediaMetadataRetriever mediaMetadataRetriever;

    public RecordedVideoAdapter(Context context, List<UploadVideoInfo> recordedVideoList, ListView recordedlv,
            int videoType, MediaMetadataRetriever retriever) {
        this.context = context;
        this.listInflater = LayoutInflater.from(context);
        // 引用数据
        this.recordedVideoList = recordedVideoList;
        this.recordedlv = recordedlv;
        this.videoType = videoType;
        mediaMetadataRetriever = retriever;
    }

    /**
     * 获取数据集长
     */
    @Override
    public int getCount() {
        return recordedVideoList.size();
    }

    /**
     * 根据位置获取当前数据
     */
    @Override
    public Object getItem(int position) {
        return recordedVideoList.get(position);
    }

    /**
     * 获取位置
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 
     * @Description:设置当前下载完成后界面更新
     * @param pos
     *            Item位置
     * @return void
     */
    public void setFinishUpdataView(int pos) {

        int visiablePosition = recordedlv.getFirstVisiblePosition();
        View view = recordedlv.getChildAt(pos - visiablePosition);

        if (view != null) {
            // Button uploading_btn = (Button)
            // view.findViewById(R.id.uploading_btn);
            //
            // uploading_btn.setText("已上传");
            //
            // uploading_btn.setEnabled(false);
        }
    }

    /**
     * 代表某一个样式 的 数值(由position返回view type id)
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * 返回你有多少个不同的布局样式
     */
    @Override
    public int getViewTypeCount() {
        return recordedVideoList.size();
    }

    public final class ViewHolder {
        public SimpleDraweeView a_img;
        public TextView music_name_tv; // 舞曲名
        public TextView music_singer_tv; // 歌曲演唱者
        public TextView music_size_tv; // 音乐大小
//        public TextView download_count_tv; // 下载量
        public Button uploading_btn;// , edit_btn; // 上传、删除，加片头
        public TextView delete_btn;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            view = listInflater.inflate(R.layout.recordedvideo_item, parent, false);

            holder.a_img = (SimpleDraweeView) view.findViewById(R.id.a_img);
            holder.music_singer_tv = (TextView) view.findViewById(R.id.music_singer_tv);
            holder.music_name_tv = (TextView) view.findViewById(R.id.music_name_tv);
            holder.music_size_tv = (TextView) view.findViewById(R.id.music_size_tv);
//            holder.download_count_tv = (TextView) view.findViewById(R.id.download_count_tv);
            holder.uploading_btn = (Button) view.findViewById(R.id.uploading_btn);
            holder.delete_btn = (TextView) view.findViewById(R.id.delete_btn);
            // holder.edit_btn = (Button) view.findViewById(R.id.edit_btn);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        // ((SwipeListView)parent).recycle(view, position);
        final UploadVideoInfo uploadVideo = recordedVideoList.get(position);
        String filename = uploadVideo.getFileName();
        String[] list = filename.split("_");
        holder.music_name_tv.setText(list[0]);
        if (list.length >= 2)
            holder.music_singer_tv.setText(list[1]);
        holder.music_size_tv.setText(uploadVideo.getCreateTime());
        String bgPicPath = uploadVideo.getFileBgPath();
        L.i(TAG, "视频缩略图地址："+bgPicPath);
        if (FileUtil.isFileExist(bgPicPath)) {
//            DisplayImageOptions opt = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
//                    .cacheOnDisk(false).build();
//            ImageLoader.getInstance().displayImage("file://" + bitmappath, holder.a_img, opt);
            holder.a_img.setImageURI("file://" + bgPicPath);
        } else {
            // try {
            // mediaMetadataRetriever.setDataSource(context,Uri.parse(uploadVideo.getFilePath()));
            // Bitmap bg = mediaMetadataRetriever.getFrameAtTime(-1);
            // bg = Bitmap.createScaledBitmap(bg, 160, 90, true);
            // holder.a_img.setBackground(new
            // BitmapDrawable(context.getResources(),bg));
            // } catch (IllegalArgumentException e) {
            // }
            // catch (RuntimeException e) {
            //
            // }
        }
        // mediaMetadataRetriever.release();
        if (videoType == 1) {
            L.i(TAG, "当前的videoType是："+videoType);
            holder.uploading_btn.setVisibility(View.VISIBLE);
            L.w(TAG, "当前的UploadState是："+uploadVideo.getUploadState());
            if (uploadVideo.getUploadState() == 1) {
                holder.uploading_btn.setOnClickListener(new MyOnClick(holder, uploadVideo, position, 0));
                LayoutParams p = (LayoutParams) holder.uploading_btn.getLayoutParams();
                p.width = 480;
                holder.uploading_btn.setLayoutParams(p);
                holder.uploading_btn.setText("上传到其它网站");
            } else {
                holder.uploading_btn.setText("上传");
                holder.uploading_btn.setOnClickListener(new MyOnClick(holder, uploadVideo, position, 1));
            }
        }
        // else if (videoType == 2) {
        // holder.edit_btn.setVisibility(View.VISIBLE);
        // holder.edit_btn.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        //
        // Intent intent = new Intent(context,PreSummeryEditorActivity.class);
        // intent.putExtra("path", uploadVideo.getFilePath());
        // context.startActivity(intent);
        // }
        // });
        // }

        // if (uploadVideo.getUploadState() == 1) {
        // holder.uploading_btn.setText("已上传");
        // holder.uploading_btn.setTextColor(context.getResources().getColor(R.color.red));
        // holder.uploading_btn.setEnabled(false);
        // }

        // 点击"删除"
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("guolei", "onClick " + position);
                Intent intent = new Intent(ConstantsUtil.ACTION_DEL_RECORDED);
                intent.putExtra("position", position);
                context.sendBroadcast(intent);
            }
        });
        return view;
    }

    class MyOnClick implements OnClickListener {

        private ViewHolder holder;
        private UploadVideoInfo uploadMusicItem;
        private int position;
        private int alreadUploaded;

        /**
         * Title: Description:
         */
        public MyOnClick(ViewHolder holder, UploadVideoInfo uploadMusicItem, int position, int uploadedType) {
            this.holder = holder;
            this.uploadMusicItem = uploadMusicItem;
            this.position = position;
            this.alreadUploaded = uploadedType;
        }

        /**
         * (非 Javadoc) Title: onClick Description:
         * 
         * @param v
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(View v) {
            if (NetUtil.getNetworkState(context) == NetUtil.NETWORN_NONE) {
                Toast.makeText(context, "请先连接网络！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (NetUtil.isWifiConnected(context)) {// 已开启wifi网络
                uploadVideoSelect(position, uploadMusicItem, alreadUploaded);
            } else {// 未开启wifi网络
                new CustomAlertDialog(context).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                        .setMsg("WIFI网络未开启,是否继续使用2G或3G网络上传!").setPositiveButton("确  认", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadVideoSelect(position, uploadMusicItem, alreadUploaded);
                            }
                        }).setNegativeButton("取  消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }

        }
    }

    /**
     * 上传选中的视频
     * 
     * @param position
     * @param uploadMusicItem
     * @param holder
     */
    private void uploadVideoSelect(int position, UploadVideoInfo uploadMusicItem, int uploaded) {
        if (position <= recordedVideoList.size()) {
            uploadMusicItem = recordedVideoList.get(position);
            String path = uploadMusicItem.getFilePath();
            L.d(TAG, "当前将要上传的文件路径是：" + path);
            L.d(TAG, "当前上传的类型是uploaded："+uploaded);
            Intent intent = new Intent(ConstantsUtil.ACTION_UPLOAD);
            intent.putExtra("position", position);
            intent.putExtra(ConstantsUtil.VIDEO_FILE_PATH, path);
            intent.putExtra("uploaded", uploaded);
            context.sendBroadcast(intent);
        } else {
            // 数组角标越界
        }
    }

}
