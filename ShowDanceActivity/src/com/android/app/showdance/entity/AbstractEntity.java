/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.android.app.showdance.entity;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 抽象实体基类.
 * @author ereal
 * @date 2013-6-5 下午5:05:56 
 * @version 1.0
 * @param <ID>
 */
public abstract class AbstractEntity<ID extends Serializable> implements Serializable{

    /**
	* @Fields serialVersionUID : 
	*/
	
	private static final long serialVersionUID = 4482129712193418272L;

	public abstract ID getId();

    /**
     * 设置主键ID.
     *
     * @param id 主键ID
     */
    public abstract void setId(final ID id);

    /**
     * 是否是新创建的对象.
     * @return
     */
    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractEntity<?> that = (AbstractEntity<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

 
}