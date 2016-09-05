package com.android.app.showdance.model;

import com.android.app.showdance.entity.BaseAutoEntity;

//@MappedSuperclass
public class UserInfo extends BaseAutoEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 登录用户（手机号或吧号）
	 */
	private String loginName; // 手机号/秀舞吧号
	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 用户姓名(艺名)
	 */
	private String name; // 艺名
	/**
	 * 性别 女(0) 男(1) 保密(2) 默认：保密
	 */
	private Integer sex = 0;
	/**
	 * 用户出生日期
	 */
	private String birthday;

	/**
	 * 头像
	 */
	private String photo;

	/**
	 * 邮件 以 ","分割
	 */
	private String email;

	/**
	 * 经度
	 */
	private String longitude;

	/**
	 * 纬度
	 */
	private String latitude;

	/**
	 * 住址
	 */
	private String address;

	/**
	 * 住宅电话 以 ","分割
	 */
	private String tel;

	/**
	 * 手机号 以 ","分割
	 */
	private String mobilephone;

	private Long cityId;

	/**
	 * 排序
	 */
	private Integer orderNo;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 个性签名
	 */
	private String signature;

	/**
	 * 照片墙壁纸
	 */
	private String wallpaper;

	/**
	 * 区域id
	 */
	private Integer regionalId;

	/**
	 * 用户类型 0系统管理员；1用户
	 */
	private Integer userType;

	private String token;

	// @Column(name = "LOGIN_NAME",length = 36, nullable = false)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	// @Column(name = "NAME",length = 36)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// @Column(name = "PASSWORD",length = 64, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// @Column(name = "SEX")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * 性别描述.
	 */
	// @Transient
	public String getSexView() {
		String str = "";
		return str;
	}

	// @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE)
	// @Column(name = "BIRTHDAY")
	// @Temporal(TemporalType.TIMESTAMP)
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	// @Column(name = "PHOTO",length = 1000)
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	// @Column(name = "EMAIL",length = 64)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// @Column(name = "ADDRESS",length = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// @Column(name = "TEL",length = 36)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	// @Column(name = "MOBILEPHONE",length = 36)
	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	// @Column(name = "ORDER_NO")
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	// @Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(String wallpaper) {
		this.wallpaper = wallpaper;
	}

	public Integer getRegionalId() {
		return regionalId;
	}

	public void setRegionalId(Integer regionalId) {
		this.regionalId = regionalId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

}
