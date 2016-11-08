package com.android.app.showdance.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对密码进行MD5加密
 * **/

public class MD5Util {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String MD5Encode(String str) {
		try {
			// 根据MD5算法生成MessageDigest对象
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] srcBytes = str.getBytes();
			// 使用srcBytes更新摘要
			md5.update(srcBytes);
			// 完成哈希计算，得到result
			byte[] result = md5.digest();

			StringBuffer hexValue = new StringBuffer();

			for (int i = 0; i < result.length; i++) {
				int val = ((int) result[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Description:MD5加密
	 * @param origin
	 * @param @return
	 * @return String
	 */
	public static String MD5Encode2(String origin) {
		String resultString = null;

		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {

		}
		return resultString;
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 
	 * @Description:
	 * @param b
	 * @param @return
	 * @return String
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}
