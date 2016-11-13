package gl.live.danceshow.media;

import gl.live.danceshow.ui.camera.CameraPreviewActivity;
import gl.live.danceshow.ui.utils.Size;
import gl.live.danceshow.ui.widget.AspectTextureView.OnFrameUpdateListener;
import gl.live.danceshow.ui.widget.FixedLyricView;
import gl.live.danceshow.ui.widget.IPreviewTexture;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import com.android.app.wumeiniang.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.PlaybackParams;
import android.media.MediaCodec.*;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.Trace;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptC;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;


public class MediaEngine implements SurfaceTexture.OnFrameAvailableListener , OnFrameUpdateListener{
    private static final String TAG = "MediaEngine";
    private static boolean bDEBUG = false;
    
    private static final int AUDIO_BITRATE= 128000;//128kb/s
	private static final int VIDEO_BITRATE = 5000000;
//	private int PREVIEW_WIDTH = 640;
//	private int PREVIEW_HEIGHT = 360;
    private MediaExtractor mAudioExtractor;
    private MediaMuxer mVideoMuxer;
    private MediaCodec mVideoEncoder;
    private MediaCodec mAudioEncoder;
    private MediaCodec mAudioDecoder;
    private AudioTrack mAudioTrack;
    private MediaFormat mVideoFormat;
    private boolean bStop = true;
//    private Camera mCamera;
    private Surface mVideoSurface;
    
    private Handler mVideoSurfaceDraw;
    private Handler mVideoHandler;
    private Handler mAudioHandler;
    private Handler mAudioPlayHandler;
    private Handler mAudioEncodeHandler;
    private Handler mMainHandler;
    
    private Activity mContext;
//    private ConditionVariable mCondiVariable = new ConditionVariable();

    private MediaFormat mAudioFormat;
    private int mAudioTrackIndex=-1;
    private String AUDIO_MIME_TYPE ="audio/mp4a-latm";
    
    public MediaEngine(Context context) {
        mContext = (Activity)context;
    }
    
    public void initSurfaceDrawnThread() {
    	HandlerThread ht = new HandlerThread("VideoSurfaceDraw");
        ht.start();
        mVideoSurfaceDraw = new Handler(ht.getLooper());
    }
    
//    public void setCamera(Camera camera) {
//		mCamera = camera;
//		Log.d(TAG,"format = "+mCamera.getParameters().getPreviewFormat());
//    }

    public void initThreads() {
    	mMainHandler = new Handler(mContext.getMainLooper());
        
        HandlerThread ht = new HandlerThread("video encoder");
        ht.start();
        mVideoHandler = new Handler(ht.getLooper());
        
        ht = new HandlerThread("audio decoder");
        ht.start();
        mAudioHandler = new Handler(ht.getLooper());
        
        ht = new HandlerThread("audio encode");
        ht.start();
        mAudioEncodeHandler = new Handler(ht.getLooper());

        ht = new HandlerThread("audio play");
        ht.start();
        mAudioPlayHandler = new Handler(ht.getLooper());
        
        
      
    }
    
    public synchronized void setAvator(int avator) {
    	mVideoSurfaceDraw.removeCallbacks(mVideoSurfaceDrawRunnable);
    	switch (avator) {
		case 1:
			mVideoSurfaceDrawRunnable = new TextureVideoSurfaceDrawEvent(this, mContext);
			break;
		case 2:
		case 3:
			mVideoSurfaceDrawRunnable = new AvatorVideoSurfaceDrawnEvent(this, mContext);
			((AvatorVideoSurfaceDrawnEvent)mVideoSurfaceDrawRunnable).setAvator(avator);
			break;
		default:
			break;
		}
    	
    }
    
    public boolean initAudio(File file) throws IOException {
    	return initAudio(file.getAbsolutePath());
    }
    
    private int magicLag = 0;
    public boolean initAudio(String file) throws IOException {
        MediaFormat mFormat = null;
        mAudioExtractor = new MediaExtractor();
        int samplerate = 44100;
        int channelcount = 2;
        int br = AUDIO_BITRATE;
        	Log.e(TAG,"setDataSource E"+file);
            mAudioExtractor.setDataSource(file);
            int count = mAudioExtractor.getTrackCount();
            String mime = "audio/amr-wb";
            Log.d(TAG,"setDataSource X");
            for (int i = 0; i < count; i++) {
            	Log.d(TAG,"get track format ");
                mFormat = mAudioExtractor.getTrackFormat(i);
                mime = mFormat.getString(MediaFormat.KEY_MIME);
                
                if (mime.startsWith("audio")) {
                	channelcount = mFormat
                            .getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                    samplerate = mFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                    try {
                    	br=mFormat.getInteger(MediaFormat.KEY_BIT_RATE);
					} catch (Exception e) {
						Log.w(TAG,"KEY_BIT_RATE exception");
					}
                    mAudioTrackIndex = i;
                    break;
                }
            }
            
            if (samplerate < 0 || channelcount < 0)
                return false;
            magicLag = 44100*1000/samplerate*23219/1000;
            Log.i(TAG, "samplerate " + samplerate + " channelcount "
                    + channelcount + " mime " + mime+ " bitrate "+br+ " magicLag "+magicLag);
            mAudioDecoder = MediaCodec.createDecoderByType(mime);
            mAudioDecoder.configure(mFormat, null, null, 0);
            
            int mAudiotrackMinBufSize = AudioTrack.getMinBufferSize(samplerate,
            		channelcount == 2?AudioFormat.CHANNEL_OUT_STEREO:AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            Log.d(TAG,"mAudioMinBufSize "+mAudiotrackMinBufSize);
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    samplerate, channelcount == 2?AudioFormat.CHANNEL_OUT_STEREO:AudioFormat.CHANNEL_OUT_MONO,
                    		AudioFormat.ENCODING_PCM_16BIT, mAudiotrackMinBufSize,
                    AudioTrack.MODE_STREAM);
            
            mAudioFormat = MediaFormat.createAudioFormat(AUDIO_MIME_TYPE, samplerate, channelcount);
            mAudioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mAudioFormat.setInteger(MediaFormat.KEY_BIT_RATE, br);
            mAudioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 128000);
        	Log.d(TAG,"mAudioEncoder.createEncoderByType");
            mAudioEncoder = MediaCodec.createEncoderByType(AUDIO_MIME_TYPE);
            try {
            	Log.d(TAG,"mAudioEncoder.configure in");
            	mAudioEncoder.configure(mAudioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            	Log.d(TAG,"mAudioEncoder.configure ok");
			} catch (IllegalStateException e) {
				return false;
			}
            
        return true;
    }

    
    public boolean initVideo(String path, String type) throws IOException {
            mVideoEncoder = MediaCodec.createEncoderByType(type);
            mVideoMuxer = new MediaMuxer(path,
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        return true;
    }
    
    public void setPreviewDisplay(SurfaceHolder surface) {
      mVideoFormat = MediaFormat.createVideoFormat("video/avc",
              PreviewManager.EXPECTED_PREVIEW_W, PreviewManager.EXPECTED_PREVIEW_H);
      mVideoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 25);// min fps
      mVideoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
              MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
      mVideoFormat.setInteger(MediaFormat.KEY_BIT_RATE, VIDEO_BITRATE);
      mVideoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
      Log.i(TAG," mVideoFormat " + mVideoFormat.toString());
      mVideoEncoder.configure(mVideoFormat, null, null,
              MediaCodec.CONFIGURE_FLAG_ENCODE);
      mVideoSurface = mVideoEncoder.createInputSurface();
      if(mVideoSurfaceDrawRunnable != null)
    	  mVideoSurfaceDrawRunnable.setSurface(mVideoSurface);
    }

//    public void setPreviewWindow(SurfaceHolder surface) {
//    	mCondiVariable.block();
//        Parameters parameters = mCamera.getParameters();
//
//        List<Size> sizes = parameters.getSupportedPreviewSizes();
//        for (Size size : sizes) {
//            Log.i(TAG, "size = w " + size.width + "X h " + size.height);
//        }
//        
//        List<String> focusmodes = parameters.getSupportedFocusModes();
//        for(String focusmode : focusmodes) {
//        	if(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE.equals(focusmode)){
//        		parameters.setFocusMode(focusmode);
//        		break;
//        	}
//        }
//        
//        List<int[]> fpslist= parameters.getSupportedPreviewFpsRange();
//        for(int[] f:fpslist)
//        Log.d(TAG,"fps " +f[0]+ " "+f[1]);
//        parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
////        parameters.setPreviewFpsRange(fps[0], fps[1]);
//        mCamera.setParameters(parameters);
//
//        try {
//            mCamera.setPreviewDisplay(surface);
//            mCamera.startPreview();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        mVideoFormat = MediaFormat.createVideoFormat("video/avc",
//                PREVIEW_WIDTH, PREVIEW_HEIGHT);
//        mVideoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 25);// min fps
//        mVideoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
//                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
//        mVideoFormat.setInteger(MediaFormat.KEY_BIT_RATE, VIDEO_BITRATE);
//        mVideoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
//        Log.i(TAG," mVideoFormat " + mVideoFormat.toString());
//        mVideoEncoder.configure(mVideoFormat, null, null,
//                MediaCodec.CONFIGURE_FLAG_ENCODE);
//        mVideoSurface = mVideoEncoder.createInputSurface();
//    }
    
    public void prepare() {
        mVideoEncoder.start();
        mAudioExtractor.selectTrack(mAudioTrackIndex);
        mAudioDecoder.start();
        mAudioEncoder.start();
    }

    public boolean start() {
        bStop = false;
        mAudioHandler.post( new AudioDecode());
        mAudioPlayHandler.post(mAudioDecodeOutputEvent);
        mVideoHandler.post(mAudioMuxEvent);
        mVideoHandler.post(mAudioMuxEvent);
//        mVideoHandler.post(mAudioMuxEvent);
        return true;
    }
    
    public boolean stop() {
    	synchronized (this) {
    		 if (bStop)
    	            return true;

    	        bStop = true;
		}
        return true;
    }
    
//    public void setCameraFlipFlag(boolean flip) {
//    	mVideoSurfaceDrawRunnable.setCameraFlipFlag(flip);
//    }
    
    
    private long audioStartTime;
    private long videoStartTime;
    private boolean bEos = false;
    private class AudioDecode implements Runnable {
        
        ByteBuffer[] in = mAudioDecoder.getInputBuffers();
        @Override
        public void run() {
            if (bStop) {
                return;
            }
            int index = mAudioDecoder.dequeueInputBuffer(1000);
            if (index < 0) {
//            	Log.d(TAG,"audio decode no input buffer index = "+index);
                mAudioHandler.postDelayed(this,50);
                return;
            }
            int samplesize = mAudioExtractor.readSampleData(in[index], 0);
            long pts = 0;
            if (samplesize < 0) {
                Log.d(TAG, "audio decode samplesize " + samplesize + "in["+index+"] "
                        + in[index].capacity());
                bEos = true;
                samplesize = 0;
            } else {
                bEos = false;
                pts = mAudioExtractor.getSampleTime();
            }

            mAudioDecoder.queueInputBuffer(index, 0, samplesize, pts,
                    bEos ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);

            if (!bEos) {
                mAudioExtractor.advance();
            }
            // mAudioTrack.pause();
            mAudioHandler.post(this);

        }
    }

    
    Runnable mAudioDecodeOutputEvent = new Runnable() {
		
		@Override
		public void run() {
			/*
			 * when audio muxer is ready but video is preparing,we should not
			 * send audio decode data to decoder until video muxer is ready
			 * */
			if(mAudioOK&& !mVideoOK) {
				mAudioPlayHandler.post(this);
				return;
			}
			ByteBuffer[] out = mAudioDecoder.getOutputBuffers();
	        BufferInfo info = new BufferInfo();
			int index = mAudioDecoder.dequeueOutputBuffer(info, 500);
            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                if (!bStop)
            	{
                    Log.w(TAG, "audio Decode reached end of audio stream unexpectedly");
                    mAudioDecoder.releaseOutputBuffer(index, false);
//                    bStop = true;
//                    return;
                    new PlayTrack(null).run();
                    return;
                } else {
                    Log.d(TAG, "audio Decode end of stream reached");
                }
            } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                Log.w(TAG, "audio Decode  INFO_OUTPUT_BUFFERS_CHANGED");
                out = mAudioDecoder.getOutputBuffers();
            } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat format = mAudioDecoder.getOutputFormat();
                Log.w(TAG, "audio Decode  INFO_OUTPUT_FORMAT_CHANGED "+format.getString(MediaFormat.KEY_MIME));
            } else if (info.flags == MediaCodec.INFO_TRY_AGAIN_LATER) {
                Log.w(TAG, "audio Decode  INFO_TRY_AGAIN_LATER");
            } else if(index <0 ) {
//                Log.w(TAG, "audio Decode  index "+index);
            } else {
                final byte[] chunk = new byte[info.size];
                out[index].get(chunk);
                out[index].clear();
                new PlayTrack(chunk).run();
                mAudioDecoder.releaseOutputBuffer(index, false);
                return;
            }
            mAudioPlayHandler.post(this);
		}
	};
    
	public boolean isPrepared() {
		synchronized (mVideoMuxer) {
			return mAudioOK&& mVideoOK;
		}
	}
    
    private class PlayTrack  implements MyRunnable {
        private byte[] buf;
        
        public PlayTrack(byte[] b){
        	buf = b;
        }
        @Override
        public void run() {
        	if(bStop) {
        		mAudioPlayHandler.removeCallbacks(this);
        		return;
        	}
        	if(buf ==null) {
        		Log.d(TAG,"end of play.....");
        		AudioEncodeEvent mAudioEncode = new AudioEncodeEvent(System.nanoTime());
                mAudioEncode.setBuffer(null);
                mAudioEncodeHandler.post(mAudioEncode);
                return;
        	}
			if (buf.length > 0) {
				
				if(bDEBUG)
				Log.w(TAG,"audio track write begin " +Build.MODEL + " buf.length "+buf.length);
				long time = System.nanoTime()/1000;
				if (bfirst) {
					bfirst = false;
					audioStartTime = time;
					if (bDEBUG)
						Log.w(TAG, "bAudioFirst " + time);
				}
				boolean isvideoready = false;
				synchronized (mVideoMuxer) {
					isvideoready = mVideoOK;
				}
				if(isvideoready) {
					mAudioTrack.write(buf, 0, buf.length);
				}else {
					if(bDEBUG)
						Log.w(TAG,"video is not ready yet....");
					audioStartTime = time;
					 mAudioPlayHandler.post(mAudioDecodeOutputEvent);
					 return;
				}
//				 time = System.nanoTime() - time;
				if(bDEBUG)
					Log.w(TAG,"audio track write end  ");
				AudioEncodeEvent mAudioEncode = new AudioEncodeEvent(
						time);//System.nanoTime() - (buf.length/2304) *10);
				mAudioEncode.setBuffer(buf);
				mAudioEncodeHandler.post(mAudioEncode);
			}
            mAudioPlayHandler.post(mAudioDecodeOutputEvent);
        }


		@Override
		public void setBuffer(byte[] buf) {
			this.buf=buf;
		}
    };
    
    private boolean bfirst = true;
    private boolean bVideoFirst = true;
//    private long time = 0;
    private interface MyRunnable extends Runnable {
    	public void setBuffer(byte[] buf);
	}
    
    
    private class AudioEncodeEvent implements MyRunnable {
    	
    	private byte[] mBuffer;
    	private long pts;
    	public AudioEncodeEvent(long audio_pts) {
    		pts = audio_pts;
    	}
    	
    	@Override
    	public void run(){
    		if(bStop) {
    			mAudioEncodeHandler.removeCallbacks(this);
        		return;
        	}
    		ByteBuffer[] in = mAudioEncoder.getInputBuffers();
        int index = mAudioEncoder.dequeueInputBuffer(-1);
        if(index>=0) {
//        	Log.w(TAG,"mAudioEncode dequeueInputBuffer-------------- " +index+ " mBuffer length "+mBuffer.length);
        	ByteBuffer inputBuffer = in[index];
        	inputBuffer.clear();

        	long presentationTimeUs = pts;//(System.nanoTime() - audioStartTime) / 1000;
        	if(bDEBUG)
        		Log.w(TAG,"audio encoder  pts = "+presentationTimeUs);
        	if(mBuffer != null) {
					inputBuffer.put(mBuffer);
					mAudioEncoder.queueInputBuffer(index, 0, mBuffer.length, presentationTimeUs,  0);
        	}else {
        		 mAudioEncoder.queueInputBuffer(index, 0, 0, presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
        	}
           
        } else {
        	Log.w(TAG,"mAudioEncode error " +index);
        }
    	}

		@Override
		public void setBuffer(byte[] buf) {
			mBuffer = buf;
		}
    };
    
    private long lastEncodedAudioTimeStamp = 0;
    private MyRunnable mAudioMuxEvent = new MyRunnable() {
    	@Override
    	public void run(){
    		handleAudioMux();
    	}

		@Override
		public void setBuffer(byte[] buf) {
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
    int index = mAudioEncoder.dequeueOutputBuffer(info, 100);
    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//        if (!bStop) {
//            Log.w(TAG, "reached end of audio stream unexpectedly");
//            mAudioEncoder.releaseOutputBuffer(index, false);
//            bStop = true;
////        	if(bEos) 
//            return;
//        } else {
            Log.d(TAG, "end of stream reached");
//        }
				synchronized (this) {
					if (!bStop) {
						bStop = true;
						mContext.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								((CameraPreviewActivity) mContext)
										.toggleCapture(true);
							}
						});
					}
				}
    		return;
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
            	if (mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
    				mAudioTrack.play();
    			}
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
					Log.w(TAG, "video muxer is not start ");
					mAudioEncoder.releaseOutputBuffer(index, false);
					mVideoHandler.postDelayed(mAudioMuxEvent, 10);
					return;
				}
				
				out[index].position(info.offset);
				out[index].limit(info.offset + info.size);
				                             if (info.presentationTimeUs < lastEncodedAudioTimeStamp)
					                                     info.presentationTimeUs = lastEncodedAudioTimeStamp += magicLag;
				                              
//					                         if(lastEncodedAudioTimeStamp > info.presentationTimeUs) {
//					                                }
					                               lastEncodedAudioTimeStamp = info.presentationTimeUs;
//				
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
                    Log.w(TAG, "video encoder output format changed: " + newFormat);
//                    videoStartTime = System.nanoTime()/1000;
                    // now that we have the Magic Goodies, start the muxer
                    mTrackIndex = mVideoMuxer.addTrack(newFormat);
                    
                    synchronized (mVideoMuxer) {
                    	if (mAudioOK) {
                        	mVideoMuxer.start();
                        	if (mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                				mAudioTrack.play();
                			}
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
			if (!isPrepared() ||audioStartTime == 0) {
				Log.w(TAG, "audio is not ready........................ "); 
				mVideoEncoder.releaseOutputBuffer(encoderStatus, false);
				return;
			}
			long tmppts = System.nanoTime()/1000;
			if (bVideoFirst) {
				bVideoFirst = false;
				videoStartTime = tmppts;//pts;// -  Math.max(pts, audioStartTime);
				Log.w(TAG, "bVideoFirst   videoStartTime "+videoStartTime +" mVideoDelayTime "+mVideoDelayTime); 
			}
			
			tmppts =  tmppts -(videoStartTime -audioStartTime) -mVideoDelayTime;// -videoStartTime;// audioStartTime / 1000;// - mVideoDelayTime;
			if (bDEBUG)
				Log.e("guolei", "video sent " + mBufferInfo.size
						+ " bytes to muxer video  pts " + tmppts);


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
				if (!isPrepared()) {
					Log.w(TAG, "video muxer hasn't started");
					mVideoEncoder.releaseOutputBuffer(encoderStatus, false);
					// mVideoHandler.post(mDrainEncoder);
					return;
				}

				// adjust the ByteBuffer values to match BufferInfo
				encodedData.position(mBufferInfo.offset);
				encodedData.limit(mBufferInfo.offset + mBufferInfo.size);
				mBufferInfo.presentationTimeUs = tmppts;
				mVideoMuxer.writeSampleData(mTrackIndex, encodedData,
						mBufferInfo);
				// Log.e("guolei", "video sent " + mBufferInfo.size
				// +
				// " bytes to muxer pts "+mBufferInfo.presentationTimeUs+" audioStartTime "+audioStartTime
				// + " videoStartTime "+videoStartTime);
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
    
       public void setFixedLyricView(FixedLyricView view) {
    	   if(mVideoSurfaceDrawRunnable== null) return;
    	   mVideoSurfaceDrawRunnable.setLyricView(view);
       }
        	
    private boolean mAudioOK = false;
    private boolean mVideoOK = false;
    private int mTrackIndex = -1;
    private long mVideoDelayTime = 0;
    
//    private class VideoSurfaceDrawEvent implements Runnable {
//        private TextureView mTexture;
//        private Drawable fg;
////        private Drawable prefg;
////        private boolean bFlip;
//        private FixedLyricView mLyricView;
//        private Paint mPaint;
//        private float scale = 0.5f;
////        private boolean bpre;
////        private Matrix preMatrix;
//        private  float scaledDensity;
//        public VideoSurfaceDrawEvent() {
////        	textpaint.setTextSize(18);
////        	textpaint.setTypeface(Typeface.DEFAULT_BOLD);
////        	textpaint.setColor(Color.GREEN);
//        	scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
//        }
////        public void setCameraFlipFlag(boolean flip) {
//////        	bFlip = flip;
//////        	if(bFlip) {
//////        	preMatrix = new Matrix();
//////			preMatrix.postScale(-1, 1);
//////        	} else {
//////        		preMatrix = null;
//////        	}
////		}
//		@Override
//        public void run() {
//        	if(bStop || mLyricView == null || mTexture == null)
//        		return;
////        	Bitmap buf = null;
//        	long lag = System.nanoTime();
////        	try{
////        		buf = outputBitmap;//mTexture.getBitmap(PREVIEW_WIDTH, PREVIEW_HEIGHT);
////        		if(buf == null) return;
////        } catch (IllegalStateException e) {
////    		mMainHandler.removeCallbacks(this);
////    		mMainHandler.post(this);
////    		return;
////    	}
////        	 Log.d(TAG, "VideoSurfaceDrawEvent draw");
//        	int version = Build.VERSION.SDK_INT;
//        	Canvas canvas;
//        	if(version>=Build.VERSION_CODES.M && Build.MODEL.equals("MI 5")) {
//        		Log.d(TAG, "lockHardwareCanvas in");
//        		canvas  = mVideoSurface.lockHardwareCanvas();
//        		Log.d(TAG, "lockHardwareCanvas out");
//        		if(canvas == null) return;
//        	}else
//        		canvas  = mVideoSurface.lockCanvas(null);
//            try {
//                Matrix mBitmapMatrix = mTexture.getTransform(null);
////                if(bFlip)
////                	mBitmapMatrix.postScale(-1, 1);
//                synchronized (MediaEngine.this) {
//                	Bitmap buf = outputBitmap;
//                	  if(mPaint==null) {
//                      	mPaint = new Paint(mLyricView.getPaint());
//                      	mPaint.setTextSize(mLyricView.getTextSize()/scaledDensity);
//                      	scale= (float)PreviewManager.EXPECTED_PREVIEW_H/(float)buf.getHeight();
//                      }
//                	if(mBitmapMatrix !=null) {
//                		mBitmapMatrix.postScale(scale, scale);
//                		buf = Bitmap.createBitmap(buf, 0, 0, outputBitmap.getWidth(), outputBitmap.getHeight(), mBitmapMatrix, true);
////                		buf = Bitmap.createScaledBitmap(buf, PREVIEW_WIDTH, PREVIEW_HEIGHT, true);
//                	}
//                	int h = buf.getHeight();
//                	canvas.drawBitmap(buf,0,(PreviewManager.EXPECTED_PREVIEW_H-h)/2, null);
////                	buf.recycle();
//                	Bitmap b= null;
////                	if(prefg !=null) {
////                		b = ((BitmapDrawable)prefg.getCurrent()).getBitmap();
////                		if(b!=null&& !b.isRecycled())
////                    		canvas.drawBitmap(b, 0,0, null);
////                	}
//                	if(fg!=null) {
//                	b = ((BitmapDrawable)fg.getCurrent()).getBitmap();
////                	if(!bpre && preMatrix != null) {
////                		b = Bitmap.createBitmap(b, 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, preMatrix, true);
////                	}
//                }
//                	if(b!=null&& !b.isRecycled()){
//                			canvas.drawBitmap(b, 0,0, null);
//                	}
//                String text = mLyricView.getText().toString();
//                canvas.drawText(text, canvas.getWidth()/2-mPaint.measureText(text)/2,	 canvas.getHeight()-40, mPaint);
//               }
//            } finally {
//                mVideoSurface.unlockCanvasAndPost(canvas);
//            }
////            mVideoDelayTime = (System.nanoTime()-lag)/1000;
////            Log.w(TAG,"mVideoDelayTime "+mVideoDelayTime);
//        }
//
//		public void setTexture(TextureView texture) {
//			if(mTexture == null)
//				mTexture = texture;
//		}
//		
//		public void setSurfaceForeground(Drawable fg) {
//			synchronized (this) {
//				this.fg=fg;
////				this.prefg = prefg;
////				this.bpre = bpre;
//			}
//		}
//    }

    private VideoSurfaceDrawEvent mVideoSurfaceDrawRunnable;

    @Override
    public void onFrameAvailable(SurfaceTexture arg0) {
    }

public boolean isStart() {
	return !bStop;
}

    public void startOrStop() {
        if (bStop)
            start();
        else
            stop();
    }

    public void releaseVideoSurfaceThread() {
    	Log.w(TAG, "release");
    	mVideoSurfaceDraw.removeCallbacks(mVideoSurfaceDrawRunnable);
    	mVideoSurfaceDraw.getLooper().quit();
        try {
        	mVideoSurfaceDraw.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(outputBitmap != null && !outputBitmap.isRecycled())
            outputBitmap.recycle();
        	outputBitmap = null;
        	if(yuvAllocation != null)
        		yuvAllocation.destroy();
        	yuvAllocation = null;
        	if(rgbaAllocation != null)
        		rgbaAllocation.destroy();
        	rgbaAllocation = null;
            if(renderScript != null) {
            renderScript.destroy();
            renderScript = null;
            }
    }
        public void release() {
        	mAudioOK = false;
        	mVideoOK  = false;
        	mTrackIndex = -1;
        	bfirst = true;
            bVideoFirst = true;
            lastEncodedAudioTimeStamp = 0;
            audioStartTime = 0;
           videoStartTime = 0;
             bEos = false;
             
    	if(mVideoHandler != null) {
        mVideoHandler.getLooper().quit();
        try {
            mVideoHandler.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	}
        Log.d(TAG, "release video thread exit");
        if(mAudioHandler != null) {
        mAudioHandler.getLooper().quit();
        try {
        	mAudioHandler.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
        Log.d(TAG, "release audio decoder thread exit");
        if(mAudioPlayHandler != null) {
        mAudioPlayHandler.getLooper().quit();
        try {
        	mAudioPlayHandler.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
        Log.d(TAG, "release audio encoder thread exit");
        if(mAudioEncodeHandler != null) {
        mAudioEncodeHandler.getLooper().quit();
        try {
        	mAudioEncodeHandler.getLooper().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            mAudioOK = false;
            mVideoOK = false;
            mVideoMuxer.release();
            mVideoMuxer = null;
        }
        
        if(mAudioTrack!=null) {
        	mAudioTrack.stop();
        	mAudioTrack = null;
        }
        	
        if(mAudioEncoder!=null) {
        	mAudioEncoder.stop();
        	mAudioEncoder.release();
        	mAudioEncoder = null;
        }
        if(mAudioDecoder!=null) {
        	mAudioDecoder.stop();
        	mAudioDecoder.release();
        	mAudioDecoder = null;
        }
  
    }
    
	public void setForegroundDrawable(Drawable fg) {
		mVideoSurfaceDrawRunnable.setSurfaceForeground(fg);
	}

//	public void setForegroundDrawable(Drawable fg) {
//		mVideoSurfaceDrawRunnable.setSurfaceForeground(fg);
//	}
	
	public void setPreviewSize(Size previewSize) {
//		int ratio = 1;
//		if(previewSize.height == 720)
//			ratio =2;
//		PREVIEW_WIDTH=previewSize.width/ratio;
//		PREVIEW_HEIGHT=previewSize.height/ratio;
	}

	@Override
	public void OnFrameUpdate(TextureView texture) {
//		synchronized (this) {
//			if(bStop)
//				return;
//		}
//		mVideoSurfaceDraw.removeCallbacks(mVideoSurfaceDrawRunnable);
//		 mVideoSurfaceDraw.post(mVideoSurfaceDrawRunnable);
//		 mVideoHandler.post(new VideoMux((System.nanoTime())/1000));
	}
	
	
	
	private RenderScript renderScript;
	private ScriptIntrinsicYuvToRGB yuvToRgb;
	private Allocation yuvAllocation;
	private Allocation rgbaAllocation;
	private Type.Builder rgbaType;
	private Camera mCamera;
	private Bitmap outputBitmap;

	public void setCamera(Camera c) {
		if(mCamera == c && c == null) {
			Log.w(TAG,"setCamera dont set ...........");
			return;
		}
		if (c != null ) {
			Log.w(TAG,"setCamera mPreviewCallback");
			c.setPreviewCallback(mPreviewCallback);
		} else {
			if(mCamera!=null)
				mCamera.setPreviewCallback(null);
		}
		mCamera = c;
//		mVideoHandler.post(new VideoMux((System.nanoTime())/1000));
	}
	
	private PreviewCallback mPreviewCallback = new PreviewCallback() {
		private long lasttime = 0L;
		private int count = 0;
		private long total = 0L;
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			synchronized (MediaEngine.this) {
				long now = System.nanoTime()/1000;
				count++;
					if(lasttime!= 0) {
						total +=(now  -lasttime);
						mVideoDelayTime = total/count;
					}
					lasttime = now;
				if(mCamera == null)
					return;
				if(bDEBUG) Log.w(TAG,"onPreviewFrame "+ mVideoDelayTime);
				if(mVideoSurfaceDrawRunnable instanceof AvatorVideoSurfaceDrawnEvent && bStop)  {
						convertYuvImageToBitmap(mContext, data, PreviewManager.PREVIEW_W, PreviewManager.PREVIEW_H);
						mVideoSurfaceDraw.post(mVideoSurfaceDrawRunnable);
						return;
				}
				if(bStop) {
					return;
				}
			mVideoSurfaceDraw.removeCallbacks(mVideoSurfaceDrawRunnable);
//			mVideoSurfaceDrawRunnable.setTexture(texture);
			mVideoSurfaceDrawRunnable.setCurrentPTS(System.nanoTime()/1000);
			convertYuvImageToBitmap(mContext, data, PreviewManager.PREVIEW_W, PreviewManager.PREVIEW_H);
			mVideoSurfaceDraw.post(mVideoSurfaceDrawRunnable);
			}
		}
	};
	
	public void sendVideoMux(long pts) {
		mVideoHandler.post(new VideoMux(pts));
	}
	
	private Bitmap convertYuvImageToBitmap(Context context, byte[] data,int w,int h) {
	    if (renderScript == null) {
	        // once
	    	renderScript = RenderScript.create(context);
	        yuvToRgb = ScriptIntrinsicYuvToRGB.create(renderScript, Element.U8(renderScript));
	    }

	    if (yuvAllocation == null || yuvAllocation.getBytesSize() < data.length) {
	    	Type.Builder yuvType = new Type.Builder(renderScript, Element.U8(renderScript)).setX(data.length);
//	    	yuvType.setYuvFormat(mCamera.getParameters().getPreviewFormat());
	        yuvAllocation = Allocation.createTyped(renderScript, yuvType.create(), Allocation.USAGE_SCRIPT);
	        Log.w(TAG, "allocate in " + yuvAllocation.getBytesSize() + " " + w + "x" + h);
	    }

	    if (rgbaAllocation == null || 
	            rgbaAllocation.getBytesSize() < rgbaAllocation.getElement().getBytesSize()*w*h) {
	        rgbaType = new Type.Builder(renderScript, Element.RGBA_8888(renderScript)).setX(w).setY(h);
	        rgbaAllocation = Allocation.createTyped(renderScript, rgbaType.create(), Allocation.USAGE_SCRIPT);
	        Log.w(TAG, "allocate out " + rgbaAllocation.getBytesSize() + " " + w + "x" + h);
	    }
	    yuvAllocation.copyFrom(data);

	    yuvToRgb.setInput(yuvAllocation);
	    yuvToRgb.forEach(rgbaAllocation);
//	    synchronized (outputBitmap) {
	    if (outputBitmap == null) {
	    	outputBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	    }
	    rgbaAllocation.copyTo(outputBitmap);
//	    }
	    return outputBitmap;
	}

	public void setPreviewTexture(IPreviewTexture cameraView) {
		if(mVideoSurfaceDrawRunnable != null)
			mVideoSurfaceDrawRunnable.setIPreviewTexture(cameraView);
	}

	public Bitmap getPreviewBitmap() {
		return outputBitmap;
	}
}
