package com.android.app.showdance.adapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;


public class MyFriendsAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private ViewHolder selectViewHolder;
	int[] picture_img={R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,
			R.drawable.found_a,R.drawable.found_a,R.drawable.found_a};
	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	
	public MyFriendsAdapter(Context context, List<Map<String, Object>> menulist) {
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
		public TextView letter_sort_tv;
		public TextView dance_man_tv; 
		public TextView dance_man_gender_tv,isTeamHeader_tv;
		public TextView dance_team_tv,dance_team_starLevel_tv; 
		public TextView dance_man_distance_tv; 
	}

	ViewHolder holder = null;

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, Object> listItem = menulist.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			allViewHolder.add(holder);
			convertView = listInflater.inflate(R.layout.found_near_man_item, null);

			holder.a_img = (ImageView) convertView.findViewById(R.id.a_img);
			holder.letter_sort_tv = (TextView) convertView.findViewById(R.id.letter_sort_tv);
			holder.dance_man_tv = (TextView) convertView.findViewById(R.id.dance_man_tv); 
			holder.dance_man_gender_tv = (TextView) convertView.findViewById(R.id.dance_man_gender);
			holder.isTeamHeader_tv = (TextView) convertView.findViewById(R.id.isTeamHeader_tv);
			holder.dance_team_tv = (TextView) convertView.findViewById(R.id.dance_team_tv);
			holder.dance_team_starLevel_tv = (TextView) convertView.findViewById(R.id.dance_team_starLevel_tv);
			holder.dance_man_distance_tv = (TextView) convertView.findViewById(R.id.dance_man_distance_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		finalBitmap.display(holder.a_img, );
		holder.a_img.setImageResource(picture_img[position]);
		holder.letter_sort_tv.setText(listItem.get("letter_sort").toString());
		holder.dance_man_tv.setText(listItem.get("dance_man").toString());
		holder.dance_man_gender_tv.setText(listItem.get("dance_man_gender").toString());
		holder.dance_team_tv.setText(listItem.get("dance_team").toString());
		holder.dance_man_distance_tv.setText(listItem.get("dance_man_distance").toString());
		
		return convertView;
	}

}
