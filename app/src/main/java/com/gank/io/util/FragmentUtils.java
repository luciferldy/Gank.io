package com.gank.io.util;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Lucifer on 2016/7/16.
 */
public class FragmentUtils {

    /**
     * pop back stack
     * @param activity
     */
    public static void popBackStack(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.popBackStack();
    }
}
