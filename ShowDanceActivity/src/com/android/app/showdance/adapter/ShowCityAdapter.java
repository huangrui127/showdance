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
import android.widget.TextView;


public class ShowCityAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private ViewHolder selectViewHolder;

	private List<Map<String, Object>> menulist;
	private List<ViewHolder> allViewHolder = new ArrayList<ViewHolder>();

	public ShowCityAdapter(Context context, List<Map<String, Object>> menulist) {
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
		public TextView city_letter_sort_tv;
		public TextView city_name_tv1,city_name_tv2,city_name_tv3; 
	}

	ViewHolder holder = null;

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, Object> listItem = menulist.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			allViewHolder.add(holder);
			convertView = listInflater.inflate(R.layout.activity_show_city_item, null);

			holder.city_letter_sort_tv = (TextView) convertView.findViewById(R.id.city_letter_sort_tv);
			holder.city_name_tv1 = (TextView) convertView.findViewById(R.id.city_name_tv1); 
			holder.city_name_tv2 = (TextView) convertView.findViewById(R.id.city_name_tv2);
			holder.city_name_tv3 = (TextView) convertView.findViewById(R.id.city_name_tv3);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.city_letter_sort_tv.setText(listItem.get("city_letter_sort").toString());
		holder.city_name_tv1.setText(listItem.get("city_name1").toString());
		holder.city_name_tv2.setText(listItem.get("city_name2").toString());
		holder.city_name_tv3.setText(listItem.get("city_name3").toString());
		
		return convertView;
	}

}