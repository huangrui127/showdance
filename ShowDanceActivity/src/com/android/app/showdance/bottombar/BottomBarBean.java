package com.android.app.showdance.bottombar;

import com.android.app.wumeiniang.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 功能：底部导航栏Bean
 * 
 * @author djd
 *
 */
@SuppressLint("NewApi")
public class BottomBarBean {
    private RelativeLayout mMainBtn;
    private ImageView mMainIV;
    private TextView mMainTV;
    private RelativeLayout mShootBtn;
    private ImageView mShootIV;
    private TextView mShootTV;
    private RelativeLayout mMineBtn;
    private ImageView mMineIV;
    private TextView mMineTV;
    private Context mContext;

    public BottomBarBean(Context context, RelativeLayout bottomBarView) {
        mContext = context;
        mMainBtn = (RelativeLayout) bottomBarView.findViewById(R.id.id_main_layout);
        mMainIV = (ImageView) bottomBarView.findViewById(R.id.id_main_iv);
        mMainTV = (TextView) bottomBarView.findViewById(R.id.id_main_tv);
        mShootBtn = (RelativeLayout) bottomBarView.findViewById(R.id.id_shoot_layout);
        mShootIV = (ImageView) bottomBarView.findViewById(R.id.id_shoot_iv);
        mShootTV = (TextView) bottomBarView.findViewById(R.id.id_shoot_tv);
        mMineBtn = (RelativeLayout) bottomBarView.findViewById(R.id.id_mine_layout);
        mMineIV = (ImageView) bottomBarView.findViewById(R.id.id_mine_iv);
        mMineTV = (TextView) bottomBarView.findViewById(R.id.id_mine_tv);
    }

    public RelativeLayout getmMainBtn() {
        return mMainBtn;
    }

    public RelativeLayout getmShootBtn() {
        return mShootBtn;
    }

    public RelativeLayout getmMineBtn() {
        return mMineBtn;
    }

    public void setMainBtnNormal() { // 因兼容性问题，这里使用ContextCompat.getDrawable()/ContextCompat.getColor()方法
        mMainIV.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.menu1));
        mMainTV.setTextColor(ContextCompat.getColor(mContext, R.color.host_text_color_normal));
    }

    public void setMainBtnPressed() {
        mMainIV.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.menu1_focus));
        mMainTV.setTextColor(ContextCompat.getColor(mContext, R.color.btn_select_bg));
    }

    public void setShootBtnNormal() {
        mShootIV.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.menu2));
        mShootTV.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mShootBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btn_select_bg));
    }

    public void setShootBtnPressed() {
        mShootIV.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.menu2_focus));
        mShootTV.setTextColor(ContextCompat.getColor(mContext, R.color.btn_shoot_text_select_color));
        // mShootBtn.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setMineBtnNormal() {
        mMineIV.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.menu3));
        mMineTV.setTextColor(ContextCompat.getColor(mContext, R.color.host_text_color_normal));
    }

    public void setMineBtnPressed() {
        mMineIV.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.menu3_focus));
        mMineTV.setTextColor(ContextCompat.getColor(mContext, R.color.btn_select_bg));
    }
}
