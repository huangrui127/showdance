package com.android.app.showdance.ui.usermanager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.app.showdance.logic.RegisterCodeTimer;
import com.android.app.showdance.logic.RegisterCodeTimerService;
import com.android.app.showdance.logic.TaskType;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.SignUpInfo;
import com.android.app.showdance.model.glmodel.SignUpInfo.GetCodeResponse;
import com.android.app.showdance.model.glmodel.SignUpInfo.SignUpResponse;
import com.android.app.showdance.ui.VolleyBaseActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.RegularUtil;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 【验证手机号是否注册过】页面 Verify the phone number is registered
 **/

public class OwnerPhoneRegisterActivity extends VolleyBaseActivity {

    private EditText owner_register_phone_edt; // 测试手机号
    private String phoneStr;
    private EditText owner_register_code_edt; // 测试验证码
    private String codeStr;
    private Button register_GetCode_btn; // 获取验证码
    @SuppressWarnings("unused")
    private Integer sign;// 操作标记

    private Button btn_clear_search_text1;
    private Button btn_clear_search_text2;
    private Button owner_register_step_btn; // 下一步

    private Intent mIntent;
    private ReceiverCmdMsg mReceiverCmdMsg; //
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_phone_register);
        findViewById();
        initView();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        button.setVisibility(View.GONE);
    }

    @Override
    protected void findViewById() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        owner_register_phone_edt = (EditText) findViewById(R.id.owner_register_phone_edt);
        owner_register_code_edt = (EditText) findViewById(R.id.owner_register_code_edt);
        register_GetCode_btn = (Button) findViewById(R.id.register_GetCode_btn);
        btn_clear_search_text1 = (Button) findViewById(R.id.btn_clear_search_text1);
        btn_clear_search_text2 = (Button) findViewById(R.id.btn_clear_search_text2);
        owner_register_step_btn = (Button) findViewById(R.id.owner_register_step_btn);
    }

    @Override
    protected void initView() {
        tvTitle.setText("填写手机号");
        return_imgbtn.setVisibility(View.VISIBLE);

        // 短信验证码***
        RegisterCodeTimerService.setHandler(mCodeHandler);
        mIntent = new Intent(OwnerPhoneRegisterActivity.this, RegisterCodeTimerService.class);

        mReceiverCmdMsg = new ReceiverCmdMsg();
        mReceiverCmdMsg.registerAction(ConstantsUtil.ACTION_SMS_RECEIVED);

    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
        btn_clear_search_text1.setOnClickListener(this);
        btn_clear_search_text2.setOnClickListener(this);
        register_GetCode_btn.setOnClickListener(this);
        owner_register_step_btn.setOnClickListener(this);
        // 短信验证码***

        owner_register_phone_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = owner_register_phone_edt.getText().length();
                if (textLength > 0) {
                    btn_clear_search_text1.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_search_text1.setVisibility(View.GONE);
                }
            }
        });

        owner_register_code_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = owner_register_code_edt.getText().length();
                if (textLength > 0) {
                    btn_clear_search_text2.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_search_text2.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:
            stopService(mIntent);
            this.finish();
            // sendBroadcast(new
            // Intent(ConstantsUtil.ACTION_OWNER_PHONE_REGISTER_ACTIVITY));
            break;
        case R.id.btn_clear_search_text1: //
            owner_register_phone_edt.setText("");
            btn_clear_search_text1.setVisibility(View.GONE);
            break;
        case R.id.btn_clear_search_text2: //
            owner_register_code_edt.setText("");
            btn_clear_search_text2.setVisibility(View.GONE);
            break;
        case R.id.register_GetCode_btn: // 获取验证码
            if (validatePhone()) {
                hideSoftInputView();
                register_GetCode_btn.setEnabled(false);
                startService(mIntent);
                showProgressDialog(this, 0);// 接口
            }
            break;
        case R.id.owner_register_step_btn: // 检验验证码 (填写手机号：下一步)
            if (validateDataCode()) {
                hideSoftInputView();
                showProgressDialog(this, 1);// 接口
            }
            break;
        default:
            break;
        }
    }

    @Override
    protected boolean validateData() {
        phoneStr = owner_register_phone_edt.getText().toString();
        boolean flag = true;
        if (TextUtils.isEmpty(phoneStr)) {
            Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (phoneStr.length() != 11) {
            Toast.makeText(getApplicationContext(), "请输入11位正确的手机号", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (!phoneStr.startsWith("1")) {
            Toast.makeText(getApplicationContext(), "请输入首位为1的正确手机号", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (!StringUtils.isMobileNO(phoneStr)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;

    }

    // 【校验电话号码格式】是否正确
    private boolean validatePhone() {
        boolean flag = true;
        phoneStr = owner_register_phone_edt.getText().toString();
        // 校验【电话号码格式】是否正确
        flag = RegularUtil.isMobileNO(OwnerPhoneRegisterActivity.this, phoneStr);
        if (!flag) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    protected boolean validateDataCode() {
        phoneStr = owner_register_phone_edt.getText().toString();
        codeStr = owner_register_code_edt.getText().toString();
        boolean flag = true;
        // 校验【验证码不能为空】
        if (TextUtils.isEmpty(codeStr)) {
            Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            flag = false;
        } else {
            // 校验【电话号码格式】是否正确
            flag = validatePhone();
        }
        return flag;
    }

    public void showProgressDialog(Context mContext, int type) {
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

        paramsMap = new HashMap<String, Object>();
        sign = 2;

        switch (type) {
        case 0: // 发送验证码

            // // paramsMap = new HashMap();
            // registerUserVo.setMobilephone(phoneStr);// 测试手机号码
            // registerUserVo.setSign(sign);
            //
            // paramsMap.put("registerUserVo", registerUserVo);
            // mTask = new Task(TaskType.TS_registerGETCODE, paramsMap);
            // MainService.newTask(mTask);
            SignUpInfo.GetCodeRequest request = new SignUpInfo.GetCodeRequest(phoneStr);
            VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_SMS,
                    new OnResponseListener<GetCodeResponse>(GetCodeResponse.class) {

                        @Override
                        protected void handleResponse(GetCodeResponse response) {
                            Toast.makeText(getApplicationContext(), "" + response.getData(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void handleFailResponse(ResponseFail response) {
                            Toast.makeText(getApplicationContext(), "" + response.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }, mErrorListener);

            break;

        case 1: // 注册时检验验证码

            // paramsMap = new HashMap();

            // registerUserVo.setPhone(phoneStr);// 测试手机号码
            // registerUserVo.setCode(StringUtils.stringTolnt(codeStr));//
            // 测试验证码,字符串转int传值
            // registerUserVo.setSign(sign);
            //
            // paramsMap.put("registerUserVo", registerUserVo);
            // mTask = new Task(TaskType.TS_registerCheckCODE, paramsMap);
            // MainService.newTask(mTask);
            SignUpInfo.SignUpRequest request1 = new SignUpInfo.SignUpRequest(codeStr, phoneStr);
            VolleyManager.getInstance().postRequest(request1, VolleyManager.METHOD_LOGIN,
                    new OnResponseListener<SignUpInfo.SignUpResponse>(SignUpInfo.SignUpResponse.class) {
                        @Override
                        protected void handleResponse(SignUpResponse response) {
                            InitApplication.mSpUtil.setUser(response.getData().getuser());
                            InitApplication.mSpUtil.setMineFragmentNeedRefresh(true); // “我的”界面需要进行刷新
                            setResult(RESULT_OK);
                            Toast.makeText(getApplicationContext(), "恭喜你，登录成功!", Toast.LENGTH_SHORT).show();
                            // sendBroadcast(new
                            // Intent(ConstantsUtil.ACTION_MINE_FRAGMENT));
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        protected void handleFailResponse(ResponseFail response) {
                            Toast.makeText(getApplicationContext(), "" + response.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }, mErrorListener);

            break;

        default:
            break;
        }
    }

    // private void openShowDanceMainActivity() {
    // startActivity(new Intent(OwnerPhoneRegisterActivity.this,
    // ShowDanceMainActivity.class));
    // }
    //
    // private void openShowDanceMainActivityWithExtra() {
    // Intent intent = new Intent(OwnerPhoneRegisterActivity.this,
    // ShowDanceMainActivity.class);
    // Bundle bundle = new Bundle();
    // bundle.putInt(ConstantsUtil.OPEN_ACTIVITY,
    // ConstantsUtil.OPEN_MINE_CONTENT);
    // intent.putExtras(bundle);
    // startActivity(intent);
    // }

    /**
     * @ClassName: ReceiverCmdMsg
     * @Description: 短信验证码
     * @author
     * @date 2015-04-14 下午16:33:55
     */
    private class ReceiverCmdMsg extends BroadcastReceiver {

        public void registerAction(String action) {
            IntentFilter filter = new IntentFilter();
            // 设置短信拦截参数
            filter.addAction(action);
            filter.setPriority(Integer.MAX_VALUE);
            registerReceiver(mReceiverCmdMsg, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConstantsUtil.ACTION_SMS_RECEIVED)) {
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for (Object obj : objs) {
                    byte[] pdu = (byte[]) obj;
                    @SuppressWarnings("deprecation")
                    SmsMessage sms = SmsMessage.createFromPdu(pdu);
                    String message = sms.getMessageBody();
                    Log.d("短信内容", "message：" + message);
                    // 短息的手机号。。+86开头？
                    String from = sms.getOriginatingAddress();
                    Log.d("短信来源", "from ：" + from);
                    // Time time = new Time();
                    // time.set(sms.getTimestampMillis());
                    // String time2 = time.format3339(true);
                    // Log.d("logo", from + " " + message + " " + time2);
                    // strContent = from + " " + message;
                    // handler.sendEmptyMessage(1);
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("messagecode", code);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        }
    }

    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 倒计时Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
                register_GetCode_btn.setText(msg.obj.toString());
            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
                register_GetCode_btn.setEnabled(true);
                register_GetCode_btn.setText(msg.obj.toString());
            }
        };
    };

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 1:
                String codeMsg = msg.getData().getString("messagecode");
                Log.d("得到的code", codeMsg);
                owner_register_code_edt.setText(codeMsg);
                break;
            }

        };
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
        unregisterReceiver(mReceiverCmdMsg);
    }

    @Override
    public void refresh(Object... param) {
        Intent mIntent = new Intent();
        int type = (Integer) param[0];
        switch (type) {
        case TaskType.TS_registerGETCODE: // 注册-发送验证码
            mDialog.dismiss();
            if (ConstantsUtil.NetworkStatus) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (HashMap<String, Object>) param[1];
                String result = map.get("result").toString();
                if (!result.equals("-1")) {
                    if (result.equals("0")) {
                        Toast.makeText(getApplicationContext(), "验证码已发送到手机", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("1")) {
                        Toast.makeText(getApplicationContext(), "该手机号已注册", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "连接服务器失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "请开启本机网络！", Toast.LENGTH_SHORT).show();
            }
            break;
        case TaskType.TS_registerCheckCODE: // 注册-检验验证码
            mDialog.dismiss();
            if (ConstantsUtil.NetworkStatus) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) param[1];
                String result = map.get("result").toString();
                if (!result.equals("-1")) {
                    if (result.equals("0")) {
                        mIntent.setClass(this, OwnerRegisterDanceIdActivity.class);
                        mIntent.putExtra("mobilephone", phoneStr);
                        startActivityForResult(mIntent, 2);
                    } else { // result不为 "0"
                        Toast.makeText(getApplicationContext(), "验证码有误", Toast.LENGTH_SHORT).show();
                    }
                } else { // result.equals("-1")
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        // sendBroadcast(new
        // Intent(ConstantsUtil.ACTION_OWNER_PHONE_REGISTER_ACTIVITY));
    }

}