package com.android.app.showdance.model;

import java.io.File;
import java.io.Serializable;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.utils.FileUtil;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;
import com.android.app.showdance.model.glmodel.MusicInfo.*;
import com.android.app.wumeiniang.app.InitApplication;

/**
 * Author: wyouflf Date: 13-11-10 Time: 下午8:11
 */
public class DownloadMusicInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8130508311582513721L;

	@Transient
	private HttpHandler<File> handler;

	private HttpHandler.State state;


	private String fileSavePath;

	private int progress;

	private Long progressCount = (long) 0; // 总大小
	private Long currentProgress = (long) 0;// 当前进度
	private Integer downloadState = ContentValue.DOWNLOAD_STATE_SUSPEND; // 下载状态
	private boolean editState;// 是否是编辑状态
	private Bitmap movieHeadImage;
	private String movieHeadImagePath;// 电影图片的路径
	private String movieName;// 电影名称
	private String setCount;// 为第几集
	private DownloadFile downloadFile; // 下载控制器
	private Long uuid; // 下载任务的标识
	private String filePath; // 存储路径
	private boolean isSelected; // 选中状态
	private String movieId;
	// private boolean existDwonloadQueue;//是否身在下载队列中
	private String trackLength;
	private String downloadAction;
	private MusicSearchResponse mMusicSearchResponse;

	public DownloadMusicInfo(MusicSearchResponse resp) {
		mMusicSearchResponse = resp;
		String tmpname = getMusic().getUrl();
		movieName = tmpname.substring(tmpname.lastIndexOf("/")+1);
		if(FileUtil.isFileExist(InitApplication.SdCardMusicPath,movieName)) {
			downloadState = ContentValue.DOWNLOAD_STATE_SUCCESS;
			SharedPreferences sp = InitApplication.mSpUtil.getMusicSp();
			String savemap3name = sp.getString(movieName, null);
			if(savemap3name==null){
				String lrcname = getMusic().getLrc();
				final String lrcurl = lrcname.substring(lrcname.lastIndexOf("/")+1);
				sp.edit().putString(movieName,lrcurl+"_"+getName()+"_"+getMusic().getTeacher()).commit();
			}
		}
	}

	public MusicSearchResponse getMusic() {
		return mMusicSearchResponse;
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
		return  mMusicSearchResponse.getUrl();
	}

	public String getFileName() {
		return getName();
	}
	
	public void setFileName() {
		;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getName() {
		return mMusicSearchResponse.getName();
	}


	public String getAuthor() {
		return mMusicSearchResponse.getSinger();
	}

	public int getDownloadCount() {
		return mMusicSearchResponse.getDownload_volume();
	}


	public String getCreateTime() {
		return mMusicSearchResponse.getCreated_at();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DownloadMusicInfo))
			return false;

		DownloadMusicInfo that = (DownloadMusicInfo) o;

		if (mMusicSearchResponse.getId() != that.getMusic().getId())
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return (int) (mMusicSearchResponse.getId() ^ (mMusicSearchResponse.getId() >>> 32));
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
		return mMusicSearchResponse.getSize();
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

	public int getPercentage() {
		return progress;
	}

	public void setPercentage(int percentage) {
		this.progress = percentage;
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

	public String getTrackLength() {
		return trackLength;
	}

	public void setTrackLength(String trackLength) {
		this.trackLength = trackLength;
	}

//	@Override
//	public String toString() {
//		return "DownloadMusicInfo [id=" + id + ", handler=" + handler + ", state=" + state + ", fileName=" + fileName + ", fileSavePath=" + fileSavePath + ", progress=" + progress + ", fileLength="
//				+ fileLength + ", autoResume=" + autoResume + ", autoRename=" + autoRename + ", name=" + name + ", author=" + author + ", fileNewName=" + fileNewName + ", userName=" + userName
//				+ ", downloadCount=" + downloadCount + ", mid=" + mid + ", createUser=" + createUser + ", createTime=" + createTime + ", progressCount=" + progressCount + ", currentProgress="
//				+ currentProgress + ", downloadState=" + downloadState + ", editState=" + editState + ", movieHeadImage=" + movieHeadImage + ", movieHeadImagePath=" + movieHeadImagePath
//				+ ", fileSize=" + fileSize + ", movieName=" + movieName + ", downloadUrl=" + downloadUrl + ", setCount=" + setCount + ", downloadFile=" + downloadFile + ", percentage=" + percentage
//				+ ", uuid=" + uuid + ", filePath=" + filePath + ", isSelected=" + isSelected + ", movieId=" + movieId + "]";
//	}

	public String getDownloadAction() {
		return downloadAction;
	}

	public void setDownloadAction(String downloadAction) {
		this.downloadAction = downloadAction;
	}

	public String getFileSavePath() {
		return fileSavePath;
	}

	public void checkstate() {
		if (downloadState !=ContentValue.DOWNLOAD_STATE_SUCCESS)
			return;
		if(FileUtil.isFileExist(InitApplication.SdCardMusicPath,movieName)) {
			downloadState = ContentValue.DOWNLOAD_STATE_SUCCESS;
		}else {
			downloadState = ContentValue.DOWNLOAD_STATE_SUSPEND;
		}
	}
}
