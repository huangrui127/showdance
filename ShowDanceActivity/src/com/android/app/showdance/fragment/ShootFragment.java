package com.android.app.showdance.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.adapter.DownloadRecommendAdapter;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.MusicEvent;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.glmodel.MusicDownloadOKRequest;
import com.android.app.showdance.model.glmodel.MusicInfo;
import com.android.app.showdance.model.glmodel.MusicInfo.MusicSearchResponse;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.ui.FoundSearchMusicActivity;
import com.android.app.showdance.ui.PlayVideoActivity;
import com.android.app.showdance.ui.TeacherActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class ShootFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ShootFragment";

    private LinearLayout mSerach_layout; // 搜索框跳转
    @SuppressWarnings("unused")
    private Button btn_teacher, btn_rhythm, btn_edit;
    private PullToRefreshListView recommendMusicListView;
    private DownloadRecommendAdapter showDanceRecommendAdapter;
    private List<DownloadMusicInfo> recommendMusicList = new ArrayList<DownloadMusicInfo>();
    private Dialog mDialog;
    private LayoutInflater inflater;
    private boolean loadingFlag = true;// 合成中进度标志

    public static ShootFragment newInstance() {
        ShootFragment fragment = new ShootFragment();
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        L.i(TAG, "onPause()");
        dismissDialog();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i(TAG, "onResume()");
        EventBus.getDefault().registerSticky(this);
        if (showDanceRecommendAdapter != null) {
            for (DownloadMusicInfo info : recommendMusicList) {
                info.checkstate();
            }
            showDanceRecommendAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoot, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 注册广播
        // registerFragmentBroadcast();
    }

    private void initView(View view) {
        mSerach_layout = (LinearLayout) view.findViewById(R.id.id_search_layout);
        btn_teacher = (Button) view.findViewById(R.id.id_btn_teacher);
        btn_rhythm = (Button) view.findViewById(R.id.id_btn_rhythm);
        btn_edit = (Button) view.findViewById(R.id.id_btn_edit);

        mSerach_layout.setOnClickListener(this);
        btn_teacher.setOnClickListener(this);

        recommendMusicListView = (PullToRefreshListView) view.findViewById(R.id.id_recommendMusicList);
        recommendMusicListView.setMode(Mode.PULL_FROM_START);
        openProgressDialog();
        initPulltoRefreshListView(recommendMusicListView);

        inflater = LayoutInflater.from(InitApplication.getRealContext());
        checkUserInfoAndRefresh(InitApplication.mSpUtil.getUser(), true);
        InitApplication.mSpUtil.setFirstRefeshShowDance(1);
    }

    private void checkUserInfoAndRefresh(User userInfo, boolean oncreate) {
        recommendMusicListView.setRefreshing(false);
    }

    private void initPulltoRefreshListView(PullToRefreshListView ptrlistview) {
        Log.d(TAG, "initPulltoRefreshListView");
        ILoadingLayout startLabels = ptrlistview.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ptrlistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(InitApplication.getRealContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                Log.d(TAG, "onPullDownToRefresh label " + label);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                pulltoRefreshListView(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // if(curPage == totalPage)
                // refreshView.onRefreshComplete();
                // else
                // pulltoRefreshListView(curPage+1);
            }
        });

    }

    private void pulltoRefreshListView(int pageno) {
        pulltoRefreshListView(pageno, Integer.MAX_VALUE);
    }

    private void pulltoRefreshListView(final int pageno, final int pagesize) {
        if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
            Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
            dismissDialog();
            return;
        }
        if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
            pulltoRefreshListViewMethod(pageno, pagesize);
        } else {// 未开启wifi网络
            new CustomAlertDialog(getActivity()).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                    .setMsg("WIFI网络未开启,是否继续使用2G、3G或4G网络!").setPositiveButton("确  认", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pulltoRefreshListViewMethod(pageno, pagesize);
                        }
                    }).setNegativeButton("取  消", new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        }
    }

    private void pulltoRefreshListViewMethod(int pageno, int pagesize) {
        // openProgressDialog();
        MusicInfo.Request request = new MusicInfo.Request();
        request.setStart(1);
        request.setStop(100);
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_MSUIC_SEARCH, mMusicResponseListener,
                mErrorListener);
    }

    private OnResponseListener<MusicInfo.Response> mMusicResponseListener = new OnResponseListener<MusicInfo.Response>(
            MusicInfo.Response.class) {

        @Override
        public void handleResponse(MusicInfo.Response response) {
            recommendMusicListView.onRefreshComplete();
            List<MusicSearchResponse> list = response.getData();
            if (list == null)
                return;
            recommendMusicList.clear();
            for (MusicSearchResponse item : list) {
                DownloadMusicInfo downmusic = new DownloadMusicInfo(item);
                recommendMusicList.add(downmusic);
                Log.d(TAG, "item " + item.getName());
            }
            if (showDanceRecommendAdapter == null) {
                showDanceRecommendAdapter = new DownloadRecommendAdapter(InitApplication.getRealContext(),
                        recommendMusicList, InitApplication.SdCardMusicPath, ConstantsUtil.ACTION_SHOW_MEDIARECORDER,
                        ConstantsUtil.ACTION_DOWNLOAD_STATE);
                recommendMusicListView.setAdapter(showDanceRecommendAdapter);//
            }
            showDanceRecommendAdapter.notifyDataSetChanged();
            dismissDialog();
        }

        @Override
        protected void handleFailResponse(ResponseFail response) {
            dismissDialog();
            Toast.makeText(InitApplication.getRealContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

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

    protected VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            Log.d(TAG, "" + error.toString());
            handleErrorResponse(error);
        };
    };

    protected void handleErrorResponse(com.android.volley.VolleyError error) {
        dismissDialog();
    }

    public void onEventMainThread(MusicEvent event) {
        // 下载进度
        final int musicId = event.musicId;
        if (musicId == -1)
            return;
        DownloadMusicInfo item = null;
        for (DownloadMusicInfo info : recommendMusicList) {
            if (info.getMusic().getId() == musicId) {
                item = info;
                break;
            }
        }
        if (item == null)
            return;
        int position = recommendMusicList.indexOf(item);
        int state = event.state;
        item.setDownloadState(state);

        Log.d(TAG, "musicId " + musicId + " info pos =   " + position + " state " + state);
        switch (state) {
        case ContentValue.DOWNLOAD_STATE_DOWNLOADING:
            long total = event.total;
            // 当前进度
            long current = event.current;
            int percentage = (int) (((int) current * 100) / (int) total);
            item.setProgress(percentage);
            break;
        case ContentValue.DOWNLOAD_STATE_SUCCESS:
            if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
                Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
                showProgressDialogSave(InitApplication.getRealContext(), musicId);
            } else {// 未开启wifi网络
                new CustomAlertDialog(getActivity()).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                        .setMsg("WIFI网络未开启,是否继续使用2G、3G或4G网络!").setPositiveButton("确  认", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showProgressDialogSave(InitApplication.getRealContext(), musicId);
                            }
                        }).setNegativeButton("取  消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
            break;
        default:
            break;
        }
        showDanceRecommendAdapter.notifyDataSetChanged();
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

    /**
     * 功能：保存下载推荐舞曲记录
     */
    public void showProgressDialogSave(Context mContext, int musicId) {
        openProgressDialog(); // 开启加载提示
        MusicDownloadOKRequest request = new MusicDownloadOKRequest();
        request.setId(musicId);
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_DOWNLOAD_MSUIC,
                new VolleyManager.ResponeListener<Object>(null) {
                    @Override
                    public void onMyResponse(Object response) {
                        dismissDialog();
                        Toast.makeText(InitApplication.getRealContext(), "下载成功", Toast.LENGTH_SHORT).show();
                    }
                }, new VolleyManager.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();
                    }
                });

    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 
     * @Description:正在合成视频文件进度对话框
     * @param mContext
     * @param total
     * @param current
     * @return void
     */
    public void showSizeProgressDialog(Context mContext, double total, double current) {
        // if (loadingFlag) {
        // mDialog = new AlertDialog.Builder(mContext).create();
        // loadingFlag = false;
        // mDialog.show();
        // }
        //
        // view = inflater.inflate(R.layout.custom_progressbar_dialog, null);
        // MyProgressBar uploading_proressbar = (MyProgressBar)
        // view.findViewById(R.id.uploading_proressbar);
        // tvLoading = (TextView) view.findViewById(R.id.uploading_tv);
        // tvLoading.setText("正在合成视频...");
        // mDialog.setContentView(view);
        // mDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
        //
        // int fileSize = (int) total;
        // int Progress = (int) current;
        //
        // uploading_proressbar.setMax(fileSize);
        // uploading_proressbar.setProgress(Progress);
        //
        // if (uploading_proressbar.getProgress() ==
        // uploading_proressbar.getMax()) { // 合成完成
        // loadingFlag = true;
        // // 关闭正在合成...的对话框
        // mDialog.cancel();
        //
        // }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.id_search_layout:
            intent.setClass(getActivity(), FoundSearchMusicActivity.class);
            startActivity(intent);
            break;
        case R.id.id_btn_teacher:
            intent.setClass(getActivity(), TeacherActivity.class);
            startActivity(intent);
            break;
        }
    }

    protected void handleRecordVideoResult(final String videopath) {
        final File jointOutFile = new File(videopath);// 转换完成后输出视频目录
        if (jointOutFile.exists() && jointOutFile.length() > 0) {
            // 自定义有标题、有确定按钮与有取消按钮对话框使用方法
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(InitApplication.getRealContext())
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setTitle("录制完成");
            mCustomDialog.setMsg("点击确定可预览视频");
            mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
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
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(InitApplication.getRealContext())
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setMsg("文件为空,请重新录制");
            mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }
    }

}
