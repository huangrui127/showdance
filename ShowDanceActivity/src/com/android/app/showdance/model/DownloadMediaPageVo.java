package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;


/**
 * 下载视频分页Vo
 * @author jeff.lee
 *
 */
public class DownloadMediaPageVo extends PageEntity {

	/**
	 * 用户id
	 */
	private Long createUser;
	/**
	 * 视频id
	 */
	private Long mediaId;
	
	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	
}
