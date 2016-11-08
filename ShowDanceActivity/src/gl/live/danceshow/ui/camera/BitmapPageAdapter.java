package gl.live.danceshow.ui.camera;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import gl.live.danceshow.media.BitmapGridFragment;
import gl.live.danceshow.media.FgAnimList;

/**
 * 
 * @ClassName: RecordedVideoAdapter
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-14 下午03:51:19
 * 
 */
public class BitmapPageAdapter extends FragmentPagerAdapter {
    @SuppressWarnings("unused")
    private Context mContext;
    private List<FgAnimList> mTitle = new ArrayList<FgAnimList>();

    public BitmapPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        init(context);
    }

    protected void init(Context context) {
        addmItem(new FgAnimList(BitmapGridFragment.LIST_TYPE_PERSON, context));
        addmItem(new FgAnimList(BitmapGridFragment.LIST_TYPE_TEAM, context));
        addmItem(new FgAnimList(BitmapGridFragment.LIST_TYPE_ANIM, context));
    }

    protected void addmItem(FgAnimList item) {
        mTitle.add(item);
    }

    @Override
    public int getCount() {
        return mTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int arg0) {
        return mTitle.get(arg0).getFragment();
    }
}
