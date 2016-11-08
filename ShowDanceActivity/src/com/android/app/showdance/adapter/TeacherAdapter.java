package com.android.app.showdance.adapter;

import java.util.List;

import com.android.app.showdance.model.TeacherDancerMusic;
import com.android.app.wumeiniang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @ClassName: DownloadedMusicAdapter
 * @Description: 已下载舞曲
 * @author maminghua
 * @date 2015-5-13 下午02:23:56
 * 
 */
public class TeacherAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private Context context;

	private List<TeacherDancerMusic> teacherList;

	public TeacherAdapter(Context context, List<TeacherDancerMusic> teacherList) {
		this.listInflater = LayoutInflater.from(context);
		// 引用数据
		this.teacherList = teacherList;
		this.context = context;

	}

	/**
	 * 获取数据集长
	 */
	@Override
	public int getCount() {
		return teacherList.size();
	}

	/**
	 * 根据位置获取当前数据
	 */
	@Override
	public Object getItem(int position) {
		return teacherList.get(position);
	}

	/**
	 * 获取位置
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	public final class ViewHolder {
		public TextView tvTeacherName; // 歌曲演唱者

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final TeacherDancerMusic listItem = teacherList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.activity_teacher_listitems, null);

			holder.tvTeacherName = (TextView) convertView.findViewById(R.id.tvTeacherName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTeacherName.setText(listItem.getTeacher().getTeacher().toString());

		return convertView;
	}

}
