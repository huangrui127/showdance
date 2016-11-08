package gl.live.danceshow.media;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import com.android.app.showdance.ui.PreSummeryEditorActivity.OnFrameUpdateListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera.Size;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;


public class StaticMediaEngine implements OnFrameUpdateListener{
    private static final String TAG = "StaticMediaEngine";
    
    private static final int AUDIO_BITRATE= 128000;//128kb/s
	private static final int VIDEO_BITRATE = 5000000;
	private int PREVIEW_WIDTH = 640;
	private int PREVIEW_HEIGHT = 360;
    private MediaMuxer mVideoMuxer;
    private MediaCodec mVideoEncoder;
    private MediaCodec mAudioEncoder;
    private MediaFormat mVideoFormat;
    private boolean bStop = true;
    private Surface mVideoSurface;
    
    private Handler mVideoSurfaceDraw;
    private Handler mVideoHandler;
    private Handler mAudioEncodeHandler;
    private Handler mMainHandler;
    
    private Activity mContext;

    private MediaFormat mAudioFormat;
    private int mAudioTrackIndex=-1;
    private String AUDIO_MIME_TYPE ="audio/mp4a-latm";
    
    public StaticMediaEngine(Context context) {
        mContext = (Activity)context;
        initThreads();
    }
    

    private void initThreads() {
    	mMainHandler = new Handler(mContext.getMainLooper());
        
        HandlerThread ht = new HandlerThread("video encoder");
        ht.start();
        mVideoHandler = new Handler(ht.getLooper());
        
        
        ht = new HandlerThread("audio encode");
        ht.start();
        mAudioEncodeHandler = new Handler(ht.getLooper());

        
        ht = new HandlerThread("VideoSurfaceDraw");
        ht.start();
        mVideoSurfaceDraw = new Handler(ht.getLooper());
        
        mVideoSurfaceDrawRunnable = new VideoSurfaceDrawEvent();
    }
    AudioStaticEncodeEvent mAudioEncoderEvent;
    public boolean initStaticAudio() throws IOException {
        int samplerate = 44100;
        int channelcount = 2;
            mAudioFormat = MediaFormat.createAudioFormat(AUDIO_MIME_TYPE, samplerate, channelcount);
            mAudioEncoderEvent = new AudioStaticEncodeEvent();
            mAudioEncoderEvent.setBuffer(4608);//default value
            mAudioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mAudioFormat.setInteger(MediaFormat.KEY_BIT_RATE, AUDIO_BITRATE);
            mAudioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 64000);
            mAudioEncoder = MediaCodec.createEncoderByType(AUDIO_MIME_TYPE);
            mAudioEncoder.configure(mAudioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        return true;
    }

    
    public boolean initVideo(String path, String type) throws IOException {
            mVideoEncoder = MediaCodec.createEncoderByType(type);

            mVideoMuxer = new MediaMuxer(path,
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        return true;
    }
    
    public void setStaticPreviewDisplay() {
      mVideoFormat = MediaFormat.createVideoFormat("video/avc",
              PREVIEW_WIDTH, PREVIEW_HEIGHT);
      mVideoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);// min fps
      mVideoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
              MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
      mVideoFormat.setInteger(MediaFormat.KEY_BIT_RATE, VIDEO_BITRATE);
      mVideoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
      Log.i(TAG," mVideoFormat " + mVideoFormat.toString());
      mVideoEncoder.configure(mVideoFormat, null, null,
              MediaCodec.CONFIGURE_FLAG_ENCODE);
      mVideoSurface = mVideoEncoder.createInputSurface();
    }

    
    public void prepare() {
        mVideoEncoder.start();
        mAudioEncoder.start();
    }

    public boolean start() {
        bStop = false;
        mAudioEncodeHandler.post(mAudioEncoderEvent);
        mVideoHandler.post(mAudioMuxEvent);
        return true;
    }
    
    public void setPreviewForegroud(int resid) {
    	Bitmap fg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), resid), PREVIEW_WIDTH, PREVIEW_HEIGHT, false);
    	Log.d(TAG,"bg w "+fg.getWidth() +" h "+fg.getHeight());
//    	mVideoSurfaceDrawRunnable.setSurfaceForeground(fg);
    }
    
    public void setPreviewForegroud(String bitmap_fg) {
    	Bitmap fg = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(bitmap_fg), PREVIEW_WIDTH, PREVIEW_HEIGHT, false);
    	Log.d(TAG,"bg w "+fg.getWidth() +" h "+fg.getHeight());
//    	mVideoSurfaceDrawRunnable.setSurfaceForeground(fg);
    }

    public boolean stop() {
        if (bStop)
            return true;

        bStop = true;
        return true;
    }
    
    private long audioStartTime;
    private long videoStartTime;

    
	private boolean isPrepared() {
		synchronized (mVideoMuxer) {
			return mAudioOK&& mVideoOK;
		}
	}
    
    
    private boolean bfirst = true;
    private boolean bVideoFirst = true;
    private interface MyRunnable extends Runnable {
    	public void setBuffer(byte[] buf);
	}
    
    
    private class AudioStaticEncodeEvent implements Runnable {
    	
    	private byte[] mBuffer;
    	public AudioStaticEncodeEvent() {
    	}
    	
    	@Override
    	public void run(){
    		if(bStop) {
    			mAudioEncodeHandler.removeCallbacks(this);
        		return;
        	}
    		
        ByteBuffer[] in = mAudioEncoder.getInputBuffers();
        int index = mAudioEncoder.dequeueInputBuffer(0);
        if(index>=0) {
//        	Log.w(TAG,"mAudioEncode dequeueInputBuffer-------------- " +index+ " mBuffer length "+mBuffer.length);
        	ByteBuffer inputBuffer = in[index];
        	inputBuffer.clear();
        	inputBuffer.put(mBuffer);
        	 if(bfirst) {
         		bfirst = false;
         		audioStartTime = System.nanoTime();
         	}
        	long presentationTimeUs = (System.nanoTime() - audioStartTime) / 1000;
            mAudioEncoder.queueInputBuffer(index, 0, mBuffer.length, presentationTimeUs, 0);
        } else {
        	Log.w(TAG,"mAudioEncode error " +index);
        }
        mAudioEncodeHandler.postDelayed(this, 22);
    	}

		public void setBuffer(int size) {
			mBuffer =  new byte[size];
		}
    };
    
    private long lastEncodedAudioTimeStamp = 0;
    private Runnable mAudioMuxEvent = new Runnable() {
    	@Override
    	public void run(){
    		handleAudioMux();
    	}
    };
    
    private void handleAudioMux(){
		if(bStop) {
			mVideoHandler.removeCallbacks(mAudioMuxEvent);
    		return;
    	}
		
    ByteBuffer[] out = mAudioEncoder.getOutputBuffers();
    BufferInfo info = new BufferInfo();
    while(true){
    int index = mAudioEncoder.dequeueOutputBuffer(info, 0);
    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
        if (!bStop) {
            Log.w(TAG, "reached end of audio stream unexpectedly");
            mAudioEncoder.releaseOutputBuffer(index, false);
            bStop = true;
            return;
        } else {
            Log.d(TAG, "end of stream reached");
        }
    } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
        Log.w(TAG, "audio INFO_OUTPUT_BUFFERS_CHANGED");
        out = mAudioEncoder.getOutputBuffers();
    } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
    	if (isPrepared()) {
            throw new RuntimeException("format changed twice");
        }
        MediaFormat newFormat = mAudioEncoder.getOutputFormat();
       
        Log.d(TAG, "audio encoder output format changed: " + newFormat);

        // now that we have the Magic Goodies, start the muxer
        mAudioTrackIndex = mVideoMuxer.addTrack(newFormat);
        synchronized (mVideoMuxer) {
        	if (mVideoOK) {
            	mVideoMuxer.start();
            }
            mAudioOK = true;
		}
    } else if (info.flags == MediaCodec.INFO_TRY_AGAIN_LATER) {
        Log.w(TAG, "audio INFO_TRY_AGAIN_LATER");
        break;
    } else if(index <0 ) {
//        Log.w(TAG, "audio encoder ---------- index "+index);
    	break;
			} else {
				if (!isPrepared()) {
					Log.w(TAG, "audio muxer is not start ");
					mAudioEncoder.releaseOutputBuffer(index, false);
					mVideoHandler.postDelayed(mAudioMuxEvent, 10);
					return;
				}
				// Log.e("guolei",
				// "audio muxer writeSampleData info.size "+info.size
				// +" pts "+info.presentationTimeUs);
				out[index].position(info.offset);
				out[index].limit(info.offset + info.size);

				if (info.presentationTimeUs < lastEncodedAudioTimeStamp)
					info.presentationTimeUs = lastEncodedAudioTimeStamp += 23219;
				lastEncodedAudioTimeStamp = info.presentationTimeUs;
				if (info.presentationTimeUs < 0) {
					info.presentationTimeUs = 0;
				}
				mVideoMuxer.writeSampleData(mAudioTrackIndex, out[index], info);
				mAudioEncoder.releaseOutputBuffer(index, false);
			}
    }
    mVideoHandler.postDelayed(mAudioMuxEvent,10);
    }
    
    // delay time (avg)
    private class VideoMux implements Runnable {
    	private long pts;
        public   VideoMux(long pts) {
        	this.pts= pts;
        }
        @Override
        public void run() {
        	handleVideoMux(pts);
        }};
        
        	private void handleVideoMux(long pts){
                if (bStop) {
//                	mVideoHandler.removeCallbacks(mDrainEncoder);
                    return;
                }
                BufferInfo mBufferInfo = new BufferInfo();
                int encoderStatus = mVideoEncoder.dequeueOutputBuffer(
                        mBufferInfo, 0);
                ByteBuffer[] encoderOutputBuffers = mVideoEncoder.getOutputBuffers();
                if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (!bStop) {
                        return;// out of while
                    } else {
                        Log.d(TAG, "video no output available, spinning to await EOS");
                        return;
                    }
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not expected for an encoder
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // should happen before receiving buffers, and should only
                    // happen once
                    if (isPrepared()) {
                        throw new RuntimeException("video format changed twice");
                    }
                    MediaFormat newFormat = mVideoEncoder.getOutputFormat();
                    Log.d(TAG, "video encoder output format changed: " + newFormat);
                    videoStartTime = System.nanoTime();
                    // now that we have the Magic Goodies, start the muxer
                    mTrackIndex = mVideoMuxer.addTrack(newFormat);
                    
                    synchronized (mVideoMuxer) {
                    	if (mAudioOK) {
                        	mVideoMuxer.start();
                        }
                    	mVideoOK = true;
					}
                    
                } else if (encoderStatus < 0) {
                    Log.w(TAG,
                            "video unexpected result from encoder.dequeueOutputBuffer: "
                                    + encoderStatus);
                    return;
                    // let's ignore it
                } else {
                	
                	if(bVideoFirst) {
                		bVideoFirst = false;
                 		videoStartTime = pts;
                 	}
                	
                	long tmppts = pts-videoStartTime;//audioStartTime/1000- mVideoDelayTime;
                	if (!isPrepared()|| tmppts <0) {
                		mVideoEncoder.releaseOutputBuffer(encoderStatus, false);
                		return;
                	}
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    if (encodedData == null) {
                        throw new RuntimeException("video encoderOutputBuffer "
                                + encoderStatus + " was null");
                    }

                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        // The codec config data was pulled out and fed to the
                        // muxer when we got
                        // the INFO_OUTPUT_FORMAT_CHANGED status. Ignore it.
                        Log.d(TAG, "video ignoring BUFFER_FLAG_CODEC_CONFIG");
                        mBufferInfo.size = 0;
                    }

                    if (mBufferInfo.size != 0) {
                    	if (!isPrepared())  {
                            Log.w(TAG,"video muxer hasn't started");
                            mVideoEncoder.releaseOutputBuffer(encoderStatus, false);
//                            mVideoHandler.post(mDrainEncoder);
                            return;
                        }

                        // adjust the ByteBuffer values to match BufferInfo
                        encodedData.position(mBufferInfo.offset);
                        encodedData
                                .limit(mBufferInfo.offset + mBufferInfo.size);
                        mBufferInfo.presentationTimeUs = tmppts;
                        mVideoMuxer.writeSampleData(mTrackIndex, encodedData,
                                mBufferInfo);
//                        Log.e("guolei", "video sent " + mBufferInfo.size
//                                + " bytes to muxer pts "+mBufferInfo.presentationTimeUs+" audioStartTime "+audioStartTime + " videoStartTime "+videoStartTime);
                    }

                    mVideoEncoder.releaseOutputBuffer(encoderStatus, false);

                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        if (!bStop) {
                            Log.w(TAG, "video reached end of stream unexpectedly");
                        } else {
                            Log.d(TAG, "video end of stream reached");
                        }
                    }
                }
        }
    
    private boolean mAudioOK = false;
    private boolean mVideoOK = false;
    private int mTrackIndex = -1;
    private long mVideoDelayTime = 0;
    
    private class VideoSurfaceDrawEvent implements Runnable {
        private List<Bitmap> fg;
        private long starttime;
        private int index=0;
        public VideoSurfaceDrawEvent() {
//        	textpaint.setTextSize(18);
//        	textpaint.setTypeface(Typeface.DEFAULT_BOLD);
//        	textpaint.setColor(Color.GREEN);
        	starttime = System.currentTimeMillis();
        }
        @Override
        public void run() {
        	if(bStop)
        		return;
        	long lag = System.nanoTime();
        	
            Canvas canvas = mVideoSurface.lockCanvas(null);
            try {
                synchronized (this) {
                if(fg!=null) {
                	Bitmap b= fg.get(getIndex());
                	if(b !=null)
                		canvas.drawBitmap(Bitmap.createScaledBitmap(b, PREVIEW_WIDTH, PREVIEW_HEIGHT, false), 0,0, null);
                }
               }
            } finally {
                mVideoSurface.unlockCanvasAndPost(canvas);
            }
            mVideoDelayTime = (System.nanoTime()-lag)/1000;
        }

        
        private int getIndex() {
        	long now = System.currentTimeMillis();
        	int size = fg.size()-1;
        	if(index >=size)
        		return size;
        	if((now - starttime)>=3000) {
        		starttime = now;
        		index++;
        	}
        	
        	return index;
        }
		
		public void setSurfaceForeground(List<Bitmap> fg) {
			synchronized (this) {
				this.fg=fg;
			}
		}
    }

    private VideoSurfaceDrawEvent mVideoSurfaceDrawRunnable;

public boolean isStart() {
	return !bStop;
}

    public void startOrStop() {
        if (bStop)
            start();
        else
            stop();
    }

    public void release() {
    	Log.d(TAG, "release");
    	mVideoSurfaceDraw.removeCallbacks(mVideoSurfaceDrawRunnable);
    	mVideoSurfaceDraw.getLooper().quit();
        try {
        	mVideoSurfaceDraw.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	
        mVideoHandler.getLooper().quit();
        try {
            mVideoHandler.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "release audio encoder thread exit");
        mAudioEncodeHandler.getLooper().quit();
        try {
        	mAudioEncodeHandler.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Log.d(TAG, "release AudioPlay exit");
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }

        if (mVideoSurface != null) {
            mVideoSurface.release();
            mVideoSurface = null;
        }
        if (mVideoMuxer != null) {
            if(isPrepared())
                mVideoMuxer.stop();
            mVideoMuxer.release();
            mVideoMuxer = null;
        }
        if(mAudioEncoder!=null) {
        	mAudioEncoder.stop();
        	mAudioEncoder.release();
        	mAudioEncoder = null;
        }
    }
    
	public void setForegroundDrawable(List<Bitmap> fg) {
		mVideoSurfaceDrawRunnable.setSurfaceForeground(fg);
	}

	public void setPreviewSize(Size previewSize) {
		int ratio = 1;
		if(previewSize.height == 720)
			ratio =2;
		PREVIEW_WIDTH=previewSize.width/ratio;
		PREVIEW_HEIGHT=previewSize.height/ratio;
	}

	@Override
	public void OnFrameUpdate() {
		if(bStop)
			return;
		mVideoSurfaceDraw.removeCallbacks(mVideoSurfaceDrawRunnable);
		 mVideoSurfaceDraw.post(mVideoSurfaceDrawRunnable);
		 mVideoHandler.post(new VideoMux((System.nanoTime())/1000));
	}
}