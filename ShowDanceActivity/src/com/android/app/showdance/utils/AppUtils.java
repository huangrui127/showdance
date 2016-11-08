package com.android.app.showdance.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 
 * @ClassName: AppUtils
 * @Description:获取当前客户端版本信息
 * @author maminghua
 * @date 2015-9-6 上午11:32:32
 * 
 */
public class AppUtils {
	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			return 1;
		}
	}

}
