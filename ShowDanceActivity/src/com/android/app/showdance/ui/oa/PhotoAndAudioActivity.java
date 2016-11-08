package com.android.app.showdance.ui.oa;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.app.showdance.adapter.PhotoAndAudioAdapter;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.wumeiniang.R;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 我的-【相册和视频】界面
 */
public class PhotoAndAudioActivity extends BaseActivity {
	private RelativeLayout account_state_ll;// 账号
	private ImageButton return_imgbtn;// 返回
	private ImageView take_photo_img; //拍照
	private ListView release_photo_audio_lv;
	
	private File picFilePath;
	private String localCameraPath; //拍照或选择相册图片后得到的图片地址
	/* 用来标识请求照相功能的activity */
	private static final int REQUESTCODE_TAKE_CAMERA = 1;// 拍照
	private static final int REQUESTCODE_TAKE_LOCAL = 2;// 本地图片
	private static String STORAGE_PATH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_and_audio);
		findViewById();
		initView();
		setOnClickListener();
	}

	/**
	 * 查找界面各控件
	 */
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		account_state_ll = (RelativeLayout) findViewById(R.id.account_state_ll);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		take_photo_img = (ImageView) findViewById(R.id.take_photo_img);
		release_photo_audio_lv = (ListView) findViewById(R.id.release_photo_audio_lv);
	}

	/*
	 * 附默认值
	 */
	@Override
	protected void initView() {
		tvTitle.setText("相册和视频");
		return_imgbtn.setVisibility(View.VISIBLE);
		
		/**
		 * // ** ListView列表数据的显示 //
		 **/
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		HashMap<String, Object> map8 = new HashMap<String, Object>();
		HashMap<String, Object> map9 = new HashMap<String, Object>();
		HashMap<String, Object> map10 = new HashMap<String, Object>();

		map1.put("dayofmonth", "3月10号");
		map1.put("content", "武大樱花园全程孕育在一种粉色的氛围中，乘着好天气大家可以去赏赏樱花的美景");

		map2.put("dayofmonth", "3月11号");
		map2.put("content", "好天气出游看风景~");

		map3.put("dayofmonth", "3月13号");
		map3.put("content", "植物园五彩缤纷");

		map4.put("dayofmonth", "3月14号");
		map4.put("content", "海洋世界模拟海底神奇的世界");
		
		map5.put("dayofmonth", "3月17号");
		map5.put("content", "福兮祸所依，祸兮福所伏");

		map6.put("dayofmonth", "3月18号");
		map6.put("content", "武大樱花园全程孕育在一种粉色的氛围中，乘着好天气大家可以去赏赏樱花的美景");

		map7.put("dayofmonth", "3月19号");
		map7.put("content", "好天气出游看风景~");

		map8.put("dayofmonth", "3月20号");
		map8.put("content", "植物园五彩缤纷");

		map9.put("dayofmonth", "3月21号");
		map9.put("content", "海洋世界模拟海底神奇的世界");
		
		map10.put("dayofmonth", "3月22号");
		map10.put("content", "福兮祸所依，祸兮福所伏");
		
		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		listItem.add(map5);
		listItem.add(map6);
		listItem.add(map7);
		listItem.add(map8);
		listItem.add(map9);
		listItem.add(map10);

		// 创建适配器对象
		PhotoAndAudioAdapter photoAndAudioAdapter = new PhotoAndAudioAdapter(PhotoAndAudioActivity.this, listItem);
		release_photo_audio_lv.setAdapter(photoAndAudioAdapter);// 为ListView绑定适配器
	}
	/**
	 * 设置事件
	 */
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		account_state_ll.setOnClickListener(this);
		take_photo_img.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回键
			finish();
			break;
		case R.id.account_state_ll:// 账号
			break;
		case R.id.take_photo_img:// 拍照
//			mIntent.setClass(this, PublishedActivity.class);
//			startActivity(mIntent);
			
			showPhotoChooseDialog();

			break;
			

		}

	}



	/**
	 * 弹出自定义上传照片对话框
	 */
	private void showPhotoChooseDialog() {
		View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
		final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		Button pickPhoto_btn = (Button) view.findViewById(R.id.pickPhoto_btn);
		Button takePhoto_btn = (Button) view.findViewById(R.id.takePhoto_btn);
		Button Photo_cancel_btn = (Button) view.findViewById(R.id.Photo_cancel_btn);

		pickPhoto_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
				pickPhoto();// 从相册中取图片
			}
		});
		takePhoto_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
				takePhoto();// 拍照获取图片

			}
		});
		Photo_cancel_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}
	
	/**
	 * @Description:从相册中取图片
	 * @param
	 * @return void
	 */
	private void pickPhoto() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUESTCODE_TAKE_LOCAL);

	}

	/**
	 * 
	 * @Description:拍照获取图片
	 * @param
	 * @return void
	 */
	private void takePhoto() {
		// 拍照后的存储路劲
		STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WirelessOrder/ImageCaches/";
		File path = new File(STORAGE_PATH);
		if (!path.exists()) {
			path.mkdirs();
		}

		picFilePath = new File(STORAGE_PATH + getPhotoFileName());

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 设置照片存储位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFilePath));
		startActivityForResult(intent, REQUESTCODE_TAKE_CAMERA);
	}
	
	// 用日期作为文件名称
		private String getPhotoFileName() {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
			return dateFormat.format(date) + ".jpg";
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Bitmap bmp;
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				case REQUESTCODE_TAKE_CAMERA:// 当取到值的时候才上传path路径下的图片到服务器
					localCameraPath = picFilePath.getPath().toString();
					bmp = BitmapFactory.decodeFile(localCameraPath);
					take_photo_img.setImageBitmap(bmp);
					break;
				case REQUESTCODE_TAKE_LOCAL:
					if (data != null) {
						Uri selectedImage = data.getData();
						if (selectedImage != null) {
							Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
							cursor.moveToFirst();
							int columnIndex = cursor.getColumnIndex("_data");
							String localSelectPath = cursor.getString(columnIndex);
							cursor.close();
							if (localSelectPath == null || localSelectPath.equals("null")) {
								return;
							}
							localCameraPath = localSelectPath.toString();
						}
					}
					bmp = BitmapFactory.decodeFile(localCameraPath);
					take_photo_img.setImageBitmap(bmp);
					break;

				}
			}
		}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	protected boolean validateData() {
		return false;
	}



}
