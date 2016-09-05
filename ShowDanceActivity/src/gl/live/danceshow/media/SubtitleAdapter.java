package gl.live.danceshow.media;


import gl.live.danceshow.fragment.SubtitleItem;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.DownloadMediaService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
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
public class SubtitleAdapter extends BaseAdapter {

	protected Context context;
	protected List<SubtitleItem> mList;
	protected static int mSelected=-1;
	
	public SubtitleAdapter(Context context){
		init();
	}
	
	public SubtitleAdapter(Context context,List<SubtitleItem> list) {
		this.context = context;
		mList = list;
		init();
	}
	
	protected void  init() {
		
	}
	 
	public void setSubTitleList(List<SubtitleItem> list) {
		mList = list;//new ArrayList<SubtitleItem>();
	}
	
	@Override
	public int getCount() {
		if(mList.size() == 0) return 0;
		return getCurrentList().size();
	}

	@Override
	public Object getItem(int position) {
		return getCurrentList().get(position);
	}

	public List<SubtitleItem> getCurrentList() {
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
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.subtitle_list, null); 
		}
		SubtitleItem item = getCurrentList().get(position);
//		String stringid = item.getName();
		TextView text = (TextView)convertView.findViewById(R.id.list_item_name);
		switch (item.getDownload()) {
		case ContentValue.DOWNLOAD_STATE_SUCCESS:
			text.setText("已下载");
			break;
		case ContentValue.DOWNLOAD_STATE_DOWNLOADING:
		case ContentValue.DOWNLOAD_STATE_WATTING:
		case ContentValue.DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
			text.setText("下载中");
			break;
		case ContentValue.DOWNLOAD_STATE_NONE:
			text.setText("未下载");
			break;
		default:
			text.setText("未下载");
			break;
		}
//		text.setText(stringid);
		if(item.getImg() == null)
			return convertView;
		
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY).cacheOnDisk(true)
				.build();
		ImageView img =  (ImageView)convertView.findViewById(R.id.fg_img);
		ImageLoader.getInstance().displayImage(VolleyManager.SERVER_URL+item.getImg(), img,opt);
//		img.setOnClickListener(new FrameOnClick(item));
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
}
