package gl.live.danceshow.media;

import gl.live.danceshow.media.CameraFgAdapter.OnFgItemClickListener;
import gl.live.danceshow.ui.camera.CameraPreviewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.widget.CustomAlertDialog;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class BitmapGridFragment extends Fragment implements OnItemClickListener, OnLongClickListener{
	public static final int LIST_TYPE_PERSON = 0;
	public static final int LIST_TYPE_TEAM = 1;
	public static final int LIST_TYPE_ANIM = 2;
	public static final int LIST_TYPE_AVATOR2 = 3;
	public static final int LIST_TYPE_AVATOR3 = 4;
	public static final int LIST_TYPE_AVATOR2_DOWNLOAD = 5;
	public static final int LIST_TYPE_AVATOR3_DOWNLOAD = 6;
	private OnFgItemClickListener mListener;
	private CameraFgAdapter adapter;
	public BitmapGridFragment(CameraFgAdapter adapter) {
		this.adapter =adapter;
	}
	
	public BitmapGridFragment() {
		this.adapter =null;
	}
	
	public CameraFgAdapter getAdapter() {
		return adapter;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(adapter!=null)
		adapter.updateList();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setonItemClickListener((CameraPreviewActivity)getActivity());
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		GridView v = (GridView)view.findViewById(R.id.fg_grid);
		v.setOnItemClickListener(this);
		if(adapter!=null){
		v.setOnItemLongClickListener(adapter);
		v.setAdapter(adapter);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		setonItemClickListener(null);
	}
	
	public void setonItemClickListener(OnFgItemClickListener listener) {
		mListener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.camera_fg, null);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(adapter==null)
			return;
		AnimItem mSelectId = ((AnimItem)adapter.getItem(position));
		Log.d("guolei","mSelectId "+mSelectId.getDtName());
		if(mListener!=null)
			mListener.onItemClick(mSelectId);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
	
}