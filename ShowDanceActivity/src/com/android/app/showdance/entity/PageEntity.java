package com.android.app.showdance.entity;

public class PageEntity extends AutoEntity {

	protected int pageNo = 1;// 页码
	protected int pageSize = 10;// 每页条数
	protected String totalPage;// 总页数
	protected String orderBy = null;// 排序字段

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

}
