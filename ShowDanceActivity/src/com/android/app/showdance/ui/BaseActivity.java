package com.android.app.showdance.ui;

import java.util.HashMap;

import com.android.app.showdance.logic.MainService;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: Activity基类
 * @author maminghua
 * @date 2014-12-1 上午10:39:54
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	protected ImageButton close_img; //关闭
	protected TextView cancel_tv; //取消
	protected TextView save_tv; //确定（保存）
	protected TextView tvTitle; 
	protected ImageButton btnReturn;
	protected ImageButton return_imgbtn; //返回键
	protected Dialog mDialog;
	protected InputMethodManager imm;
	
	//"正在加载"对话框,提示信息TextView的设置
	protected View view;
	protected LayoutInflater inflater;
	protected TextView tvLoading;
	
	protected String Tag = "BaseActivity";
	
	protected int refreshType;
	
	protected HashMap<String, Object> paramsMap;
	
	/**
	 * 初始化广播接收器
	 */
	private InternalReceiver internalReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		MainService.addActivity(this);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(Tag, "启动destroy");
		MainService.removeActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(Tag, "启动start");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(Tag, "启动ReStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
		MobclickAgent.onResume(this);
		Log.i(Tag, "启动resume");
		String[] actions = initActions();
		if (actions !=null) {
			registerReceiver(actions);
		}
	}

	protected String[] initActions() {
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
		MobclickAgent.onPause(this);
		if (internalReceiver != null) {
			unregisterReceiver(internalReceiver);
		}
		Log.i(Tag, "启动pause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(Tag, "启动stop");
	}

	/**
	 * 
	 * @Description:绑定控件id
	 * @param
	 * @return void
	 */
	protected abstract void findViewById();

	/**
	 * 
	 * @Description:初始化View
	 * @param
	 * @return void
	 */
	protected abstract void initView();

	/**
	 * 
	 * @Description:设置触发事件
	 * @param
	 * @return void
	 */

	protected abstract void setOnClickListener();

	/**
	 * 
	 * @Description:刷新事件
	 * @param param
	 * @return void
	 */
	public abstract void refresh(Object... param);

	/**
	 * 
	 * @Description:数据验证
	 * @param
	 * @return void
	 */
	protected abstract boolean validateData();
	
	/**
	 * 
	 * @Description:注册广播
	 * @param actionArray
	 * @return void
	 */
	private void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter();
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}
		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}

	/**
	 * 
	 * @ClassName: InternalReceiver
	 * @Description: 接收广播
	 * 
	 */
	private class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || intent.getAction() == null) {
				return;
			}
			handleReceiver(context, intent);
		}
	}

	/**
	 * 
	 * @Description:接收广播后处理广播
	 * @param context
	 * @param intent
	 * @return void
	 */
	protected void handleReceiver(Context context, Intent intent) {
		// 广播处理
	}
	
	/**
	 * 
	 * @Description:公用显示toast方法
	 * @param context
	 * @param resId
	 * @return void
	 */
	public static void makeToast(Context context, int resId) {
		if (context == null) {
			return;
		}

		String str = context.getResources().getString(resId);

		if (str == null || "".equals(str)) {
			return;

		}

		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

	}
	
	/**
	 * @Description:隐藏软键盘
	 * @param
	 * @return void
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 
	 * @Description:
	 * @param isShowSoft
	 * @param editText
	 * @return void
	 */
	protected void hideOrShowSoftInput(boolean isShowSoft, EditText editText) {
		if (isShowSoft) {
			imm.showSoftInput(editText, 0);
		} else {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}

	/**
	 * 
	 * @Description:通过类名启动Activity
	 * @param pClass
	 * @return void
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 
	 * @Description:通过类名启动Activity，并且含有Bundle数据
	 * @param pClass
	 * @param pBundle
	 * @return void
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 
	 * @Description:通过Action启动Activity
	 * @param pAction
	 * @return void
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 
	 * @Description:通过Action启动Activity，并且含有Bundle数据
	 * @param pAction
	 * @param pBundle
	 * @return void
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

}
