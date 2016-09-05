package com.android.app.showdance.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.ui.VideoDetailsActivity;
import com.android.app.showdance.utils.XUtilsBitmap;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @ClassName: HomePageAdapter
 * @Description: 首页视频适配器
 * @author maminghua
 * @date 2015-5-12 下午02:18:57
 * 
 */
public class HomePageListAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater listInflater;

	private List<Map<String, Object>> mediaInfoList;

	public BitmapUtils bitmapUtils;// 异步加载图片

	private final int TYPE_COUNT = 3;

	private final int FIRST_TYPE = 0;
	private final int SECOND_TYPE = 1;
	private final int THIRD_TYPE = 2;

	private int currentType;

	public HomePageListAdapter(Context context, List<Map<String, Object>> mediaInfoList) {
		this.context = context;
		this.listInflater = LayoutInflater.from(context);
		// 引用数据
		this.mediaInfoList = mediaInfoList;

		// 异步加载图片
		bitmapUtils = XUtilsBitmap.getBitmapUtils(context);

	}

	/**
	 * 获取数据集长
	 */
	@Override
	public int getCount() {
		return TYPE_COUNT;
	}

	/**
	 * 根据位置获取当前数据
	 */
	@Override
	public Object getItem(int position) {
		return mediaInfoList.get(position);
	}

	/**
	 * 获取位置
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return FIRST_TYPE;
		} else if (position >= 1 || position <= 8) {
			return SECOND_TYPE;
		} else {
			return THIRD_TYPE;
		}
	}

	// 第一个Item的ViewHolder
	public final class FirstItemViewHolder {
		public GridView firstGridView;

	}

	// 第二个Item的ViewHolder
	public final class SecondItemViewHolder {
		public GridView secondGridView;

	}

	// 除第一、二个Item以外其余Item的ViewHolder
	public final class ThirdItemViewHolder {
		public GridView thirdGridView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View firstItemView = null;// 排名第1的view
		View secondItemView = null;// 排名第2~9的view
		View thirdItemView = null;// 排名第10以后的view

		// 获取到当前位置所对应的Type
		// currentType = getItemViewType(position);

		if (position == 0) {// 排名第1的view listview的行数第1行 下标为0

			List<Map<String, Object>> firstList = new ArrayList<Map<String, Object>>();

			if (mediaInfoList.size() > 0) {
				firstList.add(mediaInfoList.get(0));
			}

			firstItemView = convertView;
			FirstItemViewHolder firstItemViewHolder = null;
			if (firstItemView == null) {
				firstItemViewHolder = new FirstItemViewHolder();
				firstItemView = listInflater.inflate(R.layout.video_first_item, null);
				firstItemViewHolder.firstGridView = (GridView) firstItemView.findViewById(R.id.firstGridView);
				firstItemView.setTag(firstItemViewHolder);

			} else {
				firstItemViewHolder = (FirstItemViewHolder) firstItemView.getTag();
			}
			// 第1行GridView适配器
			HomePageGridAdapter mHomePageAdapter = new HomePageGridAdapter(context, firstList);
			firstItemViewHolder.firstGridView.setAdapter(mHomePageAdapter);

			firstItemViewHolder.firstGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
					startVideoDetailsActivity(position);
				}

			});

			convertView = firstItemView;

		} else if (position == 1) {// 排名第2~9的view,listview的行数第2行 下标为1

			List<Map<String, Object>> secondList = new ArrayList<Map<String, Object>>();

			if (mediaInfoList.size() > 8) {
				secondList.addAll(mediaInfoList.subList(1, 9));
			} else if (mediaInfoList.size() > 0) {
				secondList.addAll(mediaInfoList.subList(1, mediaInfoList.size()));
			}

			secondItemView = convertView;
			SecondItemViewHolder secondItemViewHolder = null;
			if (secondItemView == null) {
				secondItemViewHolder = new SecondItemViewHolder();
				secondItemView = listInflater.inflate(R.layout.video_second_item, null);
				secondItemViewHolder.secondGridView = (GridView) secondItemView.findViewById(R.id.secondGridView);
				secondItemView.setTag(secondItemViewHolder);
			} else {
				secondItemViewHolder = (SecondItemViewHolder) secondItemView.getTag();
			}
			// 第2行GridView适配器
			HomePageGridAdapter mHomePageAdapter = new HomePageGridAdapter(context, secondList);
			secondItemViewHolder.secondGridView.setAdapter(mHomePageAdapter);

			secondItemViewHolder.secondGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
					startVideoDetailsActivity(position + 1);
				}

			});

			convertView = secondItemView;

		} else if (position >= 2) {// 排名第10以后的view listview的行数第3行 下标为2
			List<Map<String, Object>> thirdList = new ArrayList<Map<String, Object>>();

			if (mediaInfoList.size() > 9) {
				thirdList.addAll(mediaInfoList.subList(9, mediaInfoList.size()));
			}

			thirdItemView = convertView;
			ThirdItemViewHolder thirdItemViewHolder = null;
			if (thirdItemView == null) {
				thirdItemViewHolder = new ThirdItemViewHolder();
				thirdItemView = listInflater.inflate(R.layout.video_third_item, null);
				thirdItemViewHolder.thirdGridView = (GridView) thirdItemView.findViewById(R.id.thirdGridView);
				thirdItemView.setTag(thirdItemViewHolder);
			} else {
				thirdItemViewHolder = (ThirdItemViewHolder) thirdItemView.getTag();
			}

			// 第3行GridView适配器
			HomePageGridAdapter mHomePageAdapter = new HomePageGridAdapter(context, thirdList);
			thirdItemViewHolder.thirdGridView.setAdapter(mHomePageAdapter);

			thirdItemViewHolder.thirdGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
					startVideoDetailsActivity(position + 9);
				}

			});

			convertView = thirdItemView;
		}

		return convertView;
	}

	private void startVideoDetailsActivity(int position) {
		if (mediaInfoList != null && mediaInfoList.size() > 0) {

			Intent mIntent = new Intent();
			mIntent.setClass(context, VideoDetailsActivity.class);

			// 选中的某一项记录
			Map<String, Object> mediaItem = mediaInfoList.get(position);
			// 传数据到视频详情页面
			mIntent.putExtra("createUser", mediaItem.get("createUser").toString());
			mIntent.putExtra("mediaId", mediaItem.get("id").toString());
			mIntent.putExtra("mediaNewName", mediaItem.get("mediaNewName").toString());
			mIntent.putExtra("mediaOldName", mediaItem.get("mediaOldName").toString());
			mIntent.putExtra("remark", mediaItem.get("remark").toString());
			mIntent.putExtra("photo", mediaItem.get("photo").toString());
			mIntent.putExtra("name", mediaItem.get("name").toString());
			mIntent.putExtra("snapshot", mediaItem.get("snapshot").toString());
			mIntent.putExtra("position", position);
			// SerializableObj setMap = new SerializableObj();
			// setMap.setMediaItem(mediaItem);
			// mIntent.putExtra("setMap", setMap);

			context.startActivity(mIntent);
		}

	}

}
