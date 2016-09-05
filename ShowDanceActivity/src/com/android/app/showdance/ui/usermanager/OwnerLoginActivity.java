package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.baidupush.Utils;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.User;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.VolleyBaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.MD5Util;

/**
 * 【登陆】页面
 * **/

public class OwnerLoginActivity extends VolleyBaseActivity implements OnCheckedChangeListener {

	private EditText owner_user_name_edt; // 秀舞吧号/手机号
	private String loginName; // 登录名 (手机号/吧号)
	private EditText owner_password_edt; // 密码
	private String password; // 密码
	private String passwordMD5; // 加密的密码
	private Button owner_login_btn; // 登录
	private CheckBox loginRememberPwd; // 记住密码
	private TextView owner_forget_old_password_tv; // 忘记密码
	private Button owner_register_btn; // 用手机号注册

	private Button btn_clear_search_text1; // 清空秀舞吧号/手机号
	private Button btn_clear_search_text2; // 清空密码

	private int forgetPassword = 0;// 忘记密码
	private int phoneRegister = 1;// 验证手机号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_owner_login);
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
		owner_user_name_edt = (EditText) findViewById(R.id.owner_user_name_edt);
		owner_password_edt = (EditText) findViewById(R.id.owner_password_edt);
		owner_login_btn = (Button) findViewById(R.id.owner_login_btn);
		owner_forget_old_password_tv = (TextView) findViewById(R.id.owner_forget_old_password_tv);
		owner_register_btn = (Button) findViewById(R.id.owner_register_btn);
		loginRememberPwd = (CheckBox) findViewById(R.id.loginRememberPwd); // 记住密码
	}

	@Override
	protected void initView() {
		tvTitle.setText("登录");
		return_imgbtn.setVisibility(View.VISIBLE);

		owner_user_name_edt.setText(InitApplication.mSpUtil.getUserName());
		// 判断【记住密码】多选框的状态
		if (InitApplication.mSpUtil.getRememberPwd()) { // 若配置文件中记住密码是【被选中】状态，则进行下面操作
			// 设置选中记住密码状态
			loginRememberPwd.setChecked(true);
			// 将配置文件中保存的用户名和密码，设置显示到登陆页面
			owner_password_edt.setText(InitApplication.mSpUtil.getPassword());

		}
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		btn_clear_search_text1.setOnClickListener(this);
		btn_clear_search_text2.setOnClickListener(this);
		owner_login_btn.setOnClickListener(this);
		owner_forget_old_password_tv.setOnClickListener(this);
		owner_register_btn.setOnClickListener(this);
		loginRememberPwd.setOnCheckedChangeListener(this); // CheckBox

		// 用户名文本输入情况
		owner_user_name_edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int textLength = owner_user_name_edt.getText().length();
				if (textLength > 0) {
					btn_clear_search_text1.setVisibility(View.VISIBLE);
				} else {
					btn_clear_search_text1.setVisibility(View.GONE);
				}
			}
		});

		// 密码文本输入情况
		owner_password_edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int textLength = owner_password_edt.getText().length();
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
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:
			this.finish();
			break;

		case R.id.btn_clear_search_text1: // 叉叉键1:清空输入框秀舞吧号/手机号
			owner_user_name_edt.setText("");
			btn_clear_search_text1.setVisibility(View.GONE);

			break;

		case R.id.btn_clear_search_text2: // 叉叉键2:清空输入框密码
			owner_password_edt.setText("");
			btn_clear_search_text2.setVisibility(View.GONE);
			break;

		case R.id.owner_login_btn: // 登录按钮
			if (validateData()) {
				hideSoftInputView();
				showProgressDialog(this, 0); // 登录
			}

			break;

		case R.id.owner_forget_old_password_tv: // 忘记了密码
			mIntent.setClass(this, TheFindPasswordActivity.class);
			startActivityForResult(mIntent, forgetPassword);
			break;

		case R.id.owner_register_btn: // 用手机号注册按钮——跳转到【注册】页面
			mIntent.setClass(this, OwnerPhoneRegisterActivity.class);
			startActivityForResult(mIntent, phoneRegister);
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean validateData() {
		loginName = owner_user_name_edt.getText().toString();
		password = owner_password_edt.getText().toString();

		boolean flag = true;
		// 判断输入框不能为空
		if (TextUtils.isEmpty(loginName)) {
			Toast.makeText(getApplicationContext(), "请输入手机号/秀舞吧号", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (TextUtils.isEmpty(password)) {
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		return flag;
	}

	public void showProgressDialog(Context mContext, int type) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		User loginUser = new User();
		paramsMap = new HashMap<String, Object>();
		Task mTask;
		switch (type) {
		case 0: // 登录
			// 密码加密后，传给后台
			passwordMD5 = MD5Util.MD5Encode(password);
			loginUser.setPassword(passwordMD5);

			loginUser.setLoginName(loginName);
			loginUser.setMobilephone(loginName);

			paramsMap.put("loginUser", loginUser);
			mTask = new Task(TaskType.TS_LoginUSER, paramsMap);
			MainService.newTask(mTask);
			break;

		default:
			break;
		}
	}

	@Override
	public void refresh(Object... param) {
		
	}

	// CheckBox的监听
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.loginRememberPwd: // 监听"记住密码"多选框按钮事件
			if (isChecked) {
				System.out.println("记住密码已选中");
				InitApplication.mSpUtil.setRememberPwd(true);
			} else {
				System.out.println("记住密码没有选中");
				InitApplication.mSpUtil.setRememberPwd(false);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == phoneRegister && resultCode == RESULT_OK) {

			String phone = data.getExtras().getString("phone");// 注册成功后传回手机号
			String password = data.getExtras().getString("password");// 注册成功传回密码

			owner_user_name_edt.setText(phone);
			owner_password_edt.setText(password);

		}

		if (requestCode == forgetPassword && resultCode == RESULT_OK) {

			String phone = data.getExtras().getString("phone");// 重置成功后传回手机号
			String newPwd = data.getExtras().getString("newPwd");// 重置成功后传回密码

			owner_user_name_edt.setText(phone);
			owner_password_edt.setText(newPwd);

		}

	}

}
