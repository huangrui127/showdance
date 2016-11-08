package com.android.app.showdance.model;

import com.android.app.showdance.entity.AutoIdCreateEntity;


/**
 * 下载视频表
 */
@SuppressWarnings("serial")
public class DownloadMedia extends AutoIdCreateEntity {

	/**
	 * 视频id
	 */
	private Long mediaId;

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	
}
