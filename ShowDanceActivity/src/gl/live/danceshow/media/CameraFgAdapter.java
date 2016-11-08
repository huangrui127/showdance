package gl.live.danceshow.media;

import com.android.app.showdance.model.Resource;
import com.android.app.showdance.utils.FrescoUtils;
import com.android.app.showdance.utils.L;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @ClassName: RecordedVideoAdapter
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-14 下午03:51:19
 * 
 */
public class CameraFgAdapter extends BaseAdapter implements OnItemLongClickListener {

    private static final String TAG = "CameraFgAdapter";

    protected Context context;
    protected FgAnimList mList;
    protected static int mSelected = -1;

    public CameraFgAdapter(Context context) {
        init();
    }

    public CameraFgAdapter(Context context, FgAnimList list) {
        this.context = context;
        mList = list;
        init();
    }

    protected void init() {
    }

    @Override
    public int getCount() {
        return getCurrentList().size();
    }

    @Override
    public Object getItem(int position) {
        return getCurrentList().get(position);
    }

    private FgAnimList getCurrentList() {
        return mList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.bitmap_list, null);
            convertView.setTag(getViewHolder(holder, convertView));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AnimItem item = getCurrentList().get(position);
        holder.tvListItemName.setText(item.getName());

        // DisplayImageOptions opt = new
        // DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
        // .cacheOnDisk(false).build();
        // ImageView layout = (ImageView) convertView.findViewById(R.id.fg_img);
        if (item.getRawId() == 0) {
            holder.sdvFgImg.setImageAlpha(0);
            holder.sdvFgImg.setBackgroundColor(ContextCompat.getColor(context, R.color.camera_noframe_color));
        } else {
            holder.sdvFgImg.setImageAlpha(255);
            FrescoUtils.loadResPic(context, holder.sdvFgImg, item.getRawId());
        }

        //
        // CheckBox box =
        // (CheckBox)convertView.findViewById(R.id.list_item_checkbox);
        // box.setChecked(mSelected ==
        // getCurrentList().get(position).getRawId());
        return convertView;
    }

    public ViewHolder getViewHolder(ViewHolder holder, View convertView) {
        holder.tvListItemName = (TextView) convertView.findViewById(R.id.list_item_name);
        holder.sdvFgImg = (SimpleDraweeView) convertView.findViewById(R.id.fg_img);
        return holder;
    }

    public final class ViewHolder {
        public SimpleDraweeView sdvFgImg;
        public TextView tvListItemName;
    }

    public interface OnFgItemClickListener {
        void onItemClick(AnimItem item);
    }

    public void updateList() {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    // private void updateTitle() {
    // Activity activity =
    // ((CameraPreviewActivity) context);
    // Button btn = (Button)
    // activity.findViewById(R.id.oneBtn);
    // btn.setTextColor(bTeam?Color.GRAY:Color.WHITE);
    // btn = (Button)
    // activity.findViewById(R.id.teamBtn);
    // btn.setTextColor(bTeam?Color.WHITE:Color.GRAY);
    // }
    // @Override
    // public void onClick(View v) {
    // bTeam = v.getId() == R.id.teamBtn;
    // updateTitle();
    // notifyDataSetChanged();
    // }

}
