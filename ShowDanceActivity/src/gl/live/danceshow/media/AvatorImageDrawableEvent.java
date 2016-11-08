package gl.live.danceshow.media;

import android.graphics.Bitmap;


public class AvatorImageDrawableEvent {
	private Bitmap bbitmap;
	
	public AvatorImageDrawableEvent(Bitmap b){bbitmap = b;}
	public Bitmap getBitmap() {return bbitmap;}
}
