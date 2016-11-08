package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.Map;

import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.MD5Util;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 【修改密码】页面
 * **/

public class TheModifyPasswordActivity extends BaseActivity {
	private ImageView return_imgbtn;// 返回
	private EditText mpOldPassword; // 原密码
	private String oldPwd;
	private String oldPwdMD5;
	private EditText mpNewPassword; // 新密码
	private String newPwd;
	private String newPwdMD5;
	private EditText mpVerifyPassword; // 确认新密码
	private String VerifynewPwd;
	private TextView forget_old_password_tv; // 忘记了原密码
	private Button save_new_password_btn;

	protected Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_modify_password);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		return_imgbtn = (ImageView) findViewById(R.id.return_imgbtn);
//		close_img = (ImageButton) findViewById(R.id.close_img);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		mpOldPassword = (EditText) findViewById(R.id.mpOldPassword);
		mpNewPassword = (EditText) findViewById(R.id.mpNewPassword);
		mpVerifyPassword = (EditText) findViewById(R.id.mpVerifyPassword);

		forget_old_password_tv = (TextView) findViewById(R.id.forget_old_password_tv);
		save_new_password_btn = (Button) findViewById(R.id.save_new_password_btn);

	}

	@Override
	protected void initView() {
		return_imgbtn.setVisibility(View.VISIBLE);
		tvTitle.setText("修改密码");

		// 获取配置中的手机号
		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();
		} else { // userInfo对象不为空，则从里面取ID
			id = userInfo.getId(); // 用户Id

		}

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		forget_old_password_tv.setOnClickListener(this);
		save_new_password_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn: // 关闭
			this.finish();
			break;
		case R.id.save_new_password_btn: // 保存新密码
			if (validateData()) {

				showProgressDialog(this, 0);
			}
			break;
		case R.id.forget_old_password_tv: // 忘记密码
			mIntent.setClass(this, TheFindPasswordActivity.class);
			startActivity(mIntent);
			this.finish(); // 跳转后关闭当前页面
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean validateData() {
		oldPwd = mpOldPassword.getText().toString();
		newPwd = mpNewPassword.getText().toString();
		VerifynewPwd = mpVerifyPassword.getText().toString();
		boolean flag = true;
		// 同样判断输入框不能为空
		if (TextUtils.isEmpty(oldPwd)) {
			Toast.makeText(getApplicationContext(), "原密码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (TextUtils.isEmpty(newPwd)) {
			Toast.makeText(getApplicationContext(), "新密码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (TextUtils.isEmpty(VerifynewPwd)) {
			Toast.makeText(getApplicationContext(), "确认新密码不能为空", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		// 比较密码输入情况：新密码和其确认密码是否一致
		if ((oldPwd != null && !oldPwd.equals("")) && (newPwd != null && !newPwd.equals("")) && (VerifynewPwd != null && !VerifynewPwd.equals(""))) {
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

		UserVo modifyPWDUserVo = new UserVo();
		paramsMap = new HashMap<String, Object>();
		Task mTask;
		switch (type) {
		case 0: // 修改密码

			// 密码加密后，传给后台
			oldPwdMD5 = MD5Util.MD5Encode(oldPwd);
			newPwdMD5 = MD5Util.MD5Encode(newPwd);

			modifyPWDUserVo.setPassword(oldPwdMD5);
			modifyPWDUserVo.setNewPassword(newPwdMD5);
			modifyPWDUserVo.setId(id);

			paramsMap.put("modifyPWDUserVo", modifyPWDUserVo);
			mTask = new Task(TaskType.TS_modifyPWD, paramsMap);
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
		case TaskType.TS_modifyPWD: // 修改密码
			mDialog.dismiss();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (HashMap<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
						this.finish();
					} else if (result.equals("1")) {
						Toast.makeText(getApplicationContext(), "密码修改出错", Toast.LENGTH_SHORT).show();
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