package com.android.app.showdance.logic.event;

public class MusicEvent{
	public int musicId;
	public long total;
	public long current;
	public int percentage;
	public int state;
	public MusicEvent(int musicId,long total,long current,int percentage,int state) {
		this.musicId = musicId;
		this.total = total;
		this.current = current;
		this.percentage = percentage;
		this.state = state;
	}

}
