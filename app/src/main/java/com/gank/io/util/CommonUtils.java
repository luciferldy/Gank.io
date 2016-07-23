package com.gank.io.util;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by Lucifer on 2016/7/17.
 * include some common utils to use.
 */
public class CommonUtils {

    private static int statusBarHeight = 0;

    /**
     * get the statusbar height
     * @param context
     * @return
     */
    public static int getStatusbarHeight(Context context) {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

}
