package com.android.app.showdance.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.adapter.CategoryMainAdapter;
import com.android.app.showdance.adapter.DanceVideoMainAdapter;
import com.android.app.showdance.adapter.RollViewPagerAdapter;
import com.android.app.showdance.custom.GridViewWithHeaderAndFooter;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.DanceVideoBean;
import com.android.app.showdance.model.glmodel.AdPicAndIconInfo;
import com.android.app.showdance.model.glmodel.AdPicAndIconInfo.Ad;
import com.android.app.showdance.model.glmodel.AdPicAndIconInfo.Category;
import com.android.app.showdance.model.glmodel.HotVideoListInfo;
import com.android.app.showdance.model.glmodel.HotVideoListInfo.HotVideo;
import com.android.app.showdance.model.glmodel.HotVideoListInfo.Response;
import com.android.app.showdance.ui.MainCategoryActivity;
import com.android.app.showdance.ui.PlayVideoActivity;
import com.android.app.showdance.ui.UseIntroductionActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 功能：“首页”的Fragment
 * 
 * @author djd
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private static final List<String> mCategoryTypes = new ArrayList<String>(); // 存储当前可用的分类

    private XRefreshView mXRefreshView; // 下拉、上拉刷新布局控件
    private GridViewWithHeaderAndFooter mVideoListGridView; // 视频列表布局
    private List<HotVideo> mHotVideoList; // 通过JSON获取的视频信息集
    private List<DanceVideoBean> mDanceVideoBeanList; // 做显示数据用的数据集
    private DanceVideoMainAdapter mDanceVideoMainAdapter; // “首页”视频列表adapter

    private Dialog mDialog;

    private RelativeLayout mRollPagerViewLayout; // 顶部广告轮播部分
    private RollPagerView mRollPagerView;
    private List<Ad> mAds; // “首页”顶部轮询的广告部分
    private List<Category> mCategories; // “首页”分类图标
    private RollViewPagerAdapter mRollViewPagerAdapter;
    private RelativeLayout mCategoryGridViewLayout; // “首页”分类图标布局
    private GridView mCategoryGridView;
    private CategoryMainAdapter mCategoryMainAdapter; // “首页”分类图标adapter

    private RelativeLayout mEmptyLayout; // 未找到视频列表的提示View
    private TextView mEmptyTV; // 空白提示文字

    // 冠军视频相关元素
    private RelativeLayout mGjLayout;
    private SimpleDraweeView mGjBackground;
    private Button mGjFlowerBtn;
    private TextView mGjTitle;
    private TextView mGjPlayCountTV;
    private TextView mGjShareCountTV;
    private HotVideo mGjVideoBean;

    private HttpUtils mHttpUtils;

    private void initCategoryTypes() {
        mCategoryTypes.add("全国榜");
        mCategoryTypes.add("原创榜");
        mCategoryTypes.add("萌娃榜");
        mCategoryTypes.add("最新视频");
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i(TAG, "onResume");
        if (InitApplication.mSpUtil.getIsMainFragmentNeedRefresh()) { // 重新切回此Fragment时，需要刷新数据（用户在首页手动选择定位城市时）
            updateContent();
            InitApplication.mSpUtil.setMainFragmentNeedRefresh(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        init();
        return view;
    }

    @SuppressLint("InflateParams")
    private void initView(View view) {
        mXRefreshView = (XRefreshView) view.findViewById(R.id.id_custom_view);

        mRollPagerViewLayout = (RelativeLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_rollpagerview, null);
        mRollPagerView = (RollPagerView) mRollPagerViewLayout.findViewById(R.id.id_rollpagerview);
        mRollPagerView.setDrawingCacheEnabled(false);

        mVideoListGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.id_main_gridview_video_list);

        mCategoryGridViewLayout = (RelativeLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_gridview_category, null);
        mCategoryGridView = (GridView) mCategoryGridViewLayout.findViewById(R.id.id_gridview_category);

        initEmptyView(view);
        initChampionView(view);
    }

    @SuppressLint("InflateParams")
    private void initEmptyView(View view) {
        mEmptyLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null);
        mEmptyTV = (TextView) mEmptyLayout.findViewById(R.id.id_prompt_tv);
        mEmptyTV.setText("此地区还没有上传任何视频哦！");
    }

    @SuppressLint("InflateParams")
    private void initChampionView(View view) {
        mGjLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_gj, null);
        mGjBackground = (SimpleDraweeView) mGjLayout.findViewById(R.id.id_gj_pic_sdv);
        mGjFlowerBtn = (Button) mGjLayout.findViewById(R.id.id_gj_top_flower_count_btn);
        mGjTitle = (TextView) mGjLayout.findViewById(R.id.id_gj_title_tv);
        mGjPlayCountTV = (TextView) mGjLayout.findViewById(R.id.id_gj_playcount_tv);
        mGjShareCountTV = (TextView) mGjLayout.findViewById(R.id.id_gj_share_tv);

        mGjLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayVideoActivity(mGjVideoBean);
            }
        });
    }

    private void init() {

        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setXRefreshViewListener(getXRefreshViewListener());
        mXRefreshView.setPullLoadEnable(false); // 暂时禁用上拉加载更多
        mXRefreshView.setMoveFootWhenDisablePullLoadMore(false); // 禁止页面被向上拉动

        initCategoryTypes();
        initCategoryGridView();
        initLoopAdImage();
        initVideoListGridView();
        openProgressDialog();
        updateAllContent();
        updateContent();
    }

    /**
     * 功能：有ProgressBar的更新方法
     */
    private void updateContent() {
        openProgressDialog();
        updateAllContent();
    }

    private void initCategoryGridView() {
        mCategories = new ArrayList<AdPicAndIconInfo.Category>();
        mCategoryMainAdapter = new CategoryMainAdapter(InitApplication.getRealContext(), mCategories);
        mCategoryGridView.setAdapter(mCategoryMainAdapter);
        mCategoryGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = mCategoryMainAdapter.getItem(Integer.parseInt(id + ""));
                openCategoryActivity(category); // 打开分类图标对应的Activity，同时传递过去标题文字
            }
        });
    }

    /**
     * 功能：初始化顶部轮播图片部分
     */
    private void initLoopAdImage() {
        mAds = new ArrayList<Ad>();
        mRollViewPagerAdapter = new RollViewPagerAdapter(getActivity(), mRollPagerView, mAds);
        mRollPagerView.setAdapter(mRollViewPagerAdapter);
        mRollPagerView.setOnItemClickListener(new com.jude.rollviewpager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Ad ad = null;
                if (mAds != null) {
                    ad = mAds.get(position);
                }
                if (ad != null) {
                    openWebPageOnNewActivity(ad.getName(), ad.getUrl());
                }
            }
        });
        goToTop();
    }

    private void initVideoListGridView() {
        mDanceVideoBeanList = new ArrayList<DanceVideoBean>();
        // 添加HeaderView，完善主页面
        mVideoListGridView.addHeaderView(mRollPagerViewLayout);
        mVideoListGridView.addHeaderView(mCategoryGridViewLayout);
        mVideoListGridView.addHeaderView(mEmptyLayout);
        mVideoListGridView.addHeaderView(mGjLayout);
        mDanceVideoMainAdapter = new DanceVideoMainAdapter(InitApplication.getRealContext(), mDanceVideoBeanList,
                mVideoListGridView);
        mVideoListGridView.setAdapter(mDanceVideoMainAdapter);
        mVideoListGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPlayVideoActivity(mHotVideoList.get(Integer.parseInt(id + ""))); // 显示的视频列表从第2个开始
            }
        });
    }

    private void goToTop() {
        // 加载时，最顶部的广告栏获取焦点，保证页面显示在最上面
        mRollPagerView.setFocusable(true);
        mRollPagerView.setFocusableInTouchMode(true);
        mRollPagerView.requestFocus();
    }

    private void updateTopExtraUI(List<Ad> ads) {
        L.i(TAG, "updateTopExtraUI");
        if (ads != null && ads.size() > 0) {
            L.i(TAG, "ads.size() is " + ads.size());
            if (ads.size() == 1) {
                mRollPagerView.setPlayDelay(0);
                mRollPagerView.setHintView(null);
            } else {
                mRollPagerView.setPlayDelay(4000);
                mRollPagerView.setHintView(
                        new IconHintView(InitApplication.getRealContext(), R.drawable.point_pressed, R.drawable.point));
            }
            mRollViewPagerAdapter.setAds(ads);
            mRollViewPagerAdapter.notifyDataSetChanged();
        }
    }

    private void updateCategoryUI(List<Category> categories) {
        L.i(TAG, "updateCategoryUI");
        mCategories = filterCategories(categories);
        if (mCategories != null && mCategories.size() > 0) {
            mCategoryMainAdapter.setCategoryList(mCategories);
            mCategoryGridView.setNumColumns(mCategories.size());
            mCategoryMainAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 功能：过滤获取的Category为当前已设定可用的分类
     */
    private List<Category> filterCategories(List<Category> categories) {
        L.i(TAG, "filterCategories()");
        List<Category> tempCategories = new ArrayList<AdPicAndIconInfo.Category>();
        for (int i = 0; i < categories.size(); i++) {
            if (mCategoryTypes.contains(categories.get(i).getName())) {
                tempCategories.add(categories.get(i));
            }
        }
        return tempCategories;
    }

    /**
     * 功能：首页视图更新
     * 
     * @param hotVideoList
     */
    private void updateVideoListUI(List<HotVideo> hotVideoList) {
        L.i(TAG, "updateVideoListUI");
        if (hotVideoList != null && hotVideoList.size() > 0) {

            mEmptyLayout.setVisibility(View.GONE);
            mEmptyLayout.setPadding(0, -mEmptyLayout.getHeight(), 0, 0); // 由于隐藏HeaderView后，还是会占用位置，需要把显示内容进行上移

            updateGjUI(hotVideoList.get(0)); // 第一个视频时的冠军视频
            mGjLayout.setVisibility(View.VISIBLE);
            hotVideoList.remove(0); // 移除已经添加的冠军视频元素
            if (hotVideoList.size() > 0) { // 只有一个视频的情况下，就只显示冠军视频，隐藏其它视频列表
                updateVideoListGridView(hotVideoList);
            }
        } else { // 收到了空的热门视频列表则置空显示
            L.d(TAG, "视频列表为空！");
            mGjLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setPadding(0, 0, 0, 0); // 由于，隐藏时候上移过位置，需要恢复HeaderView的显示位置
            // 清空数据列表
            updateVideoListGridView(hotVideoList);
        }
        goToTop();
    }

    /**
     * 功能：主页冠军视图的更新
     */
    private void updateGjUI(HotVideo hotVideo) {
        mGjVideoBean = hotVideo;

        mGjBackground.setImageURI(mGjVideoBean.getImg());
        mGjFlowerBtn.setText(mGjVideoBean.getHot() + "");
        mGjTitle.setText(mGjVideoBean.getVideoname());
        mGjPlayCountTV.setText(StringUtils.getPlayCountStr(mGjVideoBean.getCount()));
        mGjShareCountTV.setText(StringUtils.getShareCountStr(mGjVideoBean.getShare_count()));
    }

    private void updateVideoListGridView(List<HotVideo> hotVideoList) {
        mDanceVideoMainAdapter.setDanceVideoBeanList(getDanceVideoBeanListFromHotVideoList(hotVideoList));
        mDanceVideoMainAdapter.notifyDataSetChanged();
        mVideoListGridView.setVisibility(View.VISIBLE);
    }

    private List<DanceVideoBean> getDanceVideoBeanListFromHotVideoList(List<HotVideo> hotVideoList) {
        if (mDanceVideoBeanList != null) {
            mDanceVideoBeanList.clear();
        } else {
            mDanceVideoBeanList = new ArrayList<DanceVideoBean>();
        }
        if (hotVideoList != null && hotVideoList.size() > 0) {
            for (HotVideo hotVideo : hotVideoList) {
                mDanceVideoBeanList.add(new DanceVideoBean(hotVideo.getImg(), hotVideo.getHot(), hotVideo.getHot(),
                        hotVideo.getVideoname(), hotVideo.getCreated_at(), hotVideo.getCount(),
                        hotVideo.getShare_count(), hotVideo.getName()));
            }
        }

        return mDanceVideoBeanList;
    }

    private XRefreshViewListener getXRefreshViewListener() {
        return new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                updateAllContent();
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

    /**
     * 功能：更新整个“首页”视图
     */
    private void updateAllContent() {
        L.d(TAG, "updateAllContent");
        if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
            mXRefreshView.stopRefresh();
            Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
            dismissDialog();
            return;
        }
        if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
            updateContentMethod();
        } else {// 未开启wifi网络
            new CustomAlertDialog(getActivity()).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                    .setMsg("WIFI网络未开启,是否继续使用2G、3G或4G网络!").setPositiveButton("确  认", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateContentMethod();
                        }
                    }).setNegativeButton("取  消", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mXRefreshView.stopRefresh();
                        }
                    }).show();
        }
    }

    private void updateContentMethod() {
        getVideoList();
        getTopExtra();
        goToTop();
    }

    /**
     * 功能：清除顶部轮播图片缓存（必须清除，否则服务器端更改图片后，无法显示新的图片）
     */
    private void clearViewPagerCache() {
        if (mRollPagerView != null) {
            L.d(TAG, "clearViewPagerCache()");
            mRollPagerView.destroyDrawingCache();
        }
    }

    /**
     * 功能：获取“首页”顶部的广告轮播图片及分类导航信息
     */
    private void getTopExtra() {
        L.d(TAG, "getTopExtra()");
        mHttpUtils = new HttpUtils();
        mHttpUtils.send(HttpMethod.GET, VolleyManager.SERVER_URL_EXTRA + VolleyManager.METHOD_MAIN_EXTRA,
                AdPicAndIconInfoRequestCallBack);
    }

    private RequestCallBack<String> AdPicAndIconInfoRequestCallBack = new RequestCallBack<String>() {

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            L.d(TAG, current + "/" + total);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String jsonStr = responseInfo.result;
            L.d(TAG, "收到的Http请求数据：" + jsonStr);
            // ObjectMapper mapper = new ObjectMapper();
            Gson gson = new Gson(); // 当前因为服务器返回数据格式问题，不能够使用Jackson进行解析
            try {
                // AdPicAndIconInfo.Response response =
                // mapper.readValue(jsonStr,
                // AdPicAndIconInfo.Response.class);
                AdPicAndIconInfo.Response response = gson.fromJson(jsonStr, AdPicAndIconInfo.Response.class);
                if (response != null) {
                    if (mAds != null && mAds.size() > 0) {
                        mAds.clear();
                    }
                    if (mCategories != null && mCategories.size() > 0) {
                        mCategories.clear();
                    }
                    mAds = response.getTopPic();
                    mCategories = response.getIcon();
                    InitApplication.mSpUtil.setTheme(response.getTheme()); // 存入主题名称
                    InitApplication.mSpUtil.setThemeType(response.getThemeType()); // 存入主题类型
                    L.d(TAG, "topPic size is " + mAds.size() + "; icon size is " + mCategories.size());
                    clearViewPagerCache();
                    updateTopExtraUI(mAds);
                    // 测试增加原创榜和萌娃榜
                    // mCategories.add(new Category("原创榜", 5));
                    // mCategories.add(new Category("萌娃榜", 6));

                    updateCategoryUI(mCategories);
                }
            } catch (Exception e) {
                e.printStackTrace();
                L.e(TAG, "error is " + e.getMessage());
            }
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            L.d(TAG, "HttpException is " + error.getMessage() + "; msg is " + msg);
        }
    };

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

    private void getVideoList() {
        L.d(TAG, "getVideoList()");
        HotVideoListInfo.Request request = new HotVideoListInfo.Request(getRequestType(), getProvinceAndCityName(), "",
                49, 0); // 传入0，默认查询国内的
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_HOT_VIDEO,
                new OnResponseListener<HotVideoListInfo.Response>(HotVideoListInfo.Response.class) {
                    @Override
                    protected void handleResponse(Response response) {
                        if (response != null) {
                            mHotVideoList = response.getData();
                            L.d(TAG, "hotVideoList size is " + mHotVideoList.size());
                            updateVideoListUI(mHotVideoList);
                        }
                        mXRefreshView.stopRefresh();
                        dismissDialog();
                    }
                }, mErrorListener);
    }

    /**
     * 功能：获取请求国家或者省、市码
     * 
     * @return
     */
    private int getRequestType() {
        L.i(TAG, "getRequestType()");
        String province = InitApplication.mSpUtil.getProvinceName();
        String city = InitApplication.mSpUtil.getCityName();
        String userChooseLocationName = InitApplication.mSpUtil.getUserChooseLocationName();
        if (!"".equals(userChooseLocationName) && !userChooseLocationName.equals("定位")) { // 优先以用户选择的显示位置显示列表
            if (userChooseLocationName.endsWith("省") || userChooseLocationName.endsWith("自治区")
                    || userChooseLocationName.endsWith("行政区")) {
                return 1;
            } else {
                return 2;
            }
        } else if (!"".equals(province)) { // 其次以省为单位显示列表
            return 1;
        } else if (!"".equals(city)) { // 最后以市为单位选择列表
            return 2;
        }
        return 0;
    }

    /**
     * 功能：更新显示定位出来的省市名称
     */
    private String getProvinceAndCityName() {
        L.i(TAG, "getProvinceAndCityName()");
        String province = InitApplication.mSpUtil.getProvinceName();
        String city = InitApplication.mSpUtil.getCityName();
        String userChooseLocationName = InitApplication.mSpUtil.getUserChooseLocationName();
        if (!"".equals(userChooseLocationName) && !userChooseLocationName.equals("定位")) { // 优先以用户选择的显示位置显示列表
            return userChooseLocationName;
        } else if (!"".equals(province)) { // 其次以省为单位显示列表
            return province;
        } else if (!"".equals(city)) { // 最后以市为单位选择列表
            return city;
        }
        return "";
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
            L.e(TAG, "onResponseFail()");
            if (response != null) {
                L.e(TAG, response.getMessage());
            }
            dismissDialog();
            mXRefreshView.stopRefresh();
            updateVideoListUI(null);
            handleFailResponse(response);
        }

        protected void handleFailResponse(com.android.app.showdance.model.glmodel.ResponseFail response) {

        }

        protected abstract void handleResponse(T response);
    };

    private VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            L.d(TAG, "onErrorResponse----" + error.toString());
            handleErrorResponse(error);
        };
    };

    private void handleErrorResponse(com.android.volley.VolleyError error) {
        mXRefreshView.stopRefresh();
        dismissDialog();
    }

    private void openCategoryActivity(Category category) {
        Intent intent = new Intent(getActivity(), MainCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantsUtil.CATEGORY, category);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void dismissDialog() {
        L.i(TAG, "dismissDialog");
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 功能：视频播放页面（内置播放器，非网页版）
     * 
     * @param danceVideoBean
     */
    public void openPlayVideoActivity(HotVideo hotVideo) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("hotVideo", hotVideo);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), PlayVideoActivity.class);
        startActivity(intent);
    }

    public void openWebPageOnNewActivity(String pageTitle, String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        Log.d(TAG, "打开的网页是----【" + pageTitle + "】");
        intent.putExtra("title", pageTitle);
        intent.setClass(getActivity(), UseIntroductionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        L.i(TAG, "onPause");
        dismissDialog();
    }
}
