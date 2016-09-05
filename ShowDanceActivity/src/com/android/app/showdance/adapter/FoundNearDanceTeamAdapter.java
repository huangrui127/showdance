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


public class FoundNearDanceTeamAdapter extends BaseAdapter {

	private LayoutInflater listInflater;

	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	int[] picture_img={R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,R.drawable.found_a,
			R.drawable.found_a,R.drawable.found_a,R.drawable.found_a};
	
	public FoundNearDanceTeamAdapter(Context context, List<Map<String, Object>> menulist) {
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
		public ImageView dance_team_img;
		public TextView dance_team_name_tv,dance_team_starLevel_tv;
		public TextView dance_team_header_tv,dance_team_num_tv;
		public TextView dance_team_fansNum_tv,dance_team_distance_tv; 
		public TextView dance_team_sign_tv; 
	}

	ViewHolder holder = null;

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, Object> listItem = menulist.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			allViewHolder.add(holder);
			convertView = listInflater.inflate(R.layout.found_near_dance_team_item, null);

			holder.dance_team_img = (ImageView) convertView.findViewById(R.id.dance_team_img);
			holder.dance_team_name_tv = (TextView) convertView.findViewById(R.id.dance_team_name_tv);
			holder.dance_team_starLevel_tv = (TextView) convertView.findViewById(R.id.dance_team_starLevel_tv);
			holder.dance_team_header_tv = (TextView) convertView.findViewById(R.id.dance_team_header_tv);
			holder.dance_team_num_tv = (TextView) convertView.findViewById(R.id.dance_team_num_tv);
			holder.dance_team_fansNum_tv = (TextView) convertView.findViewById(R.id.dance_team_fansNum_tv);
			holder.dance_team_distance_tv = (TextView) convertView.findViewById(R.id.dance_team_distance_tv); 
			holder.dance_team_sign_tv = (TextView) convertView.findViewById(R.id.dance_team_sign_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		finalBitmap.display(holder.a_img, uri);
		holder.dance_team_img.setImageResource(picture_img[position]);
		holder.dance_team_name_tv.setText(listItem.get("dance_team_name").toString());
		holder.dance_team_starLevel_tv.setText(listItem.get("dance_team_starLevel").toString());
		//队长
		holder.dance_team_header_tv.setText(listItem.get("dance_team_header").toString());
		//队员数：
		String teamNum0 = listItem.get("team_num").toString();
		String teamNum1 = "队员"+"("+teamNum0+")";
		holder.dance_team_num_tv.setText(teamNum1);
		//粉丝数：
		String fansNum0 = listItem.get("dance_team_fansNum").toString();
		String fansNum1 = "粉丝"+"("+teamNum0+")";
		holder.dance_team_fansNum_tv.setText(fansNum1);
		//距离附近多少米
		holder.dance_team_distance_tv.setText(listItem.get("dance_team_distance").toString());
		holder.dance_team_sign_tv.setText(listItem.get("dance_team_sign").toString());
		
		return convertView;
	}

}
