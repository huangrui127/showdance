package gl.live.danceshow.media;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.volley.toolbox.NetworkImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * @ClassName: RecordedVideoAdapter
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-14 下午03:51:19
 * 
 */
public class CameraFgAdapter extends BaseAdapter implements OnItemLongClickListener {

	protected Context context;
	protected FgAnimList mList;
	protected static int mSelected=-1;
	
	public CameraFgAdapter(Context context){
		init();
	}
	
	public CameraFgAdapter(Context context,FgAnimList list) {
		this.context = context;
		mList = list;
		init();
	}
	
	protected void  init() {
		
	}
	
	@Override
	public int getCount() {
		return getCurrentList().size();
	}

	@Override
	public Object getItem(int position) {
		return getCurrentList().get(position);
	}

	private FgAnimList getCurrentList() {
		return mList;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.bitmap_list, null); 
		}
		AnimItem item = getCurrentList().get(position);
		int stringid = item.getName();
		TextView text = (TextView)convertView.findViewById(R.id.list_item_name);
		text.setText(stringid);
		
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY).cacheOnDisk(false)
				.build();
		ImageView layout = (ImageView) convertView.findViewById(R.id.fg_img);
		if(item.getRawId()==0) {
			layout.setBackgroundColor(context.getResources().getColor(R.color.camera_noframe_color));
		}else
			ImageLoader.getInstance().displayImage("drawable://" +item.getRawId(), layout,opt);
//		
//		CheckBox box = (CheckBox)convertView.findViewById(R.id.list_item_checkbox);
//		box.setChecked(mSelected == getCurrentList().get(position).getRawId());
		return convertView;
	}



	
	public interface OnFgItemClickListener {
		void onItemClick(AnimItem item);
	}




	public void updateList() {
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

//	private void updateTitle() {
//		Activity activity = 
//				((CameraPreviewActivity) context);
//				Button btn = (Button)
//				activity.findViewById(R.id.oneBtn);
//				btn.setTextColor(bTeam?Color.GRAY:Color.WHITE);
//				btn = (Button)
//						activity.findViewById(R.id.teamBtn);
//						btn.setTextColor(bTeam?Color.WHITE:Color.GRAY);
//	}
//	@Override
//	public void onClick(View v) {
//		bTeam = v.getId() == R.id.teamBtn;
//		updateTitle();
//		notifyDataSetChanged();
//	}

}
