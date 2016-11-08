package com.android.app.showdance.model;

import com.android.app.showdance.entity.MediaInfoBaseEntity;


public class VMediaInfo extends MediaInfoBaseEntity {

	/**
	 * 队长艺名
	 */
	private String name;
	
	/**
	 * 点赞量
	 */
	private Integer praiseSum;
	/**
	 * 分享数
	 */
	private Integer shareMediaCount;
	public Integer getShareMediaCount() {
		return shareMediaCount;
	}
	public void setShareMediaCount(Integer shareMediaCount) {
		this.shareMediaCount = shareMediaCount;
	}
	/**
	 * 下载视频量
	 */
	private Integer downloadMediaSum;
	/**
	 * 评论数
	 */
	private Integer mediaCommentCount;
	
	public Integer getMediaCommentCount() {
		return mediaCommentCount;
	}
	public void setMediaCommentCount(Integer mediaCommentCount) {
		this.mediaCommentCount = mediaCommentCount;
	}
	/**
	 * 个人关注量
	 */
	private Integer userFansCount;
	
	/**
	 * 用户上传人所在地址
	 */
	private String address;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 个人头像
     */
    private String photo;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPraiseSum() {
		return praiseSum;
	}
	public void setPraiseSum(Integer praiseSum) {
		this.praiseSum = praiseSum;
	}
	public Integer getDownloadMediaSum() {
		return downloadMediaSum;
	}
	public void setDownloadMediaSum(Integer downloadMediaSum) {
		this.downloadMediaSum = downloadMediaSum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getUserFansCount() {
		return userFansCount;
	}
	public void setUserFansCount(Integer userFansCount) {
		this.userFansCount = userFansCount;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
