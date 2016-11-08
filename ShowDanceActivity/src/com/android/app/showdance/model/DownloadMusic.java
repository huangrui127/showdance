package com.android.app.showdance.model;

import com.android.app.showdance.entity.AutoIdCreateEntity;

/**
 * 下载舞曲表
 */
public class DownloadMusic extends AutoIdCreateEntity {

	/**
	 * 舞曲id
	 */
	private Long musicId;

	public Long getMusicId() {
		return musicId;
	}

	public void setMusicId(Long musicId) {
		this.musicId = musicId;
	}

}
