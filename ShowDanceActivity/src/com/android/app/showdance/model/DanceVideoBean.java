package com.android.app.showdance.model;

/**
 * 功能：一个视频文件Bean
 * 
 * @author djd
 */
public class DanceVideoBean {

    private String mImgSrc; // 图片路径（视频截图）
    private int mRank; // 视频排名
    private int mFlower; // 鲜花数
    private String mVideoTitle; // 视频标题
    private String mCreatedTime; // 视频的上传时间（用于计算显示在“最新视频”中的多长时间前上传）
    private int mPlayCount; // 播放次数
    private int mShareCount; // 分享次数
    private String mVideoUrl; // 视频URL地址

    public DanceVideoBean(){
        
    }
    
    public DanceVideoBean(String imgSrc, int rank, int flower,String videoTitle, String createdTime, int playCount, int shareCount, String videoUrl) {
        super();
        mImgSrc = imgSrc;
        mRank = rank;
        mFlower = flower;
        mVideoTitle = videoTitle;
        mCreatedTime = createdTime;
        mPlayCount = playCount;
        mShareCount = shareCount;
        mVideoUrl = videoUrl;
    }

    public String getImgSrc() {
        return mImgSrc;
    }

    public void setImgSrc(String imgSrc) {
        mImgSrc = imgSrc;
    }

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        mRank = rank;
    }

    public int getFlower() {
        return mFlower;
    }

    public void setFlower(int mFlower) {
        this.mFlower = mFlower;
    }

    public String getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        mVideoTitle = videoTitle;
    }

    
    public String getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        this.mCreatedTime = createdTime;
    }

    public int getPlayCount() {
        return mPlayCount;
    }

    public void setPlayCount(int playCount) {
        mPlayCount = playCount;
    }

    public int getShareCount() {
        return mShareCount;
    }

    public void setShareCount(int shareCount) {
        mShareCount = shareCount;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

}
