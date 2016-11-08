package com.android.app.showdance.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.glmodel.SignUpInfo.User;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("deprecation")
public class SharePreference {
    public static final String preference = "preference";
    private SharedPreferences sp;
    private SharedPreferences msuicsp;
    private SharedPreferences.Editor editor;

    private String isCreated = "isCreated";// 首次创建快捷方式
    private String firstStart = "firstStart";// 首次启动

    private String rememberPwd = "rememberPwd";// 记住密码
    private String userName = "userName";// 用户名
    private String password = "password";// 密码

    // 循环设置几个值的逻辑意思
    // 1)配置文件默认设置为-1,
    // 2)进入秀舞界面onCreate()方法设置1,防止进入onResume()方法重复调用接口,
    // 3)没登录时进入登录界面登录成功后设置为2,再返回秀舞界面进入onResume()后首次调用接口,
    // 4)进入onResume()首次调用接口成功后再设置为0,防止再次进入onResume()方法重复调接口;

    private String firstRefeshShowDance = "firstRefeshShowDance";// 首次刷新秀舞界面

    private String cityPosition = "cityPosition";// 选中城市位置
    private String locationMyCity = "locationMyCity";// 定位的城市
    private String changeOtherCity = "changeOtherCity";// 切换其他城市
    private String selectMyCity = "selectMyCity";// 选择我的城市
    private String cityId;

    private String provinceName = "provinceName"; // 定位的省名称
    private String cityName = "cityName"; // 定位的市名称
    private String chooseLocation = "chooseLocation"; // 用户手动选择的位置
    private String longitude = "longitude"; // 获取的定位经度
    private String latitude = "latitude"; // 获取的定位纬度 
//    private String uploadVideoName = "uploadVideoName"; // 上传的视频名称
    private String theme = "theme"; // 主题活动名称
    private String themeType = "themeType"; // 主题活动类型
    
    private UserInfo userInfo;// 用户信息
    private User user;// 用户信息

    private String isMainFragmentNeedRefresh = "isMainFragmentNeedRefresh"; // Fragment是否需要进行刷新操作
    private String isMineFragmentNeedRefresh = "isMineFragmentNeedRefresh";
    
    public SharePreference(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        msuicsp = context.getSharedPreferences("music", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setMineFragmentNeedRefresh(Boolean isNeed){
        editor.putBoolean(isMineFragmentNeedRefresh, isNeed);
        editor.commit();
    }
    
    public boolean getIsMineFragmentNeedRefresh(){
        return sp.getBoolean(isMineFragmentNeedRefresh, false);
    }
    
    public void setMainFragmentNeedRefresh(Boolean isNeed){
        editor.putBoolean(isMainFragmentNeedRefresh, isNeed);
        editor.commit();
    }
    
    public boolean getIsMainFragmentNeedRefresh(){
        return sp.getBoolean(isMainFragmentNeedRefresh, false);
    }
    
    /**
     * 功能：保存经度（longitude）
     */
    public void setLongitude(String longitude){
        editor.putString(this.longitude, longitude);
        editor.commit();
    }
    
    public String getLongitude(){
        return sp.getString(this.longitude, "");
    }
    
    /**
     * 功能：保存纬度（longitude）
     */
    public void setLatitude(String latitude){
        editor.putString(this.latitude, latitude);
        editor.commit();
    }
    
    public String getLatitude(){
        return sp.getString(this.latitude, "");
    }
    
    /**
     * 功能：保存主题名称
     */
    public void setTheme(String theme){
        editor.putString(this.theme, theme);
        editor.commit();
    }
    
    public String getTheme(){
        return sp.getString(this.theme, "");
    }
    
    /**
     * 功能：保存主题类型
     */
    public void setThemeType(int themeType){
        editor.putInt(this.themeType, themeType);
        editor.commit();
    }
    
    public int getThemeType(){
        return sp.getInt(this.themeType, 0);
    }
    
    public void setUserChooseLocationName(String location){
        editor.putString(chooseLocation, location);
        editor.commit();
    }
    
    public String getUserChooseLocationName(){
        return sp.getString(chooseLocation, "");
    }
    
    public void setProvinceName(String province){
        editor.putString(provinceName, province);
        editor.commit();
    }
    
    public String getProvinceName(){
        return sp.getString(provinceName, "");
    }
    
    public void setCityName(String city){
        editor.putString(cityName, city);
        editor.commit();
    }
    
    public String getCityName(){
        return sp.getString(cityName, "");
    }
    
    public int getIsCreated() {
        return sp.getInt(isCreated, -1);
    }

    public void setIsCreated(int iscreated) {
        editor.putInt(isCreated, iscreated);
        editor.commit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    /**
     * 
     * @Description:
     * @param @return
     * @return String
     */
    public String getFirstStart() {
        return sp.getString(firstStart, "0");
    }

    public void setFirstStart(String firststart) {
        editor.putString(firstStart, firststart);
        editor.commit();
    }

    public String getUserName() {
        return sp.getString(userName, "");
    }

    public void setUserName(String UserName) {
        editor.putString(userName, UserName);
        editor.commit();
    }

    public boolean getRememberPwd() {
        return sp.getBoolean(rememberPwd, false);
    }

    public void setRememberPwd(boolean remember_Pwd) {
        editor.putBoolean(rememberPwd, remember_Pwd);
        editor.commit();
    }

    public String getPassword() {
        return sp.getString(password, "");
    }

    public void setPassword(String Password) {
        editor.putString(password, Password);
        editor.commit();
    }

    public int getFirstRefeshShowDance() {
        return sp.getInt(firstRefeshShowDance, -1);
    }

    public void setFirstRefeshShowDance(int FirstRefeshShowDance) {
        editor.putInt(firstRefeshShowDance, FirstRefeshShowDance);
        editor.commit();
    }
    
    public int getCityPosition() {
        return sp.getInt(cityPosition, -1);
    }

    public void setCityPosition(int cityposition) {
        editor.putInt(cityPosition, cityposition);
        editor.commit();
    }

    public String getLocationMyCity() {
        return sp.getString(locationMyCity, "");
    }

    public void setLocationMyCity(String LocationMyCity) {
        editor.putString(locationMyCity, LocationMyCity);
        editor.commit();
    }

    public String getSelectMyCity() {
        return sp.getString(selectMyCity, "");
    }

    public void setSelectMyCity(String SelectMyCity) {
        editor.putString(selectMyCity, SelectMyCity);
        editor.commit();
    }

    public Long getCityId() {
        return sp.getLong(cityId, 0L);
    }

    public void setCityId(Long CityId) {
        editor.putLong(cityId, CityId);
        editor.commit();
    }

    public String getChangeOtherCity() {
        return sp.getString(changeOtherCity, "");
    }

    public void setChangeOtherCity(String ChangeOtherCity) {
        editor.putString(changeOtherCity, ChangeOtherCity);
        editor.commit();
    }

    /**
     * ***存储 用户信息userInfo 在配置文件中
     * 
     * @return
     */
    public UserInfo getUserInfo() {

        String productBase64 = sp.getString("userInfo", "");
        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis = null;
        try {
            // 再次封装
            bis = new ObjectInputStream(bais);
            try {
                // 读取对象
                userInfo = (UserInfo) bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(userInfo);
            // 将字节流编码成base64的字符串
            String productBase64 = new String(Base64.encodeBase64(baos.toByteArray()));
            // Editor editor = sp.edit();
            editor.putString("userInfo", productBase64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public User getUser() {
        String productBase64 = sp.getString("user", "");
        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis = null;
        try {
            // 再次封装
            bis = new ObjectInputStream(bais);
            // 读取对象
            user = (User) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setUser(User user) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(user);
            // 将字节流编码成base64的字符窜
            String productBase64 = new String(Base64.encodeBase64(baos.toByteArray()));
            // Editor editor = sp.edit();
            editor.putString("user", productBase64);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public SharedPreferences getMusicSp() {
        return msuicsp;
    }
}
