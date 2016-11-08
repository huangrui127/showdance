package com.android.app.showdance.ui;

import java.util.ArrayList;
import java.util.List;

import com.android.app.showdance.adapter.TeacherAdapter;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.TeacherDancerMusic;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.TeacherInfo;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.wumeiniang.R;
import com.android.volley.VolleyError;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: TeacherActivity
 * @Description: 明星老师
 * @author maminghua
 * @date 2015-5-12 下午06:11:59
 * 
 */
public class TeacherActivity extends VolleyBaseActivity implements OnItemClickListener {

    protected ListView teacherList;

    protected List<TeacherDancerMusic> teacherDancerMusic = new ArrayList<TeacherDancerMusic>();

    protected TeacherAdapter mTeacherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        findViewById();
        initView();
        setOnClickListener();

    }

    @Override
    protected void findViewById() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        return_imgbtn = (ImageButton) findViewById(R.id.return_imgbtn);
        teacherList = (ListView) findViewById(R.id.teacherList);
    }

    @Override
    protected void initView() {
        tvTitle.setText("明星老师");
        return_imgbtn.setVisibility(View.VISIBLE);

        showProgressDialog(this);
    }

    @Override
    protected void setOnClickListener() {
        return_imgbtn.setOnClickListener(this);
        teacherList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.return_imgbtn:// 返回
            this.finish();
            sendBroadcast(new Intent(ConstantsUtil.ACTION_TEACHER_ACTIVITY));
            break;
        }

    }

    /**
     * 
     * @Description:已下载舞曲
     * @param mContext
     * @param id
     * @param pageNo
     * @param refreshType
     *            刷新类型 （0：首次刷新,1：列表下拉刷新,2：列表上拉加载更多
     * @return void
     */
    public void showProgressDialog(Context mContext) {

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCancelable(true); // false设置点击其他地方不能取消进度条

        TeacherInfo.Request request = new TeacherInfo.Request();
        VolleyManager.getInstance().postRequest(request, VolleyManager.METHOD_STAR_TEACHER_LIST, mListener,
                mErrorListener);
    }

    private OnResponseListener<TeacherInfo.Response> mListener = new OnResponseListener<TeacherInfo.Response>(
            TeacherInfo.Response.class) {

        @Override
        protected void handleResponse(TeacherInfo.Response response) {
            List<TeacherInfo.Search> list = response.getData();
            if (list == null)
                return;
            teacherDancerMusic.clear();
            for (TeacherInfo.Search item : list) {
                TeacherDancerMusic downmusic = new TeacherDancerMusic(item);
                teacherDancerMusic.add(downmusic);
            }
            if (mTeacherAdapter == null) {
                mTeacherAdapter = new TeacherAdapter(TeacherActivity.this, teacherDancerMusic);
                teacherList.setAdapter(mTeacherAdapter);//
            }
            mTeacherAdapter.notifyDataSetChanged();
        }

        @Override
        protected void handleFailResponse(ResponseFail response) {
            Toast.makeText(getApplicationContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void handleErrorResponse(VolleyError error) {
    }

    @Override
    public void refresh(Object... param) {

    }

    @Override
    protected boolean validateData() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
        String dancer = teacherDancerMusic.get(position).getTeacher().getTeacher();
        Intent mIntent = new Intent();
        mIntent.setClass(this, TeacherDancerActivity.class);
        mIntent.putExtra("dancer", dancer);
        startActivity(mIntent);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        sendBroadcast(new Intent(ConstantsUtil.ACTION_TEACHER_ACTIVITY));
    }
}
