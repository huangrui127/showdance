package com.android.app.showdance.model.glmodel;

import java.io.Serializable;
import java.util.List;

public class FontInfo {

	public static class Request  {
		
		public Request() {
		}
		
		/*public Request(String _name) {
			name = _name;
			token = GIVEN_FRAME_TOKEN;
		}

		public void setname(String keyword) {
			name = keyword;
		}
		public String getname() {
			return name;
		}
		
		public void setpageNum(int pageNum) {
			this.pageNum = pageNum;
		}
		public int getpageNum() {
			return pageNum;
		}*/
	}
	
	public static class Response {
		protected boolean flag;
		protected String message;
		protected List<FontData> data;
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
		public void setData(List<FontData> d) {
			data = d;
		}
		public List<FontData> getData() {
			return data;
		}
	}
	
	
	
	public static class FontData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected String created_at;
		protected int id;
		protected String img;
		protected String name;
		protected String  updated_at;
		protected String path;
		protected int type;
		protected int count;
		
		@Override
		public String toString() {
			return "created_at "+created_at+"id "+id+
					" img "+img+
					" count "+count+
					" name "+name+
					" updated_at "+updated_at+
					" filename "+path+
					" type "+type;
		}
		
		public void setimg(String img) {
			this.img=img;
		}
		public String getimg() {
			return this.img;
		}
		public void setname(String name) {
			this.name=name;
		}
		public String getname() {
			return this.name;
		}
		public void setpath(String path) {
			this.path=path;
		}
		public String getpath() {
			return this.path;
		}
		public void setcount(int count) {
			this.count=count;
		}
		public int getcount() {
			return this.count;
		}
		public void settype(int type) {
			this.type=type;
		}
		public int gettype() {
			return this.type;
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
		
		public void setupdated_at(String updated_at) {
			this.updated_at=updated_at;
		}
		public String getupdated_at() {
			return updated_at;
		}
	}
}