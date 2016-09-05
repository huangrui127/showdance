package com.android.app.showdance.ui;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class UseIntroductionActivity extends BaseActivity  {
	WebView mVideoView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_introduce);
		findViewById();
		setOnClickListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void findViewById() {
		return_imgbtn = (ImageButton)findViewById(R.id.return_imgbtn);
		return_imgbtn.setVisibility(View.VISIBLE);
		tvTitle= (TextView)findViewById(R.id.tvTitle);
		Intent i = getIntent();
		String title = null;
		if (i != null) {
			title = i.getStringExtra("title");
		}
		if (TextUtils.isEmpty(title))
			title = "使用说明";
			tvTitle.setText(title);
		mVideoView = (WebView)findViewById(R.id.video);
//		
//		MediaController controller = new MediaController(this);
//		controller.setAnchorView(mVideoView);
//		mVideoView.setMediaController(controller);
	}

	@Override
	protected void initView() {
		
	}

@Override
protected void onPause() {
	super.onPause();
	mVideoView.onPause();
//	mVideoView.stopPlayback();
}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent i = getIntent();
		if(i == null)
		{
			finish();
			return;
		}
		Uri helpUrl = i.getData();
		if(helpUrl == null)
			return;
//		mVideoView.setVideoURI(helpUrl);
//		mVideoView.start();
//		Log.w("guolei","helpUrl.getPath() "+helpUrl.getPath() + " to stirng "+helpUrl.toString());
		mVideoView.loadUrl(helpUrl.toString());
		mVideoView.onResume();
	}
	
	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
	}

	@Override
	public void refresh(Object... param) {
		
	}

	@Override
	protected boolean validateData() {
		return false;
	}
	
}
