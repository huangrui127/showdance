package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.app.showdance.adapter.ShowCityAdapter;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.wumeiniang.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 【城市】页面
 * **/

public class ShowCityActivity extends BaseActivity {

	private LinearLayout hot_city_slot_name_ll1;
	private LinearLayout hot_city_slot_name_ll2;
	private ListView show_city_lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_city);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		close_img = (ImageButton) findViewById(R.id.close_img);
		show_city_lv = (ListView) findViewById(R.id.show_city_lv);
		hot_city_slot_name_ll1 = (LinearLayout) findViewById(R.id.hot_city_slot_name_ll1);
		hot_city_slot_name_ll2 = (LinearLayout) findViewById(R.id.hot_city_slot_name_ll2);
	}

	@Override
	protected void initView() {
		tvTitle.setText("选择城市");
		close_img.setVisibility(View.VISIBLE);
		
		/**
		 * // ** ListView列表数据的显示 //
		 **/
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();

		map1.put("city_letter_sort", "湖北");
		map1.put("city_name1", "武汉");
		map1.put("city_name2", "孝感");
		map1.put("city_name3", "襄阳");

		map2.put("city_letter_sort", "广东");
		map2.put("city_name1", "深圳");
		map2.put("city_name2", "广州");
		map2.put("city_name3", "珠海");

		map3.put("city_letter_sort", "湖南");
		map3.put("city_name1", "株洲");
		map3.put("city_name2", "长沙");
		map3.put("city_name3", "张家界");

		map4.put("city_letter_sort", "河南");
		map4.put("city_name1", "郑州");
		map4.put("city_name2", "洛阳");
		map4.put("city_name3", "信阳");

		listItem.add(map1);
		listItem.add(map2);
		listItem.add(map3);
		listItem.add(map4);

		// 创建适配器对象
		ShowCityAdapter showCityAdapter = new ShowCityAdapter(ShowCityActivity.this, listItem);
		show_city_lv.setAdapter(showCityAdapter);// 为ListView绑定适配器

	}

	@Override
	protected void setOnClickListener() {
		close_img.setOnClickListener(this);
		hot_city_slot_name_ll1.setOnClickListener(this);
		hot_city_slot_name_ll2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_img: //关闭
			this.finish();
			break;
		
		case R.id.hot_city_slot_name_ll1:
//			mIntent.setClass(this, );
//			startActivity(mIntent);
			break;
		case R.id.hot_city_slot_name_ll2:
//			mIntent.setClass(this, );
//			startActivity(mIntent);
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
