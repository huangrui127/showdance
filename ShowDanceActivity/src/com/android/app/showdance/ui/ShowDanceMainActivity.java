package com.android.app.showdance.ui;

import java.io.File;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.android.app.showdance.bottombar.BottomBar;
import com.android.app.showdance.bottombar.BottomBarClickListener;
import com.android.app.showdance.fragment.MainFragment;
import com.android.app.showdance.fragment.MineFragment;
import com.android.app.showdance.fragment.ShootFragment;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity;
import com.android.app.showdance.utils.AMapLocationUtils;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.wumeiniang.app.SocialSdkHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import gl.live.danceshow.ui.camera.CameraPreviewActivity;

/**
 * 功能：主Activity
 * 
 * @author djd
 */
@SuppressLint("NewApi")
public class ShowDanceMainActivity extends BaseActivity {

    private static final String TAG = "ShowDanceMainActivity";
    private static final int DELAY_MILLIS = 3000;

    private static final int VIDEO_CAPTURE = 2;
    // private static final int CHOOSE_LOCATION = 3;
    public static final int REQUEST_RECORD = 0;
    public static final int REQUEST_VIDEO_REVIEW = 1;
    public static final int REQUEST_EDIT = 6;
    public static final int REQUEST_OWNER_PHONE_REGISTER_ACTIVITY = 333;

    private static String[] mActionArray = { // 用于接收对应Activity广播的action
            ConstantsUtil.ACTION_SHOW_DANCE_MAIN_ACTIVITY, ConstantsUtil.ACTION_DOWNLOADED_MUSIC_ACTIVITY,
            ConstantsUtil.ACTION_RECORDED_VIDEO_ACTIVITY, ConstantsUtil.ACTION_USE_INTRODUCTION_ACTIVITY,
            ConstantsUtil.ACTION_FOUND_SEARCH_MUSIC_ACTIVITY, ConstantsUtil.ACTION_OWNER_PHONE_REGISTER_ACTIVITY,
            ConstantsUtil.ACTION_TEACHER_ACTIVITY, ConstantsUtil.ACTION_MINE_FRAGMENT,
            ConstantsUtil.ACTION_SHOW_MEDIARECORDER, ConstantsUtil.ACTION_MAIN_CATEGORY_ACTIVITY };

    private BottomBar mBottomBar;
    private FragmentManager mFragmentManager;
    private Class<? extends Fragment> mCurrentFragmentClass; // 当前界面处于的Fragment类
    /******************** 顶部bar *********************/
    private TextView mTitleTV; // 顶部bar的标题
    protected TextView mRecommendBtn; // 顶部bar的最左侧按钮
    private LinearLayout mGroup_ll; // 顶部bar中间部分
    private Button mDownloadedMusicBtn, mRecordedVideoBtn, mInfoHelpBtn; // “已下载舞曲”、“本地视频”、“帮助”按钮
    /******************** 定位相关 *********************/
    private AMapLocationClient mLocationClient;
    // private AMapLocationClientOption mLocationOption;
    private RelativeLayout mDropLayout; // 顶部bar左侧定位按钮
    private TextView mDropTV; // 顶部bar左侧定位文字

    private boolean exitFlag; // 是否可关闭Activity的标记

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    /**
     * 需要进行检测的权限数组
     */
    private static String[] locationPermissions = { Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION };

    @Override
    protected void onResume() {
        super.onResume();
        L.i(TAG, "onResume");
        if (ContextCompat.checkSelfPermission(this, // 申请定位相关权限
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            ActivityCompat.requestPermissions(this, locationPermissions, 0);
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            // 申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA }, 1);
        }
        if (ContextCompat.checkSelfPermission(this, // 申请摄像头权限
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA }, 2);
        }
        startLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 主界面不显示标题栏
        setContentView(R.layout.activity_main_showdance);
        startMainService();
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);
        init();
        initView();
        setOnClickListener();
        initBottomBar();
        // registerActivityReceiver();
        openContent();
        updateUserInfo();
    }

    private void startMainService() {
        int isCreated = InitApplication.mSpUtil.getIsCreated();
        if (isCreated != 0) {
            creatShortCut();
        }
    }

    private void creatShortCut() {
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        String title = getResources().getString(R.string.app_name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.app_logo);
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        Intent loginIntent = new Intent(this, SplashActivity.class);
        loginIntent.setAction("android.intent.action.MAIN");
        loginIntent.addCategory("android.intent.category.LAUNCHER");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, loginIntent);
        sendBroadcast(addIntent);
        InitApplication.mSpUtil.setIsCreated(0);
    }

    public static class UpdateInfoRequest {
        private String string;
        private int type;

        public void setstring(String arg) {
            string = arg;
        }

        public String getstring() {
            return string;
        }

        public void settype(int arg) {
            type = arg;
        }

        public int gettype() {
            return type;
        }
    }

    private void updateUserInfo() {
        boolean buploadinfo = InitApplication.mSpUtil.getSp().getBoolean("uploadinfo1", false);
        if (buploadinfo)
            return;
        final User user = InitApplication.mSpUtil.getUser();

        if (user == null) {
            return;
        }
        InitApplication.mSpUtil.getSp().edit().putBoolean("uploadinfo1", true);
        MobclickAgent.reportError(this, "user phone = " + user.getPhone());
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setstring(getAppVersionName(ShowDanceMainActivity.this) + ";product:" + Build.PRODUCT + ";phone:"
                + user.getPhone());
        request.settype(10);
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_COUNT, null, mErrorListener);
    }

    protected VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            Log.d(TAG, "" + error.toString());
            handleErrorResponse(error);
        };
    };

    protected void handleErrorResponse(com.android.volley.VolleyError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mDialog != null)
                        mDialog.dismiss();
                    mDialog = null;
                } catch (Exception e) {
                    mDialog = null;
                }
            }
        });

    }

    private String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            @SuppressWarnings("unused")
            int versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    @Override
    protected String[] initActions() {
        return mActionArray;
    }

    /**
     * 功能：Activity中Fragment界面更新方法
     */
    private void openContent() {
        Bundle bundle = this.getIntent().getExtras();
        int contentId = ConstantsUtil.OPEN_MAIN_CONTENT;
        if (bundle != null) {
            contentId = bundle.getInt(ConstantsUtil.OPEN_ACTIVITY, ConstantsUtil.OPEN_MAIN_CONTENT);
        }
        selectContent(contentId);
    }

    private void selectContent(int contentId) {
        switch (contentId) {
        case ConstantsUtil.OPEN_MAIN_CONTENT:
            L.d(TAG, "OPEN_MAIN_CONTENT");
            openMainContent();
            break;
        case ConstantsUtil.OPEN_SHOOT_CONTENT:
            L.d(TAG, "OPEN_SHOOT_CONTENT");
            openShootContent();
            break;
        case ConstantsUtil.OPEN_MINE_CONTENT:
            L.d(TAG, "OPEN_MINE_CONTENT");
            openMineContent();
            break;
        }
    }

    private void initBottomBar() { // 初始化底部导航栏
        mBottomBar = new BottomBar(getApplicationContext(), (RelativeLayout) findViewById(R.id.id_bottom_bar_layout));
        mBottomBar.setBottomBarClickListener(new BottomBarClickListener() {

            @Override
            public void shootClick() {
                openShootContent();
            }

            @Override
            public void mineClick() {
                openMineContent();
            }

            @Override
            public void mainClick() {
                openMainContent();
            }
        });
    }

    private void openMainContent() {
        L.i(TAG, "openMainContent");
        startLocation();
        initMainTopBar();
        changeTitleBarTitleString(getString(R.string.menu_main));
//        commitFragment(R.id.id_main_frame, MainFragment.newInstance());
        switchContent(mCurrentFragmentClass, MainFragment.class);
        mBottomBar.mainBtnClickableColorChange();
    }

    private void openShootContent() {
        L.i(TAG, "openShootContent");
        initShootTopBar();
        changeTitleBarTitleString(getString(R.string.menu_video_shoot));
//        commitFragment(R.id.id_main_frame, ShootFragment.newInstance());
        switchContent(mCurrentFragmentClass, ShootFragment.class);
        mBottomBar.shootBtnClickableColorChange();
    }

    private void openMineContent() {
        L.i(TAG, "openMineContent");
        if (InitApplication.mSpUtil.getUser() != null) { // 用户已登录
            L.d(TAG, "commitFragment(R.id.id_main_frame, MineFragment.newInstance())");
            initMineTopBar();
            changeTitleBarTitleString(getString(R.string.personal_center));
//            commitFragment(R.id.id_main_frame, MineFragment.newInstance());
            switchContent(mCurrentFragmentClass, MineFragment.class);
            mBottomBar.mineBtnClickableColorChange();
        } else { // 用户未登录，需要首先进行注册或者登陆
            L.d(TAG, "openOwnerPhoneRegisterActivity");
            openOwnerPhoneRegisterActivity();
        }
    }

    private void switchContent(Class<? extends Fragment> fromFragmentClass, Class<? extends Fragment> toFragmentClass) {

        String fromFragmentTag = fromFragmentClass.getSimpleName();
        String toFragmentTag = toFragmentClass.getSimpleName();

        Fragment fromFragment = mFragmentManager.findFragmentByTag(fromFragmentTag);
        Fragment toFragment = mFragmentManager.findFragmentByTag(toFragmentTag);

        try {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            if (fromFragment == null) {
                fromFragment = fromFragmentClass.newInstance();
            }
            if (fromFragmentClass == toFragmentClass) { // 传入的两个相同的参数，表示需要刷新当前的Fragment
                if (fromFragment.isAdded()) {
                    InitApplication.mSpUtil.setMainFragmentNeedRefresh(true); // 此时，由于Fragment是复用的，需要告诉Fragment此时需要进行数据刷新
                    mFragmentTransaction.show(fromFragment).commitAllowingStateLoss();
                }else {
                    mFragmentTransaction.add(R.id.id_main_frame, fromFragment, fromFragmentTag).commitAllowingStateLoss();
                }
            } else { // 非第一次打开应用，则肯定存在
                if (toFragment == null) {
                    toFragment = toFragmentClass.newInstance();
                }
                if (!toFragment.isAdded()) {
                    mFragmentTransaction.hide(fromFragment).add(R.id.id_main_frame, toFragment, toFragmentTag); // 传入Fragment对应的Tag，以便可以查找到是否添加过
                } else {
                    mFragmentTransaction.hide(fromFragment).show(toFragment);
                }
                mFragmentTransaction.commitAllowingStateLoss(); // 不保留状态提交事务
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCurrentFragmentClass = toFragmentClass; // 将切换到的Fragment设置为当前的Fragment类
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        exitFlag = false;
        initUserChooseLocationName();
        initAMapLocation();
    }

    private void initUserChooseLocationName() {
        String tempName = InitApplication.mSpUtil.getUserChooseLocationName();
        if (!"".equals(tempName)) {
            InitApplication.mSpUtil.setUserChooseLocationName(""); // 如果进入程序时存在用户选择的数据则清空
        }
    }

    @Override
    protected void initView() {
        // 顶部导航标题
        mTitleTV = (TextView) findViewById(R.id.tvTitle);
        // 顶部导航最左边按钮
        mRecommendBtn = (TextView) findViewById(R.id.cancel_tv);
        // 顶部导航中间的“已下载舞曲”与“本地视频”
        mGroup_ll = (LinearLayout) findViewById(R.id.group_ll);
        mDownloadedMusicBtn = (Button) findViewById(R.id.downloadedMusicBtn);
        mRecordedVideoBtn = (Button) findViewById(R.id.recordedVideoBtn);
        // 顶部最右侧帮助按钮
        mInfoHelpBtn = (Button) findViewById(R.id.login_status);
        // 顶部左侧定位按钮
        mDropLayout = (RelativeLayout) findViewById(R.id.id_drop_layout);
        mDropTV = (TextView) findViewById(R.id.id_drop_tv);
        // mDropIV = (ImageView) findViewById(R.id.id_drop_iv);

        mCurrentFragmentClass = MainFragment.class; // 默认打开的主页Fragment

    }

    private void initAMapLocation() {
        resetLocation();
        startLocation();
    }

    AMapLocationListener amapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            String provinceStr = "";
            String cityStr = "";
            String longitude = ""; // 经度
            String latitude = ""; // 纬度
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) { // 定位成功
                    L.i(TAG, "Location success!");
                    provinceStr = amapLocation.getProvince(); // 定位获取的当前省名称
                    cityStr = amapLocation.getCity(); // 定位获取的当前市名称
                    longitude = amapLocation.getLongitude() + "";
                    latitude = amapLocation.getLatitude() + "";
                    L.i(TAG, "获取的省份是：" + provinceStr + "； 获取的市是：" + cityStr + "；获取的经度是：" + longitude + "；获取的经度是："
                            + latitude);
                    if (!"".equals(provinceStr)) {
                        InitApplication.mSpUtil.setProvinceName(provinceStr);
                    }
                    if (!"".equals(cityStr)) {
                        InitApplication.mSpUtil.setCityName(cityStr);
                    }
                    if (!"".equals(longitude)) {
                        InitApplication.mSpUtil.setLongitude(longitude);
                    }
                    if (!"".equals(latitude)) {
                        InitApplication.mSpUtil.setLatitude(latitude);
                    }
                    updateProvinceAndCityName();
                } else { // 定位失败
                    L.e(TAG, "Location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
            if (!"".equals(provinceStr) || !"".equals(cityStr)) {
                stopLocation();
            }
        }
    };

    private void resetLocation() {
        mLocationClient = new AMapLocationClient(InitApplication.getRealContext());
        mLocationClient.setLocationOption(AMapLocationUtils.getDefaultOption()); // 设置默认定位参数
        mLocationClient.setLocationListener(amapLocationListener);
    }

    /**
     * 功能：开始定位
     */
    private void startLocation() {
        L.i(TAG, "startLocation");
        if (mLocationClient == null) {
            resetLocation();
        }
        // if (!mLocationClient.isStarted()) {
        // mLocationClient.startLocation();
        // }
        mLocationClient.startLocation();
    }

    /**
     * 功能：停止定位
     */
    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 功能：销毁定位
     */
    private void destroyLocation() {
        if (mLocationClient != null) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    private void initMainTopBar() {
        universalInit();
        mDropLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 功能：更新显示定位出来的省市名称
     */
    private void updateProvinceAndCityName() {
        L.i(TAG, "updateProvinceAndCityName()----更新位置信息！");
        String province = InitApplication.mSpUtil.getProvinceName();
        String city = InitApplication.mSpUtil.getCityName();
        String userChooseLocationName = InitApplication.mSpUtil.getUserChooseLocationName();
        if (!"".equals(userChooseLocationName) && !userChooseLocationName.equals("定位")) { // 优先以用户选择的显示位置为准
            mDropTV.setText(userChooseLocationName);
        } else if (!"".equals(province)) {
            mDropTV.setText(province);
        } else if (!"".equals(city)) {
            mDropTV.setText(city);
        } else { // 定位失败，默认显示全国榜
            mDropTV.setText("全国");
        }
    }

    private void initMineTopBar() {
        universalInit();
        mDropLayout.setVisibility(View.GONE);
    }

    private void universalInit() {
        mRecommendBtn.setVisibility(View.GONE);
        mTitleTV.setVisibility(View.VISIBLE);
        mGroup_ll.setVisibility(View.GONE);
        mInfoHelpBtn.setVisibility(View.GONE);
    }

    private void initShootTopBar() {
        // 初始化各个按钮的显示状态
        mRecommendBtn.setVisibility(View.VISIBLE);
        mTitleTV.setVisibility(View.GONE);
        mGroup_ll.setVisibility(View.VISIBLE);
        mInfoHelpBtn.setVisibility(View.VISIBLE);
        mDropLayout.setVisibility(View.GONE);
        // 初始化左侧推荐按钮并显示
        mRecommendBtn.setText(getString(R.string.shoot_recommend_left_button));
        mRecommendBtn.setTextSize(15);
        // 初始化右侧帮助按钮
        mInfoHelpBtn.setText("");
        mInfoHelpBtn.setBackgroundResource(R.drawable.selector_tabhost_help);
    }

    @Override
    protected void setOnClickListener() {
        mRecommendBtn.setOnClickListener(new SocialSdkHandler(this));
        mDownloadedMusicBtn.setOnClickListener(this);
        mRecordedVideoBtn.setOnClickListener(this);
        mInfoHelpBtn.setOnClickListener(this);
        mDropLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.login_status: // 顶部右侧帮助按钮infoHelpBtn
            intent.setClass(ShowDanceMainActivity.this, UseIntroductionActivity.class);
            String helpUrl = InitApplication.mSpUtil.getSp().getString("helpurl", null);
            if (helpUrl == null) {
                makeToast(getApplicationContext(), R.string.info_help_cue);
                return;
            }
            intent.setData(Uri.parse(helpUrl));
            startActivity(intent); // 点击按钮，跳转至help界面
            break;
        case R.id.downloadedMusicBtn: // 已下载视频
            intent.setClass(ShowDanceMainActivity.this, DownloadedMusicActivity.class);
            startActivity(intent);
            break;
        case R.id.recordedVideoBtn: // 本地视频
            intent.setClass(ShowDanceMainActivity.this, RecordedVideoActivity.class);
            startActivity(intent);
            break;
        case R.id.id_drop_layout: // 顶部左侧导航按钮
            intent.setClass(ShowDanceMainActivity.this, ChooseLocationActivity.class);
            // startActivity(intent);
            startActivityForResult(intent, ConstantsUtil.CHOOSE_LOCATION);
            break;
        default:
            break;
        }
    }

    @Override
    protected void findViewById() {

    }

    @Override
    public void refresh(Object... param) {

    }

    @Override
    protected boolean validateData() {
        return false;
    }

    private void changeTitleBarTitleString(String titleStr) {
        mTitleTV.setText(titleStr);
    }

    private void openOwnerPhoneRegisterActivity() {
        Intent intent = new Intent(ShowDanceMainActivity.this, OwnerPhoneRegisterActivity.class);
        startActivityForResult(intent, REQUEST_OWNER_PHONE_REGISTER_ACTIVITY);
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        String actionStr = intent.getAction();
        if (ConstantsUtil.ACTION_SHOW_DANCE_MAIN_ACTIVITY.equals(actionStr)
                || ConstantsUtil.ACTION_OWNER_PHONE_REGISTER_ACTIVITY.equals(actionStr)
                || ConstantsUtil.ACTION_MAIN_CATEGORY_ACTIVITY.equals(actionStr)) {
            openMainContent();
        } else if (ConstantsUtil.ACTION_DOWNLOADED_MUSIC_ACTIVITY.equals(actionStr)
                || ConstantsUtil.ACTION_RECORDED_VIDEO_ACTIVITY.equals(actionStr)
                || ConstantsUtil.ACTION_USE_INTRODUCTION_ACTIVITY.equals(actionStr)
                || ConstantsUtil.ACTION_FOUND_SEARCH_MUSIC_ACTIVITY.equals(actionStr)
                || ConstantsUtil.ACTION_TEACHER_ACTIVITY.equals(actionStr)) {
            // 在以上三个Activity关闭时发出的广播，默认返回“视频录制”界面
            openShootContent();
        } else if (ConstantsUtil.ACTION_MINE_FRAGMENT.equals(actionStr)) {
            openMineContent();
        } else if (ConstantsUtil.ACTION_SHOW_MEDIARECORDER.equals(actionStr)) { // 系统相机摄像
            Toast.makeText(context, "正在启动摄像机...", Toast.LENGTH_SHORT).show();
            DownloadMusicInfo downMusicItem = (DownloadMusicInfo) intent.getSerializableExtra("musicItem");
            CameraPreviewActivity.actionRecord(this, REQUEST_EDIT,
                    InitApplication.SdCardMusicPath.concat(downMusicItem.getMovieName()),
                    downMusicItem.getName() + "_" + downMusicItem.getMusic().getSinger());
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OWNER_PHONE_REGISTER_ACTIVITY && resultCode == RESULT_OK) {
            openMineContent(); // 用户登录后，默认进入用户的视频中心
        }
        
        switch (requestCode) {
        case VIDEO_CAPTURE: 
            startActivity(new Intent(this, RecordedVideoActivity.class));
            break;
        case REQUEST_EDIT:
            if (data != null && data.getData() != null)
                handleRecordVideoResult(data.getData().getPath());
            break;
        case REQUEST_RECORD: 
            // if (data != null && data.getData() != null) {
            // showDialogToast();
            //
            // // 打开等待合成...的对话框
            // showWaitProgressDialog(ShowDanceActivity.this);
            //
            // videoFile = new File(data.getData().getPath());
            // VideoConversionService.Companion.startConversion(this, videoFile,
            // null, null, newLrcFile, musicFile);
            // }
            if (data != null) {
                // Intent mIntent = new Intent();
                data.setClass(this, CameraPreviewActivity.class);
                // data.putExtra("path", data.getData().getPath());
                startActivityForResult(data, REQUEST_EDIT);
            }
            break;
        case REQUEST_VIDEO_REVIEW:
            Intent mIntent = new Intent();
            mIntent.setClass(this, RecordedVideoActivity.class);
            startActivity(mIntent);
            // Toast.makeText(this, "视频预览完毕", Toast.LENGTH_LONG).show();
            break;
        case ConstantsUtil.CHOOSE_LOCATION:
            openMainContent();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!exitFlag) {
            exitFlag = true;
            makeToast(ShowDanceMainActivity.this, R.string.exit_cue);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    exitFlag = false;
                }
            }, DELAY_MILLIS);
        } else {
            this.finish();
        }
    }

    protected void handleRecordVideoResult(final String videopath) {
        final File jointOutFile = new File(videopath);// 转换完成后输出视频目录

        if (jointOutFile.exists() && jointOutFile.length() > 0) {

            // 自定义有标题、有确定按钮与有取消按钮对话框使用方法
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(ShowDanceMainActivity.this)
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setTitle("录制完成");
            mCustomDialog.setMsg("点击确定可预览视频");
            mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowDanceMainActivity.this, PlayVideoActivity.class);
                    intent.putExtra(ConstantsUtil.VIDEO_FILE_PATH_LOCAL, videopath); // 播放本地视频
                    intent.putExtra(ConstantsUtil.VIDEO_FILE_NAME_LOCAL, jointOutFile.getName()); // 本地视频名称
                    startActivity(intent);
                }
            }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();

        } else {

            // 自定义无标题、有确定按钮与无取消按钮对话框使用方法
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(ShowDanceMainActivity.this)
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setMsg("文件为空,请重新录制");
            mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.i(TAG, "启动destroy");
        destroyLocation();
        MainService.removeActivity(this);
    }

}
