package gl.live.danceshow.media;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;


public interface IPreviewTexture {
	SurfaceTexture getTexture();
	void setPreviewTransform(Matrix m);
	Matrix getPreviewTransform();
	void setScaleX(int scale);
	int getPreviewHeight();
	void setAspectRatio(double d);
}
