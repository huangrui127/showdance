package com.android.app.showdance.ui;

import gl.live.danceshow.ui.camera.CameraPreviewActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.showdance.adapter.DownloadedMusicAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.DownloadMusicPageVo;
import com.android.app.showdance.model.DownloadedMusic;
import com.android.app.showdance.model.SerializableObj;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.ui.baidu.bvideo.VideoViewPlayingActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.showdance.widget.MyProgressBar;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

/**
 * 
 * @ClassName: DownloadedVideoActivity
 * @Description: 已下载舞曲
 * @author maminghua
 * @date 2015-5-12 下午06:11:59
 * 
 */
public class DownloadedMusicActivity extends BaseActivity implements OnItemClickListener {

	private ListView downloadedMusicList;

	private LinearLayout no_login_layout;

	private List<Map<String, Object>> mDownloadedMusicList;

	private DownloadedMusicAdapter mDownloadedMusicAdapter;

	private static final int VIDEO_CAPTURE = 2;

	private long id;

	private int totalPage;// 总页数

	private int pageNo = 1;// 默认第1页

	private String videoFilePath = "";


	public static final int REQUEST_RECORD = 0;

	public static final int REQUEST_VIDEO_REVIEW = 1;

	private File renameFileNewPath;

	private boolean loadingFlag = true;// 合成中进度标志

	private boolean waitingFlag = true;// 等待合成进度标志




	private Dialog mWaitDialog;// 等待合成的对话框

	private Dialog mDialogToast;// 不能退出程序的提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloadedmusic);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		downloadedMusicList = (ListView) findViewById(R.id.downloadedMusicList);
		no_login_layout = (LinearLayout) findViewById(R.id.no_login_layout);
	}

	@Override
	protected void initView() {
		tvTitle.setText("已下载舞曲");
		return_imgbtn.setVisibility(View.VISIBLE);

		inflater = LayoutInflater.from(this);

		mDownloadedMusicList = new ArrayList<Map<String, Object>>();


		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();

		// if (userInfo == null) {
		// userInfo = new UserInfo();
		// mPullToRefreshView.setVisibility(View.GONE);
		// no_login_layout.setVisibility(View.VISIBLE);
		// } else {
		// mPullToRefreshView.setVisibility(View.VISIBLE);
		// no_login_layout.setVisibility(View.GONE);
		// id = userInfo.getId(); //
		// // 用户登录名
		// showProgressDialog(this, 0, pageNo);
		// }

		new ScanVideoTask().execute();

	}
	@Override
	protected String[] initActions() {
		return new String[] { ConstantsUtil.ACTION_DOWN_MEDIARECORDER };
	}
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		downloadedMusicList.setOnItemClickListener(this);
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

	/** 扫描SD卡 */
	private class ScanVideoTask extends AsyncTask<Void, File, List<Map<String, Object>>> {
		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(DownloadedMusicActivity.this);
			pd.setCancelable(false);
			pd.setMessage("正在扫描已下载舞曲...");
			pd.show();
		}

		@Override
		protected List<Map<String, Object>> doInBackground(Void... params) {

			return getmp4PathFromSD();
		}

		@Override
		protected void onProgressUpdate(final File... values) {
			pd.setMessage(values[0].getName());
		}

		/** 遍历所有文件夹，查找出视频文件 */
		// 从sd卡获取mp3资源
		public List<Map<String, Object>> getmp4PathFromSD() {
			// mp4列表
			List<Map<String, Object>> showMp3InfoList = new ArrayList<Map<String, Object>>();
			// 得到该路径文件夹下所有的文件
			File mfile = new File(InitApplication.SdCardMusicPath);
			File[] files = mfile.listFiles();
			// 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
			if (files == null) {
				return null;
			}
			SharedPreferences sp = InitApplication.mSpUtil.getMusicSp();
				for (File file :files) {
					if(!FileUtil.isVideoOrAudio(file.getName())) {
						continue;
					}
					String mp3 = sp.getString(file.getName(), null);
					Map<String, Object> mp3Map = new HashMap<String, Object>();
					
					if(mp3==null) {
						mp3Map.put("name", file.getName());
						mp3Map.put("path", file.getPath());
					} else {
					mp3Map.put("name", mp3.substring(mp3.indexOf("_")+1));
					mp3Map.put("path", file.getPath());
					}
					showMp3InfoList.add(mp3Map);
				}

			// 返回得到的mp4列表
			return showMp3InfoList;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			super.onPostExecute(result);

			mDownloadedMusicList = result;

			if (mDownloadedMusicList != null && mDownloadedMusicList.size() != 0) {
				downloadedMusicList.setVisibility(View.VISIBLE);
				no_login_layout.setVisibility(View.GONE);
				mDownloadedMusicAdapter = new DownloadedMusicAdapter(DownloadedMusicActivity.this, mDownloadedMusicList);
				downloadedMusicList.setAdapter(mDownloadedMusicAdapter);
			} else {
				downloadedMusicList.setVisibility(View.GONE);
				no_login_layout.setVisibility(View.VISIBLE);
			}
			pd.dismiss();
		}
	}

	/**
	 * 
	 * @Description:已下载舞曲
	 * @param mContext
	 * @param id
	 * @param pageNo
	 * @param refreshType
	 *            刷新类型 （0：首次刷新,1：列表下拉刷新,2：列表上拉加载更多
	 * @return void
	 */
	public void showProgressDialog(Context mContext, int refreshType, int pageNo) {

		switch (refreshType) {
		case 0:
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
		DownloadMusicPageVo downloadMusicPageVo = new DownloadMusicPageVo();
		paramsMap = new HashMap<String, Object>();
		downloadMusicPageVo.setCreateUser(id);
		downloadMusicPageVo.setPageNo(pageNo);
		// downloadMusicPageVo.setPageSize(ConstantsUtil.PageSize);
		downloadMusicPageVo.setPageSize(20);
		paramsMap.put("downloadMusicPageVo", downloadMusicPageVo);
		paramsMap.put("refreshType", refreshType);

		Task mTask = new Task(TaskType.TS_MusicInfoPageList, paramsMap);
		MainService.newTask(mTask);
	}

	@Override
	protected void handleReceiver(Context context, Intent intent) {

		// 系统相机摄像
		if (intent.getAction().equals(ConstantsUtil.ACTION_DOWN_MEDIARECORDER)) {

			Toast.makeText(getApplicationContext(), "正在启动摄像机...", Toast.LENGTH_SHORT).show();

			SerializableObj serItem = (SerializableObj) intent.getSerializableExtra("musicItem");
			Map<String, Object> musicItem = serItem.getMusicItem();

			// 当前选中mp3的音乐所在SD卡里相关目录里的路径

			// 当前选中mp3的歌词所在SD卡里相关目录里的路径

				CameraPreviewActivity.actionRecord(this, REQUEST_RECORD, musicItem.get("path").toString(), musicItem.get("name").toString());
//			} else {
//				Toast.makeText(this, "音乐文件未找到", Toast.LENGTH_LONG).show();
//			}
		}

	}

	/**
	 * 保证路径以斜杠结尾
	 */
	public static String fillPath(String path) {
		if (path.endsWith(File.separator)) {
			return path;
		} else {
			return path + File.separator;
		}
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
//
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
//			showSizeProgressDialog(DownloadedMusicActivity.this, totalSize, StringUtils.StringTodouble(size_temp));
//		}
//
//		// showDialog(0, bundle);
//	}
//
//	// 合成完成
//	public void onEventMainThread(VideoConversionService.VideoConversionFinishEvent event) {
//		// try {
//		// dismissDialog(0);
//		// } catch (Exception ignored) {
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
//			getmp4PathFromSD(musicItem.get("name").toString());
//
//			// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
//			CustomAlertDialog mCustomDialog = new CustomAlertDialog(DownloadedMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
//			mCustomDialog.setTitle("转换完成");
//			mCustomDialog.setMsg("点击确定可预览视频");
//			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					VideoReviewActivity.actionReview(DownloadedMusicActivity.this, REQUEST_VIDEO_REVIEW, Uri.fromFile(renameFileNewPath));// jointOutFile
//				}
//			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			}).show();
//
//		} else {
//			// 自定义无标题、有确定按钮与无取消按钮对话框使用方法
//			CustomAlertDialog mCustomDialog = new CustomAlertDialog(DownloadedMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
//			mCustomDialog.setMsg("文件为空,请重新录制");
//			mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			}).show();
//		}
//	}
//
//	public void onEventMainThread(VideoConversionService.VideoConversionErrorEvent event) {
//		try {
//			dismissDialog(0);
//		} catch (Exception ignored) {
//		}
//
//		String exceptionMessage = event.getException().getMessage();
//		if (exceptionMessage != null) {
//			Toast.makeText(DownloadedMusicActivity.this, "转换失败: " + exceptionMessage, Toast.LENGTH_LONG).show();
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
		wl.height = (int) (screenHeight * 0.25); // 高度设置为屏幕的0.25
		wl.width = screenWidth; // 宽度设置为屏幕全屏
		// 设置显示位置
		mDialogToast.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		mDialogToast.setCanceledOnTouchOutside(true);
		mDialogToast.show();

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
			waitingFlag = true;// 设置为true 方便二次打开等待合成的进度条
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

	// 从sd卡获取mp4资源
	public File getmp4PathFromSD(String newVideoName) {
		// 得到该路径文件夹下所有的文件
		File mfile = new File(InitApplication.SdCardLrcInfoPath);
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

	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_MusicInfoPageList: // 已下载舞曲
			int refreshType = (Integer) param[2];
			if (refreshType == 0) {
				mDialog.dismiss();
			}
			if (ConstantsUtil.NetworkStatus) {
				List<Map<String, Object>> mMusicList = (ArrayList<Map<String, Object>>) param[1];

				if (mMusicList != null && mMusicList.size() != 0) {
					totalPage = StringUtils.stringTolnt(mMusicList.get(0).get("totalPage").toString());
					mMusicList.remove(0);

					mDownloadedMusicList.clear();
					mDownloadedMusicList.addAll(mMusicList);

					if (refreshType == 0) {
						mDownloadedMusicAdapter = new DownloadedMusicAdapter(DownloadedMusicActivity.this, mDownloadedMusicList);
						downloadedMusicList.setAdapter(mDownloadedMusicAdapter);

					} else if (refreshType == 1) {// 下拉刷新
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentTime = dateFormat.format(System.currentTimeMillis());

						mDownloadedMusicAdapter.notifyDataSetChanged();

					} else if (refreshType == 2) {// 上拉加载更多
						mDownloadedMusicAdapter.notifyDataSetChanged();
					}

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

	public static ArrayList<DownloadedMusic> getMusicDatas(Context context) {
		ArrayList<DownloadedMusic> musics = new ArrayList<DownloadedMusic>();
		// 循环找出所有的歌曲和信息
		ContentResolver resolver = context.getContentResolver();
		Cursor musicCursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		int musicColumnIndex;
		// 遍历游标内容
		if (null != musicCursor && musicCursor.getCount() > 0) {
			for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor.moveToNext()) {
				DownloadedMusic music = new DownloadedMusic();

				// 歌曲的名称
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);

				// 歌曲文件的路径
				music.setMp3Path(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);

				// 歌曲的总播放时长
				music.setMp3time(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);

				// 歌曲文件的大小
				music.setMp3size(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE);

				musics.add(music);
			}
			musicCursor.close();
		}
		return musics;
	}

	@Override
	protected boolean validateData() {
		return false;
	}

	private void handleRecordVideoResult(final String videopath) {
		final File jointOutFile = new File(videopath);// 转换完成后输出视频目录
		if (jointOutFile.exists() && jointOutFile.length() > 0) {


			CustomAlertDialog mCustomDialog = new CustomAlertDialog(DownloadedMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
			mCustomDialog.setTitle("录制完成");
			mCustomDialog.setMsg("点击确定可预览视频");
			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent mIntent = new Intent();
					mIntent.setClass(DownloadedMusicActivity.this, VideoViewPlayingActivity.class);
					mIntent.setData(Uri.parse(videopath));
					startActivity(mIntent);
				}
			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();

		} else {

			// 自定义无标题、有确定按钮与无取消按钮对话框使用方法
			CustomAlertDialog mCustomDialog = new CustomAlertDialog(DownloadedMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
			mCustomDialog.setMsg("文件为空,请重新录制");
			mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();

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
		}

		case REQUEST_RECORD :
//			if(data != null && data.getData()!=null) {
//				Intent mIntent = new Intent();
//				mIntent.setClass(this, PreSummeryEditorActivity.class);
//				mIntent.putExtra("path", data.getData().getPath());
//				startActivityForResult(mIntent, 3);
//			}
			if(data != null && data.getData()!=null) {
				handleRecordVideoResult(data.getData().getPath());
			}
			break;
		case 3: {
			if(data != null && data.getData()!=null) {
				handleRecordVideoResult(data.getData().getPath());
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

		super.onActivityResult(requestCode, resultCode, data);

	}

	// 下拉&&上拉刷新
	private void onRefresh(Context mContext, int refreshType, int pageNo) {

		// refreshType:1：下拉刷新 2：上拉加载更多
		showProgressDialog(this, refreshType, pageNo);

	}

	// 点击删除本地音频
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

		final String filepath = mDownloadedMusicList.get(position).get("path").toString();
		final String name = mDownloadedMusicList.get(position).get("name").toString();
		if (!FileUtil.isFileExist(filepath)) {
			Toast.makeText(this, "音乐文件未找到", Toast.LENGTH_LONG).show();
			return;
		}

		// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
		CustomAlertDialog mCustomDialog = new CustomAlertDialog(DownloadedMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
		mCustomDialog.setTitle("删除提示");
		mCustomDialog.setMsg("确认删除" + name + "吗?");
		mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
			@Override
			public void onClick(View v) {
				File delFilePath = new File(filepath);
				delFilePath.delete();
				if(position >= mDownloadedMusicList.size()) return;
				mDownloadedMusicList.remove(position);
				mDownloadedMusicAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		}).show();
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

}
