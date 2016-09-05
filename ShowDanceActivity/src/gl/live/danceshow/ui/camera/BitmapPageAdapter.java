package gl.live.danceshow.ui.camera;

import gl.live.danceshow.media.AnimItem;
import gl.live.danceshow.media.BitmapGridFragment;
import gl.live.danceshow.media.FgAnimList;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;

/**
 * 
 * @ClassName: RecordedVideoAdapter
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-14 下午03:51:19
 * 
 */
public class BitmapPageAdapter extends FragmentPagerAdapter  {
	private Context context;
	private List<FgAnimList> mTitle = new ArrayList<FgAnimList>();
	
	public BitmapPageAdapter(FragmentManager fm,Context context) {
		super(fm);
		this.context=context;
		init(context);
	}
	
	protected  void init(Context context) {
		addmItem(new FgAnimList(BitmapGridFragment.LIST_TYPE_PERSON,context));
		addmItem(new FgAnimList(BitmapGridFragment.LIST_TYPE_TEAM,context));
		addmItem(new FgAnimList(BitmapGridFragment.LIST_TYPE_ANIM,context));
	}
	
	protected void  addmItem(FgAnimList item) {
		mTitle.add(item);
	}
	@Override
	public int getCount() {
		return mTitle.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitle.get(position).getTitle();
	}
	

	@Override
	public Fragment getItem(int arg0) {
		return mTitle.get(arg0).getFragment();
	}
}
