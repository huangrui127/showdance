package com.android.app.showdance.adapter;

import java.util.List;

import com.android.app.showdance.model.glmodel.AdPicAndIconInfo.Category;
import com.android.app.wumeiniang.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 功能：“首页”分类菜单adapter
 * @author djd
 *
 */
public class CategoryMainAdapter extends BaseAdapter {

    private Context mContext;
    private List<Category> mCategories;
    private LayoutInflater mInflater;

    public CategoryMainAdapter(Context context, List<Category> categories) {
        mContext = context;
        mCategories = categories;
        mInflater = LayoutInflater.from(context);
    }

    public void setCategoryList(List<Category> categories) {
        mCategories = categories;
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Category getItem(int position) {
        return mCategories.get(position);
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
            convertView = mInflater.inflate(R.layout.grid_item_category, null);
            holder.categoryIV = (ImageView) convertView.findViewById(R.id.id_item_category_iv);
            holder.categoryTV = (TextView) convertView.findViewById(R.id.id_item_category_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateUI(holder, position);
        return convertView;
    }

    public final class ViewHolder {
        public ImageView categoryIV;
        public TextView categoryTV;
    }

    private void updateUI(ViewHolder viewHolder, int position) {
        Category category = mCategories.get(position);
        viewHolder.categoryIV.setImageDrawable(ContextCompat.getDrawable(mContext, getDrawableIdFromType(category.getType())));
        viewHolder.categoryTV.setText(category.getName());
    }

    private int getDrawableIdFromType(int type) {
        switch (type) {
        case 0: // 全国榜
            return R.drawable.selector_main_qg;
        case 1: // 达人榜
            return R.drawable.selector_main_dr;
        case 2: // 精彩合舞
            return R.drawable.selector_main_hw;
        case 3: // 优质视频
            return R.drawable.selector_main_yz;
        case 4: // 最新视频图标（原比赛图标）
            return R.drawable.selector_main_bs;
        default:
            return R.drawable.selector_main_dr;
        }
    }
}
