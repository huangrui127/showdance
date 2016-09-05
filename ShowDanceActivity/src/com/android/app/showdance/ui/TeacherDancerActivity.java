package com.android.app.showdance.ui;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import gl.live.danceshow.ui.camera.CameraPreviewActivity;
import gl.live.danceshow.ui.camera.VideoReviewActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.DownloadRecommendAdapter;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.MusicEvent;
import com.android.app.showdance.model.DownloadMusic;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.VDanceMusicPageVo;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.MusicDownloadOKRequest;
import com.android.app.showdance.model.glmodel.MusicInfo;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.ui.VolleyBaseActivity.OnResponseListener;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.showdance.widget.MyProgressBar;
import com.android.app.showdance.widget.PullToRefreshView;
import com.android.app.showdance.widget.PullToRefreshView.OnFooterRefreshListener;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: ShowDanceActivity
 * @Description: 秀舞界面
 * @author maminghua
 * @date 2015-5-6 下午02:48:16
 * 
 */

public class TeacherDancerActivity extends VolleyBaseActivity implements OnFooterRefreshListener {

	private PullToRefreshView mPullToRefreshView;// 自定义下拉刷新layout

	private ListView teacherMusicList;

	private DownloadRecommendAdapter showDanceRecommendAdapter;
	private List<DownloadMusicInfo> recommendMusicList = new ArrayList<DownloadMusicInfo>();

	private static final int VIDEO_CAPTURE = 2;

	private String videoFilePath = "";

	private long id;

	private DownloadMusicInfo downMusicItem;

	/** 录制最长时间 */
	public static int RECORD_TIME_MAX = 10 * 1000;

	public static final int REQUEST_RECORD = 0;

	public static final int REQUEST_VIDEO_REVIEW = 1;

	private File renameFileNewPath;// 转码完成后的文件重命名并移至新的目录

	private boolean loadingFlag = true;// 合成中进度标志

	private boolean waitingFlag = true;// 等待合成进度标志

	private Dialog mWaitDialog;// 等待合成的对话框

	private Dialog mDialogToast;// 不能退出程序的提示框

	private final int NotificationID = 0x10000;
	private NotificationManager mNotificationManager = null;
	private NotificationCompat.Builder builder;

	private int totalPage;
	private int pageNo = 1;

	private String dancer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_dancer);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void onResume() {
		EventBus.getDefault().registerSticky(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		EventBus.getDefault().unregister(this);
		super.onPause();
	}
	
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.teacher_refresh_view);

		teacherMusicList = (ListView) findViewById(R.id.teacherMusicList);

	}

	@Override
	protected void initView() {
		dancer = getIntent().getStringExtra("dancer");
		tvTitle.setText(dancer);

		return_imgbtn.setVisibility(View.VISIBLE);

		inflater = LayoutInflater.from(this);


		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();

		if (userInfo == null) {
			userInfo = new UserInfo();
		} else {
			id = userInfo.getId(); // 用户登录名
		}
		
		refreshType = ConstantsUtil.refreshOnFirst;
		showProgressDialog(this, refreshType, pageNo);

		mPullToRefreshView.setEnablePullTorefresh(false);// 是否禁用下拉刷新(默认启用true/禁用:false)
		mPullToRefreshView.setEnablePullLoadMoreDataStatus(false);// 是否禁用上拉加载更多(默认启用true/禁用:false)

	}
	@Override
	protected String[] initActions() {
		return new String[] { ConstantsUtil.ACTION_SHOW_MEDIARECORDER };
	}
	@Override
	protected void setOnClickListener() {
		mPullToRefreshView.setOnFooterRefreshListener(this);
		return_imgbtn.setOnClickListener(this);
//		DrawerFactory.setDefaultInstance(this);
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

	@Override
	protected void handleReceiver(Context context, Intent intent) {

		// 系统相机摄像
		if (intent.getAction().equals(ConstantsUtil.ACTION_SHOW_MEDIARECORDER)) {
			// Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			// startActivityForResult(mIntent, VIDEO_CAPTURE);

			Toast.makeText(context, "正在启动摄像机...", Toast.LENGTH_SHORT).show();

			downMusicItem = (DownloadMusicInfo) intent.getSerializableExtra("musicItem");

			// 当前选中mp3的音乐所在SD卡里相关目录里的路径
//			File musicFile = new File();

			// 当前选中mp3的歌词所在SD卡里相关目录里的路径
//			File lrcFile = new File(InitApplication.SdCardLrcPath.concat(downMusicItem.getName().concat(".ass")));

			// 使用随机数uid重命名歌词并复制到新的相关目录
//			newLrcFile = new File(InitApplication.SdCardNewLrcPath + UUID.randomUUID().toString() + ".ass");
//			try {
//				FileUtils.copyFile(lrcFile, newLrcFile);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}

			// 上报一下 Log 文件
//			File logFile = new File("/sdcard/j/stderr.log");
//			if (logFile.exists()) {
//				try {
//					String stderr = FileUtils.readFileToString(logFile);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}

			// 点击"秀舞"，跳转到视频录制页面
			CameraPreviewActivity.actionRecord(this, REQUEST_VIDEO_REVIEW, InitApplication.SdCardMusicPath.concat(downMusicItem.getMovieName()), downMusicItem.getName()+"_"+downMusicItem.getMusic().getSinger());
			return;
		}
	}
	
	public void onEventMainThread(MusicEvent event) {
		
		int musicId = event.musicId;
		if(musicId==-1)
			return;
		DownloadMusicInfo item = null;
		for(DownloadMusicInfo info :recommendMusicList) {
			if(info.getMusic().getId()==musicId) {
				item = info;
				break;
			}
		}
		if (item == null)
			return;
			// 当前点击位置
			// 上传成功后返回的舞曲ID
			// 当前点击位置
			// 总大小
			// 当前进度
			int state = event.state;
			item.setDownloadState(state);
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
				return;
			}
			showDanceRecommendAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:下载推荐舞曲
	 * @param mContext
	 * @param type
	 * @return void
	 */
	public void showProgressDialog(Context mContext, int refreshType, int pageNo) {

		switch (refreshType) {
		case ConstantsUtil.refreshOnFirst:
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.show();
			// 注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.loading_progressbar_dialog);
			mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
			break;

		default:
			break;
		}

		// 接口参数
//		VDanceMusicPageVo danceMusicPageVo = new VDanceMusicPageVo();
//		paramsMap = new HashMap<String, Object>();
//		danceMusicPageVo.setPageNo(pageNo);
//		danceMusicPageVo.setPageSize(1000);
//		danceMusicPageVo.setDancer(dancer);
//		paramsMap.put("danceMusicPageVo", danceMusicPageVo);
//		paramsMap.put("danceMusicType", ConstantsUtil.showDanceTeacher);
//
//		Task mTask = new Task(TaskType.TS_danceMusicPageList, paramsMap);
//		MainService.newTask(mTask);
		MusicInfo.Request request = new MusicInfo.Request(dancer,BaseRequest.STAR_TEACHER_SONG_TOKEN);
		request.setStart(1);
		request.setStop(512);
		VolleyManager.getInstance().postRequest(
				request,
				VolleyManager.METHOD_STAR_TEACHER_MUSIC,
				mMusicResponseListener, mErrorListener);
	}
	private OnResponseListener<MusicInfo.Response> mMusicResponseListener = new OnResponseListener<MusicInfo.Response>(MusicInfo.Response.class) {
		@Override
		public void handleResponse(MusicInfo.Response response) {
			List<MusicInfo.MusicSearchResponse> list = response.getData();
			if(list == null)
				return;
			recommendMusicList.clear();
			for(MusicInfo.MusicSearchResponse item:list) {
				DownloadMusicInfo downmusic = new DownloadMusicInfo(item);
				recommendMusicList.add(downmusic);
			}
			if(showDanceRecommendAdapter ==null) {
				showDanceRecommendAdapter = new DownloadRecommendAdapter(TeacherDancerActivity.this, recommendMusicList, InitApplication.SdCardMusicPath,
						ConstantsUtil.ACTION_SHOW_MEDIARECORDER, ConstantsUtil.ACTION_DOWNLOAD_STATE);
				teacherMusicList.setAdapter(showDanceRecommendAdapter);//
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
	protected void handleErrorResponse(VolleyError error) {
		Log.e("guolei","handleErrorResponse "+ error.toString());
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
//		paramsMap.put("downloadType", ConstantsUtil.showDanceTeacher);
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
	public void refresh(Object... param) {

		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_danceMusicPageList: // 下载推荐舞曲
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				List<DownloadMusicInfo> recommendMusicListPage = (ArrayList<DownloadMusicInfo>) param[1];

				if (recommendMusicListPage != null && recommendMusicListPage.size() != 0) {
					recommendMusicListPage.remove(0);

					recommendMusicList.addAll(recommendMusicListPage);

					if (refreshType == ConstantsUtil.refreshOnFirst) {
						showDanceRecommendAdapter = new DownloadRecommendAdapter(TeacherDancerActivity.this, recommendMusicList, InitApplication.SdCardMusicPath,
								ConstantsUtil.ACTION_TEACHER_MEDIARECORDER, ConstantsUtil.ACTION_DOWNLOAD_STATE);
						teacherMusicList.setAdapter(showDanceRecommendAdapter);//

					} else if (refreshType == ConstantsUtil.refreshOnHeader) {// 下拉刷新
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentTime = dateFormat.format(System.currentTimeMillis());
						// 设置更新时间
						mPullToRefreshView.onHeaderRefreshComplete("最近更新:" + currentTime);
						mPullToRefreshView.onHeaderRefreshComplete();
						showDanceRecommendAdapter.notifyDataSetChanged();
					} else if (refreshType == ConstantsUtil.refreshOnFooter) {// 上拉加载更多
						mPullToRefreshView.onFooterRefreshComplete();
						showDanceRecommendAdapter.notifyDataSetChanged();
					}

				} else {

				}

			} else {
				Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
			}

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

//	public void onEventMainThread(VideoConversionService.VideoConversionBeginEvent event) {
//		try {
//			// showDialog(0);
//		} catch (Exception ignored) {
//		}
//	}
//
//	// 正在合成
//	public void onEventMainThread(VideoConversionService.VideoConversionProgressEvent event) {
//		Bundle bundle = new Bundle();
//		bundle.putString("message", "转换中: " + event.getMessage());
//
//		// AlertDialog alertDialog = (AlertDialog) dialog;
//		String message = event.getMessage();
//		// alertDialog.setMessage(message);
//
//		// 根据位置取出dup值
//		int d_pos;
//		d_pos = message.indexOf("dup");
//		if (d_pos > 0) {
//			if (mWaitDialog != null) {
//				if (mWaitDialog.isShowing()) {
//					waitingFlag = false;// 设置为false 是防止视频合成完成后再次打开等待合成的进度条
//					mWaitDialog.dismiss();
//				}
//			}
//		}
//
//		String dup_temp = new String();
//		dup_temp = message.substring(d_pos, message.length());
//
//		d_pos = dup_temp.indexOf("=");
//		dup_temp = dup_temp.substring(d_pos + 1, dup_temp.length());
//
//		// 根据位置取出size值大小
//		int s_pos;
//		s_pos = message.indexOf("size");
//		String size_temp = new String();
//		size_temp = message.substring(s_pos, message.length());
//
//		s_pos = size_temp.indexOf("kB");
//		size_temp = size_temp.substring(0, s_pos);
//		s_pos = size_temp.indexOf("=");
//		size_temp = size_temp.substring(s_pos + 1, size_temp.length());
//
//		double videoFileSize = FileUtil.getFileOrFilesSize(videoFile.getPath(), 2);// 原始视频大小
//		double musicFileSize = FileUtil.getFileOrFilesSize(musicFile.getPath(), 2);// 原始音频大小
//		double subFileSize = FileUtil.getFileOrFilesSize(newLrcFile.getPath(), 2);// 原始歌词大小
//
//		double totalSize = videoFileSize + musicFileSize + subFileSize;
//
//		if (dup_temp.length() > 0) {
//			// 打开正在合成进度条
//			showSizeProgressDialog(TeacherDancerActivity.this, totalSize, StringUtils.StringTodouble(size_temp));// 当前界面进度
//			// compoundProgress(musicFile.getPath(), totalSize,
//			// StringUtils.StringTodouble(size_temp));// 通知栏进度
//		}
//
//		// showDialog(0, bundle);
//	}

	/**
	 * 
	 * @Description:等待合成进度对话框
	 * @param mContext
	 * @return void
	 */
	public void showWaitProgressDialog(Context mContext) {
		if (waitingFlag) {
			// 打开正在响应...的对话框
			waitingFlag = false;
			mWaitDialog = new AlertDialog.Builder(mContext).create();
			mWaitDialog.show();

			// "正在加载"对话框,提示信息TextView的设置
			view = inflater.inflate(R.layout.loading_progressbar_dialog, null);
			tvLoading = (TextView) view.findViewById(R.id.loading_tv);
			tvLoading.setText("等待合成...");
			mWaitDialog.setContentView(view);
			mWaitDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
		}

	}

	/**
	 * 
	 * @Description:显示不能退出的对话框
	 * @param
	 * @return void
	 */
	protected void showDialogToast() {

		View view = getLayoutInflater().inflate(R.layout.activity_toask_view, null);
		mDialogToast = new Dialog(this, R.style.transparentFrameWindowStyle);
		mDialogToast.setContentView(view);
		Window window = mDialogToast.getWindow();

		// 屏幕高度和宽度
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();

		// 设置位置在底部
		window.setGravity(Gravity.BOTTOM);
		// 边距
		wl.x = screenWidth;
		// 距离底部高度
		wl.y = (int) (screenHeight * 0.085);
		// 将对话框的大小按屏幕大小的百分比设置
		wl.height = (int) (screenHeight * 0.25); // 高度设置为屏幕的0.3
		wl.width = screenWidth; // 宽度设置为屏幕全屏
		// 设置显示位置
		mDialogToast.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		mDialogToast.setCanceledOnTouchOutside(true);
		mDialogToast.show();

	}

	// 合成完成
//	public void onEventMainThread(VideoConversionService.VideoConversionFinishEvent event) {
//		// try {
//		// dismissDialog(0);
//		// } catch (Exception ignored) {
//		//
//		// }
//
//		if (mDialog != null) { // 合成完成
//			loadingFlag = true;
//			// 关闭正在合成...的对话框
//			mDialog.cancel();
//
//		}
//
//		mDialogToast.cancel();
//
//		String jointOutFilePath = event.getOutputFile().getPath();
//		String jointFilePath = event.getFilePath().getPath();
//
//		final File jointOutFile = new File(jointOutFilePath);// 转换完成后输出视频目录
//		final File jointFile = new File(jointFilePath);// 转换之前原始视频目录
//
//		if (jointOutFile.exists() && jointOutFile.length() > 0) {
//
//			jointFile.delete();
//
//			// mNotificationManager.notify(NotificationID, builder.build());
//			// // 震动提示
//			// Vibrator vibrator = (Vibrator)
//			// getSystemService(Context.VIBRATOR_SERVICE);
//			// vibrator.vibrate(1000L);// 参数是震动时间(long类型)
//			// mNotificationManager.cancel(NotificationID);
//
//			getmp4PathFromSD(downMusicItem.getName());
//
//			// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
//			CustomAlertDialog mCustomDialog = new CustomAlertDialog(TeacherDancerActivity.this).builder(R.style.DialogTVAnimWindowAnim);
//			mCustomDialog.setTitle("转换完成");
//			mCustomDialog.setMsg("点击确定可预览视频");
//			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					VideoReviewActivity.actionReview(TeacherDancerActivity.this, REQUEST_VIDEO_REVIEW, Uri.fromFile(renameFileNewPath));// jointOutFile
//				}
//			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			}).show();
//
//		} else {
//
//			// 自定义无标题、有确定按钮与无取消按钮对话框使用方法
//			CustomAlertDialog mCustomDialog = new CustomAlertDialog(TeacherDancerActivity.this).builder(R.style.DialogTVAnimWindowAnim);
//			mCustomDialog.setMsg("文件为空,请重新录制");
//			mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			}).show();
//
//		}
//	}

	// 从sd卡获取mp4资源
	public File getmp4PathFromSD(String newVideoName) {
		// 得到该路径文件夹下所有的文件
		File mfile = new File(getfilePath());
		File[] files = mfile.listFiles();

		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

		// 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (FileUtil.checkIsMp4File(file.getPath())) {
				renameFileNewPath = new File(InitApplication.SdCardRecordedVideoPath + newVideoName.replace(".mp3", "").concat("_").concat(timeStamp).concat(".mp4"));
				file.renameTo(renameFileNewPath);
			}
		}
		return renameFileNewPath.getAbsoluteFile();
	}

	// 获取输出临时MP4文件手机路径
	private String getfilePath() {
		File file = new File(Environment.getExternalStorageDirectory(), "/j/");
		return file.getAbsolutePath();
	}

//	public void onEventMainThread(VideoConversionService.VideoConversionErrorEvent event) {
//		try {
//			dismissDialog(0);
//		} catch (Exception ignored) {
//		}
//
//		String exceptionMessage = event.getException().getMessage();
//		if (exceptionMessage != null) {
//			Toast.makeText(TeacherDancerActivity.this, "转换失败: " + exceptionMessage, Toast.LENGTH_LONG).show();
//		}
//	}

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
	 * @Description:通知栏正在合成视频文件进度条
	 * @param filePath
	 * @param total
	 * @param current
	 * @return void
	 */

	/**
	 * 
	 * @param x
	 *            当前值
	 * @param total
	 *            总值
	 * @return 当前百分比
	 * @Description:返回百分之值
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case VIDEO_CAPTURE: {

			Uri uri = data.getData();
			Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(VideoColumns._ID));
				videoFilePath = cursor.getString(cursor.getColumnIndex(VideoColumns.DATA));
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

		case REQUEST_RECORD: {
//			if (data != null && data.getData() != null) {
//				Intent mIntent = new Intent();
//				mIntent.setClass(this, PreSummeryEditorActivity.class);
//				mIntent.putExtra("path", data.getData().getPath());
//				startActivityForResult(mIntent, REQUEST_VIDEO_REVIEW);
//			}
			if(data != null) {
//				Intent mIntent = new Intent();
				data.setClass(this, CameraPreviewActivity.class);
//				data.putExtra("path", data.getData().getPath());
				startActivityForResult(data, REQUEST_VIDEO_REVIEW);
			}
			break;
		}
		case REQUEST_VIDEO_REVIEW:
			if (data != null && data.getData() != null) {
				handleRecordVideoResult(data.getData().getPath());
			}
			break;
		default: {
			super.onActivityResult(requestCode, resultCode, data);
		}
		}
	}

	// 视频绘制自定义片头和片尾
//	@Override
//	public void drawCaption(Resources resources, Bitmap bitmap) {
//		Canvas canvas = new Canvas(bitmap);
//		int w = bitmap.getWidth();
//		int h = bitmap.getHeight();
//		Paint paint = new Paint();
//		paint.setAntiAlias(true);
//		// paint.setColor(Color.BLACK);
//		// paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//		// 24f, resources.getDisplayMetrics()));
//		// canvas.drawText("椰米广场舞", w / 4, h / 4, paint);
//		paint.setColor(Color.WHITE);
//		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.getDisplayMetrics()));
//		canvas.drawText("椰米广场舞", w / 8, h / 4, paint);
//
//	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onFooterRefresh(PullToRefreshView view) {

		refreshType = ConstantsUtil.refreshOnFooter;
		if (pageNo != totalPage) {
			pageNo = pageNo + 1;
			onRefresh(this, refreshType, pageNo);
		} else {
			mPullToRefreshView.onFooterRefreshComplete();
			Toast.makeText(TeacherDancerActivity.this, "已经是最后一页数据!", Toast.LENGTH_LONG).show();
		}

	}

	// 下拉&&上拉刷新
	private void onRefresh(Context mContext, int refreshType, int pageIndex) {
		// refreshType:1：下拉刷新 2：上拉加载更多
		showProgressDialog(this, refreshType, pageIndex);
	}
}
