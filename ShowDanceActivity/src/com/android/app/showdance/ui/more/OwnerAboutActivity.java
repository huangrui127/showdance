package com.android.app.showdance.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.ui.BaseActivity;

/*关于*/
public class OwnerAboutActivity extends BaseActivity {
	private ImageButton return_imgbtn;// 返回

	private TextView weixin_tv;// 微信
	private TextView xinlang_tv;// 新浪
	private TextView company_url_tv;// 网址
	
	private String weixin = "秀舞吧";// 微信号
	private String weibo = "I dance,I happy!";// 微博
	private String mainPage = "www.tiaowuba.com";// 网站


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.owner_about);
		findViewById();
		initView();
		setOnClickListener();
	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);

		weixin_tv = (TextView) findViewById(R.id.weixin_tv);
		xinlang_tv = (TextView) findViewById(R.id.xinlang_tv);
		company_url_tv = (TextView) findViewById(R.id.company_url_tv);

	}

	@Override
	protected void initView() {
		tvTitle.setText("关于");
		return_imgbtn.setVisibility(View.VISIBLE);

		setabout();
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.return_imgbtn:
			this.finish();
			break;

		default:
			break;
		}
	}

	//设置数据到界面
	private void  setabout(){
		weixin_tv.setText(weixin);
		xinlang_tv.setText(weibo);
		company_url_tv.setText(mainPage);
	}
	
	
	@Override
	public void refresh(Object... param) {
	}

	@Override
	protected boolean validateData() {
		return false;
	}


}
