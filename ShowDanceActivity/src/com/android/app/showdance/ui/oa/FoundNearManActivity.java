package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.FoundNearManAdapter;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 发现-【附近舞友】
 **/

public class FoundNearManActivity extends BaseActivity {
	private ListView found_near_man_lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_near_man);
		findViewById();
		initView();
		setOnClickListener();
		
		found_near_man_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				intent.setClass(FoundNearManActivity.this, FoundNearManDetail.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		found_near_man_lv = (ListView) findViewById(R.id.found_near_man_lv);

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
		
		map1.put("letter_sort", "A");
		map1.put("dance_man", "阿苏");
		map1.put("dance_man_gender", "男");
		map1.put("isTeamHeader", "队长");
		map1.put("dance_team", "凤凰舞队");
		map1.put("team_starLevel", "3星级");
		map1.put("dance_man_distance", "100");
		
		map2.put("letter_sort", "B");
		map2.put("dance_man", "贝贝");
		map2.put("dance_man_gender", "男");
		map2.put("isTeamHeader", "");
		map2.put("dance_team", "绚丽舞队");
		map2.put("team_starLevel", "2星级");
		map2.put("dance_man_distance", "200");
		
		map3.put("letter_sort", "X");
		map3.put("dance_man", "西西");
		map3.put("dance_man_gender", "男");
		map3.put("isTeamHeader", "");
		map3.put("dance_team", "热火舞队");
		map3.put("team_starLevel", "4星级");
		map3.put("dance_man_distance", "300");
		
		map4.put("letter_sort", "Z");
		map4.put("dance_man", "紫紫");
		map4.put("dance_man_gender", "女");
		map4.put("isTeamHeader", "队长");
		map4.put("dance_team", "七彩舞队");
		map4.put("team_starLevel", "5星级");
		map4.put("dance_man_distance", "300");

		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		
		// 创建适配器对象
		FoundNearManAdapter FoundNearManAdapter = new FoundNearManAdapter(FoundNearManActivity.this,listItem);
		found_near_man_lv.setAdapter(FoundNearManAdapter);// 为ListView绑定适配器

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
