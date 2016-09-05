package com.android.app.showdance.model.glmodel;

import java.io.Serializable;

import android.util.Log;

public class MusicInfo {
public static class Request  extends BaseRequest {
	private String keyWord;
	protected int start;
	protected int stop;
	private String name;
	
	public Request() {
		super();
		token = MUSIC_SEARCH_TOKEN;
	}
	
	public Request(String keyword) {
		keyWord = keyword;
		token = MUSIC_SEARCH_TOKEN;
	}

	public Request(String name,String token) {
		this.name = name;
		this.token = token;
	}
	
	public void setKeyWord(String keyword) {
		keyWord = keyword;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setStart(int _start) {
		start = _start;
	}
	public int getStart() {
		return start;
	}
	public void setStop(int _stop) {
		stop = _stop;
	}
	public int getStop() {
		return stop;
	}
}

public static class Response  extends BaseResponse<MusicSearchResponse>{

}
public static class MusicSearchResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6278979222185206583L;
	/**
	 * 
	 */
	private String created_at;
	private Integer download_volume;
	private Integer id;
	private String lrc;
	private String url;
	private String name;
	private String size;
	private String singer;
	private String sort;
	private String teacher;
	private String updated_at;

	public void setCreated_at(String _created_at) {
		created_at = _created_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setDownload_volume(Integer _download_volume) {
		download_volume = _download_volume;
	}

	public Integer getDownload_volume() {
		return download_volume;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer _id) {
		id = _id;
	}

	public void setLrc(String _lrc) {
		lrc = _lrc;
	}

	public String getLrc() {
		return lrc;
	}

	public void setUrl(String _url) {
		url = _url;
	}

	public String getUrl() {
		return url;
	}

	public void setName(String _name) {
		name = _name;
	}

	public String getName() {
		return name;
	}

	public void setSize(String _size) {
		size = _size;
	}

	public String getSize() {
		return size;
	}

	public void setSinger(String _singer) {
		singer = _singer;
	}

	public String getSinger() {
		return singer;
	}

	public void setSort(String _sort) {
		sort = _sort;
	}

	public String getSort() {
		return sort;
	}

	public void setTeacher(String _teacher) {
		teacher = _teacher;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setUpdated_at(String _updated_at) {
		updated_at = _updated_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	
	@Override
	public String toString() {
		return "created_at " + created_at + "download_volume "
				+ download_volume + " id " + id + " url " + url + " name "
				+ name + " size  " + size + " singer " + singer + " sort "
				+ sort + " teacher " + teacher + "updated at  " + updated_at
				+ " teacher " + teacher;
	}
}
}