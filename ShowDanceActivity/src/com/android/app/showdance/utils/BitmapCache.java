package com.android.app.showdance.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;
/**
 * 对图片进行管理的工具类。
 * 
 * @author Tony
 */
public class BitmapCache implements ImageCache {

	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private static LruCache<String, Bitmap> mMemoryCache;

	/**
	 * ImageLoader的实例。
	 */
	private static BitmapCache mImageLoader;

	private BitmapCache() {
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
	}

	/**
	 * 获取ImageLoader的实例。
	 * 
	 * @return ImageLoader的实例。
	 */
	public static BitmapCache getInstance() {
		if (mImageLoader == null) {
			mImageLoader = new BitmapCache();
		}
		return mImageLoader;
	}


	@Override
	public Bitmap getBitmap(String url) {
		return mMemoryCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
	}

}
