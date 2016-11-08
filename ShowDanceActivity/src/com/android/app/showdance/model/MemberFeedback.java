package com.android.app.showdance.model;

import com.android.app.showdance.entity.AutoIdCreateEntity;

/**
 * 用户意见反馈表
 */
@SuppressWarnings("serial")
public class MemberFeedback extends AutoIdCreateEntity {

	private String name;
	private String phone;
	private String feedback;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
