package com.android.app.showdance.logic;

import gl.live.danceshow.fragment.SubtitleItem;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.android.app.showdance.impl.AgentConstant;
import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.event.FrameEvent;
import com.android.app.showdance.logic.event.MusicEvent;
import com.android.app.showdance.logic.event.SubtitleEvent;
import com.android.app.showdance.model.DownloadFile;
import com.android.app.showdance.model.DownloadFrameInfo;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.wumeiniang.app.InitApplication;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: DownloadMusicService
 * @Description: 下载MP3服务
 * @author maminghua
 * @date 2015-6-2 上午11:46:33
 * 
 */
public class DownloadMediaService extends Service implements ContentValue, AgentConstant {

	private List<Object> pendingitems = new ArrayList<Object>();
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			// 将下载池更新
			handler.postDelayed(this, 500);
		}

	};

	@Override
	public void onDestroy() {
		// 服务结束的时候结束定时器
		handler.removeCallbacks(runnable);
		Log.d("guolei","服务停止");
		// db.close();
		super.onDestroy();
	}

	/**
	 * (非 Javadoc) Title: onCreate Description:
	 * 
	 * @see com.cloud.coupon.service.BaseService#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// fb = FinalDb.create(getBaseContext(), "coupon.db");
		// String dbpath =
		// getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
		// db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		Log.d("guolei","服务开启成功");
	}

	/**
	 * (非 Javadoc) Title: onStart Description:
	 * 
	 * @param intent
	 * @param startId
	 * @see com.cloud.coupon.service.BaseService#onStart(android.content.Intent,
	 *      int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("guolei","服务开始");
		if (intent == null) {
			return;
		}
			int code = intent.getIntExtra(SERVICE_TYPE_NAME, ERROR_CODE);
			musicToPath = InitApplication.SdCardMusicPath;
			lrcToPath = InitApplication.SdCardLrcPath;

			switch (code) {
			case START_DOWNLOAD_MOVIE:
				// 开启下载电影
				DownloadMusicInfo startdmi = (DownloadMusicInfo) intent.getSerializableExtra(DOWNLOAD_TAG_BY_INTENT);
				// startdmi.setFilePath(toPath);
				synchronized (pendingitems) {
					pendingitems.add(startdmi);// 讲接到的任务存放到下载池中
				}
				startDownload(musicToPath);
				// 开启一个定时器,每隔1秒钟刷新一次下载进度
				startTimerUpdateProgress();
				break;
			case START_DOWNLOAD_FRAME:
				DownloadFrameInfo frameinfo = (DownloadFrameInfo) intent.getSerializableExtra(DOWNLOAD_TAG_BY_INTENT);
				// startdmi.setFilePath(toPath);
				synchronized (pendingitems) {
					pendingitems.add(frameinfo);// 讲接到的任务存放到下载池中
				}
				startDownload(intent.getStringExtra("des"));
				// 开启一个定时器,每隔1秒钟刷新一次下载进度
				startTimerUpdateProgress();
				break;
			case START_DOWNLOAD_SUBTITLE:
				SubtitleItem subItem = (SubtitleItem) intent.getSerializableExtra(DOWNLOAD_TAG_BY_INTENT);
				// startdmi.setFilePath(toPath);
				synchronized (pendingitems) {
					pendingitems.add(subItem);// 讲接到的任务存放到下载池中
				}
				startDownload(intent.getStringExtra("des"));
				// 开启一个定时器,每隔1秒钟刷新一次下载进度
				startTimerUpdateProgress();
				break;
			case DOWNLOAD_STATE_SUSPEND:
				// 暂停一个下载任务
				// DownloadMusicInfo stopdmi =
				// m.getStopOrStartDownloadMovieItem();
				// if (stopdmi != null)
				// stopDownload(stopdmi, false);
				break;
			case DOWNLOAD_STATE_START:
				// 开始一个正在暂停的下载任务
				// DownloadMusicInfo startDmiByPausing =
				// m.getStopOrStartDownloadMovieItem();
				// if (startDmiByPausing != null)
				// startPausingDownload(startDmiByPausing, toPath);
				break;
			case DOWNLOAD_STATE_DELETE:
				// DownloadMusicInfo delDownload =
				// m.getStopOrStartDownloadMovieItem();
				// deleteDownload(delDownload, toPath, false);
				break;
			case DOWNLOAD_STATE_CLEAR:
				// clearAllDownload();
				break;
			case START_DOWNLOAD_LOADITEM:
				// 数据库中装载下载任务
					// 尝试从数据库中得到items
					// items = fb.findAll(clazz);
					// if (items != null && items.size() != 0)
					// for (int i = 0; i < items.size(); i++) {
					// // 保证不管应用以什么方式终止 重新启动的时候 所有状态都为 暂停
					// if (items.get(i).getDownloadState() !=
					// DOWNLOAD_STATE_SUCCESS) {
					// // 如果当前不是下载完成的状态
					// items.get(i).setDownloadState(DOWNLOAD_STATE_SUSPEND);
					// }
					// }
					// m.setDownloadItems(items);
					// 如果是退出之后
				break;
			case START_DOWNLOAD_ALLSUSPEND:
				// 设置所有下载状态为暂停
				setAllDownloadTaskSuspend();
				break;
			default:
				break;
			}
	}

	/**
	 * @Title: setAllDownloadTaskSuspend
	 * @Description:
	 * @param
	 * @return void
	 * @throws
	 */
	private void setAllDownloadTaskSuspend() {
	}

	/**
	 * @Title: startPausingDownload
	 * @Description: 开始一个 当前是暂停状态的任务
	 * @param dmi
	 *            接收一个 下载任务
	 * @return void
	 * @throws
	 */
	private void startPausingDownload(DownloadMusicInfo dmi, String toPath) {

	}

	/**
	 * @Title: startTimerUpdateProgress
	 * @Description: 更新下载进度
	 * @param
	 * @return void
	 * @throws
	 */
	private void startTimerUpdateProgress() {
		handler.postDelayed(runnable, 500);
	}

	private Map<Long, Object> currentDownloadItems = new HashMap<Long, Object>();
	/**
	 * @Title: startDownloadMovie
	 * @Description: 开启下载电影的任务
	 * @param Intent
	 * @param List
	 *            <DownloadMusicInfo> items 存放要下载的电影
	 * @return void
	 * @throws
	 */
	private String musicToPath;
	private String lrcToPath;
	// private FinalDb fb;

	// private SQLiteDatabase db;
	private class UnzipTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			
			String path = params[0];
			String outdir = params[1];
			String sdcardpath = params[2];
			Log.d("guolei","path "+path + " outdir "+outdir + " sdcardpath "+sdcardpath);
			if(path==null|| outdir == null) {
				Log.e("guolei","download error");
				return false;
			}
			try {
				FileUtil.unzip(path, sdcardpath+File.separator+outdir);
				new File(path).delete();
			} catch (IOException e) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "“+path+” 解压失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			return true;
		}
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result)
			Toast.makeText(getApplicationContext(), "下载成功！",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void handlePendingFrame(DownloadFrameInfo curitem, final String toPath) {
		Log.d("guolei", "handlePendingFrame ");
		if (curitem.getDownloadState() == DOWNLOAD_STATE_WATTING || curitem.getDownloadState() == DOWNLOAD_STATE_EXCLOUDDOWNLOAD) {
				final DownloadFrameInfo dmi = curitem;
				String framurl = VolleyManager.SERVER_URL.concat(dmi.getFrame().geturl());

				// 构建存储文件路径
				String extension = FileUtil.getUrlExtension(dmi.getFrame().geturl());
				String filename = dmi.getName().concat("."+extension);
				File framezip = new File(toPath, filename);// mp3保存路径及文件名字

				dmi.setFilePath(framezip.getAbsolutePath());
				dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
				if (dmi.getUuid() == null) {
					Long id = Long.parseLong("0");
					id = UUID.randomUUID().getLeastSignificantBits();
					dmi.setUuid(id);
				}
				if (dmi.getUuid() != null) {
					synchronized (currentDownloadItems) {
						currentDownloadItems.put(dmi.getUuid(), dmi);// 讲下载任务存入当前的队列中
					}
				}
				DownloadFile downframe = download(framurl, dmi.getFilePath(), new RequestCallBack<File>() {

					/**
					 * (非 Javadoc) Title: onStart Description:
					 * 
					 * @see net.tsz.afinal.http.AjaxCallBack#onStart()
					 */
					@Override
					public void onStart() {
						// 如果这个任务在等待,开启这个下载任务
						// 讲下载状态设置为 正在下载
						// 开启任务
						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
						Log.d("guolei", "--开始下载");
						// 为下载任务分配ID号

					}

					/**
					 * (非 Javadoc) Title: onLoading Description:
					 * 
					 * @param count
					 * @param current
					 * @see net.tsz.afinal.http.AjaxCallBack#onLoading(long,
					 *      long)
					 */

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						// 设置当前的进度
						dmi.setProgressCount(total);
						dmi.setCurrentProgress(current);
						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);

						Intent intent = new Intent(dmi.getDownloadAction());
						intent.putExtra("musicId", dmi.getFrame().getId());
						intent.putExtra("total", dmi.getProgressCount());
						intent.putExtra("current", dmi.getCurrentProgress());
						intent.putExtra("state", DOWNLOAD_STATE_DOWNLOADING);
						sendBroadcast(intent);
						
						EventBus.getDefault().postSticky(
								new FrameEvent(dmi.getFrame().getId(), dmi
										.getProgressCount(), dmi
										.getCurrentProgress(), 
										DOWNLOAD_STATE_DOWNLOADING));

					}

					/**
					 * (非 Javadoc) Title: onFailure Description:
					 * 
					 * @param t
					 * @param strMsg
					 * @see net.tsz.afinal.http.AjaxCallBack#onFailure(java.lang.Throwable,
					 *      java.lang.String)
					 */
					// }

					@Override
					public void onFailure(HttpException t, String arg1) {

						dmi.setDownloadState(DOWNLOAD_STATE_FAIL);
						 Log.e("guolei","下载失败:" + arg1 + ":" +
						 dmi.getDownloadUrl() + "：\n原因：" + t.getMessage()
						 + "异常信息:" + t.getLocalizedMessage());
//						 Toast.makeText(getApplicationContext(),
//						 dmi.getName() + "：下载失败",
//						 Toast.LENGTH_SHORT).show();
						 t.printStackTrace();
						 synchronized (currentDownloadItems) {
						currentDownloadItems.remove(dmi.getUuid());
						 }
						SystemClock.sleep(1000);
						startDownload(toPath);

						
						EventBus.getDefault().postSticky(
								new FrameEvent(dmi.getFrame().getId(), dmi
										.getProgressCount(), dmi
										.getCurrentProgress(), 
										DOWNLOAD_STATE_SUCCESS));
						if("maybe the file has downloaded completely".equalsIgnoreCase(arg1)) {
							EventBus.getDefault().postSticky(
									new FrameEvent(dmi.getFrame().getId(), dmi
											.getProgressCount(), dmi
											.getCurrentProgress(), 
											DOWNLOAD_STATE_SUCCESS));
							new UnzipTask().execute(dmi.getFilePath(),dmi.getName(),toPath);
						}else {
							EventBus.getDefault().postSticky(
									new FrameEvent(dmi.getFrame().getId(), dmi
											.getProgressCount(), dmi
											.getCurrentProgress(), 
											DOWNLOAD_STATE_FAIL));
							Toast.makeText(getApplicationContext(), dmi.getName() + ".zip  下载失败!", Toast.LENGTH_SHORT).show();
						}
						// 更新数据库状态
						// fb.update(dmi, dmi.getUuid()+"");

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						dmi.setDownloadState(DOWNLOAD_STATE_SUCCESS);
						if(FileUtil.getUrlExtension(dmi.getFilePath()).equalsIgnoreCase("zip")) {
							new UnzipTask().execute(dmi.getFilePath(),dmi.getName(),toPath);
						}
						
						// System.out.println("下载成功：" +
						// arg0.getAbsolutePath());
						synchronized (currentDownloadItems) {
						currentDownloadItems.remove(dmi.getUuid());// 成功的时候从当前下载队列中去除
						}
						// 将下载的状态插入到数据库中
						EventBus.getDefault().postSticky(
								new FrameEvent(dmi.getFrame().getId(), dmi
										.getProgressCount(), dmi
										.getCurrentProgress(), 
										DOWNLOAD_STATE_SUCCESS));

						startDownload(toPath);// 继续寻找可以下载的任务
						
					}
				});
				dmi.setDownloadFile(downframe);
			}
	
	}
	
	
	public void startDownload(final String toPath) {
		synchronized (currentDownloadItems) {
		if (currentDownloadItems.size() >= 3) {
			return;
		}
		}
		synchronized (pendingitems) {
		Iterator<Object> i = pendingitems.iterator();
		while (i.hasNext()) {
			Object item = i.next();
			if(item instanceof DownloadMusicInfo) {
				handlePendingMusic((DownloadMusicInfo)item, toPath);
			} else if(item instanceof DownloadFrameInfo){
				handlePendingFrame((DownloadFrameInfo)item,toPath);
			} else if (item instanceof SubtitleItem) {
				handlePendingSubtitle((SubtitleItem)item,toPath);
			} else {
				continue;
			}
			i.remove();
		}
		}
	}

	private void handlePendingMusic(DownloadMusicInfo curitem, final String toPath) {
		if (curitem.getDownloadState() == DOWNLOAD_STATE_WATTING || curitem.getDownloadState() == DOWNLOAD_STATE_EXCLOUDDOWNLOAD) {
			// 如果状态为等待状态
			// 如果当前正在下载的任务 不够三个
				// 取得下载任务
				final DownloadMusicInfo dmi = curitem;
				String lrcUrl = VolleyManager.SERVER_URL.concat(dmi.getMusic().getLrc());
				
				
				// 构建存储文件路径
				String tmpurl = dmi.getMusic().getUrl();
				final String mp3url =dmi.getMovieName();
				File fmp3 = new File(musicToPath, mp3url+".tmp");// mp3保存路径及文件名字
				
				tmpurl = dmi.getMusic().getLrc();
				final String lrcurl = tmpurl.substring(tmpurl.lastIndexOf("/")+1);
				File flrc = new File(lrcToPath, lrcurl);// 歌词保存路径及文件名字
				SharedPreferences.Editor editor = InitApplication.mSpUtil.getMusicSp().edit();
				editor.putString(mp3url, lrcurl+"_"+dmi.getFileName()+"_"+dmi.getMusic().getSinger()).commit();
//				Log.d("download","mp3url "+mp3url+" lrcurl " +lrcurl+ " sp "+InitApplication.mSpUtil.getMusicSp().getString(mp3url, "null"));
//				dmi.setFilePath(fmp3.getAbsolutePath());
				dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
				if (dmi.getUuid() == null) {
					Long id = Long.parseLong("0");
					id = UUID.randomUUID().getLeastSignificantBits();
					dmi.setUuid(id);
				}
				synchronized (currentDownloadItems) {
				if (dmi.getUuid() != null) {
					currentDownloadItems.put(dmi.getUuid(), dmi);// 讲下载任务存入当前的队列中
				}
				}
				
				DownloadFile downMp3 = download(VolleyManager.SERVER_URL.concat(dmi.getMusic().getUrl()), fmp3.getAbsolutePath(), new RequestCallBack<File>() {

					/**
					 * (非 Javadoc) Title: onStart Description:
					 * 
					 * @see net.tsz.afinal.http.AjaxCallBack#onStart()
					 */
					@Override
					public void onStart() {
						// 如果这个任务在等待,开启这个下载任务
						// 讲下载状态设置为 正在下载
						// 开启任务
						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
						Log.d("guolei", "--开始下载");
						// 为下载任务分配ID号

					}

					/**
					 * (非 Javadoc) Title: onLoading Description:
					 * 
					 * @param count
					 * @param current
					 * @see net.tsz.afinal.http.AjaxCallBack#onLoading(long,
					 *      long)
					 */

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						// 设置当前的进度
						dmi.setProgressCount(total);
						dmi.setCurrentProgress(current);
						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);

//						Intent intent = new Intent(dmi.getDownloadAction());
//						intent.putExtra("musicId", dmi.getMusic().getId());
//						intent.putExtra("total", dmi.getProgressCount());
//						intent.putExtra("current", dmi.getCurrentProgress());
//						intent.putExtra("percentage", dmi.getPercentage());
//						intent.putExtra("state", DOWNLOAD_STATE_DOWNLOADING);
//						sendBroadcast(intent);
							EventBus.getDefault().postSticky(
									new MusicEvent(dmi.getMusic().getId(), dmi
											.getProgressCount(), dmi
											.getCurrentProgress(), dmi
											.getPercentage(),
											DOWNLOAD_STATE_DOWNLOADING));

					}

					/**
					 * (非 Javadoc) Title: onFailure Description:
					 * 
					 * @param t
					 * @param strMsg
					 * @see net.tsz.afinal.http.AjaxCallBack#onFailure(java.lang.Throwable,
					 *      java.lang.String)
					 */
					// }

					@Override
					public void onFailure(HttpException t, String arg1) {

						dmi.setDownloadState(DOWNLOAD_STATE_FAIL);
						 Log.e("guolei","下载失败:" + arg1 + ":" +
						 dmi.getDownloadUrl() + "：\n原因：" + t.getMessage()
						 + "异常信息:" + t.getLocalizedMessage());
//						 Toast.makeText(getApplicationContext(),
//						 dmi.getName() + "：下载失败",
//						 Toast.LENGTH_SHORT).show();
						 t.printStackTrace();
						 synchronized (currentDownloadItems) {
						currentDownloadItems.remove(dmi.getUuid());
						 }
						SystemClock.sleep(1000);
						startDownload(toPath);
						// 更新数据库状态
						// fb.update(dmi, dmi.getUuid()+"");
						
						
						EventBus.getDefault().postSticky(
								new MusicEvent(dmi.getMusic().getId(), dmi
										.getProgressCount(), dmi
										.getCurrentProgress(), dmi
										.getPercentage(),
										DOWNLOAD_STATE_FAIL));
						
						Toast.makeText(getApplicationContext(), dmi.getName() + ".mp3  下载失败!", Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						dmi.setDownloadState(DOWNLOAD_STATE_SUCCESS);
						String path = arg0.result.getAbsolutePath();
						 Log.d("guolei","下载成功：" +path
						 );
						 arg0.result.renameTo(new File(path.substring(0, path.lastIndexOf("."))));
						 synchronized (currentDownloadItems) {
						currentDownloadItems.remove(dmi.getUuid());// 成功的时候从当前下载队列中去除
						 }
						// 将下载的状态插入到数据库中
						 EventBus.getDefault().postSticky(
									new MusicEvent(dmi.getMusic().getId(), dmi
											.getProgressCount(), dmi
											.getCurrentProgress(), dmi
											.getPercentage(),
											DOWNLOAD_STATE_SUCCESS));

						startDownload(toPath);// 继续寻找可以下载的任务

					}
				});
				dmi.setDownloadFile(downMp3);

				DownloadFile downAss = download(lrcUrl, flrc.getAbsolutePath(), new RequestCallBack<File>() {

					/**
					 * (非 Javadoc) Title: onStart Description:
					 * 
					 * @see net.tsz.afinal.http.AjaxCallBack#onStart()
					 */
					@Override
					public void onStart() {
						// 如果这个任务在等待,开启这个下载任务
						// 讲下载状态设置为 正在下载
						// 开启任务
						// dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);

					}

					/**
					 * (非 Javadoc) Title: onLoading Description:
					 * 
					 * @param count
					 * @param current
					 * @see net.tsz.afinal.http.AjaxCallBack#onLoading(long,
					 *      long)
					 */

					@Override
					public void onLoading(long total, long current, boolean isUploading) {

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

					}
				});
				dmi.setDownloadFile(downAss);
			}
	}
	
	/**
	 * @Title: setDbValues
	 * @Description:
	 * @param
	 * @return void
	 * @throws
	 */
	private ContentValues setDbValues(DownloadMusicInfo dmi) {
		ContentValues values = new ContentValues();
		values.put("id", dmi.getMusic().getId());
		values.put("editState", dmi.isEditState());
//		values.put("movieName", dmi.getMovieName());
		values.put("fileSize", dmi.getFileSize());
		values.put("currentProgress", dmi.getCurrentProgress());
		values.put("isSelected", dmi.isSelected());
		values.put("percentage", dmi.getPercentage());
		values.put("filePath", dmi.getFilePath());
		values.put("downloadUrl", dmi.getDownloadUrl());
		values.put("uuid", dmi.getUuid());
		values.put("progressCount", dmi.getProgressCount());
		values.put("downloadState", dmi.getDownloadState());
		values.put("setCount", dmi.getMusic().getId());
		values.put("movieId", dmi.getMovieId());
		return values;

	}

	/**
	 * @Title: download
	 * @Description: 开启一个普通的下载任务
	 * @param
	 * @return void
	 * @throws
	 */
	private DownloadFile download(String url, String toPath, RequestCallBack<File> downCallBack) {
		return new DownloadFile().startDownloadFileByUrl(url, toPath, downCallBack);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void handlePendingSubtitle(SubtitleItem curitem, final String toPath) {
		Log.d("guolei", "handlePendingSubtitle ");
		if (curitem.getDownload() == DOWNLOAD_STATE_WATTING || curitem.getDownload()== DOWNLOAD_STATE_EXCLOUDDOWNLOAD) {
				final SubtitleItem dmi = curitem;
				String framurl = VolleyManager.SERVER_URL.concat(dmi.getData().getpath());

				// 构建存储文件路径
				String extension = FileUtil.getUrlExtension(dmi.getData().getpath());
				String filename = dmi.getName().concat("."+extension);
				File framezip = new File(toPath, filename);

				dmi.setPath(framezip.getAbsolutePath());
				dmi.setbDownload(DOWNLOAD_STATE_DOWNLOADING);
				if (dmi.getUuid() == null) {
					Long id = Long.parseLong("0");
					id = UUID.randomUUID().getLeastSignificantBits();
					dmi.setUuid(id);
				}
				if (dmi.getUuid() != null) {
					synchronized (currentDownloadItems) {
						currentDownloadItems.put(dmi.getUuid(), dmi);// 讲下载任务存入当前的队列中
					}
				}
				DownloadFile downframe = download(framurl, dmi.getPath(), new RequestCallBack<File>() {

					/**
					 * (非 Javadoc) Title: onStart Description:
					 * 
					 * @see net.tsz.afinal.http.AjaxCallBack#onStart()
					 */
					@Override
					public void onStart() {
						// 如果这个任务在等待,开启这个下载任务
						// 讲下载状态设置为 正在下载
						// 开启任务
//						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
						Log.d("guolei", "--开始下载");
						// 为下载任务分配ID号

					}

					/**
					 * (非 Javadoc) Title: onLoading Description:
					 * 
					 * @param count
					 * @param current
					 * @see net.tsz.afinal.http.AjaxCallBack#onLoading(long,
					 *      long)
					 */

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						// 设置当前的进度
//						dmi.setProgressCount(total);
//						dmi.setCurrentProgress(current);
//						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);

//						Intent intent = new Intent(dmi.getDownloadAction());
//						intent.putExtra("subtitleid", dmi.getData().getId());
//						intent.putExtra("total", dmi.getProgressCount());
//						intent.putExtra("current", dmi.getCurrentProgress());
//						intent.putExtra("state", DOWNLOAD_STATE_DOWNLOADING);
//						sendBroadcast(intent);
						
						EventBus.getDefault().postSticky(
								new SubtitleEvent(dmi.getData().getId(),
//										dmi.getProgressCount(),
//										dmi.getCurrentProgress(), 
										DOWNLOAD_STATE_DOWNLOADING));

					}

					/**
					 * (非 Javadoc) Title: onFailure Description:
					 * 
					 * @param t
					 * @param strMsg
					 * @see net.tsz.afinal.http.AjaxCallBack#onFailure(java.lang.Throwable,
					 *      java.lang.String)
					 */
					// }

					@Override
					public void onFailure(HttpException t, String arg1) {

						dmi.setbDownload(DOWNLOAD_STATE_FAIL);
						 Log.e("guolei","下载失败:" + arg1 + ":" +
						 dmi.getName() + "：\n原因：" + t.getMessage()
						 + "异常信息:" + t.getLocalizedMessage());
//						 Toast.makeText(getApplicationContext(),
//						 dmi.getName() + "：下载失败",
//						 Toast.LENGTH_SHORT).show();
						 t.printStackTrace();
						 synchronized (currentDownloadItems) {
						currentDownloadItems.remove(dmi.getUuid());
						 }
						SystemClock.sleep(1000);
						startDownload(toPath);

						
						EventBus.getDefault().postSticky(
								new SubtitleEvent(dmi.getData().getId(),
										DOWNLOAD_STATE_SUCCESS));
						if("maybe the file has downloaded completely".equalsIgnoreCase(arg1)) {
							EventBus.getDefault().postSticky(
									new SubtitleEvent(dmi.getData().getId(),dmi.getPath(),
											DOWNLOAD_STATE_SUCCESS));
//							new UnzipTask().execute(dmi.getPath(),dmi.getName(),toPath);
						}else {
							EventBus.getDefault().postSticky(
									new SubtitleEvent(dmi.getData().getId(), 
											DOWNLOAD_STATE_FAIL));
							Toast.makeText(getApplicationContext(), dmi.getName() + ".ttf  下载失败!", Toast.LENGTH_SHORT).show();
						}
						// 更新数据库状态
						// fb.update(dmi, dmi.getUuid()+"");

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						dmi.setbDownload(DOWNLOAD_STATE_SUCCESS);
//						if(FileUtil.getUrlExtension(dmi.getPath()).equalsIgnoreCase("zip")) {
//							new UnzipTask().execute(dmi.getPath(),dmi.getName(),toPath);
//						}
						
						// System.out.println("下载成功：" +
						// arg0.getAbsolutePath());
						synchronized (currentDownloadItems) {
						currentDownloadItems.remove(dmi.getUuid());// 成功的时候从当前下载队列中去除
						}
						// 将下载的状态插入到数据库中
						EventBus.getDefault().postSticky(
								new SubtitleEvent(dmi.getData().getId(),dmi.getPath(),
										DOWNLOAD_STATE_SUCCESS));

						startDownload(toPath);// 继续寻找可以下载的任务
						
					}
				});
//				dmi.setDownloadFile(downframe);
			}
	
	}
}
