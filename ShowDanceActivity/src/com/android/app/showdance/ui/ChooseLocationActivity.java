package com.android.app.showdance.ui;

import com.android.app.showdance.adapter.ChooseCityAdapter;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseLocationActivity extends BaseActivity{

    private static final String TAG = "ChooseLocationActivity";
    
    private ListView mListView; // 城市选择列表
    private TextView mTitleTV; // 顶部bar标题
    private ChooseCityAdapter mChooseCityAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 主界面不显示标题栏
        setContentView(R.layout.activity_choose_location);
        initView();
        initListView();
        setOnClickListener();
    }
    
    private void initListView(){
        mChooseCityAdapter = new ChooseCityAdapter(InitApplication.getRealContext());
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        mListView.setDivider(null);
        mListView.setAdapter(mChooseCityAdapter);
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chooseLocation = mChooseCityAdapter.getItem(Integer.parseInt(id + ""));
                InitApplication.mSpUtil.setUserChooseLocationName(chooseLocation);
                close();
            }
        });
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:
            close();
            break;
        }
    }

    private void close(){
        L.i(TAG, "close()");
        this.finish();
        setResult(ConstantsUtil.CHOOSE_LOCATION);
    }
    
    @Override
    protected void findViewById() {
        
    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.tvTitle);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        mListView = (ListView) findViewById(R.id.id_location_listview);
        
        mTitleTV.setText("选择城市");
        return_imgbtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
    }

    @Override
    public void refresh(Object... param) {
        
    }

    @Override
    protected boolean validateData() {
        return false;
    }

}
