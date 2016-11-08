package com.android.app.showdance.bottombar;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 功能：底部导航栏
 * @author djd
 *
 */
public class BottomBar {
    
    private BottomBarBean mBottomBarBean;
    private RelativeLayout mMainBtn;
    private RelativeLayout mShootBtn;
    private RelativeLayout mMineBtn;
    
    private BottomBarClickListener mBottomBarClickListener;
    
    public void setBottomBarClickListener(BottomBarClickListener listener){
        mBottomBarClickListener = listener;
    }
    
    public BottomBar(Context context,RelativeLayout bottomBarView) {
        init(context,bottomBarView);
        initClickListener();
    }
    
    private void init(Context context,RelativeLayout bottomBarView){
        mBottomBarBean = new BottomBarBean(context,bottomBarView);
        mMainBtn = mBottomBarBean.getmMainBtn();
        mShootBtn = mBottomBarBean.getmShootBtn();
        mMineBtn = mBottomBarBean.getmMineBtn();
    }
    
    private void initClickListener(){
        mMainBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mBottomBarClickListener.mainClick();
            }
        });
        mShootBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mBottomBarClickListener.shootClick();
            }
        });
        mMineBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mBottomBarClickListener.mineClick();
            }
        });
    }
    
    
    
    public void mainBtnClickableColorChange(){
        mMainBtn.setClickable(false);
        mShootBtn.setClickable(true);
        mMineBtn.setClickable(true);
        mBottomBarBean.setMainBtnPressed();
        mBottomBarBean.setShootBtnNormal();
        mBottomBarBean.setMineBtnNormal();
    }
    
    public void shootBtnClickableColorChange(){
        mShootBtn.setClickable(false);
        mMainBtn.setClickable(true);
        mMineBtn.setClickable(true);
        mBottomBarBean.setShootBtnPressed();
        mBottomBarBean.setMainBtnNormal();
        mBottomBarBean.setMineBtnNormal();
    }
    
    public void mineBtnClickableColorChange(){
        mMineBtn.setClickable(false);
        mMainBtn.setClickable(true);
        mShootBtn.setClickable(true);
        mBottomBarBean.setMineBtnPressed();
        mBottomBarBean.setMainBtnNormal();
        mBottomBarBean.setShootBtnNormal();
    }
}
