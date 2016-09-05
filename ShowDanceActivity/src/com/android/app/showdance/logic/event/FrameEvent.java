package com.android.app.showdance.logic.event;

public class FrameEvent{
	public int musicId;
	public long total;
	public long current;
	public int percentage;
	public int state;
	public FrameEvent(int musicId,long total,long current,int percentage,int state) {
		this.musicId = musicId;
		this.total = total;
		this.current = current;
		this.percentage = percentage;
		this.state = state;
	}
	public FrameEvent(int musicId,long total,long current,int state) {
		this.musicId = musicId;
		this.total = total;
		this.current = current;
		this.state = state;
	}
}
