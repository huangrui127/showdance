package com.android.app.showdance.ui;

import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UseIntroductionActivity extends BaseActivity {

    private boolean progressFalg; // 是否开启进度提示
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_introduce);
        findViewById();
        setOnClickListener();
        initWebView();
    }

    @Override
    protected void findViewById() {
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        return_imgbtn.setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        Intent i = getIntent();
        String title = null;
        if (i != null) {
            title = i.getStringExtra("title");
        }
        if (TextUtils.isEmpty(title))
            title = "使用说明";
        tvTitle.setText(title);
        mWebView = (WebView) findViewById(R.id.video);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initWebView() {
        if (NetUtil.getNetworkState(InitApplication.getRealContext()) == NetUtil.NETWORN_NONE) {
            Toast.makeText(InitApplication.getRealContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetUtil.isWifiConnected(InitApplication.getRealContext())) {// 已开启wifi网络
            initWebViewMethod();
        } else {// 未开启wifi网络
            new CustomAlertDialog(this).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示")
                    .setMsg("WIFI网络未开启,是否继续使用2G、3G或4G网络!").setPositiveButton("确  认", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initWebViewMethod();
                        }
                    }).setNegativeButton("取  消", new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewMethod() {
        Intent i = getIntent();
        if (i == null) {
            finish();
            return;
        }
        Uri helpUrl = i.getData();
        if (helpUrl == null)
            return;
        mWebView.destroyDrawingCache();
        mWebView.clearCache(true);
        mWebView.requestFocus(); // 防止点击无响应，获取焦点
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); // 开启JavaScript支持
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    dismissDialog();
                } else if (!progressFalg) {
                    openProgress();
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mqqwpa")) { // 需要打开外部QQ，不加载此Url，直接打开QQ
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });

        mWebView.loadUrl(helpUrl.toString());
        mWebView.onResume();
    }

    private void openProgress() {
        progressFalg = true;
        mDialog = new AlertDialog.Builder(this).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
    }

    @Override
    public void refresh(Object... param) {

    }

    @Override
    protected boolean validateData() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            break;
        }
    }

    private void dismissDialog() {
        progressFalg = false;
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack(); // 返回前一页
            return true;
        } else if (keyCode == event.KEYCODE_BACK && !mWebView.canGoBack()) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
