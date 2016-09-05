package com.android.app.showdance.model;

import java.util.Date;

import com.android.app.showdance.entity.AutoIdCreateEntity;

/**
 * 用户粉丝表
 */
// jackson标记不生成json对象的属性
@SuppressWarnings("serial")
public class UserFans extends AutoIdCreateEntity {

	/**
	 * 被关注用户
	 */
	private Long userId;

	// private User user;

	private String name;

	private String photo;

	/**
	 * 视频id
	 */
	private Long mediaId;

	/**
	 * 视频文件新名称
	 */
	private String mediaNewName;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaNewName() {
		return mediaNewName;
	}

	public void setMediaNewName(String mediaNewName) {
		this.mediaNewName = mediaNewName;
	}
	
	

	// // 多对多定义
	// @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch =
	// FetchType.LAZY)
	// // 中间表定义,表名采用默认命名规则
	// @JoinTable(name = "T_SYS_USER")
	// @NotFound(action = NotFoundAction.IGNORE)
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	// public User getUser() {
	// return user;
	// }
	//
	// public void setUser(User user) {
	// this.user = user;
	// }

}
