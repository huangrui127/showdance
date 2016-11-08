package com.android.app.showdance.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter {

	Context context;
	ArrayList<Drawable> drawables;

	public ImageAdapter(Context context, ArrayList<Drawable> drawables) {
		this.context = context;
		this.drawables = drawables;
	}

	public Context getContext() {
		return context;
	}

	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drawables.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return drawables.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = new ImageView(context);
			((ImageView) convertView).setScaleType(ScaleType.FIT_XY);
		}
		if (drawables.size() > 0) {
			Drawable item = drawables.get(position % drawables.size());
			((ImageView) convertView).setImageDrawable(item);
		}
		return convertView;
	}
}
