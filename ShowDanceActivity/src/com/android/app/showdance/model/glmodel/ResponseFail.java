package com.android.app.showdance.model.glmodel;

public class ResponseFail {
	String message;
	String code;
	String flag;

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		message = msg;
	}

	public void setCode(String c) {
		code = c;
	}

	public String getCode() {
		return code;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return flag;
	}
	
	@Override
	public String toString() {
		return "message = "+message+" code = "+code+" flag = "+flag;
	}
}