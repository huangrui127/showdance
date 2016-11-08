package com.android.app.showdance.utils;

import android.util.Log;

/**
 * 功能：日志类
 */
public class L {

    private static boolean IS_DEBUG = true;

    public static void v(String tag, String msg) {
        if (IS_DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IS_DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable t) {
        if (IS_DEBUG) {
            Log.w(tag, msg, t);
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (IS_DEBUG) {
            Log.e(tag, msg, t);
        }
    }
}
