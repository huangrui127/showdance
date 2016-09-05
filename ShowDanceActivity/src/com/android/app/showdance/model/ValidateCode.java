package com.android.app.showdance.model;

import java.util.Date;

import com.android.app.showdance.entity.AutoEntity;


//@Entity
//@Table(name = "validate_code")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// jackson标记不生成json对象的属性
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler" })
//@SuppressWarnings("serial")
public class ValidateCode extends AutoEntity{

	private String phone;
	private String val_code;
	private Date add_time;
	private Integer status;	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVal_code() {
		return val_code;
	}
	public void setVal_code(String val_code) {
		this.val_code = val_code;
	}
	public Date getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	
}
