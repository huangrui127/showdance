package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;

/**
 * Author: wyouflf Date: 13-11-10 Time: 下午8:11
 */
public class DownloadedMusic extends PageEntity {

	private String mp3size = null;

	private String mp3Path = null;

	private String mp3time = null;

	public DownloadedMusic() {

	}

	public String getMp3size() {
		return mp3size;
	}

	public void setMp3size(String mp3size) {
		this.mp3size = mp3size;
	}

	public String getMp3Path() {
		return mp3Path;
	}

	public void setMp3Path(String mp3Path) {
		this.mp3Path = mp3Path;
	}

	public String getMp3time() {
		return mp3time;
	}

	public void setMp3time(String mp3time) {
		this.mp3time = mp3time;
	}

}
