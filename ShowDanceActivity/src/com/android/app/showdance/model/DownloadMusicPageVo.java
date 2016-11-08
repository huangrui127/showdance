package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;


/**
 * 下载舞曲分页Vo
 * 
 * @author jeff.lee
 * 
 */
public class DownloadMusicPageVo extends PageEntity {

	/**
	 * 用户id
	 */
	private Long createUser;
	/**
	 * 舞曲id
	 */
	private Long musicId;

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getMusicId() {
		return musicId;
	}

	public void setMusicId(Long musicId) {
		this.musicId = musicId;
	}

}
