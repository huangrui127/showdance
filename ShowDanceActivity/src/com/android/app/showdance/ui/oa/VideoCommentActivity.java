package com.android.app.showdance.ui.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.VideoCommentAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.MediaCommentPageVo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;

/**
 * 视频详情—【评论】页面
 * **/

public class VideoCommentActivity extends Activity {
//	private ListView video_comment_lv;
//	private Button addComment_btn;
//	private TextView video_name_tv;
//
//	private int pageNo = 1;
//	private int pageSize = 30;
//	long mediaId;
//	long createUserId;
//	private String videoName;
//
//	private List<Map<String, Object>> list;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_video_comment);
//		findViewById();
//		initView();
//		setOnClickListener();
//
//	}
//
//	@Override
//	protected void findViewById() {
//		tvTitle = (TextView) findViewById(R.id.tvTitle);
//		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
//		video_comment_lv = (ListView) findViewById(R.id.video_comment_lv);
//		addComment_btn = (Button) findViewById(R.id.addComment_btn);
//		video_name_tv = (TextView) findViewById(R.id.video_name_tv);
//	}
//
//	@Override
//	protected void initView() {
//		tvTitle.setText("评论");
//		return_imgbtn.setVisibility(View.VISIBLE);
//		videoName = getIntent().getStringExtra("videoName");
//		video_name_tv.setText(videoName);
//
//		mediaId = getIntent().getLongExtra("mediaId", 0);
//		createUserId = getIntent().getLongExtra("createUserId", 0);
//		WS_getMediaCommentListByMediaId(this, mediaId);
//
//	}
//
//	@Override
//	protected void setOnClickListener() {
//		return_imgbtn.setOnClickListener(this);
//
//		addComment_btn.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.return_imgbtn:// 返回键
//			setResult(RESULT_OK);
//			finish();
//			break;
//		case R.id.addComment_btn:// 评论
//
//			if (createUserId != 0) {
//				Intent mIntent = new Intent();
//				mIntent.setClass(this, VideoPublishCommentActivity.class);
//				mIntent.putExtra("videoName", videoName);
//				mIntent.putExtra("createUserId", createUserId);
//				mIntent.putExtra("mediaId", mediaId);
//				startActivityForResult(mIntent, 0);
//			} else {
//				Toast.makeText(getApplicationContext(), "请登录后发表评论!", Toast.LENGTH_SHORT).show();
//			}
//
//			break;
//
//		}
//	}
//
//	@Override
//	public void refresh(Object... param) {
//
//		int type = (Integer) param[0];
//		switch (type) {
//		case TaskType.TS_GETMEDIACOMMENTLIST: // 获取视频评论
//			mDialog.cancel();
//			if (ConstantsUtil.NetworkStatus) {
//				list = (ArrayList<Map<String, Object>>) param[1];
//
//				if (list != null && list.size() != 0) {
//					String pageNo = list.get(0).get("pageNo").toString();
//					String pageSize = list.get(0).get("pageSize").toString();
//					String autoCount = list.get(0).get("autoCount").toString();
//					String totalCount = list.get(0).get("totalCount").toString();
//					String totalPage = list.get(0).get("totalPage").toString();
//					String firstPage = list.get(0).get("firstPage").toString();
//					String lastPage = list.get(0).get("lastPage").toString();
//					String commentNum = list.get(0).get("commentNum").toString(); // 评论数量
//					tvTitle.setText("评论" + "(" + commentNum + ")"); // 标题上显示评论数量
//
//					list.remove(0);
//
//					VideoCommentAdapter videoCommentAdapter = new VideoCommentAdapter(VideoCommentActivity.this, list);// 创建适配器
//					video_comment_lv.setAdapter(videoCommentAdapter);
//
//				} else {
//				}
//
//			}
//			break;
//
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	protected boolean validateData() {
//
//		return false;
//	}
//
//	/**
//	 * 
//	 * 调用接口 "获取视频评论"（）
//	 */
//	public void WS_getMediaCommentListByMediaId(Context mContext, long mediaId) {
//		mDialog = new AlertDialog.Builder(mContext).create();
//
//		mDialog.show();
//		// 注意此处要放在show之后 否则会报异常
//		mDialog.setContentView(R.layout.loading_progressbar_dialog);
//		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
//
//		// 接口参数
//		MediaCommentPageVo mediaCommentPageVo = new MediaCommentPageVo();
//		paramsMap = new HashMap<String, Object>();
//		mediaCommentPageVo.setMediaId(mediaId);
//		mediaCommentPageVo.setPageNo(pageNo);
//		mediaCommentPageVo.setPageSize(pageSize);
//
//		paramsMap.put("mediaCommentPageVo", mediaCommentPageVo);
//		Task mTask = new Task(TaskType.TS_GETMEDIACOMMENTLIST, paramsMap);
//		MainService.newTask(mTask);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if (requestCode == 0 && resultCode == RESULT_OK) {
//			WS_getMediaCommentListByMediaId(this, mediaId);
//		}
//	}
//	
//
//	// 改写物理按键——返回的逻辑
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			setResult(RESULT_OK);
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

}
