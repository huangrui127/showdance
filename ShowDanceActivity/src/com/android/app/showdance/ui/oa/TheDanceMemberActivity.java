package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.TheDanceMemberAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.UserFansPageVo;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;

/**
 * 【舞队成员】、【我关注的舞友】、【我的粉丝】即“哪些人关注了我，亦即关注我的”
 * **/

public class TheDanceMemberActivity extends BaseActivity implements OnItemClickListener {

	private int flag;
	public static final int FLAG_MYFANS = 1;// 我的粉丝
	public static final int FLAG_MYATTENTION = 2;// 我的关注

	private GridView attention_GridView;
	private long createUser;

	private List<Map<String, Object>> list;
	private int pageNo = 1;
	private int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_dance_member);
		findViewById();
		initView();
		setOnClickListener();
	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		attention_GridView = (GridView) findViewById(R.id.attention_GridView);

	}

	@Override
	protected void initView() {
		return_imgbtn.setVisibility(View.VISIBLE);
		// 获取配置中的手机号
		UserInfo userInfo = InitApplication.mSpUtil.getUserInfo();
		if (userInfo == null) {
			userInfo = new UserInfo();

		} else { // userInfo对象不为空，则从里面取ID
			createUser = userInfo.getId(); // 用户Id
		}

		flag = getIntent().getFlags();
		switch (flag) {
		// 我的粉丝
		case FLAG_MYFANS:
			tvTitle.setText("我的粉丝");
			WS_getMyFans(this, createUser);
			break;
		case FLAG_MYATTENTION:
			tvTitle.setText("我的关注");
			WS_getMyAttention(this, createUser);
			break;
		}

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
		attention_GridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn: // 返回键
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void refresh(Object... param) {

		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_MYATTENTION: // 我的关注
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				list = (ArrayList<Map<String, Object>>) param[1];

				if (list != null && list.size() != 0) {
					String pageNo = list.get(0).get("pageNo").toString();
					String pageSize = list.get(0).get("pageSize").toString();
					String autoCount = list.get(0).get("autoCount").toString();
					String totalCount = list.get(0).get("totalCount").toString();
					String totalPage = list.get(0).get("totalPage").toString();
					String firstPage = list.get(0).get("firstPage").toString();
					String lastPage = list.get(0).get("lastPage").toString();
					list.remove(0);

					TheDanceMemberAdapter theDanceMemberAdapter = new TheDanceMemberAdapter(this, list); // 创建适配器
					attention_GridView.setAdapter(theDanceMemberAdapter);

				} else {
					makeToast(this, R.string.no_my_attention);
				}
			} else {
				makeToast(this, R.string.app_network);
			}
			break;

		case TaskType.TS_MYFANS: // 我的粉丝
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				list = (ArrayList<Map<String, Object>>) param[1];

				if (list != null && list.size() != 0) {
					String pageNo = list.get(0).get("pageNo").toString();
					String pageSize = list.get(0).get("pageSize").toString();
					String autoCount = list.get(0).get("autoCount").toString();
					String totalCount = list.get(0).get("totalCount").toString();
					String totalPage = list.get(0).get("totalPage").toString();
					String firstPage = list.get(0).get("firstPage").toString();
					String lastPage = list.get(0).get("lastPage").toString();
					list.remove(0);

					TheDanceMemberAdapter theDanceMemberAdapter = new TheDanceMemberAdapter(this, list); // 创建适配器
					attention_GridView.setAdapter(theDanceMemberAdapter);

				} else {
					makeToast(this, R.string.no_my_fans);
				}
			} else {
				makeToast(this, R.string.app_network);
			}
			break;

		default:
			break;
		}

	}


	@Override
	protected boolean validateData() {
		return false;
	}

	// GridView监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String createUserId = null;
		if (flag == FLAG_MYFANS) { // 我的粉丝
			createUserId = list.get(position).get("createUser").toString(); //1（createUser）关注了-我4，是我的粉丝。
		}

		if (flag == FLAG_MYATTENTION) { // 我的关注
			// 备注：createUser指代粉丝用户id、userId被关注用户id
			createUserId = list.get(position).get("userId").toString();// 我4-关注了1，则根据1（userId）去查它相应的信息。
			
		}
		Intent mIntent = new Intent();
		mIntent.setClass(this, FoundNearManDetail.class);
		mIntent.putExtra("createUserId", createUserId); 
		startActivity(mIntent);

	}

	/**
	 * 
	 * 调用接口 "我的粉丝"
	 */
	public void WS_getMyFans(Context mContext, long createUser) {
		mDialog = new AlertDialog.Builder(mContext).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
		UserFansPageVo mUserFansPageVo = new UserFansPageVo();
		paramsMap = new HashMap<String, Object>();
		mUserFansPageVo.setUserId(createUser);
		mUserFansPageVo.setPageNo(pageNo);
		mUserFansPageVo.setPageSize(ConstantsUtil.PageSize);
		paramsMap.put("mUserFansPageVo", mUserFansPageVo);

		// 添加到任务队列
		Task mTask = new Task(TaskType.TS_MYFANS, paramsMap);
		MainService.newTask(mTask);

	}

	/**
	 * 
	 * 调用接口 "我的关注"
	 */
	public void WS_getMyAttention(Context mContext, long createUser) {
		mDialog = new AlertDialog.Builder(mContext).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
		UserFansPageVo mUserFansPageVo = new UserFansPageVo();
		paramsMap = new HashMap<String, Object>();
		mUserFansPageVo.setCreateUser(createUser);
		mUserFansPageVo.setPageNo(pageNo);
		mUserFansPageVo.setPageSize(ConstantsUtil.PageSize);
		paramsMap.put("mUserFansPageVo", mUserFansPageVo);

		// 添加到任务队列
		Task mTask = new Task(TaskType.TS_MYATTENTION, paramsMap);
		MainService.newTask(mTask);

	}

}
