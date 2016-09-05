package com.android.app.wumeiniang.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.android.app.showdance.ui.BaseActivity;

/**
 * 
 * @ClassName: AppManager
 * @Description: App管理
 * @author maminghua
 * @date 2014-12-1 上午10:42:48
 * 
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 
	 * @Description:单一实例
	 * @param @return
	 * @return AppManager
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 
	 * @Description:添加Activity到堆栈
	 * @param activity
	 * @return void
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 
	 * @Description:获取当前Activity（堆栈中最后一个压入的）
	 * @param @return
	 * @return Activity
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 
	 * @Description:结束当前Activity（堆栈中最后一个压入的）
	 * @param
	 * @return void
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 
	 * @Description:结束指定的Activity
	 * @param activity
	 * @return void
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 
	 * @Description:结束指定类名的Activity
	 * @param cls
	 * @return void
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 
	 * @Description:结束所有Activity
	 * @param
	 * @return void
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 
	 * @Description:获得所有BaseActivity
	 * @param @return
	 * @return List<BaseActivity>
	 */
	public List<BaseActivity> getAllActivity() {
		ArrayList<BaseActivity> listActivity = new ArrayList<BaseActivity>();
		for (Activity activity : activityStack) {
			listActivity.add((BaseActivity) activity);
		}
		return listActivity;
	}

	/**
	 * 
	 * @Description:根据Activity名称返回指定的Activity
	 * @param name
	 * @param @return
	 * @return BaseActivity
	 */
	public BaseActivity getActivityByName(String name) {
		for (Activity baseAct : activityStack) {
			if (baseAct.getClass().getName().indexOf(name) >= 0) {
				return (BaseActivity) baseAct;
			}
		}
		return null;
	}

	/**
	 * 
	 * @Description:根据Activity名称返回指定的Activity
	 * @param name
	 * @param @return
	 * @return BaseActivity
	 */
	public BaseActivity getActivityByName(Class<?> pClass) {
		for (Activity baseAct : activityStack) {
			if (baseAct.getClass() == pClass) {
				return (BaseActivity) baseAct;
			}
		}
		return null;

	}

	/**
	 * 
	 * @Description:退出应用程序
	 * @param context
	 * @return void
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			// System.exit(0);
		} catch (Exception e) {
		}
	}

}
