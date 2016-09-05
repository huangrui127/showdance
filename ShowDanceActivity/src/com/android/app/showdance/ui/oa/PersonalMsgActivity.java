package com.android.app.showdance.ui.oa;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.User;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.usercenter.TheBirthDayActivity;
import com.android.app.showdance.ui.usercenter.TheLivePlaceActivity;
import com.android.app.showdance.ui.usercenter.TheSignatureActivity;
import com.android.app.showdance.ui.usercenter.TheStageNameActivity;
import com.android.app.showdance.ui.usermanager.TheModifyOldPhoneActivity;
import com.android.app.showdance.ui.usermanager.TheModifyPasswordActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.PhotoUtils;
import com.android.app.showdance.utils.XUtilsBitmap;
import com.lidroid.xutils.BitmapUtils;

/**
 * 【个人信息】页面
 */
public class PersonalMsgActivity extends BaseActivity {

	private ImageView return_imgbtn;// 返回
	private String userId;
	private Button save_btn;// 保存

	private LinearLayout pmHeadPortrait_ll; // 头像线性布局
	private ImageView pmHeadPortrait_img; // 头像

	private LinearLayout the_stage_name_ll; // 艺名
	private LinearLayout the_phone_ll; // 手机号
	// private LinearLayout two_dimension_code_ll; // 二维码
	private LinearLayout the_live_place_ll; // 居住地
	private LinearLayout the_birthday_ll; // 生日
	private LinearLayout the_signature_ll; // 个性签名
	private LinearLayout the_modify_password_ll; // 修改密码

	private static final int stage_name = 11; // 艺名
	private static final int live_place = 12; // 居住地
	private static final int birthday = 13; // 生日
	private static final int signature = 14; // 个性签名

	private TextView the_stage_name_tv; // 艺名 TextView
	private TextView the_live_place_tv; // 居住地 TextView
	private TextView the_birthday_tv; // 生日 TextView
	private TextView the_signature_tv; // 个性签名 TextView
	private String stage_nameStr; // 艺名
	private String live_placeStr; // 居住地
	private String birthdayStr; // 生日
	private String signatureStr; // 个性签名

	private String show_dance_id; // 秀舞吧号
	private TextView the_phone_tv; // 手机号 TextView
	private String the_phoneStr;
	private Long createUser;
	private ImageView two_dimension_code_img; // 二维码 ImageView

	private File picFilePath;
	private String localCameraPath; // 拍照或选择相册图片后得到的图片地址
	/* 用来标识请求照相功能的activity */
	private static final int REQUESTCODE_TAKE_CAMERA = 1;// 拍照
	private static final int REQUESTCODE_TAKE_LOCAL = 2;// 本地图片
	private static final int REQUESTCODE_RESULT = 3;// 裁剪
	public BitmapUtils bitmapUtils;// 异步加载图片
	ImageView news_content;
	private String imgUrl;
	String photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_msg);
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
		return_imgbtn = (ImageView) findViewById(R.id.return_imgbtn);

		// pmHeadPortrait_ll = (LinearLayout)
		// findViewById(R.id.pmHeadPortrait_ll);
		the_stage_name_ll = (LinearLayout) findViewById(R.id.the_stage_name_ll);
		the_phone_ll = (LinearLayout) findViewById(R.id.the_phone_ll);
		// two_dimension_code_ll = (LinearLayout)
		// findViewById(R.id.two_dimension_code_ll);
		the_live_place_ll = (LinearLayout) findViewById(R.id.the_live_place_ll);
		the_birthday_ll = (LinearLayout) findViewById(R.id.the_birthday_ll);
		the_signature_ll = (LinearLayout) findViewById(R.id.the_signature_ll);
		the_modify_password_ll = (LinearLayout) findViewById(R.id.the_modify_password_ll);

		pmHeadPortrait_img = (ImageView) findViewById(R.id.pmHeadPortrait_img); // 头像
		the_stage_name_tv = (TextView) findViewById(R.id.the_stage_name_tv);
		the_phone_tv = (TextView) findViewById(R.id.the_phone_tv);
		// two_dimension_code_img = (ImageView)
		// findViewById(R.id.two_dimension_code_img);
		the_live_place_tv = (TextView) findViewById(R.id.the_live_place_tv);
		the_birthday_tv = (TextView) findViewById(R.id.the_birthday_tv);
		the_signature_tv = (TextView) findViewById(R.id.the_signature_tv);

		save_btn = (Button) findViewById(R.id.save_btn);
	}

	@Override
	protected void initView() {
		tvTitle.setText("个人信息");
		return_imgbtn.setVisibility(View.VISIBLE);

		// 获取配置中的id
		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();
		} else { // userInfo对象不为空，则从里面取ID
			createUser = userInfo.getId();

		}

		show_dance_id = getIntent().getStringExtra("show_dance_id");

		showProgressDialog(this, 1);

		// 异步加载图片
		bitmapUtils = XUtilsBitmap.getBitmapUtils(PersonalMsgActivity.this);

		bitmapUtils.display(news_content, imgUrl);

	}

	/**
	 * 设置事件
	 */
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);

		pmHeadPortrait_img.setOnClickListener(this); // 头像
		the_stage_name_ll.setOnClickListener(this);
		the_phone_ll.setOnClickListener(this);
		// two_dimension_code_ll.setOnClickListener(this);
		the_live_place_ll.setOnClickListener(this);
		the_birthday_ll.setOnClickListener(this);
		the_signature_ll.setOnClickListener(this);
		the_modify_password_ll.setOnClickListener(this);

		save_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		Intent mIntent = new Intent();
		switch (arg0.getId()) {
		case R.id.return_imgbtn:// 返回键
			this.finish();
			break;

		case R.id.pmHeadPortrait_img: // 头像
			showPhotoChooseDialog();

			break;

		case R.id.the_stage_name_ll: // 艺名
			mIntent.setClass(this, TheStageNameActivity.class);
			startActivityForResult(mIntent, stage_name);

			break;

		case R.id.the_phone_ll: // 手机号
			showModifyPhoneDialog();
			break;

		// case R.id.two_dimension_code_ll: // 二维码
		//
		// break;

		case R.id.the_live_place_ll: // 居住地
			mIntent.setClass(this, TheLivePlaceActivity.class);
			startActivityForResult(mIntent, live_place);

			break;

		case R.id.the_birthday_ll: // 生日
			mIntent.setClass(this, TheBirthDayActivity.class);
			startActivityForResult(mIntent, birthday);

			break;

		case R.id.the_signature_ll: // 个性签名
			mIntent.setClass(this, TheSignatureActivity.class);
			startActivityForResult(mIntent, signature);

			break;

		case R.id.the_modify_password_ll: // 修改密码
			mIntent.setClass(this, TheModifyPasswordActivity.class);
			startActivity(mIntent);
			break;

		case R.id.save_btn:// 保存-（修改个人信息）

			if (validateData()) {
				showProgressDialog(this, 0);
			}

			break;

		default:
			break;
		}

	}

	// 跳转的时候要用startActivityForResult来做跳转
	// 重写onActivityResult方法，用来接收***Activity回传的数据。
	// 在***Activity中"回传数据"时采用setResult方法，并且之后要调用finish方法。
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Bitmap bmp;
		Bundle bundle;
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case stage_name: // 艺名
				bundle = data.getExtras();
				stage_nameStr = bundle.getString("stage_name");
				// 显示
				the_stage_name_tv.setText(stage_nameStr);
				break;

			case live_place:// 居住地
				bundle = data.getExtras();
				live_placeStr = bundle.getString("live_place");
				// 显示
				the_live_place_tv.setText(live_placeStr);
				break;

			case birthday: // 生日
				bundle = data.getExtras();
				birthdayStr = bundle.getString("birthday");
				// 显示
				the_birthday_tv.setText(birthdayStr);
				break;

			case signature: // 个性签名
				bundle = data.getExtras();
				signatureStr = bundle.getString("signature");
				// 显示
				the_signature_tv.setText(signatureStr);
				break;

			case REQUESTCODE_TAKE_CAMERA:// 拍照
				localCameraPath = picFilePath.getPath().toString();
				
				/**
				 * 不裁剪时直接使用如下两行
				 */
//				bmp = BitmapFactory.decodeFile(localCameraPath); // decodeFile()解码成一个位图文件路径
//				pmHeadPortrait_img.setImageBitmap(bmp); // 设置一个位图作为ImageView的内容

				/**
				 * 裁剪时使用如下一行
				 */
				startPhotoZoom(Uri.fromFile(picFilePath));

				break;
			case REQUESTCODE_TAKE_LOCAL:// 本地图片
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
				
				/**
				 * 不裁剪时直接使用如下两行
				 */
//				bmp = BitmapFactory.decodeFile(localCameraPath);
//				pmHeadPortrait_img.setImageBitmap(bmp);

				/**
				 * 裁剪时使用如下一行
				 */
				startPhotoZoom(data.getData());

				break;

			// 取得裁剪后的图片
			case REQUESTCODE_RESULT:// 裁剪
				if (data != null) {
					setPicToView(data);
				}
				break;

			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUESTCODE_RESULT);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			pmHeadPortrait_img.setImageDrawable(drawable);
		}
	}

	/**
	 * 弹出修改手机号的对话框
	 */
	private void showModifyPhoneDialog() {
		View view = getLayoutInflater().inflate(R.layout.modify_phone_dialog, null);
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

		Button modify_phone_btn = (Button) view.findViewById(R.id.modify_phone_btn);
		Button modify_phone_cancel_btn = (Button) view.findViewById(R.id.modify_phone_cancel_btn);

		modify_phone_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				ToVerifyPhone();// 验证手机号的页面
			}
		});
		modify_phone_cancel_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	// 去到验证原手机号，然后修改手机号的页面
	private void ToVerifyPhone() {
		Intent intent = new Intent();
		intent.setClass(this, TheModifyOldPhoneActivity.class);
		startActivity(intent);
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

	// 用日期作为文件名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * @Description:拍照获取图片
	 * @param
	 * @return void
	 */
	private void takePhoto() {
		picFilePath = new File(InitApplication.SdCardImagePath + getPhotoFileName());

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 设置照片存储位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFilePath));
		startActivityForResult(intent, REQUESTCODE_TAKE_CAMERA);
	}

	@Override
	protected boolean validateData() {
		boolean flag = true;

		stage_nameStr = the_stage_name_tv.getText().toString();
		the_phoneStr = the_phone_tv.getText().toString();
		live_placeStr = the_live_place_tv.getText().toString();
		birthdayStr = the_birthday_tv.getText().toString();
		signatureStr = the_signature_tv.getText().toString();

		return flag;
	}

	public void showProgressDialog(Context mContext, int type) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		paramsMap = new HashMap<String, Object>();
		Task mTask;
		switch (type) {
		case 0: // 修改用户信息
			UserInfo modifyUserInfo = new UserInfo();

			modifyUserInfo.setId(createUser);
			modifyUserInfo.setName(stage_nameStr);
			String imagePath = null;
			if (localCameraPath != null) {

				try {
					imagePath = PhotoUtils.encodeBase64File(localCameraPath);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			modifyUserInfo.setLoginName(show_dance_id);
			modifyUserInfo.setPhoto(imagePath);
			modifyUserInfo.setMobilephone(the_phoneStr);
			modifyUserInfo.setAddress(live_placeStr);

			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// Date date = null;
			// try {
			// date = sdf.parse(birthdayStr);
			// modifyUserInfo.setBirthday(date); //生日类型设计的是 Date类型，要转换
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// Date date =
			// StringUtils.stringToDate(birthdayStr.concat(" 00:00:00"));

			
			
//			Date date = StringUtils.stringToDate(birthdayStr);
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
//			String str = df.format(date);
//			Date strDate = StringUtils.toDate(str);// TODO

			modifyUserInfo.setBirthday(birthdayStr); // 生日类型设计的是 Date类型，要转换

			modifyUserInfo.setSignature(signatureStr);

			paramsMap.put("modifyUserInfo", modifyUserInfo);
			mTask = new Task(TaskType.TS_modifyUserInfo, paramsMap);
			MainService.newTask(mTask);
			break;
		case 1: // 查看用户信息
			User user = new User();
			user.setId(createUser);

			paramsMap.put("user", user);
			mTask = new Task(TaskType.TS_GETUSERINFOBYID, paramsMap);
			MainService.newTask(mTask);
			break;

		default:
			break;
		}
	}

	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_modifyUserInfo: // 修改用户信息
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "修改用户信息成功!", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent();
						intent.putExtra("imgPath", localCameraPath);
						setResult(RESULT_OK, intent);
						finish();
					} else if (result.equals("1")) {
						Toast.makeText(getApplicationContext(), "修改用户信息失败!", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
			}
			break;

		case TaskType.TS_GETUSERINFOBYID: // 查看用户信息
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (Map<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						String loginName = map.get("loginName").toString();
						String id = map.get("id").toString();
						if (map.get("name") != null) {
							String name = map.get("name").toString(); // 艺名
							the_stage_name_tv.setText(name);
						}
						if (map.get("mobilephone") != null) {
							String mobilephone = map.get("mobilephone").toString();
							the_phone_tv.setText(mobilephone);
						}
						if (map.get("address") != null) {
							String address = map.get("address").toString();
							the_live_place_tv.setText(address);
						}
						if (map.get("birthday") != null) {
							String birthday = map.get("birthday").toString();
							the_birthday_tv.setText(birthday);
						}
						if (map.get("signature") != null) {// 个性签名
							String signature = map.get("signature").toString();
							the_signature_tv.setText(signature);
						}

						if (map.get("photo") != null) {
							photo = map.get("photo").toString();
							bitmapUtils.display(this.pmHeadPortrait_img, ConstantsUtil.PhotoUri.concat(photo));
						}

						// the_show_dance_number_tv.setText(loginName); //
						// 秀舞吧号（可用作登录账号）

					}
				} else {
					Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

}
