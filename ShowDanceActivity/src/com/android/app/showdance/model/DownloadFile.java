/**   
 * @Title: DownloadFile.java
 * @Package com.cloud.coupon.utils
 * @Description: TODO(用一句话描述该文件做�?��)
 * @author 陈红�?
 * @date 2013-6-26 下午5:31:23
 * @version V1.0
 */
package com.android.app.showdance.model;

import java.io.File;
import java.io.Serializable;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @ClassName: DownloadFile
 * @Description: 下载文件
 * @author 陈红�?
 * @date 2013-6-26 下午5:31:23
 * 
 */
public class DownloadFile implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示�?��)
	 */
	private static final long serialVersionUID = 1L;
	private boolean isStop;
	@SuppressWarnings("rawtypes")
	private HttpHandler mHttpHandler;

	/**
	 * @Title: downloadFileByUrl
	 * @Description: 通过URL地址下载�?��文件 将文件存储到 toPath�?
	 * @param url
	 *            下载地址
	 * @param toPath
	 *            存储的路�?
	 * @param downCallBack
	 *            下载过程的回�?下载进度,与成功之后的回调
	 * @return void
	 * @author 陈红�?
	 */
	public DownloadFile startDownloadFileByUrl(String url, String toPath, RequestCallBack<File> downCallBack) {

		if (downCallBack == null) {
			throw new RuntimeException("AjaxCallBack对象不能为null");
		} else {
			HttpUtils down = new HttpUtils();
			// 支持断点续传
			mHttpHandler = down.download(url, toPath, true, downCallBack);
			// mHttpHandler.
		}
		return this;
	}

	public void stopDownload() {
		if (mHttpHandler != null) {
			mHttpHandler.cancel();
			mHttpHandler.cancel(true);
			if (!mHttpHandler.isCancelled()) {
				mHttpHandler.cancel();
				mHttpHandler.cancel(true);
			}
		}
	}

	public boolean isStop() {
		isStop = mHttpHandler.isCancelled();
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
}
