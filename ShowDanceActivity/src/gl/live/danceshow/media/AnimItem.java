package gl.live.danceshow.media;

import com.android.app.showdance.utils.L;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AnimItem {
    private int rawId;
    private int stringId;

    public AnimItem(int string, int raw) {
        rawId = raw;
        stringId = string;
    }

    public int getRawId() {
        return rawId;
    }

    public int getName() {
        return stringId;
    }

    public String getDtName() {
        return null;
    }

    protected Options getOptions() {
        Options option = new BitmapFactory.Options();
        option.inPreferredConfig = Bitmap.Config.ARGB_8888;
        option.inJustDecodeBounds = false;
        return option;
    }

    protected Drawable createDrawable(Context context, int w, int h, Bitmap b) {
        if (b == null)
            return null;
        b = Bitmap.createScaledBitmap(b, w, h, true);
        L.d("guolei", "b w " + b.getWidth() + " h " + b.getHeight());
        Drawable d = new BitmapDrawable(context.getResources(), b);
        return d;
    }

    public Drawable getDrawable(Context context) {
        // if(size == null)
        // return null;
        return getDrawable(context, PreviewManager.EXPECTED_PREVIEW_W, PreviewManager.EXPECTED_PREVIEW_H, false); // 传入false，默认不进行图片压缩
    }

    protected Drawable getDrawable(Context context, int w, int h, boolean bpreview) {
        Options opts = getOptions();
        if (bpreview)
            opts.inSampleSize = 2;
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), rawId, opts);
        return createDrawable(context, w, h, b);
    }
}