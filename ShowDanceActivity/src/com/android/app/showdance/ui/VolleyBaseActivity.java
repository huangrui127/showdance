package com.android.app.showdance.ui;

import java.io.File;

import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public abstract class VolleyBaseActivity extends BaseActivity {
    protected Button button;

    @Override
    protected void onResume() {
        super.onResume();
        button = (Button) findViewById(R.id.login_status); // 由于此控件被其它原有类也进行了调用，名称保持原来的不变
        if (button == null || this instanceof OwnerActivity)
            return;
        button.setVisibility(View.VISIBLE); // 设置拍摄主界面上端tab栏右侧按钮可见
        // final Intent i = new Intent();
        // if(InitApplication.mSpUtil.getUser()==null) {
        // button.setText("登录");
        // button.setBackgroundResource(0);
        // i.setClass(VolleyBaseActivity.this,
        // OwnerPhoneRegisterActivity.class);
        // }else {
        // button.setText("");
        // button.setBackgroundResource(R.drawable.selector_tabhost_owner);
        // i.setClass(VolleyBaseActivity.this, OwnerActivity.class);
        // }
        button.setText("");
        button.setBackgroundResource(R.drawable.selector_tabhost_help);
        final Intent intent = new Intent();
        intent.setClass(VolleyBaseActivity.this, UseIntroductionActivity.class);
        SharedPreferences sp = InitApplication.mSpUtil.getSp();
        String helpUrl = sp.getString("helpurl", null);
        if (helpUrl == null)
            return;
        intent.setData(Uri.parse(helpUrl));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent); // 点击按钮，跳转至help界面
            }
        });
    }

    public abstract class OnResponseListener<T> extends VolleyManager.ResponeListener<T> {

        public OnResponseListener(Class<T> c) {
            super(c);
        }

        @Override
        public void onMyResponse(T response) {
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

            handleResponse(response);
        }

        public void onResponseFail(com.android.app.showdance.model.glmodel.ResponseFail response) {
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

            handleFailResponse(response);
        }

        protected void handleFailResponse(com.android.app.showdance.model.glmodel.ResponseFail response) {

        }

        protected abstract void handleResponse(T response);
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

    protected VolleyManager.ErrorListener mErrorListener = new VolleyManager.ErrorListener() {
        public void onErrorResponse(com.android.volley.VolleyError error) {
            // runOnUiThread(new Runnable() {
            //
            // @Override
            // public void run() {
            // Toast.makeText(getApplicationContext(),"网络异常!",
            // Toast.LENGTH_SHORT).show();
            // }
            // });
            Log.d("guolei", "" + error.toString());
            handleErrorResponse(error);
        };
    };

    protected void handleRecordVideoResult(final String videopath) {
        final File jointOutFile = new File(videopath);// 转换完成后输出视频目录

        if (jointOutFile.exists() && jointOutFile.length() > 0) {

            // mNotificationManager.notify(NotificationID, builder.build());
            // // 震动提示
            // Vibrator vibrator = (Vibrator)
            // getSystemService(Context.VIBRATOR_SERVICE);
            // vibrator.vibrate(1000L);// 参数是震动时间(long类型)
            // mNotificationManager.cancel(NotificationID);

            // getmp4PathFromSD(downMusicItem.getName());

            // 自定义有标题、有确定按钮与有取消按钮对话框使用方法
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(VolleyBaseActivity.this)
                    .builder(R.style.DialogTVAnimWindowAnim);
            mCustomDialog.setTitle("录制完成");
            mCustomDialog.setMsg("点击确定可预览视频");
            mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent mIntent = new Intent();
                    // mIntent.setClass(VolleyBaseActivity.this,
                    // VideoViewPlayingActivity.class);
                    // mIntent.setData(Uri.parse(videopath));
                    // startActivity(mIntent);

                    Intent intent = new Intent(VolleyBaseActivity.this, PlayVideoActivity.class);
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
            CustomAlertDialog mCustomDialog = new CustomAlertDialog(VolleyBaseActivity.this)
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
