package com.android.app.showdance.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

/**
 * 
 * @ClassName: WelcomeActivity
 * @Description: 欢迎页
 * @author maminghua
 * @date 2015-7-18 下午04:50:38
 * 
 */
public class WelcomeActivity extends Activity {
	private final int longTime = 3000; // 延迟3秒

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				// 第一次进入的页面
				String firstStart = InitApplication.mSpUtil.getFirstStart();
				Intent intent = new Intent();
				if (firstStart.equals("1")) {
					intent.setClass(WelcomeActivity.this, MainActivity.class);
				} else {
					intent.setClass(WelcomeActivity.this, GuideActivity.class);
				}
				startActivity(intent);
				finish();

			}
		}, longTime);
	}

}
