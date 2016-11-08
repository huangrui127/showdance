package com.android.app.showdance.adapter;

import java.text.ParseException;
import java.util.List;

import com.android.app.showdance.model.DanceVideoBean;
import com.android.app.showdance.utils.DensityUtil;
import com.android.app.showdance.utils.FormatCurrentData;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.wumeiniang.R;
import com.facebook.drawee.view.SimpleDraweeView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 功能：“首页”视频列表adapter
 * 
 * @author djd
 *
 */
public class DanceVideoMainAdapter extends BaseAdapter {

    private static final String TAG = "DanceVideoMainAdapter";

    private Context mContext;
    private List<DanceVideoBean> mDanceVideoBeanList;
    private LayoutInflater mInflater;
    private int mCategoryType;
    // private GridView mGridView;

    public DanceVideoMainAdapter(Context context, List<DanceVideoBean> danceVideoBeans, GridView gridView) {
        this.mDanceVideoBeanList = danceVideoBeans;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        // mGridView = gridView;
    }

    public void setCategoryType(int categoryType) {
        mCategoryType = categoryType;
    }

    public void setDanceVideoBeanList(List<DanceVideoBean> danceVideoBeanList) {
        mDanceVideoBeanList = danceVideoBeanList;
    }

    @Override
    public int getCount() {
        return mDanceVideoBeanList.size();
    }

    @Override
    public DanceVideoBean getItem(int position) {
        return mDanceVideoBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_item_dance_video, null);
            convertView.setTag(getViewHolder(holder, convertView));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateUI(holder, position, convertView);
        return convertView;
    }

    private ViewHolder getViewHolder(ViewHolder holder, View convertView) {
        holder.layoutRecent = (RelativeLayout) convertView.findViewById(R.id.id_recent_layout);
        holder.layoutPlayAndShare = (RelativeLayout) convertView.findViewById(R.id.id_playcount_and_sharecount_layout);
        holder.sdvPicBg = (SimpleDraweeView) convertView.findViewById(R.id.id_item_pic_bg_sdv);
        holder.ivTop3Medal = (ImageView) convertView.findViewById(R.id.id_top_3_medal_iv);
        holder.btnRank = (Button) convertView.findViewById(R.id.id_item_rank_btn);
        holder.btnFlower = (Button) convertView.findViewById(R.id.id_item_pic_flower_count_btn);
        holder.tvTitle = (TextView) convertView.findViewById(R.id.id_item_title_tv);
        holder.tvRecent = (TextView) convertView.findViewById(R.id.id_recent_text_tv);
        holder.tvPlayCount = (TextView) convertView.findViewById(R.id.id_item_playcount_tv);
        holder.tvShareCount = (TextView) convertView.findViewById(R.id.id_item_share_tv);
        return holder;
    }

    private void updateUI(ViewHolder viewHolder, int position, View convertView) {
        if (mDanceVideoBeanList != null && mDanceVideoBeanList.size() > 0) {
            DanceVideoBean danceVideoBean = mDanceVideoBeanList.get(position);
            String videoPicUrl = danceVideoBean.getImgSrc();
            try {
                viewHolder.tvRecent.setText(FormatCurrentData.getTimeRange(danceVideoBean.getCreatedTime()));
            } catch (ParseException e) {
                e.printStackTrace();
                L.e(TAG, e.getMessage());
            }
            if (position < 2) {
                viewHolder.btnRank.setVisibility(View.GONE);
                if (mCategoryType != 4) { // 最新视频分类下不显示排名
                    viewHolder.layoutRecent.setVisibility(View.GONE);
                    viewHolder.layoutPlayAndShare.setVisibility(View.VISIBLE);
                    viewHolder.ivTop3Medal.setVisibility(View.VISIBLE);
                    switch (position) {
                    case 0: // 亚军
                        viewHolder.ivTop3Medal.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ico_yj));
                        break;
                    case 1: // 季军
                        viewHolder.ivTop3Medal.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ico_jj));
                        break;
                    }
                } else {
                    viewHolder.layoutRecent.setVisibility(View.VISIBLE);
                    viewHolder.layoutPlayAndShare.setVisibility(View.GONE);
                    viewHolder.ivTop3Medal.setVisibility(View.GONE);
                }
            } else {
                viewHolder.ivTop3Medal.setVisibility(View.GONE);
                if (mCategoryType != 4) { // 最新视频分类下不显示排名
                    viewHolder.layoutRecent.setVisibility(View.GONE);
                    viewHolder.layoutPlayAndShare.setVisibility(View.VISIBLE);
                    viewHolder.btnRank.setVisibility(View.VISIBLE);
                    viewHolder.btnRank.setText(position + 2 + "");
                } else {
                    viewHolder.layoutRecent.setVisibility(View.VISIBLE);
                    viewHolder.layoutPlayAndShare.setVisibility(View.GONE);
                    viewHolder.btnRank.setVisibility(View.GONE);
                }
            }

            String flower = danceVideoBean.getFlower() + "";
            String title = danceVideoBean.getVideoTitle();

            viewHolder.sdvPicBg.setImageURI(videoPicUrl); // 显示网络图片
            viewHolder.btnFlower.setText(flower);
            viewHolder.tvTitle.setText(title);
            viewHolder.tvPlayCount.setText(StringUtils.getPlayCountStr(danceVideoBean.getPlayCount()));
            viewHolder.tvShareCount.setText(StringUtils.getShareCountStr(danceVideoBean.getShareCount()));
        }
    }

    public final class ViewHolder {
        public RelativeLayout layoutRecent;
        public RelativeLayout layoutPlayAndShare;
        public SimpleDraweeView sdvPicBg;
        public ImageView ivTop3Medal;
        public Button btnRank;
        public Button btnFlower;
        public TextView tvTitle;
        public TextView tvRecent;
        public TextView tvPlayCount;
        public TextView tvShareCount;
    }

}
