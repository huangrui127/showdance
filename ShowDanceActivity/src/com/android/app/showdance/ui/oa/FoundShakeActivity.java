package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.FoundShakeAdapter;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 发现-附近舞友
 **/

public class FoundShakeActivity extends BaseActivity {
	private ListView found_shake_lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.found_shake);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		found_shake_lv = (ListView) findViewById(R.id.found_shake_lv);

	}

	@Override
	protected void initView() {
		tvTitle.setText("附近舞友");
		return_imgbtn.setVisibility(View.VISIBLE);
		

		/**
		 * // ** ListView列表数据的显示 //
		 **/
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		
		map1.put("image_picture", R.drawable.found_a);
		map1.put("letter_sort", "A");
		map1.put("dance_man", "阿苏");
		map1.put("dance_team", "凤凰舞队");
		map1.put("dance_music", "凤凰传奇");
		
		map2.put("image_picture", R.drawable.found_a);
		map2.put("letter_sort", "B");
		map2.put("dance_man", "贝贝");
		map2.put("dance_team", "绚丽舞队");
		map2.put("dance_music", "最炫民族风");
		
		map3.put("image_picture", R.drawable.found_a);
		map3.put("letter_sort", "X");
		map3.put("dance_man", "西西");
		map3.put("dance_team", "热火舞队");
		map3.put("dance_music", "小苹果");
		
		map4.put("image_picture", R.drawable.found_a);
		map4.put("letter_sort", "Z");
		map4.put("dance_man", "紫紫");
		map4.put("dance_team", "七彩舞队");
		map4.put("dance_music", "泡沫");

		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		
		// 创建适配器对象
		FoundShakeAdapter foundShakeAdapter = new FoundShakeAdapter(FoundShakeActivity.this,listItem);
		found_shake_lv.setAdapter(foundShakeAdapter);// 为ListView绑定适配器

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回
			this.finish();
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
