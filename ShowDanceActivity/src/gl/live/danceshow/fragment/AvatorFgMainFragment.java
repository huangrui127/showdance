package gl.live.danceshow.fragment;

import gl.live.danceshow.media.BitmapGridFragment;
import gl.live.danceshow.media.FgAnimList;
import gl.live.danceshow.ui.camera.BitmapPageAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.android.app.showdance.ui.CameraAvatorFramActivity;
import com.android.app.showdance.ui.TeacherOfFrameActivity;
import com.android.app.wumeiniang.R;
import com.viewpagerindicator.TabPageIndicator;

public class AvatorFgMainFragment extends FgMainFragment{
	protected void initViewAndAdapter(View v) {
		Bundle b = getArguments();
		if(b == null) {
			super.initViewAndAdapter(v);
			return;
		}
		final int mAvator = b.getInt("avator");
		ViewPager pager = (ViewPager) v.findViewById(R.id.viewpager);
		TabPageIndicator indicator = (TabPageIndicator) v.findViewById(R.id.indicator);
		pager.setAdapter(new BitmapPageAdapter(getChildFragmentManager(),
				getActivity()){
			@Override
			protected void init(Context context) {
				addmItem(new FgAnimList(mAvator, context));
				addmItem(new FgAnimList(mAvator== BitmapGridFragment.LIST_TYPE_AVATOR2?BitmapGridFragment.LIST_TYPE_AVATOR2_DOWNLOAD:
					BitmapGridFragment.LIST_TYPE_AVATOR3_DOWNLOAD, context));
			}
		});
		indicator.setViewPager(pager);
		Button btn = (Button) v.findViewById(R.id.camera_fg_setok);
		btn.setOnClickListener(this);
		btn = (Button) v.findViewById(R.id.frame_more);
//		btn.setVisibility(View.INVISIBLE);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
			case R.id.frame_more:
				Intent i = new Intent(getActivity(), CameraAvatorFramActivity.class);
				i.putExtra("avator", mAvator==BitmapGridFragment.LIST_TYPE_AVATOR2?0:1);
				startActivity(i);
				break;
			default:
				break;
			}
			
			}
		});
	}
}