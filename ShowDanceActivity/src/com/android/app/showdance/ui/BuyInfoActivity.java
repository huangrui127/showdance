package com.android.app.showdance.ui;

import com.android.app.wumeiniang.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class BuyInfoActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyinfo);
		initView();
	}
	
	private void initView() {
		ImageButton btn = (ImageButton)findViewById(R.id.close);
		btn.setOnClickListener(this);
		TextView text = (TextView)findViewById(R.id.buytips_two);
		text.setOnClickListener(this);
	}
	
	
	private void sendCallIntent(String number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ number));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			finish();
			break;
		case R.id.buytips_two:
			TextView text = (TextView) v;
			sendCallIntent(text.getText().toString());
			finish();
			break;
		default:
			break;
		}
	}
}
