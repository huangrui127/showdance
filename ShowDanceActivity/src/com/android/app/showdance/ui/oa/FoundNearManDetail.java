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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.adapter.DanceVideoAdapter;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.MediaInfoPageVo;
import com.android.app.showdance.model.User;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.VideoDetailsActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.XUtilsBitmap;
import com.lidroid.xutils.BitmapUtils;

/**
 * 附近舞友--【舞友详细资料】页面
 * **/

public class FoundNearManDetail extends BaseActivity  implements OnItemClickListener{
	// private LinearLayout personal_photo_ll; // 个人相册
	// private LinearLayout personal_video_ll; // 个人视频
	// private LinearLayout danceTeam_video_ll; // 舞友所在舞队视频
	// private LinearLayout focused_dance_team_ll; // 关注的舞队
	// private ImageView team1;

	private GridView Ta_video; // Ta的视频
//	private ArrayList<HashMap<String, Object>> meumList;
//	private int[] ItemImage = { R.drawable.video1, R.drawable.video2, R.drawable.video3, R.drawable.video4, R.drawable.video5, R.drawable.video6, R.drawable.video7, R.drawable.video8,
//			R.drawable.video9 };
	
	private List<Map<String, Object>> list;
	private TextView dance_man_tv, dance_man_gender, dance_Id_tv, area_tv, fnmd_dance_team_sign;
	private ImageView dance_team_img;
	private String imgUrl;
	private String newsTitle;
	public BitmapUtils bitmapUtils;// 异步加载图片
	ImageView news_content;
	
	private int pageNo = 1;
	private int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.found_near_man_detail);
		findViewById();
		initView();
		setOnClickListener();

	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
		// personal_photo_ll = (LinearLayout)
		// findViewById(R.id.personal_photo_ll);
		// personal_video_ll = (LinearLayout)
		// findViewById(R.id.personal_video_ll);
		// danceTeam_video_ll = (LinearLayout)
		// findViewById(R.id.danceTeam_video_ll);
		// focused_dance_team_ll = (LinearLayout)
		// findViewById(R.id.focused_dance_team_ll);
		// team1 = (ImageView) findViewById(R.id.team1);
		Ta_video = (GridView) findViewById(R.id.Ta_video);
		dance_man_tv = (TextView) findViewById(R.id.dance_man_tv);
		dance_man_gender = (TextView) findViewById(R.id.dance_man_gender);
		dance_Id_tv = (TextView) findViewById(R.id.dance_Id_tv);
		area_tv = (TextView) findViewById(R.id.area_tv);
		fnmd_dance_team_sign = (TextView) findViewById(R.id.fnmd_dance_team_sign);
		dance_team_img = (ImageView) findViewById(R.id.dance_team_img);
	}

	@Override
	protected void initView() {
		tvTitle.setText("舞友详细资料");
		return_imgbtn.setVisibility(View.VISIBLE);
		
		imgUrl = getIntent().getStringExtra("imgUrl");
		// 异步加载图片
		bitmapUtils = XUtilsBitmap.getBitmapUtils(FoundNearManDetail.this);

		bitmapUtils.display(news_content, imgUrl);

//		// 加载GridView
//		meumList = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < 9; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("ItemImage", ItemImage[i]);
//			// map.put("ItemText0", ItemText0[i]);
//			// map.put("ItemText", ItemText[i]);
//			meumList.add(map);
//		}
//		SimpleAdapter saMenuItem = new SimpleAdapter(this, meumList, // 数据源
//				R.layout.dance_video_item, // GridView布局实现
//				new String[] { "ItemImage", "ItemText0", "ItemText" }, // 对应map的Key
//				new int[] { R.id.dance_video_image, R.id.dance_user_tv, R.id.dance_video_name_tv }); // 对应R的Id
//		// new String[] { "ItemImage"}, // 对应map的Key
//		// new int[] { R.id.ItemImage}); // 对应R的Id
//
//		Ta_video.setAdapter(saMenuItem);

		long createUserId = Long.parseLong(getIntent().getStringExtra("createUserId"));
		WS_getNearManDetail(this, createUserId);
		WS_getMyVedio(this,createUserId);

	}

	@Override
	protected void setOnClickListener() {
		return_imgbtn.setOnClickListener(this);
		// personal_photo_ll.setOnClickListener(this);
		// personal_video_ll.setOnClickListener(this);
		// danceTeam_video_ll.setOnClickListener(this);
		// focused_dance_team_ll.setOnClickListener(this);
		// team1.setOnClickListener(this);
		Ta_video.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.return_imgbtn:// 返回
			this.finish();
			break;
		// case R.id.personal_photo_ll: // 个人相册
		// // 跳转到【个人相册】的页面
		// mIntent.setClass(this, FoundPersonalPhotoActivity.class);
		// startActivity(mIntent);
		//
		// break;
		// case R.id.personal_video_ll: // 个人视频
		//
		// break;

		// case R.id.danceTeam_video_ll:// 舞友所在舞队视频
		// mIntent.setClass(this, ListToDanceVideoActivity.class);
		// mIntent.putExtra("flag", "member_to_team_vedio");
		// startActivity(mIntent);
		//
		// break;

		// case R.id.focused_dance_team_ll:// 关注的舞队
		//
		// break;
		default:
			break;
		}
	}

	@Override
	public void refresh(Object... param) {

		int type = (Integer) param[0];
		switch (type) {
		case TaskType.TS_GETNEARMANDETAIL: // 根据用户id，查询用户详情
			mDialog.cancel();
			if (ConstantsUtil.NetworkStatus) {
				Map<String, Object> map = (Map<String, Object>) param[1];
				String result = map.get("result").toString();
				if (!result.equals("-1")) {
					if (result.equals("0")) {
						String id = map.get("id").toString();
						
						String name = "" ;
						
						if(map.get("name")!=null){
							name = map.get("name").toString();
						}
						
						String loginName = map.get("loginName").toString();
						//TODO 性别 1女 2男？
						String sex = map.get("sex").toString(); 
						
						String photo = "";
						
						if(map.get("photo")!=null){
							photo = map.get("photo").toString();
						}
						
						String address = "";
						
						if(map.get("address")!=null){
							address = map.get("address").toString();
						}
						
						String signature = "";
						if(map.get("signature")!=null){
							signature = map.get("signature").toString();
						}
						String mobilephone = map.get("mobilephone").toString();
						
						bitmapUtils.display(this.dance_team_img, ConstantsUtil.PhotoUri.concat(photo));
						dance_man_tv.setText(name); //艺名
						//TODO 性别 1女 2男？
//						if(sex.equals("1")){
//							dance_man_gender.setText("女");
//						}else{
//							dance_man_gender.setText("男");
//						}
						dance_Id_tv.setText(loginName); // 秀舞Id
						area_tv.setText(address);
						fnmd_dance_team_sign.setText(signature); //个性签名
					} 
				} else {
					Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			} else {
				makeToast(this, R.string.app_network);
			}
			break;

		case TaskType.TS_GETMYVIDIO: // 我的视频--舞友的视频
//			mDialog.cancel();
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
					
					DanceVideoAdapter danceVideoAdapter = new DanceVideoAdapter(this, list, 1); // 创建适配器
					Ta_video.setAdapter(danceVideoAdapter);

				} else {
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

	/**
	 * 
	 * 调用接口 "根据用户id，查询用户详情"
	 */
	public void WS_getNearManDetail(Context mContext, long useId) {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_progressbar_dialog);
		mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条
		// 接口参数
		User user = new User();
		paramsMap = new HashMap<String, Object>();
		user.setId(useId);
		paramsMap.put("user", user);
		// 添加到任务队列
		Task mTask = new Task(TaskType.TS_GETNEARMANDETAIL, paramsMap);
		MainService.newTask(mTask);
	}
	
	/**
	 * 
	 * 调用接口 "我的视频"（）
	 */
	public void WS_getMyVedio(Context mContext, long userId) {
		//接口参数
		MediaInfoPageVo  mediaInfoPageVo=new MediaInfoPageVo();
		paramsMap = new HashMap<String, Object>();
		mediaInfoPageVo.setCreateUser(userId);	
		mediaInfoPageVo.setPageNo(pageNo);	
		mediaInfoPageVo.setPageSize(pageSize);	
		paramsMap.put("mediaInfoPageVo", mediaInfoPageVo);
		paramsMap.put("videoType", ConstantsUtil.videoType2);
		Task mTask = new Task(TaskType.TS_GETMYVIDIO, paramsMap);
		MainService.newTask(mTask);
	}

	//点击某一视频，切换到视频播放页面
	// GridView监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		 Intent mIntent = new Intent();
		 mIntent.setClass(this, VideoDetailsActivity.class);
		 
		 //选中的某一项记录
		 Map<String, Object>  tmpMap= list.get(position);
		
		// 传数据到视频详情页面
		 mIntent.putExtra("createUserId", tmpMap.get("createUser").toString());		 
		 mIntent.putExtra("mediaId", tmpMap.get("mediaId").toString());	
		 mIntent.putExtra("mediaNewName", tmpMap.get("mediaNewName").toString());	
		
		 startActivity(mIntent);

	}
}
