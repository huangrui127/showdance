package com.android.app.showdance.model;

import com.android.app.showdance.entity.PageEntity;

/**
 * 媒体分页Vo
 * 
 * @author jeff.lee
 * 
 */
public class MediaInfoPageVo extends PageEntity {

	/**
	 * 名称
	 */
	private String remark;

	/**
	 * 舞队创建人
	 */
	private long createUser;

	/**
	 * 舞队id
	 */
	private String danceTeamId;

	/**
	 * 所在地
	 */
	private String address;

	/**
	 * 审核标记
	 */
	private Integer flag;

	/**
	 * 省份
	 */
	private String province;

	public String getDanceTeamId() {
		return danceTeamId;
	}

	public void setDanceTeamId(String danceTeamId) {
		this.danceTeamId = danceTeamId;
	}

	public long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
