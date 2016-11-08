package com.android.app.showdance.ui;

import com.android.app.showdance.fragment.CategoryFragment;
import com.android.app.showdance.fragment.NewVideoFragment;
import com.android.app.showdance.model.glmodel.AdPicAndIconInfo.Category;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.wumeiniang.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 功能：“首页”分类图标的主Activity
 * 
 * @author djd
 */
public class MainCategoryActivity extends BaseActivity {

    private static final String TAG = "MainCategoryActivity";

    private FragmentManager mFragmentManager;
    private TextView mTitleTV;
    private Bundle mBundle;
    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 主界面不显示标题栏
        setContentView(R.layout.activity_main_category);
        init();
        initView();
        setOnClickListener();
        changeTitleBarTitleString(getTitleString());
        // commitFragment(R.id.id_main_category_frame,
        // CategoryFragment.newInstance());
        openFragment(mCategory.getType()); // 根据传入的分类不同，打开不同的分类Fragment
    }

    private String getTitleString() {
        return mCategory.getName();
    }

    private void openFragment(int type) {
        switch (type) {
        case 0:
        case 1:
        case 2:
            L.d(TAG,"打开全国、省、市分类");
            commitFragment(R.id.id_main_category_frame, CategoryFragment.newInstance()); // 打开全国、省市分类
            break;
        case 3:
        case 4:
            L.d(TAG,"打开最新视频、待审核视频分类");
            commitFragment(R.id.id_main_category_frame, NewVideoFragment.newInstance()); // 打开最新视频、待审核视频分类
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:
            this.finish();
            sendBroadcast(new Intent(ConstantsUtil.ACTION_MAIN_CATEGORY_ACTIVITY));
            break;
        }
    }

    @Override
    protected void findViewById() {

    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        mBundle = getIntent().getExtras();
        mCategory = (Category) mBundle.get(ConstantsUtil.CATEGORY);
    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.tvTitle);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);

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

    private void changeTitleBarTitleString(String titleStr) {
        mTitleTV.setText(titleStr);
    }

    private void commitFragment(int containerViewId, Fragment fragment) {
        fragment.setArguments(mBundle);
        mFragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
    }

    // @Override
    // public void onBackPressed() {
    // openShowDanceMainActivity();
    // }
    //
    // private void openShowDanceMainActivity(){
    // startActivity(new
    // Intent(MainCategoryActivity.this,ShowDanceMainActivity.class));
    // overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    // }

}
