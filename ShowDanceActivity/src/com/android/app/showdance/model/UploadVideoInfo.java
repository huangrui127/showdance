package com.android.app.showdance.model;

import java.io.File;

import com.android.app.showdance.entity.PageEntity;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * 
 * @ClassName: UploadVideoInfo
 * @Description: 上传视频信息
 * @author maminghua
 * @date 2015-7-6 下午6:41:54
 * 
 */
public class UploadVideoInfo extends PageEntity implements Comparable<UploadVideoInfo> {

	@Transient
	private HttpHandler<File> handler;

	private HttpHandler.State state;

	private String fileName;

	private String fileSavePath;

	private long progress;

	private long fileLength;

	private boolean autoResume;

	private boolean autoRename;

	private String name;

	private String author;

	private String fileNewName;

	private String userName;

	private String downloadCount;

	private String mid;

	private String createUser;

	private String createTime;

	private Long progressCount = (long) 0; // 总大小
	private Long currentProgress = (long) 0;// 当前进度
	private Integer downloadState = 0; // 下载状态
	private boolean editState;// 是否是编辑状态
	private Bitmap movieHeadImage;
	private String movieHeadImagePath;// 电影图片的路径
	private String fileSize;// 电影大小
	private String movieName;// 电影名称
	private String downloadUrl; // 下载地址
	private String setCount;// 为第几集
	private DownloadFile downloadFile; // 下载控制器
	private String percentage = "%0"; // 下载百分比的字符串
	private Long uuid; // 下载任务的标识
	private String filePath; // 视频文件存储路径
	private String fileBgPath; // 视频截图存储路径
	private boolean isSelected; // 选中状态
	private String movieId;
	// private boolean existDwonloadQueue;//是否身在下载队列中

	private int uploadState = 0; // 上传成功状态(0：未上传 1：已上传)

	public UploadVideoInfo() {

	}

	public HttpHandler<File> getHandler() {
		return handler;
	}

	public void setHandler(HttpHandler<File> handler) {
		this.handler = handler;
	}

	public HttpHandler.State getState() {
		return state;
	}

	public void setState(HttpHandler.State state) {
		this.state = state;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSavePath() {
		return fileSavePath;
	}

	public void setFileSavePath(String fileSavePath) {
		this.fileSavePath = fileSavePath;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(long progress) {
		this.progress = progress;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public boolean isAutoResume() {
		return autoResume;
	}

	public void setAutoResume(boolean autoResume) {
		this.autoResume = autoResume;
	}

	public boolean isAutoRename() {
		return autoRename;
	}

	public void setAutoRename(boolean autoRename) {
		this.autoRename = autoRename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFileNewName() {
		return fileNewName;
	}

	public void setFileNewName(String fileNewName) {
		this.fileNewName = fileNewName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UploadVideoInfo))
			return false;

		UploadVideoInfo that = (UploadVideoInfo) o;

		if (id != that.id)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	public Long getProgressCount() {
		return progressCount;
	}

	public void setProgressCount(Long progressCount) {
		this.progressCount = progressCount;
	}

	public Long getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(Long currentProgress) {
		this.currentProgress = currentProgress;
	}

	public Integer getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(Integer downloadState) {
		this.downloadState = downloadState;
	}

	public boolean isEditState() {
		return editState;
	}

	public void setEditState(boolean editState) {
		this.editState = editState;
	}

	public Bitmap getMovieHeadImage() {
		return movieHeadImage;
	}

	public void setMovieHeadImage(Bitmap movieHeadImage) {
		this.movieHeadImage = movieHeadImage;
	}

	public String getMovieHeadImagePath() {
		return movieHeadImagePath;
	}

	public void setMovieHeadImagePath(String movieHeadImagePath) {
		this.movieHeadImagePath = movieHeadImagePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getSetCount() {
		return setCount;
	}

	public void setSetCount(String setCount) {
		this.setCount = setCount;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileBgPath() {
		return fileBgPath;
	}

	public void setFileBgPath(String filePath) {
		this.fileBgPath = filePath;
	}
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public DownloadFile getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(DownloadFile downloadFile) {
		this.downloadFile = downloadFile;
	}

	public int getUploadState() {
		return uploadState;
	}

	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	@Override
	public String toString() {
		return "UploadVideoInfo [handler=" + handler + ", state=" + state + ", fileName=" + fileName + ", fileSavePath=" + fileSavePath + ", progress=" + progress + ", fileLength=" + fileLength
				+ ", autoResume=" + autoResume + ", autoRename=" + autoRename + ", name=" + name + ", author=" + author + ", fileNewName=" + fileNewName + ", userName=" + userName
				+ ", downloadCount=" + downloadCount + ", mid=" + mid + ", createUser=" + createUser + ", createTime=" + createTime + ", progressCount=" + progressCount + ", currentProgress="
				+ currentProgress + ", downloadState=" + downloadState + ", editState=" + editState + ", movieHeadImage=" + movieHeadImage + ", movieHeadImagePath=" + movieHeadImagePath
				+ ", fileSize=" + fileSize + ", movieName=" + movieName + ", downloadUrl=" + downloadUrl + ", setCount=" + setCount + ", downloadFile=" + downloadFile + ", percentage=" + percentage
				+ ", uuid=" + uuid + ", filePath=" + filePath + ", isSelected=" + isSelected + ", movieId=" + movieId + ", uploadState=" + uploadState + "]";
	}


	@Override
	public int compareTo(UploadVideoInfo another) {
		String ltime = createTime;
		String rtime = another.getCreateTime();
		
		if(TextUtils.isEmpty(ltime))
			return 1;
		if(TextUtils.isEmpty(rtime))
			return -1;
		return rtime.compareToIgnoreCase(ltime);
	}
}
