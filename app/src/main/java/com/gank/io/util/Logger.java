package com.gank.io.util;

import android.util.Log;

/**
 * Created by Lucifer on 2016/7/13.
 */
public class Logger {

    public static boolean DEBUG = true;
    public static String TAG = "调试信息";

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void i(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }

}
