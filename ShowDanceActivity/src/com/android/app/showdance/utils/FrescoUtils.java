package com.android.app.showdance.utils;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.net.Uri;

public class FrescoUtils {
    /**
     * 加载本地图片（drawable图片）
     * 
     * @param context
     * @param simpleDraweeView
     * @param id
     */
    public static void loadResPic(Context context, SimpleDraweeView simpleDraweeView, int id) {
        Uri uri = Uri.parse("res://" + context.getPackageName() + "/" + id);
        simpleDraweeView.setImageURI(uri);
    }

    /**
     * 加载本地图片（assets图片）
     * 
     * @param context
     * @param simpleDraweeView
     * @param nameWithSuffix
     *            带后缀的名称
     */
    public static void loadAssetsPic(Context context, SimpleDraweeView simpleDraweeView, String nameWithSuffix) {
        Uri uri = Uri.parse("asset:///" + nameWithSuffix);
        simpleDraweeView.setImageURI(uri);
    }
}
