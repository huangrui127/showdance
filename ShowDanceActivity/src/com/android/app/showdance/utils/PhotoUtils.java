package com.android.app.showdance.utils;

import java.io.File;
import java.io.FileInputStream;

import android.util.Base64;

public class PhotoUtils {
	/**
	 * 将文件转成base64 字符串
	 * 
	 * @param path文件路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return android.util.Base64.encodeToString(buffer, Base64.DEFAULT);
	}
}
