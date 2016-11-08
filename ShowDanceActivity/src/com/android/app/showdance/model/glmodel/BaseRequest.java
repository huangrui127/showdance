package com.android.app.showdance.model.glmodel;


public class BaseRequest {
	protected static final String MUSIC_SEARCH_TOKEN = "b464b50611e709a18f7e2c2e92407e9c";
	protected static final String MUSIC_DOWNLOAD_TOKEN = "ef1728165212100f359adf67910c5ab4";
	protected static final String STAR_TEACHER_LIST_TOKEN = "a36186be3fcc1c0e41cb539f1dda4287";
	public static final String STAR_TEACHER_SONG_TOKEN = "0b4f74af17061a12f3bfa416d4e817d8";
	protected static final String SIGN_UP_TOKEN = "d56b699830e77ba53855679cb1d252da";
//	protected static final String SIGN_IN_TOKEN = "0b4f74af17061a12f3bfa416d4e817d8";
	protected static final String AUTHOR_TOKEN = "18b43c6a536a8fe1362f7a3887936be6";
	protected static final String GIVEN_FRAME_TOKEN = "f2f72a201a5848040a04a2ebf483c90e";
	public static final String FRAME_TEACHER_TOKEN = "31536b1835f0c77f5de15d9cdb7656f0";
	public static final String UPLOAD_CLIENT_ID = "b4e6f6a250be8603044ac8a43f2589e6";
	public static final String UPLOAD_DELETE_CLIENT_ID = "011c8ebc29719be66ad60cb8c55a44d7";
	protected String token;
	public BaseRequest() {
	}
	
	public BaseRequest(String token) {
		this.token = token;
	}
	
	public void setToken(String _token) {
		token=_token;
	}
	
public String getToken() {
		return token;
	}
}