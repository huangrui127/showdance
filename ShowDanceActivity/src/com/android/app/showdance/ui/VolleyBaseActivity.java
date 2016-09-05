package com.android.app.showdance.ui;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.ui.baidu.bvideo.VideoViewPlayingActivity;
import com.android.app.showdance.ui.usermanager.OwnerPhoneRegisterActivity;
import com.android.app.showdance.widget.CustomAlertDialog;

public abstract class VolleyBaseActivity extends BaseActivity {
	protected Button button;
	
	@Override
	protected void onResume() {
		super.onResume();
		button = (Button)findViewById(R.id.login_status);
		if(button==null || this instanceof OwnerActivity)
			return;
		button.setVisibility(View.VISIBLE);
		final Intent i = new Intent();
		if(InitApplication.mSpUtil.getUser()==null) {
			button.setText("登录");
			button.setBackgroundResource(0);
			i.setClass(VolleyBaseActivity.this, OwnerPhoneRegisterActivity.class);
		}else {
			button.setText("");
			button.setBackgroundResource(R.drawable.selector_tabhost_owner);
			i.setClass(VolleyBaseActivity.this, OwnerActivity.class);
		}
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(i);
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
						if(mDialog !=null)
							mDialog.dismiss();
						mDialog = null;
					} catch (Exception e) {
						mDialog = null;
					}
				}
			});
			
			handleResponse(response);
		}

		public void onResponseFail(
				com.android.app.showdance.model.glmodel.ResponseFail response) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					try {
						if(mDialog !=null)
							mDialog.dismiss();
						mDialog = null;
					} catch (Exception e) {
						mDialog = null;
					}
				}
			});
			
			handleFailResponse(response);
		}
		protected void handleFailResponse(
				com.android.app.showdance.model.glmodel.ResponseFail response) {

		}
		protected abstract  void handleResponse(T response);
	};

	
	protected void handleErrorResponse(com.android.volley.VolleyError error) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if(mDialog !=null)
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
//			runOnUiThread(new Runnable() {
//				
//				@Override
//				public void run() {
//					Toast.makeText(getApplicationContext(),"网络异常!",
//							Toast.LENGTH_SHORT).show();
//				}
//			});
			Log.d("guolei",""+error.toString());
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

//			getmp4PathFromSD(downMusicItem.getName());

			// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
			CustomAlertDialog mCustomDialog = new CustomAlertDialog(VolleyBaseActivity.this).builder(R.style.DialogTVAnimWindowAnim);
			mCustomDialog.setTitle("录制完成");
			mCustomDialog.setMsg("点击确定可预览视频");
			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent mIntent = new Intent();
					mIntent.setClass(VolleyBaseActivity.this, VideoViewPlayingActivity.class);
					mIntent.setData(Uri.parse(videopath));
					startActivity(mIntent);
				}
			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();

		} else {

			// 自定义无标题、有确定按钮与无取消按钮对话框使用方法
			CustomAlertDialog mCustomDialog = new CustomAlertDialog(VolleyBaseActivity.this).builder(R.style.DialogTVAnimWindowAnim);
			mCustomDialog.setMsg("文件为空,请重新录制");
			mCustomDialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();

		}
	}
}
