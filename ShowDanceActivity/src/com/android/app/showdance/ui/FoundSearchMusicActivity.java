package com.android.app.showdance.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.adapter.DownloadRecommendAdapter;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.MusicEvent;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.glmodel.MusicDownloadOKRequest;
import com.android.app.showdance.model.glmodel.MusicInfo;
import com.android.app.showdance.model.glmodel.MusicInfo.MusicSearchResponse;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.volley.VolleyError;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import gl.live.danceshow.ui.camera.CameraPreviewActivity;

/**
 * 
 * @ClassName: FoundSearchMusicActivity
 * @Description: 搜索歌曲页面
 * @author maminghua
 * @date 2015-6-30 下午4:55:11
 * 
 */
public class FoundSearchMusicActivity extends VolleyBaseActivity {

    private EditText mEdtInput;
    private Button btnSearch;
    private Button mBtnClearSearchText = null;
    private ListView search_music_lv;
    private DownloadRecommendAdapter showDanceRecommendAdapter;
    private List<DownloadMusicInfo> recommendMusicList = new ArrayList<DownloadMusicInfo>();

    private LinearLayout no_login_layout;

    private String keyword = "";

    private DownloadMusicInfo downMusicItem;

    private File videoFile;// 录好后的原始视频路径

    private File musicFile;// 下载好后的音频路径

    private File newLrcFile;// 下载好后的歌词路径

    public static final int REQUEST_RECORD = 0;

    public static final int REQUEST_VIDEO_REVIEW = 1;

    private long id;

    private File renameFileNewPath;

    private Dialog mWaitDialog;// 等待合成的对话框

    private Dialog mDialogToast;// 不能退出程序的提示框

    private boolean loadingFlag = true;// 合成中进度标志

    private boolean waitingFlag = true;// 等待合成进度标志

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdancemusic);
        findViewById();
        initView();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().registerSticky(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void findViewById() {
        tvTitle = (TextView) findViewById(R.id.title_bar_title_tv);
        mEdtInput = (EditText) findViewById(R.id.onlinedictionary_et);
        search_music_lv = (ListView) findViewById(R.id.search_music_lv);
        btnSearch = (Button) findViewById(R.id.SearchBtn);
        mBtnClearSearchText = (Button) findViewById(R.id.btn_clear_search_text);
        btnReturn = (ImageButton) findViewById(R.id.title_bar_return_imgbtn);
        no_login_layout = (LinearLayout) findViewById(R.id.no_login_layout);
    }

    @Override
    protected void initView() {
        tvTitle.setText("搜舞曲");

        inflater = LayoutInflater.from(this);

        UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();

        if (userInfo == null) {
            userInfo = new UserInfo();
        } else {
            id = userInfo.getId(); // 用户登录名
        }

    }

    @Override
    protected String[] initActions() {
        return new String[] { ConstantsUtil.ACTION_SHOW_MEDIARECORDER };
    }

    @Override
    protected void onStart() {
        super.onStart();
        // EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setOnClickListener() {
        btnSearch.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        mBtnClearSearchText.setOnClickListener(this);

        mEdtInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mEdtInput.getText().toString();
                int textLength = text.length();
                if (textLength > 0) {
                    if (text.equalsIgnoreCase("5144")) {
                        if (VolleyManager.SERVER_URL.equalsIgnoreCase(VolleyManager.SERVER_URL_TEST)) {
                            VolleyManager.SERVER_URL = VolleyManager.SERVER_URL_BACKUP;
                            Toast.makeText(InitApplication.getRealContext(), "已切换到【正式服务器】！", Toast.LENGTH_SHORT).show();
                        } else {
                            VolleyManager.SERVER_URL = VolleyManager.SERVER_URL_TEST;
                            Toast.makeText(InitApplication.getRealContext(), "已切换到【测试服务器】！", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                    mBtnClearSearchText.setVisibility(View.VISIBLE);
                } else {
                    mBtnClearSearchText.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.title_bar_return_imgbtn:
            this.finish();
            sendBroadcast(new Intent(ConstantsUtil.ACTION_FOUND_SEARCH_MUSIC_ACTIVITY));
            // overridePendingTransition(R.anim.push_right_out,
            // R.anim.push_right_in);
            break;

        case R.id.SearchBtn:
            if (validateData()) {
                showProgressDialog(this);
                hideSoftInputView();
            }
            break;
        case R.id.btn_clear_search_text:
            mEdtInput.setText("");
            mBtnClearSearchText.setVisibility(View.GONE);
            break;
        default:
            break;
        }
    }

    @Override
    protected boolean validateData() {
        keyword = mEdtInput.getText().toString().trim();
        boolean flag = true;
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入歌曲名或编舞老师名", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {

        // 系统相机摄像
        if (intent.getAction().equals(ConstantsUtil.ACTION_SHOW_MEDIARECORDER)) {
            // Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            // intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            // startActivityForResult(mIntent, VIDEO_CAPTURE);

            Toast.makeText(context, "正在启动摄像机...", Toast.LENGTH_SHORT).show();

            downMusicItem = (DownloadMusicInfo) intent.getSerializableExtra("musicItem");

            // 当前选中mp3的音乐所在SD卡里相关目录里的路径
            // musicFile = new
            // File(InitApplication.SdCardMusicPath.concat(downMusicItem.getMovieName()));
            CameraPreviewActivity.actionRecord(this, REQUEST_RECORD,
                    InitApplication.SdCardMusicPath.concat(downMusicItem.getMovieName()),
                    downMusicItem.getName() + "_" + downMusicItem.getMusic().getSinger());
            return;
        }

    }

    public void onEventMainThread(MusicEvent event) {
        // 下载进度
        int musicId = event.musicId;
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
        int state = event.state;
        item.setDownloadState(state);
        switch (state) {
        case ContentValue.DOWNLOAD_STATE_DOWNLOADING:
            long total = event.total;
            // 当前进度
            long current = event.current;
            int percentage = (int) (((int) current * 100) / (int) total);
            item.setProgress(percentage);
            break;
        case ContentValue.DOWNLOAD_STATE_SUCCESS:
            showProgressDialogSave(this, musicId);
            break;
        default:
            return;
        }
        showDanceRecommendAdapter.notifyDataSetChanged();
    }

    /**
     * 
     * @Description:搜索舞曲
     * @param mContext
     * @param type
     * @return void
     */
    @SuppressWarnings("unchecked")
    public void showProgressDialog(Context mContext) {
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

        // // 接口参数
        // VDanceMusicPageVo danceMusicPageVo = new VDanceMusicPageVo();
        // paramsMap = new HashMap<String, Object>();
        // danceMusicPageVo.setName(keyword);
        // danceMusicPageVo.setPageNo(1);
        // danceMusicPageVo.setPageSize(ConstantsUtil.PageSize);
        // paramsMap.put("danceMusicPageVo", danceMusicPageVo);
        // paramsMap.put("danceMusicType", ConstantsUtil.showDanceSearch);
        //
        // Task mTask = new Task(TaskType.TS_danceMusicPageList, paramsMap);
        // MainService.newTask(mTask);

        MusicInfo.Request request = new MusicInfo.Request(keyword);
        request.setStart(1);
        request.setStop(100);
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_MSUIC_SEARCH, mMusicResponseListener,
                mErrorListener);
    }

    private OnResponseListener<MusicInfo.Response> mMusicResponseListener = new OnResponseListener<MusicInfo.Response>(
            MusicInfo.Response.class) {

        @Override
        protected void handleResponse(MusicInfo.Response response) {
            List<MusicSearchResponse> list = response.getData();
            if (list == null)
                return;
            recommendMusicList.clear();
            for (MusicSearchResponse item : list) {
                DownloadMusicInfo downmusic = new DownloadMusicInfo(item);
                recommendMusicList.add(downmusic);
            }
            if (showDanceRecommendAdapter == null) {
                showDanceRecommendAdapter = new DownloadRecommendAdapter(FoundSearchMusicActivity.this,
                        recommendMusicList, InitApplication.SdCardMusicPath, ConstantsUtil.ACTION_SHOW_MEDIARECORDER,
                        ConstantsUtil.ACTION_DOWNLOAD_STATE);
                search_music_lv.setAdapter(showDanceRecommendAdapter);//
            }
            showDanceRecommendAdapter.notifyDataSetChanged();
        }

        @Override
        protected void handleFailResponse(ResponseFail response) {
            Toast.makeText(getApplicationContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 
     * @Description:保存下载搜索舞曲记录
     * @param mContext
     * @param type
     * @return void
     */
    public void showProgressDialogSave(Context mContext, int musicId) {
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

        // 接口参数
        // DownloadMusic downloadMusic = new DownloadMusic();
        // paramsMap = new HashMap<String, Object>();
        // downloadMusic.setCreateUser(id);
        // downloadMusic.setMusicId(musicId);
        // paramsMap.put("downloadMusic", downloadMusic);
        // paramsMap.put("downloadType", ConstantsUtil.showDanceSearch);
        //
        // Task mTask = new Task(TaskType.TS_SaveDownloadMusic, paramsMap);
        // MainService.newTask(mTask);

        MusicDownloadOKRequest request = new MusicDownloadOKRequest();
        request.setId(musicId);
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_MSUIC_SEARCH,
                new VolleyManager.ResponeListener<Object>(null) {
                    @Override
                    public void onMyResponse(Object response) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                        Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                    }
                }, new VolleyManager.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                    }
                });
    }

    // public void
    // onEventMainThread(VideoConversionService.VideoConversionBeginEvent event)
    // {
    // try {
    // // showDialog(0);
    // } catch (Exception ignored) {
    // }
    // }
    //
    // // 正在合成
    // public void
    // onEventMainThread(VideoConversionService.VideoConversionProgressEvent
    // event) {
    // Bundle bundle = new Bundle();
    // bundle.putString("message", "转换中: " + event.getMessage());
    //
    // // AlertDialog alertDialog = (AlertDialog) dialog;
    // String message = event.getMessage();
    // // alertDialog.setMessage(message);
    //
    // // 根据位置取出dup值
    // int d_pos;
    // d_pos = message.indexOf("dup");
    //
    // if (d_pos > 0) {
    // if (mWaitDialog != null) {
    // if (mWaitDialog.isShowing()) {
    // waitingFlag = false;// 设置为false 是防止视频合成完成后再次打开等待合成的进度条
    // mWaitDialog.dismiss();
    // }
    // }
    // }
    //
    // String dup_temp = new String();
    // dup_temp = message.substring(d_pos, message.length());
    //
    // d_pos = dup_temp.indexOf("=");
    // dup_temp = dup_temp.substring(d_pos + 1, dup_temp.length());
    //
    // // 根据位置取出size值大小
    // int s_pos;
    // s_pos = message.indexOf("size");
    // String size_temp = new String();
    // size_temp = message.substring(s_pos, message.length());
    //
    // s_pos = size_temp.indexOf("kB");
    // size_temp = size_temp.substring(0, s_pos);
    // s_pos = size_temp.indexOf("=");
    // size_temp = size_temp.substring(s_pos + 1, size_temp.length());
    //
    // double videoFileSize = FileUtil.getFileOrFilesSize(videoFile.getPath(),
    // 2);// 原始视频大小
    // double musicFileSize = FileUtil.getFileOrFilesSize(musicFile.getPath(),
    // 2);// 原始音频大小
    // double subFileSize = FileUtil.getFileOrFilesSize(newLrcFile.getPath(),
    // 2);// 原始歌词大小
    //
    // double totalSize = videoFileSize + musicFileSize + subFileSize;
    //
    // if (dup_temp.length() > 0) {
    // showSizeProgressDialog(FoundSearchMusicActivity.this, totalSize,
    // StringUtils.StringTodouble(size_temp));
    // }
    //
    // // showDialog(0, bundle);
    // }
    //
    // // 合成完成
    // public void
    // onEventMainThread(VideoConversionService.VideoConversionFinishEvent
    // event) {
    // // try {
    // // dismissDialog(0);
    // // } catch (Exception ignored) {
    // // }
    //
    // if (mDialog != null) {
    // loadingFlag = true;
    // // 关闭正在合成...的对话框
    // mDialog.cancel();
    // }
    //
    // mDialogToast.cancel();
    //
    // String jointOutFilePath = event.getOutputFile().getPath();
    // String jointFilePath = event.getFilePath().getPath();
    //
    // final File jointOutFile = new File(jointOutFilePath);// 转换完成后输出视频目录
    // final File jointFile = new File(jointFilePath);// 转换之前原始视频目录
    //
    // if (jointOutFile.exists() && jointOutFile.length() > 0) {
    //
    // jointFile.delete();
    //
    // getmp4PathFromSD(downMusicItem.getName());
    //
    // // 自定义有标题、有确定按钮与有取消按钮对话框使用方法
    // CustomAlertDialog mCustomDialog = new
    // CustomAlertDialog(FoundSearchMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
    // mCustomDialog.setTitle("转换完成");
    // mCustomDialog.setMsg("点击确定可预览视频");
    // mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok),
    // new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // VideoReviewActivity.actionReview(FoundSearchMusicActivity.this,
    // REQUEST_VIDEO_REVIEW, Uri.fromFile(renameFileNewPath));// jointOutFile
    // }
    // }).setNegativeButton(getResources().getString(R.string.dialog_cancel),
    // new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    //
    // }
    // }).show();
    //
    // } else {
    //
    // // 自定义无标题、有确定按钮与无取消按钮对话框使用方法
    // CustomAlertDialog mCustomDialog = new
    // CustomAlertDialog(FoundSearchMusicActivity.this).builder(R.style.DialogTVAnimWindowAnim);
    // mCustomDialog.setMsg("文件为空,请重新录制");
    // mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok),
    // new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    //
    // }
    // }).show();
    // }
    // }
    //
    // public void
    // onEventMainThread(VideoConversionService.VideoConversionErrorEvent event)
    // {
    // try {
    // dismissDialog(0);
    // } catch (Exception ignored) {
    // }
    //
    // String exceptionMessage = event.getException().getMessage();
    // if (exceptionMessage != null) {
    // Toast.makeText(FoundSearchMusicActivity.this, "转换失败: " +
    // exceptionMessage, Toast.LENGTH_LONG).show();
    // }
    // }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(this).setMessage("").create();
    }

    // 正在合成(暂未调用)
    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        if (dialog instanceof AlertDialog) {
            AlertDialog alertDialog = (AlertDialog) dialog;
            alertDialog.setMessage(args.getString("message"));
        } else {
            super.onPrepareDialog(id, dialog, args);
        }
    }

    /**
     * 
     * @Description:等待合成进度对话框
     * @param mContext
     * @return void
     */
    public void showWaitProgressDialog(Context mContext) {
        if (waitingFlag) {
            // 打开正在响应...的对话框
            waitingFlag = false;
            mWaitDialog = new AlertDialog.Builder(mContext).create();
            mWaitDialog.show();

            // "正在加载"对话框,提示信息TextView的设置
            view = inflater.inflate(R.layout.loading_progressbar_dialog, null);
            tvLoading = (TextView) view.findViewById(R.id.loading_tv);
            tvLoading.setText("等待合成...");
            mWaitDialog.setContentView(view);
            mWaitDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
        }

    }

    /**
     * 
     * @Description:显示不能退出的对话框
     * @param
     * @return void
     */
    protected void showDialogToast() {

        View view = getLayoutInflater().inflate(R.layout.activity_toask_view, null);
        mDialogToast = new Dialog(this, R.style.transparentFrameWindowStyle);
        mDialogToast.setContentView(view);
        Window window = mDialogToast.getWindow();

        // 屏幕高度和宽度
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();

        // 设置位置在底部
        window.setGravity(Gravity.BOTTOM);
        // 边距
        wl.x = screenWidth;
        // 距离底部高度
        wl.y = (int) (screenHeight * 0.085);
        // 将对话框的大小按屏幕大小的百分比设置
        wl.height = (int) (screenHeight * 0.25); // 高度设置为屏幕的0.3
        wl.width = screenWidth; // 宽度设置为屏幕全屏
        // 设置显示位置
        mDialogToast.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        mDialogToast.setCanceledOnTouchOutside(true);
        mDialogToast.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

        case REQUEST_RECORD: {
            // if (data != null && data.getData() != null) {
            // Intent mIntent = new Intent();
            // mIntent.setClass(this, PreSummeryEditorActivity.class);
            // mIntent.putExtra("path", data.getData().getPath());
            // startActivityForResult(mIntent, REQUEST_VIDEO_REVIEW);
            // }
            if (data != null) {
                // Intent mIntent = new Intent();
                data.setClass(this, CameraPreviewActivity.class);
                // data.putExtra("path", data.getData().getPath());
                startActivityForResult(data, REQUEST_VIDEO_REVIEW);
            }
            break;
        }
        case REQUEST_VIDEO_REVIEW:
            if (data != null && data.getData() != null) {
                handleRecordVideoResult(data.getData().getPath());
            }
            break;
        default: {
            super.onActivityResult(requestCode, resultCode, data);
        }
        }
    }

    @Override
    public void refresh(Object... param) {

    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        sendBroadcast(new Intent(ConstantsUtil.ACTION_FOUND_SEARCH_MUSIC_ACTIVITY));
    }
}
