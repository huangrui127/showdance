package com.android.app.showdance.adapter;

import java.util.List;
import java.util.Map;

import com.android.app.showdance.model.SerializableObj;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.wumeiniang.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 
 * @ClassName: DownloadedMusicAdapter
 * @Description: 已下载舞曲
 * @author maminghua
 * @date 2015-5-13 下午02:23:56
 * 
 */
public class DownloadedMusicAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private Context context;

	private List<Map<String, Object>> musicList;

	public DownloadedMusicAdapter(Context context, List<Map<String, Object>> musicList) {
		this.listInflater = LayoutInflater.from(context);
		// 引用数据
		this.musicList = musicList;
		this.context = context;

	}

	/**
	 * 获取数据集长
	 */
	@Override
	public int getCount() {
		return musicList.size();
	}

	/**
	 * 根据位置获取当前数据
	 */
	@Override
	public Object getItem(int position) {
		return musicList.get(position);
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
	// @Override
	// public int getItemViewType(int position) {
	// return position;
	// }

	/**
	 * 返回你有多少个不同的布局样式
	 */
	// @Override
	// public int getViewTypeCount() {
	// return musicList.size();
	// }

	public final class ViewHolder {
		public TextView music_name_tv; // 舞曲名
		public TextView music_singer_tv; // 歌曲演唱者
		public TextView music_size_tv; // 音乐大小
		public TextView download_count_tv; // 下载量

		public ImageButton download_btn; // 下载

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		final Map<String, Object> listItem = musicList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			view = listInflater.inflate(R.layout.downloaded_music_item, null);

			holder.music_singer_tv = (TextView) view.findViewById(R.id.music_singer_tv);
			holder.music_name_tv = (TextView) view.findViewById(R.id.music_name_tv);
			holder.music_size_tv = (TextView) view.findViewById(R.id.music_size_tv);
			holder.download_count_tv = (TextView) view.findViewById(R.id.download_count_tv);

			holder.download_btn = (ImageButton) view.findViewById(R.id.download_btn);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		// String author = listItem.get("author").toString();
		//
		// if (!author.equals("null")) {
		// holder.music_singer_tv.setText(author);
		// } else {
		// holder.music_singer_tv.setText("");
		// }

		holder.music_name_tv.setText(listItem.get("name").toString().replace(".mp3", ""));
		// holder.download_count_tv.setText(listItem.get("downloadCount").toString());

		int pos = position;
		// 点击"秀舞"
		holder.download_btn.setOnClickListener(new MyOnClick(holder, listItem, pos));

		return view;
	}

	class MyOnClick implements OnClickListener {

		private ViewHolder holder;
		private Map<String, Object> musicItem;
		private int position;

		/**
		 * Title: Description:
		 */
		public MyOnClick(ViewHolder holder, Map<String, Object> musicItem, int position) {
			this.holder = holder;
			this.musicItem = musicItem;
			this.position = position;
		}

		/**
		 * (非 Javadoc) Title: onClick Description:
		 * 
		 * @param v
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if (position <= musicList.size()) {
				// 点击"秀舞"，跳转到视频录制页面
				musicItem = musicList.get(position);
				SerializableObj setMap = new SerializableObj();
				setMap.setMusicItem(musicItem);

				Intent intent = new Intent(ConstantsUtil.ACTION_DOWN_MEDIARECORDER);
				intent.putExtra("musicItem", setMap);
				context.sendBroadcast(intent);

			} else {
				// 数组角标越界
			}
		}
	}
}
