package com.android.app.showdance.ui;

import java.util.List;

import com.android.app.showdance.adapter.TeacherAdapter;
import com.android.app.showdance.logic.VolleyManager;
import com.android.app.showdance.model.TeacherDancerMusic;
import com.android.app.showdance.model.glmodel.BaseRequest;
import com.android.app.showdance.model.glmodel.ResponseFail;
import com.android.app.showdance.model.glmodel.TeacherInfo;
import com.android.app.wumeiniang.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * 
 * @ClassName: TeacherActivity
 * @Description: 明星老师边框
 * @author maminghua
 * @date 2015-5-12 下午06:11:59
 * 
 */
public class TeacherOfFrameActivity extends TeacherActivity {

    @Override
    protected void initView() {
        tvTitle.setText("边框老师");
        return_imgbtn.setVisibility(View.VISIBLE);

        showProgressDialog(this);
    }

    public void showProgressDialog(Context mContext) {

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(R.layout.loading_progressbar_dialog);
        mDialog.setCanceledOnTouchOutside(true); // false设置点击其他地方不能取消进度条

        VolleyManager.getInstance().postRequest(new BaseRequest(BaseRequest.FRAME_TEACHER_TOKEN),
                VolleyManager.METHOD_STAR_FRAME, mListener, mErrorListener);
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
                mTeacherAdapter = new TeacherAdapter(TeacherOfFrameActivity.this, teacherDancerMusic);
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
    public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
        String dancer = teacherDancerMusic.get(position).getTeacher().getTeacher();
        Intent mIntent = new Intent();
        mIntent.setClass(this, CameraMoreFramActivity.class);
        mIntent.putExtra("teacher", dancer);
        startActivity(mIntent);
    }
}
