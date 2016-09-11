package com.android.app.showdance.ui;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import com.android.app.wumeiniang.R;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.ShowDanceActivity;
import com.android.app.wumeiniang.app.InitApplication;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SplashActivity extends Activity {
    private static final long SPLASH_DELAY_MILLIS = 1000;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        checkVersion();
    }

    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, ShowDanceActivity.class);
//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
    
    public static class UrlRespone {
		String webBase;
		String helpUrl;
		int newVersion;
		String note;
		String noteUrl;
		String activeNote;
		
		public void setwebBase(String webBase) {this.webBase = webBase;}
		public String getwebBase() {return webBase;}
		public void sethelpUrl(String helpUrl) {this.helpUrl = helpUrl;}
		public String gethelpUrl() {return helpUrl;}
		public void setnewVersion(int newVersion) {this.newVersion = newVersion;}
		public int getnewVersion() {return newVersion;}
		public void setnote(String note) {this.note = note;}
		public String getnote() {return note;}
		public void setnoteUrl(String noteUrl) {this.noteUrl = noteUrl;}
		public String getnoteUrl() {return noteUrl;}
		public void setactiveNote(String noteUrl) {this.activeNote = noteUrl;}
		public String getactiveNote() {return activeNote;}
	}
	
	private void showUpdateApkDialog(final String uri,String content) throws UnsupportedEncodingException {
		CustomAlertDialog mCustomDialog = new CustomAlertDialog(this).builder(R.style.DialogTVAnimWindowAnim);
		mCustomDialog.setTitle("更新提示");
		mCustomDialog.setMsg(content);
		mCustomDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(Intent. ACTION_VIEW,Uri.parse(uri));
				startActivity(mIntent);
				finish();
			}
		}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new OnClickListener() {
			@Override
			public void onClick(View v) {
				goHome();
			}
		}).show();
	}
    
	private void checkVersion() {
		StringRequest request = new StringRequest(Method.GET, "http://www.xiuwuba.net:8086/androiod.json", 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						boolean bupdate = false;
						String json = response.toString();
//						Log.w("guolei", "onResponse " + json);
						ObjectMapper objectMapper = new ObjectMapper();
						UrlRespone helpUrl = null;
						try {
							helpUrl = objectMapper.readValue(
									json.substring(json.indexOf("{")),
									UrlRespone.class);
							String web = helpUrl.getwebBase();
							if (web != null) {
								VolleyManager.SERVER_URL = web;
								VolleyManager.SERVER_URL_BACKUP = web;
							}
							int newVersion = helpUrl.getnewVersion();
							String helpurl = helpUrl.gethelpUrl();
							SharedPreferences sp = InitApplication.mSpUtil.getSp();
							if(helpurl != null)
								sp.edit().putString("helpurl", helpurl).commit();
							if(helpUrl.getactiveNote() != null)
								sp.edit().putString("activeNote", helpUrl.getactiveNote()).commit();
							PackageInfo info = getPackageManager()
									.getPackageInfo(getPackageName(), 0);
							if (info.versionCode < newVersion) {
								bupdate =true;
							}
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						} catch (JsonParseException e1) {
							e1.printStackTrace();
						} catch (JsonMappingException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
							if(bupdate) {
								try {
									showUpdateApkDialog(helpUrl.getnoteUrl(), helpUrl.getnote());
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								return;
							}
							mHandler.postDelayed(new Runnable() {
					            public void run() {
					                goHome();
					            }
					        }, SPLASH_DELAY_MILLIS);
						}
					}
				}
				, new Response.ErrorListener	() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
//						Log.e("guolei","onErrorResponse "+arg0.toString());
						mHandler.postDelayed(new Runnable() {
				            public void run() {
				                goHome();
				            }
				        }, SPLASH_DELAY_MILLIS);
					}
					
				}) {
			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String str = null;
				try {
				str = new String(response.data, "utf-8");
				} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				}
				return Response.success(str,
				HttpHeaderParser.parseCacheHeaders(response));
				}
		};
		VolleyManager.getInstance().getRequestQueue().add(request);
}
}
