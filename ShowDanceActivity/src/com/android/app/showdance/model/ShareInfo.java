package com.android.app.showdance.model;

import com.android.app.showdance.entity.AutoIdCreateEntity;



public class ShareInfo  extends AutoIdCreateEntity {

	private Integer flag;
	private Integer platform;
	
	private Long mediaId;


	public ShareInfo() {
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getPlatform() {
		return this.platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	
	
}
