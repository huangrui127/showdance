package com.android.app.wumeiniang.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.android.app.showdance.db.CityDB;
import com.android.app.showdance.db.DbHelper;
import com.android.app.showdance.impl.EventHandler;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.City;
import com.android.app.showdance.model.Location;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.utils.SharePreference;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;

/**
 * 
 * @ClassName: mApplication
 * @Description: 全局配置
 * @author maminghua
 * @date 2015-4-1 上午10:43:22
 * 
 */
public class InitApplication extends Application {
	private static Context context;
	private static InitApplication mInstance = null;
	public static SharePreference mSpUtil = null;

	public static DbHelper dbHelper;

	private double density;

	// 百度地图定位
//	public LocationClient mLocationClient;
//	private MyLocationListener mMyLocationListener;
	/** 是否首次定位 */
	private boolean isFirstLoc = true;

	public static Location mLocation = new Location();

	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();

	private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	private static final int CITY_LIST_SCUESS = 0;

//	private List<City> mCityList;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<City>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;
	private boolean isCityListComplite;

//	private CityDB mCityDB;

	public static int mNetWorkState;

	private static final String FORMAT = "^[a-z,A-Z].*$";

	public static String SdCardImagePath; // 图片缓存目录
	public static String SdCardMusicPath;// 下载舞曲保存目录
	public static String SdCardLrcPath;// 下载舞曲歌词保存目录(原歌词目录)
	public static String SdCardNewLrcPath;// 下载舞曲歌词保存目录(重命名后歌词目录)
	public static String SdCardDownloadVideoPath;// 下载视频保存目录
	public static String SdCardRecordedVideoPath;// 输出拍摄转码后的视频保存目录
	public static String SdCardLrcInfoPath;// 合成视频使用舞曲的歌词信息目录
	public static String SdCardSubTitlePath;// 下载舞曲保存目录
	public static String sdCardForegroundPath;
	
	public static String sdCardAvator2ForegroundPath;
	public static String sdCardAvator3ForegroundPath;

	public static synchronized InitApplication getInstance() {
		return mInstance;
	}

	public static Context getContext() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        PlatformConfig.setWeixin("wx8926439f1cc6aeae", "c167a9bcd8569be9ae05856190b2a2bb");
        PlatformConfig.setQQZone("1105468668", "OwG5BoI5NYB0OR6J");
		
		context = getApplicationContext();
		mInstance = this;
		VolleyManager.getInstance(this);
		mSpUtil = new SharePreference(mInstance, SharePreference.preference);// 创建配置文件
		dbHelper = DbHelper.getInstance(mInstance);// 创建数据库

//		mLocationClient = new LocationClient(mInstance);
//		InitLocation();
//		initCityList();
		IntentFilter filter = new IntentFilter(NET_CHANGE_ACTION);
		registerReceiver(netChangeReceiver, filter);

		// 创建拍照后的存储路劲
		SdCardImagePath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardImagePath;
		File imagePath = new File(SdCardImagePath);
		if (!imagePath.exists()) {
			imagePath.mkdirs();
		}

		// 创建下载舞曲保存目录
		SdCardMusicPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardMusicPath;
		File musicPath = new File(SdCardMusicPath);
		if (!musicPath.exists()) {
			musicPath.mkdirs();
		}
		

		
		// 创建下载舞曲的歌词保存目录
		SdCardLrcPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardLrcPath;
		File lrcPath = new File(SdCardLrcPath);
		if (!lrcPath.exists()) {
			lrcPath.mkdirs();
		}
		
		// create foreground image dir
		sdCardForegroundPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.sdCardForegroundPath;
		File fgPath = new File(sdCardForegroundPath);
		if (!fgPath.exists()) {
			fgPath.mkdirs();
		}
		File nomedia  = new File(fgPath,".nomedia");
		if(!nomedia.exists()) {
			nomedia.mkdirs();
		}
		
		SdCardSubTitlePath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardSubtitlePath;
		File subtitle = new File(SdCardSubTitlePath);
		if (!subtitle.exists()) {
			subtitle.mkdirs();
		}
		nomedia  = new File(subtitle,".nomedia");
		if(!nomedia.exists()) {
			nomedia.mkdirs();
		}
		
		sdCardAvator2ForegroundPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.sdCardAvator2ForegroundPath;
		fgPath = new File(sdCardAvator2ForegroundPath);
		if (!fgPath.exists()) {
			fgPath.mkdirs();
		}
		nomedia  = new File(fgPath,".nomedia");
		if(!nomedia.exists()) {
			nomedia.mkdirs();
		}
		
		sdCardAvator3ForegroundPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.sdCardAvator3ForegroundPath;
		fgPath = new File(sdCardAvator3ForegroundPath);
		if (!fgPath.exists()) {
			fgPath.mkdirs();
		}
		nomedia  = new File(fgPath,".nomedia");
		if(!nomedia.exists()) {
			nomedia.mkdirs();
		}
		
		// 创建下载舞曲的歌词保存目录
		SdCardNewLrcPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardNewLrcPath;
		File newLrcPath = new File(SdCardNewLrcPath);
		if (!newLrcPath.exists()) {
			newLrcPath.mkdirs();
		}
		

		// 创建下载视频保存目录
		SdCardDownloadVideoPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardDownloadVideoPath;
		File downloadVideoPath = new File(SdCardDownloadVideoPath);
		if (!downloadVideoPath.exists()) {
			downloadVideoPath.mkdirs();
		}

		// 创建输出拍摄转码后的视频保存目录
		SdCardRecordedVideoPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardRecordedVideoPath;
		File cameraVideoPath = new File(SdCardRecordedVideoPath);
		if (!cameraVideoPath.exists()) {
			cameraVideoPath.mkdirs();
		}
		nomedia  = new File(cameraVideoPath,".nomedia");
		if(nomedia.exists()) {
			nomedia.delete();
		}
		
		// 创建输出拍摄转码后的视频保存目录
		SdCardLrcInfoPath = ConstantsUtil.RootDirectory.getAbsolutePath() + ConstantsUtil.SdCardLrcInfoPath;
		File lrcInfoPath = new File(SdCardLrcInfoPath);
		if (!lrcInfoPath.exists()) {
			lrcInfoPath.mkdirs();
		}
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).threadPoolSize(2).build();
        //Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(configuration);
	}

//	private void InitLocation() {
//		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);
//
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// 打开GPS
//		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
//		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
//		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
//		option.setIsNeedAddress(true);// 回的定位结果包含地址信息
//		mLocationClient.setLocOption(option);
//		mLocationClient.start();// 开始定位
//
//	}

//	/**
//	 * 实现实位回调监听
//	 */
//	public class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (isFirstLoc) {
//				mLocation.setTime(location.getTime());
//				mLocation.setLocType(location.getLocType());
//				mLocation.setLatitude(location.getLatitude());
//				mLocation.setLongitude(location.getLongitude());
//				mLocation.setRadius(location.getRadius());
//				if (location.getLocType() == BDLocation.TypeGpsLocation) {
//					mLocation.setSpeed(location.getSpeed());
//					mLocation.setSatelliteNumber(location.getSatelliteNumber());
//					mLocation.setAddrStr(location.getCity());
//					mLocation.setDirection(location.getDirection());
//				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//					mLocation.setAddrStr(location.getAddrStr());
//					// mSpUtil.setLocationMyCity(location.getCity());
//					mSpUtil.setLocationMyCity(location.getProvince());
//					// 运营商信息
//					mLocation.setOperators(location.getOperators());
//				}
//				Log.i("BaiduLocationApiDem", mLocation.getLatitude() + "----" + mLocation.getLongitude());
//				isFirstLoc = false;
//			}
//
//		}
//
//	}

	public void getAssetData(Context act) {
		try {
			PackageManager manager = act.getPackageManager();
			PackageInfo info = manager.getPackageInfo(act.getPackageName(), 0);

			DisplayMetrics dm = new DisplayMetrics();
			dm = act.getResources().getDisplayMetrics();
			setDensity(dm.density); //
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CITY_LIST_SCUESS:
				isCityListComplite = true;
				if (mListeners.size() > 0)// 通知接口完成加载
					for (EventHandler handler : mListeners) {
						handler.onCityComplite();
					}
				break;
			default:
				break;
			}
		}
	};

	BroadcastReceiver netChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(NET_CHANGE_ACTION)) {
				if (mListeners.size() > 0)// 通知接口完成加载
					for (EventHandler handler : mListeners) {
						handler.onNetChange();
					}
			}
			mNetWorkState = NetUtil.getNetworkState(mInstance);
		}

	};

//	private void initCityList() {
//		mCityList = new ArrayList<City>();
//		mSections = new ArrayList<String>();
//		mMap = new HashMap<String, List<City>>();
//		mPositions = new ArrayList<Integer>();
//		mIndexer = new HashMap<String, Integer>();
////		mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				isCityListComplite = false;
//				prepareCityList();
//				mHandler.sendEmptyMessage(CITY_LIST_SCUESS);
//			}
//		}).start();
//	}

//	private boolean prepareCityList() {
////		mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
//		for (City city : mCityList) {
//			// String firstName = city.getFirstPY();// 第一个字拼音的第一个字母
//			String firstName = city.getProvincePY();// 省第一个字拼音的第一个字母
//			if (firstName.matches(FORMAT)) {
//				if (mSections.contains(firstName)) {
//					mMap.get(firstName).add(city);
//				} else {
//					mSections.add(firstName);
//					List<City> list = new ArrayList<City>();
//					list.add(city);
//					mMap.put(firstName, list);
//				}
//			} else {
//				if (mSections.contains("#")) {
//					mMap.get("#").add(city);
//				} else {
//					mSections.add("#");
//					List<City> list = new ArrayList<City>();
//					list.add(city);
//					mMap.put("#", list);
//				}
//			}
//		}
//		Collections.sort(mSections);// 按照字母重新排序
//		int position = 0;
//		for (int i = 0; i < mSections.size(); i++) {
//			mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
//			mPositions.add(position);// 首字母在listview中位置，存入list中
//			position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
//		}
//		return true;
//	}

//	public synchronized CityDB getCityDB() {
//		if (mCityDB == null)
//			mCityDB = openCityDB();
//		return mCityDB;
//	}

//	private CityDB openCityDB() {
//		String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator + "com.android.app.wumeiniang" + File.separator + CityDB.CITY_DB_NAME;
//		File db = new File(path);
//		if (!db.exists()) {
//			// L.i("db is not exists");
//			try {
//				InputStream is = getAssets().open("city.db");
//				FileOutputStream fos = new FileOutputStream(db);
//				int len = -1;
//				byte[] buffer = new byte[1024];
//				while ((len = is.read(buffer)) != -1) {
//					fos.write(buffer, 0, len);
//					fos.flush();
//				}
//				fos.close();
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				// T.showLong(mApplication, e.getMessage());
//				System.exit(0);
//			}
//		}
//		return new CityDB(this, path);
//	}

//	public List<City> getCityList() {
//		return mCityList;
//	}

	public List<String> getSections() {
		return mSections;
	}

	public Map<String, List<City>> getMap() {
		return mMap;
	}

	public List<Integer> getPositions() {
		return mPositions;
	}

	public Map<String, Integer> getIndexer() {
		return mIndexer;
	}

	public boolean isCityListComplite() {
		return isCityListComplite;
	}

	/**
	 * 返回配置文件的日志开关
	 * 
	 * @return
	 */
	// public boolean getLoggingSwitch() {
	// try {
	// ApplicationInfo appInfo =
	// getPackageManager().getApplicationInfo(getPackageName(),
	// PackageManager.GET_META_DATA);
	// boolean b = appInfo.metaData.getBoolean("LOGGING");
	// LogUtil.w("[ECApplication - getLogging] logging is: " + b);
	// return b;
	// } catch (NameNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	// }


}
