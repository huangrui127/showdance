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
import com.android.app.showdance.widget.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @ClassName: HomePageAdapter
 * @Description: 首页视频适配器
 * @author maminghua
 * @date 2015-5-12 下午02:18:57
 * 
 */
public class HomePageGridAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater listInflater;

	private List<Map<String, Object>> mediaInfoList;

	public BitmapUtils bitmapUtils;// 异步加载图片

	public HomePageGridAdapter(Context context, List<Map<String, Object>> mediaInfoList) {
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
		return mediaInfoList.size();
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

	public final class ViewHolder {
		public ImageView imgDanceVideoSnapshot;
		public CircleImageView imgDanceVideoUserAvatar;
		public TextView tvTop;// 排名
		public TextView tvDanceVideoName; // 歌曲演唱者
		public TextView tvDanceUser; // 用户名
		public TextView tvDanceVideoRemark; // 备注
		public TextView tvPraiseNum;// 点赞数量
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, Object> listItem = mediaInfoList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.activity_homepage_item, null);

			holder.imgDanceVideoSnapshot = (ImageView) convertView.findViewById(R.id.imgDanceVideoSnapshot);
			holder.imgDanceVideoUserAvatar = (CircleImageView) convertView.findViewById(R.id.imgDanceVideoUserAvatar);
			holder.tvTop = (TextView) convertView.findViewById(R.id.tvTop);
			holder.tvDanceVideoName = (TextView) convertView.findViewById(R.id.tvDanceVideoName);
			holder.tvDanceUser = (TextView) convertView.findViewById(R.id.tvDanceUser);
			holder.tvDanceVideoRemark = (TextView) convertView.findViewById(R.id.tvDanceVideoRemark);
			holder.tvPraiseNum = (TextView) convertView.findViewById(R.id.tvPraiseNum);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String mediaOldName = listItem.get("mediaOldName").toString().replace(".mp4", "");

		int s_pos = 0;
		String mediaName_temp = new String();
		mediaName_temp = mediaOldName.substring(s_pos, mediaOldName.length());

		s_pos = mediaName_temp.indexOf("_");
		if (s_pos != -1) {
			mediaName_temp = mediaName_temp.substring(0, s_pos);
		}

		// 点赞数量
		holder.tvPraiseNum.setText(listItem.get("praiseSum").toString());

		if (position < 90) {
			// 排名
			holder.tvTop.setText(listItem.get("top").toString().concat(" ."));

		}

		// 视频名
		holder.tvDanceVideoName.setText("<<" + mediaName_temp + ">>");

		// 用户名
		if (!listItem.get("name").equals("null")) {
			holder.tvDanceUser.setText(listItem.get("name").toString());
		} else {
			holder.tvDanceUser.setText("");
		}

		// 备注
		if (!listItem.get("remark").equals("null")) {
			holder.tvDanceVideoRemark.setText(listItem.get("remark").toString());
		} else {
			holder.tvDanceVideoRemark.setText("");
		}

		String snapshotUrl = listItem.get("snapshot").toString();
		String photoUrl = listItem.get("photo").toString();

		bitmapUtils.display(holder.imgDanceVideoSnapshot, ConstantsUtil.WebSite_QINIU.concat(snapshotUrl));
		bitmapUtils.display(holder.imgDanceVideoUserAvatar, ConstantsUtil.PhotoUri.concat(photoUrl));

		return convertView;
	}

}
