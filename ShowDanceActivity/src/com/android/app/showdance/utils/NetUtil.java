package com.android.app.showdance.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 
 * @ClassName: NetUtil
 * @Description: 网络状态工具类
 * @author maminghua
 * @date 2015-1-13 上午09:38:18
 * 
 */
public class NetUtil {
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	/**
	 * 
	 * @Description:检测网络是否可用
	 * @param context
	 * @param @return
	 * @return boolean
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	// 检测网络连接
	public static boolean checkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
			return true;
		}
		return false;
	}

	/**
	 * 检查网络是否连接正常
	 * 
	 * @return
	 */
	public boolean checkNetWorkStatus(Context context) {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}

	/**
	 * 
	 * @param mActivity
	 * @return
	 */
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description:检测是否连接WIFI
	 * @param context
	 * @param @return
	 * @return boolean
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @Description:检测网络状态类型
	 * @param context
	 * @param @return
	 * @return int
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi
		NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info == null) return NETWORN_NONE;
		State state = info.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_WIFI;
		}

		// 3G
		info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(info == null) return NETWORN_NONE;
		state = info.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_MOBILE;
		}
		return NETWORN_NONE;
	}

	/**
	 * 
	 * @Description:获取当前网络类型
	 * @param context
	 * @param @return
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
}
