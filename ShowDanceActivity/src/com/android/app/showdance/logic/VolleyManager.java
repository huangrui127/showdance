package com.android.app.showdance.logic;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.utils.L;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AndroidRuntimeException;

/**
 * VolleyManager, send Request and receive response from web service.
 * 
 */
public class VolleyManager {
    private static final String TAG = "VolleyManager";
    private static VolleyManager mVolleyManager;
    private RequestQueue mQueue;
    private static boolean DEBUG = true;
    public static final String CLIENT_ID = "4b2fb6135ff764965febf424057697e9";

    public static String SERVER_URL = "http://www.wmnapp.com:82";
    public static String SERVER_URL_EXTRA = "http://www.wmnapp.com:8086"; // “首页”顶部广告图片及分类图标请求地址
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
    public static final String METHOD_HOT_VIDEO = "hotVideo";
    public static final String METHOD_MAIN_EXTRA = "/androidExtra.json";
    public static final String METHOD_ADD_SHARE_COUNT = "shareCount";
    public static final String SHARED = "/share/";

    private int version = -1;

    public static final String FONT_LIST = "fontList";

    public static VolleyManager getInstance(Context context) {
        if (mVolleyManager == null) {
            mVolleyManager = new VolleyManager(context);
        }
        return mVolleyManager;
    }

    public static VolleyManager getInstance() {
        if (mVolleyManager == null) {
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
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T, R> void getRequest(T request, String method, ResponeListener<R> listener, ErrorListener errorListener) {
        postRequest(Method.GET, request, method, listener, errorListener);
    }

    public <T, R> void postRequest(T request, String method, ResponeListener<R> listener, ErrorListener errorListener) {
        postRequest(Method.POST, request, method, listener, errorListener);
    }

//    private <T, R> void getRequest(int methodtype, T request, String method, ResponeListener<R> listener,
//            ErrorListener errorListener) {
//        if (SERVER_URL_EXTRA == null)
//            return;
//        try {
//            L.d(TAG, "GET请求的网络地址为："+SERVER_URL_EXTRA + method);
//            JsonObjectRequest objectRequest = new JsonObjectRequest(Method.GET,SERVER_URL_EXTRA + method, null,
//                    listener == null ? new ResponeListener<R>(null) : listener,
//                    errorListener == null ? new ErrorListener() : errorListener);
//            objectRequest.setTag("getObjectRequest");
//            mQueue.add(objectRequest);
//        } catch (Exception e) {
//            L.e(TAG, "Exception " + e.getMessage());
//        }
//    }

    private <T, R> void postRequest(int methodtype, T request, String method, ResponeListener<R> listener,
            ErrorListener errorListener) {
        if (SERVER_URL == null)
            return;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(request);
            JSONObject jsonObject;
            jsonObject = new JSONObject(json);
            // add versioncode
            jsonObject.put("version", version);
            if (DEBUG) {
                L.i(TAG, SERVER_URL + "   postRequest " + jsonObject.toString());
            }
            L.d(TAG, "POST请求的Json字符串是："+json.toString());
            L.d(TAG, "POST请求的网络地址为："+SERVER_URL + API + method);
            JsonObjectRequest objectRequest = new JsonObjectRequest(methodtype, SERVER_URL + API + method, jsonObject,
                    listener == null ? new ResponeListener<R>(null) : listener,
                    errorListener == null ? new ErrorListener() : errorListener);
            objectRequest.setTag("postObjectRequest");
            mQueue.add(objectRequest);
        } catch (Exception e) {
            L.e(TAG, "Exception " + e.getMessage());
        }
    }

//    public void getRequest(JSONObject json, String method, ResponeListener listener, ErrorListener errorListener) {
//        JsonObjectRequest request = new JsonObjectRequest(Method.GET, SERVER_URL + method, json,
//                listener == null ? new ResponeListener() : listener,
//                errorListener == null ? new ErrorListener() : errorListener);
//        mQueue.add(request);
//    }

    public void destory() {
        mQueue.cancelAll(null);
    }
    
    public static class ResponeListener<T> implements Response.Listener<JSONObject> {
        private Class<T> clazz;

        public ResponeListener(Class<T> c) {
            clazz = c;
        }

        @Override
        public void onResponse(JSONObject response) {
            String json = response.toString();
            if (DEBUG)
                L.d(TAG, "onResponse = " + json);
            if (clazz == null) {
                onMyResponse(null);
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (response.getBoolean("flag") == Boolean.FALSE) {
                    ResponseFail fail = objectMapper.readValue(json, ResponseFail.class);
                    onResponseFail(fail);
                    return;
                }
                T obj = objectMapper.readValue(json, clazz);
                onMyResponse(obj);
            } catch (Exception e) {
                L.e(TAG, "onResponse Exception " + e.toString());
                ResponseFail fail = new ResponseFail();
                fail.setMessage(e.toString());
                onResponseFail(fail);
                return;
            }
        }

        public void onMyResponse(T response) {

        }

        public void onResponseFail(ResponseFail response) {

        }
    }

    public static class ErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            L.e(TAG, "onErrorResponse " + error.toString());
        }
    }
}
