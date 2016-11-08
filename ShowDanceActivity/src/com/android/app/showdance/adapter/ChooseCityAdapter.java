package com.android.app.showdance.adapter;

import com.android.app.wumeiniang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChooseCityAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private String[] cityArray;
//    private Resources mResources;
    
    public ChooseCityAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        cityArray = context.getResources().getStringArray(R.array.city_array);
    }
    
    @Override
    public int getCount() {
        return cityArray.length;
    }

    @Override
    public String getItem(int position) {
        return cityArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_city_name, null);
            holder.cityNameTV = (TextView) convertView.findViewById(R.id.id_city_name_tv);
            holder.dividerView = convertView.findViewById(R.id.id_divider_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dividerView.setVisibility(View.VISIBLE);
        holder.cityNameTV.setText(cityArray[position]);
        if (position == (cityArray.length-1)) {
            holder.dividerView.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    private final class ViewHolder{
        public TextView cityNameTV;
        public View dividerView;
    }

}
