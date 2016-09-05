package com.android.app.showdance.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.XUtilsBitmap;
import com.lidroid.xutils.BitmapUtils;

public class MyDownloadVideoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater listInflater;
	private List<Map<String, Object>> menulist;

	public BitmapUtils bitmapUtils;// 异步加载图片

	public MyDownloadVideoAdapter(Context context, List<Map<String, Object>> menulist) {
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
		public ImageView dance_video_image;
		public TextView dance_user_tv, dance_video_name_tv, dance_music_name_tv;
		public CheckBox cbSelected;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.dance_video_item, null);
			holder.dance_video_image = (ImageView) convertView.findViewById(R.id.dance_video_image);
			holder.dance_user_tv = (TextView) convertView.findViewById(R.id.dance_user_tv);
			holder.dance_video_name_tv = (TextView) convertView.findViewById(R.id.dance_video_name_tv);
			holder.cbSelected = (CheckBox) convertView.findViewById(R.id.cbSelected);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		holder.cbSelected.setVisibility(View.GONE);
		
		String loginName = (String) menulist.get(position).get("loginName");//
		String remark = (String) menulist.get(position).get("remark");//
		String createUser = (String) menulist.get(position).get("createUser");//创建者
		
		String snapshotUrl = (String) menulist.get(position).get("snapshot");
		bitmapUtils.display(holder.dance_video_image, ConstantsUtil.WebSite_QINIU.concat(snapshotUrl));
		
		holder.dance_user_tv.setText(loginName);
		holder.dance_video_name_tv.setText(remark);

		return convertView;
	}

	

}
