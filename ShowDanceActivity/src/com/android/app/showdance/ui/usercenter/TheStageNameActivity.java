package com.android.app.showdance.ui.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.oa.PersonalMsgActivity;

/**
 * 【艺名】修改界面
 * **/

public class TheStageNameActivity extends BaseActivity {

	private EditText stage_name_edt;
	private String stage_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_stage_name);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		save_tv = (TextView) findViewById(R.id.save_tv);
		stage_name_edt = (EditText) findViewById(R.id.stage_name_edt);
	}

	@Override
	protected void initView() {
		tvTitle.setText("艺名");
		cancel_tv.setVisibility(View.VISIBLE);
		save_tv.setVisibility(View.VISIBLE);
		
	}

	@Override
	protected void setOnClickListener() {
		cancel_tv.setOnClickListener(this);
		save_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.cancel_tv: // 取消
			this.finish();
			
			break;
		case R.id.save_tv: // 确定
			stage_name = stage_name_edt.getText().toString();
			
			mIntent.setClass(this, PersonalMsgActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("stage_name", stage_name); 
			mIntent.putExtras(bundle);
			setResult(RESULT_OK, mIntent);
			this.finish(); // 要调用finish()方法
			
			break;
		default:
			break;
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
