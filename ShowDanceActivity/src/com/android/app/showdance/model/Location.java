package com.android.app.showdance.model;

public class Location {
	private String Time;
	private int LocType;
	private double Latitude;
	private double Longitude;
	private float Radius;
	private float Speed;
	private int SatelliteNumber;
	private String AddrStr;
	private float Direction;
	private int Operators;

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public int getLocType() {
		return LocType;
	}

	public void setLocType(int locType) {
		LocType = locType;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public float getRadius() {
		return Radius;
	}

	public void setRadius(float radius) {
		Radius = radius;
	}

	public float getSpeed() {
		return Speed;
	}

	public void setSpeed(float speed) {
		Speed = speed;
	}

	public int getSatelliteNumber() {
		return SatelliteNumber;
	}

	public void setSatelliteNumber(int satelliteNumber) {
		SatelliteNumber = satelliteNumber;
	}

	public String getAddrStr() {
		return AddrStr;
	}

	public void setAddrStr(String addrStr) {
		AddrStr = addrStr;
	}

	public float getDirection() {
		return Direction;
	}

	public void setDirection(float direction) {
		Direction = direction;
	}

	public int getOperators() {
		return Operators;
	}

	public void setOperators(int operators) {
		Operators = operators;
	}
}
