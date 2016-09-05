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
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.DownloadMediaService;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CircleProgressBar;
import com.android.app.showdance.widget.CustomAlertDialog;

/**
 * 
 * @ClassName: ShowDanceRecommendAdapter
 * @Description: 推荐舞曲适配器
 * @author maminghua
 * @date 2015-5-13 下午03:45:40
 * 
 */
public abstract class DownloadAdapter<T> extends BaseAdapter implements ContentValue {

	protected LayoutInflater mInflater;
	protected Context mContext;

	protected List<T> DownloadList;


	protected String filePathForSDCard;

	protected String recordedAction;

	protected String downloadAction;

	public DownloadAdapter(Context mContext, List<T> downloadInfoList, String filePathForSDCard, String recordedAction, String downloadAction) {
		this.mContext = mContext;

		this.mInflater = LayoutInflater.from(mContext);
		// 引用数据
		this.DownloadList = downloadInfoList;


		this.filePathForSDCard = filePathForSDCard;

		this.recordedAction = recordedAction;

		this.downloadAction = downloadAction;
	}



	/**
	 * 获取位置
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	protected abstract void setDownloadListener(View v);
	
	protected class ViewHolder {
		public ImageView avatar_img;
		public TextView music_name_tv; // 舞曲名
		public TextView music_singer_tv; // 歌曲演唱者
		public TextView music_size_tv; // 音乐大小
		public TextView download_count_tv; // 下载量
		public boolean bdownloadok;
		public ImageView Img_download;
		public CircleProgressBar mCircleProgressBar;// 下载进度

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		T listItem = DownloadList.get(position);
		if (listItem == null) {
			return null;
		}
		
		View view = null;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.showdance_recommend_item, null);

			holder.avatar_img = (ImageView) view.findViewById(R.id.avatar_img);
			holder.music_singer_tv = (TextView) view.findViewById(R.id.music_singer_tv);
			holder.music_name_tv = (TextView) view.findViewById(R.id.music_name_tv);
			holder.music_size_tv = (TextView) view.findViewById(R.id.music_size_tv);
			holder.download_count_tv = (TextView) view.findViewById(R.id.download_count_tv);
			holder.mCircleProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgressbar);
			holder.Img_download = (ImageView) view.findViewById(R.id.Img_download);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		updateItemView(listItem, holder);
		setState(listItem,holder);
		return view;
	}
	protected abstract void updateItemView(T item,ViewHolder holder);
	protected abstract void setState(T item,ViewHolder holder);
	
	class MyOnClick implements OnClickListener {

		protected ViewHolder holder;
		protected T dmi;

		/**
		 * Title: Description:
		 */
		public MyOnClick(ViewHolder holder, T dmi) {
			this.holder = holder;
			this.dmi = dmi;
		}

		/**
		 * (非 Javadoc) Title: onClick Description:
		 * 
		 * @param v
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {

//			if (NetUtil.isWifiConnected(mContext)) {// 已开启wifi网络
				downloadFileSelect(dmi, holder);
//			} else {// 未开启wifi网络
//				// 自定义有标题、确定按钮与取消按钮对话框使用方法
//				new CustomAlertDialog(mContext).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示").setMsg("WIFI网络未开启,是否继续使用2G或3G网络下载!").setPositiveButton("确  定", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						downloadFileSelect(dmi, holder);
//					}
//				}).setNegativeButton("取  消", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//
//					}
//				}).show();
//
//			}

		}
	}

	protected abstract void downloadFileSelect( T dmi, ViewHolder holder);

}
