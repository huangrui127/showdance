/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gl.live.danceshow.ui.camera;

import gl.live.danceshow.media.CameraFgAdapter.OnFgItemClickListener;
import gl.live.danceshow.media.MediaEngine;
import gl.live.danceshow.ui.widget.FixedLyricView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.umeng.analytics.MobclickAgent;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

// ----------------------------------------------------------------------

/**
 *
 */
@SuppressLint("NewApi")
public abstract class AbsCameraPreviewActivity extends FragmentActivity implements OnFgItemClickListener, AutoFocusCallback {
	protected CenteredPreview mPreview;
    protected MediaEngine mMediaEngine;
    protected ImageView mFg;
    protected int mAvator = 1;
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked = -1;

    private String mediaFilePath; // 录制的文件
    protected String musicFile;

    // The first rear facing camera
    int defaultCameraId;
    protected AnimationDrawable mPreDrawable;
    protected boolean preparingRecorder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        HandlerThread camerastartupthread = new HandlerThread("cameradeviceopt");
   	 camerastartupthread.start();
   	camerastartuphandler = new Handler(camerastartupthread.getLooper());
        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new CenteredPreview(this);
//        setContentView(mPreview);
        mPreview.setAvator(mAvator);
        setContentView(R.layout.camera_activity);
        mMediaEngine = new MediaEngine(this);
        FrameLayout.LayoutParams p=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP);
        FrameLayout container = (FrameLayout) findViewById(R.id.surfaceViewContainer);
        mPreview.setLayoutParams(p);
        container.addView(mPreview);
        
        mFg = new ImageView(this);
        mFg.setScaleType(ScaleType.FIT_XY);
        p=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP);
        mFg.setLayoutParams(p);
        container.addView(mFg);
        
//        mDesDetails = new ImageView(this);
//        mDesDetails.setScaleType(ScaleType.FIT_CENTER);
//        p=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP);
//        mDesDetails.setLayoutParams(p);
//        container.addView(mDesDetails);
        
        container.requestLayout();

        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
    }
//
//    private boolean checkPermission() {
//    	PackageManager pm = getPackageManager();
//		int permission = pm.checkPermission("android.permission.CAMERA", getPackageName());
//		if(permission == PackageManager.PERMISSION_DENIED) {
//			Log.d("guolei","  没有权限！");
//			return false;
//		}
//		Log.d("guolei","  有权限！");
//		return true;
//	}

	protected boolean isSupportFlip() {
        return numberOfCameras != 1;
    }

	protected Handler camerastartuphandler;
	protected boolean startok = false;
	private boolean bResume = false;
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        bResume = true;
        mMediaEngine.initSurfaceDrawnThread();
        mMediaEngine.setAvator(mAvator);
       
        openCamera(false);
//        mCameraView = mPreview.getCameraView();
        // Open the default i.e. the first rear facing camera.
      
		
//        if (cameraCurrentlyLocked == -1) {
//            cameraCurrentlyLocked = defaultCameraId;
//        }
//        try {
//            switchCameraTo(cameraCurrentlyLocked);
//        } catch (Exception e) {
//            handleCameraException(e);
//        }
    }

    protected void openCamera(final boolean bswitch) {
    	 camerastartuphandler.post(new Runnable() {
 			@Override
 			public void run() {
 				  try {
// 			        	mCamera = Camera.open();
 					 startok = false;
					if (bswitch) {
						defaultCameraId++;
						defaultCameraId = defaultCameraId % numberOfCameras;
					}
 			        	switchCameraTo(defaultCameraId);
 			        	runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(defaultCameraId == 1)
		 			        		mPreview.getCameraView().setPreviewScaleX(-1);
								else {
									mPreview.getCameraView().setPreviewScaleX(1);
								}
							}
						});
 			        	startok = true;
 					} catch (RuntimeException e) {
 						runOnUiThread(new Runnable() {
 							@Override
 							public void run() {
 								CustomAlertDialog mCustomDialog = new CustomAlertDialog(AbsCameraPreviewActivity.this).builder(R.style.DialogTVAnimWindowAnim);
 								mCustomDialog.setTitle("检查相机");
 								mCustomDialog.setMsg("可能你的相机无法使用，或者没有使用相机的权限！");
 								mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
 									@Override
 									public void onClick(View v) {
 										Intent intent = new Intent();
 										intent.setAction(Settings.ACTION_SETTINGS);
 										startActivityForResult( intent , 0);
 									}
 								}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
 									@Override
 									public void onClick(View v) {
 										finish();
 									}
 								}).show();								
 							}
 						});
 					}
 			}
 		});
    }
    
    private void handleCameraException(Exception e) {
        new AlertDialog.Builder(this)
                .setMessage(e.getLocalizedMessage())
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).setNegativeButton("cancel", null).show();
    }

    public static class CameraEvent {
    	private SurfaceTexture mTexture;
    	
    	public CameraEvent(SurfaceTexture text) {
    		mTexture = text;
    	}
    }
    
    public void onEventMainThread(Runnable event) {
    	if(camerastartuphandler == null) return;
    	camerastartuphandler.post(event);
    }
    private  void dumpParameters (Parameters parameters) {
        String flattened = parameters. flatten();
        StringTokenizer tokenizer = new StringTokenizer(flattened, ";" );
        Log.d(TAG , "Dump all camera parameters:" );
        while (tokenizer.hasMoreElements()) {
            Log. d(TAG, tokenizer.nextToken());
        }
    }
    public void onEventMainThread(CameraEvent event) {
    	final CameraEvent e = event;
    	if(camerastartuphandler == null) return;
    	camerastartuphandler.post(new Runnable() {
			
			@Override
			public void run() {
				if(mCamera == null) {
					Log.d(TAG,"CameraEvent mCamera == null");
					return;
				}
				try {
					Log.d(TAG,"CameraEvent stopPreview setPreviewTexture");
					mCamera.stopPreview();
					mCamera.setPreviewTexture(e.mTexture);
					mCamera.startPreview();
					mMediaEngine.setCamera(mCamera);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
    }
    
    @Override
    protected void onPause() {
    	Log.d(TAG,"onPause");
    		bResume = false;
    		
    		camerastartuphandler.post(new Runnable() {
				@Override
				public void run() {
					releaseMediaRecorder();
					mMediaEngine.releaseVideoSurfaceThread();
					mPreview.setCamera(null, 0);
					if(mCamera!=null) {
			        	  mCamera.stopPreview();
			  				mCamera.release();
			  				mCamera = null;
			        	  }
				}
			});
    		

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.

        super.onPause();
        MobclickAgent.onPause(this);
    }


//    boolean switchCamera() {
////        check for availability of multiple cameras
//        if (!isSupportFlip()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("不能切换").setNeutralButton("关闭", null);
//            AlertDialog alert = builder.create();
//            alert.show();
//            return true;
//        }
//
//        int cameraId = (cameraCurrentlyLocked + 1) % numberOfCameras;
//        switchCameraTo(cameraId);
//        // Start the preview
//        mCamera.startPreview();
//        return true;
//    }

    private void switchCameraTo(int cameraId) {
        // OK, we have multiple cameras.
        // Release this camera -> cameraCurrentlyLocked
        if (mCamera != null) {
            mCamera.stopPreview();
            mPreview.setCamera(null, defaultCameraId);
            mMediaEngine.setCamera(null);
            mCamera.release();
            mCamera = null;
        }

        // Acquire the next camera and request Preview to reconfigure
        // parameters.
        if(!bResume)
        	return;
        mCamera = Camera.open(cameraId);
//        dumpParameters(mCamera.getParameters());
        enableBeautyModeIfNeed();
        mMediaEngine.setCamera(mCamera);
        cameraCurrentlyLocked = cameraId;
        mPreview.switchCamera(mCamera, cameraCurrentlyLocked);
        mCamera.startPreview();
        checkTorchMode();
    }

	private void enableBeautyModeIfNeed() {
		Parameters p = mCamera.getParameters();
		String bsupported = p.get("hw_beauty_mode_support");
		if (!"true".equalsIgnoreCase(bsupported)) {
			return;
		}
		int min = p.getInt("hw-min-beauty-level");
		int max = p.getInt("hw-max-beauty-level");
		p.set("hw-beauty-level", max/2);
		mCamera.setParameters(p);
	}
	private void checkTorchMode() {
    	if(mCamera ==null) return;
    	Parameters parameters = mCamera.getParameters();
    	int max = parameters.getMaxExposureCompensation();
    	int min = parameters.getMinExposureCompensation();
//    	 List<String> modes = parameters.getSupportedFlashModes();
    	 final boolean bshowtorch = !(max == min);
    		 runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Log.d(TAG,"updateTorchView1");
					updateTorchView(bshowtorch);
					Log.d(TAG,"updateTorchView2");
				}
			});
    }
    
    protected abstract void updateTorchView(boolean bshow);
    protected FixedLyricView mFixedLyricView;
    private boolean isRecording = false;

    protected static final String TAG = "CameraPreview";

    public synchronized boolean isRecording() {
        return isRecording;
    }
    protected Drawable mMainFgDrawable;
    private boolean prepareVideoRecorder() {
        try {
        	synchronized (this) {
				
        	
        	mMediaEngine.initThreads();
//        	mMediaEngine.setAvator(mAvator);
        	 mMediaEngine.setFixedLyricView(mFixedLyricView);
        	mMediaEngine.setPreviewTexture(mPreview.getCameraView());
//        	mMediaEngine.setPreviewSize(mPreview.getPreviewSize());
//            mPreview.setOnFrameUpdateListener(mMediaEngine);
            mMediaEngine.setCamera(mCamera);
            if(mPreDrawable !=null) {
            	mMediaEngine.setForegroundDrawable(mPreDrawable);
            runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mFg.setBackground(mPreDrawable);
					mPreDrawable.start();
				}
			});
            } else {
            	mMediaEngine.setForegroundDrawable(mMainFgDrawable);
            }
            // Step 2: Set sources
            if(!mMediaEngine.initAudio(musicFile)) {
            	return false;
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            mediaFilePath = InitApplication.SdCardRecordedVideoPath +File.separator+ getIntent().getStringExtra("musicname").concat("_").concat(timeStamp).concat(".mp4");
            Log.d(TAG,"mediaFilePath "+mediaFilePath);
            File f = new File(mediaFilePath);
            if(f.exists()) {
            	f.delete();
            }
            mMediaEngine.initVideo(mediaFilePath, "video/avc");

            	mMediaEngine.setPreviewDisplay(null);

            mMediaEngine.prepare();
        	}
        } catch (IOException e) {
            Log.e(TAG, "IOException preparing MediaRecorder", e);
            MobclickAgent.reportError(this, "io fail,maybe mp3 file is not exit."+musicFile);
            return false;
        } catch (IllegalStateException e) {
            MobclickAgent.reportError(this, "IllegalStateException "+musicFile);
            return false;
		}
        return true;
    }


    protected void releaseMediaRecorder() {
    	synchronized (mMediaEngine) {
        if (mMediaEngine != null) {
            // clear recorder configuration
            // release the recorder object
        	mMediaEngine.setCamera(null);
        	mMediaEngine.stop();
        	try {
        		mMediaEngine.release();
			} catch (IllegalStateException e) {
				Log.e(TAG,"mMediaEngine release failed!");
			}
        }
    	}
    }

    /**
     * The capture button controls all user interaction. When recording, the button click
     * stops recording, releases {@link MediaRecorder} and {@link Camera}. When not recording,
     * it prepares the {@link MediaRecorder} and starts recording.
     *
     * @param view the view generating the event.
     */
    
    private void stopCapture(boolean quit) {
    	  if (mMediaEngine != null) {
              // stop recording and release camera
    		  onCaptureStopped();
              try {
              	mMediaEngine.stop();  // stop the recording
              } catch (IllegalStateException e) {
                  // TODO 相机并没有产生数据，需要清理结果
                  Toast.makeText(this, "相机录像失败", Toast.LENGTH_LONG).show();
              }
              releaseMediaRecorder(); // release the MediaRecorder object
          }
          // inform the user that recording has stopped
          isRecording = false;
//          releaseCamera();
          if(quit) {
        	  onCaptureFileReady(mediaFilePath);
        	  if(mCamera!=null) {
        	  mCamera.stopPreview();
  				mCamera.release();
  				mCamera = null;
        	  }
          } else {
        	  mMediaEngine.setCamera(mCamera);
        	  new File(mediaFilePath).delete();
          }
    }
    
    @Override
    public void onBackPressed() {
    	if(isRecording)
    		stopCapture(true);
    	super.onBackPressed();
    }
    
    
    public void toggleCapture(boolean quit) {
    	if(!startok)
    		return;
        if (isRecording) {
          stopCapture(quit);
        } else {
            if (!preparingRecorder) {
            	preparingRecorder = true;
                 camerastartuphandler.post(new MediaPrepareTask());
            }
        }
    }

    /**
     * Asynchronous task for preparing the {@link MediaRecorder} since it's a long blocking
     * operation.
     */
    
    protected enum FocusState{
    	NORMAL,
    	SUCCESS,
    	FAIL
    };
  abstract  protected void updateFocus(FocusState  state);
    protected void sendAutoFocus() {
		updateFocus(FocusState.NORMAL);
    	camerastartuphandler.post(new Runnable() {
			
			@Override
			public void run() {
				if(mCamera != null)
					mCamera.autoFocus(AbsCameraPreviewActivity.this);
			}
		});
    }
    
    
   protected  class MediaPrepareTask implements  Runnable {

		@Override
		public void run() {
			if(mCamera == null) {
				preparingRecorder = false;
				return;
			}
			boolean startok = false;
			if(mPreview.isSupportAutoFocus()) {
				sendAutoFocus();
			}
			  if (prepareVideoRecorder()) {
	                try {
	                    // Camera is available and unlocked, MediaRecorder is prepared,
	                    // now you can start recording
	                	mMediaEngine.start();
	                	startok = true;
	                } catch (Exception e) {
	                    releaseMediaRecorder();
	                }
	            } else {
	                // prepare didn't work, release the camera
	                releaseMediaRecorder();
	            }
			  preparingRecorder = false;
			  if(startok) {
				  runOnUiThread(new Runnable() {
						@Override
						public void run() {
				  isRecording = true;
	                // inform the user that recording has started
	                onCaptureStarted();
						}});
			  } else {
				  runOnUiThread(
						  new Runnable() {
						@Override
						public void run() {
							isRecording = false;
				  Toast.makeText(getApplicationContext(), "无法启动摄像机", Toast.LENGTH_SHORT).show();
	                AbsCameraPreviewActivity.this.finish();
						}});
			  }
		}
	   
} 

    protected abstract void onCaptureStarted();

    protected abstract void onCaptureStopped();

    // 处理文件
    protected abstract void onCaptureFileReady(String mediaFilePath);
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
}
