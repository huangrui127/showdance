package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.PhotoAndAudioAdapter;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 附近舞友——舞友详情——【查看个人相册】页面
 * **/

public class FoundPersonalPhotoActivity extends BaseActivity{

	private ListView personal_photo_lv;
	private ImageView rl_head; //人物图像
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_personal_photo);
		findViewById();
		initView();
		setOnClickListener();
		
	}

	@Override
	protected void findViewById() {
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		personal_photo_lv = (ListView) findViewById(R.id.personal_photo_lv);
		rl_head = (ImageView) findViewById(R.id.rl_head);
	}

	@Override
	protected void initView() {
		return_imgbtn.setVisibility(View.VISIBLE);
		tvTitle.setText("张三");
		
		//初始化测试数据
		initData();
		
	}
	
	private void initData(){
		/**
		 * // ** ListView列表数据的显示 //
		 **/
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		HashMap<String, Object> map8 = new HashMap<String, Object>();
		HashMap<String, Object> map9 = new HashMap<String, Object>();
		HashMap<String, Object> map10 = new HashMap<String, Object>();

		map1.put("dayofmonth", "3月10号");
		map1.put("content", "武大樱花园全程孕育在一种粉色的氛围中，乘着好天气大家可以去赏赏樱花的美景");

		map2.put("dayofmonth", "3月11号");
		map2.put("content", "好天气出游看风景~");

		map3.put("dayofmonth", "3月13号");
		map3.put("content", "植物园五彩缤纷");

		map4.put("dayofmonth", "3月14号");
		map4.put("content", "海洋世界模拟海底神奇的世界");
		
		map5.put("dayofmonth", "3月17号");
		map5.put("content", "福兮祸所依，祸兮福所伏");

		map6.put("dayofmonth", "3月18号");
		map6.put("content", "武大樱花园全程孕育在一种粉色的氛围中，乘着好天气大家可以去赏赏樱花的美景");

		map7.put("dayofmonth", "3月19号");
		map7.put("content", "好天气出游看风景~");

		map8.put("dayofmonth", "3月20号");
		map8.put("content", "植物园五彩缤纷");

		map9.put("dayofmonth", "3月21号");
		map9.put("content", "海洋世界模拟海底神奇的世界");
		
		map10.put("dayofmonth", "3月22号");
		map10.put("content", "福兮祸所依，祸兮福所伏");
		
		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		listItem.add(map5);
		listItem.add(map6);
		listItem.add(map7);
		listItem.add(map8);
		listItem.add(map9);
		listItem.add(map10);

		// 创建适配器对象
		PhotoAndAudioAdapter photoAndAudioAdapter = new PhotoAndAudioAdapter(this, listItem);
		personal_photo_lv.setAdapter(photoAndAudioAdapter);// 为ListView绑定适配器
		
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		rl_head.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回键
			this.finish();
			break;
		case R.id.rl_head:// 点击"人物图像"——【舞友详细资料】页面
			mIntent.setClass(this, FoundNearManDetail.class);
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
