package gl.live.danceshow.ui.camera;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.android.app.showdance.ui.AnimDrawableItem;
import com.android.app.showdance.ui.PreSummeryEditorActivity;
import com.android.app.showdance.utils.L;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.umeng.socialize.utils.BitmapUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiConfiguration.Status;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import gl.live.danceshow.fragment.AvatorFgMainFragment;
import gl.live.danceshow.fragment.FgMainFragment;
import gl.live.danceshow.fragment.SubtitleControlFragment;
import gl.live.danceshow.media.AnimItem;
import gl.live.danceshow.media.BitmapGridFragment;
import gl.live.danceshow.ui.widget.FixedLyricView;
import gl.live.danceshow.ui.widget.VerticalSeekBar;

/**
 * 录制 act 管理录制流程 This activity uses the camera/camcorder as the A/V source for
 * the {@link android.media.MediaRecorder} API. A
 * {@link android.view.TextureView} is used as the camera preview which limits
 * the code to API 14+. This can be easily replaced with a
 * {@link android.view.SurfaceView} to run on older devices.
 */
public class CameraPreviewActivity extends AbsCameraPreviewActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "CameraPreviewActivity";

    private static final int REQ_REVIEW = 21;
    private static final int REQ_PREFRAME = 22;
    private int MAX_TIME_IN_MS = 10 * 60 * 1000; // 将被音乐长度覆盖，这里给一个默认值 10分钟
    private ImageButton captureButton;
    private ImageView mImageViewTimer;
    private TextView titleText;
    private ProgressBar progressBar;
    private TimerTask task;
    private Timer timer;
    private LyricManager mLyricManager;
    private ImageView mFocusIndicator;
    private ActivityManager mActivityManager;
    private ActivityManager.MemoryInfo mMemoryInfo;

    private UpdateMaskAsyncTask mUpdateMaskAsyncTask; // 设置遮罩效果图的异步任务
    private Dialog mDialog;

    // private int fgDrawableId; // 遮罩图片ID
    /**
     * 录制视频，返回视频本地 Uri
     * 
     * @param activity
     * @param reqRecord
     * @param musicFile
     */
    public static void actionRecord(@NonNull Activity activity, int reqRecord, String musicFile, String name) {
        activity.startActivityForResult(new Intent(activity, CameraPreviewActivity.class)
                .putExtra("musicFile", musicFile).putExtra("musicname", name), reqRecord);
    }

    private void initPreFrame() {
        if (PreSummeryEditorActivity.sBitmapList.size() == 0) {
            return;
        }
        L.d(TAG, "PreSummeryEditorActivity.sBitmapList.size() " + PreSummeryEditorActivity.sBitmapList.size());
        mPreDrawable = new AnimationDrawable();
        mPreDrawable.setOneShot(true);
        for (AnimDrawableItem b : PreSummeryEditorActivity.sBitmapList) {
            mPreDrawable.addFrame(new BitmapDrawable(getResources(), b.getBitmap()), 2500);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mMemoryInfo = new ActivityManager.MemoryInfo();
        // initPreFrame();
        captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(this);
        mFixedLyricView = (FixedLyricView) findViewById(R.id.audio_lrc);
        processIntent();
        mFixedLyricView.setTextColor(Color.MAGENTA);

        ImageButton setting = (ImageButton) findViewById(R.id.buttonSettings);
        setting.setOnClickListener(this);
        setting = (ImageButton) findViewById(R.id.quit);
        setting.setOnClickListener(this);
        setting = (ImageButton) findViewById(R.id.buttonCancelSwitch);
        setting.setOnClickListener(this);

        setting = (ImageButton) findViewById(R.id.setPreFrame);
        setting.setOnClickListener(this);
        setting = (ImageButton) findViewById(R.id.setAvator);
        setting.setOnClickListener(this);
        setting = (ImageButton) findViewById(R.id.subtitle_controller);
        setting.setOnClickListener(this);

        mImageViewTimer = (ImageView) findViewById(R.id.timer);
        mFocusIndicator = (ImageView) findViewById(R.id.focus);
        titleText = (TextView) findViewById(R.id.titleText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        timer = new Timer("rec_count_down");
        VerticalSeekBar seekBar = (VerticalSeekBar) findViewById(R.id.preview_scale);
        seekBar.setOnSeekBarChangeListener(this);

        onCaptureStopped();
    }

    private int count = 0;

    Animation.AnimationListener mAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            L.d(TAG, "onAnimationStart");
            mImageViewTimer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            L.d(TAG, "onAnimationEnd " + count);
            count++;
            if (count > 4) {
                count = 0;
                mImageViewTimer.setImageLevel(count);
                mImageViewTimer.setVisibility(View.GONE);
                setSwitchButtonState(true);
                toggleCapture(false);
                captureButton.setBackgroundResource(R.drawable.camera_recording__stop_btn);
                return;
            }
            mImageViewTimer.setImageLevel(count);
            playTimer();
            // mImageViewTimer.startAnimation(animation);
        }
    };

    private void playTimer() {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(AnimationUtils.loadAnimation(CameraPreviewActivity.this, R.anim.camera_record_set));
        // set.setRepeatCount(6);
        set.setFillAfter(true);
        set.setFillBefore(true);
        // set.setRepeatMode(Animation.RESTART);
        set.setAnimationListener(mAnimListener);
        mImageViewTimer.startAnimation(set);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            L.d(TAG, "onProgressChanged " + progress);
            int color = Color.MAGENTA;
            switch (progress) {
            case 1:
                color = Color.RED;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 7:
                color = Color.MAGENTA;
                break;
            case 4:
                color = Color.WHITE;
                break;
            case 5:
                color = Color.BLUE;
                break;
            case 6:
                color = Color.YELLOW;
                break;
            case 3:
                color = Color.CYAN;
                break;
            case 0:
                color = Color.BLACK;
                break;
            default:
                break;
            }
            switch (seekBar.getId()) {
            case R.id.lrc_color:
                mFixedLyricView.setTextColor(color);
                break;
            case R.id.lrc_textsize:
                mFixedLyricView.setTextSize(18 + progress);
                break;
            default:
                break;
            }

        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        processIntent();
    }

    private void processIntent() {
        Intent intent = getIntent();
        musicFile = intent.getStringExtra("musicFile");
        if (musicFile == null || intent == null) {
            Toast.makeText(this, "音乐文件未找到", Toast.LENGTH_LONG).show();
            finish();
        }
        mFixedLyricView.setText(intent.getStringExtra("musicname"));
        mLyricManager = new LyricManager(this, mFixedLyricView);
        // lryic.setOnClickListener(this);
        mLyricManager.prepare(musicFile);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        try {
            mmr.setDataSource(musicFile);

            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            MAX_TIME_IN_MS = Integer.parseInt(duration);
        } catch (Exception e) {
            Toast.makeText(this, "未知错误！", Toast.LENGTH_LONG).show();
        }
    }

    private void setViewVisible(int show) {
        mHandler.sendMessage(mHandler.obtainMessage(show, findViewById(R.id.preview_controller)));
        mHandler.sendMessage(mHandler.obtainMessage(show, findViewById(R.id.buttonSettings)));
        mHandler.sendMessage(mHandler.obtainMessage(show, findViewById(R.id.buttonSettings_text)));
        mHandler.sendMessage(mHandler.obtainMessage(show, findViewById(R.id.setPreFrame)));
        TextView text = (TextView) findViewById(R.id.tips_stop);
        text.setText(show == HIDE ? "停止" : "拍摄");
        // captureButton.setImageResource(show== HIDE?
        // R.drawable.btn_stop:R.drawable.btn_play);
    }

    @Override
    protected void onCaptureStarted() {
        setViewVisible(HIDE);
        titleText.setText("正在摄像...");
        titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.capture_rec_blink, 0, 0, 0);
        startCountDown();
        mLyricManager.startUpdateLyric();
    }

    public static final long PROGRESS_STEP_IN_MS = 50L;

    private void startCountDown() {
        stopCountDown();
        mFixedLyricView.setText("");
        task = new TimerTask() {
            private boolean bupdatefg = false;
            private int progress = 0;

            @Override
            public boolean cancel() {
                bupdatefg = false;
                progress = 0;
                return super.cancel();
            }

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // debug("tick " + progress);
                        progress += PROGRESS_STEP_IN_MS;
                        if (progress >= MAX_TIME_IN_MS) {
                            debug("progress >= MAX_TIME_IN_MS, toggle capture");
                            stopCountDown();
                            toggleCapture(true);
                        } else {
                            if (!bupdatefg && mPreDrawable != null
                                    && (progress >= mPreDrawable.getNumberOfFrames() * 3000)) {
                                // mPreDrawable.stop();
                                mFg.setBackground(mMainFgDrawable);
                                // mFgSdv.destroyDrawingCache();
                                // mFgSdv.setBackground(null);
                                // mFgSdv.setBackground(mMainFgDrawable);
                                mMediaEngine.setForegroundDrawable(mMainFgDrawable);
                                if (mMainFgDrawable instanceof AnimationDrawable) {
                                    ((AnimationDrawable) mMainFgDrawable).stop();
                                    ((AnimationDrawable) mMainFgDrawable).start();
                                }
                                bupdatefg = true;
                            }

                            // if(!desdetailhiden && progress>=3000) {
                            // desdetailhiden = true;
                            // mDesDetails.setBackground(null);
                            // }

                            if (isRecording() && mMediaEngine != null && !mMediaEngine.isPrepared())
                                progress = 0;
                            progressBar.setProgress(progress);
                            mLyricManager.updatePostion(progress);

                        }
                    }
                });
            }
        };
        debug("start timer");
        timer.scheduleAtFixedRate(task, 0l, PROGRESS_STEP_IN_MS);
    }

    private void debug(String s) {
        L.d("camera", s);
    }

    private void stopCountDown() {
        if (task != null) {
            task.cancel();
        }
        progressBar.setProgress(0);
        mLyricManager.updatePostion(0);
        mLyricManager.stopUpdateLyric();
        mFixedLyricView.setText(getIntent().getStringExtra("musicname"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        focusing = false;
        progressBar.setMax(MAX_TIME_IN_MS);
    }

    @Override
    protected void onPause() {
        dismissDialog();
        mHandler.removeCallbacksAndMessages(null);
        stopCountDown();
        super.onPause();
    }

    @Override
    protected void onCaptureStopped() {
        // if (musicPlayer != null) {
        // musicPlayer.stop();
        // musicPlayer.seekTo(0);
        // }
        mLyricManager.stopUpdateLyric();
        stopCountDown();
        captureButton.setBackgroundResource(R.drawable.camera_recording_btn);
        titleText.setText("准备就绪");
        titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.capture_rec_off, 0, 0, 0);
    }

    /**
     * 视频已经处理完毕
     * 
     * @param mediaFilePath
     */
    @Override
    protected void onCaptureFileReady(String mediaFilePath) {
        if (!TextUtils.isEmpty(mediaFilePath)) {
            final File file = new File(mediaFilePath);
            if (file.exists()) {
                try {
                    MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                    mRetriever.setDataSource(mediaFilePath);
                    Bitmap b = mRetriever.getFrameAtTime();
                    if (b != null) {
                        b = Bitmap.createScaledBitmap(b, 160, 90, true);
                        BitmapUtils.saveBitmap(
                                mediaFilePath.substring(0, mediaFilePath.lastIndexOf(".") + 1).concat("png"), b);
                    }
                } catch (RuntimeException e) {
                    L.w(TAG, "setDataSource  fail " + e.getMessage());
                }

                scanVideoFile(mediaFilePath);
                setResult(RESULT_OK, new Intent().setData(Uri.fromFile(file)));
                finish();
                // 先直接返回
                // VideoReviewActivity.actionReview(CameraPreviewActivity.this,
                // REQ_REVIEW, Uri.fromFile(file));
            }
        }
    }

    private void scanVideoFile(final String filepath) {
        // ContentResolver contentResolver = getContentResolver();
        // ContentValues values = new ContentValues();
        // values.put(MediaStore.Video.DEFAULT_SORT_ORDER, 0);
        // values.put(Media.DISPLAY_NAME,filepath.substring(filepath.lastIndexOf("/")+1));
        // values.put(Media.MIME_TYPE,"video/avc");
        // values.put(Media.DATA, filepath);
        // contentResolver.insert(Media.EXTERNAL_CONTENT_URI , values);
        final MediaScannerConnection connect = new MediaScannerConnection(getApplicationContext(), null) {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                super.onServiceConnected(className, service);
                scanFile(filepath, "video/avc");
            }
        };
        connect.connect();
    }

    private void saveEditInfo(Intent i, Intent o) {
        if (i == null || o == null)
            return;
        String content = null;
        if (!TextUtils.isEmpty(content = i.getStringExtra("compose"))) {
            o.putExtra("compose", content);
        }
        if (!TextUtils.isEmpty(content = i.getStringExtra("video_name"))) {
            o.putExtra("video_name", content);
        }

        if (!TextUtils.isEmpty(content = i.getStringExtra("singer"))) {
            o.putExtra("singer", content);
        }
        if (!TextUtils.isEmpty(content = i.getStringExtra("dancer"))) {
            o.putExtra("dancer", content);
        }
        if (!TextUtils.isEmpty(content = i.getStringExtra("golden_words"))) {
            o.putExtra("golden_words", content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQ_REVIEW:
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else {
                toast("请重试");
            }
            break;
        case REQ_PREFRAME:
            if (data == null)
                break;
            mPreFrameIntent = new Intent();
            saveEditInfo(data, mPreFrameIntent);
            ((TextView) findViewById(R.id.setPreFrame_text)).setText(R.string.modifypreframe);
            initPreFrame();
            break;
        }
    }

    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private Intent mPreFrameIntent;

    @Override
    public void onClick(@NonNull View v) {
        if (count != 0)
            return;
        switch (v.getId()) {
        case R.id.setPreFrame:
            if (mPreFrameIntent == null)
                mPreFrameIntent = new Intent();
            mPreFrameIntent.putExtra("musicFile", musicFile);
            mPreFrameIntent.putExtra("musicname", getIntent().getStringExtra("musicname"));
            mPreFrameIntent.setClass(this, PreSummeryEditorActivity.class);
            startActivityForResult(mPreFrameIntent, REQ_PREFRAME);
            break;
        case R.id.button_capture:
            if (!startok)
                return;
            if (isRecording()) {
                toggleCapture(true);
                return;
            }
            count = 0;
            playTimer();

            break;
        // case R.id.buttonFlip:
        // if (!isRecording())
        // switchCamera();
        // break;
        case R.id.subtitle_controller:
            if (isRecording())
                return;
            DialogFragment subDialogFragment;
            subDialogFragment = new SubtitleControlFragment(mFixedLyricView);
            subDialogFragment.setCancelable(true);
            subDialogFragment.show(getSupportFragmentManager(), "SubtitleControlFragment");
            break;
        case R.id.setAvator:
            mFg.setBackground(null);
            // mFgSdv.destroyDrawingCache();
            // mFgSdv.setBackground(null);

            mMainFgDrawable = null;
            mAvator++;

            mAvator = mAvator % 4;
            if (mAvator == 0)
                mAvator = 1;
            mPreview.setAvator(mAvator);
            mMediaEngine.setAvator(mAvator);
            break;
        case R.id.buttonSettings:
            if (isRecording())
                return;
            FgMainFragment dialog;
            if (mAvator == 1)
                dialog = new FgMainFragment();
            else {
                dialog = new AvatorFgMainFragment();
                Bundle b = new Bundle();
                b.putInt("avator",
                        mAvator == 2 ? BitmapGridFragment.LIST_TYPE_AVATOR2 : BitmapGridFragment.LIST_TYPE_AVATOR3);
                dialog.setArguments(b);
            }
            dialog.show(getSupportFragmentManager(), "FgMainFragment");
            dialog.setCancelable(true);
            break;
        case R.id.audio_lrc:
            // if (isRecording())
            // return;
            // View bar = findViewById(R.id.lrc_color);
            // if(bar.getVisibility() == View.GONE)
            // mHandler.sendMessage(mHandler.obtainMessage(SHOW, bar));
            // audioLrcBarHidenDelayed(bar);
            break;
        case R.id.torchbutton:
            if (mCamera == null)
                break;
            Parameters p = mCamera.getParameters();
            int index = p.getExposureCompensation();
            int max = p.getMaxExposureCompensation();
            if (index == max) {
                p.setExposureCompensation(0);
                ((TextView) findViewById(R.id.torchbutton_text)).setText("加亮");
            } else {
                p.setExposureCompensation(max);
                ((TextView) findViewById(R.id.torchbutton_text)).setText("恢复");
            }
            mCamera.setParameters(p);
            break;
        case R.id.quit:
            finish();
            break;

        case R.id.buttonCancelSwitch:
            setSwitchButtonState(false);
            if (!isRecording()) {
                openCamera(true);
                return;
            }
            stopCountDown();
            if (mPreDrawable != null) {
                mFg.setBackground(mMainFgDrawable);
                // mFgSdv.destroyDrawingCache();
                // mFgSdv.setBackground(null);
                // mFgSdv.setBackground(mMainFgDrawable);
                mPreDrawable.stop();
                if (mMainFgDrawable instanceof AnimationDrawable) {
                    ((AnimationDrawable) mMainFgDrawable).stop();
                    ((AnimationDrawable) mMainFgDrawable).start();
                }
            }
            toggleCapture(false);
            setViewVisible(SHOW);
            break;
        default:
            break;
        }
    }

    private void setSwitchButtonState(boolean state) {
        TextView text = (TextView) findViewById(R.id.button_cancel);
        ImageButton setting = (ImageButton) findViewById(R.id.buttonCancelSwitch);
        if (state) {
            setting.setBackgroundResource(R.drawable.camera_cancel_btn);
            text.setText("取消");
        } else {
            setting.setBackgroundResource(R.drawable.camera_switch_selector);
            text.setText("切换");
        }
    }

    // private void audioLrcBarHidenDelayed(View v) {
    // mHandler.removeMessages(HIDE);
    // mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE, v), 3000);
    // }

    private static final int HIDE = 0;
    private static final int SHOW = 1;
    private static final int HIDE_FOCUS = 3;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            View v = (View) msg.obj;
            switch (msg.what) {
            case SHOW:
                v.setVisibility(View.VISIBLE);
                break;
            case HIDE:
                v.setVisibility(View.INVISIBLE);
                break;
            case HIDE_FOCUS:
                v.setVisibility(View.INVISIBLE);
            default:
                break;
            }
        };
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreDrawable = null;
        PreSummeryEditorActivity.sBitmapList.clear();
        System.gc();
    }

    /**
     * 功能：获取手机当前的内存信息
     */
    private ActivityManager.MemoryInfo getMemoryInfo() {
        mActivityManager.getMemoryInfo(mMemoryInfo);

        L.i(TAG, "系统剩余内存:" + (mMemoryInfo.availMem >> 10) / 1024 + "MB");
        L.i(TAG, "系统是否处于低内存运行：" + mMemoryInfo.lowMemory);
        L.i(TAG, "当系统剩余内存低于" + ((mMemoryInfo.threshold >> 10) / 1024 + "MB") + "时就看成低内存运行");
        return mMemoryInfo;
    }

    /**
     * 功能：判断手机是否处于低内存状态
     */
    private boolean isInLowMemory(ActivityManager.MemoryInfo info) {
        return info.lowMemory;
    }

    @Override
    public void onItemClick(AnimItem animItem) {
        L.d(TAG, "onItemClick");
        if (!isInLowMemory(getMemoryInfo())) { // 手机不处于低内存状态，则可以设置遮罩效果
            // Drawable fg = animItem.getDrawable(this);
            // L.d(TAG, "获取了遮罩Drawable");
            // // fgDrawableId = animItem.getRawId();
            // mFg.setBackground(fg);
            // L.d(TAG, "设置了遮罩背景");
            // // mFgSdv.destroyDrawingCache();
            // // mFgSdv.setBackground(null);
            // // mFgSdv.setBackground(fg);
            // mMainFgDrawable = fg;
            // if (fg instanceof AnimationDrawable)
            // ((AnimationDrawable) fg).start();
            if (mUpdateMaskAsyncTask == null) {
                mUpdateMaskAsyncTask = new UpdateMaskAsyncTask(animItem);
                mUpdateMaskAsyncTask.execute();
            } else {// 如果已经有一个异步任务在执行，则首先取消执行
                if (!mUpdateMaskAsyncTask.isCancelled()
                        && AsyncTask.Status.RUNNING == mUpdateMaskAsyncTask.getStatus()) {
                    mUpdateMaskAsyncTask.cancel(true);
                    mUpdateMaskAsyncTask = null;
                }
                mUpdateMaskAsyncTask = new UpdateMaskAsyncTask(animItem);
                mUpdateMaskAsyncTask.execute();
            }

        } else { // 手机处于低内存状态，则不能够设置遮罩效果
            Toast.makeText(InitApplication.getRealContext(), "您的手机当前可用运行内存过低，无法正常设置遮罩效果！", 2500).show();
        }
    }

    /**
     * 功能：设置遮罩效果图AsyncTask（由于动态遮罩生成较为耗时，使用此异步任务，以免阻塞主线程）
     * @author djd
     */
    private class UpdateMaskAsyncTask extends AsyncTask<Void, Void, Drawable> {

        private AnimItem animItem;

        public UpdateMaskAsyncTask(AnimItem animItem) {
            this.animItem = animItem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dismissDialog();
            openProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (isCancelled()) {
                dismissDialog();
                return;
            }
        }

        @Override
        protected Drawable doInBackground(Void... params) {
            if (isCancelled()) {
                dismissDialog();
                return null;
            }
            // 获取当前选中的遮罩所生成的Drawable（动态遮罩由于使用多张图片合成一个AnimalDrawable，较为耗时）
            return animItem.getDrawable(InitApplication.getRealContext());
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            dismissDialog();
            mFg.setBackground(drawable);
            L.d(TAG, "设置了遮罩背景");
            mMainFgDrawable = drawable;
            if (drawable instanceof AnimationDrawable)
                ((AnimationDrawable) drawable).start();
        }

    }

    private void openProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new AlertDialog.Builder(this).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCancelable(false); // false设置点击其他地方不能取消进度条
        // mDialog.setCanceledOnTouchOutside(false);
    }

    private void dismissDialog() {
        L.i(TAG, "dismissDialog");
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public interface UpdateMusicPlayCallback {
        void updatePostion(long time);
    }

    @Override
    public void onProgressChanged(SeekBar VerticalSeekBar, int progress, boolean fromUser) {
        L.d(TAG, "onProgressChanged " + progress);
        float y = ((float) (100 + VerticalSeekBar.getMax() - progress)) / 100.0f;
        Matrix m = new Matrix();
        L.d(TAG, "mPreview.getCameraView().getPreviewHeight()  " + mPreview.getCameraView().getPreviewHeight() / 2);
        m.postScale(1, y, 0, mPreview.getCameraView().getPreviewHeight() / 2);
        // if(defaultCameraId == 1) {
        // m.postScale(-1 ,1);
        // }
        mPreview.getCameraView().setPreviewTransform(m, y);
    }

    @Override
    public void onStartTrackingTouch(SeekBar VerticalSeekBar) {
        L.d(TAG, "onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar VerticalSeekBar) {
        L.d(TAG, "onStopTrackingTouch");
    }

    @Override
    protected void updateTorchView(boolean bshow) {
        findViewById(R.id.torch).setVisibility(bshow ? View.VISIBLE : View.INVISIBLE);
        ImageButton btn = (ImageButton) findViewById(R.id.torchbutton);
        if (bshow) {
            btn.setOnClickListener(this);
        } else {
            btn.setOnClickListener(null);
        }

    }

    private boolean focusing = false;

    public void onEventMainThread(MotionEvent event) {
        if (focusing)
            return;
        if (count != 0)
            return;
        focusing = true;
        sendAutoFocus();
    }

    private static final int FOCUS_SHOW_DURATION = 2000;

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        L.d(TAG, "onAutoFocus result = " + success);
        focusing = false;
        updateFocus(success ? FocusState.SUCCESS : FocusState.FAIL);
    }

    @Override
    protected void updateFocus(final FocusState state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHandler.removeMessages(HIDE_FOCUS);
                L.d(TAG, "FocusState " + state);
                switch (state) {
                case NORMAL:
                    mFocusIndicator.setImageLevel(5);
                    mFocusIndicator.setVisibility(View.VISIBLE);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE_FOCUS, mFocusIndicator),
                            2 * FOCUS_SHOW_DURATION);
                    break;
                case SUCCESS:
                    mFocusIndicator.setImageLevel(6);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE_FOCUS, mFocusIndicator),
                            FOCUS_SHOW_DURATION);
                    break;
                case FAIL:
                    mFocusIndicator.setImageLevel(7);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE_FOCUS, mFocusIndicator),
                            FOCUS_SHOW_DURATION);
                    break;
                default:
                    break;
                }
            }
        });
    }
}
