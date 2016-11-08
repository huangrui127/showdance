package com.android.app.showdance.utils;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 
 * @ClassName: SoapWebServiceUtil
 * @Description: webservice数据传输工具类
 * @author maminghua
 * @date 2014-11-18 下午05:37:23
 * 
 */
public class SoapWebServiceUtil {

	/**
	 * 
	 * @Description:返回String类型数据
	 * @param methodName
	 * @param propertys
	 * @param
	 * @param @throws JSONException
	 * @return String
	 */
	public static String getStringResponseData(String ObjectKeyName, String methodName, JSONObject mJSONObject) throws JSONException {
		String result = null;
		SoapObject soapObject = null;
		// 创建httpTransportSE传输对象
		HttpTransportSE transport = new HttpTransportSE(ConstantsUtil.WebServiceUrl);
		transport.debug = true;
		// 使用soap1.1协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// 实例化SoapObject对象 指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(ConstantsUtil.WebServiceNameSpace, methodName);
		if (ObjectKeyName != "") {
			request.addProperty(ObjectKeyName, mJSONObject.toString());
		}

		// 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		envelope.bodyOut = request;

		// 设置是否调用的是dotNet开发的WebService
		// envelope.dotNet = true;

		// 等价于envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);

		// 调用WebService
		// String transportUrl =
		// ConstantsUtil.WebServiceNameSpace.concat(methodName);

		try {

			transport.call(null, envelope); // 向WebService发送请求

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		soapObject = (SoapObject) envelope.bodyIn;

		if (soapObject != null) {
			result = (soapObject).getProperty(0).toString();
		}

		return result;

	}
	
	/**
	 * 
	 * @Description:返回String类型数据
	 * @param ObjectKeyName
	 * @param methodName
	 * @param mJSONArray
	 * @param @return
	 * @param @throws JSONException
	 * @return String
	 */

	public static String getStringResponseData(String ObjectKeyName, String methodName, JSONArray mJSONArray) throws JSONException {
		String result = null;
		SoapObject soapObject = null;
		try {
			// 创建httpTransportSE传输对象
			HttpTransportSE transport = new HttpTransportSE(ConstantsUtil.WebServiceUrl);
			transport.debug = true;
			// 使用soap1.1协议创建Envelop对象
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			// 实例化SoapObject对象 指定WebService的命名空间和调用的方法名
			SoapObject request = new SoapObject(ConstantsUtil.WebServiceNameSpace, methodName);
			if (ObjectKeyName != "") {

				request.addProperty(ObjectKeyName, mJSONArray.toString());
			}
			// 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
			envelope.bodyOut = request;

			// 设置是否调用的是dotNet开发的WebService
			// envelope.dotNet = true;

			// 等价于envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);

			// 调用WebService
			// String transportUrl =
			// ConstantsUtil.WebServiceNameSpace.concat(methodName);
			transport.call(null, envelope); // 向WebService发送请求
											// (后台打了断点，这里就总是跳不过去，出现资源找不到的情况，存在WebService超时的问题)
			if (envelope.getResponse() != null) {
				soapObject = (SoapObject) envelope.bodyIn;
			}
			if (soapObject != null) {
				result = (soapObject).getProperty(0).toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return result;
	}

}
