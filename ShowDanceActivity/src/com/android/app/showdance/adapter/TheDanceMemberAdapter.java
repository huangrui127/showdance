package com.android.app.showdance.adapter;

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
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.XUtilsBitmap;
import com.lidroid.xutils.BitmapUtils;

public class TheDanceMemberAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater listInflater;
	private List<Map<String, Object>> menulist;

	public BitmapUtils bitmapUtils;// 异步加载图片

	public TheDanceMemberAdapter(Context context, List<Map<String, Object>> menulist) {
		this.listInflater = LayoutInflater.from(context);
		this.context = context;
		// 引用数据
		this.menulist = menulist;
		
		// 异步加载图片
		bitmapUtils = XUtilsBitmap.getBitmapUtils(context);

	}

	@Override
	public int getCount() {
		return menulist.size();
	}

	@Override
	public Object getItem(int position) {
		return menulist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		public ImageView ItemImage;
		public TextView ItemText;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.activity_team_dance_member_item, null);
			holder.ItemImage = (ImageView) convertView.findViewById(R.id.ItemImage);
			holder.ItemText = (TextView) convertView.findViewById(R.id.ItemText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String photoUrl = (String) menulist.get(position).get("photo");
		bitmapUtils.display(holder.ItemImage, ConstantsUtil.PhotoUri.concat(photoUrl));
		String name = (String) menulist.get(position).get("name");

		holder.ItemText.setText(name);
		
		return convertView;
	}

}
