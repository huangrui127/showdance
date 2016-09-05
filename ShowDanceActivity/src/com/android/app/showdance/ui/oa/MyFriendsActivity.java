package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.FoundNearManAdapter;
import com.android.app.showdance.adapter.MyFriendsAdapter;
import com.android.app.showdance.adapter.MyLastChatAdapter;
import com.android.app.showdance.ui.BaseActivity;

/**
 * 聊天
 **/

public class MyFriendsActivity extends BaseActivity {
	private ListView myfiens_lv,chat_lv;
	private Button chat_btn,myfiens_btn;
	private List<Map<String, Object>> MyFriends = new ArrayList<Map<String, Object>>(20);
	private List<Map<String, Object>> LastChats = new ArrayList<Map<String, Object>>(20);

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friends);
		findViewById();
		initView();
		setOnClickListener();
		
		myfiens_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				intent.setClass(MyFriendsActivity.this, FoundNearManDetail.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		
		
		chat_btn = (Button) findViewById(R.id.chat_btn);
		myfiens_btn = (Button) findViewById(R.id.myfiens_btn);
		
		myfiens_lv = (ListView) findViewById(R.id.myfiens_lv);
		chat_lv = (ListView) findViewById(R.id.chat_lv);		

	}

	@Override
	protected void initView() {
		tvTitle.setText("聊天");
		
		//测试数据
		testData();	
		testData_chat();
		
		//我的舞友---- 创建适配器对象
		MyFriendsAdapter myFriendsAdapter = new MyFriendsAdapter(MyFriendsActivity.this,MyFriends);
		myfiens_lv.setAdapter(myFriendsAdapter);// 为ListView绑定适配器
		
		//最近聊天---- 创建适配器对象
		MyLastChatAdapter myLastChatAdapter = new MyLastChatAdapter(MyFriendsActivity.this,LastChats);
		chat_lv.setAdapter(myLastChatAdapter);// 为ListView绑定适配器
	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		chat_btn.setOnClickListener(this);
		myfiens_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回
			this.finish();
			break;

		case R.id.chat_btn://聊天
			myfiens_lv.setVisibility(View.GONE);
			chat_lv.setVisibility(View.VISIBLE);
			
			break;
			
		case R.id.myfiens_btn://我的舞友
			myfiens_lv.setVisibility(View.VISIBLE);
			chat_lv.setVisibility(View.GONE);
			break;
			
		default:
			break;
		}
		
	}
	
	//测试数据----我的舞友
	private void testData(){
		/**
		 * // ** ListView列表数据的显示 //
		 **/
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		
		map1.put("letter_sort", "A");
		map1.put("dance_man", "阿苏");
		map1.put("dance_man_gender", "男");
		map1.put("dance_team", "凤凰舞队");
		map1.put("dance_man_distance", "100");
		
		map2.put("letter_sort", "B");
		map2.put("dance_man", "贝贝");
		map2.put("dance_man_gender", "男");
		map2.put("dance_team", "绚丽舞队");
		map2.put("dance_man_distance", "200");
		
		map3.put("letter_sort", "X");
		map3.put("dance_man", "西西");
		map3.put("dance_man_gender", "男");
		map3.put("dance_team", "热火舞队");
		map3.put("dance_man_distance", "300");
		
		map4.put("letter_sort", "Z");
		map4.put("dance_man", "紫紫");
		map4.put("dance_man_gender", "女");
		map4.put("dance_team", "七彩舞队");
		map4.put("dance_man_distance", "300");

		MyFriends.add(map1);
		MyFriends.add(map2);
		MyFriends.add(map3);
		MyFriends.add(map4);
		MyFriends.add(map1);
		MyFriends.add(map2);
		MyFriends.add(map3);
		MyFriends.add(map4);
	}
	
	
	//测试数据----最近聊天
		private void testData_chat(){
			/**
			 * // ** ListView列表数据的显示 //
			 **/
			
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			HashMap<String, Object> map4 = new HashMap<String, Object>();
			
			map1.put("image_picture", R.drawable.found_a);
			map1.put("dance_man", "阿苏");
			map1.put("time_tv", "2015-01-12 13:34:34");
			map1.put("topic_tv", "最近在什么吗啊？");

			
			map2.put("image_picture", R.drawable.found_a);
			map2.put("dance_man", "贝贝");
			map2.put("time_tv", "2015-03-12 14:34:34");
			map2.put("topic_tv", "武汉2015年10件大事...");
			
			map3.put("image_picture", R.drawable.found_a);			
			map3.put("dance_man", "西西");
			map3.put("time_tv", "2015-02-12 14:34:34");
			map3.put("topic_tv", "祝你春节快乐，万事如意");
			
			map4.put("image_picture", R.drawable.found_a);			
			map4.put("dance_man", "紫紫");
			map4.put("time_tv", "2015-02-14 14:34:34");
			map4.put("topic_tv", "情人节快乐！");

			LastChats.add(map1);
			LastChats.add(map2);
			LastChats.add(map3);
			LastChats.add(map4);
		}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	protected boolean validateData() {
		return false;
	}

}
