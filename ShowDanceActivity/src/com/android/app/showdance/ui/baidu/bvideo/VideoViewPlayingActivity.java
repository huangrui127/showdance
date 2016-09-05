package com.android.app.showdance.ui.baidu.bvideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;

@SuppressLint("NewApi")
public class VideoViewPlayingActivity extends Activity implements OnPreparedListener, OnCompletionListener, OnErrorListener, OnInfoListener,OnBufferingUpdateListener {

	public static VideoViewPlayingActivity instance = null;

	private final String TAG = "VideoViewPlayingActivity";

	/**
	 * 您的ak
	 */
	 private String AK = "VeDAh0XdUtfBqUYSRgPQzcZP";
	/**
	 * 您的sk的前16位
	 */
	 private String SK = "r7qCWF9XY5yGyG9E";

	private String mVideoSource = null;

	private ImageButton mPlaybtn = null;

	private LinearLayout mController = null;

	private SeekBar mProgress = null;
	private TextView mDuration = null;
	private TextView mCurrPostion = null;

	/**
	 * 记录播放位置
	 */
	private int mLastPos = 0;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	private VideoView mVV = null;

	private EventHandler mEventHandler;
//	private HandlerThread mHandlerThread;

	private final Object SYNC_Playing = new Object();

	private static final String POWER_LOCK = "VideoViewPlayingActivity";


	private final int EVENT_PLAY = 0;
	private final int UI_EVENT_UPDATE_CURRPOSITION = 1;

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				/**
				 * 设置播放url
				 */
				mVV.setVideoPath(mVideoSource);

				mLastPos = mProgress.getProgress();

				/**
				 * 续播，如果需要如此
				 */
				if (mLastPos > 0) {

					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}

				/**
				 * 显示或者隐藏缓冲提示
				 */
//				mVV.showCacheInfo(true);

				/**
				 * 开始播放
				 */
				mVV.start();

				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				
				// 隐藏
				mDismissHandler.removeMessages(0);
				mDismissHandler.sendEmptyMessageDelayed(0, 500);
				
				break;
			default:
				break;
			}
		}
	}
	
	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateControlBar(false);
		}
	};

	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 更新进度及时间
			 */
			case UI_EVENT_UPDATE_CURRPOSITION:
				if(!mVV.isPlaying()) {
					mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
					return;
				}
				int currPosition = mVV.getCurrentPosition();
				int duration = mVV.getDuration();
				updateTextViewWithTimeFormat(mCurrPostion, currPosition);
				updateTextViewWithTimeFormat(mDuration, duration);
				mProgress.setMax(duration);
				mProgress.setProgress(currPosition);

				mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
				
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN	, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.controllerplaying);

		instance = this;

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);


		Uri uriPath = getIntent().getData();
		if (null != uriPath) {
			String scheme = uriPath.getScheme();
			if (null != scheme) {
				mVideoSource = uriPath.toString();
			} else {
				mVideoSource = uriPath.getPath();
			}
		}

		initUI();

		/**
		 * 开启后台事件处理线程
		 */
//		mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
//		mHandlerThread.start();
		mEventHandler = new EventHandler(getMainLooper());

	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		mPlaybtn = (ImageButton) findViewById(R.id.play_btn);
		mController = (LinearLayout) findViewById(R.id.controlbar);

		mProgress = (SeekBar) findViewById(R.id.media_progress);
		mDuration = (TextView) findViewById(R.id.time_total);
		mCurrPostion = (TextView) findViewById(R.id.time_current);

		int duration = getIntent().getIntExtra("duration", 0);
		int currPosition = getIntent().getIntExtra("currPosition", 0);

		updateTextViewWithTimeFormat(mCurrPostion, currPosition);
		updateTextViewWithTimeFormat(mDuration, duration);
		mProgress.setMax(duration);
		mProgress.setProgress(currPosition);

		registerCallbackForControl();

		/**
		 * 设置ak及sk的前16位
		 */
//		BVideoView.setAKSK(AK, SK);

		/**
		 * 获取BVideoView对象
		 */
		mVV = (VideoView) findViewById(R.id.video_view);
		/**
		 * 注册listener
		 */
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);

		mPlaybtn.setImageResource(R.drawable.player_btn_pause_style);

		/**
		 * 设置解码模式
		 */
//		mVV.setDecodeMode(mIsHwDecode ? BVideoView.DECODE_HW : BVideoView.DECODE_SW);
	}

	/**
	 * 为控件注册回调处理函数
	 */
	private void registerCallbackForControl() {
		mPlaybtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (mVV.isPlaying()) {
					mPlaybtn.setImageResource(R.drawable.player_btn_play_style);
					/**
					 * 暂停播放
					 */
					mVV.pause();
				} else {
					mPlaybtn.setImageResource(R.drawable.player_btn_pause_style);
					/**
					 * 继续播放
					 */
					mVV.start();
				}

			}
		});

		/**
		 * 实现切换示例
		 */
//		mPrebtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				/**
//				 * 如果已经播放了，等待上一次播放结束
//				 */
//				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
//					mVV.stopPlayback();
//				}
//
//				/**
//				 * 发起一次新的播放任务
//				 */
//				if (mEventHandler.hasMessages(EVENT_PLAY))
//					mEventHandler.removeMessages(EVENT_PLAY);
//				mEventHandler.sendEmptyMessage(EVENT_PLAY);
//			}
//		});
//
//		mForwardbtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//
//			}
//		});

		OnSeekBarChangeListener osbc1 = new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// Log.v(TAG, "progress: " + progress);
				updateTextViewWithTimeFormat(mCurrPostion, progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				/**
				 * SeekBar开始seek时停止更新
				 */
				mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				int iseekPos = seekBar.getProgress();
				/**
				 * SeekBark完成seek时执行seekTo操作并更新界面
				 * 
				 */
				mVV.seekTo(iseekPos);
				Log.v(TAG, "seek to " + iseekPos);
				mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
			}
		};
		mProgress.setOnSeekBarChangeListener(osbc1);
	}

	private void updateTextViewWithTimeFormat(TextView view, int sec) {
		int second = sec/1000;
		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;
		String strTemp = null;
		if (0 != hh) {
			strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
		} else {
			strTemp = String.format("%02d:%02d", mm, ss);
		}
		view.setText(strTemp);
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		/**
		 * 发起一次播放任务,当然您不一定要在这发起
		 */
		mEventHandler.sendEmptyMessage(EVENT_PLAY);
	}

	private long mTouchTime;
	private boolean barShow = true;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN)
			mTouchTime = System.currentTimeMillis();
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			long time = System.currentTimeMillis() - mTouchTime;
			if (time < 400) {
				updateControlBar(!barShow);
			}
		}

		return true;
	}

	public void updateControlBar(boolean show) {

		if (show) {
			mController.setVisibility(View.VISIBLE);
		} else {
			mController.setVisibility(View.INVISIBLE);
		}
		barShow = show;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * 退出后台事件处理线程
		 */
//		mHandlerThread.quit();
	}

//	@Override
//	public boolean onInfo(int what, int extra) {
//		switch (what) {
//		/**
//		 * 开始缓冲
//		 */
//		case BVideoView.MEDIA_INFO_BUFFERING_START:
//			break;
//		/**
//		 * 结束缓冲
//		 */
//		case BVideoView.MEDIA_INFO_BUFFERING_END:
//			break;
//		default:
//			break;
//		}
//		return true;
//	}

	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
//	@Override
//	public void onPlayingBufferCache(int percent) {
//
//	}

	/**
	 * 播放出错
	 */
//	@Override
//	public boolean onError(int what, int extra) {
//		Log.v(TAG, "onError");
//		synchronized (SYNC_Playing) {
//			SYNC_Playing.notify();
//		}
//		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
//		return true;
//	}

	/**
	 * 播放完成
	 */
//	@Override
//	public void onCompletion() {
//		Log.v(TAG, "onCompletion");
//		synchronized (SYNC_Playing) {
//			SYNC_Playing.notify();
//		}
//		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
//		finish();
//	}

	/**
	 * 准备播放就绪
	 */
//	@Override
//	public void onPrepared() {
//		Log.v(TAG, "onPrepared");
//		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
//		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
//	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		/**
		 * 开始缓冲
		 */
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			break;
		/**
		 * 结束缓冲
		 */
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.v(TAG, "onError");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.v(TAG, "onCompletion");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		finish();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);		
	}

}
