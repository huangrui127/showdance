package com.android.app.showdance.utils;

import com.android.app.wumeiniang.R;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 
 * @ClassName: XUtilsBitmap
 * @Description:
 * @author maminghua
 * @date 2015-6-13 上午10:04:26
 * 
 */
public class XUtilsBitmap {
	private XUtilsBitmap() {

	}

	private static BitmapUtils bitmapUtils;// 异步加载图片

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 * 
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static BitmapUtils getBitmapUtils(Context appContext) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(appContext.getApplicationContext());// 初始化BitmapUtils模块
			bitmapUtils.configDefaultLoadingImage(R.drawable.image_loading);// 设置默认加载图片
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.empty_photo);// 加载失败后图片
			bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		}
		return bitmapUtils;
	}
}
