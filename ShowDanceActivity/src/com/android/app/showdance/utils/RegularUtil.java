package com.android.app.showdance.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

public class RegularUtil {
	
	public static boolean checkSize(Activity context,String contentString){
		int length = contentString.length();
		if (TextUtils.isEmpty(contentString) || length < 6 || contentString.length() > 500 || !contentFormat(contentString)) {
//			Toast.makeText(context, "请输入6-500个中英文字符、数字", Toast.LENGTH_SHORT).show();
            return false;
        }
		return true;
	}
	 
    public static boolean checkName(Activity context, String name) {
        if (TextUtils.isEmpty(name) || name.length() < 3 || name.length() > 16 || !nameFormat(name)) {
            Toast.makeText(context, "昵称不符合规范，3-16个中英文字符、数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean checkHeight(Activity context, int height) {
        if (height < 100 || height > 250) {
        	Toast.makeText(context, "身高超出正常范围", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean checkWeight(Activity context, int weight) {
        if (weight < 40 || weight > 250) {
        	Toast.makeText(context, "体重超出正常范围", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean checkStepSize(Activity context, int stepSize) {
        if (stepSize < 30 || stepSize > 150) {
        	Toast.makeText(context, "步长超出正常范围", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean checkEmail(Activity context, String email) {
        if (!emailFormat(email) || email.length() > 31) {
        	Toast.makeText(context, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean checkPassword(Activity context, String password) {
        if (!passwordFormat(password)) {
//        	Toast.makeText(context, "密码格式是6-20位英文字符、数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean checkPassword(Activity context, String password, String confirm) {
        if (!checkPassword(context, password)) {
            return false;
        }
        if (!password.equals(confirm)) {
        	Toast.makeText(context, "密码设置不一致", 0);
            return false;
        }
        return true;
    }
    
    /** 
     * 验证手机格式 
     */  
    public static boolean isMobileNO(Activity context,String mobiles) {  
        /* 
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
        联通：130、131、132、152、155、156、185、186 
        电信：133、153、180、189、（1349卫通） 
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
        */  
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
        if (TextUtils.isEmpty(mobiles)){
//        	Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        	return false;  
        }
        else return mobiles.matches(telRegex);  
       }  
 
    public static boolean checkCode(Activity context, String code) {
        if (code.length() != 4) {
        	Toast.makeText(context, "请输入正确的四位验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
 
    public static boolean check(Activity context, String email, String password) {
        if (!emailFormat(email) || email.length() > 31) {
        	Toast.makeText(context, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkPassword(context, password)) {
            return false;
        }
        return true;
    }
 
    private static boolean emailFormat(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z\\d]+(\\.[A-Za-z\\d]+)*@([\\dA-Za-z](-[\\dA-Za-z])?)+(\\.{1,2}[A-Za-z]+)+$");
        Matcher mc = pattern.matcher(email);
        return mc.matches();
    }
 
    /**
     * 以字母开头，长度在3~16之间，只能包含字符、数字和下划线（w）
     *
     * @param password
     * @return
     */
    private static boolean passwordFormat(String password) {
        Pattern pattern = Pattern.compile("^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,20}$");
        Matcher mc = pattern.matcher(password);
        return mc.matches();
    }
 
    public static boolean nameFormat(String name) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5A-Za-z0-9_]{3,16}$");
        Matcher mc = pattern.matcher(name);
        return mc.matches();
    }
    
    public static boolean contentFormat(String contentString) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5A-Za-z0-9_ ]{6,500}$"); //\u4e00-\u9fa5是所有汉字的Unicode编码范围
        // "\\ "表示可以包含空格
        Matcher mc = pattern.matcher(contentString);
        return mc.matches();
    }
 
    /**
     * 获取含双字节字符的字符串字节长度
     *
     * @param s
     * @return
     */
    public static int getStringLength(String s) {
        char[] chars = s.toCharArray();
        int count = 0;
        for (char c : chars) {
            count += getSpecialCharLength(c);
        }
        return count;
    }
 
    /**
     * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
     *
     * @param c
     *            字符
     * @return 字符长度
     */
    private static int getSpecialCharLength(char c) {
        if (isLetter(c)) {
            return 1;
        } else {
            return 2;
        }
    }
 
    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param char c, 需要判断的字符
     * @return boolean, 返回true,Ascill字符
     */
    private static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }
    
    /**
     * 功能：过滤特殊字符
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
}
