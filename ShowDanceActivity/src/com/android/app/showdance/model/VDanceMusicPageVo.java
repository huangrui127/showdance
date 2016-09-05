package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;

/**
 * 舞曲视图分页Vo
 * 
 * @author jeff.lee
 * 
 */
public class VDanceMusicPageVo extends PageEntity {
	/**
	 * 名称(舞曲/作者/队长)
	 */
	private String name;

	private String dancer;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDancer() {
		return dancer;
	}

	public void setDancer(String dancer) {
		this.dancer = dancer;
	}

}
