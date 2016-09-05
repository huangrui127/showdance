/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.android.app.showdance.model;

import java.io.Serializable;
import java.util.List;

import com.android.app.showdance.entity.BaseEntity;


/**
 * 角色管理Role.
 * 
 * @author 尔演&Eryan eryanwcp@gmail.com
 * @date 2013-3-21 上午12:27:56
 * 
 */
public class Role extends BaseEntity implements Serializable {

	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色编码
	 */
	private String code;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 关联的资源
	 */
	private List<Resource> resources;
	/**
	 * 关联资源ID集合 @Transient
	 */
	private List<Long> resourceIds;

	/**
	 * 关联的用户
	 */
	private List<User> users;
	/**
	 * 关联用户ID集合 @Transient
	 */
	private List<Long> userIds;

	public Role() {

	}

	// @Column(name = "NAME",length = 100,nullable = false,unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// @Column(name = "CODE",length = 36)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// @Column(name = "REMARK",length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	/**
	 * 角色拥有的资源id字符串集合
	 * 
	 * @return
	 */
	public List<Long> getResourceIds() {
		// if (!Collections3.isEmpty(resources)) {
		// resourceIds = ConvertUtils.convertElementPropertyToList(resources,
		// "id");
		// }
		return resourceIds;
	}

	public void setResourceIds(List<Long> resourceIds) {
		this.resourceIds = resourceIds;
	}

	/**
	 * 角色拥有的资源字符串,多个之间以","分割
	 * 
	 * @return
	 */
	// @Transient
//	public String getResourceNames() {
//		List<Resource> ms = Lists.newArrayList();
//		for (Resource m : resources) {
//			if (m.getStatus().equals(StatusState.normal.getValue())) {
//				ms.add(m);
//			}
//		}
//		return ConvertUtils.convertElementPropertyToString(ms, "name", ", ");
//	}

	/**
	 * 用户拥有的角色字符串,多个之间以","分割
	 * 
	 * @return
	 */
	// @Transient
//	public String getUserNames() {
//		return ConvertUtils.convertElementPropertyToString(users, "loginName", ", ");
//	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	// @Transient
	public List<Long> getUserIds() {
		// if (!Collections3.isEmpty(users)) {
		// userIds = ConvertUtils.convertElementPropertyToList(users, "id");
		// }
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}
