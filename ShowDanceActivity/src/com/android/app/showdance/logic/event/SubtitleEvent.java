package com.android.app.showdance.logic.event;

public class SubtitleEvent{
	public int subtitleid;
	public long total;
	public long current;
	public String path;
	public int state;
	public SubtitleEvent(int musicId,long total,long current,int state) {
		this.subtitleid = musicId;
		this.total = total;
		this.current = current;
//		this.percentage = percentage;
		this.state = state;
	}
	
	public SubtitleEvent(int musicId/*,long total,long current,int percentage*/,int state) {
		this.subtitleid = musicId;
//		this.total = total;
//		this.current = current;
//		this.percentage = percentage;
		this.state = state;
	}
	public SubtitleEvent(int musicId,String path/*,long current*/,int state) {
		this.subtitleid = musicId;
		this.path = path;
		this.state = state;
	}
}
