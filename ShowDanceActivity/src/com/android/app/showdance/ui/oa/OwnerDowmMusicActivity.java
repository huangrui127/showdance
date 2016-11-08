package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.app.showdance.adapter.OwnerDownMusicAdapter;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.wumeiniang.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 已下载舞曲
 **/

public class OwnerDowmMusicActivity extends BaseActivity {
	private ListView found_ramdom_lv;
	//private TextView random_tv;
	private List<Map<String, Object>> listMapItem = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.owner_downmusic);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		found_ramdom_lv = (ListView) findViewById(R.id.found_ramdom_lv);
		//random_tv= (TextView) findViewById(R.id.random_tv);
	}

	@Override
	protected void initView() {
		tvTitle.setText("已下载舞曲");
		return_imgbtn.setVisibility(View.VISIBLE);	
		//random_tv.setVisibility(View.VISIBLE);	
		
		//创建测试数据
		TestData();
		
		// 创建适配器对象
		OwnerDownMusicAdapter ownerDownMusicAdapter = new OwnerDownMusicAdapter(OwnerDowmMusicActivity.this,listMapItem);
		found_ramdom_lv.setAdapter(ownerDownMusicAdapter);// 为ListView绑定适配器
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
		map1.put("dance_man", "小苹果");
		map1.put("dance_team", "筷子兄弟");
		map1.put("dance_music", "3M");
		
		map2.put("image_picture", R.drawable.found_a);
		map2.put("letter_sort", "B");
		map2.put("dance_man", "最新民族风");
		map2.put("dance_team", "凤凰传奇");
		map2.put("dance_music", "3.5M");
		
		map3.put("image_picture", R.drawable.found_a);
		map3.put("letter_sort", "X");
		map3.put("dance_man", "月亮之上");
		map3.put("dance_team", "凤凰传奇");
		map3.put("dance_music", "2.5M");
		
		map4.put("image_picture", R.drawable.found_a);
		map4.put("letter_sort", "Z");
		map4.put("dance_man", "十送红军");
		map4.put("dance_team", "凤凰传奇");
		map4.put("dance_music", "2.0M");

		map5.put("image_picture", R.drawable.found_a);
		map5.put("letter_sort", "Z");
		map5.put("dance_man", "十五的月亮");
		map5.put("dance_team", "董文华");
		map5.put("dance_music", "2.0M");
		
		map6.put("image_picture", R.drawable.found_a);
		map6.put("letter_sort", "Z");
		map6.put("dance_man", "青藏高原");
		map6.put("dance_team", "韩红");
		map6.put("dance_music", "3M");
		
		map7.put("image_picture", R.drawable.found_a);
		map7.put("letter_sort", "Z");
		map7.put("dance_man", "我和你");
		map7.put("dance_team", "刘欢");
		map7.put("dance_music", "3.24M");
		
		listMapItem.add(map1);
		listMapItem.add(map2);
		listMapItem.add(map3);
		listMapItem.add(map4);
		listMapItem.add(map5);
		listMapItem.add(map6);
		listMapItem.add(map7);
		
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
			//	mIntent.setClass(OwnerDowmMusicActivity.this, FoundNearManDetail.class);
			//	startActivity(mIntent);	
				
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
