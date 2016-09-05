package com.android.app.showdance.ui;

import android.graphics.Bitmap;

public class AnimDrawableItem {
	private Bitmap mDrawable;
//	private boolean bChanged;
	
	public AnimDrawableItem(Bitmap drawable, boolean changed) {
		mDrawable = drawable;
//		bChanged =changed;
	}

	public AnimDrawableItem(Bitmap drawable) {
		mDrawable = drawable;
//		bChanged =false;
	}
	
//	public boolean isChanged(){
//		return bChanged;
//	}
	
//	public void setChanged(boolean bchanged) {
//		bChanged = bchanged;
//	}
	
	public Bitmap getBitmap() {
		return mDrawable;
	}
}
