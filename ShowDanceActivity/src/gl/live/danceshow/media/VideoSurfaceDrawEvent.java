package gl.live.danceshow.media;

import java.util.LinkedList;

import gl.live.danceshow.ui.widget.FixedLyricView;
import gl.live.danceshow.ui.widget.IPreviewTexture;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

public class VideoSurfaceDrawEvent implements Runnable {
	private Drawable fg;
	private static final String TAG = "videodraw";
	private FixedLyricView mLyricView;
	private Paint mPaint;
	private int textscalerate = 1;
	private float scaledDensity;
	protected MediaEngine mEngine;
	protected Context mContext;
	private Surface mVideoSurface;
	protected  IPreviewTexture mIPreviewTexture;
	private long mCurrentPTS;
	private float textmarginbottom = 1.0f;
	public VideoSurfaceDrawEvent(MediaEngine engine, Context context) {
		mEngine = engine;
		mContext = context;
		scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
		textscalerate= 640/PreviewManager.EXPECTED_PREVIEW_W ;
	}

	@Override
	public void run() {
		synchronized (mEngine) {
		if (!mEngine.isStart() || mLyricView == null) {
			updateAvatorIfNeed();
			return;
		}
		int version = Build.VERSION.SDK_INT;
		Canvas canvas;
		
		if (version >= Build.VERSION_CODES.M && Build.MODEL.equals("MI 5")) {
			Log.d(TAG, "lockHardwareCanvas in");
			canvas = mVideoSurface.lockHardwareCanvas();
			Log.d(TAG, "lockHardwareCanvas out");
			if (canvas == null)
				return;
		} else
			canvas = mVideoSurface.lockCanvas(null);
		
		try {
			if (mPaint == null) {
				mPaint = new Paint(mLyricView.getPaint());
				mPaint.setAntiAlias(true);
				mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
				mPaint.setTextSize(mLyricView.getTextSize() / (scaledDensity*textscalerate));
				textmarginbottom = mLyricView.getTextRatio();
				Log.d(TAG,"textmarginbottom "+textmarginbottom);
			}
			
				//draw camera preview
				Bitmap previewbitmap = mEngine.getPreviewBitmap() ;
				if(previewbitmap == null)
					return;
				Matrix mBitmapMatrix = getMatrix();
				drawCameraPreview(canvas, mBitmapMatrix,previewbitmap);

				// draw frame
				Bitmap b = null;
				if (fg != null) {
					b = ((BitmapDrawable) fg.getCurrent()).getBitmap();
				}
				if (b != null && !b.isRecycled()) {
					canvas.drawBitmap(b, 0, 0, null);
				}

				// draw text
				String text = mLyricView.getText().toString();
				canvas.drawText(text,
						canvas.getWidth() / 2 - mPaint.measureText(text) /2,
						canvas.getHeight() - textmarginbottom*PreviewManager.EXPECTED_PREVIEW_H-20, mPaint);
			
		} finally {
			mVideoSurface.unlockCanvasAndPost(canvas);
		}
		if(mCurrentPTS!= -1)
			mEngine.sendVideoMux(mCurrentPTS);
		}
	}

	public void setCurrentPTS(long pts) {mCurrentPTS = pts;}
	
	protected void updateAvatorIfNeed() {
	}

	public void setSurfaceForeground(Drawable fg) {
		synchronized (this) {
			this.fg = fg;
			// this.prefg = prefg;
			// this.bpre = bpre;
		}
	}

	protected Matrix getMatrix() {
		return mIPreviewTexture.getPreviewTransform();
	}

	protected void drawCameraPreview(Canvas canvas, Matrix mBitmapMatrix,Bitmap preview) {
	}

	public void setLyricView(FixedLyricView view) {
		mLyricView =view;
	}

	public void setIPreviewTexture(IPreviewTexture cameraView) {
		mIPreviewTexture  =cameraView;
	}

	public void setSurface(Surface mVideoSurface) {
		this.mVideoSurface = mVideoSurface;
	}
}