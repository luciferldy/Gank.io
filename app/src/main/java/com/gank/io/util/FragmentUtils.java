package com.gank.io.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gank.io.R;

/**
 * Created by Lucifer on 2016/7/16.
 * Fragment 使用工具类
 */
public class FragmentUtils {

    private static final String LOG_TAG = FragmentUtils.class.getSimpleName();

    public enum FragmentAnim {
        SLIDE_RIGHT,
        FADE,
        NONE
    }

    /**
     * 添加 Fragment 到 android.R.id.content
     * @param fragment
     * @param manager
     * @param bundle
     * @param anim
     * @param addBackStack
     * @return
     */
    public static synchronized Fragment addFragment(Fragment fragment, FragmentManager manager, Bundle bundle, FragmentAnim anim, boolean addBackStack) {

        if (bundle != null && !bundle.isEmpty()) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction transaction = manager.beginTransaction();
        //
        // must set animation before add fragment, or animation not work.
        //
        if (anim == FragmentAnim.SLIDE_RIGHT) {
            Logger.i(LOG_TAG, "set slide right animation.");
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (anim == FragmentAnim.FADE) {
            Logger.i(LOG_TAG, "set fade animation.");
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        } else {
            //
            Logger.i(LOG_TAG, "anim=" + anim);
        }

        transaction.add(android.R.id.content, fragment);
        if (addBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName() + System.currentTimeMillis());
        }
        Logger.i(LOG_TAG, "add Fragment " + fragment.getClass().getSimpleName());
        transaction.commit();

        return fragment;
    }

    /**
     * pop back stack
     * @param activity
     */
    public static void popBackStack(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.popBackStack();
    }
}
