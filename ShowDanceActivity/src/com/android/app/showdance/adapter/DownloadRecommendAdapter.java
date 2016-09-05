package com.android.app.showdance.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.DownloadAdapter.ViewHolder;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.DownloadMediaService;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CircleProgressBar;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @ClassName: ShowDanceRecommendAdapter
 * @Description: 推荐舞曲适配器
 * @author maminghua
 * @date 2015-5-13 下午03:45:40
 * 
 */
public class DownloadRecommendAdapter extends DownloadAdapter<DownloadMusicInfo> implements ContentValue {


	public DownloadRecommendAdapter(Context mContext, List<DownloadMusicInfo> downloadInfoList, String filePathForSDCard, String recordedAction, String downloadAction) {
		super(mContext, downloadInfoList, filePathForSDCard, recordedAction, downloadAction);
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
		return 1;//recommendList.size();
	}

	@Override
	protected void setState(DownloadMusicInfo item,ViewHolder holder) {
		Log.d("guolei"," setState item name "+item.getName() + "state "+item.getDownloadState());
		switch (item.getDownloadState()) {
		case DOWNLOAD_STATE_SUCCESS:
			// 如果下载完成,可以播放
			holder.bdownloadok = true;
			holder.Img_download.setVisibility(View.VISIBLE);
			holder.Img_download.setImageLevel(1);
			holder.mCircleProgressBar.setVisibility(View.GONE);
			break;
		case DOWNLOAD_STATE_DOWNLOADING:
			// 如果下载中,可以停止
			// holder.tv_download.setBackgroundResource(R.drawable.stop);
			Log.d("guolei","name = "+item.getName());
			holder.mCircleProgressBar.setVisibility(View.VISIBLE);
			holder.mCircleProgressBar.setProgress(item.getPercentage());
			holder.Img_download.setVisibility(View.GONE);
			// holder.current_progress.setText(listItem.getPercentage());
			// holder.current_progress.setTextColor(Color.parseColor("#23b5bc"));
			break;
		case DOWNLOAD_STATE_SUSPEND:
		case DOWNLOAD_STATE_FAIL:
			// 如果已经停止,可以开始
			// holder.tv_download.setBackgroundResource(R.drawable.start);
			holder.bdownloadok = false;
			holder.Img_download.setVisibility(View.VISIBLE);
			holder.mCircleProgressBar.setVisibility(View.GONE);
			holder.Img_download.setImageLevel(0);
			// holder.current_progress.setText(listItem.getPercentage());
			// holder.current_progress.setTextColor(Color.parseColor("#23b5bc"));
			break;
		case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
			// 如果不在当前下载队列之内
			// holder.tv_download.setBackgroundResource(R.drawable.stop);
			// holder.current_progress.setText("等待中");
			// holder.current_progress.setTextColor(Color.parseColor("#23b5bc"));
			break;
//		case DOWNLOAD_STATE_FAIL:
			// 如果是下载失败状态
			// holder.tv_download.setBackgroundResource(R.drawable.button_bg_retry);//
			// 重试
//			holder.tv_download.setText("重试");
//			holder.tv_download.setTextColor(Color.parseColor("#333333"));
			// holder.current_progress.setText("下载失败");
			// holder.current_progress.setTextColor(Color.parseColor("#f39801"));
//			break;
		default:
			break;
		}
	}
	
	@Override
	protected void updateItemView(DownloadMusicInfo item,ViewHolder holder) {
			holder.music_name_tv.setText(item.getName());
			String author = item.getAuthor();
			if (!author.equals("null")) {
				holder.music_singer_tv.setText(author);
			} else {
				holder.music_singer_tv.setText("");
			}
			holder.music_size_tv.setText(item.getFileSize().concat(" MB"));
			holder.Img_download.setOnClickListener(new MyOnClick(holder, item));
	}


	/**
	 * 下载选中的MP3
	 * 
	 * @param position
	 * @param dmi
	 * @param holder
	 */
	@Override
	protected void downloadFileSelect(DownloadMusicInfo dmi,
			ViewHolder holder) {
		if (dmi.getDownloadState()==DOWNLOAD_STATE_SUCCESS) {
			if (holder.bdownloadok) {
				// 点击"秀舞"，跳转到视频录制页面
				Intent intent = new Intent(recordedAction);
				intent.putExtra("musicItem", dmi);
				mContext.sendBroadcast(intent);
			}
			return;
		}
		
		final Intent i = new Intent(mContext, DownloadMediaService.class);
		int code = dmi.getDownloadState();
		switch (code) {
		case DOWNLOAD_STATE_SUSPEND:
			holder.mCircleProgressBar.setVisibility(View.VISIBLE);
			holder.Img_download.setVisibility(View.GONE);
			dmi.setDownloadAction(downloadAction);
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, dmi);
			dmi.setDownloadState(DOWNLOAD_STATE_WATTING);
			mContext.startService(i);
			break;
		default:
			return;
		}
		
	}

	@Override
	protected void setDownloadListener(View v) {
		// TODO Auto-generated method stub
		
	}
}
