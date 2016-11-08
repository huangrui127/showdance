package com.android.app.showdance.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.android.app.showdance.custom.MarqueeTextView;
import com.android.app.showdance.logic.UploadViedoService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.UploadEvent;
import com.android.app.showdance.model.glmodel.AddShareOrPlayCountInfo;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.HotVideoListInfo.HotVideo;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.model.glmodel.VideoUploadInfo;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;
import com.android.app.showdance.playvideo.CustomVideoView;
import com.android.app.showdance.ui.RecordedVideoActivity.BaseResponse;
import com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.DensityUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.showdance.widget.MyProgressBar;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMVideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

/**
 * 功能：视频播放界面（网络视频及本地视频）
 * 
 * @author djd
 *
 */
public class PlayVideoActivity extends Activity {

    private static final String TAG = "PlayVideoActivity";
    private static final String TEMP_URL = "http://www.xiuwuba.net:82";

    private static final int REQUEST_CUSTOM_UPLOAD_VIDEO_TITLE = 1111;
    private static final int REQUEST_OWNER_PHONE_REGISTER_ACTIVITY = 2222;
    private boolean isJoinInTheActivitiy = false; // 是否参加活动
    private boolean loadingFlag = true; // 是否正在加载进度（上传进度）
    private Dialog mUploadingFileDialog; // 正在上传Dialog
    protected Dialog mDialog;

    private int mUserId;
    private User mUserInfo;
    private String token; // 用于上传token
    private View mProgressBarView; // 上传进度View

    private TextView mTitleTV; // 顶部bar标题（播放界面隐藏）
    private MarqueeTextView mMarqueeTextView; // 顶部bar标题（播放界面使用的标题）
    private ImageButton return_imgbtn;
    private CustomVideoView mVideoView;
    private RelativeLayout mTitleLayout; // 顶部标题栏
    private FrameLayout mVideoLayout; // VideoView父布局
    private RelativeLayout mContentLayout; // 视频底部内容部分
    private TextView mPlayCountTV; // 播放次数view
    private TextView mShareCountTV; // 分享次数view
    private Button mShareBtn; // 分享按钮

    private RelativeLayout mBackAndUploadLayout; // 视频界面顶部返回及上传按钮
    private RelativeLayout mVideoBackBtn; // 横屏时，视频播放界面左上方返回按钮
    private Button mUploadBtn; // 横屏时，播放本地视频时出现的上传按钮

    private AudioManager mAudioManager; // 系统音量控制
    private LinearLayout mControllerLayout; // 播放器的控制界面父布局
    private ImageButton mPlayIB; // 播放、暂停按钮
    private TextView mCurrentTimeTV; // 当前播放时间文字
    private SeekBar mProgressSB; // 播放进度显示Bar
    private TextView mTotalTimeTV; // 播放总时间文字
    private ImageView mFullScreeIV; // 全屏、小屏切换按钮

    private ProgressBar mVideoProgressBar; // 视频缓冲进度
    // private TextView mDownloadRateTV; // 视频缓冲下载进度
    private TextView mLoadRateTV; // 视频加载进度

    // 根据接收的不同的Bean，进行相应的判断
    private HotVideo mHotVideo;
    private VideoUploadResponse mVideoUploadResponse;

    private int mLastPos = 0; // 记录播放位置
    private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE; // 播放状态
    private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
    private long mTouchTime;
    private Uri mVideoUri; // 网络视频Uri地址
    private String mVideoPathLocal; // 本地视频播放路径
    private String mVideoNameLocal; // 本地视频名称
    private int mVideoUploadState; // 本地视频上传状态
    private int ACTIVITY_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; // 屏幕信息初始化

    private boolean isControllerBarShow = true; // 播放界面控制条是否隐藏，初始显示状态

    // isShareSuccess为true且isShareSuccessCount为1时，表示分享成功
    private int isShareSuccessCount = 100;
    private boolean isShareSuccess = false;

    private enum PLAYER_STATUS {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
    }

    // 存储下列值的目的是，防止横屏模式下，无法正确获取到横屏的宽、高值
    private int widthPixelLandScape; // 横屏模式下获取的屏幕宽值
    private int heightPixelLandScape; // 横屏模式下获取的屏幕高值

    private boolean isNeedRecoveryVideoLayout = false;

    Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UI_EVENT_UPDATE_CURRPOSITION: // 更新进度及时间
                // L.i(TAG, "更新进度及时间UI_EVENT_UPDATE_CURRPOSITION");
                if (!mVideoView.isPlaying()) {
                    mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
                    return;
                }
                int currPosition = mVideoView.getCurrentPosition();
                int duration = mVideoView.getDuration();
                updateTextViewWithTimeFormat(mCurrentTimeTV, currPosition);
                updateTextViewWithTimeFormat(mTotalTimeTV, duration);
                mProgressSB.setMax(duration);
                mProgressSB.setProgress(currPosition);
                mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
                break;
            }
        }
    };

    @Override
    protected void onRestart() { // 用于恢复不可见之前的Activity状态
        super.onRestart();
        L.v(TAG, "onRestart");
        L.d(TAG, "isNeedRecoveryVideoLayout is " + isNeedRecoveryVideoLayout);
        isShareSuccessCount++;
        if (isShareSuccess && isShareSuccessCount == 1) { // 重新切回此界面时，判断是否进行了分享操作
            isShareSuccess = false;
            isShareSuccessCount = 100;
            addShareOrPlayCount(0); // http请求，添加分享次数
        }

        if (isNeedRecoveryVideoLayout) { // 用于播放网络视频时，按HOME键、熄灭屏幕恢复播放
            recoveryVideoLayout();
        }
    }

    @Override
    protected void onResume() { // onResume()时，不能够确定Activity的视图全部加载完成
        super.onResume();
        L.v(TAG, "onResume");
        initUserInfo();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) { // onWindowFocusChanged()时，才能够确定Activity的视图全部加载完成
        super.onWindowFocusChanged(hasFocus);
        L.v(TAG, "onWindowFocusChanged()----hasFocus is " + hasFocus);
        if (!isNeedRecoveryVideoLayout) { // 用于播放本地视频时，按HOME键、熄灭屏幕恢复播放
            swithToLandScapePlayLocalVideo();
            playVideo();
        }
    }

    /**
     * 功能：用户按HOME键或者熄屏后恢复播放界面的操作
     */
    private void recoveryVideoLayout() {
        if (!"".equals(mVideoPathLocal)) {
            swithToLandScapePlayLocalVideo();
        } else {
            if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == ACTIVITY_ORIENTATION) {
                switchToLandScapeWithRecovery();
            } else if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == ACTIVITY_ORIENTATION) {
                switchToPortrait();
            }
        }
        mPlayIB.setImageResource(R.drawable.player_btn_pause_style);
        // playVideoFromUri();
        playVideo();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.v(TAG, "onCreate()");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 主界面不显示标题栏
        setContentView(R.layout.activity_video_play);
        init();
        initContentString();
        if (mVideoUri != null) { // 播放的是网络视频路径则直接播放
            playVideo();
        }
        addShareOrPlayCount(1); // 打开一次播放页面，当前视频播放次数+1
    }

    private void initContentString() {
        if (mHotVideo != null) {
            // mTitleTV.setText(mHotVideo.getVideoname());
            initTitleTv(mHotVideo.getVideoname());
            mPlayCountTV.setText(mHotVideo.getCount() + "");
            mShareCountTV.setText(mHotVideo.getShare_count() + "");
        } else if (mVideoUploadResponse != null) {
            // mTitleTV.setText(mVideoUploadResponse.getvideoname());
            initTitleTv(mVideoUploadResponse.getvideoname());
            mPlayCountTV.setText(mVideoUploadResponse.getcount() + "");
            mShareCountTV.setText(mVideoUploadResponse.getshare_count() + "");
        }
    }

    private void initTitleTv(String title) {
        // mTitleTV.setText(title);
        mTitleTV.setVisibility(View.GONE);
        mMarqueeTextView.setText(title);
        mMarqueeTextView.setVisibility(View.VISIBLE);
        mMarqueeTextView.setWidth(DensityUtil.dip2px(200, PlayVideoActivity.this));
        mMarqueeTextView.setFocusable(true);
        mMarqueeTextView.setFocusableInTouchMode(true);
    }

    private Uri getVideoUri() {
        if (mHotVideo != null) {
            L.i(TAG, "当前播放的网络视频地址----" + mHotVideo.getName());
            return Uri.parse(mHotVideo.getName());
        } else {
            if (mVideoUploadResponse != null) {
                return Uri.parse(mVideoUploadResponse.getname());
            }
        }
        return null;
    }

    private void playVideo() {
        isNeedRecoveryVideoLayout = true; // 经过了OnCreate方法后，表明已经经过了第一次初始化
        if (mVideoUri != null || !"".equals(mVideoPathLocal)) {
            if (mVideoUri != null) { // 播放网络视频需要进行网络判断
                mVideoView.setVideoURI(mVideoUri);
                // 判断当前网络状态，再决定是否继续播放
                if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
                    Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
                    playVideoCommonMethod();
                } else {// 未开启wifi网络
                    new CustomAlertDialog(this).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                            .setMsg("WIFI网络未开启,是否继续使用2G、3G或4G网络!").setPositiveButton("确  认", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    playVideoCommonMethod();
                                }
                            }).setNegativeButton("取  消", new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
            } else if (!"".equals(mVideoPathLocal)) { // 播放本地视频无需进行网络判断
                mVideoView.setVideoPath(mVideoPathLocal);
                playVideoCommonMethod();
            }
        } else {
            L.e(TAG, "mVideoUri is " + mVideoUri + "; mVideoPathLocal is " + mVideoPathLocal);
        }
    }

    private void playVideoCommonMethod() {
        mVideoView.requestFocus();
        if (mLastPos > 0) { // 是否需要续播，进行判断
            mProgressSB.setProgress(mLastPos);
            mVideoView.seekTo(mLastPos);
        } else {
            mProgressSB.setProgress(0);
            mVideoView.seekTo(0);
        }
        mVideoView.start(); // 开始播放
        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hiddenControllerBar(); // 播放视频后5秒默认隐藏控制条
            }
        }, 5000);
    }

    private void close() {
        L.i(TAG, "close()");
        releaseSource();
        this.finish();
        // sendBroadcast(new
        // Intent(ConstantsUtil.ACTION_CHOOSE_LOCATION_ACTIVITY));
    }

    private void releaseSource() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView.suspend();
            mVideoView = null;
        }
        if (mUIHandler != null) {
            mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
            mUIHandler = null;
        }
        System.gc();
    }

    private void initVideoBean() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        HotVideo hotVideo = (HotVideo) bundle.get(ConstantsUtil.HOT_VIDEO);
        String localFilePath = intent.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH_LOCAL);
        mVideoUploadState = intent.getIntExtra(ConstantsUtil.VIDEO_FILE_UPLOAD_STATE, 0);
        mVideoNameLocal = intent.getStringExtra(ConstantsUtil.VIDEO_FILE_NAME_LOCAL);
        VideoUploadResponse videoUploadResponse = (VideoUploadResponse) bundle.get(ConstantsUtil.VIDEO_UPLOAD_RESPONSE);
        if (hotVideo != null) {
            mHotVideo = hotVideo;
            mVideoUri = getVideoUri();
            mVideoPathLocal = "";
        } else if (videoUploadResponse != null) {
            mVideoUploadResponse = videoUploadResponse;
            mVideoUri = getVideoUri();
            mVideoPathLocal = "";
        } else if (!"".equals(localFilePath)) {
            mVideoPathLocal = localFilePath;
            mVideoUri = null;
        }
    }

    private void initUserInfo() {
        mUserInfo = InitApplication.mSpUtil.getUser();
        if (mUserInfo != null) {
            mUserId = mUserInfo.getId(); // 用户已登录
        }
    }

    @SuppressLint("InflateParams")
    private void init() {
        initUserInfo();

        mProgressBarView = LayoutInflater.from(this).inflate(R.layout.custom_progressbar_dialog, null);

        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);

        initVideoBean();
        mTitleTV = (TextView) findViewById(R.id.tvTitle);
        mMarqueeTextView = (MarqueeTextView) findViewById(R.id.id_marquee_tv);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        mTitleLayout = (RelativeLayout) findViewById(R.id.id_title_bar_layout);
        mContentLayout = (RelativeLayout) findViewById(R.id.id_content_layout);
        mPlayCountTV = (TextView) findViewById(R.id.id_playcount_tv);
        mShareCountTV = (TextView) findViewById(R.id.id_sharecount_tv);
        mShareBtn = (Button) findViewById(R.id.id_share_btn);
        mShareBtn.setOnClickListener(ShareBtnClickListener);

        mBackAndUploadLayout = (RelativeLayout) findViewById(R.id.id_back_and_upload_layout);

        mVideoBackBtn = (RelativeLayout) findViewById(R.id.id_back_btn_layout);
        mVideoBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(mVideoPathLocal)) { // 当前为播放本地视频的状态则直接退出当前Activity
                    close();
                } else { // 当前为播放网络视频的状态则可以进行横竖屏切换
                    if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == ACTIVITY_ORIENTATION) { // 如果当前处于横屏模式，按返回键则返回竖屏模式
                        switchToPortrait();
                    }
                }
            }
        });

        mUploadBtn = (Button) findViewById(R.id.id_upload_btn);
        mUploadBtn.setOnClickListener(UploadBtnClickListener);

        return_imgbtn.setVisibility(View.VISIBLE);
        return_imgbtn.setOnClickListener(new OnClickListener() { // 返回按钮事件
            @Override
            public void onClick(View v) {
                close();
            }
        });

        initVideo();
    }

    private OnClickListener UploadBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!"".equals(mVideoPathLocal)) {
                if (mUserId != 0) { // 用户已登录，可以直接上传
                    Intent newIntent = new Intent(PlayVideoActivity.this, CustomUploadVideoTitleActivity.class);
                    newIntent.putExtra(ConstantsUtil.VIDEO_FILE_PATH, mVideoPathLocal);
                    startActivityForResult(newIntent, REQUEST_CUSTOM_UPLOAD_VIDEO_TITLE);
                } else { // 用户未登录，则跳转至用户登录界面
                    Toast.makeText(getApplicationContext(), "请先登录后上传视频", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PlayVideoActivity.this, OwnerPhoneRegisterActivity.class); // 打开登录页面
                    startActivityForResult(intent, REQUEST_OWNER_PHONE_REGISTER_ACTIVITY);
                }
            }
        }
    };

    private OnClickListener ShareBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            isShareSuccess = true;
            isShareSuccessCount = 0;
            shareVideoSocial();
        }
    };

    private void initVideo() {
        mControllerLayout = (LinearLayout) findViewById(R.id.id_controlbar_layout);
        mPlayIB = (ImageButton) findViewById(R.id.id_play_ib);
        setPlayIBClickListener();
        mCurrentTimeTV = (TextView) findViewById(R.id.id_time_current_tv);
        mTotalTimeTV = (TextView) findViewById(R.id.id_time_total_tv);
        mProgressSB = (SeekBar) findViewById(R.id.id_media_progress_sb);
        setSeekBarChangeListener();
        mFullScreeIV = (ImageView) findViewById(R.id.id_full_screen_iv);
        setFullScreenIVClickListener();
        mVideoLayout = (FrameLayout) findViewById(R.id.id_video_layout);

        mLoadRateTV = (TextView) findViewById(R.id.id_load_rate_tv);
        mVideoProgressBar = (ProgressBar) findViewById(R.id.id_progressbar);

        initVideoView();
    }

    private void initVideoView() {
        mVideoView = (CustomVideoView) findViewById(R.id.id_videoView);
        // 设置一张初始的视频播放图
        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) { // 视频播放前准备阶段
                L.i(TAG, "onPrepared");
                L.d(TAG, "当前播放的视频的总时间是：" + getFormatTime(mVideoView.getDuration()));
                mVideoProgressBar.setVisibility(View.GONE);
                updateTextViewWithTimeFormat(mTotalTimeTV, mVideoView.getDuration());
                mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
                mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
            }
        });
        mVideoView.setOnCompletionListener(new OnCompletionListener() { // 视频播放结束
            @Override
            public void onCompletion(MediaPlayer mp) {
                L.i(TAG, "onCompletion");
                mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
                // 播放完成直接重新继续播放
                updateTextViewWithTimeFormat(mCurrentTimeTV, 0);
                mProgressSB.setProgress(0);
                // playVideoFromUri();
                playVideo();
            }
        });
        mVideoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                L.i(TAG, "onError");
                mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
                return true;
            }
        });
        mVideoView.setOnInfoListener(new OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                L.i(TAG, "onInfo");
                // mp.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
                switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    if (mVideoView.isPlaying()) {
                        mVideoProgressBar.setVisibility(View.GONE);
                    }
                    break;
                }
                return true;
            }
        });
    }

    @SuppressWarnings("unused")
    private OnBufferingUpdateListener mOnBufferingUpdateListener = new OnBufferingUpdateListener() {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (percent < 100) {
                mLoadRateTV.setVisibility(View.VISIBLE);
            } else {
                mLoadRateTV.setVisibility(View.GONE);
            }
            mLoadRateTV.setText(percent + "%");
        }
    };

    private void setPlayIBClickListener() {
        mPlayIB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 切换播放与暂停
                L.d(TAG, "点击了播放按钮");
                if (mVideoView.isPlaying()) {
                    L.d(TAG, "暂停播放！");
                    mPlayIB.setImageResource(R.drawable.player_btn_play_style);
                    mVideoView.pause(); // 暂停播放
                    mLastPos = mVideoView.getCurrentPosition();
                } else {
                    L.d(TAG, "开始播放！");
                    mPlayIB.setImageResource(R.drawable.player_btn_pause_style);
                    // playVideoFromUri();
                    playVideo();
                }
            }
        });
    }

    private void setSeekBarChangeListener() {
        mProgressSB.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTextViewWithTimeFormat(mCurrentTimeTV, progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // SeekBar开始seek时停止更新
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int iseekPos = seekBar.getProgress();
                // SeekBark完成seek时执行seekTo操作并更新界面
                mVideoView.seekTo(iseekPos);
                L.v(TAG, "seek to " + iseekPos);
                mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
            }
        });
    }

    private void setFullScreenIVClickListener() {
        mFullScreeIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLargeAndSmallScreen(); // 切换全屏和小屏
            }
        });
    }

    /**
     * 功能：播放本地视频时需要切换至横屏模式，并不可切换为竖屏
     */
    private void swithToLandScapePlayLocalVideo() {
        if (!"".equals(mVideoPathLocal)) { // 播放本地视频时默认为横屏模式且不可切换
            // switchToLandScape();
            switchToLandScapeWithLocalPath();
            if (mVideoUploadState != 1) {
                mUploadBtn.setVisibility(View.VISIBLE);
            }
            mFullScreeIV.setVisibility(View.GONE);
        }
    }

    private void switchToLandScapeWithLocalPath() {
        L.d(TAG, "切换为【横屏】模式--播放本地视频");
        mTitleLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.GONE);

        int widthPixelPortrait = getWidthPixel(PlayVideoActivity.this);
        int heihtPixelPortrait = getHeightPixel(PlayVideoActivity.this);

        if (ACTIVITY_ORIENTATION != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 当前不处于横屏模式，才做切换动作
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 改变Activity屏幕模式
            ACTIVITY_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; // 保存当前的屏幕模式
        }
        int statusBarHeight = getStatusBarHeight(PlayVideoActivity.this);
        L.d(TAG, "获取的bar高度为：" + statusBarHeight);
        if (widthPixelLandScape == 0 || heightPixelLandScape == 0) {
            widthPixelLandScape = heihtPixelPortrait;
            heightPixelLandScape = widthPixelPortrait - statusBarHeight;
        }
        L.d(TAG, "获取的屏幕宽为：" + widthPixelLandScape + "; 获取的屏幕高为：" + heightPixelLandScape);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixelLandScape,
                heightPixelLandScape);
        mBackAndUploadLayout.setVisibility(View.VISIBLE);
        mFullScreeIV.setVisibility(View.VISIBLE);
        mFullScreeIV.setImageResource(R.drawable.ic_ratio_perfect); // 设置全屏按钮样式
        mVideoLayout.setLayoutParams(layoutParams);
    }

    /**
     * 功能：全屏/小屏切换方法
     */
    private void switchLargeAndSmallScreen() {
        if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == ACTIVITY_ORIENTATION) {
            switchToLandScape();
        } else if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == ACTIVITY_ORIENTATION) {
            switchToPortrait();
        }
    }

    /**
     * 功能：切换到横屏
     */
    private void switchToLandScape() {
        L.d(TAG, "切换为【横屏】模式");
        mTitleLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.GONE);

        if (ACTIVITY_ORIENTATION != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 当前不处于横屏模式，才做切换动作
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 改变Activity屏幕模式
            ACTIVITY_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; // 保存当前的屏幕模式
        }
        widthPixelLandScape = getWidthPixel(PlayVideoActivity.this);
        heightPixelLandScape = getHeightPixel(PlayVideoActivity.this) - getStatusBarHeight(PlayVideoActivity.this);
        L.d(TAG, "获取的屏幕宽为：" + widthPixelLandScape + "; 获取的屏幕高为：" + heightPixelLandScape);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixelLandScape,
                heightPixelLandScape);
        mBackAndUploadLayout.setVisibility(View.VISIBLE);
        mFullScreeIV.setVisibility(View.VISIBLE);
        mFullScreeIV.setImageResource(R.drawable.ic_ratio_perfect); // 设置全屏按钮样式
        mVideoLayout.setLayoutParams(layoutParams);
    }

    /**
     * 功能：熄灭屏幕后恢复横屏模式专用方法（熄灭屏幕后，获取的宽高与按HOME键时的值相反）
     */
    private void switchToLandScapeWithRecovery() {
        L.d(TAG, "切换为【横屏】模式");
        mTitleLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.GONE);
        if (ACTIVITY_ORIENTATION != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 当前不处于横屏模式，才做切换动作
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 改变Activity屏幕模式
            ACTIVITY_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; // 保存当前的屏幕模式
        }
        LinearLayout.LayoutParams layoutParams = null;
        if (widthPixelLandScape == 0 || heightPixelLandScape == 0) {
            widthPixelLandScape = getWidthPixel(PlayVideoActivity.this);
            heightPixelLandScape = getHeightPixel(PlayVideoActivity.this) - getStatusBarHeight(PlayVideoActivity.this);
        }
        layoutParams = new LinearLayout.LayoutParams(widthPixelLandScape, heightPixelLandScape);
        L.d(TAG, "当前使用的屏幕宽为：" + widthPixelLandScape + "; 当前使用的屏幕高为：" + heightPixelLandScape);
        mFullScreeIV.setVisibility(View.VISIBLE);
        mFullScreeIV.setImageResource(R.drawable.ic_ratio_perfect); // 设置全屏按钮样式
        mVideoLayout.setLayoutParams(layoutParams);
    }

    /**
     * 功能：切换到竖屏
     */
    private void switchToPortrait() {
        L.d(TAG, "切换为【竖屏】模式");
        if (ACTIVITY_ORIENTATION != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) { // 当前不处于竖屏模式，才做切换动作
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ACTIVITY_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        int widthPixelPortrait = getWidthPixel(PlayVideoActivity.this);
        int heihtPixelPortrait = DensityUtil.dip2px(200, PlayVideoActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixelPortrait, heihtPixelPortrait);
        L.d(TAG, "获取的屏幕宽为：" + widthPixelPortrait + "; 获取的屏幕高为：" + heihtPixelPortrait);
        mBackAndUploadLayout.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mFullScreeIV.setImageResource(R.drawable.ic_ratio_full);
        mVideoLayout.setLayoutParams(layoutParams);
    }

    /**
     * 功能：社会化分享（友盟实现）
     */
    private void shareVideoSocial() {
        L.i(TAG, "shareVideoSocial()");
        String url = "";
        if (mHotVideo != null) {
            url = TEMP_URL/* VolleyManager.SERVER_URL */ + VolleyManager.SHARED + mHotVideo.getId();
        } else if (mVideoUploadResponse != null) {
            url = TEMP_URL/* VolleyManager.SERVER_URL */ + VolleyManager.SHARED + mVideoUploadResponse.getid();
        }
        UMVideo video = new UMVideo(url);
        String videoname = null;
        try {
            if (mHotVideo != null) {
                videoname = mHotVideo.getVideoname().substring(0, mHotVideo.getVideoname().indexOf("_"));
            } else if (mVideoUploadResponse != null) {
                videoname = mVideoUploadResponse.getvideoname().substring(0,
                        mVideoUploadResponse.getvideoname().indexOf("_"));
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            L.e(TAG, e.getMessage());
        }
        if (TextUtils.isEmpty(videoname)) {
            if (mHotVideo != null) {
                videoname = mHotVideo.getVideoname();
            } else if (mVideoUploadResponse != null) {
                videoname = mVideoUploadResponse.getvideoname();
            }
        }

        video.setDescription(videoname);
        String title = InitApplication.mSpUtil.getSp().getString("activeNote", null);
        if (title == null) {
            title = "分享了一个视频给您";
        }
        video.setTitle(title);
        // 设置分享展示的缩略图
        if (mHotVideo != null) {
            video.setThumb(mHotVideo.getImg());
        }else if (mVideoUploadResponse != null){
            video.setThumb(mVideoUploadResponse.getimg());
        }
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withText("舞媚娘App帮我制作的" + "《" + videoname + "》" + ",快帮我点赞吧!").withMedia(video).withTitle(title)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            L.d(TAG, "onResult----" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                L.d(TAG, "onResult----" + platform + " 收藏成功！");
                Toast.makeText(InitApplication.getRealContext(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                L.d(TAG, "onResult----" + platform + " 分享成功！");
                Toast.makeText(InitApplication.getRealContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                // 分享成功，则提交HTTP请求，增加此视频的分享次数 （由于分享时，APP被挂至后台，无法接收返回数据，所以此处不会抵达）
                // addShareCount();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            L.e(TAG, "onError----" + platform + " 分享失败了！");
            Toast.makeText(InitApplication.getRealContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            L.e(TAG, "onCancel----" + platform + " 分享取消了！");
            Toast.makeText(InitApplication.getRealContext(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 功能：增加被分享视频的分享次数或者增加视频的播放次数
     * 
     * @param requestType
     *            为1表示增加播放次数，为0表示增加分享次数
     */
    private void addShareOrPlayCount(final int requestType) {
        L.d(TAG, "addShareCount()");
        AddShareOrPlayCountInfo.Request request = null;
        if (mHotVideo != null) {
            request = new AddShareOrPlayCountInfo.Request(mHotVideo.getId(), requestType);
        } else if (mVideoUploadResponse != null) {
            request = new AddShareOrPlayCountInfo.Request(mVideoUploadResponse.getid(), requestType);
        }

        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_ADD_SHARE_COUNT,
                new OnResponseListener<AddShareOrPlayCountInfo.Response>(AddShareOrPlayCountInfo.Response.class) {

                    @Override
                    protected void handleResponse(
                            com.android.app.showdance.model.glmodel.AddShareOrPlayCountInfo.Response response) {
                        if (response != null) {
                            if (requestType == 0) {
                                if (response.isFlag()) {
                                    L.d(TAG, "视频分享【成功】！");
                                    Toast.makeText(InitApplication.getRealContext(), "视频分享【成功】！", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    L.e(TAG, "视频分享【失败】！");
                                    Toast.makeText(InitApplication.getRealContext(), "视频分享【失败】！", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } else {
                                if (response.isFlag()) {
                                    L.d(TAG, "增加视频播放次数成功！");
                                } else {
                                    L.e(TAG, "增加视频播放次数失败！");
                                }
                            }
                        } else {
                            L.e(TAG, "AddShareOrPlayCountInfo.Response error!");
                        }
                    }
                }, mErrorListener);
    }

    public abstract class OnResponseListener<T> extends VolleyManager.ResponeListener<T> {
        public OnResponseListener(Class<T> c) {
            super(c);
        }

        @Override
        public void onMyResponse(T response) {
            handleResponse(response);
        }

        public void onResponseFail(com.android.app.showdance.model.glmodel.ResponseFail response) {
            L.e(TAG, "onResponseFail()");
            if (response != null) {
                L.e(TAG, response.getMessage());
            }
            handleFailResponse(response);
        }

        protected void handleFailResponse(com.android.app.showdance.model.glmodel.ResponseFail response) {

        }

        protected abstract void handleResponse(T response);
    };

    private VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            L.d(TAG, "onErrorResponse----" + error.toString());
            mUploadBtn.setVisibility(View.GONE);
            dismissDialog();
            handleErrorResponse(error);
        };
    };

    private void handleErrorResponse(com.android.volley.VolleyError error) {

    }

    /**
     * 功能：更新显式的时间
     * 
     * @param timeView
     * @param sec
     */
    private void updateTextViewWithTimeFormat(TextView timeView, int sec) {
        timeView.setText(getFormatTime(sec));
    }

    /**
     * 功能：格式化时间
     * 
     * @param sec
     * @return
     */
    @SuppressLint("DefaultLocale")
    private String getFormatTime(int sec) {
        int second = sec / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }

    /**
     * 功能：更新视频播放控制界面的显示/隐藏（触摸屏幕用）
     * 
     * @param isBarShow
     */
    private void updateControlBar(boolean isBarShow) {
        if (isBarShow) {
            hiddenControllerBar();
        } else {
            showControllerBar();
        }
    }

    /**
     * 功能：显示视频控制条
     */
    private void showControllerBar() {
        if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == ACTIVITY_ORIENTATION) { // 如果当前处于横屏模式，按返回键则返回竖屏模式
            mBackAndUploadLayout.setVisibility(View.GONE);
        }
        mControllerLayout.setVisibility(View.GONE);
        isControllerBarShow = true;
    }

    /**
     * 功能：隐藏视频控制条
     */
    private void hiddenControllerBar() {
        if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == ACTIVITY_ORIENTATION) { // 如果当前处于横屏模式，按返回键则返回竖屏模式
            mBackAndUploadLayout.setVisibility(View.VISIBLE);
        }
        mControllerLayout.setVisibility(View.VISIBLE);
        isControllerBarShow = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.v(TAG, "onPause()");
        EventBus.getDefault().unregister(this);
        // 在停止播放前先记录当前播放的位置,以便以后可以续播
        if (mUIHandler != null) {
            mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        }
        if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
            if (mVideoView != null) {
                mLastPos = mVideoView.getCurrentPosition(); // 存入当前的播放进度位置
                mVideoView.stopPlayback(); // 停止播放
            }
        }
        if (mPlayIB != null) {
            mPlayIB.setImageResource(R.drawable.player_btn_play_style);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.v(TAG, "onStop()");
    }

    /**
     * 功能：触摸屏幕显示/隐藏播放控制条
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchTime = System.currentTimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            long time = System.currentTimeMillis() - mTouchTime;
            if (time < 400) {
                updateControlBar(isControllerBarShow);
            }
        }
        return true;
    }

    public int getHeightPixel(Activity activity) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.heightPixels;
    }

    public int getWidthPixel(Activity activity) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }

    public int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 重写android主菜单中的返回键
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK: // 返回键重写
            if (!"".equals(mVideoPathLocal)) { // 当前为播放本地视频的状态则直接退出当前Activity
                close();
            } else { // 当前为播放网络视频的状态则可以进行横竖屏切换
                if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == ACTIVITY_ORIENTATION) { // 如果当前处于横屏模式，按返回键则返回竖屏模式
                    switchToPortrait();
                } else if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == ACTIVITY_ORIENTATION) { // 如果当前处于竖屏模式，按返回键则关闭此Activity
                    close();
                }
            }
            return true;
        case KeyEvent.KEYCODE_VOLUME_UP: // 音量增加重写
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            return true;
        case KeyEvent.KEYCODE_VOLUME_DOWN: // 音量减少重写
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.v(TAG, "onDestroy()");
        releaseSource();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d(TAG, "onActivityResult()");
        if (requestCode == REQUEST_CUSTOM_UPLOAD_VIDEO_TITLE && resultCode == RESULT_OK) {
            mVideoPathLocal = data.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH);
            mVideoNameLocal = data.getStringExtra(ConstantsUtil.VIDEO_FILE_NAME_LOCAL);
            isJoinInTheActivitiy = data.getBooleanExtra(ConstantsUtil.IS_JOIN_IN_THE_ACTIVITY, false);
            showSizeProgressDialog(PlayVideoActivity.this,
                    new UploadEvent(0.00, data.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH), null, null));
        } else if (requestCode == REQUEST_OWNER_PHONE_REGISTER_ACTIVITY && resultCode == RESULT_OK) {
            initUserInfo(); // 重新获取一下用户登录信息
            compareVideoIsUploaded();
        }
    }

    /**
     * 功能：用于未登录用户在播放视频界面点按上传按钮后，返回播放界面，对当前播放的视频判断是否上传过
     */
    @SuppressLint("ShowToast")
    public void compareVideoIsUploaded() {
        if (mUserId != 0) {
            mDialog = new AlertDialog.Builder(this).create();
            mDialog.show();
            // 注意此处要放在show之后 否则会报异常
            mDialog.setContentView(R.layout.loading_progressbar_dialog);
            mDialog.setCanceledOnTouchOutside(true);

            VideoUploadInfo.ShowRequest request = new VideoUploadInfo.ShowRequest();
            request.setpageNumber(Integer.MAX_VALUE);
            request.setuser_id(mUserId);
            VolleyManager.getInstance().postRequest(request,
                    BaseRequest.UPLOAD_CLIENT_ID + "/" + InitApplication.mSpUtil.getUser().getuser_token()
                            + "/videoList",
                    new OnResponseListener<VideoUploadInfo.ShowResponse>(VideoUploadInfo.ShowResponse.class) {
                        @Override
                        protected void handleResponse(ShowResponse response) {
                            dismissDialog();
                            List<VideoUploadResponse> videoUploadResponseList = response.getData().getdata();
                            Log.d(TAG, "mVideoUploadList size " + videoUploadResponseList.size());
                            for (VideoUploadResponse videoUploadResponse : videoUploadResponseList) {
                                String videoPath = videoUploadResponse.getpath();
                                String videoName = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                                if (videoName.equals(mVideoNameLocal)) { // 当前播放的视频上传过，需要隐藏上传按钮
                                    mUploadBtn.setVisibility(View.GONE);
                                    Toast.makeText(InitApplication.getRealContext(), "您已上传过该视频!", 2000).show();
                                }
                            }
                        }

                        @Override
                        protected void handleFailResponse(ResponseFail response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissDialog();
                                    mUploadBtn.setVisibility(View.GONE); // 默认隐藏上传按钮
                                    Toast.makeText(InitApplication.getRealContext(), "服务器请求失败!", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    }, mErrorListener);
        }
    }

    public void onEventMainThread(UploadEvent event) {
        L.w(TAG, "onEventMainThread()----当前的event percent是：" + event.percent);
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        showSizeProgressDialog(PlayVideoActivity.this, event); // 上传状态:当前进度
    }

    /**
     * 功能：HTTP接口上传文件对话框
     */
    public void showProgressDialogFile(Context mContext, String filePath) {
        if (token == null)
            return;
        Intent intent = new Intent(PlayVideoActivity.this, UploadViedoService.class);
        intent.putExtra("token", token);
        intent.putExtra(ConstantsUtil.VIDEO_FILE_PATH, filePath);
        // mIntent.putExtra("remark", descriptive);
        intent.putExtra("id", mUserId);
        startService(intent);
    }

    /**
     * @Description:HTTP接口正在上传文件进度对话框
     */
    public void showSizeProgressDialog(Context mContext, UploadEvent event) {
        MyProgressBar uploading_proressbar;
        L.d(TAG, "loadingFlag is " + loadingFlag);
        if (loadingFlag) {
            getQiniuToken(event.filepath);
            dismissDialog();
            if (mUploadingFileDialog != null) {
                mUploadingFileDialog.dismiss();
            }
            mUploadingFileDialog = new AlertDialog.Builder(mContext).create();
            loadingFlag = false;
            mUploadingFileDialog.show();
            uploading_proressbar = (MyProgressBar) mProgressBarView.findViewById(R.id.uploading_proressbar);
            uploading_proressbar.setMax(100);
            uploading_proressbar.setProgress(0);
            mUploadingFileDialog.setContentView(mProgressBarView);
            mUploadingFileDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
        }
        double Progress = event.percent * 100;
        L.i(TAG, "当前的Progress是：" + Progress);
        uploading_proressbar = (MyProgressBar) mProgressBarView.findViewById(R.id.uploading_proressbar);
        if (uploading_proressbar == null)
            return;

        if (uploading_proressbar.getProgress() < Progress)
            uploading_proressbar.setProgress((int) Progress);

        if (Progress >= 100.00 && event.newname != null) { // 上传完成
            loadingFlag = true;
            token = null;
            // 关闭正在上传...的对话框
            if (mUploadingFileDialog != null) {
                mUploadingFileDialog.dismiss();
            }
            Toast.makeText(PlayVideoActivity.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
            // 打开正在响应...的对话框

            VideoUploadInfo.Request request = null;
            String theme = "";
            if (isJoinInTheActivitiy) {
                theme = InitApplication.mSpUtil.getTheme();
            }
            request = new VideoUploadInfo.Request(event.newname, mUserInfo.getId(), event.oldname, event.filepath,
                    InitApplication.mSpUtil.getProvinceName(), InitApplication.mSpUtil.getCityName(),
                    InitApplication.mSpUtil.getLongitude(), InitApplication.mSpUtil.getLatitude(), theme);
            VolleyManager.getInstance().postRequest(request, // 上传成功后提交http请求，添加视频文件信息
                    VolleyManager.CLIENT_ID + "/" + mUserInfo.getuser_token() + "/quniuVideo",
                    new OnResponseListener<VideoUploadInfo.Response>(VideoUploadInfo.Response.class) {

                        @Override
                        protected void handleResponse(VideoUploadInfo.Response response) {
                            if (response.getFlag()) {
                                InitApplication.mSpUtil.setMineFragmentNeedRefresh(true); // 上传成功后，让“我的”界面可以自动重新获取数据
                                mUploadBtn.setVisibility(View.GONE); // 上传成功后，需要隐藏上传按钮
                            }
                        }

                    }, mErrorListener);
        } else if (Progress < 0) {
            L.e(TAG, "当前的Progress为：" + Progress);
            dismissDialog();
        }

    }

    public void getQiniuToken(final String filePath) {
        StringRequest request = new StringRequest(Method.POST, "http://112.74.83.166:82/api/qiniuToken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String json = response.toString();
                        Log.d(TAG, "onResponse " + json);
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            BaseResponse result = objectMapper.readValue(json.substring(json.indexOf("{")),
                                    BaseResponse.class);
                            if (!result.getFlag())
                                return;
                            token = result.getData();
                            showProgressDialogFile(PlayVideoActivity.this, filePath);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            if (mDialog != null) {
                                mDialog.setCancelable(true);
                            }
                        } finally {
                            // token = null;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        dismissDialog();
                        Log.d(TAG, "onErrorResponse " + arg0.toString());
                        Toast.makeText(PlayVideoActivity.this, getString(R.string.getqiniutoken_fail),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        VolleyManager.getInstance().getRequestQueue().add(request);
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
