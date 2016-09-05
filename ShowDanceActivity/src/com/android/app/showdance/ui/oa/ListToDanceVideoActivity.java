package com.android.app.showdance.ui.oa;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.DanceVideoAdapter;
import com.android.app.showdance.adapter.MyDownloadVideoAdapter;
import com.android.app.showdance.adapter.RecordedVideoAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.DownloadMediaPageVo;
import com.android.app.showdance.model.MediaInfo;
import com.android.app.showdance.model.MediaInfoPageVo;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.model.glmodel.MusicInfo;
import com.android.app.showdance.model.glmodel.VideoUploadInfo;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowResponse;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.VideoDetailsActivity;
import com.android.app.showdance.ui.VolleyBaseActivity;
import com.android.app.showdance.ui.baidu.bvideo.VideoViewPlayingActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.widget.CustomAlertDialog;

/**
 * 我的视频（上传的视频）、我下载的视频
 */
public class ListToDanceVideoActivity extends VolleyBaseActivity implements OnItemClickListener {
	public static final int FALG_MyDownAv = 1;// 我下载的视频
	public static final int FALG_MyAv = 2;// 我的视频（上传的视频）

	private ListView downloadedVideoVideoList;
	private GridView downloadedVideo_GridView;
	private MediaMetadataRetriever mrRetriever;
	private LinearLayout no_login_layout;

//	private long createUser;
	private int pageNo = 1;
	private int pageSize = 10;
	long userId;

	private DanceVideoAdapter danceVideoAdapter;
	private List<Map<String, Object>> list;

	private List<MediaInfo> deleteMediaInfoSelList;

	private RecordedVideoAdapter mRecordedVideoAdapter;

	private List<UploadVideoInfo> recordedVideoListInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dance_video);
		mrRetriever = new MediaMetadataRetriever();
		findViewById();
		initView();
		setOnClickListener();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mrRetriever.release();
	}
	
	/**
	 * 查找界面各控件
	 */
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		save_tv = (TextView) findViewById(R.id.save_tv);
		downloadedVideo_GridView = (GridView) findViewById(R.id.downloadedVideo_GridView);
		downloadedVideoVideoList = (ListView) findViewById(R.id.downloadedVideoVideoList);
		no_login_layout = (LinearLayout) findViewById(R.id.no_login_layout);
	}

	/*
	 * 附默认值
	 */
	@Override
	protected void initView() {
		return_imgbtn.setVisibility(View.VISIBLE);


		deleteMediaInfoSelList = new ArrayList<MediaInfo>();

//		createUser = getIntent().getLongExtra("createUser", 0);
		int flag = getIntent().getIntExtra("flag", FALG_MyAv);
		switch (flag) {
		case FALG_MyDownAv:// 我下载的视频
			tvTitle.setText("我的下载");
			// WS_getMyDownAv(this, createUser);
			downloadedVideoVideoList.setVisibility(View.VISIBLE);
			downloadedVideo_GridView.setVisibility(View.GONE);
			new ScanVideoTask().execute();

			break;

		case FALG_MyAv:// 我的视频（上传的视频）

			save_tv.setVisibility(View.INVISIBLE);
//			save_tv.setText("删除");

			tvTitle.setText("我的视频");
			WS_getMyVedio();
			break;
		}

	}

	@Override
	protected String[] initActions() {
		return new String[] { ConstantsUtil.ACTION_DEL_RECORDED };
	}
	
	/**
	 * 设置事件
	 */
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		downloadedVideo_GridView.setOnItemClickListener(this);
		downloadedVideoVideoList.setOnItemClickListener(this);
		save_tv.setOnClickListener(this);
	}

	/** 扫描SD卡 */
	private class ScanVideoTask extends AsyncTask<Void, File, List<UploadVideoInfo>> {
		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ListToDanceVideoActivity.this);
			pd.setMessage("正在扫描已下载视频...");
			pd.show();
		}

		@Override
		protected List<UploadVideoInfo> doInBackground(Void... params) {

			return getmp4PathFromSD();
		}

		@Override
		protected void onProgressUpdate(final File... values) {
			pd.setMessage(values[0].getName());
		}

		/** 遍历所有文件夹，查找出视频文件 */
		// 从sd卡获取mp4资源
		public List<UploadVideoInfo> getmp4PathFromSD() {
			// mp4列表
			List<UploadVideoInfo> uploadVideoInfoList = new ArrayList<UploadVideoInfo>();
			// 得到该路径文件夹下所有的文件
			File mfile = new File(InitApplication.SdCardDownloadVideoPath);
			File[] files = mfile.listFiles();
			// 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					UploadVideoInfo uploadVideoItem = new UploadVideoInfo();
					uploadVideoItem.setFileName(file.getName());
					uploadVideoItem.setFilePath(file.getPath());
					uploadVideoItem.setFileSize(StringUtils.doubleToString(FileUtil.getFileOrFilesSize(file.getPath(), 3)));
					if (FileUtil.checkIsMp4File(file.getPath())) {
						uploadVideoInfoList.add(uploadVideoItem);

					}
				}
			}

			// 返回得到的mp4列表
			return uploadVideoInfoList;
		}

		@Override
		protected void onPostExecute(List<UploadVideoInfo> result) {
			super.onPostExecute(result);

			recordedVideoListInfo = result;

			if (recordedVideoListInfo != null && recordedVideoListInfo.size() != 0) {
				no_login_layout.setVisibility(View.GONE);
				mRecordedVideoAdapter = new RecordedVideoAdapter(ListToDanceVideoActivity.this, recordedVideoListInfo, downloadedVideoVideoList, 1,mrRetriever);
				downloadedVideoVideoList.setAdapter(mRecordedVideoAdapter);
			} else {
				no_login_layout.setVisibility(View.VISIBLE);
			}

			pd.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回键
			finish();
			break;
		case R.id.save_tv:// 删除
//if(danceVideoAdapter == null)
//	return;
//			int count = danceVideoAdapter.getCount();
//			for (int i = 0; i < count; i++) {
//				if (danceVideoAdapter.isCheckMap.get(i)) {
//					String id = list.get(i).get("mediaId").toString();
//					String mediaNewName = list.get(i).get("mediaNewName").toString();
//					MediaInfo deleteMediaInfoList = new MediaInfo();
//					deleteMediaInfoList.setId(StringUtils.toLong(id));
//					deleteMediaInfoList.setMediaNewName(mediaNewName);
//					deleteMediaInfoSelList.add(deleteMediaInfoList);
//				}
//			}
//			if (deleteMediaInfoSelList.size() != 0) {
//				WS_deleteMediaInfo(this, deleteMediaInfoSelList);
//			} else {
//				Toast.makeText(getApplicationContext(), "请选择要删除的视频", Toast.LENGTH_SHORT).show();
//			}
			break;
			default:
				return;
		}

	}
	  private void scanVideoFile(final String filepath) {
	    	final MediaScannerConnection connect = new MediaScannerConnection(getApplicationContext(),null) {
	    		@Override
	    		public void onServiceConnected(ComponentName className,
	    				IBinder service) {
	    			super.onServiceConnected(className, service);
	    			scanFile(filepath, "video/avc");
	    		}
	    	};
	    	connect.connect();
	    }
	@Override
	protected void handleReceiver(Context context, Intent intent) {
		// 删除视频
		if (intent.getAction().equals(ConstantsUtil.ACTION_DEL_RECORDED)) {
			final int position = intent.getIntExtra("position", 0);
			final String fileName = recordedVideoListInfo.get(position).getFileName();
			final String filePath = recordedVideoListInfo.get(position).getFilePath();

			// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
			CustomAlertDialog mCustomDialog = new CustomAlertDialog(ListToDanceVideoActivity.this).builder(R.style.DialogTVAnimWindowAnim);
			mCustomDialog.setTitle("删除提示");
			mCustomDialog.setMsg("确认删除" + fileName + "吗?");
			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
				@Override
				public void onClick(View v) {
					File delFilePath = new File(filePath);
					delFilePath.delete();
					recordedVideoListInfo.remove(position);
					scanVideoFile(filePath);
					mRecordedVideoAdapter.notifyDataSetChanged();
				}
			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();

		}

	}

	@Override
	public void refresh(Object... param) {

		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_GETMYVIDIO: // 我的视频
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				list = (ArrayList<Map<String, Object>>) param[1];

				if (list != null && list.size() != 0) {
					String pageNo = list.get(0).get("pageNo").toString();
					String pageSize = list.get(0).get("pageSize").toString();
					String autoCount = list.get(0).get("autoCount").toString();
					String totalCount = list.get(0).get("totalCount").toString();
					String totalPage = list.get(0).get("totalPage").toString();
					String firstPage = list.get(0).get("firstPage").toString();
					String lastPage = list.get(0).get("lastPage").toString();
					list.remove(0);

					danceVideoAdapter = new DanceVideoAdapter(this, list, 0); // 创建适配器
					downloadedVideo_GridView.setAdapter(danceVideoAdapter);

				} else {
					makeToast(this, R.string.no_my_video);
				}
			} else {
				makeToast(this, R.string.app_network);
			}
			break;

		case TaskType.TS_DELETEMEDIAINFO: // 我的视频
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				if (map != null) {
					String result = map.get("result").toString();
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "删除成功!", Toast.LENGTH_SHORT).show();
						WS_getMyVedio();
					}
				} else {

				}
			} else {
				makeToast(this, R.string.app_network);
			}
			break;

		case TaskType.TS_MYDOWNLOADMV: // 我的下载
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				list = (ArrayList<Map<String, Object>>) param[1];

				if (list != null && list.size() != 0) {
					String pageNo = list.get(0).get("pageNo").toString();
					String pageSize = list.get(0).get("pageSize").toString();
					String autoCount = list.get(0).get("autoCount").toString();
					String totalCount = list.get(0).get("totalCount").toString();
					String totalPage = list.get(0).get("totalPage").toString();
					String firstPage = list.get(0).get("firstPage").toString();
					String lastPage = list.get(0).get("lastPage").toString();
					list.remove(0);

					MyDownloadVideoAdapter myDownloadVideoAdapter = new MyDownloadVideoAdapter(this, list); // 创建适配器
					downloadedVideo_GridView.setAdapter(myDownloadVideoAdapter);

				} else {
					makeToast(this, R.string.no_my_downVideo);
				}
			} else {
				makeToast(this, R.string.app_network);
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

	// GridView监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent mIntent = new Intent();

		switch (parent.getId()) {
		case R.id.downloadedVideo_GridView:
			int flag = getIntent().getFlags();
			switch (flag) {
			case FALG_MyDownAv:// 我下载的视频
				userId = Long.parseLong(list.get(position).get("userId").toString());
				break;
			case FALG_MyAv:// 我的视频（上传的视频）
				break;
			}
			String mediaId = list.get(position).get("mediaId").toString();
			mIntent.setClass(this, VideoDetailsActivity.class);
			mIntent.putExtra("mediaNewName", list.get(position).get("mediaNewName").toString());
			mIntent.putExtra("userId", userId);
//			mIntent.putExtra("createUser", String.valueOf(createUser));
			mIntent.putExtra("mediaId", mediaId);
			mIntent.putExtra("snapshot", list.get(position).get("snapshot").toString());
			mIntent.putExtra("remark", list.get(position).get("remark").toString());
			startActivity(mIntent);

			break;

		case R.id.downloadedVideoVideoList:
			mIntent.setClass(this, VideoViewPlayingActivity.class);
			mIntent.setData(Uri.parse(recordedVideoListInfo.get(position).getFilePath()));
			startActivity(mIntent);

			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * 调用接口 "我的视频"（）
	 */
	public void WS_getMyVedio() {
		mDialog = new AlertDialog.Builder(this).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
//		MediaInfoPageVo mediaInfoPageVo = new MediaInfoPageVo();
//		paramsMap = new HashMap<String, Object>();
////		mediaInfoPageVo.setCreateUser(createUser);
//		mediaInfoPageVo.setPageNo(pageNo);
//		mediaInfoPageVo.setPageSize(ConstantsUtil.PageSize);
//		paramsMap.put("mediaInfoPageVo", mediaInfoPageVo);
//
//		paramsMap.put("videoType", ConstantsUtil.videoType1);
//		Task mTask = new Task(TaskType.TS_GETMYVIDIO, paramsMap);
//		MainService.newTask(mTask);
		
		
		VideoUploadInfo.ShowRequest request = new VideoUploadInfo.ShowRequest();
		request.setpageNumber(Integer.MAX_VALUE);
		request.setuser_id(InitApplication.mSpUtil.getUser().getId());
		VolleyManager.getInstance().postRequest(
				request,
				VolleyManager.CLIENT_ID+"/"+InitApplication.mSpUtil.getUser().getuser_token()+"/videoList",
				new OnResponseListener<VideoUploadInfo.ShowResponse>(VideoUploadInfo.ShowResponse.class) {
					@Override
					protected void handleResponse(ShowResponse response) {
						Log.d("guolei","response " +response.toString());
					}
				}, mErrorListener);
		
	}

	
	
	/**
	 * 
	 * 调用接口 "我的下载"
	 */
	public void WS_getMyDownAv(Context mContext, long createUser) {
		mDialog = new AlertDialog.Builder(mContext).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
		DownloadMediaPageVo downloadMediaPageVo = new DownloadMediaPageVo();
		paramsMap = new HashMap<String, Object>();
		downloadMediaPageVo.setCreateUser(createUser);
		downloadMediaPageVo.setPageNo(pageNo);
		downloadMediaPageVo.setPageSize(ConstantsUtil.PageSize);
		paramsMap.put("downloadMediaPageVo", downloadMediaPageVo);

		Task mTask = new Task(TaskType.TS_MYDOWNLOADMV, paramsMap);
		MainService.newTask(mTask);

	}

	/**
	 * 
	 * 调用接口 "删除我的视频"
	 */
	public void WS_deleteMediaInfo(Context mContext, List<MediaInfo> deleteMediaInfoSelList) {
		mDialog = new AlertDialog.Builder(mContext).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
		paramsMap = new HashMap<String, Object>();
		paramsMap.put("deleteMediaInfo", deleteMediaInfoSelList);

		Task mTask = new Task(TaskType.TS_DELETEMEDIAINFO, paramsMap);
		MainService.newTask(mTask);
	}

}
