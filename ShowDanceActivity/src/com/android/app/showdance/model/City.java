package com.android.app.showdance.model;

import java.io.Serializable;

public class City implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String province;
	private String city;
	private String number;
	private String firstPY;
	private String allPY;
	private String allFristPY;
	private String provincePY;

	public City() {

	}

	public City(int id, String province, String city, String number, String firstPY, String allPY, String allFristPY, String provincePY) {
		super();
		this.id = id;
		this.province = province;
		this.city = city;
		this.number = number;
		this.firstPY = firstPY;
		this.allPY = allPY;
		this.allFristPY = allFristPY;
		this.provincePY = provincePY;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFirstPY() {
		return firstPY;
	}

	public void setFirstPY(String firstPY) {
		this.firstPY = firstPY;
	}

	public String getAllPY() {
		return allPY;
	}

	public void setAllPY(String allPY) {
		this.allPY = allPY;
	}

	public String getAllFristPY() {
		return allFristPY;
	}

	public void setAllFristPY(String allFristPY) {
		this.allFristPY = allFristPY;
	}

	public String getProvincePY() {
		return provincePY;
	}

	public void setProvincePY(String provincePY) {
		this.provincePY = provincePY;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", province=" + province + ", city=" + city + ", number=" + number + ", firstPY=" + firstPY + ", allPY=" + allPY + ", allFristPY=" + allFristPY + ", provincePY="
				+ provincePY + "]";
	}

}
