package com.android.app.showdance.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.android.app.showdance.db.SQLiteHelper;
import com.android.app.showdance.model.PFile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class FileBusiness {

	/** 获取所有已经排好序的列表 */
	public static ArrayList<PFile> getAllSortFiles(final Context ctx) {
		ArrayList<PFile> result = new ArrayList<PFile>();
		SQLiteHelper sqlite = new SQLiteHelper(ctx);
		SQLiteDatabase db = sqlite.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT " + ConstantsUtil.COL_ID + "," + ConstantsUtil.COL_TITLE + "," + ConstantsUtil.COL_TITLE_PINYIN + "," + ConstantsUtil.COL_PATH + "," + ConstantsUtil.COL_DURATION
					+ "," + ConstantsUtil.COL_POSITION + "," + ConstantsUtil.COL_LAST_ACCESS_TIME + "," + ConstantsUtil.COL_THUMB + "," + ConstantsUtil.COL_FILE_SIZE + " FROM files", null);
			while (c.moveToNext()) {
				PFile po = new PFile();
				int index = 0;
				po._id = c.getLong(index++);
				po.title = c.getString(index++);
				po.title_pinyin = c.getString(index++);
				po.path = c.getString(index++);
				po.duration = c.getInt(index++);
				po.position = c.getInt(index++);
				po.last_access_time = c.getLong(index++);
				po.thumb = c.getString(index++);
				po.file_size = c.getLong(index++);
				result.add(po);
			}
		} finally {
			if (c != null)
				c.close();
		}
		db.close();

		Collections.sort(result, new Comparator<PFile>() {

			@Override
			public int compare(PFile f1, PFile f2) {
				char c1 = f1.title_pinyin.charAt(0);
				char c2 = f2.title_pinyin.charAt(0);
				return c1 == c2 ? 0 : (c1 > c2 ? 1 : -1);
			}// 相等返回0，-1 f2 > f2，-1

		});
		return result;
	}
}
