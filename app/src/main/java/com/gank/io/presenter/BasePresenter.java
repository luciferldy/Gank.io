package com.gank.io.presenter;

import android.app.Activity;

import com.gank.io.ui.view.IBaseView;

/**
 * Created by Lucifer on 2016/7/13.
 */
public class BasePresenter<BV extends IBaseView> {

    protected BV mView;

    protected Activity mActivity;

    public BasePresenter(Activity activity, BV view) {
        this.mActivity = activity;
        this.mView = view;
    }
}
