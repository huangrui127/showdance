package gl.live.danceshow.ui.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 根据宽度，调整宽高比的SurfaceView
 * Created by zhengxiao on 14-2-28.
 */
public class AspectTextureView extends TextureView implements OnTouchListener,IPreviewTexture {
    private double aspectRatio = 16.0 / 9.0;//4.0 / 3.0;

    public AspectTextureView(Context context) {
        super(context);
    }

    public AspectTextureView(@NonNull Context context, AttributeSet attrs) {
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
        super.onMeasure(measuredWidth, measuredHeight);
    }
    
    public interface OnFrameUpdateListener {
    	void OnFrameUpdate(TextureView texture);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public SurfaceTexture getTexture() {
		return getSurfaceTexture();
	}

	@Override
	public void setPreviewTransform(Matrix m,float y) {
		setTransform(m);
	}

	@Override
	public void setPreviewScaleX(int scale) {
		setScaleX(scale);
	}

	@Override
	public int getPreviewHeight() {
		return getHeight();
	}

	@Override
	public Matrix getPreviewTransform() {
		return getTransform(null);
	}
}
