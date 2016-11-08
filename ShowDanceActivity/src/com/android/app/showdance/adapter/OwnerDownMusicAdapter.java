package com.android.app.showdance.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.app.wumeiniang.R;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

// 已下载舞曲适配器

public class OwnerDownMusicAdapter extends BaseAdapter {

	private LayoutInflater listInflater;

	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	public OwnerDownMusicAdapter(Context context, List<Map<String, Object>> menulist) {
		this.listInflater = LayoutInflater.from(context);
		// 引用数据
		this.menulist = menulist;

	}

	/**
	 * 获取数据集长
	 */
	@Override
	public int getCount() {
		return menulist.size();
	}

	/**
	 * 根据位置获取当前数据
	 */
	@Override
	public Object getItem(int position) {
		return menulist.get(position);
	}

	/**
	 * 获取位置
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

//	public final class ViewHolder {
//		public ImageView a_img;
//		public TextView music_name_tv; // 舞曲名
//		public TextView music_singer_tv; // 歌曲演唱者
//		public TextView music_size_tv; // 音乐大小
//		
//		public TextView download_count_tv; // 下载量
//
//		public LinearLayout delete_ll; //删除
//	}
	
	// 提供一个静态方法:
	public static class ViewHolder {
		// I added a generic return type to reduce the casting noise in client
		// code
		@SuppressWarnings("unchecked")
		public static <T extends View> T get(View view, int id) {
			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
			if (viewHolder == null) {
				viewHolder = new SparseArray<View>();
				view.setTag(viewHolder);
			}
			View childView = viewHolder.get(id);
			if (childView == null) {
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}
			return (T) childView;
		}
	}

	// 在getView中使用:
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = listInflater.inflate(R.layout.owner_downmusic_item, parent, false);
		}
		
		LinearLayout delete_ll; // 删除

		TextView music_name_tv = ViewHolder.get(convertView, R.id.music_name_tv);// 舞曲名
		TextView music_singer_tv = ViewHolder.get(convertView, R.id.music_singer_tv); // 歌曲演唱者
		TextView music_size_tv = ViewHolder.get(convertView, R.id.music_size_tv);// 音乐大小
		TextView download_count_tv = ViewHolder.get(convertView, R.id.download_count_tv); // 下载量
		delete_ll = ViewHolder.get(convertView, R.id.delete_ll);

		music_singer_tv.setText(menulist.get(position).get("dance_team").toString());
		music_name_tv.setText(menulist.get(position).get("dance_man").toString());
		music_size_tv.setText(menulist.get(position).get("dance_music").toString());

		// ImageView bananaView = ViewHolder.get(convertView, R.id.banana);
		// TextView phoneView = ViewHolder.get(convertView, R.id.phone);
		// BananaPhone bananaPhone = getItem(position);
		// phoneView.setText(bananaPhone.getPhone());
		// bananaView.setImageResource(bananaPhone.getBanana());

		// 点击"删除"
		delete_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		return convertView;
	}
	

//	ViewHolder holder = null;
//	
//	@SuppressLint("NewApi")
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		final Map<String, Object> listItem = menulist.get(position);
//		if (convertView == null) {
//			holder = new ViewHolder();
//			allViewHolder.add(holder);
//			convertView = listInflater.inflate(R.layout.owner_downmusic_item, null);
//
//			holder.a_img = (ImageView) convertView.findViewById(R.id.a_img);
//			// holder.letter_sort_tv = (TextView)
//			// convertView.findViewById(R.id.letter_sort_tv);
//			holder.music_singer_tv = (TextView) convertView.findViewById(R.id.music_singer_tv);
//			holder.music_name_tv = (TextView) convertView.findViewById(R.id.music_name_tv);
//			holder.music_size_tv = (TextView) convertView.findViewById(R.id.music_size_tv);
//			holder.download_count_tv = (TextView) convertView.findViewById(R.id.download_count_tv);
//
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		// finalBitmap.display(holder.a_img, );
//		// holder.letter_sort_tv.setText(listItem.get("letter_sort").toString());
//		holder.music_singer_tv.setText(listItem.get("dance_man").toString());
//		holder.music_name_tv.setText(listItem.get("dance_team").toString());
//		holder.music_size_tv.setText(listItem.get("dance_music").toString());
//
//		// 点击"删除"
//		holder.delete_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
//
//
//		return convertView;
//	}

}
