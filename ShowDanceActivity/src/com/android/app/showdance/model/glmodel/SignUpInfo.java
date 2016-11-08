package com.android.app.showdance.model.glmodel;

import java.io.Serializable;

public class SignUpInfo  {
	public static class GetCodeRequest  extends BaseRequest {
		protected String phoneNumber;
		
		public GetCodeRequest() {
			super();
			token = AUTHOR_TOKEN;
		}
		
		public GetCodeRequest(String phone) {
			this.phoneNumber = phone;
			token = AUTHOR_TOKEN;
		}

		public void setphoneNumber(String phone) {
			this.phoneNumber = phone;
		}
		public String getphoneNumber() {
			return phoneNumber;
		}
	}
	
	public static class SignUpRequest  extends GetCodeRequest {
		private String code;
		
		public SignUpRequest() {
			super();
			token = SIGN_UP_TOKEN;
		}
		
		public SignUpRequest(String code,String phone) {
			this.code = code;
			this.phoneNumber = phone;
			token = SIGN_UP_TOKEN;
		}

		public void setCode(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}
	public static class GetCodeResponse {
		protected boolean flag;
		protected String message;
		protected String data;
		public void setFlag(boolean f) {
			flag = f;
		}
		public boolean getFlag() {
			return flag;
		}
		public void setMessage(String ms) {
			message = ms;
		}
		public String getMessage() {
			return message;
		}
		public void setData(String d) {
			data = d;
		}
		public String getData() {
			return data;
		}
	}
	
//	public static class RegCode {
//		private String code;
//		public void setCode(String d) {
//			code = d;
//		}
//		public String getCode() {
//			return code;
//		}
//	}
	
	public static class SignUpResponse  {
		protected boolean flag;
		protected String message;
		protected Code data;
		public void setFlag(boolean f) {
			flag = f;
		}
		public boolean getFlag() {
			return flag;
		}
		public void setMessage(String ms) {
			message = ms;
		}
		public String getMessage() {
			return message;
		}
		public void setData(Code d) {
			data = d;
		}
		public Code getData() {
			return data;
		}
	}
	
	public static class Code {
		private User user;
		private String user_token;
		public void setuser_token(String user_token) {
			this.user_token = user_token;
			if(user !=null)
				user.setuser_token(user_token);
		}

		public String getuser_token() {
			return user_token;
		}
		
		public void setuser(User user) {
			this.user = user;
			if(user_token !=null)
				user.setuser_token(user_token);
		}

		public User getuser() {
			return user;
		}
	}
	
	public static class User implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String created_at;
		private int id;
		private String user_token;
		private String phone;
		private String updated_at;
		private String starttime;
		private String endtime;
		private String password;
		private String day;
		private String status;
		
		public void setstatus(String token) {
			status = token;
		}
		
		public String gestatus() {
			return status;
		}
		public void setuser_token(String token) {
			user_token = token;
		}
		
		public String getuser_token() {
			return user_token;
		}
		public void setcreated_at(String created_at) {
			this.created_at=created_at;
		}
		public String getCreated_at() {
			return created_at;
		}
		public void setId(int id) {
			this.id=id;
		}
		public int getId() {
			return id;
		}
		public void setPhone(String phone) {
			this.phone=phone;
		}
		public String getPhone() {
			return phone;
		}
		public void setupdated_at(String updated_at) {
			this.updated_at=updated_at;
		}
		public String getupdated_at() {
			return updated_at;
		}
		
		public void setstarttime(String starttime) {
			this.starttime=starttime;
		}
		public String getstarttime() {
			return starttime;
		}
		public void setendtime(String endtime) {
			this.endtime=endtime;
		}
		public String getendtime() {
			return endtime;
		}
		public void setpassword(String password) {
			this.password=password;
		}
		public String getpassword() {
			return password;
		}
		
		public void setDay(String day) {
			this.day=day;
		}
		public String getDay() {
			return day;
		}
	}
}