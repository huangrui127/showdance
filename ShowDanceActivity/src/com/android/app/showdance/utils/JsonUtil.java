package com.android.app.showdance.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {
	private static JsonParser parser = new JsonParser();
	private static Gson json = new Gson();

	/**
	 * 将对象转成Json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return json.toJson(obj);
	}

	/**
	 * 将字符串转换为JsonElement
	 * 
	 * @param data
	 * @return
	 */
	public static JsonElement convertToJson(String data) {
		return parser.parse(data);
	}

	/**
	 * 将Json字符串转成对象
	 * 
	 * @param data
	 * @param clz
	 * @return
	 */
	public static <T> T convertToObject(Class<T> clz, String data) {
		return json.fromJson(data, clz);
	}

	/**
	 * 将Json字符串转成ArrayList
	 * 
	 * @param type
	 * @param data
	 * @return
	 */
	public static <T> ArrayList<T> convertToObject(Type type, String data) {
		return json.fromJson(data, type);
	}
}
