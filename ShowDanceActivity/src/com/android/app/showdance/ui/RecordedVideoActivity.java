package com.android.app.showdance.ui;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.android.app.showdance.adapter.RecordedVideoAdapter;
import com.android.app.showdance.logic.UploadViedoService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.UploadEvent;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.model.glmodel.VideoUploadInfo;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;
import com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.utils.StringUtils;
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
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: RecordedVideoActivity
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-12 下午06:08:27
 * 
 */
public class RecordedVideoActivity extends VolleyBaseActivity{
    private static final String TAG = "RecordedVideoActivity";

    private static String[] mActionArray = { // 用于接收对应Activity广播的action
            ConstantsUtil.ACTION_UPLOAD, ConstantsUtil.ACTION_UPLOAD_SIZE, ConstantsUtil.ACTION_UPLOAD_STATE,
            ConstantsUtil.ACTION_DEL_RECORDED, ConstantsUtil.ACTION_CUSTOM_UPLOAD_VIDEO_TITLE_ACTIVITY };

    private static final int REQUEST_CUSTOM_UPLOAD_VIDEO_TITLE = 122;
//    private static final int REQUEST_PLAY_VIDEO_ACTIVIY = 233;

    private SwipeListView recordedVideoList;
    private List<UploadVideoInfo> recordedVideoListInfo;
    private RecordedVideoAdapter mRecordedVideoAdapter;
    private LinearLayout no_login_layout;
    private long mUserId;
    private User mUserInfo;
    // private Long cityId;
    private MediaMetadataRetriever mRetriever;
    private boolean loadingFlag = true;
    private int selectPosition;
    // 用于上传token
    private String token;

    private Dialog mUploadingFileDialog; // 正在上传Dialog
    private boolean isJoinInTheActivitiy = false; // 是否参加活动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordedvideo);
        mRetriever = new MediaMetadataRetriever();
        findViewById();
        setOnClickListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRetriever.release();
    }

    @Override
    protected void findViewById() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);

        recordedVideoList = (SwipeListView) findViewById(R.id.recordedVideoList);

        no_login_layout = (LinearLayout) findViewById(R.id.no_login_layout);
    }

    @Override
    protected void initView() {
        L.i(TAG, "initView()");
        tvTitle.setText("本地视频");
        return_imgbtn.setVisibility(View.VISIBLE);
        inflater = LayoutInflater.from(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mUserInfo = InitApplication.mSpUtil.getUser();
        if (mUserInfo != null) {
            mUserId = mUserInfo.getId(); // 用户已登录
        }
        new ScanVideoTask().execute();
        // 获取上传凭证token
    }

    @Override
    protected String[] initActions() {
        return mActionArray;
    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
        recordedVideoList.setSwipeListViewListener(swipeListViewListener);
    }

    private SwipeListViewListener swipeListViewListener = new SwipeListViewListener() {

        @Override
        public void onStartOpen(int position, int action, boolean right) {
            Log.d(TAG, "onStartOpen");
        }

        @Override
        public void onStartClose(int position, boolean right) {
            Log.d(TAG, "onStartClose");
        }

        @Override
        public void onOpened(int position, boolean toRight) {
            Log.d(TAG, "onOpened");
        }

        @Override
        public void onMove(int position, float x) {
            Log.d(TAG, "onMove");
        }

        @Override
        public void onListChanged() {
            Log.d(TAG, "onListChanged");
        }

        @Override
        public void onLastListItem() {
            Log.d(TAG, "onLastListItem");
        }

        @Override
        public void onFirstListItem() {
            Log.d(TAG, "onFirstListItem");
        }

        @Override
        public void onDismiss(int[] reverseSortedPositions) {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onClosed(int position, boolean fromRight) {
            Log.d(TAG, "onClosed");
        }

        @Override
        public void onClickFrontView(int position) {
            Intent intent = new Intent(RecordedVideoActivity.this, PlayVideoActivity.class);
            intent.putExtra(ConstantsUtil.VIDEO_FILE_PATH_LOCAL, recordedVideoListInfo.get(position).getFilePath()); // 播放本地视频
            intent.putExtra(ConstantsUtil.VIDEO_FILE_NAME_LOCAL, recordedVideoListInfo.get(position).getFileName()); // 本地视频名称
            intent.putExtra(ConstantsUtil.VIDEO_FILE_UPLOAD_STATE,
                    recordedVideoListInfo.get(position).getUploadState()); // 本地视频当前的是否已上传的状态
             startActivity(intent);
        }

        @Override
        public void onClickBackView(int position) {

        }

        @Override
        public void onChoiceStarted() {

        }

        @Override
        public void onChoiceEnded() {

        }

        @Override
        public void onChoiceChanged(int position, boolean selected) {

        }

        @Override
        public int onChangeSwipeMode(int position) {
            return SwipeListView.SWIPE_MODE_DEFAULT;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:// 返回
            this.finish();
            break;
        }

    }

    /**
     * 
     * @Description:获取上传凭证token
     * @param mContext
     * @return void
     */

    public static class BaseResponse {
        protected boolean flag;
        protected String message;
        protected String data;

        public void setFlag(boolean f) {
            flag = f;
        }

        public boolean getFlag() {
            return flag;
        }

        public void setMessage(String ms) {
            message = ms;
        }

        public String getMessage() {
            return message;
        }

        public void setData(String d) {
            data = d;
        }

        public String getData() {
            return data;
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
                            showProgressDialogFile(RecordedVideoActivity.this, filePath);
                        } catch (JsonParseException e1) {
                            e1.printStackTrace();
                            if (mDialog != null) {
                                mDialog.setCancelable(true);
                            }
                        } catch (JsonMappingException e1) {
                            e1.printStackTrace();
                            if (mDialog != null) {
                                mDialog.setCancelable(true);
                            }
                        } catch (IOException e1) {
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
                        makeToast(RecordedVideoActivity.this, R.string.getqiniutoken_fail);
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

    @Override
    protected boolean validateData() {

        return false;
    }

    private void scanVideoFile(final String filepath) {
        final MediaScannerConnection connect = new MediaScannerConnection(getApplicationContext(), null) {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                super.onServiceConnected(className, service);
                scanFile(filepath, "video/avc");
            }
        };
        connect.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i(TAG, "onResume()");
        EventBus.getDefault().register(this);
        // userInfo = InitApplication.mSpUtil.getUser();
        //
        // if (userInfo == null) {
        // return;
        // } else if (cityId == null || cityId == 0) {
        // cityId = InitApplication.mSpUtil.getCityId(); // 用户城市
        // }
        initView();
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        L.w(TAG, "获取到了ACTION：" + intent.getAction() + ";----当前的loadingFlag是" + loadingFlag);
        if (ConstantsUtil.ACTION_UPLOAD.equals(intent.getAction())) {
            if (mUserId != 0) {
                String filePath = intent.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH);
                selectPosition = intent.getIntExtra("position", 0);
                int upload = intent.getIntExtra("uploaded", 0);
                if (upload == 0) {
                    showUploadZoneDialog(this, filePath);
                } else { // upload == 1 准备上传视频时，首先打开一个视频标题的设置Activity
                    L.w(TAG, "获取到了ACTION：" + intent.getAction() + ";----当前的loadingFlag是" + loadingFlag);
                    Intent newIntent = new Intent(RecordedVideoActivity.this, CustomUploadVideoTitleActivity.class);
                    newIntent.putExtra(ConstantsUtil.VIDEO_FILE_PATH, filePath);
                    startActivityForResult(newIntent, REQUEST_CUSTOM_UPLOAD_VIDEO_TITLE);
                }
            } else {
                Toast.makeText(getApplicationContext(), "请先登录后上传视频", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecordedVideoActivity.this, OwnerPhoneRegisterActivity.class)); // 打开登录页面
            }
        } else if (ConstantsUtil.ACTION_DEL_RECORDED.equals(intent.getAction())) {
            final int position = intent.getIntExtra("position", 0);
            final String fileName = recordedVideoListInfo.get(position).getFileName();
            final String filePath = recordedVideoListInfo.get(position).getFilePath();
            final String fileBgPath = recordedVideoListInfo.get(position).getFileBgPath();
            // 自定义有标题、有确定按钮与有取消按钮对话框使用方法
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(RecordedVideoActivity.this)
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setTitle("删除提示");
            mCustomDialog.setMsg("确认删除视频《 " + fileName.substring(0, fileName.indexOf("_")) + "》吗?");
            mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    File delFilePath = new File(filePath);
                    delFilePath.delete();
                    new File(fileBgPath).delete();
                    // 删除 根据条件 fileName 等于选中的就把它删除掉
                    // InitApplication.dbHelper.deleteCriteria(UploadVideoInfo.class,
                    // "fileName", fileName);
                    recordedVideoListInfo.remove(position);
                    scanVideoFile(filePath);
                    recordedVideoList.closeOpenedItems();
                    mRecordedVideoAdapter.notifyDataSetChanged();
                }
            }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d(TAG, "onActivityResult()");
        if (requestCode == REQUEST_CUSTOM_UPLOAD_VIDEO_TITLE && resultCode == RESULT_OK) {
            isJoinInTheActivitiy = data.getBooleanExtra(ConstantsUtil.IS_JOIN_IN_THE_ACTIVITY, false);
            showSizeProgressDialog(RecordedVideoActivity.this,
                    new UploadEvent(0.00, data.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH), null, null));
        }
//        else if (requestCode == REQUEST_PLAY_VIDEO_ACTIVIY && resultCode == RESULT_OK) {
//            initView(); // 在视频播放界面进行了上传操作，需要更新一下当前的视频列表界面
//        }
    }

    public void onEventMainThread(UploadEvent event) {
        L.w(TAG, "onEventMainThread()----当前的event percent是：" + event.percent);
        // 上传状态:当前进度
        showSizeProgressDialog(RecordedVideoActivity.this, event);
        // else

        // 上传状态:成功
        // if (intent.getAction().equals(ConstantsUtil.ACTION_UPLOAD_STATE)) {
        // Log.d(TAG,"ACTION_UPLOAD_STATE");
        // // 关闭正在响应...的对话框
        // if (mDialog != null) {
        // mDialog.cancel();
        // }
        // String filePath =
        // intent.getStringExtra(ConstantsUtil.VIDEO_FILE_PATH);
        // String mediaOldName = filePath.substring(filePath.lastIndexOf("/") +
        // 1, filePath.length());
        // String resultNewName = intent.getStringExtra("resultNewName");
        // showProgressDialogUpolad(RecordedVideoActivity.this, mediaOldName,
        // resultNewName, 0);// requestCode
        // // 0:
        // // 保存视频记录
        // }
    }

    /** 扫描SD卡 */
    private class ScanVideoTask extends AsyncTask<Void, File, List<UploadVideoInfo>> {
//        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(RecordedVideoActivity.this);
//            pd.setMessage("正在扫描已录制视频...");
//            pd.show();
        }

        @Override
        protected List<UploadVideoInfo> doInBackground(Void... params) {

            return getmp4PathFromSD();
        }

        @Override
        protected void onProgressUpdate(final File... values) {
//            pd.setMessage(values[0].getName());
        }

        /** 遍历所有文件夹，查找出视频文件 */
        // 从sd卡获取mp4资源
        public List<UploadVideoInfo> getmp4PathFromSD() {
            // mp4列表
            List<UploadVideoInfo> uploadVideoInfoList = new ArrayList<UploadVideoInfo>();
            // 得到该路径文件夹下所有的文件
            File mfile = new File(InitApplication.SdCardRecordedVideoPath);
            // 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            SimpleDateFormat outsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            File[] files = mfile.listFiles();
            if (files == null)
                return uploadVideoInfoList;

            for (File file : files) {
                if (!FileUtil.checkIsMp4File(file.getPath()))
                    continue;
                // Log.d(TAG,"file "+file.getPath());
                UploadVideoInfo uploadVideoItem = new UploadVideoInfo();
                uploadVideoItem.setFileName(file.getName());
                String path = file.getPath();
                uploadVideoItem.setFilePath(path);
                String[] list = uploadVideoItem.getFileName().split("_");
                if (list.length >= 3) {
                    try {
                        Date date = sdf.parse(list[2]);
                        uploadVideoItem.setCreateTime(outsdf.format(date));
                    } catch (ParseException e) {
                    }
                } else {
                    uploadVideoItem.setCreateTime("");
                }

                uploadVideoItem.setFileBgPath(path.substring(0, path.lastIndexOf(".") + 1) + "png");
                uploadVideoItem.setFileSize(StringUtils.doubleToString(FileUtil.getFileOrFilesSize(file.getPath(), 3)));
                uploadVideoInfoList.add(uploadVideoItem);
                // 保存之前根据视频名查询当前数据是否存在
                UploadVideoInfo uploadVideoInfo = (UploadVideoInfo) InitApplication.dbHelper
                        .searchOne(UploadVideoInfo.class, uploadVideoItem.getFileName());
                if (uploadVideoInfo == null) {
                    // 保存每一条视频信息到数据库
                    InitApplication.dbHelper.save(uploadVideoItem);
                }
            }
            Collections.sort(uploadVideoInfoList);
            // 返回得到的mp4列表
            return uploadVideoInfoList;
        }

        @Override
        protected void onPostExecute(List<UploadVideoInfo> result) {
            super.onPostExecute(result);

            // 从数据库读取保存的所有视频信息List
            recordedVideoListInfo = result;// InitApplication.dbHelper.search(UploadVideoInfo.class);

            if (recordedVideoListInfo != null && recordedVideoListInfo.size() != 0) {
                no_login_layout.setVisibility(View.GONE);
                mRecordedVideoAdapter = new RecordedVideoAdapter(RecordedVideoActivity.this, recordedVideoListInfo,
                        recordedVideoList, 1, mRetriever);
                recordedVideoList.setAdapter(mRecordedVideoAdapter);
                if (mRecordedVideoAdapter != null) {
                    mRecordedVideoAdapter.setVideoType(1);
                    mRecordedVideoAdapter.notifyDataSetChanged();
                }
            } else {
                no_login_layout.setVisibility(View.VISIBLE);
            }
            if (mUserInfo != null) {
                getMyVedio();
            }
            // else{
            // makeToast(InitApplication.getRealContext(), R.string.load_tips);
            // }
//            pd.dismiss();
        }
    }

    public void getMyVedio() {
        L.v(TAG, "getMyVedio()");
        if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
            Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
            getMyVedioMethod();
        } else {// 未开启wifi网络
            new CustomAlertDialog(this).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
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

    public void getMyVedioMethod() {
        mDialog = new AlertDialog.Builder(this).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCanceledOnTouchOutside(true);

        // 接口参数
        // MediaInfoPageVo mediaInfoPageVo = new MediaInfoPageVo();
        // paramsMap = new HashMap<String, Object>();
        //// mediaInfoPageVo.setCreateUser(createUser);
        // mediaInfoPageVo.setPageNo(pageNo);
        // mediaInfoPageVo.setPageSize(ConstantsUtil.PageSize);
        // paramsMap.put("mediaInfoPageVo", mediaInfoPageVo);
        //
        // paramsMap.put("videoType", ConstantsUtil.videoType1);
        // Task mTask = new Task(TaskType.TS_GETMYVIDIO, paramsMap);
        // MainService.newTask(mTask);

        VideoUploadInfo.ShowRequest request = new VideoUploadInfo.ShowRequest();
        request.setpageNumber(Integer.MAX_VALUE);
        request.setuser_id(mUserInfo.getId());
        VolleyManager.getInstance().postRequest(request,
                BaseRequest.UPLOAD_CLIENT_ID + "/" + InitApplication.mSpUtil.getUser().getuser_token() + "/videoList",
                new OnResponseListener<VideoUploadInfo.ShowResponse>(VideoUploadInfo.ShowResponse.class) {
                    @Override
                    protected void handleResponse(ShowResponse response) {
                        dismissDialog();
                        List<VideoUploadResponse> list = response.getData().getdata();
                        Log.d(TAG, "mVideoUploadList size " + list.size());
                        Iterator<VideoUploadResponse> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            VideoUploadResponse item = iterator.next();
                            if (item.getvideoname() == null) {
                                iterator.remove();
                            } else {
                                String videoname = item.getpath();
                                videoname = videoname.substring(videoname.lastIndexOf("/") + 1);
                                for (UploadVideoInfo item1 : recordedVideoListInfo) {
                                    if (item1.getFileName().equalsIgnoreCase(videoname)) {
                                        item1.setUploadState(1);
                                    }
                                }
                            }
                        }
                        if (mRecordedVideoAdapter != null) {
                            mRecordedVideoAdapter.setVideoType(1);
                            mRecordedVideoAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    protected void handleFailResponse(ResponseFail response) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "服务器请求失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, mErrorListener);
    }

    /**
     * 
     * @Description:获取已录制视频接口(未调用)
     * @param mContext
     * @param id
     * @return void
     */
    // public void showProgressDialog(Context mContext, long id) {
    // mDialog = new AlertDialog.Builder(mContext).create();
    // mDialog.show();
    // // 注意此处要放在show之后 否则会报异常
    // mDialog.setContentView(R.layout.loading_progressbar_dialog);
    // mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
    //
    // // 接口参数
    // MediaInfoPageVo mediaInfoPageVo = new MediaInfoPageVo();
    // paramsMap = new HashMap<String, Object>();
    // mediaInfoPageVo.setCreateUser(id);
    // mediaInfoPageVo.setPageNo(1);
    // mediaInfoPageVo.setPageSize(ConstantsUtil.PageSize);
    // paramsMap.put("mediaInfoPageVo", mediaInfoPageVo);
    //
    // Task mTask = new Task(TaskType.TS_MediaInfoPageList, paramsMap);
    // MainService.newTask(mTask);
    // }

    /**
     * 
     * @Description:添加视频描叙对话框
     * @param title
     * @param filePath
     * @return void
     */
    // private void CustomAlertDialog(final String title, final String filePath)
    // {
    // getQiniuToken();
    // new
    // CustomAlertDialogForEditText(RecordedVideoActivity.this).builder(R.style.DialogTVAnimWindowAnim).setTitle(title).setNegativeButton("发
    // 布", new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // showProgressDialogFile(RecordedVideoActivity.this, filePath);
    // }
    // }).show();
    //
    // }

    /**
     * 
     * @Description:HTTP接口上传文件对话框
     * @param mContext
     * @return void
     */
    public void showProgressDialogFile(Context mContext, String filePath) {

        // 接口参数
        // HashMap paramsMap = new HashMap();
        // paramsMap.put(ConstantsUtil.VIDEO_FILE_PATH, filePath);
        //
        // Task mTask = new Task(TaskType.TS_PostMediaInfoList, paramsMap);
        // MainService.newTask(mTask);
        if (token == null)
            return;
        Intent mIntent = new Intent();
        mIntent.setClass(this, UploadViedoService.class);
        mIntent.putExtra("token", token);
        mIntent.putExtra(ConstantsUtil.VIDEO_FILE_PATH, filePath);
        // mIntent.putExtra("remark", descriptive);
        mIntent.putExtra("id", mUserId);
        startService(mIntent);

    }

    /**
     * 
     * @Description:HTTP接口正在上传文件进度对话框
     * @param @param
     *            mContext
     * @param @param
     *            total
     * @param @param
     *            current
     * @return void
     */
    @SuppressLint("InflateParams")
    public void showSizeProgressDialog(Context mContext, UploadEvent event) {
        MyProgressBar uploading_proressbar;
        L.d(TAG, "loadingFlag is " + loadingFlag);
        if (loadingFlag) {
            getQiniuToken(event.filepath);
            dismissDialog();
            // mDialog = new AlertDialog.Builder(mContext).create();
            // loadingFlag = false;
            // mDialog.show();
            // view = inflater.inflate(R.layout.custom_progressbar_dialog,
            // null);
            // uploading_proressbar = (MyProgressBar)
            // view.findViewById(R.id.uploading_proressbar);
            // uploading_proressbar.setMax(100);
            // uploading_proressbar.setProgress(0);
            // mDialog.setContentView(view);
            // mDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
            if (mUploadingFileDialog != null) {
                mUploadingFileDialog.dismiss();
            }
            mUploadingFileDialog = new AlertDialog.Builder(mContext).create();
            loadingFlag = false;
            mUploadingFileDialog.show();
            view = inflater.inflate(R.layout.custom_progressbar_dialog, null);
            uploading_proressbar = (MyProgressBar) view.findViewById(R.id.uploading_proressbar);
            uploading_proressbar.setMax(100);
            uploading_proressbar.setProgress(0);
            mUploadingFileDialog.setContentView(view);
            mUploadingFileDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
        }
        double Progress = event.percent * 100;
        L.i(TAG, "当前的Progress是：" + Progress);
        uploading_proressbar = (MyProgressBar) view.findViewById(R.id.uploading_proressbar);
        if (uploading_proressbar == null)
            return;

        if (uploading_proressbar.getProgress() < Progress)
            uploading_proressbar.setProgress((int) Progress);

        // int p = (int) ((Progress * 100) / fileSize);// 计算进度
        // tvLoading.setText(p + "%");

        if (Progress >= 100.00 && event.newname != null) { // 上传完成
            loadingFlag = true;
            token = null;
            // 关闭正在上传...的对话框
            if (mUploadingFileDialog != null) {
                mUploadingFileDialog.dismiss();
            }
            makeToast(RecordedVideoActivity.this, R.string.upload_success);
            // 打开正在响应...的对话框

            // "正在加载"对话框,提示信息TextView的设置
            // VideoUploadInfo.Request request = new VideoUploadInfo.Request();
            // request.setname(event.newname);
            // request.setuser_id(userInfo.getId());
            // request.setpath(event.filepath);
            // request.setvideo_name(event.oldname);

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
                            InitApplication.mSpUtil.setMineFragmentNeedRefresh(true); // 上传成功后，让“我的”界面可以自动重新获取数据
                            initView();
                        }

                    }, mErrorListener);
        } else if (Progress < 0) {
            L.e(TAG, "当前的Progress为：" + Progress);
            dismissDialog();
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
            L.e(TAG, "onResponseFail()");
            if (response != null) {
                L.e(TAG, response.getMessage());
            }
            dismissDialog();
            // initView();
            handleFailResponse(response);
        }

        protected void handleFailResponse(com.android.app.showdance.model.glmodel.ResponseFail response) {

        }

        protected abstract void handleResponse(T response);
    };

    private VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            L.d(TAG, "onErrorResponse----" + error.toString());
            dismissDialog();
            initView();
            handleErrorResponse(error);
        };
    };

    @SuppressLint("InflateParams")
    public void showUploadZoneDialog(Context mContext, String filepath) {

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        View view = inflater.inflate(R.layout.custom_upload_zone_dialog, null);
        TextView v = (TextView) view.findViewById(R.id.wumeiniang);
        v.setTag(filepath);
        UploadVideoInfo item = recordedVideoListInfo.get(selectPosition);// .getFileName();
        if (item.getUploadState() == 1) {
            v.setText("武媚娘已上传");
        } else
            v.setOnClickListener(mUploadZoneListener);

        v = (TextView) view.findViewById(R.id.tangdou);
        v.setOnClickListener(mUploadZoneListener);
        v = (TextView) view.findViewById(R.id.jiuai);
        v.setOnClickListener(mUploadZoneListener);

        mDialog.setContentView(view);
        // mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
        mDialog.setCanceledOnTouchOutside(true);
    }

    private void updateWuMeiFirst() {
        Toast.makeText(this, "请先上传到武媚娘!", Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener mUploadZoneListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.wumeiniang:
                dismissDialog();
                showSizeProgressDialog(RecordedVideoActivity.this,
                        new UploadEvent(0.00, (String) v.getTag(), null, null));
                break;
            case R.id.tangdou:
                UploadVideoInfo item = recordedVideoListInfo.get(selectPosition);// .getFileName();
                if (item.getUploadState() != 1) {
                    updateWuMeiFirst();
                    break;
                }
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://uu.tangdou.com/"));
                startActivity(mIntent);
                dismissDialog();
                break;
            case R.id.jiuai:
                UploadVideoInfo item1 = recordedVideoListInfo.get(selectPosition);// .getFileName();
                if (item1.getUploadState() != 1) {
                    updateWuMeiFirst();
                    break;
                }
                Intent mIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://i.9igcw.com/my/upvideo.html"));
                startActivity(mIntent1);
                dismissDialog();
                break;
            default:
                break;
            }
        }
    };

    /**
     * 
     * @Description:HTTP接口上传视频完成后调用此接口保存视频记录
     * @param mContext
     * @param mediaOldName
     * @param @param
     *            mediaNewName
     * @return void
     */
    // public void showProgressDialogUpolad(Context mContext, String
    // mediaOldName, String mediaNewName, int requestCode) {
    // mDialog = new AlertDialog.Builder(mContext).create();
    //
    // mDialog.show();
    // // 注意此处要放在show之后 否则会报异常
    // mDialog.setContentView(R.layout.loading_progressbar_dialog);
    // mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
    //
    // VideoUploadInfo.Request request = new VideoUploadInfo.Request();
    // request.setname(mediaNewName);
    // request.setuser_id(userInfo.getId());
    // request.setvideo_name(mediaNewName);
    // VolleyManager.getInstance().postRequest(
    // request,
    // VolleyManager.CLIENT_ID + "/" + userInfo.getuser_token()
    // + "/quniuVideo",
    // new OnResponseListener<VideoUploadInfo.Response>(
    // VideoUploadInfo.Response.class) {
    //
    // @Override
    // protected void handleResponse(
    // VideoUploadInfo.Response response) {
    //
    // }
    //
    // }, mErrorListener);
    //
    // // 接口参数
    //// MediaInfo mediaInfo = new MediaInfo();
    //// paramsMap = new HashMap<String, Object>();
    //// mediaInfo.setCreateUser(id);
    //// mediaInfo.setMediaOldName(mediaOldName);
    //// mediaInfo.setMediaNewName(mediaNewName);
    //// mediaInfo.setRemark(descriptive);
    //// // mediaInfo.setDanceMusicId(1);
    //// paramsMap.put("mediaInfo", mediaInfo);
    //// paramsMap.put("requestCode", requestCode);
    ////
    //// Task mTask = new Task(TaskType.TS_SaveMediaInfo, paramsMap);
    //// MainService.newTask(mTask);
    //
    // }

    @Override
    public void refresh(Object... param) {

    }

    // 点击播放本地视频
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
//        Intent mIntent = new Intent();
//        mIntent.setClass(RecordedVideoActivity.this, VideoViewPlayingActivity.class);
//        mIntent.setData(Uri.parse(recordedVideoListInfo.get(position).getFilePath()));
//        startActivity(mIntent);
//    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
