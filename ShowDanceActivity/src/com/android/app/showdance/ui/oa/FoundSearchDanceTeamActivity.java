package com.android.app.showdance.ui.oa;

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

import com.android.app.wumeiniang.R;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 【搜舞队】页面
 * 
 * @date 2015-4-2 19:33
 * 
 */
public class FoundSearchDanceTeamActivity extends BaseActivity {

	private EditText mEdtInput;
	private Button btnSearch;
	private Button mBtnClearSearchText = null;

	private String keyword = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_search_dance_team);
		findViewById();
		initView();
		setOnClickListener();
	}

	@Override
	protected void findViewById() {
		mEdtInput = (EditText) findViewById(R.id.onlinedictionary_et);
		btnSearch = (Button) findViewById(R.id.SearchBtn);
		mBtnClearSearchText = (Button) findViewById(R.id.btn_clear_search_text);
		btnReturn = (ImageButton) findViewById(R.id.title_bar_return_imgbtn);
		tvTitle = (TextView) findViewById(R.id.title_bar_title_tv);
	}

	@Override
	protected void initView() {
		tvTitle.setText("搜舞队");

		mEdtInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int textLength = mEdtInput.getText().length();
				if (textLength > 0) {
					mBtnClearSearchText.setVisibility(View.VISIBLE);
				} else {
					mBtnClearSearchText.setVisibility(View.GONE);
				}
			}
		});
	}

	// Click
	@Override
	protected void setOnClickListener() {
		btnSearch.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
		mBtnClearSearchText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_return_imgbtn:
			finish();
			overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
			break;

		case R.id.SearchBtn:
			if (validateData()) {
			}
			break;
		case R.id.btn_clear_search_text:
			mEdtInput.setText("");
			mBtnClearSearchText.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean validateData() {
		keyword = mEdtInput.getText().toString().trim();
		boolean flag = true;
		if (TextUtils.isEmpty(keyword)) {
			Toast.makeText(this, "请输入舞队名称", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		return flag;
	}

	@Override
	public void refresh(Object... param) {

	}

}
