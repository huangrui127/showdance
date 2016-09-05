package com.android.app.showdance.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.XUtilsBitmap;
import com.android.app.wumeiniang.R;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @ClassName: DanceVideoAdapter
 * @Description: 我的秀舞适配器
 * @author maminghua
 * @date 2015-6-13 上午11:39:54
 * 
 */
public class DanceVideoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater listInflater;
	private List<Map<String, Object>> videoList;
	private	int showType;
	public BitmapUtils bitmapUtils;// 异步加载图片

	// CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
	public Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

	public DanceVideoAdapter(Context context, List<Map<String, Object>> videoList, int showType) {
		this.listInflater = LayoutInflater.from(context);
		this.context = context;
		// 引用数据
		this.videoList = videoList;

		// 异步加载图片
		bitmapUtils = XUtilsBitmap.getBitmapUtils(context);

		// 初始化,默认都是选中的
		configCheckMap(false);

		// 引用数据
		this.showType = showType;
		
	}

	/**
	 * 全选项目初始化状态
	 */
	public void configCheckMap(boolean selectState) {
		// 进行遍历
		for (int i = 0; i < videoList.size(); i++) {
			isCheckMap.put(i, selectState);
		}

	}

	@Override
	public int getCount() {
		return videoList.size();
	}

	@Override
	public Object getItem(int position) {
		return videoList.get(position);
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
			holder.cbSelected = (CheckBox) convertView.findViewById(R.id.cbSelected);
			holder.dance_user_tv = (TextView) convertView.findViewById(R.id.dance_user_tv);
			holder.dance_video_name_tv = (TextView) convertView.findViewById(R.id.dance_video_name_tv);
			holder.dance_music_name_tv = (TextView) convertView.findViewById(R.id.dance_music_name_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String musicAuthor = (String) videoList.get(position).get("musicAuthor");//
		String mediaOldName = (String) videoList.get(position).get("mediaOldName");//
		String createUser = (String) videoList.get(position).get("createUser");// 创建者
		String remark = (String) videoList.get(position).get("remark");

		String snapshotUrl = (String) videoList.get(position).get("snapshot");// 缩略图
		bitmapUtils.display(holder.dance_video_image, ConstantsUtil.WebSite_QINIU.concat(snapshotUrl));

		if (!mediaOldName.equals("null")) {
			holder.dance_user_tv.setText(mediaOldName.replace(".mp4", ""));
		} else {
			holder.dance_user_tv.setText("");
		}

		if (!remark.equals("null")) {
			holder.dance_video_name_tv.setText(remark);
		} else {
			holder.dance_video_name_tv.setText("");
		}
		
		if(showType==1){
			holder.cbSelected.setVisibility(View.GONE);
		}

		/* 状态保存 */
		holder.cbSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				/*
				 * 将选择项加载到map里面寄存
				 */
				if (isChecked) {
					isCheckMap.put(position, true);
				} else {
					isCheckMap.put(position, false);
				}
			}
		});

		/* CheckBox监听事件必须放在setChecked之前 */
		holder.cbSelected.setChecked(isCheckMap.get(position));

		// if (!holder.cbSelected.isChecked()) {
		// holder.cbSelected.setVisibility(View.VISIBLE);
		// }

		return convertView;
	}

}
