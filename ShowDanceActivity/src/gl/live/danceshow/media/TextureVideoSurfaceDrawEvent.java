package gl.live.danceshow.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class TextureVideoSurfaceDrawEvent  extends VideoSurfaceDrawEvent {
    private float scale = -1;
    
    public TextureVideoSurfaceDrawEvent(MediaEngine engine, Context context) {
    	super(engine, context);
    }

	@Override
	protected void drawCameraPreview(Canvas canvas,Matrix mBitmapMatrix,Bitmap outputBitmap) {
		Bitmap buf = outputBitmap;
		if(scale == -1) {
			scale= (float)PreviewManager.EXPECTED_PREVIEW_H/(float)outputBitmap.getHeight();
		}
  	if(mBitmapMatrix !=null) {
  		mBitmapMatrix.preScale(scale, scale);
  		buf = Bitmap.createBitmap(buf, 0, 0, outputBitmap.getWidth(), outputBitmap.getHeight(), mBitmapMatrix, true);
//  		buf = Bitmap.createScaledBitmap(buf, PREVIEW_WIDTH, PREVIEW_HEIGHT, true);
  	}
  	int h = buf.getHeight();
  	canvas.drawBitmap(buf,0,(PreviewManager.EXPECTED_PREVIEW_H-h)/2, null);
	}
	
}