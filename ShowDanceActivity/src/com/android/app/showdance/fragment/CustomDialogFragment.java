package com.android.app.showdance.fragment;

import com.android.app.wumeiniang.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class CustomDialogFragment extends android.support.v4.app.DialogFragment{

    private static int mLayoutId = 0;
    
    public static CustomDialogFragment newInstance(int dialogLayoutId){
        mLayoutId = dialogLayoutId;
        return new CustomDialogFragment();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLayoutId == 0) {
            mLayoutId = R.layout.loading_progressbar_dialog;
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(mLayoutId, null);
    }
    
}
