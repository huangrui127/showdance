/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.android.app.showdance.model;

import java.io.Serializable;
import java.util.List;


/**
 * 用户管理User.
 * 
 * @author 尔演&Eryan eryanwcp@gmail.com
 * @date 2013-3-21 上午12:28:04
 * 
 */
public class User extends UserInfo implements Serializable {

	/**
	 * 有序的关联对象集合
	 */
	private List<Role> roles;
	/**
	 * 有序的关联Role对象id集合
	 */
	private List<Long> roleIds;

	/**
	 * 资源 有序的关联对象集合
	 */
	private List<Resource> resources;
	/**
	 * 资源 id集合 @Transient
	 */
	private List<Long> resourceIds;

	/**
	 * 默认组织机构
	 */
	private Organ defaultOrgan;
	/**
	 * 默认组织机构 @Transient
	 */
	private Long defaultOrganId;

	/**
	 * 组织机构
	 */
	private List<Organ> organs;

	/**
	 * 组织机构ID集合 @Transient
	 */
	private List<Long> organIds;

	/**
	 * 组织机构名称 @Transient
	 */
	private String organNames;

	/**
	 * 用户岗位信息
	 */
	private List<Post> posts;
	
	private City city;

	public User() {

	}

	// 多对多定义
	// @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =
	// FetchType.LAZY)
	// // 中间表定义,表名采用默认命名规则
	// @JoinTable(name = "T_SYS_USER_ROLE", joinColumns = { @JoinColumn(name =
	// "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	// // Fecth策略定义
	// // @Fetch(FetchMode.SUBSELECT)
	// // 集合按id排序.
	// @OrderBy("id")
	// @NotFound(action = NotFoundAction.IGNORE)
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔. <br>
	 * 如果是超级管理员 直接返回 "超级管理员" AppConstants.ROLE_SUPERADMIN
	 */
	// @Transient
	// 非持久化属性.
	// public String getRoleNames() {
	// Long superId = 1L;
	// if (superId.equals(this.getId())) {
	// return SecurityConstants.ROLE_SUPERADMIN;
	// }
	// return ConvertUtils.convertElementPropertyToString(roles, "name", ", ");
	// }

	@SuppressWarnings("unchecked")
	// @Transient
	public List<Long> getRoleIds() {
		// if (!Collections3.isEmpty(roles)) {
		// roleIds = ConvertUtils.convertElementPropertyToList(roles, "id");
		// }
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	// @Transient
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

	public Organ getDefaultOrgan() {
		return defaultOrgan;
	}

	public void setDefaultOrgan(Organ defaultOrgan) {
		this.defaultOrgan = defaultOrgan;
	}

	// @Transient
	public Long getDefaultOrganId() {
		if (defaultOrgan != null) {
			defaultOrganId = defaultOrgan.getId();
		}
		return defaultOrganId;
	}

	public void setDefaultOrganId(Long defaultOrganId) {
		this.defaultOrganId = defaultOrganId;
	}

	public String getDefaultOrganName() {
		String doName = null;
		if (defaultOrgan != null) {
			doName = defaultOrgan.getName();
		}
		return doName;
	}

	public String getDefaultOrganSysCode() {
		String sysCode = null;
		if (defaultOrgan != null) {
			sysCode = defaultOrgan.getSysCode();
		}
		return sysCode;
	}

	public List<Organ> getOrgans() {
		return organs;
	}

	public void setOrgans(List<Organ> organs) {
		this.organs = organs;
	}

	public String getOrganNames() {
		return organNames;
	}

	public void setOrganNames(String organNames) {
		this.organNames = organNames;
	}

	// @Transient
	public List<Long> getOrganIds() {
		// if (!Collections3.isEmpty(organs)) {
		// organIds = ConvertUtils.convertElementPropertyToList(organs, "id");
		// }
		return organIds;
	}

	public void setOrganIds(List<Long> organIds) {
		this.organIds = organIds;
	}

	// @Transient
	// public String getOrganNames() {
	// return ConvertUtils.convertElementPropertyToString(organs, "name", ", ");
	// }

	// @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =
	// FetchType.LAZY)
	// @JoinTable(name = "T_SYS_USER_POST", joinColumns = { @JoinColumn(name =
	// "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "POST_ID") })
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * 用户岗位名称 VIEW 多个之间以","分割
	 * 
	 * @return
	 */
	// @Transient
	// public String getPostNames() {
	// return ConvertUtils.convertElementPropertyToString(posts, "name", ",");
	// }

	// @Transient
	// public List<Long> getPostIds() {
	// if (Collections3.isNotEmpty(posts)) {
	// return ConvertUtils.convertElementPropertyToList(posts, "id");
	// }
	// return Lists.newArrayList();
	// }
	
	

}
