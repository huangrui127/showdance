package com.android.app.showdance.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

/**
 * 
 * @ClassName: StringUtils
 * @Description: 字符串转换工具类
 * @author maminghua
 * @date 2014-6-17 下午08:03:18
 * 
 */
public class StringUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat timeFormater = new SimpleDateFormat("mm:ss.SS");
    private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final static SimpleDateFormat dateFormatUserId = new SimpleDateFormat("yyMMddkkmmss");
    private static final String[] WEEK = { "天", "一", "二", "三", "四", "五", "六" };
    public static final String XING_QI = "星期";
    public static final String ZHOU = "周";

    private static Pattern p = null;
    private static Matcher m = null;

    // java 比较时间大小
    public static int dateCompare(String s1, String s2) {
        // String s1 = "2008-01-25 09:12:09";
        // String s2 = "2008-01-29 09:12:11";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(s1)); // parse()由String转Date
            c2.setTime(df.parse(s2));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        if (result == 0)
            System.out.println("c1相等c2");
        else if (result < 0)
            System.out.println("c1小于c2");
        else
            // result >= 0
            System.out.println("c1大于c2");
        return result;

    }

    /**
     * 验证手机号码
     * 
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            mobiles = mobiles.replace(" ", "");
            mobiles = mobiles.replace("+86", "");
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 过滤字符串中的\t水平制表符,\r换行,\n回车,\s空格
     * 
     * @param str
     * @return
     */
    public static String FilterStr(String str) {
        return str.replace("\r", "").replace("\t", "").replace("\n", "").replace("\\s", "").trim();
    }

    /**
     * 
     * @Description:将字符串转位日期类型
     * @param sdate
     * @param @return
     * @return Date
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 
     * @Description:将字符串转位日期类型
     * @param sdate
     * @param @return
     * @return Date
     */
    public static Date toTime(String sdate) {
        try {
            return timeFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     * 
     * @param group
     *            字符形式的时间点
     * @return Long形式的时间
     */
    public static long strToLong(String timeStr) {
        // 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        // 1:使用：分割 2：使用.分割
        String[] s = timeStr.split(":");
        int min = Integer.parseInt(s[0]);
        String[] ss = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min * 60 * 1000 + sec * 1000 + mill * 10;
    }

    /**
     * 
     * @Description:将字符串转位日期类型
     * @param sdate
     * @param @return
     * @return Date
     */
    public static Date toDate2(String sdate) {
        try {
            return dateFormater2.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 
     * @Description:将日期转换为字符串
     * @param date
     * @param @return
     * @return String
     */
    public static String date2Time(Date date) {
        String time = sqlDateFormat.format(date);
        return time;
    }

    /**
     * 
     * @Description:将日期转换为 年月日 格式字符串
     * @param date
     * @param @return
     * @return String
     */
    public static String date2String(Date date) {
        String datestring = null;
        if (date == null) {
            datestring = "";
        } else {
            datestring = dateFormater2.format(date);
        }
        return datestring;
    }

    // 格式化日期为字符串 "yyyy-MM-dd hh:mm"
    public String formatDateTime(Date basicDate, String strFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat);
        return df.format(basicDate);
    }

    // 格式化日期为字符串 "yyyy-MM-dd hh:mm"
    public String formatDateTime(String basicDate, String strFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat);
        Date tmpDate = null;
        try {
            tmpDate = df.parse(basicDate);
        } catch (Exception e) {
            // 日期型字符串格式错误
        }
        return df.format(tmpDate);
    }

    // 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
    public static String nDaysAftertoday(int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return df.format(rightNow.getTime());
    }

    // 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
    public Date nDaysAfterNowDate(int n) {
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return rightNow.getTime();
    }

    // 给定一个日期型字符串，返回加减n天后的日期型字符串
    public String nDaysAfterOneDateString(String basicDate, int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tmpDate = null;
        try {
            tmpDate = df.parse(basicDate);
        } catch (Exception e) {
            // 日期型字符串格式错误
        }
        long nDay = (tmpDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n) * (24 * 60 * 60 * 1000);
        tmpDate.setTime(nDay);

        return df.format(tmpDate);
    }

    // 给定一个日期，返回加减n天后的日期
    public Date nDaysAfterOneDate(Date basicDate, int n) {
        long nDay = (basicDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n) * (24 * 60 * 60 * 1000);
        basicDate.setTime(nDay);

        return basicDate;
    }

    // 计算两个日期相隔的天数
    public int nDaysBetweenTwoDate(Date firstDate, Date secondDate) {
        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    // 计算两个日期相隔的天数
    public int nDaysBetweenTwoDate(String firstString, String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
            // 日期型字符串格式错误
        }

        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    /**
     * 
     * @Description:以友好的方式显示时间
     * @param sdate
     * @param @return
     * @return String
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.format(cal.getTime());
        String paramDate = dateFormater2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.format(time);
        }
        return ftime;
    }

    /**
     * 
     * @Description:获取星期
     * @param num
     * @param format
     * @param @return
     * @return String
     */
    public static String getWeek(int num, String format) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int weekNum = c.get(Calendar.DAY_OF_WEEK) + num;
        if (weekNum > 7)
            weekNum = weekNum - 7;
        return format + WEEK[weekNum - 1];
    }

    /**
     * 
     * @Description:
     * @param @return
     * @return String
     */
    public static String getZhouWeek() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(new Date(System.currentTimeMillis())) + " " + getWeek(0, ZHOU);
    }

    /**
     * 
     * @Description:取得当前日期
     * @param timesamp
     * @param @return
     * @return String
     */
    public static String getDay(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
        case 0:
            result = "今天";
            break;
        case 1:
            result = "昨天";
            break;
        case 2:
            result = "前天";
            break;

        default:
            result = temp + "天前";
            break;
        }

        return result;
    }

    /**
     * 
     * @Description:判断给定字符串时间是否为今日
     * @param sdate
     * @param @return
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.format(today);
            String timeDate = dateFormater2.format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 根据日期选择性加零 如2013-3-15 编程2013-03-15
     * 
     * @param date
     *            要加零月份和日期
     */
    public static String addZeroForDate(int date) {
        String newDate = String.valueOf(date);
        if (newDate.length() < 2) {
            newDate = "0" + date;
        }
        return newDate;
    }

    /**
     * 将字符串中的所有字符转换为全角字符
     * 
     * @param inputStr
     *            输入字符串
     * @return 返回字符串
     */
    public static String toDBC(String inputStr) {
        char[] c = inputStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 生成不同一串由不同大小字体组成的字符
     * 
     * @param head
     *            加粗
     * @param middle
     * @param end
     * @return
     */
    public static SpannableStringBuilder getTextWithDifferentSize(String head, String middle, String end, float size) {
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(head);
        strBuilder.append(middle).append(end);
        strBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, head.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new RelativeSizeSpan(size), 0, head.length() + middle.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return strBuilder;
    }

    /**
     * 说明：检查字符串是否是空字符串或是null
     * 
     * @param string
     *            待检验的字符串
     * 
     * @return true: 如果字符串不为null且不为空，false：字符串为空或Null
     */
    public static boolean isNotNull(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return false;
        }

        return true;
    }

    /**
     * 说明：检查字符串是否是空字符串或是null
     * 
     * @param string
     *            待检验的字符串
     * 
     * @return true: 如果字符串为空或Null，false：字符串不为null且不为空
     */
    public static boolean isNull(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }

        return false;
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     * 
     * @param str
     *            指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 
     * @Description:判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串
     *                            若输入字符串为null或空字符串，返回true
     * @param input
     * @param @return
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        if (str == null || "".equals(str))
            return true;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @Description:判断是不是一个合法的电子邮件地址
     * @param email
     * @param @return
     * @return boolean
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 将String型转换为Int型
     * 
     * @param intstr
     * @return
     */
    public static int stringTolnt(String intstr) {
        Integer integer;
        integer = Integer.valueOf(intstr);
        return integer.intValue();
    }

    /**
     * 
     * @Description:对象转整数
     * @param obj
     * @param @return
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 
     * @Description:字符串转布尔值
     * @param b
     * @param @return
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 
     * @Description:格式化soap传过来的date+time数据
     * @param soapDateTime
     * @param @return
     * @return String
     */
    public static String formatSoapDateTime(String soapDateTime) {
        String returnString = soapDateTime.substring(0, 19).replace("T", " ");
        return returnString;
    }

    /**
     * 
     * @Description:
     * @param anyType
     * @param @return
     * @return String
     */
    public static String formatSoapNullString(String anyType) {
        String returnString = anyType.equals("anyType{}") ? "" : anyType;
        return returnString;
    }

    /**
     * 
     * @Description:检查邮箱输入格式是否正确
     * @param email
     * @param @return
     * @return boolean
     */
    public static boolean checkEmailInput(String email) {
        p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
        m = p.matcher(email);
        return m.matches();
    }

    /**
     * 
     * @Description:###检查账号是否由 数字、字母、下划线 组合而成 是则返回true，否则返回false## 账号可为任意字符，包括中文
     * @param username
     * @param @return
     * @return boolean
     */
    public static boolean checkUsernameInput(String username) {
        // p = Pattern.compile("^\\w+$");
        // m = p.matcher(username);
        return 1 > 0;
    }

    /**
     * 
     * @Description:检查两次输入的密码是否一致
     * @param password
     * @param password2
     * @param @return
     * @return boolean
     */
    public static boolean check2Password(String password, String password2) {
        return password.equals(password2);
    }

    /**
     * 
     * @Description:生成用户id，用时间生成
     * @param @return
     * @return String
     */
    public static String date2UserId() {
        String time = dateFormatUserId.format(new Date());
        return time;
    }

    /**
     * 
     * @Description:格式化生成当前时间
     * @param @return
     * @return Date
     */
    public static Date genCurrentDate() {
        Date date = null;
        try {
            String now = dateFormater.format(new Date());
            // DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormater.parse(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 
     * @Description:格式化boolean字符串
     * @param booleanStr
     * @param @return
     * @return boolean
     */
    public static boolean formatBoolean(String booleanStr) {
        if ("true".equalsIgnoreCase(booleanStr)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将Int型转换为String型
     * 
     * @param value
     * @return
     */
    public static String intToString(int value) {
        Integer integer = new Integer(value);
        return integer.toString();
    }

    /**
     * 将String型转换为float型
     * 
     * @param floatstr
     * @return
     */
    public static float stringToFloat(String floatstr) {
        Float floatee;
        floatee = Float.valueOf(floatstr);
        return floatee.floatValue();
    }

    /**
     * 将float型转换为String型
     * 
     * @param value
     * @return
     */
    public static String floatToString(float value) {
        Float floatee = new Float(value);
        return floatee.toString();
    }

    /**
     * 将String型转换为sqlDate型
     * 
     * @param dateStr
     * @return
     */
    public static java.sql.Date stringToDate(String dateStr) {
        return java.sql.Date.valueOf(dateStr);
    }

    /**
     * 将sqlDate型转换为String型
     * 
     * @param datee
     * @return
     */
    public static String dateToString(java.sql.Date datee) {
        return datee.toString();
    }

    /***
     * 
     * 得到天干地支
     * 
     * @return
     */
    public static String[] getYera() {
        String str[] = new String[107];

        int index1 = 1942;
        // int num = year - 1900 + 36;
        for (int i = 0; i < str.length; i++) {
            str[i] = cyclicalm(index1 - 1900 + 36) + "年(" + index1 + ")";
            index1++;
        }
        return str;
    }

    // ====== 传入 月日的offset 传回干支, 0=甲子
    public static String cyclicalm(int num) {
        final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
        final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    /**
     * 字符串数组换字符串
     * 
     * @param args
     * @return
     */
    public String getStr(String[] args) {
        String str = "";
        for (int i = 0; i < args.length; i++) {
            str += (String) args[i];
        }
        return str;
    }

    /**
     * 
     * @Description:long类型转成byte数组
     * @param number
     * @param @return
     * @return byte[]
     */
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 
     * @Description:byte数组转成long
     * @param b
     * @param @return
     * @return long
     */
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff; // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * 注释：int到字节数组的转换！
     * 
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * * 注释：字节数组到int的转换！
     * 
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    /**
     * * 注释：short到字节数组的转换！
     * 
     * @param s
     * @return
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * * 注释：字节数组到short的转换！
     * 
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 将int类型的数据转换为byte数组 原理：将int数据中的四个byte取出，分别存储
     * 
     * @param n
     *            int数据
     * @return 生成的byte数组
     */
    public static byte[] intToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    /**
     * 将byte数组转换为int数据
     * 
     * @param b
     *            字节数组
     * @return 生成的int数据
     */
    public static int byteToInt2(byte[] b) {
        return (((int) b[0]) << 24) + (((int) b[1]) << 16) + (((int) b[2]) << 8) + b[3];
    }

    public static float byteToFloat(byte[] v) {
        ByteBuffer bb = ByteBuffer.wrap(v);
        FloatBuffer fb = bb.asFloatBuffer();
        return fb.get();
    }

    public static byte[] floatToByte(float v) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] ret = new byte[4];
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(v);
        bb.get(ret);
        return ret;
    }

    public int getUnsignedByte(byte data) { // 将data字节型数据转换为0~255 (0xFF 即BYTE)。
        return data & 0x0FF;
    }

    public int getUnsignedByte(short data) { // 将data字节型数据转换为0~65535 (0xFFFF 即
                                             // WORD)。
        return data & 0x0FFFF;
    }

    public long getUnsignedIntt(int data) { // 将int数据转换为0~4294967295
                                            // (0xFFFFFFFF即DWORD)。
        return data & 0x0FFFFFFFFl;
    }

    /**
     * float转byte[]
     * 
     * @param v
     * @return
     */
    public static byte[] floatToByte_new(float v) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] ret = new byte[4];
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(v);
        bb.get(ret);
        return ret;
    }

    /**
     * byte[]转float
     * 
     * @param v
     * @return
     */
    public static float byteToFloat_new(byte[] v) {
        ByteBuffer bb = ByteBuffer.wrap(v);
        FloatBuffer fb = bb.asFloatBuffer();
        return fb.get();
    }

    /**
     * 获得一个0~max之间的随机数
     * 
     * @param max
     * @return
     */
    public static int getRandomInt(final int max) {
        int min = 0;
        return (int) (Math.random() * max + min);
    }

    /**
     * 获得一个从0~max之间的随机数组,数组中只有四个随机数
     * 
     * @param max
     * @return
     */
    public static int[] getRandomIntArr(final int max) {
        int min = 0;
        int[] ints = { -1, -1, -1, -1 };
        int temp = -1;
        int index = 0;

        // 由于很容易产生重复的随机数，所以，循环次数超过四次
        for (int i = 0; i < 10; i++) {

            // 如果已经产生了4个不同的随机数，则退出循环
            if (index >= 4) {
                break;
            }

            temp = (int) (Math.random() * max + min);

            // 检查该随机数是否已经存在
            boolean flag = false;
            for (int j = 0; j < 4; j++) {
                if (ints[j] == temp) {
                    flag = true;
                    break;
                }
            }

            // 如果该随机数不存在，则添加到ints
            if (!flag) {
                ints[index] = temp;
                index = index + 1;

            }
        }
        return ints;
    }

    /**
     * 转换答案。0-->A 1--
     * 
     * @param answer
     * @return
     */
    public static String convertAnswer(int answer) {
        String result = null;
        switch (answer) {
        case 0: {
            result = "A";
            break;
        }
        case 1: {
            result = "B";
            break;
        }
        case 2: {
            result = "C";
            break;
        }
        case 3: {
            result = "D";
            break;
        }
        }
        return result;
    }

    /**
     * 去掉filename中的特殊字符（\ :）
     * 
     * @param title
     * @return
     */
    public static String FilterFilename(String filename) {
        String temp = null;
        // 如果长度太长，并
        if (filename.length() > 30) {
            temp = filename.substring(0, 30).replace("\\", "").replace(":", "").replace("'", "");
        } else {
            temp = filename.replace("\\", "").replace(":", "").replace("'", "");
        }

        return temp.trim();
    }

    /**
     * 
     * @Description:将double型转换为String型
     * @param value
     * @param @return
     * @return String
     */
    public static String doubleToString(double value) {
        Double doublestr = new Double(value);
        return doublestr.toString();
    }

    /**
     * 
     * @Description:将String型转换为double型
     * @param value
     * @param @return
     * @return double
     */
    public static double StringTodouble(String value) {
        double strdouble = Double.parseDouble(value);
        return strdouble;
    }

    private static final float UNIT = 1000.0F;

    /**
     * 毫秒转秒
     * 
     * @param time
     *            毫秒
     * @return
     */
    public static float ms2s(long time) {
        return time / UNIT;
    }

    /**
     * 微秒转秒
     * 
     * @param time
     *            微秒
     * @return
     */
    public static float us2s(long time) {
        return time / UNIT / UNIT;
    }

    /**
     * 纳秒转秒
     * 
     * @param time
     *            纳秒
     * @return
     */
    public static float ns2s(long time) {
        return time / UNIT / UNIT / UNIT;
    }

    /**
     * 转换字符串为boolean
     * 
     * @param str
     * @return
     */
    public static boolean toBoolean(String str) {
        return toBoolean(str, false);
    }

    /**
     * 转换字符串为boolean
     * 
     * @param str
     * @param def
     * @return
     */
    public static boolean toBoolean(String str, boolean def) {
        if (StringUtils.isEmpty(str))
            return def;
        if ("false".equalsIgnoreCase(str) || "0".equals(str))
            return false;
        else if ("true".equalsIgnoreCase(str) || "1".equals(str))
            return true;
        else
            return def;
    }

    /**
     * 转换字符串为float
     * 
     * @param str
     * @return
     */
    public static float toFloat(String str) {
        return toFloat(str, 0F);
    }

    /**
     * 转换字符串为float
     * 
     * @param str
     * @param def
     * @return
     */
    public static float toFloat(String str, float def) {
        if (StringUtils.isEmpty(str))
            return def;
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 转换字符串为long
     * 
     * @param str
     * @return
     */
    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    /**
     * 转换字符串为long
     * 
     * @param str
     * @param def
     * @return
     */
    public static long toLong(String str, long def) {
        if (StringUtils.isEmpty(str))
            return def;
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 转换字符串为short
     * 
     * @param str
     * @return
     */
    public static short toShort(String str) {
        return toShort(str, (short) 0);
    }

    /**
     * 转换字符串为short
     * 
     * @param str
     * @param def
     * @return
     */
    public static short toShort(String str, short def) {
        if (StringUtils.isEmpty(str))
            return def;
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 转换字符串为int
     * 
     * @param str
     * @return
     */
    public static int toInt(String str) {
        return toInt(str, 0);
    }

    /**
     * 转换字符串为int
     * 
     * @param str
     * @param def
     * @return
     */
    public static int toInt(String str, int def) {
        if (StringUtils.isEmpty(str))
            return def;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static String toString(Object o) {
        return toString(o, "");
    }

    public static String toString(Object o, String def) {
        if (o == null)
            return def;

        return o.toString();
    }

    public static int dipToPX(final Context ctx, int dip) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dip / 1.5D * scale + 0.5D);
    }

    public static String getPlayCountStr(int playCount) {
        String playCountStr = "";
        if (playCount > 9999) { // 播放次数为一万次以上，则直接显示如“5.7万”
            playCountStr = String.format("%.1f", playCount / 10000.0) + "万";
        } else {
            playCountStr = playCount + "";
        }
        return playCountStr;
    }

    public static String getShareCountStr(int shareCount) {
        String shareCountStr = "";
        if (shareCount > 9999) {
            shareCountStr = String.format("%.1f", shareCount / 10000.0) + "万";
        } else {
            shareCountStr = shareCount + "";
        }
        return shareCountStr;
    }

}
