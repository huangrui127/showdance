package com.android.app.showdance.utils;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @ClassName: ConstantsUtil
 * @Description: 常量文字工具类
 * @author maminghua
 * @date 2014-11-18 下午05:28:42
 * 
 */
public class ConstantsUtil {

    public static boolean NetworkStatus;// 各网络接口判断网络状态

    public static final File RootDirectory = Environment.getExternalStorageDirectory();// SD卡根目录
    public static final String SdCardImagePath = "/ShowDance/ImageCaches/";// 图片缓存目录
    public static final String SdCardMusicPath = "/ShowDance/MusicCaches/";// 下载舞曲保存目录
    public static final String SdCardSubtitlePath = "/ShowDance/SubtitleCaches/";// 下载字幕保存目录
    public static final String SdCardLrcPath = "/ShowDance/LrcCaches/";// 下载舞曲的歌词保存目录(原歌词目录)
    public static final String sdCardForegroundPath = "/ShowDance/foreground/";
    public static final String sdCardAvator2ForegroundPath = "/ShowDance/foreground2/";
    public static final String sdCardAvator3ForegroundPath = "/ShowDance/foreground3/";
    public static final String SdCardNewLrcPath = "/ShowDance/newLrcPath/";// 下载舞曲的歌词保存目录(移动并重命名后歌词目录)
    public static final String SdCardDownloadVideoPath = "/ShowDance/DownloadVideoCaches/";// 下载视频保存目录
    public static final String SdCardRecordedVideoPath = "/ShowDance/RecordedVideoCaches/";// 输出拍摄转码后的视频保存目录
    public static final String SdCardLrcInfoPath = "/j/";// 合成视频使用舞曲的歌词信息目录

    public static final String WebServiceNameSpace = "http://webservice.ereal.com";// 命名空间

    public final static String HTTP = "http://";
    // public final static String HOST = "192.168.1.122:8090";// 本地局域网(测试环境地址)
    public final static String HOST = "112.74.83.166:8080";// 阿里云存储(正式环境地址)
    public final static String HOST_QINIU = "7xn1vo.com1.z0.glb.clouddn.com";// 七牛云存储(视频地址用于播放和下载)
    public final static String URL_SPLITTER = "/";
    public static final String WebSite = HTTP + HOST + URL_SPLITTER;

    public static final String WebSite_QINIU = HTTP + HOST_QINIU + URL_SPLITTER;

    public static final String PhotoUri = WebSite;// 照片访问前缀地址
    public static final String WebServiceUrl = WebSite + "dancebar/ws/danceBarWs?wsdl";// 接口地址
    public static final String HttpPostUrl = WebSite + "dancebar/upload";// http接口地址上传视频
    public static final String HttpUrl = WebSite + "dancebar/service/user/getUserInfoById";// http接口地址测试

    public static final String ShareContent = "秀舞吧";
    public static final String ShareContentFirst = "分享自 ";
    public static final String ShareContentEnd = " 的秀舞吧,一起来看~";
    public static final String Website = "http://cocodance.m.tmall.com/";// 天猫店主页

    public static final int videoType1 = 1;// 舞友列表视频
    public static final int videoType2 = 2;// 舞友详情视频
    public static final int videoType3 = 3;// 我的舞曲-详情视频

    public final static int refreshType = -1;
    public final static int refreshOnFirst = 0; // 首次接口调用
    public final static int refreshOnHeader = 1; // 下拉刷新
    public final static int refreshOnFooter = 2; // 上拉加载更多

    public final static int showDanceHome = 1; // 秀舞界面
    public final static int showDanceSearch = 2; // 搜索界面
    public final static int showDanceTeacher = 3; // 老师界面
    
    /**********************首页分类图标跳转***********************/
    public static final String CATEGORY = "category";
    /**********************视频播放相关***********************/
    public static final String HOT_VIDEO = "hotVideo";
    public static final String VIDEO_UPLOAD_RESPONSE = "VideoUploadResponse";
    
    public static final String VIDEO_TITLE = "title";
    public static final String VIDEO_PLAY_COUNT = "palyCount";
    public static final String VIDEO_SHARE_COUNT = "shareCount";

    public static final String IS_JOIN_IN_THE_ACTIVITY = "IsJoinInTheActivities"; // 是否参加活动
    public static final String VIDEO_FILE_PATH = "VIDEO_FILE_PATH";
    public static final String VIDEO_FILE_PATH_LOCAL = "VIDEO_FILE_PATH_LOCAL";
    public static final String VIDEO_FILE_NAME_LOCAL = "VIDEO_FILE_NAME_LOCAL";
    public static final String VIDEO_FILE_UPLOAD_STATE = "VIDEO_FILE_UPLOAD_STATE";
    
    /**
     * ******************用于视频上传获取token******************
     */
    public static final String ACCESS_KEY = "IdnpLX4PuTbgA5VnE4JtjuuII7ahmDNBhNxlZNly";
    public static final String SECRET_KEY = "O4t5RQJw-1PGWa8Wva9-TB_le3eRfzeiKkBwSvqR";

    /**
     * ******************微信分享密钥，切勿外泄******************
     */
    public static final String APP_ID = "wx8926439f1cc6aeae";
    public static final String APP_SECRET = "c167a9bcd8569be9ae05856190b2a2bb";

    /**
     * ******************高德定位密钥，切勿外传*************************
     */
    public static final String GAODE_SECRET_KEY = "f2941c07f4cc03a68b095128a22fbc13";
    
    // 【已调试】
    // 调用WebService的方法名称
    public static final String checkPhone = "checkPhone"; // 注册时发送验证码、修改手机号发送验证码、找回密码发送验证码
    public static final String checkCode = "checkCode"; // 注册时检验验证码、修改手机号
    public static final String registerUser = "registerUser"; // 注册
    public static final String login = "login"; // 登录
    public static final String modifyPwd = "modifyPwd"; // 修改密码
    public static final String modifyPwdByMobilephone = "modifyPwdByMobilephone"; // 找回密码--修改密码
    public static final String modifyUser = "modifyUser"; // 修改用户信息
    public static final String getUserInfoById = "getUserInfoById";
    public static final String getMediaInfoPageByCreateUser = "getMediaInfoPageByCreateUser";// 获取对应视频分页---对应“我的秀舞”
    public static final String deleteMediaInfo = "deleteMediaInfo";// 删除“我的秀舞”
    public static final String getDownloadMusicPagesByCreateUser = "getDownloadMusicPagesByCreateUser";// 获取下载舞曲分页
    public static final String getMediaInfoList = "getMediaInfoList";// 首页视频榜单
    public static final String getUserFansPagesByCreateUser = "getUserFansPagesByCreateUser";// 获取关注的用户分页---我关注的
    public static final String getUserFansPagesByUserId = "getUserFansPagesByUserId";// 获取粉丝用户分页----我的粉丝
    public static final String getDownloadMediaPagesByCreateUser = "getDownloadMediaPagesByCreateUser";// 获取下载视频分页---我的下载
    public static final String getVDanceMusicDancerList = "getVDanceMusicDancerList";// 获取明星老师列表

    public static final String saveDownloadMusic = "saveDownloadMusic";// 保存下载舞曲记录
    public static final String modifyRegional = "modifyRegional";// 修改登录用户所在城市

    // 【未调试】
    public static final String savePhotoInfo = "savePhotoInfo";// 发布动态
    public static final String getPhotoInfoPageByCreateUser = "getPhotoInfoPageByCreateUser";// 获取动态分页
    public static final String modifyUserWallpaper = "modifyUserWallpaper";// 修改照片墙
    public static final String getVUserFansMediaMusicCountById = "getVUserFansMediaMusicCountById";// 获取关注舞友、粉丝、下载视频、我的秀舞
                                                                                                   // 数量
    public static final String saveDanceTeam = "saveDanceTeam";// 新建舞队
    public static final String updateDanceTeam = "updateDanceTeam";// 修改舞队
    public static final String getDanceTeamUserByUserId = "getDanceTeamUserByUserId";// 查询所属舞队
    public static final String getVDanceTeamUserByDanceId = "getVDanceTeamUserByDanceId";// 获取舞队，队员数，前三条队员信息
    // public static final String getVDanceTeamUserByDanceId =
    // "getVDanceTeamUserByDanceId";// 获取舞队粉丝数，前五个粉丝
    public static final String getVMediaInfoDanceTeamByDanceTeamId = "getVMediaInfoDanceTeamByDanceTeamId";// 获取舞队视频，视频数，前N条视频信息
    public static final String saveDanceTeamUser = "saveDanceTeamUser";// 关注舞队
    public static final String saveDanceTeamUserApply = "saveDanceTeamUserApply";// 申请加入/退出
                                                                                 // 舞队
    public static final String getMediaInfoPageByDanceId = "getMediaInfoPageByDanceId";// 获取对应舞队视频分页
    public static final String getVDanceTeamUserPageByDanceId = "getVDanceTeamUserPageByDanceId";// 获取对应舞队
                                                                                                 // 队员/粉丝
                                                                                                 // 分页
    public static final String saveUserFans = "saveUserFans";// 关注用户
    public static final String deleteUserFans = "deleteUserFans";// 取消关注
    public static final String saveDownloadMedia = "saveDownloadMedia";// 保存下载视频记录
    public static final String saveMemberFeedback = "saveMemberFeedback";// 提交用户反馈
    public static final String getVDanceTeamUserPageByDanceName = "getVDanceTeamUserPageByDanceName";// 根据舞队名称，搜索舞队
    public static final String giveMyGoods = "giveMyGoods";// 花的接收对象是人，不是视频
    public static final String getSumMyGoods = "getSumMyGoods";// 获取鲜花数
    public static final String savePraise = "savePraise";// 点赞操作
    public static final String updatePlayCount = "updatePlayCount";// 播放次数+1
    public static final String getVDanceMusicByName = "getVDanceMusicByName";// 舞曲名/作者/队长
                                                                             // 搜索舞曲
                                                                             // 分页
    public static final String getVDanceTeamUserByFansUserId = "getVDanceTeamUserByFansUserId";// 获取我关注的舞队分页
    public static final String getVMediaInfoDanceTeamByCreateUser = "getVMediaInfoDanceTeamByCreateUser";// 获取对应上传的视频数量，视频信息
    public static final String saveShareInfo = "saveShareInfo";// 新增用户分享
    public static final String getMediaInfoById = "getMediaInfoById";// 查询视频详情和个人相关详情

    public static final String getMediaCommentListByMediaId = "getMediaCommentListByMediaId";// 获得视频评论
    public static final String saveMediaComment = "saveMediaComment";// 评论视频
    public static final String saveMediaInfo = "saveMediaInfo";// 上传视频
    public static final String pushUserFansListAfterSaveMedia = "pushUserFansListAfterSaveMedia";// 上传视频信息后推送给关注用户
    public static final String getMark = "getMark";// 获取上传凭证token

    /*----------------Activity跳转相关-----------------*/
    public static final String OPEN_ACTIVITY = "open_activity";

    public static final int OPEN_MAIN_CONTENT = 0x10000001;
    public static final int OPEN_SHOOT_CONTENT = 0x10000002;
    public static final int OPEN_MINE_CONTENT = 0x10000003;

    public static final int OPEN_PHONE_REGISTER_ACTIVITY = 0x20000001;
    
    public static final int CHOOSE_LOCATION = 3;

    public static final String ACTION_SHOW_DANCE_MAIN_ACTIVITY = "com.android.app.showdance.ui.ShowDanceMainActivity";
    public static final String ACTION_DOWNLOADED_MUSIC_ACTIVITY = "com.android.app.showdance.ui.DownloadedMusicActivity";
    public static final String ACTION_RECORDED_VIDEO_ACTIVITY = "com.android.app.showdance.ui.RecordedVideoActivity";
    public static final String ACTION_USE_INTRODUCTION_ACTIVITY = "com.android.app.showdance.ui.UseIntroductionActivity";
    public static final String ACTION_OWNER_PHONE_REGISTER_ACTIVITY = "com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity";
    public static final String ACTION_FOUND_SEARCH_MUSIC_ACTIVITY = "com.android.app.showdance.ui.FoundSearchMusicActivity";
    public static final String ACTION_TEACHER_ACTIVITY = "com.android.app.showdance.ui.TeacherActivity";
    public static final String ACTION_MAIN_CATEGORY_ACTIVITY = "com.android.app.showdance.ui.MainCategoryActivity";
    public static final String ACTION_CHOOSE_LOCATION_ACTIVITY = "com.android.app.showdance.ui.ChooseLocationActivity";
    public static final String ACTION_CUSTOM_UPLOAD_VIDEO_TITLE_ACTIVITY = "com.android.app.showdance.ui.CustomUploadVideoTitleActivity";
    
    public static final String ACTION_MINE_FRAGMENT = "com.android.app.showdance.fragment.MineFragment";
    
    /**
     * 传递的对象
     */
    public static final String userJson = "userJson";

    /**
     * 公用部分
     */
    public static final int PageSize = 10;

    public static final String COL_ID = "_id";
    /** 视频标题 */
    public static final String COL_TITLE = "title";
    /** 视频标题拼音 */
    public static final String COL_TITLE_PINYIN = "title_pinyin";
    /** 视频路径 */
    public static final String COL_PATH = "path";
    /** 最后一次访问时间 */
    public static final String COL_LAST_ACCESS_TIME = "last_access_time";
    /** 视频时长 */
    public static final String COL_DURATION = "duration";
    /** 视频播放进度 */
    public static final String COL_POSITION = "position";
    /** 视频缩略图 */
    public static final String COL_THUMB = "thumb";
    /** 文件大小 */
    public static final String COL_FILE_SIZE = "file_size";

    /**
     * 传递消息的Action
     ***/
    // 推荐舞曲下载进度
    public static final String ACTION_SHOW_PAY_INFO = "ACTION_SHOW_PAY_INFO";
    // 搜索舞曲下载进度
    // public static final String ACTION_SEARCH_DOWNLOAD_SIZE =
    // "ACTION_SEARCH_DOWNLOAD_SIZE";
    // 明星老师舞曲下载进度
    // public static final String ACTION_TEACHER_DOWNLOAD_SIZE =
    // "ACTION_TEACHER_DOWNLOAD_SIZE";

    // 推荐舞曲视频录制
    public static final String ACTION_SHOW_MEDIARECORDER = "ACTION_SHOW_MEDIARECORDER";
    // 搜索舞曲视频录制
    public static final String ACTION_SEARCH_MEDIARECORDER = "ACTION_SEARCH_MEDIARECORDER";
    // 明星老师舞曲视频录制
    public static final String ACTION_TEACHER_MEDIARECORDER = "ACTION_TEACHER_MEDIARECORDER";
    // 已下载视频录制
    public static final String ACTION_DOWN_MEDIARECORDER = "ACTION_DOWN_MEDIARECORDER";

    // 删除已录制视频
    public static final String ACTION_DEL_RECORDED = "ACTION_DEL_RECORDED";
    // 下载状态
    public static final String ACTION_DOWNLOAD_STATE = "ACTION_DOWNLOAD_STATE";
    // 上传
    public static final String ACTION_UPLOAD = "ACTION_UPLOAD";
    // 上传进度
    public static final String ACTION_UPLOAD_SIZE = "ACTION_UPLOAD_SIZE";
    // 上传状态
    public static final String ACTION_UPLOAD_STATE = "ACTION_UPLOAD_STATE";
    // 下载成功
    public static final String ACTION_DOWNLOADVIDEO_SUCCESS = "ACTION_DOWNLOADVIDEO_SUCCESS";
    // 短信接收
    public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    /**
     * 屏幕分辨率
     */
    public static float currentDensity;
    public static int currentWidthPixels;
}
