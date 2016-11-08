package com.android.app.showdance.model;

import com.android.app.showdance.entity.AutoEntity;


public class VUserFansMediaMusicCount extends AutoEntity {

	/**
	 * 下载视频数量
	 */
	private long downloadMediaCount;
	
	/**
	 * 我的秀舞（上传视频数量）
	 */
	private long mediaInfoCount;
	
	/**
	 * 我的粉丝数量
	 */
	private long createUserFansCount;
	
	/**
	 * 我关注的舞友数量
	 */
	private long userFansCount;
	
	/**
	 * 下载舞曲数量
	 */
	private long downloadMusicCount;
	
	public long getDownloadMediaCount() {
		return this.downloadMediaCount;
	}

	public void setDownloadMediaCount(long downloadMediaCount) {
		this.downloadMediaCount = downloadMediaCount;
	}

	public long getDownloadMusicCount() {
		return this.downloadMusicCount;
	}

	public void setDownloadMusicCount(long downloadMusicCount) {
		this.downloadMusicCount = downloadMusicCount;
	}

	public long getCreateUserFansCount() {
		return this.createUserFansCount;
	}

	public void setCreateUserFansCount(long createUserFansCount) {
		this.createUserFansCount = createUserFansCount;
	}

	public long getUserFansCount() {
		return this.userFansCount;
	}

	public void setUserFansCount(long userFansCount) {
		this.userFansCount = userFansCount;
	}

	public long getMediaInfoCount() {
		return mediaInfoCount;
	}

	public void setMediaInfoCount(long mediaInfoCount) {
		this.mediaInfoCount = mediaInfoCount;
	}

}
