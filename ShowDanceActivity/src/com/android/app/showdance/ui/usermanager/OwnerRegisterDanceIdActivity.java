package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.Map;

import com.android.app.showdance.logic.MainService;
import com.android.app.showdance.logic.Task;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.MD5Util;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 【注册-秀舞吧号Id】页面
 **/

public class OwnerRegisterDanceIdActivity extends BaseActivity {

    private EditText setting_dance_id_edt; // 设置秀舞吧号
    private String dance_id_Str; // 注册吧号
    private EditText setting_pwd_edt; // 设置密码
    private String password;
    private String passwordMD5; // 加密的密码
    private EditText setting_pwd_again_edt; // 再次输入密码
    private String passwordAgain;
    private EditText write_recommendCode_edt; // 填写推荐码
    private String recommendCode;
    private Button btn_clear_search_text1;
    private Button btn_clear_search_text2;
    private Button btn_clear_search_text3;
    private Button btn_clear_search_text4;
    private Button settingDanceId_RegisterBtn; // 设置秀舞吧号和密码后，注册

    private String mobilephone; // 注册手机号
    private double longitude; // 经度
    private double latitude; // 纬度
    private String address; // 居住地
    private String phone; // phone取值为邀请人手机号/吧号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register_dance_id);
        findViewById();
        initView();
        setOnClickListener();

    }

    @Override
    protected void findViewById() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        setting_dance_id_edt = (EditText) findViewById(R.id.setting_dance_id_edt);
        setting_pwd_edt = (EditText) findViewById(R.id.setting_pwd_edt);
        setting_pwd_again_edt = (EditText) findViewById(R.id.setting_pwd_again_edt);
        write_recommendCode_edt = (EditText) findViewById(R.id.write_recommendCode_edt);
        btn_clear_search_text1 = (Button) findViewById(R.id.btn_clear_search_text1);
        btn_clear_search_text2 = (Button) findViewById(R.id.btn_clear_search_text2);
        btn_clear_search_text3 = (Button) findViewById(R.id.btn_clear_search_text3);
        btn_clear_search_text4 = (Button) findViewById(R.id.btn_clear_search_text4);
        settingDanceId_RegisterBtn = (Button) findViewById(R.id.settingDanceId_RegisterBtn);
    }

    @Override
    protected void initView() {
        tvTitle.setText("设置秀舞吧号");
        return_imgbtn.setVisibility(View.VISIBLE);

        // 获取手机号码
        mobilephone = getIntent().getStringExtra("mobilephone");

        // longitude = InitApplication.mLocation.getLongitude(); // 经度
        // latitude = InitApplication.mLocation.getLatitude(); // 纬度
        // address = InitApplication.mLocation.getAddrStr();// 地址

    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
        btn_clear_search_text1.setOnClickListener(this);
        btn_clear_search_text2.setOnClickListener(this);
        btn_clear_search_text3.setOnClickListener(this);
        btn_clear_search_text4.setOnClickListener(this);
        settingDanceId_RegisterBtn.setOnClickListener(this);
        // 秀舞吧号输入框
        setting_dance_id_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = setting_dance_id_edt.getText().length();
                if (textLength > 0) {
                    btn_clear_search_text1.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_search_text1.setVisibility(View.GONE);
                }
            }
        });

        // 密码输入框
        setting_pwd_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = setting_pwd_edt.getText().length();
                if (textLength > 0) {
                    btn_clear_search_text2.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_search_text2.setVisibility(View.GONE);
                }
            }
        });

        // 再次输入密码框
        setting_pwd_again_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = setting_pwd_again_edt.getText().length();
                if (textLength > 0) {
                    btn_clear_search_text3.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_search_text3.setVisibility(View.GONE);
                }
            }
        });

        // 推荐码文本输入框
        write_recommendCode_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = write_recommendCode_edt.getText().length();
                if (textLength > 0) {
                    btn_clear_search_text4.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_search_text4.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:
            this.finish();
            break;

        case R.id.btn_clear_search_text1: // 清空输入框1
            setting_dance_id_edt.setText("");
            btn_clear_search_text1.setVisibility(View.GONE);
            break;

        case R.id.btn_clear_search_text2: // 清空输入框2
            setting_pwd_edt.setText("");
            btn_clear_search_text2.setVisibility(View.GONE);
            break;

        case R.id.btn_clear_search_text3: // 清空输入框3
            setting_pwd_again_edt.setText("");
            btn_clear_search_text3.setVisibility(View.GONE);
            break;

        case R.id.btn_clear_search_text4: // 清空输入框4
            write_recommendCode_edt.setText("");
            btn_clear_search_text4.setVisibility(View.GONE);
            break;

        case R.id.settingDanceId_RegisterBtn: // (设置秀舞吧号后) 注册
            if (validateData()) {

                showProgressDialog(this, 0); // 注册
            }

            break;

        default:
            break;
        }
    }

    @Override
    protected boolean validateData() {
        dance_id_Str = setting_dance_id_edt.getText().toString();// 艺名
        password = setting_pwd_edt.getText().toString(); // 密码
        passwordAgain = setting_pwd_again_edt.getText().toString();
        phone = write_recommendCode_edt.getText().toString(); // 注册邀请码

        boolean flag = true;
        // 判断输入框不能为空
        if (TextUtils.isEmpty(dance_id_Str)) {
            Toast.makeText(getApplicationContext(), "请设置秀舞吧号", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "请设置密码", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (TextUtils.isEmpty(passwordAgain)) {
            Toast.makeText(getApplicationContext(), "确认密码不能为空", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        // 判断确认密码与输入密码是否一致
        if (password != null && !password.equals("") && passwordAgain != null && !passwordAgain.equals("")) {
            if (!(passwordAgain.equals(password))) {
                Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
                flag = false;
            }
        }
        return flag;

    }

    public void showProgressDialog(Context mContext, int type) {
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

        UserVo registerUserVo = new UserVo();
        paramsMap = new HashMap<String, Object>();
        Task mTask;

        switch (type) {
        case 0: // 设置秀舞吧号和密码后，进行注册

            registerUserVo.setMobilephone(mobilephone);// 注册的手机号码（值是上一个页面传过来的）
            registerUserVo.setLoginName(dance_id_Str); // 秀舞吧号
            // 密码加密后，传给后台
            passwordMD5 = MD5Util.MD5Encode(password);
            registerUserVo.setPassword(passwordMD5); // 加密的密码
            // 自动定位后获取经纬度和位置
            // "longitude":"经度","latitude":"纬度","address":"居住地"
            registerUserVo.setLongitude(StringUtils.doubleToString(longitude));
            registerUserVo.setLatitude(StringUtils.doubleToString(latitude));
            registerUserVo.setAddress(address);
            registerUserVo.setPhone(phone); // 注册邀请码

            paramsMap.put("registerUserVo", registerUserVo);
            mTask = new Task(TaskType.TS_registerUSER, paramsMap);
            MainService.newTask(mTask);
            break;

        default:
            break;
        }

    }

    @Override
    public void refresh(Object... param) {
        int type = (Integer) param[0];
        switch (type) {
        case TaskType.TS_registerUSER: // 注册
            mDialog.dismiss();
            if (ConstantsUtil.NetworkStatus) {
                Map<String, Object> map = (HashMap<String, Object>) param[1];
                String result = map.get("result").toString();
                if (!result.equals("-1")) {
                    if (result.equals("0")) {
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        // 将手机号存储到配置文件的用户信息userInfo中，在登录的时候会用到它。
                        UserInfo userInfo = new UserInfo();
                        userInfo.setMobilephone(mobilephone);
                        InitApplication.mSpUtil.setUserInfo(userInfo);
                        // 跳转
                        Intent mIntent = new Intent();
                        Bundle bundle = new Bundle();
                        mIntent.setClass(this, OwnerPhoneRegisterActivity.class);
                        bundle.putString("phone", mobilephone);
                        bundle.putString("password", password);
                        mIntent.putExtras(bundle);

                        setResult(RESULT_OK, mIntent);
                        this.finish();// 关闭当前页面

                    } else if (result.equals("1")) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
            }
            break;

        default:
            break;
        }

    }

}
