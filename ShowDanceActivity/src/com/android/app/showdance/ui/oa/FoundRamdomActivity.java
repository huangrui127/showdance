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
import com.android.app.showdance.adapter.FoundRamdomAdapter;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 发现-附近舞友
 **/

public class FoundRamdomActivity extends BaseActivity {
	private ListView found_ramdom_lv;
	private TextView random_tv;
	private List<Map<String, Object>> listMapItem = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.found_ramdom);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		found_ramdom_lv = (ListView) findViewById(R.id.found_ramdom_lv);
		random_tv= (TextView) findViewById(R.id.random_tv);
	}

	@Override
	protected void initView() {
		tvTitle.setText("随机约舞");
		return_imgbtn.setVisibility(View.VISIBLE);	
		random_tv.setVisibility(View.VISIBLE);	
		
		//创建测试数据
		TestData();
		
		// 创建适配器对象
		FoundRamdomAdapter foundRamdomAdapter = new FoundRamdomAdapter(FoundRamdomActivity.this,listMapItem);
		found_ramdom_lv.setAdapter(foundRamdomAdapter);// 为ListView绑定适配器
	}
	
	
	private void TestData(){
		/**
		 * // ** ListView列表数据的显示 //
		 **/
		//List<Map<String, Object>> listMapItem = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		
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

		map5.put("image_picture", R.drawable.found_a);
		map5.put("letter_sort", "Z");
		map5.put("dance_man", "紫紫");
		map5.put("dance_team", "七彩舞队");
		map5.put("dance_music", "泡沫");
		
		map6.put("image_picture", R.drawable.found_a);
		map6.put("letter_sort", "Z");
		map6.put("dance_man", "紫紫");
		map6.put("dance_team", "七彩舞队");
		map6.put("dance_music", "泡沫");
		
		map7.put("image_picture", R.drawable.found_a);
		map7.put("letter_sort", "Z");
		map7.put("dance_man", "紫紫");
		map7.put("dance_team", "七彩舞队");
		map7.put("dance_music", "泡沫");
		
		listMapItem.add(map1);
		listMapItem.add(map2);
		listMapItem.add(map3);
		listMapItem.add(map4);
		listMapItem.add(map5);
		listMapItem.add(map6);
		listMapItem.add(map7);
		
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		found_ramdom_lv.setOnItemClickListener(mainItemClick);
	}
	
	// 处理列表的单击事件
		private AdapterView.OnItemClickListener mainItemClick = new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent mIntent = new Intent();
				mIntent.setClass(FoundRamdomActivity.this, FoundNearManDetail.class);
				startActivity(mIntent);		
				
			}
		};

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
