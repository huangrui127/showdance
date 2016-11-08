package gl.live.danceshow.ui.widget;

import com.android.app.wumeiniang.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 根据宽度，调整宽高比的SurfaceView
 * Created by zhengxiao on 14-2-28.
 */
public class AspectFrameLayout extends FrameLayout {
    private double aspectRatio = 3.0 / 4.0;//4.0 / 3.0;

    public AspectFrameLayout(Context context) {
        super(context);
    }

    public AspectFrameLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectFrameLayout, defStyle, 0);
        float aspectWidth = a.getFloat(R.styleable.AspectFrameLayout_aspectWidth, 3.0f);
        float aspectHeight = a.getFloat(R.styleable.AspectFrameLayout_aspectHeight, 4.0f);
        aspectRatio = aspectWidth / aspectHeight;
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
//            if (widthMode == MeasureSpec.EXACTLY) {
//                widthDynamic = receivedWidth == 0;
//            } else 
            {
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
        super.onMeasure(measuredWidth, measuredHeight);
    }
}
