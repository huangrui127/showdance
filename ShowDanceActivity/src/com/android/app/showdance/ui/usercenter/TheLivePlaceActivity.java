package com.android.app.showdance.ui.usercenter;

import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.oa.PersonalMsgActivity;
import com.android.app.wumeiniang.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 【居住地修改】页面
 * **/

public class TheLivePlaceActivity extends BaseActivity {
	private EditText the_live_place_edt;
	private String live_place;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_live_place);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		save_tv = (TextView) findViewById(R.id.save_tv);
		the_live_place_edt = (EditText) findViewById(R.id.the_live_place_edt);
	}

	@Override
	protected void initView() {
		tvTitle.setText("居住地");
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
			live_place = the_live_place_edt.getText().toString();

			mIntent.setClass(this, PersonalMsgActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("live_place", live_place);
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
