package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.android.app.wumeiniang.R;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.RegisterCodeTimer;
import com.android.app.showdance.logic.RegisterCodeTimerService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.RegularUtil;
import com.android.app.showdance.utils.StringUtils;

/**
 * 【找回密码】页面
 * **/

public class TheFindPasswordActivity extends BaseActivity {

	private Button btn_clear_search_text1;
	private Button btn_clear_search_text2;
	
	private EditText phone_edit;
	private String old_phone; // 输入框输入的手机号
	private EditText forgetPwd_code_edt;
	private String codeString;
	
	private Button forgetPwdGetCodeBtn; // 忘记密码-获取验证码
	private Button find_pwd_next_step_btn; // 下一步
	private Integer sign;// 操作标记

	private Intent mIntent;
	private ReceiverCmdMsg mReceiverCmdMsg; //
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_find_password);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		forgetPwd_code_edt = (EditText) findViewById(R.id.forgetPwd_code_edt);
		forgetPwdGetCodeBtn = (Button) findViewById(R.id.forgetPwdGetCodeBtn);
		btn_clear_search_text1 = (Button) findViewById(R.id.btn_clear_search_text1);
		btn_clear_search_text2 = (Button) findViewById(R.id.btn_clear_search_text2);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		find_pwd_next_step_btn = (Button) findViewById(R.id.find_pwd_next_step_btn);

	}

	@Override
	protected void initView() {
		tvTitle.setText("找回秀舞吧密码");
		return_imgbtn.setVisibility(View.VISIBLE);

		// 短信验证码***
		RegisterCodeTimerService.setHandler(mCodeHandler);
		mIntent = new Intent(TheFindPasswordActivity.this, RegisterCodeTimerService.class);

		mReceiverCmdMsg = new ReceiverCmdMsg();
		mReceiverCmdMsg.registerAction(ConstantsUtil.ACTION_SMS_RECEIVED);
		// 短信验证码***

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		btn_clear_search_text1.setOnClickListener(this);
		btn_clear_search_text2.setOnClickListener(this);
		forgetPwdGetCodeBtn.setOnClickListener(this);
		find_pwd_next_step_btn.setOnClickListener(this);
		// 手机号文本输入情况
		phone_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int textLength = phone_edit.getText().length();
				if (textLength > 0) {
					btn_clear_search_text1.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text1.setVisibility(View.GONE);
				}
			}
		});

		// 验证码文本输入
		forgetPwd_code_edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int textLength = forgetPwd_code_edt.getText().length();
				if (textLength > 0) {
					btn_clear_search_text2.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text2.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn: // 返回键
			this.finish();

			break;

		case R.id.btn_clear_search_text1: //
			phone_edit.setText("");
			btn_clear_search_text1.setVisibility(View.GONE);

			break;

		case R.id.btn_clear_search_text2: //
			forgetPwd_code_edt.setText("");
			btn_clear_search_text2.setVisibility(View.GONE);

			break;

		case R.id.forgetPwdGetCodeBtn: // 忘记密码-获取验证码

			if (validatePhone()) {

				forgetPwdGetCodeBtn.setEnabled(false);
				startService(mIntent);
				showProgressDialog(this , 0); // 调接口: 忘记密码-获取验证码
				hideSoftInputView();
			}

			break;

		case R.id.find_pwd_next_step_btn: // 下一步
			if (validateDataCode()) {
				showProgressDialog(this , 1);
				hideSoftInputView();
			}
			
			break;

		default:
			break;
		}
	}

	// 【校验电话号码格式】是否正确
	protected boolean validatePhone() {
		old_phone = phone_edit.getText().toString();
		
		boolean flag = true;
		flag = RegularUtil.isMobileNO(TheFindPasswordActivity.this, old_phone);
		if (!flag) {
			Toast.makeText(getApplicationContext(), "请输入正确的原绑定手机号", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (TextUtils.isEmpty(old_phone)) {
			Toast.makeText(getApplicationContext(), "原绑定手机号不正确", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		return flag;
	}

	protected boolean validateDataCode() {
		old_phone = phone_edit.getText().toString();
		codeString = forgetPwd_code_edt.getText().toString();
		boolean flag = true;
		// 校验【验证码不能为空】
		if (TextUtils.isEmpty(codeString)) {
			Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		} else {
			// 校验【电话号码格式】是否正确
			flag = validatePhone();
		}
		return flag;
	}
	
	@Override
	protected boolean validateData() {
		return false;
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
				forgetPwdGetCodeBtn.setText(msg.obj.toString());
			} else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
				forgetPwdGetCodeBtn.setEnabled(true);
				forgetPwdGetCodeBtn.setText(msg.obj.toString());
			}
		};
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String codeMsg = msg.getData().getString("messagecode");
				Log.d("得到的code", codeMsg);
				forgetPwd_code_edt.setText(codeMsg);
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

	public void showProgressDialog(Context mContext, int type) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		UserVo forgetPwdUserVo = new UserVo();
		paramsMap = new HashMap<String, Object>();
		sign = 1; // sign 取值1指代“找回密码”
		Task mTask;

		switch (type) {
		case 0: // 找回密码-发送验证码

			forgetPwdUserVo.setMobilephone(old_phone);// 原手机号码
			forgetPwdUserVo.setSign(sign);

			paramsMap.put("forgetPwdUserVo", forgetPwdUserVo);
			mTask = new Task(TaskType.TS_forgetPwdGETCODE, paramsMap);
			MainService.newTask(mTask);
			break;
			
		case 1: // 找回密码-校验验证码

			forgetPwdUserVo.setPhone(old_phone);// 原手机号码
			forgetPwdUserVo.setCode(StringUtils.stringTolnt(codeString));// 验证码,字符串转int型
			forgetPwdUserVo.setSign(sign);

			paramsMap.put("forgetPwdUserVo", forgetPwdUserVo);
			mTask = new Task(TaskType.TS_forgetPwd_CheckCODE, paramsMap);
			MainService.newTask(mTask);
			break;
			
		}
	}

	@Override
	public void refresh(Object... param) {
		Intent mIntent = new Intent();
		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_forgetPwdGETCODE: // 找回密码-获取验证码
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "验证码已发送到手机", Toast.LENGTH_SHORT).show();
					} else if (result.equals("1")) {
						Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
			}
			break;

		case TaskType.TS_forgetPwd_CheckCODE: // 找回密码-检验验证码
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (Map<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) { //验证码通过-跳转到【重置密码】页面

						mIntent.setClass(this, TheResetPasswordActivity.class);
						mIntent.putExtra("mobilephone", old_phone);
						startActivityForResult(mIntent, 2);
						
					} else { // result不为 "0"
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
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 2 && resultCode == RESULT_OK) {
//
//			String phone = data.getExtras().getString("phone");// 重置成功后传回手机号
//			String newPwd = data.getExtras().getString("newPwd");// 重置成功后传回密码
//
//			Intent intent = new Intent();
//			Bundle bundle = new Bundle();
//			intent.setClass(this, OwnerLoginActivity.class);
//			bundle.putString("phone", phone);
//			bundle.putString("newPwd", newPwd);
//			intent.putExtras(bundle);
//			
//			setResult(RESULT_OK, intent);
//			this.finish();// 关闭当前页面
//		}
//
//	}

}
