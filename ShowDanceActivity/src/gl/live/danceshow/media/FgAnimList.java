package gl.live.danceshow.media;

import java.util.ArrayList;

import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AndroidRuntimeException;

@SuppressWarnings("serial")
public class FgAnimList extends ArrayList<AnimItem> {
    private String mTitle;

    private Fragment mFrag;

    public FgAnimList(int type, Context context) {
        CameraFgAdapter adapter = null;
        switch (type) {
        case BitmapGridFragment.LIST_TYPE_PERSON:
            mTitle = "个人";
            adapter = new CameraFgAdapter(context, this);
            initDefaultList(type);
            break;
        case BitmapGridFragment.LIST_TYPE_TEAM:
            mTitle = "舞队";
            adapter = new CameraFgAdapter(context, this);
            initDefaultList(type);
            break;
        case BitmapGridFragment.LIST_TYPE_ANIM:
            mTitle = "我的";
            adapter = new ForegroundAnimAdapter(context, this);
            break;
        case BitmapGridFragment.LIST_TYPE_AVATOR2:
            mTitle = "1分2";
            initDefaultList(type);
            adapter = new CameraFgAdapter(context, this);
            break;
        case BitmapGridFragment.LIST_TYPE_AVATOR3:
            mTitle = "1分3";
            initDefaultList(type);
            adapter = new CameraFgAdapter(context, this);
            break;
        case BitmapGridFragment.LIST_TYPE_AVATOR2_DOWNLOAD:
            mTitle = "我的";
            adapter = new AvatorForegroundAdapter(context, this, InitApplication.sdCardAvator2ForegroundPath);
            break;
        case BitmapGridFragment.LIST_TYPE_AVATOR3_DOWNLOAD:
            mTitle = "我的";
            adapter = new AvatorForegroundAdapter(context, this, InitApplication.sdCardAvator3ForegroundPath);
            break;
        default:
            throw new AndroidRuntimeException();
        }
        mFrag = new BitmapGridFragment(adapter);
    }

    public String getTitle() {
        return mTitle;
    }

    public Fragment getFragment() {
        return mFrag;
    }

    private void initDefaultList(int type) {

        add(new AnimItem(R.string.camera_fg_none, 0)); // 第一张为“无边框”图

        if (type == BitmapGridFragment.LIST_TYPE_PERSON) {
            // 新遮罩图（20161027）
            add(new AnimItem(R.string.camera_fg_private_holiday_beach, R.drawable.private_holiday_beach));
            add(new AnimItem(R.string.camera_fg_private_indoor, R.drawable.private_indoor));
            add(new AnimItem(R.string.camera_fg_private_love_card, R.drawable.private_love_card));
            add(new AnimItem(R.string.camera_fg_private_patriotic_anthem, R.drawable.private_patriotic_anthem));
            add(new AnimItem(R.string.camera_fg_private_spring_suburb, R.drawable.private_spring_suburb));
            add(new AnimItem(R.string.camera_fg_private_stage, R.drawable.private_stage));
            add(new AnimItem(R.string.camera_fg_private_swan_lake, R.drawable.private_swan_lake));
            add(new AnimItem(R.string.camera_fg_private_tulip, R.drawable.private_tulip));
            // 老遮罩图
            add(new AnimItem(R.string.camera_fg_private_aut, R.drawable.private_aut));
            add(new AnimItem(R.string.camera_fg_private_dream_world, R.drawable.private_dream_world));
            add(new AnimItem(R.string.camera_fg_private_flower, R.drawable.private_flower));
            add(new AnimItem(R.string.camera_fg_private_forest, R.drawable.private_forest));
            // add(new AnimItem(R.string.camera_fg_private_happynewyear_2016,
            // R.drawable.private_happynewyear_2016));
            add(new AnimItem(R.string.camera_fg_private_jiangnan, R.drawable.private_jiangnan));
            add(new AnimItem(R.string.camera_fg_private_neimeng, R.drawable.private_neimeng));
            add(new AnimItem(R.string.camera_fg_private_lake, R.drawable.private_lake));
            add(new AnimItem(R.string.camera_fg_private_winter, R.drawable.private_winter));

        } else if (type == BitmapGridFragment.LIST_TYPE_TEAM) {
            // 新遮罩图（20161027）
            add(new AnimItem(R.string.camera_fg_team_dance_stage, R.drawable.team_dance_stage));
            add(new AnimItem(R.string.camera_fg_team_drift_bottle, R.drawable.team_drift_bottle));
            add(new AnimItem(R.string.camera_fg_team_french_window, R.drawable.team_french_window));
            add(new AnimItem(R.string.camera_fg_team_long_grass_orioles_fly, R.drawable.team_long_grass_orioles_fly));
            add(new AnimItem(R.string.camera_fg_team_love_of_love, R.drawable.team_love_of_love));
            add(new AnimItem(R.string.camera_fg_team_red_song, R.drawable.team_red_song));
            add(new AnimItem(R.string.camera_fg_team_savanna, R.drawable.team_savanna));
            add(new AnimItem(R.string.camera_fg_team_warm_spring, R.drawable.team_warm_spring));
            // 老遮罩图
            add(new AnimItem(R.string.camera_fg_team_aut, R.drawable.team_aut));
            add(new AnimItem(R.string.camera_fg_team_green, R.drawable.team_green));
            add(new AnimItem(R.string.camera_fg_team_flower, R.drawable.team_flower));
            add(new AnimItem(R.string.camera_fg_team_badalagong, R.drawable.team_budalagong));
            // add(new AnimItem(R.string.camera_fg_team_happynewyear_2016,
            // R.drawable.team_happynewyear_2016));
            add(new AnimItem(R.string.camera_fg_team_pic, R.drawable.team_pic));
            add(new AnimItem(R.string.camera_fg_team_beijing, R.drawable.team_beijing));
            add(new AnimItem(R.string.camera_fg_team_summer, R.drawable.team_summer));
            add(new AnimItem(R.string.camera_fg_team_winter, R.drawable.team_winter));
            add(new AnimItem(R.string.camera_fg_team_water, R.drawable.team_water));

        } else if (type == BitmapGridFragment.LIST_TYPE_AVATOR2) {
            add(new AnimItem(R.string.camera_fg_avator2_lake, R.drawable.avator2_lake));
            add(new AnimItem(R.string.camera_fg_avator2_country, R.drawable.avator2_country));
            add(new AnimItem(R.string.camera_fg_avator2_show, R.drawable.avator2_show));
            add(new AnimItem(R.string.camera_fg_avator2_hill_flower, R.drawable.avator2_hill_flower));
            add(new AnimItem(R.string.camera_fg_avator2_moon, R.drawable.avator2_moon));
            add(new AnimItem(R.string.camera_fg_avator2_peace, R.drawable.avator2_peace));
            add(new AnimItem(R.string.camera_fg_avator2_show_dance, R.drawable.avator2_show_dance));
            add(new AnimItem(R.string.camera_fg_avator2_summer, R.drawable.avator2_summer_lake));
        } else if (type == BitmapGridFragment.LIST_TYPE_AVATOR3) {
            add(new AnimItem(R.string.camera_fg_avator3_bench, R.drawable.avator3_bench));
            add(new AnimItem(R.string.camera_fg_avator3_forest, R.drawable.avator3_forest));
            add(new AnimItem(R.string.camera_fg_avator3_lover, R.drawable.avator3_lover));
            add(new AnimItem(R.string.camera_fg_avator3_congratulation, R.drawable.avator3_congratulation));
            add(new AnimItem(R.string.camera_fg_avator3_downset, R.drawable.avator3_downset));
            add(new AnimItem(R.string.camera_fg_avator3_sands, R.drawable.avator3_sands));
            add(new AnimItem(R.string.camera_fg_avator3_stage, R.drawable.avator3_stage_show));
            add(new AnimItem(R.string.camera_fg_avator3_zhangdengjiecai, R.drawable.avator3_zhangdengjiecai));
        }

        // add(new AnimItem(R.string.camera_fg_none, 0));
    }
}