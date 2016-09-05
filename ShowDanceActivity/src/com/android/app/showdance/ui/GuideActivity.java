package com.android.app.showdance.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

/**
 * 
 * @ClassName: GuideActivity
 * @Description: 引导界面
 * @author maminghua
 * @date 2015-7-18 下午4:56:09
 * 
 */
public class GuideActivity extends Activity implements OnPageChangeListener, OnClickListener 
{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
//
//	// 显示导航页面的viewpager
//	private ViewPager guideViewPager;
//
//	// 页面适配器
//	private ViewPagerAdapter guideViewAdapter;
//
//	// 页面图片列表
//	private ArrayList<View> mViews;
//
//	// 图片资源，这里放入了3张图片，因为第四张图片已经在guide_content_view.xml中加载好了
//	// 一会直接添加这个文件就可以了。
//	private final int images[] = { R.drawable.guide_page1, R.drawable.guide_page2, R.drawable.guide_page3 };
//
//	// 底部导航的小点
//	private ImageView[] guideDots;
//
//	// 记录当前选中的图片
//	private int currentIndex;
//
//	// 开始按钮
//	private Button startBtn;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_guide);
//
//		initView();
//
//		initDot();
//
//		setOnClickListener();
//
//	}
//
//	// 初始化页面
//	private void initView() {
//		guideViewPager = (ViewPager) findViewById(R.id.guide_view_pager);
//		mViews = new ArrayList<View>();
//
//		for (int i = 0; i < images.length; i++) {
//			// 新建一个ImageView容器来放置图片。
//			ImageView iv = new ImageView(GuideActivity.this);
//			iv.setBackgroundResource(images[i]);
//
//			// 将容器添加到图片列表中
//			mViews.add(iv);
//		}
//
//		// 上面添加了三张图片了，还有一张放在guide_content_view.xml中，把这个页面也添加进来。
//		View view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_splash, null);
//		mViews.add(view);
//
//		// 现在为开始按钮找到对应的控件
//		startBtn = (Button) view.findViewById(R.id.start_btn);
//
//		// 现在用到页面适配器了
//		guideViewAdapter = new ViewPagerAdapter(mViews);
//
//		guideViewPager.setAdapter(guideViewAdapter);
//	}
//
//	// 初始化导航小点
//	private void initDot() {
//		// 找到放置小点的布局
//		LinearLayout layout = (LinearLayout) findViewById(R.id.guide_dots);
//
//		// 初始化小点数组
//		guideDots = new ImageView[mViews.size()];
//
//		// 循环取得小点图片，让每个小点都处于正常状态
//		for (int i = 0; i < mViews.size(); i++) {
//			guideDots[i] = (ImageView) layout.getChildAt(i);
//			guideDots[i].setSelected(false);
//		}
//
//		// 初始化第一个小点为选中状态
//		currentIndex = 0;
//		guideDots[currentIndex].setSelected(true);
//	}
//
//	// 页面更换时，更新小点状态
//	private void setCurrentDot(int position) {
//		if (position < 0 || position > mViews.size() - 1 || currentIndex == position) {
//			return;
//		}
//
//		guideDots[position].setSelected(true);
//		guideDots[currentIndex].setSelected(false);
//
//		currentIndex = position;
//	}
//
//	protected void setOnClickListener() {
//
//		guideViewPager.setOnPageChangeListener(this);
//
//		startBtn.setOnClickListener(this);
//	}
//
//	// 开始按钮的点击事件监听
//	@Override
//	public void onClick(View arg0) {
//
//		// 我们随便跳转一个页面
//		Intent intent = new Intent(GuideActivity.this, MainActivity.class);
//		startActivity(intent);
//		finish();
//
//		InitApplication.mSpUtil.setFirstStart("1");
//
//	}
//
//	// 添加页面更换监听事件，来更新导航小点的状态。
//	@Override
//	public void onPageSelected(int arg0) {
//		setCurrentDot(arg0);
//	}
//
//	@Override
//	public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//	}
//
//	@Override
//	public void onPageScrollStateChanged(int arg0) {
//
//	}
//
//}
