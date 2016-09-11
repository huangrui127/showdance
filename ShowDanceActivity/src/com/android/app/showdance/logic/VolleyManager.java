package com.android.app.showdance.logic;

import java.io.IOException;

import javax.xml.transform.Templates;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


/**
 * VolleyManager, send Request and receive response from web service.
 * 
 */
public class VolleyManager  {
	private static final String TAG = "VolleyManager";
	private static VolleyManager mVolleyManager;
	private RequestQueue mQueue;
	private static boolean DEBUG = true;
	public static final String CLIENT_ID = "4b2fb6135ff764965febf424057697e9";
	
	public static String SERVER_URL= "http://www.wmnapp.com:82";
	public static String SERVER_URL_TEST = "http://www.wmnapp.com:83";
	public static String SERVER_URL_BACKUP = "http://www.wmnapp.com:82";
	public static final String API = "/api/";
	public static final String METHOD_DOWNLOAD_MSUIC = "downloadMusic";
	public static final String METHOD_MSUIC_SEARCH = "musicSearch";
	public static final String METHOD_STAR_TEACHER_LIST = "starTeacherList";
	public static final String METHOD_STAR_TEACHER_MUSIC = "starTeacheMusic";
	public static final String METHOD_LOGIN = "login";
	public static final String METHOD_COUNT = "count";
	public static final String METHOD_DOWNLOAD_FRAME = "downloadFrame";
	public static final String METHOD_MASK_LIST = "maskList";
	public static final String METHOD_SMS = "sms";
	public static final String METHOD_STAR_FRAME = "starFrameTeacherList";
	public static final String SHARED = "/share/";
	
	private int version = -1;
	
	public static final String FONT_LIST = "fontList";
	
	public static VolleyManager getInstance(Context context) {
		if(mVolleyManager == null) {
			mVolleyManager = new VolleyManager(context);
		}
		return mVolleyManager;
	}
	
	public static VolleyManager getInstance() {
		if(mVolleyManager == null) {
			throw new AndroidRuntimeException();
		}
		return mVolleyManager;
	}
	
	public RequestQueue getRequestQueue() {
		return mQueue;
	}
	
	private VolleyManager(Context context) {
		mQueue = Volley.newRequestQueue(context.getApplicationContext());
			try {
				PackageInfo info = context.getPackageManager()
						.getPackageInfo(context.getPackageName(), 0);
				version = info.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	public <T,R> void getRequest(T req, String method,
			ResponeListener<R> listener, ErrorListener errorListener) {
		postRequest(Method.GET,req, method, listener, errorListener);
	}
	
	public <T,R> void postRequest(T req, String method,
			ResponeListener<R> listener, ErrorListener errorListener) {
		postRequest(Method.POST,req, method, listener, errorListener);
	}
	
	private <T,R> void postRequest(int methodtype,T req, String method,
			ResponeListener<R> listener, ErrorListener errorListener) {
		if(SERVER_URL == null)
			return;
		
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		String result
			 = objectMapper.writeValueAsString(req);
		JSONObject json;
			json = new JSONObject(result);
			//add versioncode
			json.put("version", version);
			
			if(DEBUG) {
				Log.i(TAG, SERVER_URL +"   postRequest "+json.toString());
			}
		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				SERVER_URL + API+method, json,
				listener == null ? new ResponeListener<R>(null) : listener,
				errorListener == null ? new ErrorListener() : errorListener);
		mQueue.add(request);
		} catch (JsonGenerationException e) {
			Log.e(TAG,"JsonGenerationException "+e.getMessage());
		} catch (JsonMappingException e) {
			Log.e(TAG,"JsonMappingException "+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,"IOException "+e.getMessage());
		} catch (JSONException e) {
			Log.e(TAG,"JSONException "+e.getMessage());
		}
	}
	
//	public void getRequest(JSONObject json, String method,
//			ResponeListener listener, ErrorListener errorListener) {
//		JsonObjectRequest request = new JsonObjectRequest(Method.GET,
//				SERVER_URL + method, json,
//				listener == null ? new ResponeListener() : listener,
//				errorListener == null ? new ErrorListener() : errorListener);
//		mQueue.add(request);
//	}
	
	public void destory() {
		mQueue.cancelAll(null);
	}
	
	public static class ResponeListener<T> implements Response.Listener<JSONObject>	{
		private Class<T> clazz;
		
		public ResponeListener (Class<T> c) {
			clazz = c;
		}
		
		
		@Override
		public void onResponse(JSONObject response) {
			String json = response.toString();
			if(DEBUG)
				Log.d(TAG,"onResponse = "+json);
			if(clazz ==null) {
				onMyResponse(null);
				return;
			}

			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				try {
					if (response.getBoolean("flag") == Boolean.FALSE) {
						ResponseFail fail = objectMapper.readValue(json,ResponseFail.class);
						onResponseFail(fail);
						return;
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
					return;
				}
				T obj = objectMapper.readValue(json,clazz);
				onMyResponse(obj);
			} catch (JsonParseException e) {
				Log.e(TAG,"onResponse JsonParseException "+e.toString());
			} catch (JsonMappingException e) {
				Log.e(TAG,"onResponse JsonMappingException "+e.toString());
			} catch (IOException e) {
				Log.e(TAG,"onResponse IOException "+e.toString());
			}
		}
		
		public void onMyResponse(T response) {
			
		}
		
		public void onResponseFail(ResponseFail response) {
			
		}
	}
	
	public static class ErrorListener implements Response.ErrorListener	{

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG,"onErrorResponse "+error.toString());
		}
	}
}
