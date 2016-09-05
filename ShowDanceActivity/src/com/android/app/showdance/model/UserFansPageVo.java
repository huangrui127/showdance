package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;


/**
 * 用户粉丝分页Vo
 * @author jeff.lee
 *
 */
public class UserFansPageVo extends PageEntity {

	/**
	 * 粉丝用户id
	 */
	private Long createUser;
	/**
	 * 被关注用户id
	 */
	private Long userId;
	
	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
