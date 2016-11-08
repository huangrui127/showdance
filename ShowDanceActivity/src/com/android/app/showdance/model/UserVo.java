package com.android.app.showdance.model;

import java.util.Date;


public class UserVo extends UserInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String phone;//注册手机号
	private Integer code;//验证码
	private Date haveTheTime;//验证码产生时间
	private Integer sign;//操作标记
    /**
     * 新密码
     */
    private String newPassword;
    
	public UserVo(){}
	
	public UserVo(String phone,Integer code,Date haveTheTime){
		this.phone=phone;
		this.code=code;
		this.haveTheTime=haveTheTime;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Date getHaveTheTime() {
		return haveTheTime;
	}
	public void setHaveTheTime(Date haveTheTime) {
		this.haveTheTime = haveTheTime;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Integer getSign() {
		return sign;
	}

	public void setSign(Integer sign) {
		this.sign = sign;
	}
	
	
}
