package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;

/**
 * 视频评论 分页Vo
 * @author jeff.lee
 *
 */
public class MediaCommentPageVo extends PageEntity {
	
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
