package com.android.app.showdance.ui;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.RecordedVideoAdapter;
import com.android.app.showdance.adapter.UploadedVideoAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.logic.UploadViedoService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.UploadEvent;
import com.android.app.showdance.model.MediaInfo;
import com.android.app.showdance.model.MediaInfoPageVo;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.VideoUploadInfo;
import com.android.app.showdance.ui.VolleyBaseActivity.OnResponseListener;
import com.android.app.showdance.ui.baidu.bvideo.VideoViewPlayingActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.showdance.widget.CustomAlertDialogForEditText;
import com.android.app.showdance.widget.MyProgressBar;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: RecordedVideoActivity
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-12 下午06:08:27
 * 
 */
public class RecordedVideoActivity extends VolleyBaseActivity implements OnItemClickListener {
	private static final String TAG="RecordedVideoActivity";
	private SwipeListView recordedVideoList;

	private List<UploadVideoInfo> recordedVideoListInfo;

	private RecordedVideoAdapter mRecordedVideoAdapter;

	private LinearLayout no_login_layout;
	
	private long id;

	private User userInfo;
	private Long cityId;

private MediaMetadataRetriever mRetriever;
	private boolean loadingFlag = true;

	private int selectPosition;


	// 用于上传token
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recordedvideo);
		mRetriever = new MediaMetadataRetriever();
		findViewById();
		initView();
		setOnClickListener();

	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRetriever.release();
	}
	
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);

		recordedVideoList = (SwipeListView) findViewById(R.id.recordedVideoList);

		no_login_layout = (LinearLayout) findViewById(R.id.no_login_layout);
	}

	@Override
	protected void initView() {
		tvTitle.setText("本地视频");
		return_imgbtn.setVisibility(View.VISIBLE);
		inflater = LayoutInflater.from(this);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


		userInfo = InitApplication.mSpUtil.getUser();

		if (userInfo == null) {
			userInfo = new User();
		} else {
			id = userInfo.getId(); // 用户登录

//			cityId = userInfo.getCityId(); // 用户城市
		}

		new ScanVideoTask().execute();

		// 获取上传凭证token

	}
	@Override
	protected String[] initActions() {
		return new String[] { ConstantsUtil.ACTION_UPLOAD, ConstantsUtil.ACTION_UPLOAD_SIZE, ConstantsUtil.ACTION_UPLOAD_STATE, ConstantsUtil.ACTION_DEL_RECORDED };
	}
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		recordedVideoList.setSwipeListViewListener(swipeListViewListener);
//		recordedVideoList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			 recordedVideoList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//
//	                @Override
//	                public void onItemCheckedStateChanged(ActionMode mode, int position,
//	                                                      long id, boolean checked) {
//	                    mode.setTitle("Selected (" + recordedVideoList.getCountSelected() + ")");
//	                }
//
//	                @Override
//	                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//	                    switch (item.getItemId()) {
////	                        case R.id.menu_delete:
////	                            swipeListView.dismissSelected();
////	                            mode.finish();
////	                            return true;
//	                        default:
//	                            return false;
//	                    }
//	                }
//
//	                @Override
//	                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//	                    MenuInflater inflater = mode.getMenuInflater();
////	                    inflater.inflate(R.menu.menu_choice_items, menu);
//	                    return true;
//	                }
//
//	                @Override
//	                public void onDestroyActionMode(ActionMode mode) {
//	                	recordedVideoList.unselectedChoiceStates();
//	                }
//
//	                @Override
//	                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//	                    return false;
//	                }
//	            });
//		 }
	}

	private SwipeListViewListener swipeListViewListener = new SwipeListViewListener() {
		
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			Log.d("RecordedVideoActivity","onStartOpen");
		}
		
		@Override
		public void onStartClose(int position, boolean right) {
			Log.d("guolei","onStartClose");
		}
		
		@Override
		public void onOpened(int position, boolean toRight) {
			Log.d("guolei","onOpened");
		}
		
		@Override
		public void onMove(int position, float x) {
			Log.d("guolei","onMove");
		}
		
		@Override
		public void onListChanged() {
			Log.d("guolei","onListChanged");
		}
		
		@Override
		public void onLastListItem() {
			Log.d("guolei","onLastListItem");
		}
		
		@Override
		public void onFirstListItem() {
			Log.d("guolei","onFirstListItem");
		}
		
		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			Log.d("guolei","onDismiss");
		}
		
		@Override
		public void onClosed(int position, boolean fromRight) {
			Log.d("guolei","onClosed");
		}
		
		@Override
		public void onClickFrontView(int position) {
			Intent mIntent = new Intent();
			mIntent.setClass(RecordedVideoActivity.this, VideoViewPlayingActivity.class);
			mIntent.setData(Uri.parse(recordedVideoListInfo.get(position).getFilePath()));
			startActivity(mIntent);
//			Intent it = new Intent(Intent.ACTION_VIEW);  
//	        it.setDataAndType(Uri.parse(recordedVideoListInfo.get(position).getFilePath()), "video/mp4");  
//	        startActivity(it);
		}
		
		@Override
		public void onClickBackView(int position) {
			
		}
		
		@Override
		public void onChoiceStarted() {
			
		}
		
		@Override
		public void onChoiceEnded() {
			
		}
		
		@Override
		public void onChoiceChanged(int position, boolean selected) {
			
		}
		
		@Override
		public int onChangeSwipeMode(int position) {
			return SwipeListView.SWIPE_MODE_DEFAULT;
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

	/**
	 * 
	 * @Description:获取上传凭证token
	 * @param mContext
	 * @return void
	 */
	
    
	
	public static class BaseResponse {
		protected boolean flag;
		protected String message;
		protected String data;
		public void setFlag(boolean f) {
			flag = f;
		}
		public boolean getFlag() {
			return flag;
		}
		public void setMessage(String ms) {
			message = ms;
		}
		public String getMessage() {
			return message;
		}
		public void setData(String d) {
			data = d;
		}
		public String getData() {
			return data;
		}
	}
	public void getQiniuToken(final String filePath) {

		StringRequest request = new StringRequest(Method.POST, "http://112.74.83.166:82/api/qiniuToken", 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						String json = response.toString();
						Log.d("guolei", "onResponse " + json);
						ObjectMapper objectMapper = new ObjectMapper();
						try {
							BaseResponse result = objectMapper.readValue(
									json.substring(json.indexOf("{")),
									BaseResponse.class);
							if (!result.getFlag() )
								return;
							
							token = result.getData();
							showProgressDialogFile(RecordedVideoActivity.this, filePath);
						}catch (JsonParseException e1) {
							e1.printStackTrace();
							if(mDialog!=null) {
								mDialog.setCancelable(true);
							}
						} catch (JsonMappingException e1) {
							e1.printStackTrace();
							if(mDialog!=null) {
								mDialog.setCancelable(true);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
							if(mDialog!=null) {
								mDialog.setCancelable(true);
							}
						} finally {
							
//							token = null;
						}
					}
				}
				, new Response.ErrorListener	() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if(mDialog!=null)
							mDialog.dismiss();
						Log.d("guolei","onErrorResponse "+arg0.toString());
						makeToast(RecordedVideoActivity.this, R.string.getqiniutoken_fail);
					}
					
				}) {
			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String str = null;
				try {
				str = new String(response.data, "utf-8");
				} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				}
				return Response.success(str,
				HttpHeaderParser.parseCacheHeaders(response));
				}
		};
		VolleyManager.getInstance().getRequestQueue().add(request);

	}

	@Override
	protected boolean validateData() {

		return false;
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
	protected void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
		userInfo = InitApplication.mSpUtil.getUser();

		if (userInfo == null) {
			return;
		} else if (cityId == null || cityId == 0) {
			cityId = InitApplication.mSpUtil.getCityId(); // 用户城市
		}
	}

	
	
	@Override
	protected void handleReceiver(Context context, Intent intent) {
		if (intent.getAction().equals(ConstantsUtil.ACTION_UPLOAD)) {
			if (id != 0) {
						String filePath = intent.getStringExtra("filePath");
						selectPosition = intent.getIntExtra("position", 0);
//						String titleName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
//						CustomAlertDialog(titleName.replace(".mp4", ""), filePath);
//						showProgressDialogFile(RecordedVideoActivity.this, filePath);
						showUploadZoneDialog(this,filePath);
			} else {
				Toast.makeText(getApplicationContext(), "请先登录后上传视频", Toast.LENGTH_SHORT).show();
			}
		}		else if (intent.getAction().equals(ConstantsUtil.ACTION_DEL_RECORDED)) {
			final int position = intent.getIntExtra("position", 0);
			final String fileName = recordedVideoListInfo.get(position).getFileName();
			final String filePath = recordedVideoListInfo.get(position).getFilePath();
			final String fileBgPath = recordedVideoListInfo.get(position).getFileBgPath();
			// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
			CustomAlertDialog mCustomDialog = new CustomAlertDialog(RecordedVideoActivity.this).builder(R.style.DialogTVAnimWindowAnim);
			mCustomDialog.setTitle("删除提示");
			mCustomDialog.setMsg("确认删除 " + fileName.substring(0, fileName.indexOf("_")) + " 吗?");
			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
				@Override
				public void onClick(View v) {
					File delFilePath = new File(filePath);
					delFilePath.delete();
					new File(fileBgPath).delete();
					// 删除 根据条件 fileName 等于选中的就把它删除掉
//					InitApplication.dbHelper.deleteCriteria(UploadVideoInfo.class, "fileName", fileName);
					recordedVideoListInfo.remove(position);
					scanVideoFile(filePath);
					recordedVideoList.closeOpenedItems();
					mRecordedVideoAdapter.notifyDataSetChanged();
				}
			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();

		}
	}
	
	public void onEventMainThread(UploadEvent event) {

		// 上传状态:当前进度
			showSizeProgressDialog(RecordedVideoActivity.this, event);
//		else 

		// 上传状态:成功
//		if (intent.getAction().equals(ConstantsUtil.ACTION_UPLOAD_STATE)) {
//			Log.d("guolei","ACTION_UPLOAD_STATE");
//			// 关闭正在响应...的对话框
//			if (mDialog != null) {
//				mDialog.cancel();
//			}
//			String filePath = intent.getStringExtra("filePath");
//			String mediaOldName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
//			String resultNewName = intent.getStringExtra("resultNewName");
//			showProgressDialogUpolad(RecordedVideoActivity.this, mediaOldName, resultNewName, 0);// requestCode
//																									// 0:
//																									// 保存视频记录
//		}
	}

	/** 扫描SD卡 */
	private class ScanVideoTask extends AsyncTask<Void, File, List<UploadVideoInfo>> {
		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(RecordedVideoActivity.this);
			pd.setMessage("正在扫描已录制视频...");
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
			File mfile = new File(InitApplication.SdCardRecordedVideoPath);
			// 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			SimpleDateFormat outsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			File[] files = mfile.listFiles();
			if(files == null)
				return uploadVideoInfoList;
			
				for (File file:files) {
					if (!FileUtil.checkIsMp4File(file.getPath())) 
						continue;
//					Log.d("guolei","file "+file.getPath());
					UploadVideoInfo uploadVideoItem = new UploadVideoInfo();
					uploadVideoItem.setFileName(file.getName());
					String path = file.getPath();
					uploadVideoItem.setFilePath(path);
					String[] list = uploadVideoItem.getFileName().split("_");
					if (list.length >= 3) {
						try {
							Date date = sdf.parse(list[2]);
							uploadVideoItem.setCreateTime(outsdf.format(date));
						} catch (ParseException e) {
						}
					} else {
						uploadVideoItem.setCreateTime("");
					}
					
					uploadVideoItem.setFileBgPath(path.substring(0, path.lastIndexOf(".")+1)+"png");
					uploadVideoItem.setFileSize(StringUtils.doubleToString(FileUtil.getFileOrFilesSize(file.getPath(), 3)));
						uploadVideoInfoList.add(uploadVideoItem);
						// 保存之前根据视频名查询当前数据是否存在
						UploadVideoInfo uploadVideoInfo = (UploadVideoInfo) InitApplication.dbHelper.searchOne(UploadVideoInfo.class, uploadVideoItem.getFileName());
						if (uploadVideoInfo == null) {
							// 保存每一条视频信息到数据库
							InitApplication.dbHelper.save(uploadVideoItem);
						}
				}
				Collections.sort(uploadVideoInfoList);
			// 返回得到的mp4列表
			return uploadVideoInfoList;
		}

		
		@Override
		protected void onPostExecute(List<UploadVideoInfo> result) {
			super.onPostExecute(result);

			// 从数据库读取保存的所有视频信息List
			recordedVideoListInfo = result;// InitApplication.dbHelper.search(UploadVideoInfo.class);

			if (recordedVideoListInfo != null && recordedVideoListInfo.size() != 0) {
				no_login_layout.setVisibility(View.GONE);
				mRecordedVideoAdapter = new RecordedVideoAdapter(RecordedVideoActivity.this, recordedVideoListInfo, recordedVideoList, 1,mRetriever);
				recordedVideoList.setAdapter(mRecordedVideoAdapter);
			}else{
				no_login_layout.setVisibility(View.VISIBLE);
			}
			if (userInfo != null)
				WS_getMyVedio();
			else
				makeToast(RecordedVideoActivity.this, R.string.load_tips);
			pd.dismiss();
		}
	}

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
		request.setuser_id(userInfo.getId());
		VolleyManager.getInstance().postRequest(
				request,
				BaseRequest.UPLOAD_CLIENT_ID+"/"+InitApplication.mSpUtil.getUser().getuser_token()+"/videoList",
				new OnResponseListener<VideoUploadInfo.ShowResponse>(VideoUploadInfo.ShowResponse.class) {
					@Override
					protected void handleResponse(ShowResponse response) {
						
						List<VideoUploadResponse> list= response.getData().getdata();
						Log.d("guolei","mVideoUploadList size " +list.size());
						Iterator<VideoUploadResponse > iterator = list.iterator();
						while(iterator.hasNext()) {
							VideoUploadResponse item = iterator.next();
							if(item.getvideoname() == null) {
								iterator.remove();
							} else {
								String videoname = item.getpath();
								videoname = videoname.substring(videoname.lastIndexOf("/")+1);
								for(UploadVideoInfo item1: recordedVideoListInfo) {
									if(item1.getFileName().equalsIgnoreCase(videoname)) {
										item1.setUploadState(1);
									}
								}
							}
						}
						if(mRecordedVideoAdapter==null) {
						}else {
							mRecordedVideoAdapter.setVideoType(1);
							mRecordedVideoAdapter.notifyDataSetChanged();
						}
					}
					
					@Override
					protected void handleFailResponse(ResponseFail response) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),"服务器请求失败!",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				}, mErrorListener);
		
	}
	
	/**
	 * 
	 * @Description:获取已录制视频接口(未调用)
	 * @param mContext
	 * @param id
	 * @return void
	 */
	public void showProgressDialog(Context mContext, long id) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
		MediaInfoPageVo mediaInfoPageVo = new MediaInfoPageVo();
		paramsMap = new HashMap<String, Object>();
		mediaInfoPageVo.setCreateUser(id);
		mediaInfoPageVo.setPageNo(1);
		mediaInfoPageVo.setPageSize(ConstantsUtil.PageSize);
		paramsMap.put("mediaInfoPageVo", mediaInfoPageVo);

		Task mTask = new Task(TaskType.TS_MediaInfoPageList, paramsMap);
		MainService.newTask(mTask);
	}

	/**
	 * 
	 * @Description:添加视频描叙对话框
	 * @param title
	 * @param filePath
	 * @return void
	 */
//	private void CustomAlertDialog(final String title, final String filePath) {
//		getQiniuToken();
//		new CustomAlertDialogForEditText(RecordedVideoActivity.this).builder(R.style.DialogTVAnimWindowAnim).setTitle(title).setNegativeButton("发  布", new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showProgressDialogFile(RecordedVideoActivity.this, filePath);
//			}
//		}).show();
//
//	}

	/**
	 * 
	 * @Description:HTTP接口上传文件对话框
	 * @param mContext
	 * @return void
	 */
	public void showProgressDialogFile(Context mContext, String filePath) {

		// 接口参数
		// HashMap paramsMap = new HashMap();
		// paramsMap.put("filePath", filePath);
		//
		// Task mTask = new Task(TaskType.TS_PostMediaInfoList, paramsMap);
		// MainService.newTask(mTask);
		if (token == null)
			return;
		Intent mIntent = new Intent();

		mIntent.setClass(this, UploadViedoService.class);
		mIntent.putExtra("token", token);
		mIntent.putExtra("filePath", filePath);
//		mIntent.putExtra("remark", descriptive);
		mIntent.putExtra("id", id);
		startService(mIntent);

	}

	/**
	 * 
	 * @Description:HTTP接口正在上传文件进度对话框
	 * @param @param mContext
	 * @param @param total
	 * @param @param current
	 * @return void
	 */
	public void showSizeProgressDialog(Context mContext, UploadEvent event) {
		MyProgressBar uploading_proressbar;
		if (loadingFlag) {
			getQiniuToken(event.filepath);
			mDialog = new AlertDialog.Builder(mContext).create();
			loadingFlag = false;
			mDialog.show();
			view = inflater.inflate(R.layout.custom_progressbar_dialog, null);
			uploading_proressbar = (MyProgressBar) view
					.findViewById(R.id.uploading_proressbar);
			uploading_proressbar.setMax(100);
			uploading_proressbar.setProgress(0);
			mDialog.setContentView(view);
			mDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
		}
		double Progress = event.percent*100;
		uploading_proressbar = (MyProgressBar) view.findViewById(R.id.uploading_proressbar);
		if(uploading_proressbar == null)
			return;
		
		if(uploading_proressbar.getProgress() < Progress)
			uploading_proressbar.setProgress((int)Progress);

		// int p = (int) ((Progress * 100) / fileSize);// 计算进度
		// tvLoading.setText(p + "%");

		if (Progress >= 100.00 && event.newname != null) { // 上传完成
			loadingFlag = true;
			token = null;
			// 关闭正在上传...的对话框
			makeToast(RecordedVideoActivity.this, R.string.upload_success);
			// 打开正在响应...的对话框

			// "正在加载"对话框,提示信息TextView的设置
			VideoUploadInfo.Request request = new VideoUploadInfo.Request();
			request.setname(event.newname);
			request.setuser_id(userInfo.getId());
			request.setpath(event.filepath);
			request.setvideo_name(event.oldname);
			VolleyManager.getInstance().postRequest(
					request,
					VolleyManager.CLIENT_ID + "/" + userInfo.getuser_token()
							+ "/quniuVideo",
					new OnResponseListener<VideoUploadInfo.Response>(
							VideoUploadInfo.Response.class) {

						@Override
						protected void handleResponse(
								VideoUploadInfo.Response response) {
							WS_getMyVedio();
						}

					}, mErrorListener);
		} else if(Progress <0){
			mDialog.dismiss();
			mDialog = null;
		}

	}

	
	public void showUploadZoneDialog(Context mContext,String filepath) {
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.show();
			View view = inflater.inflate(R.layout.custom_upload_zone_dialog, null);
			TextView v = (TextView)view.findViewById(R.id.wumeiniang);
			v.setTag(filepath);
			UploadVideoInfo item = recordedVideoListInfo.get(selectPosition);//.getFileName();
			if(item.getUploadState()==1) {
				v.setText("武媚娘已上传");
			}else
				v.setOnClickListener(mUploadZoneListener);
			
			v = (TextView)view.findViewById(R.id.tangdou);
			v.setOnClickListener(mUploadZoneListener);
			v = (TextView)view.findViewById(R.id.jiuai);
			v.setOnClickListener(mUploadZoneListener);
			
			
			mDialog.setContentView(view);
			mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
	}
	
	private void updateWuMeiFirst() {
		Toast.makeText(this, "请先上传到武媚娘!", Toast.LENGTH_SHORT).show();
	}
	
	private View.OnClickListener mUploadZoneListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.wumeiniang:
				mDialog.dismiss();
				showSizeProgressDialog(RecordedVideoActivity.this,  new UploadEvent(0.00, (String)v.getTag(), null, null));
				break;
			case R.id.tangdou:
				UploadVideoInfo item = recordedVideoListInfo.get(selectPosition);//.getFileName();
				if(item.getUploadState()!=1) {
					updateWuMeiFirst();
					break;
				}
				Intent mIntent = new Intent(Intent. ACTION_VIEW,Uri.parse("http://u.tangdou.com/"));
				startActivity(mIntent);
				mDialog.dismiss();
				break;
			case R.id.jiuai:
				UploadVideoInfo item1 = recordedVideoListInfo.get(selectPosition);//.getFileName();
				if(item1.getUploadState()!=1) {
					updateWuMeiFirst();
					break;
				}
				Intent mIntent1 = new Intent(Intent. ACTION_VIEW,Uri.parse("http://i.9igcw.com/my/upvideo.html"));
				startActivity(mIntent1);
				mDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 
	 * @Description:HTTP接口上传视频完成后调用此接口保存视频记录
	 * @param mContext
	 * @param mediaOldName
	 * @param @param mediaNewName
	 * @return void
	 */
//	public void showProgressDialogUpolad(Context mContext, String mediaOldName, String mediaNewName, int requestCode) {
//		mDialog = new AlertDialog.Builder(mContext).create();
//
//		mDialog.show();
//		// 注意此处要放在show之后 否则会报异常
//		mDialog.setContentView(R.layout.loading_progressbar_dialog);
//		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
//
//		VideoUploadInfo.Request request = new VideoUploadInfo.Request();
//		request.setname(mediaNewName);
//		request.setuser_id(userInfo.getId());
//		request.setvideo_name(mediaNewName);
//		VolleyManager.getInstance().postRequest(
//				request,
//				VolleyManager.CLIENT_ID + "/" + userInfo.getuser_token()
//						+ "/quniuVideo",
//				new OnResponseListener<VideoUploadInfo.Response>(
//						VideoUploadInfo.Response.class) {
//
//					@Override
//					protected void handleResponse(
//							VideoUploadInfo.Response response) {
//
//					}
//
//				}, mErrorListener);
//		
//		// 接口参数
////		MediaInfo mediaInfo = new MediaInfo();
////		paramsMap = new HashMap<String, Object>();
////		mediaInfo.setCreateUser(id);
////		mediaInfo.setMediaOldName(mediaOldName);
////		mediaInfo.setMediaNewName(mediaNewName);
////		mediaInfo.setRemark(descriptive);
////		// mediaInfo.setDanceMusicId(1);
////		paramsMap.put("mediaInfo", mediaInfo);
////		paramsMap.put("requestCode", requestCode);
////
////		Task mTask = new Task(TaskType.TS_SaveMediaInfo, paramsMap);
////		MainService.newTask(mTask);
//
//	}

	@Override
	public void refresh(Object... param) {
		
	}

	// 点击播放本地视频
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

		Intent mIntent = new Intent();
		mIntent.setClass(RecordedVideoActivity.this, VideoViewPlayingActivity.class);
		mIntent.setData(Uri.parse(recordedVideoListInfo.get(position).getFilePath()));
		startActivity(mIntent);

	}

}
