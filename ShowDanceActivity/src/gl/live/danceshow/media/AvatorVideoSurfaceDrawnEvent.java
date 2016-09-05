package gl.live.danceshow.media;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class AvatorVideoSurfaceDrawnEvent extends VideoSurfaceDrawEvent{
	private int mAvator = 1;
	private Matrix matrix;
	private float scale = -1;
	private Rect mRect;
	public AvatorVideoSurfaceDrawnEvent(MediaEngine engine, Context context) {
		super(engine, context);
	}
	
	public void setAvator(int avator) {
		mAvator = avator;
		mRect = new Rect();
		int w = PreviewManager.PREVIEW_W/mAvator;
		mRect.left = (PreviewManager.PREVIEW_W -w)/2;
		mRect.right = (PreviewManager.PREVIEW_W +w)/2;
		mRect.bottom = 0;
		mRect.top = 0;
	}
	
	private void postAvatorBitmap(Bitmap b) {
		EventBus.getDefault().post(new AvatorImageDrawableEvent(b));
	}
	
	@Override
	protected void updateAvatorIfNeed() {
		synchronized (mEngine) {
			Bitmap mAvatorBitmap = Bitmap.createBitmap(mEngine.getPreviewBitmap(), mRect.left, 0,
					mRect.right - mRect.left, mEngine.getPreviewBitmap().getHeight(),
					null, true);
			postAvatorBitmap(Bitmap.createBitmap(mAvatorBitmap));
		}
	}
	
	@Override
	protected void drawCameraPreview(Canvas canvas,
			Matrix mBitmapMatrix,Bitmap outputBitmap) {
		if (scale == -1) {
			scale = (float) PreviewManager.EXPECTED_PREVIEW_H
					/ (float) outputBitmap.getHeight();
		}
		Bitmap mAvatorBitmap;
		if (mBitmapMatrix == null) {
			mBitmapMatrix = new Matrix();
		}
		mBitmapMatrix.preScale(scale, scale);
		int w = mRect.right - mRect.left;
//		Log.d("guolei","w "+w+ " outputBitmap.getHeight() "+outputBitmap.getHeight());
		mAvatorBitmap = Bitmap.createBitmap(outputBitmap, mRect.left, 0,
				w, outputBitmap.getHeight(),
				mBitmapMatrix, true);
		// buf = Bitmap.createScaledBitmap(buf, PREVIEW_WIDTH, PREVIEW_HEIGHT,
		// true);
		int h = mAvatorBitmap.getHeight();
//		Log.d("guolei","drawCameraPreview w +"+mAvatorBitmap.getWidth() + "x "+h);
		for (int i = 0; i < mAvator; i++) {
			canvas.drawBitmap(mAvatorBitmap, i * mAvatorBitmap.getWidth(),
					(PreviewManager.EXPECTED_PREVIEW_H - h) / 2, null);
		}
		postAvatorBitmap(Bitmap.createBitmap(mAvatorBitmap));
	}
}