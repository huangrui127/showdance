package com.android.app.showdance.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;

/**
 * 功能：高德定位相关工具类
 * 
 * @author djd
 *
 */
public class AMapLocationUtils {

    /**
     * 默认的高德定位参数
     */
    public static AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);// 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);// 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);// 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);// 可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);// 可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);// 可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);// 设置setOnceLocationLatest(booleanb)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(booleanb)接口也会被设置为true，反之不会，默认为fals
        mOption.setWifiActiveScan(true); // 设置是否强制刷新WIFI，默认为强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);// 可选，设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    public synchronized static String getLocationStr(AMapLocation location) {

        return null;
    }
}
