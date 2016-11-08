package com.android.app.showdance.ui.more;

import java.util.HashMap;
import java.util.Map;

import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.MemberFeedback;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

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

/*
 * 意见反馈
 */
public class OwnerFeedBack extends BaseActivity {
	private ImageButton return_imgbtn;// 返回
	private EditText content_et;// 意见输入框
	private TextView tv;
	private String content;//
	private Button submit_btn;// 提交宝贵意见
	final int MAX_LENGTH = 500;
	int Rest_Length = MAX_LENGTH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.owner_feed_back);
		findViewById();
		initView();
		setOnClickListener();
	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		content_et = (EditText) findViewById(R.id.content_et);
		tv = (TextView) findViewById(R.id.tv);
		submit_btn = (Button) findViewById(R.id.submit_btn);

	}

	@Override
	protected void initView() {
		tvTitle.setText("问题反馈");
		return_imgbtn.setVisibility(View.VISIBLE);
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		submit_btn.setOnClickListener(this);
		content_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (Rest_Length > 0) {
					Rest_Length = MAX_LENGTH - content_et.getText().length();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				tv.setText("还可以输入" + Rest_Length + "个字");

			}

			@Override
			public void afterTextChanged(Editable s) {
				tv.setText("还可以输入" + Rest_Length + "个字");

			}
		});

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.return_imgbtn:// 返回
			this.finish();
			break;

		case R.id.submit_btn:

			if (validateData()) {
				hideSoftInputView();
				showProgressDialog(this);
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];
		Intent mIntent = new Intent();
		switch (type) {
		case TaskType.TS_SAVEMEMFEEDBACK: // 保存用户反馈
			mDialog.dismiss(); // 对话框关闭
			if (ConstantsUtil.NetworkStatus) {

				Map<String, Object> map = (Map<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "反馈成功！", Toast.LENGTH_SHORT).show();
						this.finish();
					} else {
						Toast.makeText(getApplicationContext(), "反馈失败！", Toast.LENGTH_SHORT).show();
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

	@Override
	protected boolean validateData() {
		content = content_et.getText().toString();
		boolean flag = true;
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(getApplicationContext(), "请填写您的宝贵意见", Toast.LENGTH_SHORT).show();
			flag = false;
		} else if (content.length() > 500) {
			Toast.makeText(getApplicationContext(), "最多字数500", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		return flag;

	}

	/**
	 * 调用接口 
	 */
	public void showProgressDialog(Context mContext) {
		mDialog = new AlertDialog.Builder(mContext).create();

		mDialog.show();
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true);

		MemberFeedback memberFeedBack = new MemberFeedback();
		paramsMap = new HashMap<String, Object>();

		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();
		} else {

		}
		Long id = userInfo.getId();

		memberFeedBack.setCreateUser(id);
		memberFeedBack.setFeedback(content);
		
		paramsMap.put("memberFeedBack", memberFeedBack);
		Task mTask = new Task(TaskType.TS_SAVEMEMFEEDBACK, paramsMap);
		MainService.newTask(mTask);

	}

}
