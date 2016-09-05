package com.android.app.showdance.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.app.showdance.ui.oa.FoundNearDanceTeamActivity;
import com.android.app.showdance.ui.oa.FoundNearManActivity;
import com.android.app.showdance.ui.oa.FoundRamdomActivity;
import com.android.app.showdance.ui.oa.FoundSearchDanceTeamActivity;
import com.android.app.showdance.ui.oa.FoundShakeActivity;
import com.android.app.wumeiniang.R;

/**
 * 发现
 */
public class FoundActivity extends BaseActivity {
	private int loginType = 0;// 登录状态 默认0未登录

	private LinearLayout radom_ll;// 
	private LinearLayout shake_ll;//
	private LinearLayout searchmusic_ll;//搜舞曲
	private LinearLayout searchDanceTeam_ll; //搜舞队 
	
	private LinearLayout NearDance_ll;// 
	private LinearLayout nearmen_ll;//


	private TextView address_tv;// 地址
	String cellName;
	String detailAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found);
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
		radom_ll = (LinearLayout) findViewById(R.id.radom_ll);
		shake_ll = (LinearLayout) findViewById(R.id.shake_ll);
		searchmusic_ll = (LinearLayout) findViewById(R.id.searchmusic_ll);
		searchDanceTeam_ll = (LinearLayout) findViewById(R.id.searchDanceTeam_ll);
		NearDance_ll = (LinearLayout) findViewById(R.id.NearDance_ll);
		nearmen_ll = (LinearLayout) findViewById(R.id.nearmen_ll);

	}

	/*
	 * 附默认值
	 */
	@Override
	protected void initView() {
		tvTitle.setText("发现");
	}

	/**
	 * 设置事件
	 */
	@Override
	protected void setOnClickListener() {
		radom_ll.setOnClickListener(this);
		shake_ll.setOnClickListener(this);
		searchmusic_ll.setOnClickListener(this);
		searchDanceTeam_ll.setOnClickListener(this);
		NearDance_ll.setOnClickListener(this);
		nearmen_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.searchmusic_ll:// 搜索舞曲
			mIntent.setClass(FoundActivity.this, FoundSearchMusicActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.searchDanceTeam_ll:// 搜舞队
			mIntent.setClass(FoundActivity.this, FoundSearchDanceTeamActivity.class);
			startActivity(mIntent);
			break;

		case R.id.shake_ll:// 摇一摇
			mIntent.setClass(FoundActivity.this, FoundShakeActivity.class);
			startActivity(mIntent);
			break;


		case R.id.radom_ll:// 随机约舞
			mIntent.setClass(FoundActivity.this, FoundRamdomActivity.class);
			startActivity(mIntent);
			break;

		case R.id.nearmen_ll:// 附近舞友
			mIntent.setClass(FoundActivity.this, FoundNearManActivity.class);
			startActivity(mIntent);
			break;
		case R.id.NearDance_ll:// 附近舞蹈队
			mIntent.setClass(FoundActivity.this, FoundNearDanceTeamActivity.class);
			startActivity(mIntent);
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
