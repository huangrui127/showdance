package com.android.app.showdance.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * 
 * @ClassName: ViewPagerAdapter
 * @Description: 欢迎页界面适配器
 * @author maminghua
 * @date 2015-7-18 下午5:15:04
 * 
 */

public class ViewPagerAdapter extends PagerAdapter {

	private final ArrayList<View> mViews;

	public ViewPagerAdapter(ArrayList<View> views) {
		mViews = views;
	}

	// 返回页面数目
	@Override
	public int getCount() {
		if (mViews != null) {
			return mViews.size();
		}
		return 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	// 初始化position位置的页面
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(mViews.get(position), 0);
		return mViews.get(position);
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	// 销毁position位置的界面
	@Override
	public void destroyItem(View view, int position, Object arg2) {
		((ViewPager) view).removeView(mViews.get(position));
	}
}
