package com.android.app.showdance.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.app.wumeiniang.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class PhotoAndAudioAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private ViewHolder selectViewHolder;
	int[] picture_img={R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,
			R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a};

	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	public PhotoAndAudioAdapter(Context context, List<Map<String, Object>> menulist) {
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

	public final class ViewHolder {
		public TextView release_dayofmonth_tv;
		public ImageView release_picture_img;
		public TextView release_content_tv; 
	}

	ViewHolder holder = null;

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, Object> listItem = menulist.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			allViewHolder.add(holder);
			convertView = listInflater.inflate(R.layout.photo_and_audio_item, null);

			holder.release_dayofmonth_tv = (TextView) convertView.findViewById(R.id.release_dayofmonth_tv);
			holder.release_picture_img = (ImageView) convertView.findViewById(R.id.release_picture_img); 
			holder.release_content_tv = (TextView) convertView.findViewById(R.id.release_content_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.release_picture_img.setImageResource(picture_img[position]);
		holder.release_dayofmonth_tv.setText(listItem.get("dayofmonth").toString());
		holder.release_content_tv.setText(listItem.get("content").toString());
		
		return convertView;
	}

}