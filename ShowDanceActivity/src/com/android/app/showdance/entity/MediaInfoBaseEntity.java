package com.android.app.showdance.entity;

import java.util.Date;




/**
 * 相册视频表基类
 * @author Administrator
 *
 */
public class MediaInfoBaseEntity  extends AutoEntity {
	
	/**
	 * 视频文件原名称（带路径）
	 */
	private String mediaOldName;
	
	/**
	 * 文件新名称（带路径）
	 */
	private String mediaNewName;
	
	/**
	 * 视频快照文件名
	 */
	private String snapshot;
	
	/**
	 * 视频名称
	 */
	private String remark;
	
	/**
	 * 文件标记(1图片2视频)
	 */
	private Integer flag;

	/**
	 * 播放量
	 */
	private Integer playCount;
	
	/**
	 * 创建人
	 */
	private Long createUser;
	
	/**
	 * 舞曲Id
	 */
	private Long danceMusicId;
	
	/**
	 * 上传时间
	 */
	private Date createTime;

	public MediaInfoBaseEntity() {
	}

	public String getMediaOldName() {
		return this.mediaOldName;
	}

	public void setMediaOldName(String mediaOldName) {
		this.mediaOldName = mediaOldName;
	}

	public String getMediaNewName() {
		return this.mediaNewName;
	}

	public void setMediaNewName(String mediaNewName) {
		this.mediaNewName = mediaNewName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public Long getDanceMusicId() {
		return danceMusicId;
	}

	public void setDanceMusicId(Long danceMusicId) {
		this.danceMusicId = danceMusicId;
	}

}
