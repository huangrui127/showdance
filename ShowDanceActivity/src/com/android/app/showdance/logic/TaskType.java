package com.android.app.showdance.logic;

/**
 * 
 * @ClassName: TaskType
 * @Description: 任务类型
 * @author maminghua
 * @date 2014-12-1 上午10:44:20
 * 
 */
public class TaskType {
	public static final int TS_CHECKOPENREGIONALINFO = 13;// 获取开通服务的城市
	
	public static final int TS_SAVECOMPLAINT = 15;// 15保存用户投诉信息
	public static final int TS_GETLISTdISTRICINFO = 16;// 查询符合条件小区
	public static final int TS_MODIFYUSERCITY = 17;// 修改用户所在城市

	public static final int TS_GETRECHARGEINFO = 18;// 充值记录 消费记录
	public static final int TS_BALANCEOFGOLD = 19;// 金币余额
	public static final int TS_BALANCEOFACCOUNT = 20;// 账户余额
	public static final int TS_ADDRECHARGEINFO = 21;// 充值； sign=3退款
	public static final int TS_GETAUNTINFO = 22;// 查询匹配阿姨
	public static final int TS_LISTGOLDINFO = 23;// 金币交易记录

	// yangsiyang add
	public static final int TS_registerGETCODE = 31;// 注册-获取验证码
	public static final int TS_registerCheckCODE = 32;// 注册-检验验证码
	public static final int TS_registerUSER = 33;// 设置秀舞吧号后进行-注册

	public static final int TS_LoginUSER = 34; // 登录
	public static final int TS_modifyPWD = 35; // 修改密码

	public static final int TS_modifyPhoneGETCODE = 36;// 修改手机号时获取验证码
	public static final int TS_modifyPhoneCheckCODE = 37;// 修改手机号时校验验证码

	public static final int TS_forgetPwdGETCODE = 38;// 找回密码-发送验证码
	public static final int TS_forgetPwd_CheckCODE = 39;// 找回密码-校验验证码
	public static final int TS_forgetPwd_ModifyPwd = 40;// 找回密码--修改密码

	// 41 //查看用户信息
	public static final int TS_modifyUserInfo = 42;// 修改用户信息
	
	public static final int TS_modifyRegional = 43;// 修改登录用户所在城市

	public static final int TS_danceMusicPageList = 44;// 下载推荐舞曲
	
	// add by daish
	public static final int TS_DELETEMEDIAINFO = 100;// 删除我的视频
	public static final int TS_GETMYVIDIO = 101;// 我的秀舞
	public static final int TS_MYATTENTION = 102;// 我的关注
	public static final int TS_MYFANS = 103;// 我的粉丝
	public static final int TS_MYDOWNLOADMV = 104;// 我下载的视频

	public static final int TS_MediaInfoList = 301;// 首页 视频榜单
	public static final int HTTPGET_TEST = 302;// 首页 视频榜单
	public static final int TS_PostMediaInfoList = 303;// 上传视频
	public static final int TS_MediaInfoPageList = 304;// 已录制视频
	public static final int TS_MusicInfoPageList = 305;// 已下载舞曲
	public static final int TS_SaveMediaInfo = 306;// 上传视频信息
	public static final int TS_SaveDownloadMusic = 307;// 保存下载舞曲记录
	public static final int TS_UploadToken = 308;// 获取上传凭证token
	public static final int TS_getVDanceMusicDancerList = 309;// 获取明星老师列表
	
	public static final int TS_GETNEARMANDETAIL = 105;// 根据用户id，查询用户详情
	public static final int TS_GETMEDIAINFO = 106;// 根据用户id，查询用户详情getMediaInfoById 
	public static final int TS_GETUSERMEDIAMUSICCOUNT = 107;//  根据用户id，获取关注舞友、粉丝、下载视频、我的秀舞 数量getVUserFansMediaMusicCountById
	public static final int TS_ADDFOUCES = 108;//  关注用户saveUserFans 
	public static final int TS_DELETEFOUCES = 109;//  取消关注用户
	public static final int TS_SAVEDOWNLOADMEDIA = 110;//  保存下载视频记录
	public static final int TS_SAVEPRAISE = 111;//  点赞操作
	public static final int TS_SAVESHAREINFO = 112;//  分享
	public static final int TS_GETMEDIACOMMENTLIST = 113;//  获取视频评论
	public static final int TS_SAVEMEDIACOMMENT = 114;//  评论视频
	public static final int TS_SAVEMEMFEEDBACK = 115;// 保存用户反馈信息
	public static final int TS_GETUSERINFOBYID = 116;// C查看用户信息

}
