package gl.live.danceshow.media;

import android.content.Context;


public abstract class MediaMuxerWrapper {
    static final String TAG = "MediaMuxWrapper";
    private TrackInfo mTrackInfo;
    private long pts;
    
    protected Context c;
    protected String outputLocation;


    public MediaMuxerWrapper(Context c, String outputLocation, TrackInfo info){
        this.c = c;
        this.outputLocation = outputLocation;
        mTrackInfo = info;
    }

    public void start() {
    	
    }
    
    public void pause() {
    	
    }
    
    public void stop(){
    	
    }
    public void init() {
    	
    }
    
    public void release() {
    	
    }
}