package com.android.app.showdance.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class HttpsInterfaces {

	public static String httpGet(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		HttpResponse response;
		StringBuffer sBuffer = null;
		try {
			request.setURI(new URI(url));
			response = client.execute(request);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			sBuffer = new StringBuffer();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				sBuffer.append(line);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println(sBuffer.toString());
		return sBuffer.toString();
	}

	public static String httpGet2(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		String body = null;

		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		HttpEntity entity = response.getEntity();

		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
		} catch (IOException e) {
		}

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String httpPost(String url, Map<String, String> params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		HttpResponse response = null;
		String body = null;

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}

		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		HttpEntity entity = response.getEntity();

		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
		} catch (IOException e) {
		}

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static void httpUpload(final Context context, final String filePath) {

		// PreferencesCookieStore preferencesCookieStore = new
		// PreferencesCookieStore(context);
		//
		// BasicClientCookie cookie = new BasicClientCookie("test", "hello");
		// cookie.setDomain("192.168.1.122");
		// cookie.setPath("/");
		// preferencesCookieStore.addCookie(cookie);

		// 设置请求参数的编码
		// RequestParams params = new RequestParams("GBK");
		RequestParams params = new RequestParams();// 默认编码UTF-8
		params.addHeader("name", "value");
		params.addQueryStringParameter("method", "upload");

		// 只包含字符串参数时默认使用BodyParamsEntity，
		// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
		params.addBodyParameter("name", "value");

		// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
		// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
		// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
		// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
		// 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
		params.addBodyParameter("file", new File(filePath));

		HttpUtils http = new HttpUtils();
		
		http.configCurrentHttpCacheExpiry(1000 * 1000);//设置超时时间  		
		http.configSoTimeout(1000 * 1000);		
		http.configTimeout(1000 * 1000);
		
		http.configHttpCacheSize(1024*1024*1024);
		
		// 设置返回文本的编码， 默认编码UTF-8
		// http.configResponseTextCharset("GBK");

		http.send(HttpRequest.HttpMethod.POST, ConstantsUtil.HttpPostUrl, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
//				Toast.makeText(context, "conn...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
//					Toast.makeText(context, "upload: " + current + "/" + total, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ConstantsUtil.ACTION_UPLOAD_SIZE);
					intent.putExtra("total", total);
					intent.putExtra("current", current);
					context.sendBroadcast(intent);
				} else {
//					Toast.makeText(context, "reply: " + current + "/" + total, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				Toast.makeText(context, "reply: " + responseInfo.result, Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(ConstantsUtil.ACTION_UPLOAD_STATE);
				intent.putExtra("filePath", filePath);
				intent.putExtra("resultNewName", responseInfo.result);
				context.sendBroadcast(intent);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
//				Toast.makeText(context, "msg：" + msg, Toast.LENGTH_SHORT).show();

			}
		});

	}

}
