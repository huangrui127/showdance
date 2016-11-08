package com.android.app.showdance.ui.oa;

import java.util.HashMap;
import java.util.Map;

import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.MediaComment;
import com.android.app.showdance.model.User;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.wumeiniang.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: VideoPublishCommentActivity
 * @Description: 视频详情【发表评论】
 * @author maminghua
 * @date 2015-6-2 下午05:28:03
 * 
 */
public class VideoPublishCommentActivity extends BaseActivity {
	private EditText comment_content_et;

	private String comment;
	long mediaId;
	long createUserId;

	private TextView video_name_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_comment);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		save_tv = (TextView) findViewById(R.id.save_tv);
		comment_content_et = (EditText) findViewById(R.id.comment_content_et);
		video_name_tv = (TextView) findViewById(R.id.video_name_tv);
	}

	@Override
	protected void initView() {
		tvTitle.setText("发表新评论");
		return_imgbtn.setVisibility(View.VISIBLE);
		save_tv.setVisibility(View.VISIBLE);
		save_tv.setText("发表");

		String videoName = getIntent().getStringExtra("videoName");
		video_name_tv.setText(videoName);

		mediaId = getIntent().getLongExtra("mediaId", 0);
		createUserId = getIntent().getLongExtra("createUserId", 0);

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		save_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回键
			finish();
			break;
		case R.id.save_tv:// 评论
			if (validateData()) {
				WS_saveMediaComment(this);
				hideSoftInputView();
			}
			break;

		}
	}

	@Override
	protected boolean validateData() {
		comment = comment_content_et.getText().toString();

		boolean flag = true;
		if (TextUtils.isEmpty(comment)) {
			Toast.makeText(getApplicationContext(), "评论不能为空!", Toast.LENGTH_SHORT).show();
			flag = false;
		}

		return flag;
	}

	/**
	 * 
	 * 调用接口 "评论视频"（）
	 */
	public void WS_saveMediaComment(Context mContext) {
		mDialog = new AlertDialog.Builder(mContext).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
		User user = new User();
		user.setId(createUserId);

		MediaComment mediaComment = new MediaComment();
		paramsMap = new HashMap<String, Object>();
		mediaComment.setUser(user);
		mediaComment.setMediaId(mediaId);
		mediaComment.setRemark(comment);
		paramsMap.put("mediaComment", mediaComment);

		Task mTask = new Task(TaskType.TS_SAVEMEDIACOMMENT, paramsMap);
		MainService.newTask(mTask);
	}

	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_SAVEMEDIACOMMENT:
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (Map<String, Object>) param[1];
				if (map.size() != 0) {
					String result = map.get("result").toString();
					if (!result.equals("-1")) {
						if (result.equals("0")) {
							Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();

							comment_content_et.setText("");
							setResult(RESULT_OK);
							finish();

							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(comment_content_et.getWindowToken(), 0);

						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			}

			break;

		default:
			break;
		}

	}

}
