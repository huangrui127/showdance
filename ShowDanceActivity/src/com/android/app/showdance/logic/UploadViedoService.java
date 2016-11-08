package com.android.app.showdance.logic;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.app.showdance.logic.event.UploadEvent;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.wumeiniang.R;
import com.lidroid.xutils.http.HttpHandler;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: DownloadViedoService
 * @Description: 上传视频服务工具类,通知栏显示进度,上传完成震动提示,
 * @author maminghua
 * @date 2015-6-2 上午11:19:52
 * 
 */
public class UploadViedoService extends Service {

	private final int NotificationID = 0x10000;
	private NotificationManager mNotificationManager = null;
	private NotificationCompat.Builder builder;

	private HttpHandler<File> mDownLoadHelper;

	private UploadManager uploadManager;

	// 文件上传路径
	private String filePath = "";

	/**
	 * Title: onBind
	 * 
	 * @Description:
	 * @param intent
	 * @return
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Title: onCreate
	 * 
	 * @Description:
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * Title: onStartCommand
	 * 
	 * @Description:
	 * @param intent
	 * @param flags
	 * @param startId
	 * @return
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("guolei","onStartCommand");
		if(intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		// 接收Intent传来的参数:
		String token = intent.getStringExtra("token");
		filePath = intent.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH);
		String remark = intent.getStringExtra("remark");
		long id = intent.getLongExtra("id", 0);
		
//		Configuration config = new Configuration.Builder()
//        .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
//        .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
//        .connectTimeout(10) // 链接超时。默认 10秒
//        .responseTimeout(60) // 服务器响应超时。默认 60秒
//        .recorder(recorder)  // recorder 分片上传时，已上传片记录器。默认 null
//        .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//        .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
//        .build();
//重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
//UploadManager uploadManager = new UploadManager(config);
		uploadManager = new UploadManager();// 初始化七牛上传管理器
		String title = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());

		doUpload(new File(filePath), token, title, remark, id);// 七牛文件上传

		// uploadFile(filePath, remark, id);// xUtils文件上传

		return super.onStartCommand(intent, flags, startId);
	}

	private void sendUploadStateIntent(String state,double percent,String filepath,String oldname,String newname) {
//	if(ConstantsUtil.ACTION_UPLOAD_SIZE.equalsIgnoreCase(state)) {
//		intent.putExtra("percent", percent);
//	} else {
//		intent.putExtra("filePath", filepath);
//	}
//	sendBroadcast(intent);
	
	EventBus.getDefault().post(new UploadEvent(percent,filepath,oldname,newname));
	}
	

	/**
	 * 
	 * @Description:七牛接口上传文件
	 * @param file
	 * @param token
	 * @param pre
	 * @return void
	 */
	private void doUpload(File file, String token, String pre, final String remark, final long id) {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(getApplicationContext());
		builder.setSmallIcon(R.drawable.app_logo);
		builder.setTicker("正在上传视频");
		builder.setContentTitle(pre);
		builder.setContentText("正在上传,请稍后...");
		builder.setNumber(0);
		builder.setAutoCancel(true);
		mNotificationManager.notify(NotificationID, builder.build());

		final String expectKey = UUID.randomUUID().toString() + ".mp4";// 文件名
		uploadManager.put(file, expectKey, token, new UpCompletionHandler() {
			@Override
			public void complete(String k, com.qiniu.android.http.ResponseInfo rinfo, JSONObject response) {
				// String s = k + ", " + rinfo + ", " + response;
				if (rinfo.statusCode == 200 && response != null) {
					Log.i("guolei", "complete "+response.toString());
					mNotificationManager.notify(NotificationID, builder.build());
					// 震动提示
					Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(1000L);// 参数是震动时间(long类型)
					stopSelf();
					mNotificationManager.cancel(NotificationID);
					String mediaOldName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
					mediaOldName = mediaOldName.substring(0, mediaOldName.indexOf("_"));
					String mediaNewName=null;
					try {
						mediaNewName = response.getString("key");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					sendUploadStateIntent(ConstantsUtil.ACTION_UPLOAD_STATE, 1.00, filePath,mediaOldName , mediaNewName);
					// 接口参数
//					final MediaInfo mediaInfo = new MediaInfo();
//					mediaInfo.setCreateUser(id);
//					mediaInfo.setMediaOldName(mediaOldName);
//					mediaInfo.setMediaNewName(mediaNewName);
//					mediaInfo.setRemark(remark);
//					mediaInfo.setAccess_KEY(ConstantsUtil.ACCESS_KEY);
//					mediaInfo.setSecret_KEY(ConstantsUtil.SECRET_KEY);
					
					
//					new Thread() {
//						public void run() {
//							try {
//								Map<String, Object> resultUploadState;
//
//								Thread.sleep(10);
//								resultUploadState = SoapWebServiceInterface.saveMediaInfoToJson(mediaInfo, 0);// requestCode
//																																		// 0:保存视频记录
//								if (resultUploadState != null) {
//									String result = resultUploadState.get("result").toString();
//									if (!result.equals("-1")) {
//
//										if (resultUploadState.get("result").equals("0")) {
//
//											JSONObject mJsonObject = new JSONObject(resultUploadState);
//
//											String resultInfo = mJsonObject.getString("resultInfo");
//											JSONObject mJsonObjectResult = new JSONObject(resultInfo);
//
//											// 接口参数
//											final MediaInfo mediaInfoPush = new MediaInfo();
//											mediaInfoPush.setCreateUser(id);
//											mediaInfoPush.setId(StringUtils.toLong(mJsonObjectResult.get("id").toString()));
//											mediaInfoPush.setMediaOldName(mJsonObjectResult.get("mediaOldName").toString());
//											mediaInfoPush.setMediaNewName(mJsonObjectResult.get("mediaNewName").toString());
//											mediaInfoPush.setRemark(remark);
//
//											// 修改之前根据视频名查询当前数据条目item
//											UploadVideoInfo uploadVideoInfo = (UploadVideoInfo) InitApplication.dbHelper.searchOne(UploadVideoInfo.class, mediaInfoPush.getMediaOldName());
//											uploadVideoInfo.setUploadState(1);
//
//											// 修改上传成功状态为1
//											InitApplication.dbHelper.update(uploadVideoInfo, "uploadState");
//
//											new Thread() {
//												public void run() {
//													try {
//														Map<String, Object> resultUploadState;
//
//														Thread.sleep(10);
//
//														resultUploadState = SoapWebServiceInterface.saveMediaInfoToJson(mediaInfoPush, 1);// requestCode
//														// 1:
//														// 推送
//
//														// 向handler发消息
//														Message message = handler.obtainMessage();
//														message.obj = resultUploadState;
//														handler.sendMessage(message);
//
//													} catch (Exception e) {
//														e.printStackTrace();
//													}
//												}
//											}.start();
//
//										}
//
//									} else {
//										Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
//									}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}.start();
				} else {
					mNotificationManager.cancel(NotificationID);
					sendUploadStateIntent(ConstantsUtil.ACTION_UPLOAD_STATE, -1, filePath,null , null);
				}
			}

		}, new UploadOptions(null, "video/mp4", true, new UpProgressHandler() {
			public void progress(String key, double percent) {
//				Log.i("guolei", key + ": " + percent);
//				Log.i("guolei","文件上传中...");

				String result = "";// 接受百分比的值
				// 百分比格式，后面不足2位的用0补齐 ##.00%
				DecimalFormat df1 = new DecimalFormat("0.00%");
				result = df1.format(percent);

				// int x = Long.valueOf(current).intValue();
				// int totalS = Long.valueOf(total).intValue();
				// builder.setProgress(totalS, x, false);
				builder.setContentInfo(result);
				mNotificationManager.notify(NotificationID, builder.build());
				sendUploadStateIntent(ConstantsUtil.ACTION_UPLOAD_STATE, percent, filePath,null , null);
			}
		}, null));

	}

	/**
	 * 用Handler来更新UI
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Map<String, Object> mapData = (Map<String, Object>) msg.obj;
			String result = mapData.get("result").toString();
			if (!result.equals("-1")) {
				if (mapData.get("result").equals("2")) {
					String resultInfo = mapData.get("resultInfo").toString();
					Toast.makeText(getApplicationContext(), resultInfo, Toast.LENGTH_SHORT).show();

				}
			}

		}
	};

	/**
	 * 
	 * @param x
	 *            当前值
	 * @param total
	 *            总值
	 * @return 当前百分比
	 * @Description:返回百分之值
	 */
	private String getPercent(int x, int total) {
		String result = "";// 接受百分比的值
		double x_double = x * 1.0;
		double tempresult = x_double / total;
		// 百分比格式，后面不足2位的用0补齐 ##.00%
		DecimalFormat df1 = new DecimalFormat("0.00%");
		result = df1.format(tempresult);
		return result;
	}

	/**
	 * Title: onDestroy
	 * 
	 * @Description:
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopSelf();
	}

}
