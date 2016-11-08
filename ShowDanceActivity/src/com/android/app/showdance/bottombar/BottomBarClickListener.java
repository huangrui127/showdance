package com.android.app.showdance.bottombar;

/**
 * 功能：底部导航栏点击事件接口
 * @author djd
 *
 */
public interface BottomBarClickListener {
    /**
     * 功能：导航栏“首页”按钮点击事件
     */
    void mainClick();
    /**
     * 功能：导航栏“拍摄”按钮点击事件
     */
    void shootClick();
    /**
     * 功能：导航栏“我的”按钮点击事件
     */
    void mineClick();
}
