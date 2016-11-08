package com.android.app.showdance.model.glmodel;

import java.util.List;

public class BaseResponse <T> {
	protected boolean flag;
	protected String message;
	protected List<T> data;
	public void setFlag(boolean f) {
		flag = f;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setMessage(String ms) {
		message = ms;
	}
	public String getMessage() {
		return message;
	}
	public void setData(List<T> d) {
		data = d;
	}
	public List<T> getData() {
		return data;
	}
}