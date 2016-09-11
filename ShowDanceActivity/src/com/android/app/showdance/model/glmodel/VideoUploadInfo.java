package com.android.app.showdance.model.glmodel;

import java.util.List;



public class VideoUploadInfo {
	
	 public static class Request {
		 private String name;
		 private int  user_id;
		 private String video_name;
		 private String path;
		 public void setpath(String arg) {
			 path = arg;
		 }
		 public String getpath() {
			 return path;
		 }
		 public void setname(String arg) {
			 name = arg;
		 }
		 public String getname() {
			 return name;
		 }
		 public void setvideo_name(String arg) {
			 video_name = arg;
		 }
		 public String getvideo_name() {
			 return video_name;
		 }
		 public void setuser_id(int arg) {
			 user_id = arg;
		 }
		 public int getuser_id() {
			 return user_id;
		 }
	 }
	 
	 public static class Response {

			protected boolean flag;
			protected String message;
			protected VideoUploadResponse data;
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
			public void setData(VideoUploadResponse d) {
				data = d;
			}
			public VideoUploadResponse getData() {
				return data;
			}

	 }
	 
	 public static class VideoUploadResponse {
		
		 private String videoname;
		 private String updated_at;
		 private String path;
		 protected String created_at;
		 protected int  id;
		 protected int  count;
		 protected int  share_count;
		 protected String img;
		 protected String name;
		 protected String user_id;
		 protected String province;
		 
		 public void setuser_id(String arg) {
			 user_id = arg;
		 }
		 public String getuser_id() {
			 return user_id;
		 }
		 public void setprovince(String arg) {
			 province = arg;
		 }
		 public String getprovince() {
			 return province;
		 }
		 public void setname(String arg) {
			 name = arg;
		 }
		 public String getname() {
			 return name;
		 }
		 public void setimg(String arg) {
			 img = arg;
		 }
		 public String getimg() {
			 return img;
		 }
		 
		 public void setcount(int arg) {
			 count = arg;
		 }
		 public int getcount() {
			 return count;
		 }
		 
		 public void setshare_count(int arg) {
			 share_count = arg;
		 }
		 public int getshare_count() {
			 return share_count;
		 }
		 
		 
		 public void setpath(String arg) {
			 path = arg;
		 }
		 public String getpath() {
			 return path;
		 }
		 public void setcreated_at(String arg) {
			 created_at = arg;
		 }
		 public String getcreated_at() {
			 return created_at;
		 }
		 public void setid(int arg) {
			 id = arg;
		 }
		 public int getid() {
			 return id;
		 }
			public void setUpdated_at(String _updated_at) {
				updated_at = _updated_at;
			}

			public String getUpdated_at() {
				return updated_at;
			}
		 public void setvideoname(String arg) {
			 videoname = arg;
		 }
		 public String getvideoname() {
			 return videoname;
		 }
	 }
		 
		 public static class ShowRequest {
			 private int pageNumber;
			 private int  user_id;
			 
			 public void setpageNumber(int arg) {
				 pageNumber = arg;
			 }
			 public int getpageNumber() {
				 return pageNumber;
			 }
			 public void setuser_id(int arg) {
				 user_id = arg;
			 }
			 public int getuser_id() {
				 return user_id;
			 }
		 }
		 
		 public static class DeleteRequest {
			 private int  id;
			 
			 public void setid(int arg) {
				 id = arg;
			 }
			 public int getid() {
				 return id;
			 }
		 }
		 
		 public static class DeleteResponse {
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
		 public static class ShowResponse {
				protected boolean flag;
				protected String message;
				protected ShowListResponse data;
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
				public void setData(ShowListResponse d) {
					data = d;
				}
				public ShowListResponse getData() {
					return data;
				}

		 
		 }
		 
		 public static class ShowListResponse  {
			 
			 private int  current_page;
			 private int last_page;
			 private int  from;
			 private int  to;
			 private List<VideoUploadResponse> data;
			 private String next_page_url;
			 private String prev_page_url;
			 private int per_page;
			 private int total;
			 
			 public void setcurrent_page(int arg) {
				 current_page = arg;
			 }
			 public int getcurrent_page() {
				 return current_page;
			 }
			 public void settotal(int arg) {
				 total = arg;
			 }
			 public int gettotal() {
				 return total;
			 }
			 public void setlast_page(int arg) {
				 last_page = arg;
			 }
			 public int getlast_page() {
				 return last_page;
			 }
			 public void setfrom(int arg) {
				 from = arg;
			 }
			 public int getfrom() {
				 return from;
			 }
			 
			 public void setto(int arg) {
				 to = arg;
			 }
			 public int getto() {
				 return to;
			 }
			 
			 public void setdata(List<VideoUploadResponse> arg) {
				 data = arg;
			 }
			 public List<VideoUploadResponse> getdata() {
				 return data;
			 }
			 public void setper_page(int arg) {
				 per_page = arg;
			 }
			 public int getper_page() {
				 return per_page;
			 }
			 public void setnext_page_url(String arg) {
				 next_page_url = arg;
			 }
			 public String getnext_page_url() {
				 return next_page_url;
			 }
			 public void setprev_page_url(String arg) {
				 prev_page_url = arg;
			 }
			 public String getprev_page_url() {
				 return prev_page_url;
			 }
	 }
}