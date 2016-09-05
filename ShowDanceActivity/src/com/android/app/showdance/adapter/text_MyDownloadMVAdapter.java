package com.android.app.showdance.adapter;

	import java.util.List;
import java.util.Map;

	import org.json.JSONObject;

	import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	import com.android.app.wumeiniang.R;
import com.android.app.showdance.utils.ConstantsUtil;


	public class text_MyDownloadMVAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater listInflater;
		private List<Map<String, Object>> menulist;

//		private FinalBitmap fb;

		public text_MyDownloadMVAdapter(Context context, List<Map<String, Object>> menulist) {
			this.listInflater = LayoutInflater.from(context);
			this.context = context;
//			fb = FinalBitmap.create(context);
//			fb.configLoadingImage(R.drawable.image_loading);
			// 引用数据
			this.menulist = menulist;

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
				holder.dance_music_name_tv = (TextView) convertView.findViewById(R.id.dance_music_name_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String createUser = (String) menulist.get(position).get("createUser");
			String mediaOldName = (String) menulist.get(position).get("mediaOldName");
			String mediaNewName = (String) menulist.get(position).get("mediaNewName");
//			String userId = (String) menulist.get(position).get("userId");
//			String loginName = (String) menulist.get(position).get("loginName");
			
//			fb.display(holder.dance_video_image, ConstantsUtil.DomainUrl.concat(snapshot));
			
			holder.dance_user_tv.setText(createUser);
			holder.dance_video_name_tv.setText(mediaNewName);
//			holder.dance_music_name_tv.setText(remark);

			return convertView;
		}

		

	}

