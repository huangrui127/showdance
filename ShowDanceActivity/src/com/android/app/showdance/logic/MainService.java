package com.android.app.showdance.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.app.showdance.entity.Keys;
import com.android.app.showdance.model.DownloadMedia;
import com.android.app.showdance.model.DownloadMediaPageVo;
import com.android.app.showdance.model.DownloadMusic;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.DownloadMusicPageVo;
import com.android.app.showdance.model.MediaComment;
import com.android.app.showdance.model.MediaCommentPageVo;
import com.android.app.showdance.model.MediaInfo;
import com.android.app.showdance.model.MediaInfoPageVo;
import com.android.app.showdance.model.MemberFeedback;
import com.android.app.showdance.model.Praise;
import com.android.app.showdance.model.ShareInfo;
import com.android.app.showdance.model.TeacherDancerMusic;
import com.android.app.showdance.model.User;
import com.android.app.showdance.model.UserFans;
import com.android.app.showdance.model.UserFansPageVo;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.model.VDanceMusicPageVo;
import com.android.app.showdance.model.VUserFansMediaMusicCount;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.DownloadedMusicActivity;
import com.android.app.showdance.ui.FoundSearchMusicActivity;
import com.android.app.showdance.ui.OwnerActivity;
import com.android.app.showdance.ui.RecordedVideoActivity;
import com.android.app.showdance.ui.TeacherActivity;
import com.android.app.showdance.ui.TeacherDancerActivity;
import com.android.app.showdance.ui.more.OwnerFeedBack;
import com.android.app.showdance.ui.oa.OwnerMoreActivity;
import com.android.app.showdance.ui.oa.PersonalMsgActivity;
import com.android.app.showdance.ui.oa.VideoCommentActivity;
import com.android.app.showdance.ui.oa.VideoPublishCommentActivity;
import com.android.app.showdance.ui.usermanager.OwnerLoginActivity;
import com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity;
import com.android.app.showdance.ui.usermanager.OwnerRegisterDanceIdActivity;
import com.android.app.showdance.ui.usermanager.TheFindPasswordActivity;
import com.android.app.showdance.ui.usermanager.TheModifyOldPhoneActivity;
import com.android.app.showdance.ui.usermanager.TheModifyPasswordActivity;
import com.android.app.showdance.ui.usermanager.TheResetPasswordActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.utils.SoapWebServiceInterface;
import com.android.app.wumeiniang.ShowDanceActivity;
import com.android.app.wumeiniang.app.AppManager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * MainService 后台总调度服务类
 * 
 */
public class MainService extends Service implements Runnable {

    public static boolean isrun = false;
    private static ArrayList<Task> allTask = new ArrayList<Task>();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * 
     * @Description:添加窗口到集合中
     * @param baseAct
     * @return void
     */
    public static void addActivity(BaseActivity baseAct) {
        AppManager.getAppManager().addActivity(baseAct);
    }

    /**
     * 
     * @Description:从集合中结束指定的Activity
     * @param baseAct
     * @return void
     */
    public static void removeActivity(BaseActivity baseAct) {
        AppManager.getAppManager().finishActivity(baseAct);
    }

    /**
     * 
     * @Description:添加任务
     * @param ts
     * @return void
     */
    public static void newTask(Task ts) {
        allTask.add(ts);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isrun = false;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        isrun = true;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (isrun) {

            if (allTask.size() > 0) {
                doTask(allTask.get(0));
            } else {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {

                }
            }

        }
    }

    /**
     * 
     * @Description:
     * @param mTask
     * @return void
     */
    private void doTask(Task mTask) {
        // 得到一个消息对象message
        Message message = handler.obtainMessage();
        message.what = mTask.getTaskID();
        if (NetUtil.isNetworkConnected(this)) {
            ConstantsUtil.NetworkStatus = true;
            switch (mTask.getTaskID()) {
            case TaskType.TS_registerGETCODE: // 31 注册时获取验证码
                UserVo registerUserVo = (UserVo) mTask.getTaskParam().get("registerUserVo");
                Map<String, Object> registerGetCodeData = SoapWebServiceInterface.GetcodeToJson(registerUserVo);
                message.obj = registerGetCodeData;
                break;
            case TaskType.TS_registerCheckCODE: // 32 注册时校验验证码
                UserVo registerUserVo2 = (UserVo) mTask.getTaskParam().get("registerUserVo");
                Map<String, Object> registerGetCodeData2 = SoapWebServiceInterface.CheckcodeToJson(registerUserVo2);
                message.obj = registerGetCodeData2;
                break;
            case TaskType.TS_registerUSER: // 33 注册
                UserVo registerUserVo3 = (UserVo) mTask.getTaskParam().get("registerUserVo");
                Map<String, Object> registerGetCodeData3 = SoapWebServiceInterface.registerUserToJson(registerUserVo3);
                message.obj = registerGetCodeData3;
                break;
            case TaskType.TS_LoginUSER: // 34 登录
                User loginUser = (User) mTask.getTaskParam().get("loginUser");
                Map<String, Object> loginDataMap = SoapWebServiceInterface.loginUserToJson(loginUser);
                message.obj = loginDataMap;
                break;
            case TaskType.TS_modifyPWD: // 35 修改密码
                UserVo modifyPWDUserVo = (UserVo) mTask.getTaskParam().get("modifyPWDUserVo");
                Map<String, Object> modifyPWDMap = SoapWebServiceInterface.modifyPWDToJson(modifyPWDUserVo);
                message.obj = modifyPWDMap;
                break;
            case TaskType.TS_modifyPhoneGETCODE: // 36 修改手机号-获取验证码
                UserVo modifyPhoneUserVo = (UserVo) mTask.getTaskParam().get("modifyPhoneUserVo");
                Map<String, Object> modifyPhoneMap = SoapWebServiceInterface.GetcodeToJson(modifyPhoneUserVo);
                message.obj = modifyPhoneMap;
                break;
            case TaskType.TS_modifyPhoneCheckCODE: // 37 修改手机号-检验验证码
                UserVo modifyPhoneUserVo2 = (UserVo) mTask.getTaskParam().get("modifyPhoneUserVo");
                Map<String, Object> modifyPhoneMap2 = SoapWebServiceInterface.CheckcodeToJson(modifyPhoneUserVo2);
                message.obj = modifyPhoneMap2;
                break;
            case TaskType.TS_forgetPwdGETCODE: // 38 找回密码-获取验证码
                UserVo forgetPwdUserVo = (UserVo) mTask.getTaskParam().get("forgetPwdUserVo");
                Map<String, Object> forgetPwdMap = SoapWebServiceInterface.GetcodeToJson(forgetPwdUserVo);
                message.obj = forgetPwdMap;
                break;
            case TaskType.TS_forgetPwd_CheckCODE: // 39 找回密码-校验验证码
                UserVo forgetPwdUserVo2 = (UserVo) mTask.getTaskParam().get("forgetPwdUserVo");
                Map<String, Object> forgetPwdMap2 = SoapWebServiceInterface.CheckcodeToJson(forgetPwdUserVo2);
                message.obj = forgetPwdMap2;
                break;
            case TaskType.TS_forgetPwd_ModifyPwd: // 40 找回密码--重置密码
                UserVo forgetPwdUserVo3 = (UserVo) mTask.getTaskParam().get("forgetPwdUserVo");
                Map<String, Object> forgetPwdMap3 = SoapWebServiceInterface.forgetPwd_ModifyPwdToJson(forgetPwdUserVo3);
                message.obj = forgetPwdMap3;
                break;

            case TaskType.TS_modifyUserInfo: // 42 修改用户信息
                UserInfo modifyUserInfo = (UserInfo) mTask.getTaskParam().get("modifyUserInfo");
                Map<String, Object> modifyUserMap = SoapWebServiceInterface.modifyUserInfoToJson(modifyUserInfo);
                message.obj = modifyUserMap;
                break;

            case TaskType.TS_modifyRegional: // 42 修改登录用户所在城市
                User modifyRegional = (User) mTask.getTaskParam().get("modifyRegional");
                Map<String, Object> resultModifyRegional = SoapWebServiceInterface.modifyRegionalToJson(modifyRegional);
                message.obj = resultModifyRegional;
                break;

            case TaskType.TS_danceMusicPageList: // 43 下载推荐舞曲
                VDanceMusicPageVo danceMusicPageVo = (VDanceMusicPageVo) mTask.getTaskParam().get("danceMusicPageVo");
                int danceMusicType = (Integer) mTask.getTaskParam().get("danceMusicType");
                List<DownloadMusicInfo> resultDanceMusicPageVo = SoapWebServiceInterface
                        .danceMusicPageVoToJson(danceMusicPageVo);
                message.obj = resultDanceMusicPageVo;
                message.arg1 = danceMusicType;
                break;

            case TaskType.TS_GETMYVIDIO:// 101 查询我的视频 (即我的秀舞)
                MediaInfoPageVo mediaInfoPageVo = (MediaInfoPageVo) mTask.getTaskParam().get("mediaInfoPageVo");
                int videoType = (Integer) mTask.getTaskParam().get("videoType");
                ArrayList<Map<String, Object>> mediaInfoPageVoMap2 = SoapWebServiceInterface
                        .MediaInfoPageVoToJson(mediaInfoPageVo);
                message.obj = mediaInfoPageVoMap2;
                message.arg1 = videoType;
                break;

            case TaskType.TS_DELETEMEDIAINFO:// 100 删除我的视频 (即我的秀舞)
                List<MediaInfo> deleteMediaInfo = (List<MediaInfo>) mTask.getTaskParam().get("deleteMediaInfo");
                Map<String, Object> resultDeleteMediaInfo = SoapWebServiceInterface
                        .deleteMediaInfoToJson(deleteMediaInfo);
                message.obj = resultDeleteMediaInfo;
                break;

            case TaskType.TS_MYATTENTION: // 102 我关注的
                UserFansPageVo mUserFansPageVo2 = (UserFansPageVo) mTask.getTaskParam().get("mUserFansPageVo");
                ArrayList<Map<String, Object>> userFansPageVoMap2 = SoapWebServiceInterface
                        .myAttentionPageVoToJson(mUserFansPageVo2);
                message.obj = userFansPageVoMap2;
                break;

            case TaskType.TS_MYFANS:// 103 我的粉丝
                UserFansPageVo mUserFansPageVo = (UserFansPageVo) mTask.getTaskParam().get("mUserFansPageVo");
                ArrayList<Map<String, Object>> userFansPageVoMap = SoapWebServiceInterface
                        .UserFansPageVoToJson(mUserFansPageVo);
                message.obj = userFansPageVoMap;
                break;

            case TaskType.TS_MYDOWNLOADMV:// 104 我的下载（我下载的视频）
                DownloadMediaPageVo downloadMediaPageVo = (DownloadMediaPageVo) mTask.getTaskParam()
                        .get("downloadMediaPageVo");
                ArrayList<Map<String, Object>> downloadMediaPageVoMap = SoapWebServiceInterface
                        .downloadMediaPageVoToJson(downloadMediaPageVo);
                message.obj = downloadMediaPageVoMap;
                break;

            case TaskType.TS_GETNEARMANDETAIL:// 105 根据用户id，查询用户详情
                User user = (User) mTask.getTaskParam().get("user");
                Map<String, Object> userData = SoapWebServiceInterface.getNearManDetailToJson(user);
                message.obj = userData;
                break;

            case TaskType.TS_GETMEDIAINFO:// 106
                                          // 通过视频id，查询视频详情和个人相关详情getMediaInfoById
                MediaInfoPageVo mediaInfoPageVo2 = (MediaInfoPageVo) mTask.getTaskParam().get("mediaInfoPageVo");
                Map<String, Object> mediaInfoPageVoData2 = SoapWebServiceInterface
                        .getMediaInfoByIdToJson(mediaInfoPageVo2);
                message.obj = mediaInfoPageVoData2;
                break;
            case TaskType.TS_GETUSERMEDIAMUSICCOUNT:// 107
                                                    // 根据用户id，获取关注舞友、粉丝、下载视频、我的秀舞
                                                    // 数量
                VUserFansMediaMusicCount vUserFansMediaMusicCount = (VUserFansMediaMusicCount) mTask.getTaskParam()
                        .get("vUserFansMediaMusicCount");
                Map<String, Object> vUserFansMediaMusicCountData = SoapWebServiceInterface
                        .getVUserFansMediaMusicCountByIdToJson(vUserFansMediaMusicCount);
                message.obj = vUserFansMediaMusicCountData;
                break;
            case TaskType.TS_ADDFOUCES:// 108 关注用户
                UserFans userFans = (UserFans) mTask.getTaskParam().get("userFans");
                Map<String, Object> userFansData = SoapWebServiceInterface.saveUserFansToJson(userFans);
                message.obj = userFansData;
                break;
            case TaskType.TS_DELETEFOUCES:// 109取消关注用户
                UserFans userFans2 = (UserFans) mTask.getTaskParam().get("userFans");
                Map<String, Object> userFansData2 = SoapWebServiceInterface.deleteUserFansToJson(userFans2);
                message.obj = userFansData2;
                break;
            case TaskType.TS_SAVEDOWNLOADMEDIA:// 110 保存下载视频记录
                DownloadMedia downloadMedia = (DownloadMedia) mTask.getTaskParam().get("downloadMedia");
                Map<String, Object> downloadMediaData = SoapWebServiceInterface.saveDownloadMediaToJson(downloadMedia);
                message.obj = downloadMediaData;
                break;
            case TaskType.TS_SAVEPRAISE:// 111 点赞操作
                Praise praise = (Praise) mTask.getTaskParam().get("praise");
                Map<String, Object> praiseData = SoapWebServiceInterface.savePraiseToJson(praise);
                message.obj = praiseData;
                break;
            case TaskType.TS_SAVESHAREINFO:// 112 分享
                ShareInfo shareInfo = (ShareInfo) mTask.getTaskParam().get("shareInfo");
                Map<String, Object> shareInfoData = SoapWebServiceInterface.saveShareInfoToJson(shareInfo);
                message.obj = shareInfoData;
                break;
            case TaskType.TS_GETMEDIACOMMENTLIST:// 113 获取视频评论
                MediaCommentPageVo mediaCommentPageVo = (MediaCommentPageVo) mTask.getTaskParam()
                        .get("mediaCommentPageVo");
                ArrayList<Map<String, Object>> mediaCommentPageVoData = SoapWebServiceInterface
                        .getMediaCommentListByMediaIdToJson(mediaCommentPageVo);
                message.obj = mediaCommentPageVoData;
                break;
            case TaskType.TS_SAVEMEDIACOMMENT:// 114 评论视频
                MediaComment mediaComment = (MediaComment) mTask.getTaskParam().get("mediaComment");
                Map<String, Object> mediaCommentData = SoapWebServiceInterface.saveMediaCommentToJson(mediaComment);
                message.obj = mediaCommentData;
                break;
            case TaskType.TS_SAVEMEMFEEDBACK:// 115 保存用于意见反馈
                MemberFeedback memberFeedBack = (MemberFeedback) mTask.getTaskParam().get("memberFeedBack");
                Map<String, Object> memberFeedBackData = SoapWebServiceInterface.menberFeedBackToJson(memberFeedBack);
                message.obj = memberFeedBackData;
                break;
            case TaskType.TS_GETUSERINFOBYID:// 116 查看用户信息
                User user2 = (User) mTask.getTaskParam().get("user");
                Map<String, Object> userData2 = SoapWebServiceInterface.getNearManDetailToJson(user2);
                message.obj = userData2;
                break;

            case TaskType.TS_MediaInfoList:// 301 首页 视频榜单
                MediaInfoPageVo mediaInfoPageVoList = (MediaInfoPageVo) mTask.getTaskParam().get("mediaInfoPageVo");
                int refreshTypeMedia = (Integer) mTask.getTaskParam().get("refreshType");
                ArrayList<Map<String, Object>> resultMediaInfoPageVoList = SoapWebServiceInterface
                        .mediaInfoPageVoListToJson(mediaInfoPageVoList);
                message.obj = resultMediaInfoPageVoList;
                message.arg1 = refreshTypeMedia;
                break;
            case TaskType.TS_MediaInfoPageList:// 304 已录制视频
                MediaInfoPageVo mediaInfoPageList = (MediaInfoPageVo) mTask.getTaskParam().get("mediaInfoPageVo");
                ArrayList<Map<String, Object>> resultMediaInfoPageList = SoapWebServiceInterface
                        .mediaInfoPageListToJson(mediaInfoPageList);
                message.obj = resultMediaInfoPageList;
                break;
            case TaskType.TS_MusicInfoPageList:// 305 已下载舞曲
                DownloadMusicPageVo downloadMusicPageVoList = (DownloadMusicPageVo) mTask.getTaskParam()
                        .get("downloadMusicPageVo");
                int refreshTypeMusic = (Integer) mTask.getTaskParam().get("refreshType");
                ArrayList<Map<String, Object>> resultDownloadMusicPage = SoapWebServiceInterface
                        .downloadMusicPageListToJson(downloadMusicPageVoList);
                message.obj = resultDownloadMusicPage;
                message.arg1 = refreshTypeMusic;
                break;
            case TaskType.TS_SaveDownloadMusic:// 307 保存已下载舞曲记录
                DownloadMusic downloadMusic = (DownloadMusic) mTask.getTaskParam().get("downloadMusic");
                int downloadType = (Integer) mTask.getTaskParam().get("downloadType");
                Map<String, Object> resultDownloadMusic = SoapWebServiceInterface
                        .saveDownloadMusicToJson(downloadMusic);
                message.obj = resultDownloadMusic;
                message.arg1 = downloadType;
                break;
            case TaskType.TS_getVDanceMusicDancerList:// 获取明星老师列表
                DownloadMusic dancerList = (DownloadMusic) mTask.getTaskParam().get("downloadMusic");
                List<TeacherDancerMusic> resultGetVDanceMusicDancerList = SoapWebServiceInterface
                        .dancerListToJson(dancerList);
                message.obj = resultGetVDanceMusicDancerList;
                break;
            case TaskType.TS_PostMediaInfoList:// 303 上传视频
                String filePath = (String) mTask.getTaskParam().get("filePath");
                Map<String, Object> resultfilePath = SoapWebServiceInterface.uploadFile(this, filePath);
                message.obj = resultfilePath;
                break;
            case TaskType.TS_SaveMediaInfo:// 306 上传视频信息
                MediaInfo mediaInfo = (MediaInfo) mTask.getTaskParam().get("mediaInfo");
                int requestCode = (Integer) mTask.getTaskParam().get("requestCode");
                Map<String, Object> resultMediaInfo = SoapWebServiceInterface.saveMediaInfoToJson(mediaInfo,
                        requestCode);
                message.obj = resultMediaInfo;
                break;
            case TaskType.TS_UploadToken:// 308 获取上传凭证tokenn
                Keys keys = (Keys) mTask.getTaskParam().get("keys");
                Map<String, Object> resultKeys = SoapWebServiceInterface.getKeysToJson(keys);
                message.obj = resultKeys;
                break;

            }
        } else {
            ConstantsUtil.NetworkStatus = false;
            switch (message.what) {
            case TaskType.TS_danceMusicPageList: // 43 下载推荐舞曲
                // VDanceMusicPageVo danceMusicPageVo = (VDanceMusicPageVo)
                // mTask.getTaskParam().get("danceMusicPageVo");
                int danceMusicType = (Integer) mTask.getTaskParam().get("danceMusicType");
                // List<DownloadMusicInfo> resultDanceMusicPageVo =
                // SoapWebServiceInterface.danceMusicPageVoToJson(danceMusicPageVo);
                // message.obj = resultDanceMusicPageVo;
                message.arg1 = danceMusicType;
                break;
            default:
                break;
            }
        }

        allTask.remove(mTask);
        // 将message对象加入到消息队列当中,然后会去执行handler对象的handleMessage方法。
        handler.sendMessage(message);
    }

    /**
     * 
     */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity curActivity = null;
            int paramCount = 2;

            switch (msg.what) {
            case TaskType.TS_registerGETCODE: // 31 注册时获取验证码
                curActivity = AppManager.getAppManager().getActivityByName(OwnerPhoneRegisterActivity.class);
                break;
            case TaskType.TS_registerCheckCODE: // 32 注册检验验证码
                curActivity = AppManager.getAppManager().getActivityByName(OwnerPhoneRegisterActivity.class);
                break;
            case TaskType.TS_registerUSER: // 33 注册
                curActivity = AppManager.getAppManager().getActivityByName(OwnerRegisterDanceIdActivity.class);
                break;
            case TaskType.TS_LoginUSER: // 34 登录
                curActivity = AppManager.getAppManager().getActivityByName(OwnerLoginActivity.class);
                break;
            case TaskType.TS_modifyPWD: // 35 修改密码
                curActivity = AppManager.getAppManager().getActivityByName(TheModifyPasswordActivity.class);
                break;
            case TaskType.TS_modifyPhoneGETCODE: // 36 修改手机号-获取验证码
                curActivity = AppManager.getAppManager().getActivityByName(TheModifyOldPhoneActivity.class);
                break;
            case TaskType.TS_modifyPhoneCheckCODE: // 37 修改手机号-检验验证码
                curActivity = AppManager.getAppManager().getActivityByName(TheModifyOldPhoneActivity.class);
                break;
            case TaskType.TS_forgetPwdGETCODE: // 38 修改手机号-检验验证码
                curActivity = AppManager.getAppManager().getActivityByName(TheFindPasswordActivity.class);
                break;
            case TaskType.TS_forgetPwd_CheckCODE: // 39 找回密码-校验验证码
                curActivity = AppManager.getAppManager().getActivityByName(TheFindPasswordActivity.class);
                break;
            case TaskType.TS_forgetPwd_ModifyPwd: // 40 找回密码-重置密码
                curActivity = AppManager.getAppManager().getActivityByName(TheResetPasswordActivity.class);
                break;

            case TaskType.TS_modifyUserInfo: // 42 修改用户信息
                curActivity = AppManager.getAppManager().getActivityByName(PersonalMsgActivity.class);
                break;
            case TaskType.TS_modifyRegional: // 42修改登录用户所在城市
                curActivity = AppManager.getAppManager().getActivityByName(OwnerMoreActivity.class);
                break;

            case TaskType.TS_MediaInfoList: // 42 首页 视频榜单
                // curActivity =
                // AppManager.getAppManager().getActivityByName(HomePageActivity.class);
                paramCount = 3;
                break;
            case TaskType.TS_MediaInfoPageList: // 42 首页 视频榜单
                curActivity = AppManager.getAppManager().getActivityByName(RecordedVideoActivity.class);
                break;
            case TaskType.TS_MusicInfoPageList: // 42 已下载舞曲
                curActivity = AppManager.getAppManager().getActivityByName(DownloadedMusicActivity.class);
                paramCount = 3;
                break;
            case TaskType.TS_SaveDownloadMusic: // 42 保存下载舞曲记录
                switch (msg.arg1) {
                case ConstantsUtil.showDanceHome:
                    curActivity = AppManager.getAppManager().getActivityByName(ShowDanceActivity.class);
                    break;
                case ConstantsUtil.showDanceSearch:
                    curActivity = AppManager.getAppManager().getActivityByName(FoundSearchMusicActivity.class);
                    break;
                case ConstantsUtil.showDanceTeacher:
                    curActivity = AppManager.getAppManager().getActivityByName(TeacherDancerActivity.class);
                    break;
                default:
                    break;
                }

                break;
            case TaskType.TS_danceMusicPageList: // 42 下载推荐舞曲

                switch (msg.arg1) {
                case ConstantsUtil.showDanceHome:
                    curActivity = AppManager.getAppManager().getActivityByName(ShowDanceActivity.class);
                    break;
                case ConstantsUtil.showDanceSearch:
                    curActivity = AppManager.getAppManager().getActivityByName(FoundSearchMusicActivity.class);
                    break;
                case ConstantsUtil.showDanceTeacher:
                    curActivity = AppManager.getAppManager().getActivityByName(TeacherDancerActivity.class);
                    break;
                default:
                    break;
                }
                break;
            case TaskType.TS_getVDanceMusicDancerList: // 获取明星老师列表
                curActivity = AppManager.getAppManager().getActivityByName(TeacherActivity.class);
                break;

            case TaskType.TS_PostMediaInfoList: // 303 上传视频
                curActivity = AppManager.getAppManager().getActivityByName(RecordedVideoActivity.class);
                break;
            case TaskType.TS_SaveMediaInfo: // 306 上传视频
                curActivity = AppManager.getAppManager().getActivityByName(RecordedVideoActivity.class);
                break;
            case TaskType.TS_UploadToken: // 308 获取上传凭证token
                curActivity = AppManager.getAppManager().getActivityByName(RecordedVideoActivity.class);
                break;

            // ==============================================================
            // add by daish

            case TaskType.TS_GETUSERMEDIAMUSICCOUNT:// 107
                                                    // 根据用户id，获取关注舞友、粉丝、下载视频、我的秀舞
                                                    // 数量
                curActivity = AppManager.getAppManager().getActivityByName(OwnerActivity.class);
                break;
            case TaskType.TS_GETMEDIACOMMENTLIST:// 113 获取视频评论
                curActivity = AppManager.getAppManager().getActivityByName(VideoCommentActivity.class);
                break;
            case TaskType.TS_SAVEMEDIACOMMENT:// 114 评论视频
                curActivity = AppManager.getAppManager().getActivityByName(VideoPublishCommentActivity.class);
                break;
            case TaskType.TS_SAVEMEMFEEDBACK:// 115 保存用户反馈信息
                curActivity = AppManager.getAppManager().getActivityByName(OwnerFeedBack.class);
                break;
            case TaskType.TS_GETUSERINFOBYID:// 116 查看用户信息
                curActivity = AppManager.getAppManager().getActivityByName(PersonalMsgActivity.class);
                break;

            }

            // 调用activity 的refresh
            if (curActivity != null) {
                if (paramCount == 2) {
                    curActivity.refresh(msg.what, msg.obj);
                } else {
                    curActivity.refresh(msg.what, msg.obj, msg.arg1);
                }
            }
        };
    };
}
