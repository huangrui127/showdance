package gl.live.danceshow.ui.camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import gl.live.danceshow.media.PreviewManager;
import gl.live.danceshow.ui.camera.AbsCameraPreviewActivity.CameraEvent;
import gl.live.danceshow.ui.utils.Size;
import gl.live.danceshow.ui.widget.AspectAvatorView;
import gl.live.danceshow.ui.widget.AspectTextureView;
import gl.live.danceshow.ui.widget.IPreviewTexture;

/**
 * A simple wrapper around a Camera and a SurfaceView that renders a centered preview of the Camera
 * to the surface. We need to center the SurfaceView because not all devices have cameras that
 * support preview sizes at the same aspect ratio as the device's display.
 */
public class CenteredPreview extends FrameLayout implements TextureView.SurfaceTextureListener, View.OnTouchListener {
    private final String TAG = "Preview";

    IPreviewTexture mSurfaceView;
    Camera mCamera;
    private Context mContext;
    private int rotaiton;


    CenteredPreview(Context context) {
        super(context);
        mContext = context;
        setOnTouchListener(this);
//        mSurfaceView.setSurfaceTextureListener(this);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
    }

    public void setAvator(int avator) {
    	removeAllViews();
    	View v;
    	switch (avator) {
		case 1:
			v = new AspectTextureView(mContext);
	        addView(v);
	        ((AspectTextureView)v).setSurfaceTextureListener(this);
			break;
		case 2:
		case 3:
			v = new AspectAvatorView(mContext,avator);
			FrameLayout.LayoutParams params  = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			v.setLayoutParams(params);
	        addView(v);
	        break;
		default:
			return;
		}
    	mSurfaceView = (IPreviewTexture)v;
    }
    
    public void setCamera(Camera camera, int cameraCurrentlyLocked) {
        mCamera = camera;
        if (mCamera != null) {
        	List<Camera.Size>  mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            setCameraOrientationPortrait(cameraCurrentlyLocked, mCamera);
            if (mSupportedPreviewSizes != null) {
            	Camera.Size mPreviewSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes, PreviewManager.PREVIEW_W, PreviewManager.PREVIEW_H);
                PreviewManager.PREVIEW_W = mPreviewSize.width;
                PreviewManager.PREVIEW_H = mPreviewSize.height;
            }
            post(new Runnable() {
				@Override
				public void run() {
					requestLayoutParent();
				}
			});
        }
    }

    protected final void setCameraOrientationPortrait(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);

        this.rotaiton = result;
    }

    /**
     * 选择预览宽高比
     */
    private void requestLayoutParent() {
            // 选择预览宽高比
//            Log.i("camera", String.format("选择了分辨率 %s : %s 宽高比 %s", mPreviewSize.width, mPreviewSize.height, (double)optimalSize.width / (double)optimalSize.height));
            mSurfaceView.setAspectRatio((double) PreviewManager.PREVIEW_W / (double) PreviewManager.PREVIEW_H);
        requestLayout();
    }

    public void switchCamera(Camera camera, int cameraId) {
        setCamera(camera, cameraId);
        try {
        	Log.d(TAG," mSurfaceView.getTexture() "+PreviewManager.PREVIEW_W + "  " +PreviewManager.PREVIEW_H);
        	if(mSurfaceView.getTexture()!=null)
        		camera.setPreviewTexture(mSurfaceView.getTexture());
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(PreviewManager.PREVIEW_W, PreviewManager.PREVIEW_H);
        
        onOrientationChanged(parameters, 0, cameraId);
//        requestLayoutParent();

        //Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
        if (hasFocusFunction(camera,Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            bSupportAutoFocus = true;
        } else {
        	bSupportAutoFocus = false;
        }
        
        camera.setParameters(parameters);
    }

    
    private boolean bSupportAutoFocus = false;
    public final void onOrientationChanged(Camera.Parameters mParameters, int orientation, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        orientation = (orientation + 45) / 90 * 90;
        int rotation = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else {  // back-facing camera
            rotation = (info.orientation + orientation) % 360;
        }
        this.rotaiton = rotation;
        mParameters.setRotation(rotation);
    }

    private boolean hasFocusFunction(Camera camera,String mode) {
        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        if (supportedFocusModes != null)
            return supportedFocusModes.contains(mode);
        return false;
    }

//    public void surfaceCreated(SurfaceHolder holder) {
//        // The Surface has been created, acquire the camera and tell it where
//        // to draw.
//        try {
//            if (mCamera != null) {
//                mCamera.setPreviewDisplay(holder);
//            }
//        } catch (IOException exception) {
//            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
//        }
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // Surface will be destroyed when we return, so stop the preview.
//        if (mCamera != null) {
//            mCamera.stopPreview();
//        }
//    }


//    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
//        final double ASPECT_TOLERANCE = 0.1;
//        double targetRatio = (double) w / h;
//        if (sizes == null) return null;
//
//        Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        int targetHeight = h;
//
//        // Try to find an size match aspect ratio and size
//        for (Size size : sizes) {
//            double ratio = (double) size.width / size.height;
//            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
//            if (Math.abs(size.height - targetHeight) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - targetHeight);
//            }
//        }
//
//        // Cannot find the one match the aspect ratio, ignore the requirement
//        if (optimalSize == null) {
//            minDiff = Double.MAX_VALUE;
//            for (Size size : sizes) {
//                if (Math.abs(size.height - targetHeight) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - targetHeight);
//                }
//            }
//        }
//        return optimalSize;
//    }

    public Size getPreviewSize() {
        return new Size(PreviewManager.EXPECTED_PREVIEW_W,PreviewManager.EXPECTED_PREVIEW_H);
    }


//    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//        // Now that the size is known, set up the camera parameters and begin
//        // the preview.
//        if(mCamera != null) {
//            Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//            requestLayout();
//
//            mCamera.setParameters(parameters);
//            mCamera.startPreview();
//        }
//    }


//    public SurfaceHolder getHolder() {
//        if (mSurfaceView != null)
//            return mSurfaceView.getHolder();
//        else return null;
//    }

    public int getCameraRotation() {
        return rotaiton;
    }

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
			Log.d(TAG,"onSurfaceTextureAvailable surface" +surface);
            ((AbsCameraPreviewActivity)getContext()).onEventMainThread(new CameraEvent(surface));
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// Now that the size is known, set up the camera parameters and begin
        // the preview.
		Log.d(TAG,"onSurfaceTextureSizeChanged");
//		((AbsCameraPreviewActivity)getContext()).onEventMainThread(new Runnable() {
//			@Override
//			public void run() {
//				 if(mCamera != null) {
//			            Camera.Parameters parameters = mCamera.getParameters();
//			            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//			            requestLayout();
//
//			            mCamera.setParameters(parameters);
//			            mCamera.startPreview();
//			        }				
//			}
//		});
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		Log.d(TAG,"onSurfaceTextureDestroyed");
//		((AbsCameraPreviewActivity)getContext()).onEventMainThread(new Runnable() {
//			@Override
//				public void run() {
//				if (mCamera != null) {
//		            mCamera.stopPreview();
//		        }
//				}
//        
//		});
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//		if(mListerner!=null)
//			mListerner.OnFrameUpdate(mSurfaceView);
	}
	
	public IPreviewTexture getCameraView() {
		return mSurfaceView;
	}

	public boolean isSupportAutoFocus() {
		return bSupportAutoFocus;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!bSupportAutoFocus) return false;
		 ((CameraPreviewActivity)getContext()).onEventMainThread(event);
		return true;
	}
}
