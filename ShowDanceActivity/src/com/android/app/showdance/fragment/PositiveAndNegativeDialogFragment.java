package com.android.app.showdance.fragment;

import com.android.app.wumeiniang.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PositiveAndNegativeDialogFragment extends DialogFragment {

    private TextView mTitleTV;
    private TextView mMessageTV;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private static Context mContext;

    private String mTitile;
    private String mPositiveStr;
    private String mNegativeStr;
    private DialogInterface.OnClickListener mClickListener;

    public PositiveAndNegativeDialogFragment(String dialogTitle, String dialogPositiveStr, String dialogNegativeStr,
            DialogInterface.OnClickListener dialogClickListener) {
        mTitile = dialogTitle;
        mPositiveStr = dialogPositiveStr;
        mNegativeStr = dialogNegativeStr;
        mClickListener = dialogClickListener;
    }

    // public static PositiveAndNegativeDialogFragment newInstance(Context
    // context){
    // mContext = context;
    // return new PositiveAndNegativeDialogFragment();
    // }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new AlertDialog.Builder(mContext);
        // LayoutInflater inflater = LayoutInflater.from(mContext);
        // View view = inflater.inflate(R.layout.custom_alertdialog, null);
        // initCustomDialogView(view);
        // builder.setView(view);
        builder.setTitle(mTitile).setPositiveButton(mPositiveStr, mClickListener)
                .setNegativeButton(mNegativeStr, mClickListener).setCancelable(false);
        return builder.create();
    }

    private void initCustomDialogView(View view) {
        mTitleTV = (TextView) view.findViewById(R.id.txt_title);
        mMessageTV = (TextView) view.findViewById(R.id.txt_msg);
        mNegativeBtn = (Button) view.findViewById(R.id.btn_neg);
        mPositiveBtn = (Button) view.findViewById(R.id.btn_pos);
    }

    public void setTitle(String dialogTitle) {
        if ("".equals(dialogTitle)) {
            mTitleTV.setText("标题");
        } else {
            mTitleTV.setText(dialogTitle);
        }
    }

    public void setMsg(String dialogMessage) {
        if ("".equals(dialogMessage)) {
            mMessageTV.setText("内容");
        } else {
            mMessageTV.setText(dialogMessage);
        }
    }

    public void setPositiveButton(String dialogPositiveStr, final OnClickListener listener) {
        if ("".equals(dialogPositiveStr)) {
            mPositiveBtn.setText(getResources().getString(R.string.dialog_ok));
        } else {
            mPositiveBtn.setText(dialogPositiveStr);
        }
        mPositiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    public void setNegativeButton(String dialogNegativeStr, final OnClickListener listener) {
        if ("".equals(dialogNegativeStr)) {
            mNegativeBtn.setText(getResources().getString(R.string.dialog_cancel));
        } else {
            mNegativeBtn.setText(dialogNegativeStr);
        }
        mNegativeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

}
