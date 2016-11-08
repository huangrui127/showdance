package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.Map;

import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.MD5Util;
import com.android.app.wumeiniang.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 【重置密码】页面
 * **/

public class TheResetPasswordActivity extends BaseActivity {

	private Button btn_clear_search_text1;
	private Button btn_clear_search_text2;
	private EditText rpNewPassword_edt; // 新密码
	private String newPwd;
	private String newPwdMD5;
	private String mobilephone;
	private EditText rpVerifyNewPassword_edt; // 确认新密码
	private String VerifynewPwd;
	private Button resetpwd_save_new_password_btn; // 保存新密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_reset_password);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		btn_clear_search_text1 = (Button) findViewById(R.id.btn_clear_search_text1);
		btn_clear_search_text2 = (Button) findViewById(R.id.btn_clear_search_text2);
		rpNewPassword_edt = (EditText) findViewById(R.id.rpNewPassword_edt);
		rpVerifyNewPassword_edt = (EditText) findViewById(R.id.rpVerifyNewPassword_edt);
		resetpwd_save_new_password_btn = (Button) findViewById(R.id.resetpwd_save_new_password_btn);
	}

	@Override
	protected void initView() {
		tvTitle.setText("重置密码");
		return_imgbtn.setVisibility(View.VISIBLE);
		
		//找回密码-校验手机号页面传过来的手机号（也可以直接从配置文件中取）
		mobilephone = getIntent().getStringExtra("mobilephone");
		
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		btn_clear_search_text1.setOnClickListener(this);
		btn_clear_search_text2.setOnClickListener(this);
		resetpwd_save_new_password_btn.setOnClickListener(this);
		//新密码输入框
		rpNewPassword_edt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int textLength = rpNewPassword_edt.getText().length();
				if (textLength > 0) {
					btn_clear_search_text1.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text1.setVisibility(View.GONE);
				}
			}
		});
		//确认新密码输入框
		rpVerifyNewPassword_edt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int textLength = rpVerifyNewPassword_edt.getText().length();
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

		case R.id.btn_clear_search_text1: 
			rpNewPassword_edt.setText("");
			btn_clear_search_text1.setVisibility(View.GONE);
			
			break;

		case R.id.btn_clear_search_text2:
			rpVerifyNewPassword_edt.setText("");
			btn_clear_search_text2.setVisibility(View.GONE);
			
			break;

		case R.id.resetpwd_save_new_password_btn: // 保存新密码
			if (validateData()) {
				
				showProgressDialog(this , 0);
				hideSoftInputView();
			}
			
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean validateData() {
		boolean flag = true;
		newPwd = rpNewPassword_edt.getText().toString();
		VerifynewPwd = rpVerifyNewPassword_edt.getText().toString();
		
		if (TextUtils.isEmpty(newPwd)) {
			Toast.makeText(getApplicationContext(), "新密码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (TextUtils.isEmpty(VerifynewPwd)) {
			Toast.makeText(getApplicationContext(), "确认新密码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		// 比较密码输入情况：新密码和其确认密码是否一致
		if ((newPwd != null && !newPwd.equals("")) && (VerifynewPwd != null && !VerifynewPwd.equals(""))) {
			if (!newPwd.equals(VerifynewPwd)) {
				Toast.makeText(getApplicationContext(), "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
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

		UserVo forgetPwdUserVo = new UserVo();
		paramsMap = new HashMap<String, Object>();
		Task mTask;
		switch (type) {
		case 0: //重置密码
			
			// 密码加密后，传给后台
			newPwdMD5 = MD5Util.MD5Encode(newPwd);
			forgetPwdUserVo.setPassword(newPwdMD5);
			forgetPwdUserVo.setMobilephone(mobilephone); //原手机号

			paramsMap.put("forgetPwdUserVo", forgetPwdUserVo);
			mTask = new Task(TaskType.TS_forgetPwd_ModifyPwd, paramsMap);
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
		case TaskType.TS_forgetPwd_ModifyPwd: // 重置密码
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "重置密码成功", Toast.LENGTH_SHORT).show();
						//跳转
						Intent mIntent = new Intent();
						Bundle bundle = new Bundle();
						mIntent.setClass(this, TheFindPasswordActivity.class);
						bundle.putString("phone", mobilephone);
						bundle.putString("newPwd", newPwd);
						mIntent.putExtras(bundle);
						
						setResult(RESULT_OK, mIntent);
						this.finish();//重置密码成功后，跳转到【登录】页面后关闭当前页面
						
					} else if (result.equals("1")) {
						Toast.makeText(getApplicationContext(), "重置密码失败", Toast.LENGTH_SHORT).show();
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
