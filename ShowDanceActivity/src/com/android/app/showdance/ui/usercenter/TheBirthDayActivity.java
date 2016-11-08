package com.android.app.showdance.ui.usercenter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.android.app.showdance.ui.BaseActivity;
import com.android.app.showdance.ui.oa.PersonalMsgActivity;
import com.android.app.showdance.utils.StringUtils;
import com.android.app.showdance.wheelview.ScreenInfo;
import com.android.app.showdance.wheelview.WheelMain;
import com.android.app.wumeiniang.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 【生日】页面
 * **/

public class TheBirthDayActivity extends BaseActivity {
	
	private TextView the_birthday_tv0;
	private String birthday ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_birthday);
		findViewById();
		initView();
		setOnClickListener();
		
	}

	@Override
	protected void findViewById() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		cancel_tv =(TextView) findViewById(R.id.cancel_tv);
		save_tv = (TextView) findViewById(R.id.save_tv);
		the_birthday_tv0 = (TextView) findViewById(R.id.the_birthday_tv0);
		
	}

	@Override
	protected void initView() {
		tvTitle.setText("生日");
		cancel_tv.setVisibility(View.VISIBLE);
		save_tv.setVisibility(View.VISIBLE);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		//当前时间
		String CurrentTimeStr = formatter.format(new Date(System.currentTimeMillis()));
		the_birthday_tv0.setText(CurrentTimeStr);
	}

	@Override
	protected void setOnClickListener() {
		cancel_tv.setOnClickListener(this);
		save_tv.setOnClickListener(this);
		the_birthday_tv0.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.cancel_tv: // 取消
			this.finish();
			
			break;
			
		case R.id.save_tv: // 确定 
			//获取界面上的生日
			birthday = the_birthday_tv0.getText().toString();
			
			mIntent.setClass(this, PersonalMsgActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("birthday", birthday); 
			mIntent.putExtras(bundle);
			setResult(RESULT_OK, mIntent);
			this.finish(); // 要调用finish()方法

			break;
			
		case R.id.the_birthday_tv0: //选定生日对话框
			showDateTimePicker(TheBirthDayActivity.this , the_birthday_tv0 , "生日");

			break;
			
		default:
			break;
		}
	}
	
	/**
	 * @Description:时间滚动器(生日选择)
	 * @param context
	 * @return void
	 */
	public void showDateTimePicker(Activity mContext, final TextView mTextView, String dialogTitle) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View view = inflater.inflate(R.layout.custom_dialog_time_set, null);
		view.setMinimumWidth(mContext.getWindowManager().getDefaultDisplay().getWidth());
		ScreenInfo screenInfo = new ScreenInfo(mContext);
		final WheelMain wheelMain = new WheelMain(view);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		wheelMain.setTime(year, month, day, hour, minutes, second);
		// final AlertDialog dialog = new
		// AlertDialog.Builder(mContext).setView(view).show();

		final Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
		dialog.show();
		dialog.setContentView(view);

		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.dialog_anim_style); // 添加动画
		TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
		Button alertCancel_btn = (Button) view.findViewById(R.id.alertCancel_btn);
		Button alertOk_btn = (Button) view.findViewById(R.id.alertOk_btn);
		dialog_title.setText(dialogTitle);
		
		//确定按钮
		alertOk_btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String selectDateString = String.format("%04d", wheelMain.getYear()) + "-" + String.format("%02d", wheelMain.getMonth()) +"-" + String.format("%02d", wheelMain.getDay());
				dialog.dismiss();
//				mTextView.setText(selectDateString);// 时间轮的选择结果:2015-03-17 
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//当前时间
				String CurrentTimeStr = formatter.format(new Date(System.currentTimeMillis()));//2015.04.02
				System.out.println(CurrentTimeStr);
				int result;
 				result = StringUtils.dateCompare(CurrentTimeStr,selectDateString);
				if (result < 0) { // 系统当前时间不能小于生日时间的设置
					Toast.makeText(getApplicationContext(), "对不起，您不能设置当前时间以后的某个日期", Toast.LENGTH_LONG).show();
				} else {
					mTextView.setText(selectDateString);// 时间轮的选择结果:2015-03-17 
				}
				
			}
		});
		
		// 取消按钮监听
		alertCancel_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}
	
	@Override
	public void refresh(Object... param) {
		
	}

	@Override
	protected boolean validateData() {
		return false;
	}

}
