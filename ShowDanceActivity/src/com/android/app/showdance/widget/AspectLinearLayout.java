package com.android.app.showdance.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.widget.LinearLayout;

/**
 * 根据宽度，调整宽高比的SurfaceView
 * Created by zhengxiao on 14-2-28.
 */
public class AspectLinearLayout extends LinearLayout {
    private double aspectRatio = 16.0 / 9.0;//4.0 / 3.0;

    public AspectLinearLayout(Context context) {
        super(context);
    }

    public AspectLinearLayout(@NonNull Context context, AttributeSet attrs) {
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
    
    public interface OnFrameUpdateListener {
    	void OnFrameUpdate(TextureView texture);
    }
}
