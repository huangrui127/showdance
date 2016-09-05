package com.android.app.wumeiniang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import gl.live.danceshow.ui.camera.CameraPreviewActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.wumeiniang.app.SocialSdkHandler;
import com.android.app.showdance.adapter.DownloadRecommendAdapter;
import com.android.app.showdance.baidupush.Utils;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.MusicEvent;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.glmodel.MusicDownloadOKRequest;
import com.android.app.showdance.model.glmodel.MusicInfo;
import com.android.app.showdance.model.glmodel.MusicInfo.*;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.ui.DownloadedMusicActivity;
import com.android.app.showdance.ui.FoundSearchMusicActivity;
import com.android.app.showdance.ui.PreSummeryEditorActivity;
import com.android.app.showdance.ui.RecordedVideoActivity;
import com.android.app.showdance.ui.SplashActivity;
import com.android.app.showdance.ui.TeacherActivity;
import com.android.app.showdance.ui.UseIntroductionActivity;
import com.android.app.showdance.ui.VolleyBaseActivity;
import com.android.app.showdance.ui.usermanager.OwnerLoginActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.widget.MyProgressBar;
import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.baidu.android.pushservice.PushConstants;
//import com.baidu.android.pushservice.PushManager;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: ShowDanceActivity
 * @Description: 秀舞界面
 * @author maminghua
 * @date 2015-5-6 下午02:48:16
 * 
 */

public class ShowDanceActivity extends VolleyBaseActivity {
	private static final String TAG = "ShowDanceActivity";
	private LinearLayout serach_layout; // 搜索框跳转
	private LinearLayout group_ll;
	private Button downloadedMusicBtn, recordedVideoBtn;

	private Button btn_tearch, btn_rhythm, btn_edit;


	private Button login_btn;

	private PullToRefreshListView recommendMusicListView;

	private DownloadRecommendAdapter showDanceRecommendAdapter;
	private List<DownloadMusicInfo> recommendMusicList = new ArrayList<DownloadMusicInfo>();

	private static final int VIDEO_CAPTURE = 2;


	private long id;

	private DownloadMusicInfo downMusicItem;

	/** 录制最长时间 */
	public static int RECORD_TIME_MAX = 10 * 1000;

	public static final int REQUEST_RECORD = 0;

	public static final int REQUEST_VIDEO_REVIEW = 1;
	public static final int REQUEST_EDIT = 6;
	private File videoFile;// 录好后的原始视频路径



//	private File renameFileNewPath;// 转码完成后的文件重命名并移至新的目录

	private boolean loadingFlag = true;// 合成中进度标志


//	private int curPage = 1;
//	private int totalPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_showdance);
		startMainService();
		MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setScenarioType(this,EScenarioType.E_UM_NORMAL);
//		UmengUpdateAgent.update(this);
		findViewById();
		initView();
		setOnClickListener();
		
		updateUserInfo();
		
	}

	public static class UpdateInfoRequest {
		private String string;
		private int type;
		
		public void setstring(String arg) {
			string = arg;
		}
		public String getstring() {
			return string;
		}
public void settype(int arg) {
		type =arg;
		}
		public int gettype() {
			return type;
		}
	}			
	
	private void updateUserInfo() {
		
		boolean buploadinfo = InitApplication.mSpUtil.getSp().getBoolean(
				"uploadinfo1", false);
		if (buploadinfo)
			return;
		final User user = InitApplication.mSpUtil.getUser();

		if (user == null) {
			return;
		}
		InitApplication.mSpUtil.getSp().edit().putBoolean(
				"uploadinfo1", true);
		MobclickAgent.reportError(this, "user phone = "+user.getPhone());
		UpdateInfoRequest request = new UpdateInfoRequest();
		request.setstring(getAppVersionName(ShowDanceActivity.this) + ";product:"
				+ Build.PRODUCT + ";phone:" + user.getPhone());
		request.settype(10);
		VolleyManager.getInstance().postRequest(
				request,
				VolleyManager.METHOD_COUNT,
				null, mErrorListener);
	}
	
	 private String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
	        int  versioncode = pi.versionCode;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        Log.e("VersionInfo", "Exception", e);  
	    }  
	    return versionName;  
	}  
	
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		group_ll = (LinearLayout) findViewById(R.id.group_ll);
		downloadedMusicBtn = (Button) findViewById(R.id.downloadedMusicBtn);
		recordedVideoBtn = (Button) findViewById(R.id.recordedVideoBtn);

		btn_tearch = (Button) findViewById(R.id.btn_tearch);
		btn_rhythm = (Button) findViewById(R.id.btn_rhythm);
		btn_edit = (Button) findViewById(R.id.btn_edit);

		serach_layout = (LinearLayout) findViewById(R.id.serach_layout);
		login_btn = (Button) findViewById(R.id.login_btn);

		
		//tuijian
		cancel_tv =  (TextView) findViewById(R.id.cancel_tv);
		cancel_tv.setText("推荐");
		cancel_tv.setTextSize(15);
		cancel_tv.setVisibility(View.VISIBLE);
		cancel_tv.setOnClickListener(new SocialSdkHandler(this));
		
		recommendMusicListView = (PullToRefreshListView) findViewById(R.id.recommendMusicList);
		recommendMusicListView.setMode(Mode.PULL_FROM_START);
		initPulltoRefreshListView(recommendMusicListView);
	}

	@Override
	protected void initView() {
		tvTitle.setText("秀舞池");
		tvTitle.setVisibility(View.GONE);
		group_ll.setVisibility(View.VISIBLE);
		inflater = LayoutInflater.from(this);
		checkUserInfoAndRefresh(InitApplication.mSpUtil.getUser(),true);
		InitApplication.mSpUtil.setFirstRefeshShowDance(1);
	}
	@Override
	protected String[] initActions() {
		return new String[] { ConstantsUtil.ACTION_SHOW_MEDIARECORDER};
	}
	 private void initPulltoRefreshListView(PullToRefreshListView ptrlistview)    
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
	        
	        ptrlistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

				@Override
				public void onPullDownToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
	                // Update the LastUpdatedLabel  
					Log.d(TAG,"onPullDownToRefresh label "+label);
	                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	                pulltoRefreshListView(1);
				}

				@Override
				public void onPullUpToRefresh(
						PullToRefreshBase<ListView> refreshView) {
//					if(curPage == totalPage)
//						refreshView.onRefreshComplete();
//					else
//						pulltoRefreshListView(curPage+1);
					
				}
			});
	        
	    }
	 
	private void creatShortCut() {
		Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		String title = getResources().getString(R.string.app_name);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.app_logo);
		addIntent.putExtra("duplicate", false);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		Intent loginIntent = new Intent(this, SplashActivity.class);
		loginIntent.setAction("android.intent.action.MAIN");
		loginIntent.addCategory("android.intent.category.LAUNCHER");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, loginIntent);
		sendBroadcast(addIntent);
		InitApplication.mSpUtil.setIsCreated(0);

	}
	
	private void startMainService() {
//		if (!MainService.isrun) {
//			Intent it = new Intent(this, MainService.class);
//			this.startService(it);
//		}

		int isCreated = InitApplication.mSpUtil.getIsCreated();
		if (isCreated != 0) {
			creatShortCut();
		}

//		PushManager.startWork(getApplicationContext(),
//				PushConstants.LOGIN_TYPE_API_KEY,
//				Utils.getMetaValue(this, "api_key"));

	}

	@Override
	protected void setOnClickListener() {
		serach_layout.setOnClickListener(this);
		downloadedMusicBtn.setOnClickListener(this);
		recordedVideoBtn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		btn_tearch.setOnClickListener(this);
		btn_rhythm.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
//		DrawerFactory.setDefaultInstance(this);
		findViewById(R.id.use_introduction).setOnClickListener(this);
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
	protected void onPause() {
		EventBus.getDefault().unregister(this);
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
	              != PackageManager.PERMISSION_GRANTED && Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
	          //申请WRITE_EXTERNAL_STORAGE权限
	          requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA},
	                  1);
	      }
		if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
	              != PackageManager.PERMISSION_GRANTED && Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
	          requestPermissions(new String[]{android.Manifest.permission.CAMERA},
	                  2);
	      }
		EventBus.getDefault().registerSticky(this);
		if(showDanceRecommendAdapter!=null) {
			for(DownloadMusicInfo info:recommendMusicList) {
				info.checkstate();
			}
			showDanceRecommendAdapter.notifyDataSetChanged();
		}
	}
	
	
	private void checkUserInfoAndRefresh(User userInfo,boolean oncreate) {
				recommendMusicListView.setRefreshing(false);
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
				
		MusicInfo.Request request = new MusicInfo.Request();
		request.setStart(1);
		request.setStop(100);
		VolleyManager.getInstance().postRequest(
				request,
				VolleyManager.METHOD_MSUIC_SEARCH,
				mMusicResponseListener, mErrorListener);
	}
	
	private OnResponseListener<MusicInfo.Response> mMusicResponseListener = new OnResponseListener<MusicInfo.Response>(MusicInfo.Response.class) {
		
		@Override
		public void handleResponse(MusicInfo.Response response) {
			recommendMusicListView.onRefreshComplete();
			List<MusicSearchResponse> list = response.getData();
			if(list == null)
				return;
			recommendMusicList.clear();
			for(MusicSearchResponse item:list) {
				DownloadMusicInfo downmusic = new DownloadMusicInfo(item);
				recommendMusicList.add(downmusic);
				Log.d("guolei","item "+item.getName());
			}
			if(showDanceRecommendAdapter ==null) {
				showDanceRecommendAdapter = new DownloadRecommendAdapter(ShowDanceActivity.this, recommendMusicList,  InitApplication.SdCardMusicPath,
						ConstantsUtil.ACTION_SHOW_MEDIARECORDER, ConstantsUtil.ACTION_DOWNLOAD_STATE);
				recommendMusicListView.setAdapter(showDanceRecommendAdapter);//
			}
			showDanceRecommendAdapter.notifyDataSetChanged();
		}
	
	@Override
	protected void handleFailResponse(ResponseFail response) {
		if(mDialog!=null) {
			mDialog.dismiss();
			mDialog = null;
			}
		Toast.makeText(getApplicationContext(), ""+response.getMessage(),
				Toast.LENGTH_SHORT).show();
	}
};
	
	
	
		
	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回
			this.finish();
			break;

		case R.id.serach_layout: // 搜索框跳转
			mIntent.setClass(this, FoundSearchMusicActivity.class);
			startActivity(mIntent);

			break;

		case R.id.downloadedMusicBtn: // 已下载视频
			mIntent.setClass(this, DownloadedMusicActivity.class);
			startActivity(mIntent);
			break;

		case R.id.recordedVideoBtn: // 已录制视频
			mIntent.setClass(this, RecordedVideoActivity.class);
			startActivity(mIntent);
			break;

		case R.id.login_btn: // 登录
			mIntent.setClass(this, OwnerLoginActivity.class);
			startActivity(mIntent);

			break;
		case R.id.btn_tearch: // 明星老师
			mIntent.setClass(this, TeacherActivity.class);
			startActivity(mIntent);
			break;

		case R.id.btn_rhythm: // 节奏

			break;

		case R.id.btn_edit: // 制作

			break;
		case R.id.use_introduction:
			mIntent.setClass(this, UseIntroductionActivity.class);
			SharedPreferences sp = InitApplication.mSpUtil.getSp();
		            String helpUrl = sp.getString("helpurl", null);
		            if(helpUrl == null)
		            	return;
			mIntent.setData(Uri.parse(helpUrl));
			startActivity(mIntent);
			break;
		default:
			break;
		}

	}

	@Override
	protected void handleReceiver(Context context, Intent intent) {

		// 系统相机摄像
		if (intent.getAction().equals(ConstantsUtil.ACTION_SHOW_MEDIARECORDER)) {
			Toast.makeText(context, "正在启动摄像机...", Toast.LENGTH_SHORT).show();
			downMusicItem = (DownloadMusicInfo) intent
					.getSerializableExtra("musicItem");
			CameraPreviewActivity.actionRecord(this, REQUEST_EDIT, InitApplication.SdCardMusicPath.concat(downMusicItem
					.getMovieName()),
					downMusicItem.getName()+"_"+downMusicItem.getMusic().getSinger());
			return;
		}
	}
	
	public void onEventMainThread(MusicEvent event){
		
		// 下载进度
		int musicId =event.musicId;
		if (musicId == -1)
			return;
		DownloadMusicInfo item = null;
		for (DownloadMusicInfo info : recommendMusicList) {

			if (info.getMusic().getId() == musicId) {
				item = info;
				break;
			}
		}
		if (item == null)
			return;
		int position = recommendMusicList.indexOf(item);
			int state = event.state;
			item.setDownloadState(state);

			Log.d("guolei", "musicId " + musicId + " info pos =   " + position
					+ " state " + state);
			switch (state) {
			case ContentValue.DOWNLOAD_STATE_DOWNLOADING:
				long total = event.total;
				// 当前进度
				long current = event.current;
				int percentage = (int) (((int) current * 100) / (int) total);
				item.setProgress(percentage);
				break;
			case ContentValue.DOWNLOAD_STATE_SUCCESS:
				showProgressDialogSave(this, musicId);
				break;
			default:
				break;
			}
			showDanceRecommendAdapter.notifyDataSetChanged();
	}


	/**
	 * 
	 * @Description:保存下载推荐舞曲记录
	 * @param mContext
	 * @param type
	 * @return void
	 */
	public void showProgressDialogSave(Context mContext, int musicId) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
//		DownloadMusic downloadMusic = new DownloadMusic();
//		paramsMap = new HashMap<String, Object>();
//		downloadMusic.setCreateUser(id);
//		downloadMusic.setMusicId(musicId);
//		paramsMap.put("downloadMusic", downloadMusic);
//		paramsMap.put("downloadType", ConstantsUtil.showDanceHome);
//
//		Task mTask = new Task(TaskType.TS_SaveDownloadMusic, paramsMap);
//		MainService.newTask(mTask);
		MusicDownloadOKRequest request = new MusicDownloadOKRequest();
		request.setId(musicId);
		VolleyManager.getInstance().postRequest(
				request,
				VolleyManager.METHOD_DOWNLOAD_MSUIC,
				new VolleyManager.ResponeListener<Object>(null) {
					@Override
					public void onMyResponse(Object response) {
						if (mDialog != null) {
							mDialog.dismiss();
							mDialog = null;
						}
						Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
					}
				}, new VolleyManager.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (mDialog != null) {
							mDialog.dismiss();
							mDialog = null;
						}
					}
				});
		
	}

	@Override
	protected void handleErrorResponse(VolleyError error) {
		recommendMusicListView.onRefreshComplete();
	}
	
	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_danceMusicPageList: // 下载推荐舞曲
			// mDialog.dismiss();
			Log.d(TAG, "refresh TS_danceMusicPageList ");
			recommendMusicListView.onRefreshComplete();
			if (!ConstantsUtil.NetworkStatus) {
				Toast.makeText(getApplicationContext(), "请开启本机网络！",
						Toast.LENGTH_SHORT).show();
				break;
			}
			List<DownloadMusicInfo> recommendMusicListPage = (ArrayList<DownloadMusicInfo>) param[1];
			if (recommendMusicListPage == null
					|| recommendMusicListPage.size() == 0) {
				break;
			}
//			int totalPage = StringUtils.toInt(
//					headInfo.getTotalPage().toString());
//			int curPage = headInfo.getPageNo();
			recommendMusicListPage.remove(0);
//			recommendMusicList.addAll(recommendMusicListPage);
//			InitApplication.mSpUtil.setFirstRefeshShowDance(0);
			showDanceRecommendAdapter.notifyDataSetChanged();
			break;

		case TaskType.TS_SaveDownloadMusic: // 下载推荐舞曲
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				if (map != null) {
					Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
				} else {

				}

			} else {
				Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}

	}

	@Override
	protected boolean validateData() {
		return false;
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(this).setMessage("").create();
	}

	// 正在合成(暂未调用)
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (dialog instanceof AlertDialog) {
			AlertDialog alertDialog = (AlertDialog) dialog;
			alertDialog.setMessage(args.getString("message"));
		} else {
			super.onPrepareDialog(id, dialog, args);
		}
	}

	/**
	 * 
	 * @Description:正在合成视频文件进度对话框
	 * @param mContext
	 * @param total
	 * @param current
	 * @return void
	 */
	public void showSizeProgressDialog(Context mContext, double total, double current) {
		if (loadingFlag) {
			mDialog = new AlertDialog.Builder(mContext).create();
			loadingFlag = false;
			mDialog.show();
		}

		view = inflater.inflate(R.layout.custom_progressbar_dialog, null);
		MyProgressBar uploading_proressbar = (MyProgressBar) view.findViewById(R.id.uploading_proressbar);
		tvLoading = (TextView) view.findViewById(R.id.uploading_tv);
		tvLoading.setText("正在合成视频...");
		mDialog.setContentView(view);
		mDialog.setCancelable(false); // false设置点击其他地方不能取消进度条

		int fileSize = (int) total;
		int Progress = (int) current;

		uploading_proressbar.setMax(fileSize);
		uploading_proressbar.setProgress(Progress);

		if (uploading_proressbar.getProgress() == uploading_proressbar.getMax()) { // 合成完成
			loadingFlag = true;
			// 关闭正在合成...的对话框
			mDialog.cancel();

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case VIDEO_CAPTURE: {

			Uri uri = data.getData();
			Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(VideoColumns._ID));
				// Bitmap bitmap = Thumbnails.getThumbnail(getContentResolver(),
				// id, Thumbnails.MICRO_KIND, null);
				// imageView.setImageBitmap(getBitmap(filePath));
				Intent mIntent = new Intent();
				// 点击"秀舞"，跳转到视频录制页面
				mIntent.setClass(this, RecordedVideoActivity.class);
				startActivity(mIntent);
				cursor.close();
			}

			break;
		}
		case REQUEST_EDIT:
			if(data != null && data.getData()!=null)
				handleRecordVideoResult(data.getData().getPath());
			break;
		case REQUEST_RECORD: {
//			if (data != null && data.getData() != null) {
//				showDialogToast();
//
//				// 打开等待合成...的对话框
//				showWaitProgressDialog(ShowDanceActivity.this);
//
//				videoFile = new File(data.getData().getPath());
//				VideoConversionService.Companion.startConversion(this, videoFile, null, null, newLrcFile, musicFile);
//			}
			if(data != null) {
//				Intent mIntent = new Intent();
				data.setClass(this, CameraPreviewActivity.class);
//				data.putExtra("path", data.getData().getPath());
				startActivityForResult(data, REQUEST_EDIT);
			}
			break;
		}
		case REQUEST_VIDEO_REVIEW:
			Intent mIntent = new Intent();
			mIntent.setClass(this, RecordedVideoActivity.class);
			startActivity(mIntent);
			// Toast.makeText(this, "视频预览完毕", Toast.LENGTH_LONG).show();
			break;
		default: {
			super.onActivityResult(requestCode, resultCode, data);
		}
		}
	}
	
	
}