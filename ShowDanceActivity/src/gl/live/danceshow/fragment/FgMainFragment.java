package gl.live.danceshow.fragment;

import gl.live.danceshow.ui.camera.BitmapPageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.android.app.showdance.ui.TeacherOfFrameActivity;
import com.android.app.wumeiniang.R;
import com.viewpagerindicator.TabPageIndicator;

public class FgMainFragment extends DialogFragment implements OnClickListener{
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int w = getContext().getResources().getDimensionPixelSize(R.dimen.fg_width);
		int h = getContext().getResources().getDimensionPixelSize(R.dimen.fg_height);
		getDialog().getWindow().setLayout(w,h);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE	, R.style.Mdialog);
	}
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		initViewAndAdapter(v);
	}
	
	protected void initViewAndAdapter(View v) {
		ViewPager pager = (ViewPager) v.findViewById(R.id.viewpager);
		TabPageIndicator indicator = (TabPageIndicator) v.findViewById(R.id.indicator);
		pager.setAdapter(new BitmapPageAdapter(getChildFragmentManager(),
				getActivity()));
		indicator.setViewPager(pager);
		Button btn = (Button) v.findViewById(R.id.camera_fg_setok);
		btn.setOnClickListener(this);
		btn = (Button) v.findViewById(R.id.frame_more);
		btn.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.camera_fg_list, container, true);
		return v;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_fg_setok:
			dismiss();
		break;
	case R.id.frame_more:
		Intent i = new Intent(getActivity(), TeacherOfFrameActivity.class);
		startActivity(i);
		break;
	default:
		break;
	}
	}
	
}