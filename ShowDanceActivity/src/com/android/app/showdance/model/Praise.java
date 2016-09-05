package com.android.app.showdance.model;

import com.android.app.showdance.entity.AutoIdCreateEntity;

// Generated 2015-4-16 9:37:37 by Hibernate Tools 3.4.0.CR1


/**
 * 视频点赞表
 */
public class Praise extends AutoIdCreateEntity {

	private long mediaId;

	public Praise() {
	}

	public long getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

}
