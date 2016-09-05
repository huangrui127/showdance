package com.android.app.showdance.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.app.showdance.adapter.DanceVideoAdapter;
import com.android.app.showdance.logic.DownloadViedoService;
import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.DownloadMedia;
import com.android.app.showdance.model.MediaInfoPageVo;
import com.android.app.showdance.model.Praise;
import com.android.app.showdance.model.ShareInfo;
import com.android.app.showdance.model.UserFans;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.ui.baidu.bvideo.VideoViewPlayingActivity;
import com.android.app.showdance.ui.oa.VideoCommentActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.utils.XUtilsBitmap;
import android.widget.VideoView;
import android.media.MediaPlayer.*;
import android.media.MediaPlayer;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.social.UMSocialService;
import com.umeng.socialize.bean.*;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
//import com.umeng.socialize.sso.SinaSsoHandler;
//import com.umeng.socialize.sso.SmsHandler;
//import com.umeng.socialize.sso.TencentWBSsoHandler;

/**
 * 
 * @ClassName: VideoDetailsActivity
 * @Description: 首页点击图片跳转到【跳舞视频的作品详情页面】
 * @author
 * @date 2015-5-1 上午10:40:20
 * 
 */
public class VideoDetailsActivity extends BaseActivity implements OnPreparedListener, OnCompletionListener, OnErrorListener, OnInfoListener, OnBufferingUpdateListener {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setOnClickListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean validateData() {
		// TODO Auto-generated method stub
		return false;
	}
	
}