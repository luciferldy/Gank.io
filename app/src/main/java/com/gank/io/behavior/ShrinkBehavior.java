package com.gank.io.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.gank.io.util.Logger;

import java.util.List;
import java.util.Locale;

/**
 * Created by lian_ on 2017/3/10.
 */

public class ShrinkBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private static final String LOG_TAG = ShrinkBehavior.class.getSimpleName();

    public ShrinkBehavior() {
        super();
    }

    public ShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        Logger.i(LOG_TAG, String.format(Locale.CHINA, "onDependentChanged dependency getTranslationY %f, getHeight %d", ViewCompat.getTranslationY(dependency), dependency.getHeight()));
//        float translationY = getFabTranslationYForSnackbar(parent, child);
//        float percentComplete = -translationY / dependency.getHeight();
        float translationY = dependency.getHeight() - ViewCompat.getTranslationY(dependency);
        float percentComplete = translationY / dependency.getHeight();
        float scaleFactor = 1-percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);
        return false;
    }

    /**
     * 这个方法貌似可以不用，如果遇到问题再解决
     * @param parent CoordinateLayout
     * @param fab FloatingActionButton
     * @return offset
     */
    @Deprecated
    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }
        return minOffset;
    }
}
