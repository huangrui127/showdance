package com.android.app.showdance.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.showdance.adapter.FrameInfoListAdapter;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.FrameEvent;
import com.android.app.showdance.model.DownloadFrameInfo;
import com.android.app.showdance.model.glmodel.FrameInfo;
import com.android.app.showdance.model.glmodel.FrameInfo.AvatorFrame;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: ShowDanceActivity
 * @Description: 秀舞界面
 * @author maminghua
 * @date 2015-5-6 下午02:48:16
 * 
 */

public class CameraAvatorFramActivity extends VolleyBaseActivity {
	private static final String TAG = "CameraAvatorFramActivity";

	private Button login_btn;

	private PullToRefreshGridView frameList;

	private FrameInfoListAdapter showDanceRecommendAdapter;
	private List<DownloadFrameInfo> recommendFrameList = new ArrayList<DownloadFrameInfo>();


	/** 录制最长时间 */
	public static int RECORD_TIME_MAX = 10 * 1000;

	public static final int REQUEST_RECORD = 0;

	public static final int REQUEST_VIDEO_REVIEW = 1;
	private int mAvatorType = 0;

//	private File renameFileNewPath;// 转码完成后的文件重命名并移至新的目录


//	private int curPage = 1;
//	private int totalPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moreframe);
		Intent i = getIntent();
		if(i != null)
		mAvatorType = i.getIntExtra("avator", 0);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);


//		moreframe_layout = (LinearLayout) findViewById(R.id.moreframe_layout);
//		no_login_layout = (LinearLayout) findViewById(R.id.no_login_layout);
		login_btn = (Button) findViewById(R.id.login_btn);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		frameList = (PullToRefreshGridView) findViewById(R.id.frameList);
		frameList.getRefreshableView().setNumColumns(2);
		frameList.setMode(Mode.PULL_FROM_START);
		initPulltoRefreshListView(frameList);
	}

	@Override
	protected void initView() {
		tvTitle.setText("边框列表");
		tvTitle.setVisibility(View.VISIBLE);
		return_imgbtn.setVisibility(View.VISIBLE);
		inflater = LayoutInflater.from(this);


		checkUserInfoAndRefresh(InitApplication.mSpUtil.getUser(),true);


		InitApplication.mSpUtil.setFirstRefeshShowDance(1);

	}
	@Override
	protected String[] initActions() {
		return new String[] {ConstantsUtil.ACTION_SHOW_PAY_INFO };
	}
	
	 private void initPulltoRefreshListView(PullToRefreshGridView ptrlistview)    
	    {
		 Log.d(TAG,"initPulltoRefreshListView");
	        ILoadingLayout startLabels = ptrlistview    
	                .getLoadingLayoutProxy(true, false);    
	        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示    
	        startLabels.setRefreshingLabel("正在载入...");// 刷新时    
	        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
//	        ILoadingLayout endLabels = ptrlistview.getLoadingLayoutProxy(    
//	                false, true);    
//	        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示    
//	        endLabels.setRefreshingLabel("正在载入...");// 刷新时    
//	        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
	        
	        ptrlistview.setOnRefreshListener(new OnRefreshListener2<GridView>() {

				@Override
				public void onPullDownToRefresh(
						PullToRefreshBase<GridView> refreshView) {
					String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
	                // Update the LastUpdatedLabel  
					Log.d(TAG,"onPullDownToRefresh label "+label);
	                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	                pulltoRefreshListView(1);
				}

				@Override
				public void onPullUpToRefresh(
						PullToRefreshBase<GridView> refreshView) {
//					if(curPage == totalPage)
//						refreshView.onRefreshComplete();
//					else
//						pulltoRefreshListView(curPage+1);
					
				}
			});
	        
	    }
	 
	
	@Override
	protected void setOnClickListener() {
		login_btn.setOnClickListener(this);
		return_imgbtn.setOnClickListener(this);
//		frameList.setOnItemClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
//		EventBus.getDefault().unregister(this);
	}

	/**
	 * Activity恢复时执行 当Activity可以得到用户焦点的时候就会调用onResume方法
	 */
	@Override
	protected void onResume() {
		super.onResume();
		EventBus.getDefault().registerSticky(this);
		checkUserInfoAndRefresh(InitApplication.mSpUtil.getUser(),false);
	}
	
	@Override
	protected void onPause() {
		EventBus.getDefault().unregister(this);
		super.onPause();
	}
	
	private void checkUserInfoAndRefresh(User user,boolean oncreate) {
//		if (userInfo == null) {
//			showdance_layout.setVisibility(View.GONE);
//			no_login_layout.setVisibility(View.VISIBLE);
//		} else 
		{
//			showdance_layout.setVisibility(View.VISIBLE);
//			no_login_layout.setVisibility(View.GONE);
//			id = userInfo.getId(); // 用户登录名
			int firstRefeshShowDance = InitApplication.mSpUtil.getFirstRefeshShowDance();
			if (firstRefeshShowDance == 2 || oncreate) {
//				showProgressDialog(this, refreshType, pageNo);
				frameList.setRefreshing(false);
			}

		}
	}

	private void pulltoRefreshListView(int pageno) {
		pulltoRefreshListView(pageno,Integer.MAX_VALUE);
	}
	
	private void pulltoRefreshListView(int pageno,int pagesize) {
				
//				VDanceMusicPageVo danceMusicPageVo = new VDanceMusicPageVo();
//				paramsMap = new HashMap<String, Object>();
//				danceMusicPageVo.setPageNo(pageno);
//				danceMusicPageVo.setPageSize(pagesize);
//				paramsMap.put("danceMusicPageVo", danceMusicPageVo);
//				paramsMap.put("danceMusicType", ConstantsUtil.showDanceHome);
//
//				Task mTask = new Task(TaskType.TS_danceMusicPageList, paramsMap);
//				MainService.newTask(mTask);
				
		FrameInfo.AvatorRequest request = new FrameInfo.AvatorRequest(mAvatorType);
		VolleyManager.getInstance().postRequest(
				request,
				VolleyManager.METHOD_MASK_LIST,
				mMusicResponseListener, mErrorListener);
	}
	private OnResponseListener<FrameInfo.AvatorResponse> mMusicResponseListener = new OnResponseListener<FrameInfo.AvatorResponse>(FrameInfo.AvatorResponse.class) {
		@Override
		public void handleResponse(FrameInfo.AvatorResponse response) {
			frameList.onRefreshComplete();
			List<AvatorFrame> list = response.getData();
			if(list == null)
				return;
			recommendFrameList.clear();
			
//			SharedPreferences.Editor editor = InitApplication.mSpUtil.getEditor();
			
			for(AvatorFrame item:list) {
				Log.d(TAG,"frame = "+item.toString());
				DownloadFrameInfo downmusic = new DownloadFrameInfo(item,mAvatorType==0?InitApplication.sdCardAvator2ForegroundPath:InitApplication.sdCardAvator3ForegroundPath);
//				editor.putInt(item.getname(), item.gettype());
				recommendFrameList.add(downmusic);
			}
//			editor.commit();
			if(showDanceRecommendAdapter ==null) {
				showDanceRecommendAdapter = new FrameInfoListAdapter(CameraAvatorFramActivity.this, recommendFrameList,
						mAvatorType== 0?InitApplication.sdCardAvator2ForegroundPath:InitApplication.sdCardAvator3ForegroundPath);
				frameList.setAdapter(showDanceRecommendAdapter);//
			}
			showDanceRecommendAdapter.notifyDataSetChanged();
		}
	
	@Override
	protected void handleFailResponse(ResponseFail response) {
		Toast.makeText(getApplicationContext(), ""+response.getMessage(),
				Toast.LENGTH_SHORT).show();
	}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回
			this.finish();
			break;
		default:
			break;
		}

	}

	private void showPlayInfoDialog() {
		if (InitApplication.mSpUtil.getUser() == null) {
			Toast.makeText(getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT)
					.show();
			Intent i = new Intent();
			i.setClass(CameraAvatorFramActivity.this,
					OwnerPhoneRegisterActivity.class);
			startActivity(i);
			return;
		}
		CustomAlertDialog mCustomDialog = new CustomAlertDialog(
				CameraAvatorFramActivity.this)
				.builder(R.style.DialogTVAnimWindowAnim);

		mCustomDialog.setTitle("收费边框");
		mCustomDialog.setMsg("购买请联系 13429845118");
		mCustomDialog.setPositiveButton(
				getResources().getString(R.string.dialog_ok),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}
	
	@Override
	protected void handleReceiver(Context context, Intent intent) {

		 if (intent.getAction().equals(ConstantsUtil.ACTION_SHOW_PAY_INFO)){
			showPlayInfoDialog();
			return;
		}
	}
	
	public void onEventMainThread(FrameEvent event) {
		
		int musicId = event.musicId;
		if(musicId==-1)
			return;
		DownloadFrameInfo item = null;
		for(DownloadFrameInfo info :recommendFrameList) {
			if(info.getFrame().getId()==musicId) {
				item = info;
				break;
			}
		}
		if(item == null)
			return;
		// 下载进度
			int state = event.state;
			item.setDownloadState(state);			
			Log.d("guolei","item "+item.getFrame().toString());
			switch (state) {
			case ContentValue.DOWNLOAD_STATE_DOWNLOADING:
				long total =event.total;
				// 当前进度
				long current = event.current;
				int percentage = (int) (((int) current * 100) / (int) total);
				item.setProgress(percentage);
				break;
			case ContentValue.DOWNLOAD_STATE_SUCCESS:
//				new UnzipTask().execute(intent.getStringExtra("downloadpath"),item.getName());
				break;
			case ContentValue.DOWNLOAD_STATE_FAIL:
//				showPlayInfoDialog();
				Toast.makeText(getApplicationContext(), "下载失败！", Toast.LENGTH_SHORT)
				.show();
				break;
			default:
				return;
			}
			showDanceRecommendAdapter.notifyDataSetChanged();

	}

	
	/**
	 * 准备音乐文件
	 * 
	 * @return
	 */

	/**
	 * 准备歌词文件
	 * 
	 * @return
	 */

	@Override
	protected void handleErrorResponse(VolleyError error) {
		frameList.onRefreshComplete();
		Log.d(TAG,"error "+error);
	}
	
	@Override
	public void refresh(Object... param) {
		
	}

	@Override
	protected boolean validateData() {
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(this).setMessage("").create();
	}

//	private boolean deleteDir(File dir) {
//		if (dir.isDirectory()) {
//			String[] children = dir.list();
//			for (int i = 0; i < children.length; i++) {
//				boolean success = deleteDir(new File(dir, children[i]));
//				if (!success) {
//					return false;
//				}
//			}
//		}
//		// The directory is now empty so now it can be smoked
//		return dir.delete();
//	} 
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
//
//		final String fileName = recommendFrameList.get(position).getFileName().toString();
//		if (!FileUtil.isFileExist(InitApplication.sdCardForegroundPath, fileName)) {
//			Toast.makeText(this, "边框未下载", Toast.LENGTH_LONG).show();
//			return;
//		}
//		
//		CustomAlertDialog mCustomDialog = new CustomAlertDialog(CameraMoreFramActivity.this).builder(R.style.DialogTVAnimWindowAnim);
//		mCustomDialog.setTitle("删除提示");
//		mCustomDialog.setMsg("确认删除" + fileName + "吗?");
//		mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				File delFilePath = new File(InitApplication.sdCardForegroundPath.concat(fileName));
//				Log.d("guolei","delFilePath "+deleteDir(delFilePath));
//				DownloadFrameInfo item = recommendFrameList.get(position);
//				item.setDownloadState(ContentValue.DOWNLOAD_STATE_SUSPEND);
//				showDanceRecommendAdapter.notifyDataSetChanged();
//			}
//		}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		}).show();
//	}
	
}
