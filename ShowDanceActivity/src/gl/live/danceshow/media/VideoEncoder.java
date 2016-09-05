package gl.live.danceshow.media;

import android.content.Context;

public class VideoEncoder extends MediaMuxerWrapper {

	public VideoEncoder(Context c, String outputLocation, TrackInfo info) {
		super(c, outputLocation, info);
	}

}
