package com.android.app.showdance.model;

import java.util.Date;

import com.android.app.showdance.entity.AutoEntity;

//@Entity
//@Table(name = "media_info")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// jackson标记不生成json对象的属性
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler" })
//@SuppressWarnings("serial")
public class MediaInfo extends AutoEntity {

	/**
	 * 文件原名称(多个使用,号分割)
	 */
	private String mediaOldName;

	/**
	 * 文件新名称(多个使用,号分割)
	 */
	private String mediaNewName;

	/**
	 * 说说
	 */
	private String remark;

	/**
	 * 文件标记(1图片2视频)
	 */
	private Integer flag;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 舞曲Id
	 */
	private Integer danceMusicId;

	private Date createTime;

	private String secret_KEY;

	private String access_KEY;

	public MediaInfo() {
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

	public Integer getDanceMusicId() {
		return this.danceMusicId;
	}

	public void setDanceMusicId(Integer danceMusicId) {
		this.danceMusicId = danceMusicId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSecret_KEY() {
		return secret_KEY;
	}

	public void setSecret_KEY(String secret_KEY) {
		this.secret_KEY = secret_KEY;
	}

	public String getAccess_KEY() {
		return access_KEY;
	}

	public void setAccess_KEY(String access_KEY) {
		this.access_KEY = access_KEY;
	}

}
