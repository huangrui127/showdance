package com.android.app.showdance.model.glmodel;

public class TeacherInfo{
public static class Request  extends BaseRequest {
	private String pageNum = String.valueOf(Integer.MAX_VALUE);
	
	public Request() {
		super();
		token = STAR_TEACHER_LIST_TOKEN;
	}
	
	public void setpageNum(String keyword) {
		pageNum = keyword;
	}
	public String getpageNum() {
		return pageNum;
	}
}
public static class Search {
private String teacher;
public void setTeacher(String _teacher) {
	teacher = _teacher;
}

public String getTeacher() {
	return teacher;
}
}
public static class Response extends BaseResponse<Search>{

}
}