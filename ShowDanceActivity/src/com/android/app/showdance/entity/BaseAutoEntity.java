/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.android.app.showdance.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 统一定义entity基类. <br>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略. <br>
 * 子类可重载getId()函数重定义id的列名映射和生成策略. <br>
 * 2012-12-15 wencp:新加并发控制(乐观锁,用于并发控制)、数据更新时间、操作用户ID.
 * 
 * @author : 尔演&Eryan eryanwcp@gmail.com
 * @date : 2012-12-21 上午11:12:07
 */
// @Column(name="...") 该属性对应表中的字段是什么，没有name表示一样
// @Table 对象与表映射
// @UniqueConstraint 唯一约束
// @Version 方法和字段级，乐观锁用法，返回数字和timestamp，数字为首选
// @Transient 暂态属性，表示不需要处理
// @Basic 最基本的注释。有两个属性：fetch是否延迟加载，optional是否允许null
// @Enumerated 枚举类型
// @Temporal 日期转换。默认转换Timestamp
// @Lob 通常与@Basic同时使用，提高访问速度。
// @Embeddable 类级，表可嵌入的
// @Embedded 方法字段级，表被嵌入的对象和@Embeddable一起使用
// @AttributeOverrides 属性重写
// @AttributeOverride 属性重写的内容和@AttributeOverrides一起嵌套使用
// @SecondaryTables 多个表格映射
// @SecondaryTable 定义辅助表格映射和@SecondaryTables一起嵌套使用
// @GeneratedValue 标识符生成策略，默认Auto
// JPA 基类的标识
// @AttributeOverride(name = "id", column = @Column(name = "base_id"))
public class BaseAutoEntity implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2142201445199112425L;

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String TIME_FORMAT = "HH:mm:ss";

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	public static final String TIMEZONE = "GMT+08:00";

	/**
	 * 主键ID
	 */
	protected Long id;

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

	/**
	 * 记录状态标志位 正常(0) 已删除(1) 待审核(2) 锁定(3)
	 */
	protected Integer status = 0;
	/**
	 * 操作版本(乐观锁,用于并发控制)
	 */
	protected Integer version;

	/**
	 * 记录创建者用户登录名
	 */
	protected String createUser;
	/**
	 * 记录创建时间
	 */
	protected Date createTime;

	/**
	 * 记录更新用户 用户登录名
	 */
	protected String updateUser;
	/**
	 * 记录更新时间
	 */
	protected Date updateTime;

	public BaseAutoEntity() {
		super();
	}

	/**
	 * 状态标志位
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 状态描述
	 */
	public String getStatusView() {
		String str = "";
		return str;
	}

	/**
	 * 设置 状态标志位
	 * 
	 * @param status
	 *            状态标志位
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 版本号(乐观锁)
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 设置 版本号(乐观锁)
	 * 
	 * @param version
	 *            版本号(乐观锁)
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 记录创建者 用户登录名
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 设置 记记录创建者 用户登录名
	 * 
	 * @param createUser
	 *            用户登录名
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * 记录创建时间.
	 */
	// 设定JSON序列化时的日期格式
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置 记录创建时间
	 * 
	 * @param createTime
	 *            记录创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 记录更新用户 用户登录名
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 设置 记录更新用户 用户登录名
	 * 
	 * @param updateUser
	 *            用户登录名
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * 记录更新时间
	 */
	// 设定JSON序列化时的日期格式
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置 记录更新时间
	 * 
	 * @param updateTime
	 *            记录更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BaseAutoEntity clone() {
		BaseAutoEntity o = null;
		try {
			o = (BaseAutoEntity) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
