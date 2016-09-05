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
import com.android.app.showdance.adapter.FoundNearDanceTeamAdapter;
import com.android.app.showdance.adapter.IfocusedTheDanceTeamAdapter;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 我关注的舞队
 * **/

public class IfocusedTheDanceTeamActivity extends BaseActivity{
	
	private ListView found_near_dance_team_lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_i_focused_the_dance_team);
		findViewById();
		initView();
		setOnClickListener();
		
		found_near_dance_team_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				intent.setClass(IfocusedTheDanceTeamActivity.this, FoundNearDanceTeamDetail.class);
				startActivity(intent);
			
			}
		});
	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		found_near_dance_team_lv = (ListView) findViewById(R.id.found_near_dance_team_lv);
	}

	@Override
	protected void initView() {
		tvTitle.setText("我关注的舞队");
		return_imgbtn.setVisibility(View.VISIBLE);
		
		/**
		 * // ** ListView列表数据的显示 //
		 **/
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		Map<String, Object> map4 = new HashMap<String, Object>();
		
		map1.put("dance_team_name", "凤凰舞队");
		map1.put("dance_team_starLevel", "普通舞队LV3");
		map1.put("dance_team_header", "张山");
		map1.put("team_num", "10"); //队员数
		map1.put("dance_team_fansNum", "12"); // 粉丝数
		map1.put("dance_team_distance", "100");
		map1.put("dance_team_sign", "人生莫过于三件事，吃饭睡觉广场舞！");
		
		map2.put("dance_team_name", "传奇舞队");
		map2.put("dance_team_starLevel", "普通舞队LV2");
		map2.put("dance_team_header", "李四");
		map2.put("team_num", "9");
		map2.put("dance_team_fansNum", "13");
		map2.put("dance_team_distance", "200");
		map2.put("dance_team_sign", "开心跳舞！");
		
		map3.put("dance_team_name", "七彩舞队");
		map3.put("dance_team_starLevel", "普通舞队LV3");
		map3.put("dance_team_header", "王五");
		map3.put("team_num", "8");
		map3.put("dance_team_fansNum", "13");
		map3.put("dance_team_distance", "300");
		map3.put("dance_team_sign", "锻炼身体！");
		
		map4.put("dance_team_name", "炫动舞队");
		map4.put("dance_team_starLevel", "普通舞队LV4");
		map4.put("dance_team_header", "大白");
		map4.put("team_num", "15"); 
		map4.put("dance_team_fansNum", "14");
		map4.put("dance_team_distance", "400");
		map4.put("dance_team_sign", "周末斗舞走起！");
		
		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);
		
		// 创建适配器对象
		IfocusedTheDanceTeamAdapter ifocusedTheDanceTeamAdapter = new IfocusedTheDanceTeamAdapter(IfocusedTheDanceTeamActivity.this,listItem);
		found_near_dance_team_lv.setAdapter(ifocusedTheDanceTeamAdapter);// 为ListView绑定适配器
		
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
