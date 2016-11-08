package gl.live.danceshow.media;

import android.media.MediaMuxer;

public class TrackInfo {
	public int mIndex = 0;
	public MediaMuxer mMuxer;

	public TrackInfo(int index, MediaMuxer muxer) {
		mIndex = index;
		mMuxer = muxer;
	}

}