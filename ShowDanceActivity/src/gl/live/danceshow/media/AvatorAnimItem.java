package gl.live.danceshow.media;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.utils.BitmapCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Size;
import android.util.Log;

public class AvatorAnimItem extends AnimItem {
	private String mTitle;
	private File mFile;
	public AvatorAnimItem(String f) {
		super(-1, -1);
		mTitle =f;
	}
	
	public void add(File...files) {
		for(File f:files) {
			if(f.isDirectory()) {
//				Log.d("guolei","f dir "+f.getAbsolutePath());
				add(f.listFiles());
				continue;
			}
			Log.d("guolei","f  "+f.getAbsolutePath());
			String name = f.getName();
			if(name.substring(name.lastIndexOf(".")+1).equals("png"))
				mFile = f;
		}
	}
	
	@Override
	public String getDtName() {
		return mTitle;
	}
	@Override
	public Drawable getDrawable(Context context, int w, int h, boolean bpreview) {
		try {
//		BitmapDrawable d = new BitmapDrawable();
		Options opts = getOptions();
		if (bpreview)
			opts.inSampleSize = 12;
//		BitmapCache cache = BitmapCache.getInstance();
//		for (File f : mFileList) {
		File f = mFile;
			if(f == null ||!f.exists())
				return null;
//			String path = f.getAbsolutePath();
			Bitmap b;
//			b= cache.getBitmap(path);
//			if (b == null) {
				b = BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
//				cache.putBitmap(path, b);
//			}
			Drawable drawable = createDrawable(context, w, h, b);
			b.recycle();
//			if (drawable != null)
//				d.addFrame(drawable, 333);
//			if (bpreview)
//				break;
//		}
//		if(d.getNumberOfFrames() == 0)
//			return null;
//		d.setOneShot(false);
		return drawable;
		} catch (Exception e) {
			return null;
		}
	}
	

	public boolean isEmpty() {
		if (mFile == null) {
			return true;
		}
		return false;
	}

	public String getUri() {
		return mFile.getAbsolutePath();
	}
}