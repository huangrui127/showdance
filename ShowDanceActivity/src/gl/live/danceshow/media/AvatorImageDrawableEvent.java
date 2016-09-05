package gl.live.danceshow.media;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;


public class AvatorImageDrawableEvent {
	private Bitmap bbitmap;
	
	public AvatorImageDrawableEvent(Bitmap b){bbitmap = b;}
	public Bitmap getBitmap() {return bbitmap;}
}
