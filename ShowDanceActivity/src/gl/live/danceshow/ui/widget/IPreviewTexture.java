package gl.live.danceshow.ui.widget;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;


public interface IPreviewTexture {
	SurfaceTexture getTexture();
	void setPreviewTransform(Matrix m,float y);
	Matrix getPreviewTransform();
	void setPreviewScaleX(int scale);
	int getPreviewHeight();
	void setAspectRatio(double d);
}
