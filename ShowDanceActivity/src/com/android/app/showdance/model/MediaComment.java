package com.android.app.showdance.model;

import java.util.Date;

import com.android.app.showdance.entity.AutoEntity;


/**
 * 视频评论表
 */
public class MediaComment extends AutoEntity {
	
	/**
	 * 创建人信息
	 */
	private User user;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 视频id
	 */
	private Long mediaId;
	
	/**
	 * 视频评论
	 */
	private String remark;

	private String name;

	private String photo;
	
	public MediaComment() {
	}

	public Long getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	// 一对多 定义
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
