package com.android.app.showdance.ui;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.HomeActivity;
import com.android.app.wumeiniang.R;
import com.android.app.showdance.baidupush.Utils;
import com.android.app.showdance.logic.MainService;
import com.android.app.wumeiniang.ShowDanceActivity;
import com.android.app.wumeiniang.app.AppManager;
import com.android.app.wumeiniang.app.InitApplication;
/**
 * 
 * @ClassName: MainActivity
 * @Description: Tab页导航
 * @author maminghua
 * @date 2014-12-1 上午10:41:06
 * 
 */
@SuppressLint("ResourceAsColor")
public class MainActivity extends TabActivity implements OnClickListener   {

	private TabHost mTabHost;
	public static final String TAB_HomePage = "HomePage";
	public static final String TAB_Capture = "Capture";
	public static final String TAB_Owner = "Owner";

	private FrameLayout layoutHome;
	private FrameLayout layoutCapture;
	private FrameLayout layoutOwner;
	
	private ImageView img_home;
//	private ImageView img_capture;
	private ImageView img_owner;
	
	private TextView txt_home;
	private TextView txt_owner;
	
	private long exitTime = 0;

	@SuppressWarnings("deprecation")
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		startActivity(new Intent(this, ShowDanceActivity.class));
//		finish();
//		UmengUpdateAgent.setUpdateCheckConfig(false);
//		UmengUpdateAgent.setUpdateOnlyWifi(false);// 在非Wifi情况下也检测版本更新
//		UmengUpdateAgent.update(this);// 友盟更新
		
		// 保持屏幕常亮 不黑屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.main);
		
		mTabHost = this.getTabHost();
		findViewById();
		initTabWidget();
		setOnClickListener();
		// 启动系统服务
		if (!MainService.isrun) {
			Intent it = new Intent(this, MainService.class);
			this.startService(it);
		}

		int isCreated = InitApplication.mSpUtil.getIsCreated();
		if (isCreated != 0) {
			creatShortCut();
		}

//		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(MainActivity.this, "api_key"));

	}

	@SuppressWarnings("deprecation")
    private void findViewById() {
//		tabHost_radioGroup = (RadioGroup) findViewById(R.id.tabHost_radioGroup);
	    layoutHome = (FrameLayout) findViewById(R.id.frame_home);
	    layoutCapture = (FrameLayout) findViewById(R.id.frame_capture);
	    layoutOwner = (FrameLayout) findViewById(R.id.frame_owner);
	    
	    img_home = (ImageView) findViewById(R.id.img_home);
	    img_owner = (ImageView) findViewById(R.id.img_owner);
//	    img_capture = (ImageView) findViewById(R.id.img_)
	    
	    txt_home = (TextView) findViewById(R.id.txt_home);
	    txt_owner = (TextView) findViewById(R.id.txt_owner);
//	    txt_home.setTextColor(R.color.btn_select_bg);
//	    txt_owner.setTextColor(R.color.host_text_color_normal);
	    
	    txt_home.setTextColor(getResources().getColor(R.color.btn_select_bg));
	    txt_owner.setTextColor(getResources().getColor(R.color.host_text_color_normal));
	}

	/**
	 * 
	 * @Description:初始化TabWidget选项
	 * @param
	 * @return void
	 */
	private void initTabWidget() {
//		TabSpec ts1 = mTabHost.newTabSpec(TAB_HomePage).setIndicator(TAB_HomePage);
//		ts1.setContent(new Intent(MainActivity.this, HomePageActivity.class));
//		mTabHost.addTab(ts1);

		TabSpec ts3 = mTabHost.newTabSpec(TAB_HomePage).setIndicator(TAB_HomePage);
		// ts3.setContent(new Intent(MainActivity.this, ShowActivity.class));
		ts3.setContent(new Intent(MainActivity.this, HomeActivity.class));
		mTabHost.addTab(ts3);
		
		ts3 = mTabHost.newTabSpec(TAB_Capture).setIndicator(TAB_Capture);
		ts3.setContent(new Intent(MainActivity.this, ShowDanceActivity.class));
		mTabHost.addTab(ts3);

		ts3 = mTabHost.newTabSpec(TAB_Owner).setIndicator(TAB_Owner);
		ts3.setContent(new Intent(MainActivity.this, OwnerActivity.class));
		mTabHost.addTab(ts3);
	}
	
	/**
	 * 
	 * @Description:设置OnClick事件
	 * @param
	 * @return void
	 */
	private void setOnClickListener() {
//		tabHost_radioGroup.setOnCheckedChangeListener(this);
	    layoutHome.setOnClickListener(this);
	    layoutCapture.setOnClickListener(this);
	    layoutOwner.setOnClickListener(this);
	}
	
    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.frame_home:
                mTabHost.setCurrentTabByTag(TAB_HomePage);

                img_home.setImageResource(R.drawable.menu1_focus);
                img_owner.setImageResource(R.drawable.menu3);
                txt_home.setTextColor(getResources().getColor(R.color.btn_select_bg));
                txt_owner.setTextColor(getResources().getColor(R.color.host_text_color_normal));
                break;

            case R.id.frame_capture:
                mTabHost.setCurrentTabByTag(TAB_Capture);
                
                img_home.setImageResource(R.drawable.menu1);
                img_owner.setImageResource(R.drawable.menu3);
                txt_home.setTextColor(getResources().getColor(R.color.host_text_color_normal));
                txt_owner.setTextColor(getResources().getColor(R.color.host_text_color_normal));
                break;
            case R.id.frame_owner:
                mTabHost.setCurrentTabByTag(TAB_Owner);
                
                img_home.setImageResource(R.drawable.menu1);
                img_owner.setImageResource(R.drawable.menu3_focus);    
                txt_home.setTextColor(getResources().getColor(R.color.host_text_color_normal));
                txt_owner.setTextColor(getResources().getColor(R.color.btn_select_bg));
                break;
            default:
                break;
        }
    }

//	/**
//	 * Tab页点击设置标签
//	 */
//	@Override
//	public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//		switch (checkedId) {
//		// 【首页】
//		case R.id.tabHost_HomePage:
//			mTabHost.setCurrentTabByTag(TAB_HomePage);
//			break;
//
//		// 【舞曲】
//		// case tabHost_Music:
//		// mTabHost.setCurrentTabByTag(TAB_Orders);
//		// break;
//
//		// 【秀舞】
//		case R.id.tabHost_ShowDance:
//			mTabHost.setCurrentTabByTag(TAB_Member);
//			break;
//		case R.id.tabHost_VideoEditor:
//			mTabHost.setCurrentTabByTag(TAB_VideoEditor);
//			break;
//		// 【我的】菜单
//		case R.id.tabHost_Owner:
//			mTabHost.setCurrentTabByTag(TAB_Owner);
//			break;
//
//		}
//
//	}
    
//  OnClickListener l = new OnClickListener() {

//  public void onClick(View arg0) {
//      // TODO Auto-generated method stub
//      if (arg0 == layout1) {
//          tabHost.setCurrentTabByTag("1");
//
//          tab_home.setImageResource(R.drawable.tab_home_click);
//          tab_bang.setImageResource(R.drawable.tab_bang);
//      }

//    } else if (arg0 == layout2) {
//
//        tab_home.setImageResource(R.drawable.tab_home);
//        tab_bang.setImageResource(R.drawable.tab_bang_click);
//
//        tabHost.setCurrentTabByTag("2");
//
//    }

//  }

//};

	/**
	 * 
	 * @Description:创建快捷方式
	 * @param
	 * @return void
	 */
	private void creatShortCut() {
		Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		String title = getResources().getString(R.string.app_name);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.app_logo);
		addIntent.putExtra("duplicate", false);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		Intent loginIntent = new Intent(this, MainActivity.class);
		loginIntent.setAction("android.intent.action.MAIN");
		loginIntent.addCategory("android.intent.category.LAUNCHER");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, loginIntent);
		sendBroadcast(addIntent);
		InitApplication.mSpUtil.setIsCreated(0);

	}

	/**
	 * 监测系统返回键
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再点击一次退出秀舞吧!", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				AppManager.getAppManager().AppExit(MainActivity.this);
			}

			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	// 解绑
	private void unBindForApp() {
		// Push: 解绑
//		PushManager.stopWork(getApplicationContext());
	}
}
