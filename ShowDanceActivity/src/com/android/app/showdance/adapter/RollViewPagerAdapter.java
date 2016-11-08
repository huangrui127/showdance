package com.android.app.showdance.adapter;

import java.util.List;

import com.android.app.showdance.model.glmodel.AdPicAndIconInfo.Ad;
import com.android.app.showdance.utils.L;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

/**
 * 功能：首页广告图片轮播Adapter
 * 
 * @author djd
 */
public class RollViewPagerAdapter extends LoopPagerAdapter {

    private static final String TAG = "RollViewPagerAdapter";

    private List<Ad> mAds;
    private Context mContext;

    public RollViewPagerAdapter(Context context,RollPagerView viewPager, List<Ad> ads) {
        super(viewPager);
        mContext = context;
        mAds = ads;
    }

    public void setAds(List<Ad> ads) {
        mAds = ads;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        SimpleDraweeView sdv = new SimpleDraweeView(mContext);
        sdv.setScaleType(ScaleType.CENTER_INSIDE);
        sdv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if(mAds != null){
            sdv.setImageURI(mAds.get(position).getPic());
        }else {
            L.e(TAG, "没有广告轮播图！");
        }
        return sdv;
    }

    @Override
    public int getRealCount() {
        return mAds.size();
    }

}
