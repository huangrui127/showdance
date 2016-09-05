package com.android.app.wumeiniang.app;
import java.util.Map;

import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.logic.event.SharedEvent;
import com.android.app.showdance.ui.OwnerActivity;
import com.android.app.wumeiniang.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWebPage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMediaObject.MediaType;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class SocialSdkHandler implements OnClickListener{
private Activity context;
	public SocialSdkHandler(Activity a) {context = a;}
	
	@Override
	public void onClick(View v) {
		configPlatforms(context);
	}
	
	private void configPlatforms(Activity context) {
//		UMImage image = new UMImage(OwnerActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
		String url = "http://www.xiuwuba.net/share/";
		String title = "我推荐了舞媚娘App给您";//InitApplication.mSpUtil.getSp().getString("activeNote", null);
		String content = "手机就能制作舞蹈视频MV，简单迅速，你也快来试试吧!";
		ShareContent shareContent = new ShareContent();
		shareContent.mText = content;
		shareContent.mTitle = title;
		shareContent.mMedia = new UMImage(context,  R.drawable.app_logo);
//		QZoneShareContent qzone = new QZoneShareContent(shareContent);
//        // 设置分享文字
//        // 设置点击消息的跳转URL
//        qzone.setTargeturl(url);
//        // 设置分享内容的标题
//        qzone.setTitle(title);
//        // 设置分享图片
//        qzone.setImage(new UMImage(context, R.drawable.app_logo));
//		
//		QQShareContent qq =  new QQShareContent(new ShareContent());
//		// 设置分享文字
//		qq.setText(content);
//        // 设置点击消息的跳转URL
//		qq.setTargeturl(url);
//        // 设置分享内容的标题
//		qq.setTitle(title);
//        // 设置分享图片
//		qq.setImage(new UMImage(context, R.drawable.app_logo));
		
//		video.setDescription(videoname);
//		video.setTargetUrl(url);
		
		 new ShareAction((Activity)context).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
         .withText(content)
//         .withMedia(video)
//		 	.withExtra(new UMImage(context, BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo)))
//         .withTargetUrl(event.event.getname())
		 	.setShareContent(shareContent)
         .withTitle(title)
         .setCallback(umShareListener)
         .withTargetUrl(url)
          //.withShareBoardDirection(view, Gravity.TOP|Gravity.LEFT)
         .open();
	}
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(context,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
