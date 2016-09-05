package gl.live.danceshow.ui.widget;

import javax.microedition.khronos.opengles.GL10;

import de.greenrobot.event.EventBus;
import gl.live.danceshow.media.AvatorImageDrawableEvent;
import gl.live.danceshow.media.PreviewManager;
import gl.live.danceshow.ui.camera.AbsCameraPreviewActivity;
import gl.live.danceshow.ui.camera.AbsCameraPreviewActivity.CameraEvent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES10;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AspectAvatorView extends  LinearLayout implements OnTouchListener ,IPreviewTexture, OnFrameAvailableListener{
    private double aspectRatio = 16.0 / 9.0;//4.0 / 3.0;
    private int mActor;
    private SurfaceTexture mSurfaceTexture;
    public AspectAvatorView(Context context, int actor) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        mActor = actor;
        for(int i = 0;i<mActor;i++) {
          	ImageView item = new ImageView(getContext());
          	item.setScaleType(ImageView.ScaleType.FIT_XY);
//          	item.setPivotY(0.5f);
          	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
          	addView(item, params);
          }
    }

    public void onEventMainThread(AvatorImageDrawableEvent event) {
    	setDanceShow(event.getBitmap());
//    	event.getBitmap().recycle();
    }
    
    private void setDanceShow(Bitmap b) {
    	Bitmap a =b;
    	
    	for(int i = 0;i<mActor;i++) {
    		ImageView v = (ImageView)getChildAt(i);
    		if(scaleY != -1) v.setScaleY(scaleY);
    		v.setImageBitmap(a);
          }
    }
    
    public AspectAvatorView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAspectRatio(double ratio) {
        aspectRatio = ratio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int receivedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int receivedHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measuredWidth;
        int measuredHeight;
        boolean widthDynamic;
        if (heightMode == MeasureSpec.EXACTLY) {
            if (widthMode == MeasureSpec.EXACTLY) {
                widthDynamic = receivedWidth == 0;
            } else {
                widthDynamic = true;
            }
        } else if (widthMode == MeasureSpec.EXACTLY) {
            widthDynamic = false;
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        if (widthDynamic) {
            // Width is dynamic.
            int w = (int) (receivedHeight * aspectRatio);
            measuredWidth = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
            measuredHeight = heightMeasureSpec;
        } else {
            // Height is dynamic.
            measuredWidth = widthMeasureSpec;
            int h = (int) (receivedWidth / aspectRatio);
            measuredHeight = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
//        Log.d("guolei","measuredWidth "+measuredWidth+" measuredHeight "+measuredHeight);
        
        super.onMeasure(measuredWidth, measuredHeight);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public SurfaceTexture getTexture() {
		if (mSurfaceTexture == null) {
//			int[] textures = new int[1];
//            GLES20.glGenTextures(1, textures, 0);
//            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
//         // No mip-mapping with camera source.
//         GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
//                 GL10.GL_TEXTURE_MIN_FILTER,
//                                 GL10.GL_LINEAR);        
//         GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
//                 GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//         // Clamp to edge is only option.
//         GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
//                 GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
//         GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
//                 GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			 final int GL_TEXTURE_EXTERNAL_OES = 10;// 0x8D65;
            int mTextureID =GL_TEXTURE_EXTERNAL_OES;// textures[0];
            mSurfaceTexture = new SurfaceTexture(mTextureID);
//            mSurfaceTexture.attachToGLContext(mTextureID);
//			surfaceTexture.setDefaultBufferSize(PreviewManager.PREVIEW_W,
//					PreviewManager.PREVIEW_H);
            mSurfaceTexture.setOnFrameAvailableListener(this);
		}
		return mSurfaceTexture;
	}

	private Matrix  mMatrix;
	private float scaleY = -1;
	@Override
	public void setPreviewTransform(Matrix m,float y) {
		mMatrix = m;
		scaleY = y;
	}

	@Override
	public void setPreviewScaleX(int scale) {
		
	}

	@Override
	public int getPreviewHeight() {
		return 720;
	}

	@Override
	public Matrix getPreviewTransform() {
		return new Matrix(mMatrix);
	}
	
	@Override
	protected void onAttachedToWindow() {
		EventBus.getDefault().register(this);
		((AbsCameraPreviewActivity)getContext()).onEventMainThread(new CameraEvent(getTexture()));
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		EventBus.getDefault().unregister(this);
		mSurfaceTexture.setOnFrameAvailableListener(null);
		super.onDetachedFromWindow();
	}

	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//		Log.d("guolei","onFrameAvailable");
	}
}
