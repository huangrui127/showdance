package com.android.app.showdance.model;

import com.android.app.showdance.entity.MediaInfoBaseEntity;


public class VMediaInfoDanceTeam extends MediaInfoBaseEntity {

	/**
	 * 舞队id
	 */
	private Long danceTeamId;

	/**
	 * 用户类型(0队长1领队2队员3粉丝)
	 */
	private Integer userType;
	
	/**
	 * 舞曲名
	 */
	private String musicName;
	
	/**
	 * 舞曲作者名
	 */
	private String musicAuthor;

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getDanceTeamId() {
		return danceTeamId;
	}

	public void setDanceTeamId(Long danceTeamId) {
		this.danceTeamId = danceTeamId;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getMusicAuthor() {
		return musicAuthor;
	}

	public void setMusicAuthor(String musicAuthor) {
		this.musicAuthor = musicAuthor;
	}
	
}
