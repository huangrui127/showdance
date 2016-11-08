package com.android.app.showdance.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.showdance.widget.MyProgressBar;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import gl.live.danceshow.media.PreviewManager;
import gl.live.danceshow.media.StaticMediaEngine;

/**
 * 
 * @ClassName: ShowDanceActivity
 * @Description: 秀舞界面
 * @author maminghua
 * @date 2015-5-6 下午02:48:16
 * 
 */

public class PreSummeryEditorActivity extends BaseActivity {
	private static final String TAG = "PreSummeryEditorActivity";

//	private String videoFilePath;// 录好后的原始视频路径
	private String Title;
	private File tmpJpgFile = new File(InitApplication.sdCardForegroundPath+"/tmp.jpg");
	private StaticMediaEngine mMediaEngine;
	private LinearLayout mEditor;
	private Handler mHandler;
	
	public static List<AnimDrawableItem> sBitmapList = new ArrayList<AnimDrawableItem>();
//	private final int NotificationID = 0x10000;
//	private NotificationManager mNotificationManager = null;
//	private NotificationCompat.Builder builder;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presummeryedit);
		mHandler = new Handler();
		findViewById();
		Title = getIntent().getStringExtra("musicname");
//		videoFilePath = getIntent().getStringExtra("musicFile");
//		if(videoFilePath == null) {
//			videoFilePath =  getIntent().getData().getPath();
//		}
//		if(videoFilePath == null)  {
//			Log.e("guolei","get video file path fail, finish.");
//			finish();
//		}
//		if(!tmpFile.exists()) {
//		try {
//			tmpFile.createNewFile();
//		} catch (IOException e) {
//			finish();
//			return;
//		}
//		}
		initView();
		setOnClickListener();
		
	}
	
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		mEditor = (LinearLayout)findViewById(R.id.editor_layout);
		setTextColor(Color.MAGENTA);
		SeekBar seekBar = (SeekBar) findViewById(R.id.video_text_color);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Log.d(TAG,"onProgressChanged "+progress);
				int color = Color.MAGENTA;
				switch (progress) {
				case 1:
					color = Color.RED;
					break;
				case 2:
					color = Color.GREEN;
					break;
				case 3:
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
				case 7:
					color = Color.CYAN;
					break;
				case 0:
					color = Color.BLACK;
					break;
				default:
					break;
				}
				setTextColor(color);
			}
		});
	}

	private void setTextColor(int color) {
		EditText editor = (EditText)findViewById(R.id.video_name);
		editor.setTextColor(color);
		editor.setHintTextColor(color);
		editor = (EditText)findViewById(R.id.singer_editor);
		editor.setTextColor(color);
		editor.setHintTextColor(color);
		editor = (EditText)findViewById(R.id.dancer_editor);
		editor.setTextColor(color);
		editor.setHintTextColor(color);
		editor = (EditText)findViewById(R.id.compose_editor);
		editor.setTextColor(color);
		editor.setHintTextColor(color);
		editor = (EditText)findViewById(R.id.golden_words_editor);
		editor.setTextColor(color);
		editor.setHintTextColor(color);
		
		TextView text = (TextView)findViewById(R.id.singer);
		text.setTextColor(color);
		text = (TextView)findViewById(R.id.dancer);
		text.setTextColor(color);
		text = (TextView)findViewById(R.id.compose);
		text.setTextColor(color);
		text = (TextView)findViewById(R.id.golden_words);
		text.setTextColor(color);
		
	}
	
//	private Bitmap drawableToBitamp(Drawable drawable)
//	    {
//	        int w = drawable.getIntrinsicWidth();
//	        int h = drawable.getIntrinsicHeight();
//	        System.out.println("Drawable转Bitmap");
//	        Bitmap.Config config = 
//	                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//	                        : Bitmap.Config.RGB_565;
//	        Bitmap  bitmap = Bitmap.createBitmap(w,h,config);
//	        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
//	        Canvas canvas = new Canvas(bitmap);   
//	        drawable.setBounds(0, 0, w, h);   
//	        drawable.draw(canvas);
//	        return bitmap;
//	    }
	
	@Override
	protected void initView() {
		tvTitle.setText("片头设置");
		tvTitle.setVisibility(View.VISIBLE);
		return_imgbtn.setVisibility(View.VISIBLE);
		TextView v = (TextView) findViewById(R.id.login_status);
		v.setText("去除背景");
		v.setVisibility(View.VISIBLE);
		v.setOnClickListener(this);
		inflater = LayoutInflater.from(this);
		
		EditText videoName = (EditText)findViewById(R.id.video_name);
		
		
		Intent i = getIntent();
		
		if(i==null)
			return;
		
		String content = null;
		if(!TextUtils.isEmpty(content=i.getStringExtra("video_name"))) {
			videoName.setText(content);
		} else {
				String title  = Title.split("_")[0];
				if(Title.split("_").length>=1)
					videoName.setText(Title.split("_")[0]);
				else
					videoName.setText(title);
		}
		videoName.setSelection(videoName.getText().toString().length());
		
		if((content=i.getStringExtra("compose"))!=null) {
			videoName = (EditText)findViewById(R.id.compose_editor);
			videoName.setText(content);
			videoName.setSelection(content.length());
		}
		
		if((content=i.getStringExtra("singer"))!=null) {
			videoName = (EditText)findViewById(R.id.singer_editor);
			videoName.setText(content);
			videoName.setSelection(content.length());
		}
		
		if((content=i.getStringExtra("dancer"))!=null) {
			videoName = (EditText)findViewById(R.id.dancer_editor);
			videoName.setText(content);
			videoName.setSelection(content.length());
		}
		
		if((content=i.getStringExtra("golden_words"))!=null) {
			videoName = (EditText)findViewById(R.id.golden_words_editor);
			videoName.setText(content);
			videoName.setSelection(content.length());
		}
	}
	
	private void saveEditInfo(Intent i) {
		if(i==null)
			return;
		String content = null;
		EditText videoName = (EditText)findViewById(R.id.video_name);
		if(!TextUtils.isEmpty(content=videoName.getText().toString())) {
			i.putExtra("video_name", content);
		}
		
		 videoName = (EditText)findViewById(R.id.compose_editor);
		if(!TextUtils.isEmpty(content=videoName.getText().toString())) {
			i.putExtra("compose", content);
		}
		
		videoName = (EditText)findViewById(R.id.singer_editor);
		if(!TextUtils.isEmpty(content=videoName.getText().toString())) {
			i.putExtra("singer", content);
		}
		
		videoName = (EditText)findViewById(R.id.dancer_editor);
		if(!TextUtils.isEmpty(content=videoName.getText().toString())) {
			i.putExtra("dancer", content);
		}
		
		videoName = (EditText)findViewById(R.id.golden_words_editor);
		if(!TextUtils.isEmpty(content=videoName.getText().toString())) {
			i.putExtra("golden_words", content);
		}
	}
	
	public interface OnFrameUpdateListener {
    	void OnFrameUpdate();
    }
	
	private class UpdateFrameRunnable implements Runnable {

		@Override
		public void run() {
			mMediaEngine.OnFrameUpdate();
		}
		
	}
	
	private void startRecordStaticImage() {
		mMediaEngine.start();
		mHandler.post(mFrameRunnable);
	}
	
	
	 private UpdateFrameRunnable mFrameRunnable;
	 
	private void initMediaEngine() {
		mMediaEngine = new StaticMediaEngine(this);
		mFrameRunnable = new UpdateFrameRunnable();
		try {
			mMediaEngine.initStaticAudio();
			mMediaEngine.initVideo(tmpJpgFile.getAbsolutePath(), "video/avc");
			mMediaEngine.setStaticPreviewDisplay();
			mMediaEngine.prepare();
//			mMediaEngine.start();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("guolei",""+e.getMessage());
		}
	}
	
	
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		Button videoButton = (Button) findViewById(R.id.btn_video_edit);
		videoButton.setOnClickListener(this);
		ImageButton pickerbutton = (ImageButton) findViewById(R.id.btn_picker);
		pickerbutton.setOnClickListener(this);
		int size = sBitmapList.size();
//		if(size>=1) {
//			if(sBitmapList.get(0).isChanged())
//				mEditor.setBackground(new BitmapDrawable(getResources(), sBitmapList.get(0).getBitmap()));
//		}
		
		ImageView v = (ImageView)findViewById(R.id.imag2);
		if(size>=2) {
			v.setTag(new Object());
			v.setBackground(createScaledBitmapDrawable(sBitmapList.get(1).getBitmap()));
		}
		v.setOnClickListener(this);
		v = (ImageView)findViewById(R.id.imag3);
		if(size>=3) {
			v.setTag(new Object());
			v.setBackground(createScaledBitmapDrawable(sBitmapList.get(2).getBitmap()));
		}
		v.setOnClickListener(this);
		v = (ImageView)findViewById(R.id.imag4);
		if(size>=4) {
			v.setTag(new Object());
			v.setBackground(createScaledBitmapDrawable(sBitmapList.get(3).getBitmap()));
		}
		v.setOnClickListener(this);
		v = (ImageView)findViewById(R.id.imag5);
		if(size>=5) {
			v.setTag(new Object());
			v.setBackground(createScaledBitmapDrawable(sBitmapList.get(4).getBitmap()));
		}
		v.setOnClickListener(this);
	}

	private BitmapDrawable createScaledBitmapDrawable(Bitmap b) {
		if(b.getWidth() == PreviewManager.EXPECTED_PREVIEW_W
				&&b.getHeight() == PreviewManager.EXPECTED_PREVIEW_H
				) {
			return new BitmapDrawable(getResources(), b);
		}
		Bitmap a = Bitmap.createScaledBitmap(b, PreviewManager.EXPECTED_PREVIEW_W, PreviewManager.EXPECTED_PREVIEW_H, false);
		BitmapDrawable d = new BitmapDrawable(getResources(), a); 
		return d;
	}
	
	private View curent;
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * Activity恢复时执行 当Activity可以得到用户焦点的时候就会调用onResume方法
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void setEditorViewVisibility() {
		EditText editor = (EditText)findViewById(R.id.video_name);
		editor.setCursorVisible(false);
		editor = (EditText)findViewById(R.id.singer_editor);
		if(TextUtils.isEmpty(editor.getText().toString())) {
			((View)editor.getParent()).setVisibility(View.GONE);
		}
		editor.setCursorVisible(false);
		
		editor = (EditText)findViewById(R.id.singer_editor);
		if(TextUtils.isEmpty(editor.getText().toString())) {
			((View)editor.getParent()).setVisibility(View.GONE);
		}
		editor.setCursorVisible(false);
		
		editor = (EditText)findViewById(R.id.dancer_editor);
		if(TextUtils.isEmpty(editor.getText().toString())) {
			((View)editor.getParent()).setVisibility(View.GONE);
		}
		editor.setCursorVisible(false);
		
		editor = (EditText)findViewById(R.id.compose_editor);
		if(TextUtils.isEmpty(editor.getText().toString())) {
			((View)editor.getParent()).setVisibility(View.GONE);
		}
		editor.setCursorVisible(false);
		
		editor = (EditText)findViewById(R.id.golden_words_editor);
		if(TextUtils.isEmpty(editor.getText().toString())) {
			((View)editor.getParent()).setVisibility(View.GONE);
		}
		editor.setCursorVisible(false);
		
		((ImageView)findViewById(R.id.imag2)).setImageDrawable(null);
		((ImageView)findViewById(R.id.imag3)).setImageDrawable(null);
		((ImageView)findViewById(R.id.imag4)).setImageDrawable(null);
		((ImageView)findViewById(R.id.imag5)).setImageDrawable(null);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_video_edit: // 制作
			setEditorViewVisibility();
			mEditor.post(new Runnable() {
				
				@Override
				public void run() {
					if(PreSummeryEditorActivity.this.isFinishing())
						return;
					sBitmapList.clear();
					
					if(mEditor.getTag()!=null) {
					}else {
						mEditor.setBackground(null);
					}
					mEditor.destroyDrawingCache();
					mEditor.setDrawingCacheEnabled(true);
					mEditor.buildDrawingCache();
					sBitmapList.add(new AnimDrawableItem(Bitmap
							.createScaledBitmap(mEditor.getDrawingCache(), PreviewManager.EXPECTED_PREVIEW_W,
									PreviewManager.EXPECTED_PREVIEW_H, false)));
					View img = findViewById(R.id.imag2);
					if(img.getTag()!=null) {
						sBitmapList.add(new AnimDrawableItem(((BitmapDrawable)img.getBackground()).getBitmap()));
					}
					img = findViewById(R.id.imag3);
					if(img.getTag()!=null) {
						sBitmapList.add(new AnimDrawableItem(((BitmapDrawable)img.getBackground()).getBitmap()));
					}
					img = findViewById(R.id.imag4);
					if(img.getTag()!=null) {
						sBitmapList.add(new AnimDrawableItem(((BitmapDrawable)img.getBackground()).getBitmap()));
					}
					img = findViewById(R.id.imag5);
					if(img.getTag()!=null) {
						sBitmapList.add(new AnimDrawableItem(((BitmapDrawable)img.getBackground()).getBitmap()));
					}
					
					
					Intent i = new Intent();
//					i.putExtra("musicFile", videoFilePath);
					i.putExtra("musicname", Title);
					saveEditInfo(i);
					setResult(Activity.RESULT_OK, i);
					finish();
//					showSizeProgressDialog(PreSummeryEditorActivity.this, 3050*list.size(), 0);
//					new ProcessVideoTask().execute(list);					
				}
			});
			
			break;
		case R.id.btn_picker: // 制作
			curent = mEditor;
			showDialog();
			break;
		case R.id.return_imgbtn:
			finish();
			break;
		case R.id.login_status://clear image
			setPicToView(null);
			break;
		case R.id.imag2:
			showDialog();
			curent = v;
			break;
		case R.id.imag3:
			showDialog();
			curent = v;
			break;
		case R.id.imag4:
			showDialog();
			curent =v;
			break;
		case R.id.imag5:
			showDialog();
			curent = v;
			break;
		default:
			break;
		}

	}

	@Override
	protected void handleReceiver(Context context, Intent intent) {
		
	}

	@Override
	protected boolean validateData() {
		return false;
	}

	private class ProcessVideoTask extends AsyncTask<List<Bitmap>, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(List<Bitmap>... params) {
			initMediaEngine();
			mMediaEngine.setForegroundDrawable(params[0]);
			startRecordStaticImage();
			int endtime = 3000*params[0].size()+100;
			int time = 0;
			while(true) {
				mHandler.post(mFrameRunnable);
				publishProgress(time);
				time+=30;
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(time >endtime)
					break;
			}
			mMediaEngine.stop();
			mMediaEngine.release();
			try {
				publishProgress(time);
				time+=30;
//				mergeVideo();
				publishProgress(time);
				time+=30;
			} catch (Exception e) {
				Log.d(TAG,""+e.getMessage());
				return false;
			}
			return true;
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			if(view==null)
				return;
			MyProgressBar uploading_proressbar = (MyProgressBar) view.findViewById(R.id.uploading_proressbar);
			uploading_proressbar.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
				Toast.makeText(getApplicationContext(),result?"合成视频成功！": "合成视频失败！",
						Toast.LENGTH_SHORT).show();
			mDialog.dismiss();
			if(result)
				handleRecordVideoResult();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		handleRecordVideoResult();
	}
	
	private void handleRecordVideoResult() {
//            final File file = new File(videoFilePath);
//            if (file.exists()) {
//                setResult(RESULT_OK, new Intent().setData(Uri.fromFile(file)));
//                finish();
//            }
//			CustomAlertDialog mCustomDialog = new CustomAlertDialog(PreSummeryEditorActivity.this).builder(R.style.DialogTVAnimWindowAnim);
//			mCustomDialog.setTitle("合成完成");
//			mCustomDialog.setMsg("点击确定可预览视频");
//			mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent mIntent = new Intent();
//					mIntent.setClass(PreSummeryEditorActivity.this, VideoViewPlayingActivity.class);
//					mIntent.setData(Uri.parse(videopath));
//					startActivity(mIntent);
//				}
//			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			}).show();

		}
	
	/**
	 * 
	 * @Description:正在合成视频文件进度对话框
	 * @param mContext
	 * @param total
	 * @param current
	 * @return void
	 */
	public void showSizeProgressDialog(Context mContext, int total, int current) {
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.show();

		view = inflater.inflate(R.layout.custom_progressbar_dialog, null);
		MyProgressBar uploading_proressbar = (MyProgressBar) view.findViewById(R.id.uploading_proressbar);
		tvLoading = (TextView) view.findViewById(R.id.uploading_tv);
		tvLoading.setText("正在合成视频...");
		mDialog.setContentView(view);
		mDialog.setCancelable(false); // false设置点击其他地方不能取消进度条

		uploading_proressbar.setMax(total);
		uploading_proressbar.setProgress(current);

		if (uploading_proressbar.getProgress() >= uploading_proressbar.getMax()) { // 合成完成
			// 关闭正在合成...的对话框
			mDialog.cancel();
		}

	}
	
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
	
    private void showDialog() {
    	final CustomAlertDialog mCustomDialog = new CustomAlertDialog(PreSummeryEditorActivity.this).builder(R.style.DialogTVAnimWindowAnim);
		mCustomDialog.setTitle("片头背景");
    	mCustomDialog.setPositiveButton("拍照", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tmpJpgFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("相册", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
        case PHOTO_REQUEST_TAKEPHOTO:
        	if(data!=null)
        		startPhotoZoom(Uri.fromFile(tmpJpgFile));
            break;

        case PHOTO_REQUEST_GALLERY:
            if (data != null) {
                startPhotoZoom(data.getData());
            }
            break;

        case PHOTO_REQUEST_CUT:
            if (data != null) {
                setPicToView(tmpJpgFile);
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", PreviewManager.EXPECTED_PREVIEW_W);
        intent.putExtra("outputY", PreviewManager.EXPECTED_PREVIEW_H);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tmpJpgFile));
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //将进行剪裁后的图片显示到UI界面上
    private void setPicToView(File file) {
//    		findViewById(R.id.login_status).setVisibility(file == null?View.GONE:View.VISIBLE);
    		if(file == null) {
    			sBitmapList.clear();
    			mEditor.setBackgroundColor(getResources().getColor(R.color.main_tab_bottom_bg));
    			mEditor.setTag(null);
    			View v = findViewById(R.id.imag2);
    			v.setBackgroundColor(getResources().getColor(R.color.main_tab_bottom_bg));
    			v.setTag(null);
    			 v = findViewById(R.id.imag3);
     			v.setBackgroundColor(getResources().getColor(R.color.main_tab_bottom_bg));
     			v.setTag(null);
     			 v = findViewById(R.id.imag4);
     			v.setBackgroundColor(getResources().getColor(R.color.main_tab_bottom_bg));
     			v.setTag(null);
     			 v = findViewById(R.id.imag5);
     			v.setBackgroundColor(getResources().getColor(R.color.main_tab_bottom_bg));
     			v.setTag(null);
    			return;
    		}
    		Uri uri = Uri.fromFile(file);
    		if(curent == null)
    			return;
    		 Log.d("guolei","setPicToView photo "+uri.toString());
            Bitmap photo = null;
                try {              
                	photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    
                }
            if(photo == null)
            	return;	
            Log.d("guolei","setPicToView photo "+photo.getWidth()+ " X  "+photo.getHeight());
            Drawable drawable = new BitmapDrawable(photo);
            curent.setBackgroundDrawable(drawable);
            curent.setTag(new Object());
    }

	
//	private  void mergeVideo() throws Exception {
//
//	    Log.d("guolei", "Merge process started ");
//	     List<String> fileList = new ArrayList<String>();
//	     List<Movie> moviesList = new LinkedList<Movie>();
//	     fileList.add(tmpFile);
//	     fileList.add(videoFilePath);
//	     for (String file:fileList){
//	             //Set rotation I tried to experiment with this instruction but is not working
//	    	 Log.d("guolei", "Video " + file  + " start" );
//	             moviesList.add(MovieCreator.build(file));
//	     }
//
//
//	        List<Track> videoTracks = new LinkedList<Track>();
//	        List<Track> audioTracks = new LinkedList<Track>();
//
//	        for (Movie m : moviesList) {
//	            for (Track t : m.getTracks()) {
//	                if (t.getHandler().equals("soun")) {
//	                    audioTracks.add(t);
//	                }
//	                if (t.getHandler().equals("vide")) {
//	                    videoTracks.add(t);
//	                }
//	            }
//	        }
//
//	        Movie result = new Movie();
//
//	        if (audioTracks.size() > 0) {
//	            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
//	        }
//	        if (videoTracks.size() > 0) {
//	            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
//	        }
//
//	        Container out = new DefaultMp4Builder().build(result);
//
//	        //out.getMovieBox().getMovieHeaderBox().setMatrix(Matrix.ROTATE_180); //set orientation, default merged video have wrong orientation
//	        // Create a media file name
//	        //
//	        String filename =  videoFilePath+"_tmp.mp4";
//
//	        FileChannel fc = new RandomAccessFile(String.format(filename), "rw").getChannel();
//	        out.writeContainer(fc);
//	        fc.close();
//
//	        moviesList.clear();
//	        fileList.clear();
//	        //don't leave until the file is on his place
//	        if(!new File(tmpFile).delete()) Log.d("guolei", "delete fail "+tmpFile);
//	        File oldfile = new File(videoFilePath);
//	        if(!oldfile.delete()) Log.d("guolei", "delete fail "+videoFilePath);
//	        File newfile = new File(filename);
//	        newfile.renameTo(oldfile);
//	        Log.d("guolei", "Merge process finished");
//	}
	
	
	public class MyViewPagerAdapter extends PagerAdapter{
		private List<View> mListViews;
		
		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 	{	
			container.removeView(mListViews.get(position));//删除页卡
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {	//这个方法用来实例化页卡		
			 container.addView(mListViews.get(position), 0);//添加页卡
			 return mListViews.get(position);
		}

		@Override
		public int getCount() {			
			return  mListViews.size();//返回页卡的数量
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {			
			return arg0==arg1;//官方提示这样写
		}
	}
}
