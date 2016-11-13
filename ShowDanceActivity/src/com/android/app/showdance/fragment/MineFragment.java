package com.android.app.showdance.fragment;

import java.util.Iterator;
import java.util.List;

import com.android.app.showdance.adapter.UploadedVideoAdapter;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.VolleyManager.ResponeListener;
import com.android.app.showdance.logic.event.SharedEvent;
import com.android.app.showdance.logic.event.UploadDeleteEvent;
import com.android.app.showdance.model.glmodel.AddShareOrPlayCountInfo;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.model.glmodel.VideoUploadInfo;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.DeleteResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;
import com.android.app.showdance.ui.PlayVideoActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMVideo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

/**
 * 功能：“我的”的Fragment
 * 
 * @author djd
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MineFragment";

    // 主键ID
    private TextView stage_name_tv;// 艺名
    private TextView location_name_tv; // 定位的位置
    private XRefreshView mXRefreshView; // 下拉、上拉刷新布局控件
    private SwipeListView mListView;
    private List<VideoUploadResponse> mVideoUploadList;
    private UploadedVideoAdapter mAdapter;
    // private String show_dance_id;
    private Button logout_btn;
    private User mUserInfo;
    private String mLocationName; // 自动定位的位置信息
    private String mUserChooseLocationName; // 用户选择的位置信息

    private Dialog mDialog;
    private PositiveAndNegativeDialogFragment mPositiveAndNegativeDialogFragment;

    // 1)首次进入onCreate()调用接口刷新为1,防止进入onResume()重复调用接口
    // 2)调用接口后设置为2,方便每次触发onResume()方法时调用接口;
    private int refeshState = 1;

    private VideoUploadResponse mVideoUploadResponse; // 当前点击的ListView的item返回的对应元素
    // isShareSuccess为true且isShareSuccessCount为1时，表示分享成功
    private int isShareSuccessCount = 100;
    private boolean isShareSuccess = false;

    private RelativeLayout mEmptyLayout; // 未找到视频列表的提示View
    private TextView mEmptyTV; // 空白提示文字
    // private boolean isAddHeaderView = false;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    /**
     * Activity恢复时执行 当Activity可以得到用户焦点的时候就会调用onResume方法
     */
    @Override
    public void onResume() {
        super.onResume();
        L.i(TAG, "onResume()");
        EventBus.getDefault().register(this);
        if (refeshState == 2) {
            // 获取配置中的手机号
            mUserInfo = InitApplication.mSpUtil.getUser();
            if (mUserInfo == null) {
                mUserInfo = new User();
            }
        }
        mLocationName = InitApplication.mSpUtil.getProvinceName() + " " + InitApplication.mSpUtil.getCityName();
        mUserChooseLocationName = InitApplication.mSpUtil.getUserChooseLocationName();
        // WS_getMyVedio();

        isShareSuccessCount++;
        if (isShareSuccess && isShareSuccessCount == 1) { // 重新切回此界面时，判断是否进行了分享操作
            isShareSuccess = false;
            isShareSuccessCount = 100;
            addShareCount(); // http请求，添加分享次数
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(SharedEvent event) {
        L.i(TAG, "get SharedEvent");
        if (mUserInfo == null) {
            Toast.makeText(InitApplication.getRealContext(), "请先登录后分享视频!", Toast.LENGTH_SHORT).show();
            return;
        }
        L.d(TAG, "userInfo is not null!");
        mVideoUploadResponse = event.event; // 获取当前操作的item对象
        isShareSuccess = true;
        isShareSuccessCount = 0;

        configPlatforms(event);
    }

    public void onEventMainThread(UploadDeleteEvent event) {
        L.i(TAG, "get UploadDeleteEvent");
        if (mUserInfo == null) {
            Toast.makeText(InitApplication.getRealContext(), "请先登录后删除视频!", Toast.LENGTH_SHORT).show();
            return;
        }
        L.d(TAG, "userInfo is not null!");

        if (event.videoUploadResponse.getStatus() != 2) {
            Toast.makeText(InitApplication.getRealContext(), "视频审核中，无法进行删除操作!", Toast.LENGTH_SHORT).show();
        } else {
            // 自定义有标题、有确定按钮与有取消按钮对话框使用方法
            final int eventId = event.videoUploadResponse.getid();

            CustomAlertDialog mCustomDialog = new CustomAlertDialog(getActivity())
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setTitle("删除提示");
            String videoName = "";
            Iterator<VideoUploadResponse> iterator = mVideoUploadList.iterator();
            while (iterator.hasNext()) {
                VideoUploadResponse item = iterator.next();
                if (item.getid() == eventId) {
                    videoName = item.getvideoname();
                }
            }
            mCustomDialog.setMsg("确认删除视频《" + videoName + "》吗？");
            mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteVideoRequest(eventId);
                }
            }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }
    }

    private void deleteVideoRequest(int eventId) {
        VideoUploadInfo.DeleteRequest request = new VideoUploadInfo.DeleteRequest();
        request.setid(eventId);
        VolleyManager.getInstance().postRequest(request,
                BaseRequest.UPLOAD_DELETE_CLIENT_ID + "/" + InitApplication.mSpUtil.getUser().getuser_token()
                        + "/videoDelete",
                new ResponeListener<VideoUploadInfo.DeleteResponse>(VideoUploadInfo.DeleteResponse.class) {
                    @Override
                    public void onMyResponse(DeleteResponse response) {
                        dismissDialog();
                        getMyVedio();
                    }

                }, mErrorListener);
    }

    private VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            L.d(TAG, "" + error.toString());
            handleErrorResponse(error);
        };
    };

    private void handleErrorResponse(com.android.volley.VolleyError error) {
        mXRefreshView.stopRefresh();
        dismissDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        findViewById(view);
        initView();
        MobclickAgent.onProfileSignIn(mUserInfo.getPhone());
        setOnClickListener();
        return view;
    }

    public void getMyVedio() {
        L.i(TAG, "getMyVedio()");
        if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
            Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
            getMyVedioMethod();
        } else {// 未开启wifi网络
            new CustomAlertDialog(getActivity()).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                    .setMsg("WIFI网络未开启,是否继续使用2G、3G或4G网络!").setPositiveButton("确  认", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMyVedioMethod();
                        }
                    }).setNegativeButton("取  消", new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        }
    }

    private void openProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        // mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
        mDialog.setCanceledOnTouchOutside(true);
    }

    private void getMyVedioMethod() {
        // 首先判断用户是否登录过（由于个人中心界面不进行销毁，会存在未登录，界面也进行网络请求的问题）
        User user = InitApplication.mSpUtil.getUser();
        if (user != null) {
            openProgressDialog();
            VideoUploadInfo.ShowRequest request = new VideoUploadInfo.ShowRequest();
            request.setpageNumber(Integer.MAX_VALUE);
            request.setuser_id(user.getId());
            VolleyManager.getInstance().postRequest(request,
                    BaseRequest.UPLOAD_CLIENT_ID + "/" + InitApplication.mSpUtil.getUser().getuser_token()
                            + "/videoList",
                    new OnResponseListener<VideoUploadInfo.ShowResponse>(VideoUploadInfo.ShowResponse.class) {
                        @Override
                        protected void handleResponse(ShowResponse response) {
                            mVideoUploadList = response.getData().getdata();
                            L.d(TAG, "mVideoUploadList size " + mVideoUploadList.size());
                            Iterator<VideoUploadResponse> iterator = mVideoUploadList.iterator();
                            while (iterator.hasNext()) {
                                VideoUploadResponse item = iterator.next();
                                if (item.getvideoname() == null) {
                                    iterator.remove();
                                }
                            }
                            mListView.closeOpenedItems();

                            // if (mVideoUploadList != null &&
                            // mVideoUploadList.size() > 0) {
                            // mEmptyLayout.setVisibility(View.GONE);
                            // if (isAddHeaderView) {
                            // mListView.removeHeaderView(mEmptyLayout);
                            // isAddHeaderView = false;
                            // }
                            // }else {
                            // mEmptyLayout.setVisibility(View.VISIBLE);
                            // if (!isAddHeaderView) {
                            // mListView.addHeaderView(mEmptyLayout);
                            // isAddHeaderView = false;
                            // }
                            // }

                            if (mAdapter == null) {
                                mAdapter = new UploadedVideoAdapter(InitApplication.getRealContext(), mVideoUploadList);
                                mListView.setAdapter(mAdapter);
                            }

                            mAdapter.notifyDataSetChanged(mVideoUploadList);

                            if (mVideoUploadList != null && mVideoUploadList.size() > 0) {
                                mEmptyLayout.setVisibility(View.GONE);
                                mXRefreshView.setVisibility(View.VISIBLE);
                            } else {
                                mXRefreshView.setVisibility(View.GONE);
                                mEmptyLayout.setVisibility(View.VISIBLE);
                            }

                            mXRefreshView.stopRefresh();
                        }
                    }, mErrorListener);
        }
    }

    public abstract class OnResponseListener<T> extends VolleyManager.ResponeListener<T> {
        public OnResponseListener(Class<T> c) {
            super(c);
        }

        @Override
        public void onMyResponse(T response) {
            dismissDialog();
            handleResponse(response);
        }

        public void onResponseFail(com.android.app.showdance.model.glmodel.ResponseFail response) {
            dismissDialog();
            handleFailResponse(response);
        }

        protected void handleFailResponse(com.android.app.showdance.model.glmodel.ResponseFail response) {

        }

        protected abstract void handleResponse(T response);
    };

    /**
     * 查找界面各控件
     */
    @SuppressLint("InflateParams")
    protected void findViewById(View view) {
        initXRefreshView(view);
        initEmptyView(view);

        stage_name_tv = (TextView) view.findViewById(R.id.stage_name_tv);
        location_name_tv = (TextView) view.findViewById(R.id.location_name_tv);
        logout_btn = (Button) view.findViewById(R.id.logout_btn);
        mListView = (SwipeListView) view.findViewById(R.id.uploadedlist);
        // mListView.addHeaderView(mEmptyLayout);
        // mListView.setHeaderDividersEnabled(false);
    }

    private void initXRefreshView(View view) {
        mXRefreshView = (XRefreshView) view.findViewById(R.id.id_refresh_view);
        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setXRefreshViewListener(getXRefreshViewListener());
        mXRefreshView.setPullLoadEnable(false); // 禁用上拉加载更多
        mXRefreshView.setMoveFootWhenDisablePullLoadMore(false); // 禁止页面被向上拉动
    }

    private void initEmptyView(View view) {
        mEmptyLayout = (RelativeLayout) view.findViewById(R.id.id_empty_layout);
        mEmptyTV = (TextView) view.findViewById(R.id.id_prompt_tv);
        mEmptyTV.setText("您还没有上传任何视频哦！");
    }

    protected void initView() {

        // 获取配置中的手机号
        mUserInfo = InitApplication.mSpUtil.getUser();

        if (mUserInfo == null) {
            // finish();
            Toast.makeText(InitApplication.getRealContext(), "请登录!", Toast.LENGTH_SHORT).show();
            return;
        }
        String stage_name = mUserInfo.getPhone();
        // show_dance_id = userInfo.getId();
        if (!TextUtils.isEmpty(stage_name)) {
            stage_name_tv.setText(stage_name);
        } else {
            stage_name_tv.setText("艺名:" + "");
        }
        if (TextUtils.isEmpty(mLocationName)) {
            mLocationName = InitApplication.mSpUtil.getProvinceName() + " " + InitApplication.mSpUtil.getCityName();
        }
        if (TextUtils.isEmpty(mUserChooseLocationName)) {
            mUserChooseLocationName = InitApplication.mSpUtil.getUserChooseLocationName();
        }
        if (!TextUtils.isEmpty(mLocationName)) {
            location_name_tv.setText(mLocationName);
        } else if (!TextUtils.isEmpty(mUserChooseLocationName)) {
            location_name_tv.setText(mUserChooseLocationName);
        } else {
            location_name_tv.setText("没有位置信息!");
        }

        getMyVedio();
    }

    private XRefreshViewListener getXRefreshViewListener() {
        return new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                getMyVedio();
            }

            @Override
            public void onLoadMore(boolean isSilence) { // 当前未使用上拉加载更多
                super.onLoadMore(isSilence);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXRefreshView.stopLoadMore();
                        Toast.makeText(InitApplication.getRealContext(),
                                "上拉加载成功--" + mXRefreshView.getLastRefreshTime(), Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }

        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(InitApplication.getRealContext()).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 
     * @Description:配置需要分享的相关平台
     * @param
     * @return void
     */
    private final static String TEMP_URL = "http://www.xiuwuba.net:82";

    /**
     * 功能：增加被分享视频的分享次数
     */
    private void addShareCount() {
        L.d(TAG, "addShareCount()");
        if (mUserInfo != null) {
            int user_id = mUserInfo.getId();
            String phoneNum = mUserInfo.getPhone();
            if (user_id != 0 && !"".equals(phoneNum)) { // 只有登录后的用户才可以增加播放与分享次数
                AddShareOrPlayCountInfo.Request request = null;
                if (mVideoUploadResponse != null) {
                    request = new AddShareOrPlayCountInfo.Request(mVideoUploadResponse.getid(), 0, user_id, phoneNum);
                    VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_ADD_SHARE_COUNT,
                            addShareOrPlayCountListener, mErrorListener);
                }
            } else {
                L.e(TAG, "已登录用户的id为：" + user_id + "----手机号为：" + phoneNum);
            }
        } else {
            L.e(TAG, "用户未登录，不计算播放、分享次数！");
        }
    }

    private OnResponseListener<AddShareOrPlayCountInfo.Response> addShareOrPlayCountListener = new OnResponseListener<AddShareOrPlayCountInfo.Response>(
            AddShareOrPlayCountInfo.Response.class) {

        @Override
        protected void handleResponse(
                com.android.app.showdance.model.glmodel.AddShareOrPlayCountInfo.Response response) {
            if (response != null) {
                if (response.isFlag()) {
                    L.d(TAG, "视频分享【成功】！");
                    Toast.makeText(InitApplication.getRealContext(), "视频分享【成功】！", Toast.LENGTH_SHORT).show();
                } else {
                    L.e(TAG, "视频分享【失败】！");
                    Toast.makeText(InitApplication.getRealContext(), "视频分享【失败】！", Toast.LENGTH_SHORT).show();
                }
            } else {
                L.e(TAG, "AddShareCountInfo.Response error!");
            }
        }
    };

    private void configPlatforms(SharedEvent event) {
        // UMImage image = new UMImage(OwnerActivity.this,
        // "http://www.umeng.com/images/pic/social/integrated_3.png");
        String url = TEMP_URL/* VolleyManager.SERVER_URL */ + VolleyManager.SHARED + event.event.getid();
        UMVideo video = new UMVideo(url);
        String videoname = null;
        try {
            videoname = event.event.getvideoname().substring(0, event.event.getvideoname().indexOf("_"));
        } catch (StringIndexOutOfBoundsException e) {
        }
        if (TextUtils.isEmpty(videoname)) {
            videoname = event.event.getvideoname();
        }

        video.setDescription(videoname);
        // video.setTargetUrl(url);
        String title = InitApplication.mSpUtil.getSp().getString("activeNote", null);
        if (title == null) {
            title = "分享了一个视频给您";
        }
        video.setTitle(title);
        video.setThumb(event.event.getimg());
        new ShareAction(getActivity())
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withText("舞媚娘App帮我制作的" + "《" + videoname + "》" + ",快帮我点赞吧!").withMedia(video)
                // .withTargetUrl(event.event.getname())
                .withTitle(title).setCallback(umShareListener)
                // .withTargetUrl(url)
                // .withShareBoardDirection(OwnerActivity.this, Gravity.CENTER)
                .open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            L.d(TAG, "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(InitApplication.getRealContext(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InitApplication.getRealContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InitApplication.getRealContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InitApplication.getRealContext(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    private SwipeListViewListener swipeListViewListener = new SwipeListViewListener() {
        @Override
        public void onStartOpen(int position, int action, boolean right) {
            L.d(TAG, "onStartOpen");
        }

        @Override
        public void onStartClose(int position, boolean right) {
            L.d(TAG, "onStartClose");
        }

        @Override
        public void onOpened(int position, boolean toRight) {
            L.d(TAG, "onOpened");
        }

        @Override
        public void onMove(int position, float x) {
            L.d(TAG, "onMove");
        }

        @Override
        public void onListChanged() {
            L.d(TAG, "onListChanged");
        }

        @Override
        public void onLastListItem() {
            L.d(TAG, "onLastListItem");
        }

        @Override
        public void onFirstListItem() {
            L.d(TAG, "onFirstListItem");
        }

        @Override
        public void onDismiss(int[] reverseSortedPositions) {
            L.d(TAG, "onDismiss");
        }

        @Override
        public void onClosed(int position, boolean fromRight) {
            L.d(TAG, "onClosed");
        }

        @Override
        public void onClickFrontView(int position) {
            L.d(TAG, "onClickFrontView");
            VideoUploadResponse videoUploadResponse = mVideoUploadList.get(position);
            if (videoUploadResponse != null) {
                if (videoUploadResponse.getStatus() == 2) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantsUtil.VIDEO_UPLOAD_RESPONSE, videoUploadResponse);
                    intent.putExtras(bundle);
                    L.i(TAG, "视频【" + videoUploadResponse.getvideoname() + "】的播放次数是：" + videoUploadResponse.getcount()
                            + "次");
                    L.i(TAG, "视频【" + videoUploadResponse.getvideoname() + "】的分享次数是："
                            + videoUploadResponse.getshare_count() + "次");
                    intent.setClass(getActivity(), PlayVideoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(InitApplication.getRealContext(), "该视频正在审核中，请耐心等待...", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onClickBackView(int position) {
            L.d(TAG, "onClickBackView");
        }

        @Override
        public void onChoiceStarted() {
            L.d(TAG, "onChoiceStarted");
        }

        @Override
        public void onChoiceEnded() {
            L.d(TAG, "onChoiceEnded");
        }

        @Override
        public void onChoiceChanged(int position, boolean selected) {
            L.d(TAG, "onChoiceChanged");
        }

        @Override
        public int onChangeSwipeMode(int position) {
            L.d(TAG, "onChangeSwipeMode");
            return SwipeListView.SWIPE_MODE_DEFAULT;
        }
    };

    /**
     * 设置事件
     */
    protected void setOnClickListener() {
        mListView.setSwipeListViewListener(swipeListViewListener);
        logout_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.logout_btn:// 退出登录
            InitApplication.mSpUtil.getSp().edit().putBoolean("uploadinfo", false).commit();
            InitApplication.mSpUtil.setUser(null);
            InitApplication.mSpUtil.setPassword("");
            logout_btn.setVisibility(View.GONE);
            MobclickAgent.onProfileSignOff();
            getActivity().sendBroadcast(new Intent(ConstantsUtil.ACTION_SHOW_DANCE_MAIN_ACTIVITY));
            break;
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (mPositiveAndNegativeDialogFragment != null) {
            mPositiveAndNegativeDialogFragment.dismiss();
            mPositiveAndNegativeDialogFragment = null;
        }
    }

    /**
     * 功能：使用Fragment相关的hide和show方法时，Fragment的生命周期
     */
    @Override
    public void onHiddenChanged(boolean hidden) { // true表示该Fragment隐藏了，false表示该Fragment显示了
        super.onHiddenChanged(hidden);
        L.i(TAG, "onHiddenChanged()----hidden is " + hidden);
        if (!hidden) {
            if (InitApplication.mSpUtil.getIsMineFragmentNeedRefresh()) {
                getMyVedio();
                InitApplication.mSpUtil.setMineFragmentNeedRefresh(false);
            }
            mUserInfo = InitApplication.mSpUtil.getUser();
            if (mUserInfo != null && logout_btn.getVisibility() == View.GONE) {
                logout_btn.setVisibility(View.VISIBLE);
            }
        }

    }
}
