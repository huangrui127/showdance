package com.android.app.showdance.ui.oa;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.CityAdapter;
import com.android.app.showdance.adapter.SearchCityAdapter;
import com.android.app.showdance.db.CityDB;
import com.android.app.showdance.impl.EventHandler;
import com.android.app.showdance.model.City;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.BladeView;
import com.android.app.showdance.widget.BladeView.OnItemClickListener;
import com.android.app.showdance.widget.PinnedHeaderListView;

public class SelectCtiyActivity extends BaseActivity implements TextWatcher, OnClickListener, EventHandler {
	private EditText mSearchEditText;
	// private Button mCancelSearchBtn;
	private LinearLayout all_city;
	private ImageButton mClearSearchBtn;
	private View mCityContainer;
	private View mSearchContainer;
	private PinnedHeaderListView mCityListView;
	private BladeView mLetter;
	private ListView mSearchListView;
	private List<City> mCities;
	private SearchCityAdapter mSearchCityAdapter;
	private CityAdapter mCityAdapter;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<City>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;
	private CityDB mCityDB;
	private InitApplication mApplication;
	private InputMethodManager mInputMethodManager;

	private TextView mTitleTextView;
	private ImageView mBackBtn;
	private ProgressBar mTitleProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);
		findViewById();
		initView();
		setOnClickListener();
		initData();

	}
	
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		all_city= (LinearLayout) findViewById(R.id.all_city);
		mSearchEditText = (EditText) findViewById(R.id.search_edit);
		mClearSearchBtn = (ImageButton) findViewById(R.id.ib_clear_text);
		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = findViewById(R.id.search_content_container);
		mCityListView = (PinnedHeaderListView) findViewById(R.id.citys_list);
		mCityListView.setEmptyView(findViewById(R.id.citys_list_empty));
		mLetter = (BladeView) findViewById(R.id.citys_bladeview);
		
		mSearchListView = (ListView) findViewById(R.id.search_list);
		mSearchListView.setEmptyView(findViewById(R.id.search_empty));
	}
	
	@Override
	protected void initView() {
		String selectMyCtiy = getIntent().getStringExtra("selectMyCtiy");
		if(selectMyCtiy.equals("1")){
			all_city.setVisibility(View.GONE);
		}
		
		InitApplication.mListeners.add(this);
		mLetter.setVisibility(View.GONE);
		mSearchContainer.setVisibility(View.GONE);
		return_imgbtn.setVisibility(View.VISIBLE);
		tvTitle.setText("当前定位-"+InitApplication.mSpUtil.getLocationMyCity());
	}

	

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		mSearchEditText.addTextChangedListener(this);
		mClearSearchBtn.setOnClickListener(this);
		all_city.setOnClickListener(this);

		mLetter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mCityListView.setSelection(mIndexer.get(s));
				}
			}
		});
		
		mSearchListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mInputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
				return false;
			}
		});
		mCityListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				L.i(mCityAdapter.getItem(position).toString());
				startActivity(mCityAdapter.getItem(position));
			}
		});

		mSearchListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				L.i(mSearchCityAdapter.getItem(position).toString());
				startActivity(mSearchCityAdapter.getItem(position));
			}
		});
	}

	private void startActivity(City city) {
		Intent i = new Intent();
		i.putExtra("city", city);
		setResult(RESULT_OK, i);
		finish();
	}

	private void initData() {
		mApplication = InitApplication.getInstance();
//		mCityDB = mApplication.getCityDB();
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		if (mApplication.isCityListComplite()) {
//			mCities = mApplication.getCityList();
			mSections = mApplication.getSections();
			mMap = mApplication.getMap();
			mPositions = mApplication.getPositions();
			mIndexer = mApplication.getIndexer();

			mCityAdapter = new CityAdapter(SelectCtiyActivity.this, mCities, mMap, mSections, mPositions);
			mCityListView.setAdapter(mCityAdapter);
			mCityListView.setOnScrollListener(mCityAdapter);
			mCityListView.setPinnedHeaderView(LayoutInflater.from(SelectCtiyActivity.this).inflate(R.layout.city_list_group_item, mCityListView, false));
//			mTitleProgressBar.setVisibility(View.GONE);
			mLetter.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// do nothing
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mSearchCityAdapter = new SearchCityAdapter(SelectCtiyActivity.this, mCities);
		mSearchListView.setAdapter(mSearchCityAdapter);
		mSearchListView.setTextFilterEnabled(true);
		if (mCities.size() < 1 || TextUtils.isEmpty(s)) {
			mCityContainer.setVisibility(View.VISIBLE);
			mSearchContainer.setVisibility(View.INVISIBLE);
			mClearSearchBtn.setVisibility(View.GONE);
		} else {
			mClearSearchBtn.setVisibility(View.VISIBLE);
			mCityContainer.setVisibility(View.INVISIBLE);
			mSearchContainer.setVisibility(View.VISIBLE);
			mSearchCityAdapter.getFilter().filter(s);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// 如何搜索字符串长度为0，是否隐藏输入法
		// if(TextUtils.isEmpty(s)){
		// mInputMethodManager.hideSoftInputFromWindow(
		// mSearchEditText.getWindowToken(), 0);
		// }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:
			finish();
			break;
		case R.id.ib_clear_text:
			if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
				mSearchEditText.setText("");
				mInputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
			}
			break;
		case R.id.all_city:
			City city=new City();
			city.setProvince("全国");
			startActivity(city);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		InitApplication.mListeners.remove(this);
	}

	@Override
	public void onCityComplite() {
		// 城市列表加载完的回调函数
//		mCities = mApplication.getCityList();
//		mSections = mApplication.getSections();
		mMap = mApplication.getMap();
		mPositions = mApplication.getPositions();
		mIndexer = mApplication.getIndexer();

		mCityAdapter = new CityAdapter(SelectCtiyActivity.this, mCities, mMap, mSections, mPositions);
		mLetter.setVisibility(View.VISIBLE);
		mCityListView.setAdapter(mCityAdapter);
		mCityListView.setOnScrollListener(mCityAdapter);
		mCityListView.setPinnedHeaderView(LayoutInflater.from(SelectCtiyActivity.this).inflate(R.layout.city_list_group_item, mCityListView, false));
		// mActionBar.setProgressBarVisibility(View.INVISIBLE);
//		mTitleProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void onNetChange() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE);
//			T.showLong(this, R.string.net_err);
	}




	@Override
	public void refresh(Object... param) {
		
	}

	@Override
	protected boolean validateData() {
		return false;
	}
}
