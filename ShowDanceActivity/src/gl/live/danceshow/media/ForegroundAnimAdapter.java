package gl.live.danceshow.media;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.android.app.showdance.model.glmodel.SignUpInfo.User;
import com.android.app.showdance.utils.L;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.wumeiniang.R;
import com.android.app.wumeiniang.app.InitApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class ForegroundAnimAdapter extends CameraFgAdapter {
    
    private static final String TAG = "ForegroundAnimAdapter";
    
    public ForegroundAnimAdapter(Context context, FgAnimList list) {
        super(context, list);
    }

    @Override
    public void updateList() {
        mList.clear();
        loadDownloadImages();
        notifyDataSetChanged();
    }

    @Override
    protected void init() {
        // loadDownloadImages();
    }

    private void updateSP(String name) {
        User user = InitApplication.mSpUtil.getUser();
        if (user == null)
            return;
        SharedPreferences.Editor editor = InitApplication.mSpUtil.getEditor();
        Set<String> value = InitApplication.mSpUtil.getSp().getStringSet(user.getPhone(), new HashSet<String>());
        value.remove(name);
        editor.putStringSet(user.getPhone(), value).commit();
    }

    public void loadDownloadImages() {
        File dir = new File(InitApplication.sdCardForegroundPath);
        // SharedPreferences sp = InitApplication.mSpUtil.getSp();
        // User user = InitApplication.mSpUtil.getUser();
        // Set<String> sets = sp.getStringSet(user.getPhone(), new
        // HashSet<String>());
        for (File f : dir.listFiles()) {
            String name = f.getName();
            if (".nomedia".equals(name))
                continue;
            // if(!sets.contains(name)) {
            // if(sp.getInt(name, 0) == 1)
            // continue;
            // }
            L.d("guolei", "loadDownloadImages name " + name);
            if (f.isDirectory()) {
                DtAnimItem item = new DtAnimItem(name);
                item.add(f);
                if (item.isEmpty()) {
                    f.delete();
                    continue;
                }
                mList.add(item);
            }
        }
        Collections.sort(mList, new Comparator<AnimItem>() {

            @Override
            public int compare(AnimItem lhs, AnimItem rhs) {
                return lhs.getDtName().compareToIgnoreCase(rhs.getDtName());
            }
        });
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.bitmap_list, null);
            convertView.setTag(getViewHolder(holder, convertView));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DtAnimItem item = (DtAnimItem) mList.get(position);
        holder.tvListItemName.setText(item.getDtName());

        // DisplayImageOptions opt = new
        // DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
        // .cacheOnDisk(false).build();
        // ImageView imageview = (ImageView)
        // convertView.findViewById(R.id.fg_img);
        String uri = item.getUri();
        if (uri != null) {
            // ImageLoader.getInstance().displayImage("file://" + uri,
            // imageview, opt);
            String picUri = "file://" + uri;
            L.d(TAG,"获取的图片绝对路径是："+picUri);
            holder.sdvFgImg.setImageURI(picUri);
        }

        // imageview.setBackgroundDrawable(item.getDrawable(context,
        // 160,90,true));
        // CheckBox box =
        // (CheckBox)convertView.findViewById(R.id.list_item_checkbox);
        // box.setChecked(mSelected == mList.get(position).);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String fileName = mList.get(position).getDtName();
        final int index = position;
        CustomAlertDialog mCustomDialog = new CustomAlertDialog(context).builder(R.style.DialogTVAnimWindowAnim);
        mCustomDialog.setTitle("删除提示");
        mCustomDialog.setMsg("确认删除" + fileName + "吗?");
        mCustomDialog.setPositiveButton(context.getResources().getString(R.string.dialog_ok), new OnClickListener() {
            @Override
            public void onClick(View v) {
                File delFilePath = new File(InitApplication.sdCardForegroundPath.concat(fileName));
                L.d("guolei", "deleteDir " + deleteDir(delFilePath));
                mList.remove(index);
                updateSP(fileName);
                notifyDataSetChanged();
            }
        }).setNegativeButton(context.getResources().getString(R.string.dialog_cancel), new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();

        return true;
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so now it can be smoked
        return dir.delete();
    }
}