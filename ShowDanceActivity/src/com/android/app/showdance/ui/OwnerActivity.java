package com.android.app.showdance.ui;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.UploadedVideoAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.VolleyManager.ResponeListener;
import com.android.app.showdance.logic.event.SharedEvent;
import com.android.app.showdance.logic.event.UploadDeleteEvent;
import com.android.app.showdance.logic.event.UploadEvent;
import com.android.app.showdance.model.ShareInfo;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.VideoUploadInfo;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.DeleteResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowListResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.ShowResponse;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;
import com.android.app.showdance.ui.baidu.bvideo.VideoViewPlayingActivity;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: OwnerActivity
 * @Description: 我的
 * @author yangsiyang
 * @date 2015-4-7 下午5:04:10
 * 
 */
public class OwnerActivity extends VolleyBaseActivity {
	/**
	 * 主键ID
	 */
	private TextView stage_name_tv;// 艺名
	private SwipeListView mListView;
	private List<VideoUploadResponse> mVideoUploadList;
	private UploadedVideoAdapter mAdapter;
//	private String show_dance_id;


	private Button logout_btn;


	private User userInfo;

	// 1)首次进入onCreate()调用接口刷新为1,防止进入onResume()重复调用接口
	// 2)调用接口后设置为2,方便每次触发onResume()方法时调用接口;
	private int refeshState = 1;

	/**
	 * Activity恢复时执行 当Activity可以得到用户焦点的时候就会调用onResume方法
	 */
	
	@Override
	protected void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
		
		if (refeshState == 2) {

			// 获取配置中的手机号
			userInfo = InitApplication.mSpUtil.getUser();
			if (userInfo == null) {
				userInfo = new User();
			} else { // userInfo对象不为空，则从里面取ID
//				WS_getVUserFansMediaMusicCountById(this, createUser);
			}

		}
		WS_getMyVedio();
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}
	
	public void onEventMainThread(SharedEvent event) {
		if (userInfo == null) {
			Toast.makeText(getApplicationContext(), "请先登录后分享视频!", Toast.LENGTH_SHORT).show();
			return;
		}
		configPlatforms(event);
	}
	
	public void onEventMainThread(UploadDeleteEvent event) {
		if (userInfo == null) {
			Toast.makeText(getApplicationContext(), "请先登录后删除视频!", Toast.LENGTH_SHORT).show();
			return;
		}

		// 自定义有标题、有确定按钮与有取消按钮对话框使用方法
		final int id = event.id;
		CustomAlertDialog mCustomDialog = new CustomAlertDialog(OwnerActivity.this).builder(R.style.DialogTVAnimWindowAnim);
		mCustomDialog.setTitle("删除提示");
		mCustomDialog.setMsg("确认删除吗?");
		mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
			@Override
			public void onClick(View v) {
				VideoUploadInfo.DeleteRequest request = new VideoUploadInfo.DeleteRequest();
				request.setid(id);
				VolleyManager.getInstance().postRequest(
						request,
						BaseRequest.UPLOAD_DELETE_CLIENT_ID+"/"+InitApplication.mSpUtil.getUser().getuser_token()+"/videoDelete",
						new ResponeListener<VideoUploadInfo.DeleteResponse>(VideoUploadInfo.DeleteResponse.class) {
							@Override
							public void onMyResponse(DeleteResponse response) {
								WS_getMyVedio();
							}
							
						}, mErrorListener);
			}
		}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		}).show();

	
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_owner);
		findViewById();
		initView();
		// TODO move to register activity
		MobclickAgent.onProfileSignIn(userInfo.getPhone());
		setOnClickListener();
	}

	
	public void WS_getMyVedio() {
		mDialog = new AlertDialog.Builder(this).create();

		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

		// 接口参数
//		MediaInfoPageVo mediaInfoPageVo = new MediaInfoPageVo();
//		paramsMap = new HashMap<String, Object>();
////		mediaInfoPageVo.setCreateUser(createUser);
//		mediaInfoPageVo.setPageNo(pageNo);
//		mediaInfoPageVo.setPageSize(ConstantsUtil.PageSize);
//		paramsMap.put("mediaInfoPageVo", mediaInfoPageVo);
//
//		paramsMap.put("videoType", ConstantsUtil.videoType1);
//		Task mTask = new Task(TaskType.TS_GETMYVIDIO, paramsMap);
//		MainService.newTask(mTask);
		
		
		VideoUploadInfo.ShowRequest request = new VideoUploadInfo.ShowRequest();
		request.setpageNumber(Integer.MAX_VALUE);
		request.setuser_id(InitApplication.mSpUtil.getUser().getId());
		VolleyManager.getInstance().postRequest(
				request,
				BaseRequest.UPLOAD_CLIENT_ID+"/"+InitApplication.mSpUtil.getUser().getuser_token()+"/videoList",
				new OnResponseListener<VideoUploadInfo.ShowResponse>(VideoUploadInfo.ShowResponse.class) {
					@Override
					protected void handleResponse(ShowResponse response) {
						
						mVideoUploadList = response.getData().getdata();
						Log.d("guolei","mVideoUploadList size " +mVideoUploadList.size());
						Iterator<VideoUploadResponse > iterator = mVideoUploadList.iterator();
						while(iterator.hasNext()) {
							VideoUploadResponse item = iterator.next();
							if(item.getvideoname() == null) {
								iterator.remove();
							}
						}
						mListView.closeOpenedItems();
						if(mAdapter==null) {
							mAdapter = new UploadedVideoAdapter(OwnerActivity.this, mVideoUploadList, mListView);
							mListView.setAdapter(mAdapter);
						}else
							mAdapter.notifyDataSetChanged(mVideoUploadList);
					}
				}, mErrorListener);
		
	}
	/**
	 * 查找界面各控件
	 */
	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		stage_name_tv = (TextView) findViewById(R.id.stage_name_tv);
		logout_btn = (Button) findViewById(R.id.logout_btn);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		mListView =(SwipeListView) findViewById(R.id.uploadedlist);
	}

	@Override
	protected void initView() {
		tvTitle.setText("我的");
		return_imgbtn.setVisibility(View.VISIBLE);
		// 异步加载图片
//		bitmapUtils = XUtilsBitmap.getBitmapUtils(OwnerActivity.this);
//
//		// 获取配置中的手机号
		userInfo = InitApplication.mSpUtil.getUser();
//
		if (userInfo == null) {
			finish();
			Toast.makeText(getApplicationContext(), "请登录!", Toast.LENGTH_SHORT).show();
			return;
		}
			String stage_name = userInfo.getPhone();
//			show_dance_id = userInfo.getId();
			if (!TextUtils.isEmpty(stage_name)) {
				stage_name_tv.setText(stage_name);
			} else {
				stage_name_tv.setText("艺名:" + "");
//				Toast.makeText(getApplicationContext(), "请优先完善个人信息!", Toast.LENGTH_SHORT).show();
			}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
	}
	
	/**
	 * 
	 * @Description:配置需要分享的相关平台
	 * @param
	 * @return void
	 */
	private final static String TEMP_URL = "http://www.xiuwuba.net:82";
	private void configPlatforms(SharedEvent event) {
//		UMImage image = new UMImage(OwnerActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
		String url = TEMP_URL/*VolleyManager.SERVER_URL*/+VolleyManager.SHARED+event.event.getid();
		UMVideo video = new UMVideo(url);
		String videoname = null;
		try {
			videoname = event.event.getvideoname().substring(0, event.event.getvideoname().indexOf("_"));
		} catch (StringIndexOutOfBoundsException e) {
		}
		if(TextUtils.isEmpty(videoname)) {
			videoname = event.event.getvideoname();
		}
		
		video.setDescription(videoname);
//		video.setTargetUrl(url);
		String title = InitApplication.mSpUtil.getSp().getString("activeNote", null);
		if(title == null) {
			title = "分享了一个视频给您";
		}
		video.setTitle(title);
		video.setThumb(event.event.getimg());
		 new ShareAction(this).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
         .withText("舞媚娘App帮我制作的"+"《"+videoname+"》"+",快帮我点赞吧!")
         .withMedia(video)
//         .withTargetUrl(event.event.getname())
         .withTitle(title)
         .setCallback(umShareListener)
//         .withTargetUrl(url)
//          .withShareBoardDirection(OwnerActivity.this, Gravity.CENTER)
         .open();
	}
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(OwnerActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(OwnerActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(OwnerActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(OwnerActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
private SwipeListViewListener swipeListViewListener = new SwipeListViewListener() {
		
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			Log.d("RecordedVideoActivity","onStartOpen");
		}
		
		@Override
		public void onStartClose(int position, boolean right) {
			Log.d("guolei","onStartClose");
		}
		
		@Override
		public void onOpened(int position, boolean toRight) {
			Log.d("guolei","onOpened");
		}
		
		@Override
		public void onMove(int position, float x) {
			Log.d("guolei","onMove");
		}
		
		@Override
		public void onListChanged() {
			Log.d("guolei","onListChanged");
		}
		
		@Override
		public void onLastListItem() {
			Log.d("guolei","onLastListItem");
		}
		
		@Override
		public void onFirstListItem() {
			Log.d("guolei","onFirstListItem");
		}
		
		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			Log.d("guolei","onDismiss");
		}
		
		@Override
		public void onClosed(int position, boolean fromRight) {
			Log.d("guolei","onClosed");
		}
		
		@Override
		public void onClickFrontView(int position) {
			Log.d("guolei","onClickFrontView");
			Intent mIntent = new Intent();
			mIntent.setData(Uri.parse(mVideoUploadList.get(position).getname()));
			mIntent.putExtra("title", mVideoUploadList.get(position).getvideoname());
			mIntent.setClass(OwnerActivity.this, UseIntroductionActivity.class);
			startActivity(mIntent);
		}
		
		@Override
		public void onClickBackView(int position) {
			
		}
		
		@Override
		public void onChoiceStarted() {
			
		}
		
		@Override
		public void onChoiceEnded() {
			
		}
		
		@Override
		public void onChoiceChanged(int position, boolean selected) {
			
		}
		
		@Override
		public int onChangeSwipeMode(int position) {
			return SwipeListView.SWIPE_MODE_DEFAULT;
		}
	};
	
	
	/**
	 * 设置事件
	 */
	@Override
	protected void setOnClickListener() {
		mListView.setSwipeListViewListener(swipeListViewListener);
		return_imgbtn.setOnClickListener(this);
		logout_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_imgbtn:// 账号状态：未登录
			finish();
			break;

		case R.id.logout_btn:// 退出登录
			InitApplication.mSpUtil.getSp().edit().putBoolean("uploadinfo", false).commit();
			InitApplication.mSpUtil.setUser(null);
			InitApplication.mSpUtil.setPassword("");
			logout_btn.setVisibility(View.GONE);
			MobclickAgent.onProfileSignOff();
			finish();
			break;

//
//			if (createUser != 0) {
//				configPlatforms();
//				setShareContent();
//
//				uMController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
//
//				// uMController.openShare(VideoContentActivity.this, false);
//				uMController.openShare(OwnerActivity.this, new SnsPostListener() {
//
//					@Override
//					public void onStart() {
//
//					}
//
//					@Override
//					public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//						if (eCode == 200) {
//							int flagPlatform = 0;
//
//							if (platform.name() == "SMS") { // 短信
//								flagPlatform = 0;
//							} else if (platform.name() == "WEIXIN") { // 微信
//								flagPlatform = 1;
//							} else if (platform.name() == "WEIXIN_CIRCLE") { // 朋友圈
//								flagPlatform = 2;
//							} else if (platform.name() == "SINA") { // 新浪微博
//								flagPlatform = 3;
//							} else if (platform.name() == "TENCENT") { // 腾讯微博
//								flagPlatform = 3;
//							}
//
//							WS_saveShareInfo(OwnerActivity.this, createUser, flagPlatform);
//						}
//					}
//				});
//
//			} else {
//				Toast.makeText(getApplicationContext(), "请先登录后分享视频!", Toast.LENGTH_SHORT).show();
//			}

		
		}
	}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	protected boolean validateData() {
		return false;
	}
	
	private void shareVideoTo(){}
}