package com.android.app.showdance.logic;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

import com.android.app.showdance.model.DownloadMedia;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.SoapWebServiceInterface;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * 
 * @ClassName: DownloadViedoService
 * @Description: 下载视频服务工具类,通知栏显示进度,下载完成震动提示,
 * @author maminghua
 * @date 2015-6-2 上午11:19:52
 * 
 */
public class DownloadViedoService extends Service {

	private final int NotificationID = 0x10000;
	private NotificationManager mNotificationManager = null;
	private NotificationCompat.Builder builder;

	private HttpHandler<File> mDownLoadHelper;

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
		System.out.println("onStartCommand");
		// 接收Intent传来的参数:
		// 文件下载路径
		String downloadUrl = intent.getStringExtra("downloadUrl");
		String mediaOldName = intent.getStringExtra("mediaOldName");
		String remark = intent.getStringExtra("remark");
		long createUser = intent.getLongExtra("createUser", 0);
		long mediaId = intent.getLongExtra("mediaId", 0);

		DownFile(downloadUrl, mediaOldName, remark, createUser, mediaId);

		return super.onStartCommand(intent, flags, startId);
	}

	private void DownFile(String downloadUrl, String mediaOldName, final String remark, final long createUser, final long mediaId) {
		// 创建保存路径
		File f = new File(InitApplication.SdCardDownloadVideoPath, mediaOldName);

		mDownLoadHelper = new HttpUtils().download(downloadUrl, f.getAbsolutePath(), true, false, new RequestCallBack<File>() {

			@Override
			public void onStart() {
				super.onStart();
				System.out.println("开始下载文件");
				mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				builder = new NotificationCompat.Builder(getApplicationContext());
				builder.setSmallIcon(R.drawable.app_logo);
				builder.setTicker("正在下载视频");
				builder.setContentTitle(remark);
				builder.setContentText("正在下载,请稍后...");
				builder.setNumber(0);
				builder.setAutoCancel(true);
				mNotificationManager.notify(NotificationID, builder.build());

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				System.out.println("文件下载中");
				int x = Long.valueOf(current).intValue();
				int totalS = Long.valueOf(total).intValue();
				builder.setProgress(totalS, x, false);
				builder.setContentInfo(getPercent(x, totalS));
				mNotificationManager.notify(NotificationID, builder.build());
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				System.out.println("文件下载完成");

				mNotificationManager.notify(NotificationID, builder.build());
				// 震动提示
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(1000L);// 参数是震动时间(long类型)
				stopSelf();
				// startActivity(installIntent);// 下载完成之后自动弹出安装界面
				mNotificationManager.cancel(NotificationID);

				// 接口参数
				final DownloadMedia downloadMedia = new DownloadMedia();
				downloadMedia.setCreateUser(createUser);
				downloadMedia.setMediaId(mediaId);

				new Thread() {
					public void run() {
						try {
							Thread.sleep(1000);
							Map<String, Object> resultDownState = SoapWebServiceInterface.saveDownloadMediaToJson(downloadMedia);
							if (resultDownState != null) {

								// 向handler发消息
								Message message = handler.obtainMessage();
								message.obj = resultDownState;
								handler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("文件下载失败");
				mNotificationManager.cancel(NotificationID);
				Toast.makeText(getApplicationContext(), "下载失败,此文件已经存在!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancelled() {
				super.onCancelled();
				System.out.println("文件下载结束，停止下载器");
				mDownLoadHelper.cancel();
			}

		});
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
				if (result.equals("0")) {
					Intent intent = new Intent(ConstantsUtil.ACTION_DOWNLOADVIDEO_SUCCESS);
					sendBroadcast(intent);
					Toast.makeText(getApplicationContext(), "下载成功！", Toast.LENGTH_SHORT).show();
				} else if (result.equals("1")) {
					Toast.makeText(getApplicationContext(), "已下载！", Toast.LENGTH_SHORT).show();

				}
			} else {
				Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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
	 * @return
	 * @Description:获取当前应用的名称
	 */
	private String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
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
