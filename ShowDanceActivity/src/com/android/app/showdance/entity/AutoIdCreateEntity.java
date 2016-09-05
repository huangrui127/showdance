package com.android.app.showdance.entity;

import java.util.Date;


/**
 * 统一定义entity基类. <br>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略. <br>
 * 子类可重载getId()函数重定义id的列名映射和生成策略. <br>
 * 2015-04-13 wencp:新加并发控制(乐观锁,用于并发控制)、数据更新时间、操作用户ID. 2015-4-13 19:59:42
 * 新增创建人、创建时间属性
 * 
 * @author : jeff.lee
 * @date : 2015-4-13 19:58:55
 */
public abstract class AutoIdCreateEntity extends AbstractEntity<Long> {

	/**
	 * 主键ID
	 */
	protected Long id;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	protected String token;

	public AutoIdCreateEntity() {
	}

	/**
	 * 主键ID 根据数据库主键自增长策略 依赖于数据库(SQL Serveer、MySQL数据库使用)
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置 主键ID
	 * 
	 * @param id
	 *            主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
