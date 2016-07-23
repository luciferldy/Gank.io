package com.gank.io.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IBaseView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.GetRss;
import com.gank.io.util.Logger;
import com.gank.io.util.ParseRss;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lucifer on 2016/7/13.
 */
public class MainPresenter extends BasePresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    public MainPresenter(Activity context, IBaseView view) {
        super(context, view);
    }

    /**
     * load meizhi pic
     */
    public synchronized void loadMeizhi() {
        // 网络访问请求妹纸图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i(TAG, "loadMeizhi");
                String result = GetRss.getMeizhiImg("10/1");
                if (TextUtils.isEmpty(result)) {
                    Logger.i(TAG, "get meizhi img but no response.");
                    return;
                }
//                Logger.i(TAG, "getMeizhiImg response=" + result);
                ArrayList<ContentItem> items = ParseRss.parseMeizhi(result);
                if (items == null || items.isEmpty()) {
                    Logger.i(TAG, "parse meizhi but no result.");
                    return;
                }
                if (mView instanceof IMainView)
                    ((IMainView) mView).fillData(items);
            }
        }).start();
    }

}
