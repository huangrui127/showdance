package gl.live.danceshow.ui.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import com.android.app.wumeiniang.app.InitApplication;
import com.aws.mp3.Mp3ReadId3v1;
import com.aws.mp3.Mp3ReadId3v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import gl.live.danceshow.ui.camera.CameraPreviewActivity.UpdateMusicPlayCallback;
import gl.live.danceshow.ui.utils.Audio;
import gl.live.danceshow.ui.widget.FixedLyricView;
import gl.live.danceshow.ui.widget.Lyric;
import gl.live.danceshow.ui.widget.LyricView;

public class LyricManager implements UpdateMusicPlayCallback {
	private static final String TAG = "LyricManager";
	private Object mLock = new Object();
	private Lyric mLyric;
	private FixedLyricView lyricView;
	private Handler mLyricHandler;
	private boolean isplaying = false;
	private long mCurrentPostion = 0;

	public LyricManager(Context context, FixedLyricView view) {
		mLyricHandler = new Handler(context.getMainLooper());
		lyricView = view;
	}

	Runnable mUpdateResults = new Runnable() {
		public void run() {
			if (lyricView.updateIndex(mCurrentPostion))
				lyricView.invalidate();
			synchronized (mLock) {
				if (isplaying)
					mLyricHandler.postDelayed(this, 50);
			}
		}
	};

	public void prepare(String path) {
		new Thread(new startLyricRunnable(path)).start();
	}
	
	public void startUpdateLyric() {
		synchronized (mLock) {
			if(isplaying)
				return;
			isplaying = true;
			mLyricHandler.post(mUpdateResults);
		}
	}

	public void removeLyric() {
		
	}
	
	public void stopUpdateLyric() {
		synchronized (mLock) {
			if(!isplaying)
				return;
			isplaying = false;
		}
	}

	private class startLyricRunnable implements Runnable {
		private String audiopath;

		public startLyricRunnable(String path) {
			audiopath = path;
		}

		@Override
		public void run() {
			Audio info = null;
			File f = null;
			{

				String artistFromeRetriever = null;
				String titleFromeRetriever = null;
				String albumFromRetriever = null;

				InputStream is = null;
				try {
					is = new FileInputStream(audiopath);
					Mp3ReadId3v2 reader = new Mp3ReadId3v2(is);
					reader.readId3v2();
					artistFromeRetriever = reader.getAuthor();
					titleFromeRetriever = reader.getName();
					albumFromRetriever = reader.getSpecial();

				} catch (Exception e) {
					try {
						Mp3ReadId3v1 readerv1 = new Mp3ReadId3v1(
								new FileInputStream(audiopath));
						readerv1.readId3v1();
						artistFromeRetriever = readerv1.getAuthor();
						titleFromeRetriever = readerv1.getName();
						albumFromRetriever = readerv1.getSpecial();
					} catch (Exception e1) {
						Log.e(TAG, "e1= " + e1.toString());
						return;
					}
				}

				Log.d(TAG, "titleFromeRetriever " + titleFromeRetriever
						+ " audiopath "+audiopath);
				SharedPreferences sp = InitApplication.mSpUtil.getMusicSp();
				String filename = audiopath.substring((audiopath.lastIndexOf("/"))+1);
				String lrcname = sp.getString(filename, null);
				if(lrcname == null) {
					lrcname = filename.substring(0, filename.lastIndexOf("."))+"lrc";
					info = new Audio(lrcname, audiopath, 0L, true);
				}else
					info = new Audio(lrcname.substring(0, lrcname.indexOf("_")), audiopath, 0L, true);
				info.setTitle(titleFromeRetriever);
				info.setAlbum(albumFromRetriever);
				info.setArtist(artistFromeRetriever);
				info.setTrack(titleFromeRetriever);
				info.setPath(audiopath);
//				f = new File(audiopath.replace(type, ".lrc"));
			}
			WeakReference<Audio> weakRf_audio = new WeakReference<Audio>(info);
//			WeakReference<File> weakRf_file = new WeakReference<File>(f);
			Lyric lyric;
//			if (f.exists()) {
//				Log.d(TAG, "lyric file tyle is " + Lyric.getFilecharset(f));
//				lyric = new Lyric(weakRf_file.get(), weakRf_audio.get());
//			} else 
			{
				lyric = new Lyric(weakRf_audio.get());
			}
			WeakReference<Lyric> weakRf_lyric = new WeakReference<Lyric>(lyric);
			lyricView.setmLyric(weakRf_lyric.get());
			lyricView.setSentencelist(weakRf_lyric.get().list);
//			lyricView.setNotCurrentPaintColor(Color.GREEN);
//			lyricView.setCurrentPaintColor(Color.GREEN);
			lyricView.setCurrentTextSize(18);
			lyricView.setLrcTextSize(18);
//			lyricView.setTexttypeface(Typeface.SERIF);
//			lyricView.setBrackgroundcolor(Color.TRANSPARENT);
//			lyricView.setTextHeight(50);
			info = null;
			f = null;
			lyric = null;
			System.gc();
		}
	}

	@Override
	public void updatePostion(long time) {
		mCurrentPostion = time;
	}
}