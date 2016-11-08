package com.android.app.showdance.model.glmodel;

public class MusicDownloadOKRequest  extends BaseRequest {
	private int id;
	
	public MusicDownloadOKRequest() {
		super();
		token = MUSIC_DOWNLOAD_TOKEN;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	public int getId() {
		return id;
	}
}