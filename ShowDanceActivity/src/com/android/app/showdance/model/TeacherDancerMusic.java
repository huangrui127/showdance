package com.android.app.showdance.model;

import java.io.Serializable;

import com.android.app.showdance.model.glmodel.TeacherInfo;

/**
 * Author: wyouflf Date: 13-11-10 Time: 下午8:11
 */
public class TeacherDancerMusic implements Serializable {
	private TeacherInfo.Search mItem;
	public TeacherDancerMusic(TeacherInfo.Search item) {
		mItem =item;
	}
	
	public TeacherInfo.Search getTeacher() {
		return mItem;
	}
}
