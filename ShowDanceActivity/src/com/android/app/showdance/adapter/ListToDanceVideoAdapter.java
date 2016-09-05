package com.android.app.showdance.adapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;

public class ListToDanceVideoAdapter extends BaseAdapter {

	private LayoutInflater listInflater;

	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	public ListToDanceVideoAdapter(Context context, List<Map<String, Object>> menulist) {
		this.listInflater = LayoutInflater.from(context);
		// 引用数据
		this.menulist = menulist;
	}

	/**
	 * 获取数据总大小
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

	private static class ViewHolder {
		// 声明ListView里面控件的值是变化的控件
		public ImageView dance_video_image;
		public TextView dance_video_man_tv,dance_video_name_tv;
	}

	ViewHolder holder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			allViewHolder.add(holder);
			convertView = listInflater.inflate(R.layout.dance_video_item, null);
			// 找到控件Id
			// 下订单时间
			holder.dance_video_image = (ImageView) convertView.findViewById(R.id.dance_video_image);
			holder.dance_video_man_tv = (TextView) convertView.findViewById(R.id.dance_user_tv);
			holder.dance_video_name_tv = (TextView) convertView.findViewById(R.id.dance_video_name_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// set item values to the viewHolder
		holder.dance_video_image.setImageResource((Integer) menulist.get(position).get("ItemImage"));
		holder.dance_video_man_tv.setText((String) menulist.get(position).get("dance_video_man"));
		holder.dance_video_name_tv.setText((String) menulist.get(position).get("dance_video_name"));
		
		return convertView;
	}

}
