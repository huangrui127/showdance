package com.android.app.showdance.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义的圆角矩形ImageView
 */
public class XCRoundRectImageView extends ImageView {

    private Paint paint;

    public XCRoundRectImageView(Context context) {
        this(context, null);
    }

    public XCRoundRectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCRoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    /**
     * 绘制圆角矩形图片
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {

             Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

//            Bitmap bitmap = drawableToBitamp(drawable); // 从网络加载的图片转换
//            // 一下三句必须加上，否则会看不到转换后的图
//            Canvas tempCanvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            drawable.draw(tempCanvas);

            Bitmap b = getRoundBitmap(bitmap, 20);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 功能：Drawable转Bitmap
     * 
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_4444
                : Bitmap.Config.RGB_565;
        return Bitmap.createBitmap(w, h, config);

    }

    /**
     * 获取圆角矩形图片方法
     * 
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        @SuppressWarnings("unused")
        int x = bitmap.getWidth();

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

}
