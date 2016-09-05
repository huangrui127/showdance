package com.android.app.showdance.model.glmodel;

import java.io.Serializable;
import java.util.List;

public class FrameInfo {

	public static class Request  extends BaseRequest {
		private String name;
		protected int pageNum;
		
		public Request() {
			super();
			token = GIVEN_FRAME_TOKEN;
		}
		
		public Request(String _name) {
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
		}
	}
	
	public static class Response {
		protected boolean flag;
		protected String message;
		protected FrameData data;
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
		public void setData(FrameData d) {
			data = d;
		}
		public FrameData getData() {
			return data;
		}
	}
	
	public static class FrameData {
		private String current_page;
		private String from;
		private String last_page;
		private String next_page_url;
		private String per_page;
		private String prev_page_url;
		private String to;
		private String total;
		private List<Frame> data; 
		public void setcurrent_page(String current_page) {
			this.current_page = current_page;
		}

		public String getcurrent_page() {
			return this.current_page;
		}
		public void setfrom(String from) {
			this.from = from;
		}

		public String getfrom() {
			return this.from;
		}
		public void setlast_page(String last_page) {
			this.last_page = last_page;
		}

		public String getlast_page() {
			return this.last_page;
		}
		public void setnext_page_url(String next_page_url) {
			this.next_page_url = next_page_url;
		}

		public String getnext_page_url() {
			return this.next_page_url;
		}
		public void setper_page(String per_page) {
			this.per_page = per_page;
		}

		public String getper_page() {
			return this.per_page;
		}
		public void setprev_page_url(String prev_page_url) {
			this.prev_page_url = prev_page_url;
		}

		public String getprev_page_url() {
			return this.prev_page_url;
		}
		public void setto(String to) {
			this.to = to;
		}

		public String getto() {
			return this.to;
		}
		public void settotal(String total) {
			this.total = total;
		}

		public String gettotal() {
			return this.total;
		}
		public void setdata(List<Frame> data) {
			this.data = data;
		}

		public List<Frame> getdata() {
			return this.data;
		}
	}
	
	
	public static class AvatorRequest  {
		protected int type;
		
		public AvatorRequest() {
		}
		
		public AvatorRequest(int type) {
			this.type=type;
		}

		
		public void settype(int type) {
			this.type = type;
		}
		public int gettype() {
			return type;
		}
	}
	
	public static class AvatorResponse {
		protected boolean flag;
		protected String message;
		protected List<AvatorFrame> data;
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
		public void setData(List<AvatorFrame> d) {
			data = d;
		}
		public List<AvatorFrame> getData() {
			return data;
		}
	}
	
	public static class AvatorFrame extends Frame {
		protected int mask_type;

		public void setmask_type(int mask_type) {
			this.mask_type=mask_type;
		}
		public int getmask_type() {
			return this.mask_type;
		}
	}
	
	public static class Frame implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected String created_at;
		protected int id;
		protected int frame_type;
		protected String img;
		protected int sort;
		protected String name;
		protected String teacher;
		protected String  updated_at;
		protected String url;
		protected String filename;
		protected int type;
		
		@Override
		public String toString() {
			return "created_at "+created_at+"id "+id+
					" frame_type "+frame_type+
					" img "+img+
					" sort "+sort+
					" name "+name+
					" teacher "+teacher+
					" updated_at "+updated_at+
					" url "+url+
					" filename "+filename+
					" type "+type;
		}
		
		public void setframe_type(int frame_type) {
			this.frame_type=frame_type;
		}
		public int getframe_type() {
			return this.frame_type;
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
		public void setteacher(String teacher) {
			this.teacher=teacher;
		}
		public String getteacher() {
			return this.teacher;
		}
		public void setsort(int sort) {
			this.sort=sort;
		}
		public int getsort() {
			return this.sort;
		}
		public void settype(int type) {
			this.type=type;
		}
		public int gettype() {
			return this.type;
		}
		public void seturl(String url) {
			this.url=url;
		}
		public String geturl() {
			return this.url;
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
		
		public void setFilename(String filename) {
			this.filename=filename;
		}
		public String getFilename() {
			return filename;
		}
		
		public void setupdated_at(String updated_at) {
			this.updated_at=updated_at;
		}
		public String getupdated_at() {
			return updated_at;
		}
	}
}