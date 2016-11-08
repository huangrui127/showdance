package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.RegisterCodeTimer;
import com.android.app.showdance.logic.RegisterCodeTimerService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.RegularUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 【验证原手机号，然后修改手机号】的页面
 * **/

public class TheModifyOldPhoneActivity extends BaseActivity {

	private EditText old_phone_edt; // 原手机号
	private String old_phone; //输入的原手机号
	private Button modifyPhone_GetCodeBtn; // 修改手机号-获取验证码
	private EditText code_edt; // 验证码
	private String codeStr;
	private EditText new_phone_edt; // 新手机号码
	private String new_phone;
	private Button modify_oldPhone_btn;// 校验验证码-修改手机号按钮
	private Integer sign;// 操作标记
	protected Long id;
	
	private Button btn_clear_search_text1;
	private Button btn_clear_search_text2;
	private Button btn_clear_search_text3;
	
	private Intent mIntent;
	private ReceiverCmdMsg mReceiverCmdMsg; //
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_modify_old_phone);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		old_phone_edt = (EditText) findViewById(R.id.old_phone_edt);
		modifyPhone_GetCodeBtn = (Button) findViewById(R.id.modifyPhone_GetCodeBtn);
		code_edt = (EditText) findViewById(R.id.modifyPhone_code_edt);
		btn_clear_search_text1 = (Button) findViewById(R.id.btn_clear_search_text1);
		btn_clear_search_text2 = (Button) findViewById(R.id.btn_clear_search_text2);
		btn_clear_search_text3 = (Button) findViewById(R.id.btn_clear_search_text3);
		new_phone_edt = (EditText) findViewById(R.id.new_phone_edt);
		modify_oldPhone_btn = (Button) findViewById(R.id.modify_oldPhone_btn);

	}

	@Override
	protected void initView() {
		tvTitle.setText("修改绑定手机号");
		return_imgbtn.setVisibility(View.VISIBLE);

		// 短信验证码***
		RegisterCodeTimerService.setHandler(mCodeHandler);
		mIntent = new Intent(TheModifyOldPhoneActivity.this, RegisterCodeTimerService.class);

		mReceiverCmdMsg = new ReceiverCmdMsg();
		mReceiverCmdMsg.registerAction(ConstantsUtil.ACTION_SMS_RECEIVED);
		// 短信验证码***
		
		// 获取配置中的手机号
		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();
		} else { // userInfo对象不为空，则从里面取ID
			id = userInfo.getId(); // 用户Id
		}
		//
		old_phone_edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				int textLength = old_phone_edt.getText().length();
				if (textLength > 0) {
					btn_clear_search_text1.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text1.setVisibility(View.GONE);
				}
			}
		});
		//
		code_edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				int textLength = code_edt.getText().length();
				if (textLength > 0) {
					btn_clear_search_text2.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text2.setVisibility(View.GONE);
				}
			}
		});

		new_phone_edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				int textLength = new_phone_edt.getText().length();
				if (textLength > 0) {
					btn_clear_search_text3.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text3.setVisibility(View.GONE);
				}
			}
		});

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		btn_clear_search_text1.setOnClickListener(this);
		btn_clear_search_text2.setOnClickListener(this);
		btn_clear_search_text3.setOnClickListener(this);
		modifyPhone_GetCodeBtn.setOnClickListener(this);
		modify_oldPhone_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:
			this.finish();
			break;
		case R.id.btn_clear_search_text1: //输入框1
			old_phone_edt.setText("");
			btn_clear_search_text1.setVisibility(View.GONE);
			break;
		case R.id.btn_clear_search_text2: //输入框2
			code_edt.setText("");
			btn_clear_search_text2.setVisibility(View.GONE);
			break;
		case R.id.btn_clear_search_text3: //输入框3
			new_phone_edt.setText("");
			btn_clear_search_text3.setVisibility(View.GONE);
			break;
		case R.id.modifyPhone_GetCodeBtn: // 修改手机号-获取验证码
			if (validatePhone()) {
				
				modifyPhone_GetCodeBtn.setEnabled(false);
				startService(mIntent);
				showProgressDialog(this, 0);// 接口
				
				hideSoftInputView();
			}

			break;

		case R.id.modify_oldPhone_btn: // 校验验证码-修改手机号
			if (validateData()) {
				showProgressDialog(this, 1);
			}

			break;

		default:
			break;
		}
	}

	// 【校验电话号码格式】是否正确
	protected boolean validatePhone() {
		old_phone = old_phone_edt.getText().toString();
		boolean flag = true;
		flag = RegularUtil.isMobileNO(TheModifyOldPhoneActivity.this, old_phone);
		if (!flag) {
			Toast.makeText(getApplicationContext(), "请输入格式正确的手机号", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (TextUtils.isEmpty(old_phone)) {
			Toast.makeText(getApplicationContext(), "请输入已绑定的手机号", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		return flag;
	}

//	protected boolean validateCode() {
//		old_phone = old_phone_edt.getText().toString();
//		codeStr = code_edt.getText().toString();
//		boolean flag = true;
//		// 判断输入框不能为空
//		if (TextUtils.isEmpty(codeStr)) {
//			Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
//			flag = false;
//		} else {
//			// 校验【电话号码格式】是否正确
//			flag = validatePhone(old_phone);
//		}
//		return flag;
//	}

	@Override
	protected boolean validateData() {
		old_phone = old_phone_edt.getText().toString();
		codeStr = code_edt.getText().toString();
		new_phone = new_phone_edt.getText().toString();
		boolean flag = true;
		// 判断输入框不能为空
		if (TextUtils.isEmpty(codeStr)) {
			Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (!RegularUtil.isMobileNO(TheModifyOldPhoneActivity.this, old_phone)) {
			Toast.makeText(getApplicationContext(), "请输入格式正确的原手机号", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (!RegularUtil.isMobileNO(TheModifyOldPhoneActivity.this, new_phone)) {
			Toast.makeText(getApplicationContext(), "请输入格式正确的新手机号", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if ((old_phone != null && !old_phone.equals("")) && (new_phone != null && !new_phone.equals(""))) {
			if (new_phone.equals(old_phone)) {
				Toast.makeText(getApplicationContext(), "请输入与原手机号不同的新手机号", Toast.LENGTH_SHORT).show();
				flag = false;
			}
		}
		return flag;
	}

	public void showProgressDialog(Context mContext, int type) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		UserVo modifyPhoneUserVo = new UserVo();
		paramsMap = new HashMap<String, Object>();
		sign = 3; // sign取值3指代“修改手机号”
		Task mTask;
		switch (type) {
		case 0: // 修改手机号-发送验证码

			modifyPhoneUserVo.setMobilephone(old_phone);// 原手机号码
			modifyPhoneUserVo.setSign(sign);

			paramsMap.put("modifyPhoneUserVo", modifyPhoneUserVo);
			mTask = new Task(TaskType.TS_modifyPhoneGETCODE, paramsMap);
			MainService.newTask(mTask);
			break;

		case 1: // 修改手机号-检验验证码

			modifyPhoneUserVo.setPhone(old_phone);// 原手机号码
			modifyPhoneUserVo.setMobilephone(new_phone); // 新手机号码
			modifyPhoneUserVo.setCode(StringUtils.stringTolnt(codeStr));// 测试验证码,字符串转int传值
			modifyPhoneUserVo.setSign(sign);
			modifyPhoneUserVo.setId(id);

			paramsMap.put("modifyPhoneUserVo", modifyPhoneUserVo);
			mTask = new Task(TaskType.TS_modifyPhoneCheckCODE, paramsMap);
			MainService.newTask(mTask);
			break;

		default:
			break;
		}
	}
	
	/**
	 * @ClassName: ReceiverCmdMsg
	 * @Description: 短信验证码
	 * @author
	 * @date 2015-04-14 下午16:33:55
	 */
	private class ReceiverCmdMsg extends BroadcastReceiver {

		public void registerAction(String action) {
			IntentFilter filter = new IntentFilter();
			// 设置短信拦截参数
			filter.addAction(action);
			filter.setPriority(Integer.MAX_VALUE);
			registerReceiver(mReceiverCmdMsg, filter);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ConstantsUtil.ACTION_SMS_RECEIVED)) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					String message = sms.getMessageBody();
					Log.d("短信内容", "message：" + message);
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					Log.d("短信来源", "from ：" + from);
					// Time time = new Time();
					// time.set(sms.getTimestampMillis());
					// String time2 = time.format3339(true);
					// Log.d("logo", from + "   " + message + "  " + time2);
					// strContent = from + "   " + message;
					// handler.sendEmptyMessage(1);
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							Message msg = new Message();
							msg.what = 1;
							Bundle bundle = new Bundle();
							bundle.putString("messagecode", code);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					}
				}
			}
		}
	}

	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/**
	 * 倒计时Handler
	 */
	@SuppressLint("HandlerLeak")
	private Handler mCodeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
				modifyPhone_GetCodeBtn.setText(msg.obj.toString());
			} else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
				modifyPhone_GetCodeBtn.setEnabled(true);
				modifyPhone_GetCodeBtn.setText(msg.obj.toString());
			}
		};
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String codeMsg = msg.getData().getString("messagecode");
				Log.d("得到的code", codeMsg);
				code_edt.setText(codeMsg);
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(mIntent);
		unregisterReceiver(mReceiverCmdMsg);
	}

	@Override
	public void refresh(Object... param) {
		Intent mIntent = new Intent();
		Bundle bundle = new Bundle();
		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_modifyPhoneGETCODE: // 修改手机号-发送验证码
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "验证码已发送到手机", Toast.LENGTH_SHORT).show();
					} else if (result.equals("1")) {
						Toast.makeText(getApplicationContext(), "请输入已注册的手机号", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
			}
			break;
		case TaskType.TS_modifyPhoneCheckCODE: // 修改手机号-检验验证码
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (Map<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "手机号修改成功", Toast.LENGTH_SHORT).show();
						//手机号修改成功,则更改配置文件中的手机号
						UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();
						userInfo.setMobilephone(new_phone);
						InitApplication.mSpUtil.setUserInfo(userInfo);
						
					} else if (result.equals("1")){ // result不为 "0"
						Toast.makeText(getApplicationContext(), "验证码有误", Toast.LENGTH_SHORT).show();
					} 
				} else { // result.equals("-1")
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
