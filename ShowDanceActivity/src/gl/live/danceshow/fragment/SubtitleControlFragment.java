package gl.live.danceshow.fragment;

import gl.live.danceshow.media.SubtitleAdapter;
import gl.live.danceshow.ui.widget.FixedLyricView;
import gl.live.danceshow.ui.widget.VerticalSeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.logic.DownloadMediaService;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.FrameEvent;
import com.android.app.showdance.logic.event.SubtitleEvent;
import com.android.app.showdance.model.DownloadFrameInfo;
import com.android.app.showdance.model.glmodel.FontInfo;
import com.android.app.showdance.model.glmodel.FontInfo.FontData;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import de.greenrobot.event.EventBus;

public class SubtitleControlFragment extends DialogFragment implements OnClickListener, OnItemClickListener{
	private static final String TAG = "SubtitleControlFragment";
	private FixedLyricView mFixedLyricView;
	private SubtitleAdapter cameraFgAdapter;
	private List<SubtitleItem> subtitleItems = new ArrayList<SubtitleItem>();
	private List<SubtitleItem> downloadsubtitleItems;
	public SubtitleControlFragment(FixedLyricView lyricView) {
		mFixedLyricView = lyricView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
public void onEventMainThread(SubtitleEvent event) {
		int musicId = event.subtitleid;
		if(musicId==-1)
			return;
		SubtitleItem item = null;
		for(SubtitleItem info :subtitleItems) {
			if(info.getData().getId()==musicId) {
				item = info;
				break;
			}
		}
		if(item == null)
			return;
		// 下载进度
			int state = event.state;
			item.setbDownload(state);			
//			Log.d("guolei","item "+item.getData().toString());
			Log.d(TAG,"item state "+item.getDownload());
			switch (state) {
			case ContentValue.DOWNLOAD_STATE_DOWNLOADING:
				item.setPercent(event.current*100/event.total);
				break;
			case ContentValue.DOWNLOAD_STATE_SUCCESS:
//				new UnzipTask().execute(intent.getStringExtra("downloadpath"),item.getName());
				item.setPath(event.path);
				break;
			case ContentValue.DOWNLOAD_STATE_FAIL:
//				showPlayInfoDialog();
				Toast.makeText(getContext(), "下载失败！", Toast.LENGTH_SHORT)
				.show();
				break;
			default:
				return;
			}
			cameraFgAdapter.notifyDataSetChanged();

	}
	
	@Override
	public void onResume() {
		super.onResume();
		int w = getContext().getResources().getDimensionPixelSize(R.dimen.subtitle_fg_width);
		int h = getContext().getResources().getDimensionPixelSize(R.dimen.subtitle_fg_height);
		getDialog().getWindow().setLayout(w,h);
		EventBus.getDefault().registerSticky(this);
		new ScanSubtitleTask().execute();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}
	
private void getFontList() {
	JsonObjectRequest request = new JsonObjectRequest(Method.GET, VolleyManager.SERVER_URL+VolleyManager.API+VolleyManager.FONT_LIST, null,
			new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					String json = response.toString();
//					Log.w("guolei", "onResponse " + json);
					ObjectMapper objectMapper = new ObjectMapper();
					FontInfo.Response result = null;
					try {
						result = objectMapper.readValue(
								json,
								FontInfo.Response.class);
						subtitleItems.clear();
						Editor sp = InitApplication.mSpUtil.getMusicSp().edit();
						for(FontInfo.FontData item:result.getData()) {
							Log.w("guolei","item "+item.getname());
							SubtitleItem found = null;
								for (SubtitleItem subItem : downloadsubtitleItems) {
									if (subItem.getName()
											.contains(item.getname())) {
										found = subItem;
										subItem.setImg(item.getimg());
										break;
									}
								}
								sp.putString(item.getname()+".ttf", item.getimg());
								if(found == null) {
									found = new SubtitleItem();
								} else {
									found.setbDownload(ContentValue.DOWNLOAD_STATE_SUCCESS);
								}
								found.setData(item);
								subtitleItems.add(found);
						}
						sp.commit();
						if(cameraFgAdapter!= null) {
							cameraFgAdapter.setSubTitleList(subtitleItems);
							cameraFgAdapter.notifyDataSetChanged();
						} else {
								cameraFgAdapter = new SubtitleAdapter(getContext(), subtitleItems);
								((GridView)getView().findViewById(R.id.subtitle_grid)).setAdapter(cameraFgAdapter);
						}
					} catch (JsonParseException e1) {
						e1.printStackTrace();
					} catch (JsonMappingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
				}
								
				}
			},new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
				}
			});
	VolleyManager.getInstance().getRequestQueue().add(request);
}	
	

private class ScanSubtitleTask extends AsyncTask<Void, Void, List<SubtitleItem>> {
	private ProgressDialog pd;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(getContext());
		pd.setCancelable(false);
		pd.setMessage("正在扫描已下载字幕...");
		pd.show();
	}

	@Override
	protected List<SubtitleItem> doInBackground(Void... params) {

		return getsubtitlePathFromSD();
	}

	@Override
	protected void onProgressUpdate(Void...params ) {
	}

	/** 遍历所有文件夹，查找出视频文件 */
	// 从sd卡获取mp3资源
	public List<SubtitleItem> getsubtitlePathFromSD() {
		// mp4列表
		List<SubtitleItem> subtitleItems = new ArrayList<SubtitleItem>();
		// 得到该路径文件夹下所有的文件
		File mfile = new File(InitApplication.SdCardSubTitlePath);
		File[] files = mfile.listFiles();
		// 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
		if (files == null) {
			return null;
		}
		SharedPreferences sp = InitApplication.mSpUtil.getMusicSp();
			for (File file :files) {
				if(file.isDirectory()|| file.getName().equalsIgnoreCase(".nomedia")) {
					continue;
				}
				Log.d(TAG,"file = "+file.getAbsolutePath());
				
				SubtitleItem subtitle = new SubtitleItem();
				subtitle.setPath(file.getAbsolutePath());
				subtitle.setName(file.getName());
				subtitle.setImg(sp.getString(file.getName(), null));
				subtitle.setbDownload(ContentValue.DOWNLOAD_STATE_SUCCESS);
				subtitleItems.add(subtitle);
			}
			getFontList();
		return subtitleItems;
	}

	@Override
	protected void onPostExecute(List<SubtitleItem> result) {
		super.onPostExecute(result);

		downloadsubtitleItems = result;

		if (downloadsubtitleItems != null && downloadsubtitleItems.size() != 0) {
			if(cameraFgAdapter==null){
			cameraFgAdapter = new SubtitleAdapter(getContext(), downloadsubtitleItems);
			GridView grid = ((GridView)getView().findViewById(R.id.subtitle_grid));
			grid.setAdapter(cameraFgAdapter);
			grid.setOnItemLongClickListener(cameraFgAdapter);
			}else {
				cameraFgAdapter.setSubTitleList(result);
				cameraFgAdapter.notifyDataSetChanged();
			}
		} 
		pd.dismiss();
	}
}

	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Log.d(TAG, "onProgressChanged " + progress);
			int color = Color.MAGENTA;
			switch (progress) {
			case 1:
				color = Color.RED;
				break;
			case 2:
				color = Color.GREEN;
				break;
			case 7:
				color = Color.MAGENTA;
				break;
			case 4:
				color = Color.WHITE;
				break;
			case 5:
				color = Color.BLUE;
				break;
			case 6:
				color = Color.YELLOW;
				break;
			case 3:
				color = Color.CYAN;
				break;
			case 0:
				color = Color.BLACK;
				break;
			default:
				break;
			}
			switch (seekBar.getId()) {
			case R.id.lrc_color:
				mFixedLyricView.setTextColor(color);
				break;
			case R.id.lrc_textsize:
				mFixedLyricView.setTextSize(18 + progress);
				break;
			default:
				break;
			}

		}
	};
	
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE	, R.style.Mdialog);
	}
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		initViewAndAdapter(v);
		Button b = (Button)v.findViewById(R.id.subtitle_setok);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SubtitleControlFragment.this.dismiss();
			}
		});
	}
	
	protected void initViewAndAdapter(View v) {
		VerticalSeekBar seekBar = (VerticalSeekBar) v.findViewById(R.id.lrc_color);
		seekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
		seekBar = (VerticalSeekBar) v.findViewById(R.id.lrc_textsize);
		seekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
		GridView grid = ((GridView)getView().findViewById(R.id.subtitle_grid));
		grid.setOnItemClickListener(SubtitleControlFragment.this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.subtitle_controller, container, true);
		return v;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	default:
		break;
	}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SubtitleItem data = cameraFgAdapter.getCurrentList().get(arg2);
		Log.d(TAG, "onItemClick getDownload " + data.getDownload());
		if(data.getDownload() == ContentValue.DOWNLOAD_STATE_SUCCESS) {
			Typeface typeface = Typeface.createFromFile(data.getPath());
			mFixedLyricView.setTypeface(typeface);
		} else
			downloadFileSelect(data);
	}
	
	protected void downloadFileSelect(SubtitleItem item) {
		
		int code = item.getDownload();
		Log.d(TAG, "downloadFileSelect code " + code);
		switch (code) {
		case ContentValue.DOWNLOAD_STATE_NONE:
			item.setbDownload(ContentValue.DOWNLOAD_STATE_WATTING);
			Intent i = new Intent(getContext(), DownloadMediaService.class);
			i.putExtra(ContentValue.SERVICE_TYPE_NAME,
					ContentValue.START_DOWNLOAD_SUBTITLE);
			i.putExtra(ContentValue.DOWNLOAD_TAG_BY_INTENT, item);
			i.putExtra("des", InitApplication.SdCardSubTitlePath);
			getContext().startService(i);
			break;
		default:
			break;
		}
		}
}