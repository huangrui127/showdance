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


public class MyLastChatAdapter extends BaseAdapter {

	private LayoutInflater listInflater;

	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	public MyLastChatAdapter(Context context, List<Map<String, Object>> menulist) {
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
		public ImageView a_img;
		public TextView dance_man_tv; 
		public TextView time_tv; 
		public TextView topic_tv;
	}

	ViewHolder holder = null;

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, Object> listItem = menulist.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			allViewHolder.add(holder);
			convertView = listInflater.inflate(R.layout.my_last_chat_item, null);

			holder.a_img = (ImageView) convertView.findViewById(R.id.a_img);
			holder.dance_man_tv = (TextView) convertView.findViewById(R.id.dance_man_tv); 
			holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
			holder.topic_tv = (TextView) convertView.findViewById(R.id.topic_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		finalBitmap.display(holder.a_img, );
		//holder.letter_sort_tv.setText(listItem.get("letter_sort").toString());
		holder.dance_man_tv.setText(listItem.get("dance_man").toString());
		holder.time_tv.setText(listItem.get("time_tv").toString());
		holder.topic_tv.setText(listItem.get("topic_tv").toString());
		
		return convertView;
	}

}
