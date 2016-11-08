package com.android.app.showdance.ui;

import java.io.File;

import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.utils.RegularUtil;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CustomUploadVideoTitleActivity extends BaseActivity {

    private static final String TAG = "CustomUploadVideoTitleActivity";

    private TextView mTitleTV;
    private TextView mPromptTV;
    private EditText mNameOfPerformerET;
    private EditText mDanceNameET;
    private Button mContinueUploadBtn;
    private CheckBox mCheckBox01;

    private String mTheme; // 可参加的主题活动名称
    private boolean isJoinInTheActivitiy = false; // 是否参加活动

    private int mMaxLenth = 30;// 设置允许输入的字符长度
    private String oldVideoPath = ""; // 需要上传的原始文件路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 主界面不显示标题栏
        setContentView(R.layout.activity_custom_upload_video_title);
        findViewById();
        initView();
        setOnClickListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn: // 返回按钮
            this.finish();
            break;
        case R.id.id_Continue_upload_btn: // 继续上传按钮
            if (isRightEdit()) {
                String newVideoName = getAllEditString();
                if (!"".equals(newVideoName)) {
                    File newFile = getNewVideoNameFile(newVideoName);
                    L.i(TAG, "需要上传的原文件路径是：" + oldVideoPath);
                    L.i(TAG, "需要上传的新文件路径是：" + newFile.getPath());
                    Intent intent = new Intent();
                    intent.putExtra(ConstantsUtil.VIDEO_FILE_PATH, newFile.getPath());
                    intent.putExtra(ConstantsUtil.VIDEO_FILE_NAME_LOCAL, newFile.getName());
                    intent.putExtra(ConstantsUtil.IS_JOIN_IN_THE_ACTIVITY, isJoinInTheActivitiy);
                    setResult(RESULT_OK, intent);
                    // EventBus.getDefault().post(new UploadEvent(0.00,
                    // newFile.getPath(), null, null));
                    this.finish();
                } else {
                    Toast.makeText(InitApplication.getRealContext(), "请输入自定义的视频标题，再进行上传操作！", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(InitApplication.getRealContext(), "您输入的视频标题超出字数限制！", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private boolean isRightEdit(){
        if (getAllEditString().length() <= 30) {
            return true;
        }else {
            return false;
        }
    }
    
    private String getAllEditString(){
        return mNameOfPerformerET.getText().toString()+"-"+mDanceNameET.getText().toString();
    }
    
    private File getNewVideoNameFile(String newVideoName) {
        String pathHead = oldVideoPath.substring(0, oldVideoPath.lastIndexOf("/") + 1); // 需要拼接的新路径头部
        String pathTail = oldVideoPath.substring(oldVideoPath.indexOf("_"), oldVideoPath.length()); // 需要拼接的新路径尾部
        String newVideoPath = pathHead + newVideoName + pathTail;

        File oldFile = new File(oldVideoPath);
        File newFile = new File(newVideoPath);
        oldFile.renameTo(newFile);

        changeVideoScreenShootsName(newVideoPath);

        return newFile;
    }

    /**
     * 功能：改变本地保存的视频截图名称
     */
    private void changeVideoScreenShootsName(String newVideoPath) {
        File oldPicFile = new File(oldVideoPath.substring(0, oldVideoPath.lastIndexOf(".")) + ".png"); // 原视频截图路径
        File newPicFile = new File(newVideoPath.substring(0, newVideoPath.lastIndexOf(".")) + ".png"); // 新视频截图路径
        oldPicFile.renameTo(newPicFile);
    }

    @Override
    protected void findViewById() {
        mTitleTV = (TextView) findViewById(R.id.tvTitle);
        mPromptTV = (TextView) findViewById(R.id.id_prompt_tv);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        mNameOfPerformerET = (EditText) findViewById(R.id.id_name_of_performer_et);
        mDanceNameET = (EditText) findViewById(R.id.id_dance_name_et);
        mContinueUploadBtn = (Button) findViewById(R.id.id_Continue_upload_btn);
    }

    @Override
    protected void initView() {
        mTitleTV.setText("上传视频设置");
        return_imgbtn.setVisibility(View.VISIBLE);

        oldVideoPath = getIntent().getStringExtra(ConstantsUtil.VIDEO_FILE_PATH);
        L.d(TAG, "oldVideoPath is " + oldVideoPath);
        // initPromptTV();
        initEditText();
        initCheckBox01();
        initThemeButton();
    }

    /**
     * 功能：改变TextView中部分字体颜色
     */
    private void initPromptTV() {
        SpannableStringBuilder builder = new SpannableStringBuilder(mPromptTV.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, 14, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPromptTV.setText(builder);
    }

    private void initEditText() {
        // String oldVideoName =
        // oldVideoPath.substring(oldVideoPath.lastIndexOf("/") + 1,
        // oldVideoPath.indexOf("_"));
        // mEditText.setText(oldVideoName);

        String danceName = oldVideoPath.substring(oldVideoPath.lastIndexOf("/")+1,oldVideoPath.indexOf("_"));
        mNameOfPerformerET.addTextChangedListener(mTextWatcher);
        mDanceNameET.setText(danceName);
        mDanceNameET.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        
        private int cou = 0;
        int selectionEnd = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cou = before + count;
            String editable = mNameOfPerformerET.getText().toString();
            String str = RegularUtil.stringFilter(editable); // 过滤特殊字符
            if (!editable.equals(str)) {
                Toast.makeText(InitApplication.getRealContext(), "文本中含有特殊字符，将进行自动过滤！", Toast.LENGTH_SHORT).show();
                mNameOfPerformerET.setText(str);
            }
            mNameOfPerformerET.setSelection(mNameOfPerformerET.length());
            cou = mNameOfPerformerET.length();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (cou > mMaxLenth) {
                Toast.makeText(InitApplication.getRealContext(), "超出了最大长度限制，将进行自动截取！", Toast.LENGTH_SHORT).show();
                selectionEnd = mNameOfPerformerET.getSelectionEnd();
                editable.delete(mMaxLenth, selectionEnd);
            }
        }
    };

    private void initCheckBox01() {
        mCheckBox01 = (CheckBox) findViewById(R.id.id_checkbox_01);
        mCheckBox01.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.id_checkbox_01) {
                    if (!isChecked) {
                        isJoinInTheActivitiy = false;
                        // Toast.makeText(InitApplication.getRealContext(),
                        // "【取消】参加梁海洋活动", Toast.LENGTH_SHORT).show();
                    } else {
                        isJoinInTheActivitiy = true;
                        // Toast.makeText(InitApplication.getRealContext(),
                        // "【选择】参加梁海洋活动", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initThemeButton() {
        mTheme = InitApplication.mSpUtil.getTheme();
        if ("".equals(mTheme)) {
            mCheckBox01.setVisibility(View.GONE);
        } else {
            mCheckBox01.setVisibility(View.VISIBLE);
            mCheckBox01.setText("参加" + mTheme + "活动");
        }
    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
        mContinueUploadBtn.setOnClickListener(this);
    }

    @Override
    public void refresh(Object... param) {

    }

    @Override
    protected boolean validateData() {
        return false;
    }

}
