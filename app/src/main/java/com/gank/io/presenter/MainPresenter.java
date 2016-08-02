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
    private static final int REQUEST_COUNT = 10; // 请求次数
    private static int mPageNumber = 1; // 第几页，初始值为1，自增，可以大于请求次数
    private static boolean isLoadingData = false;

    public MainPresenter(Activity context, IBaseView view) {
        super(context, view);
    }

    /**
     * load meizhi pic
     */
    public synchronized void loadMeizhi(final boolean loadMore, final LoadCallback callback) {
        // 网络访问请求妹纸图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i(TAG, "loadMeizhi");
                isLoadingData = true;
                if (loadMore) {
                    mPageNumber++;
                } else {
                    mPageNumber = 1;
                }
                String result = GetRss.getMeizhiImg(REQUEST_COUNT + "/" + mPageNumber);
                if (TextUtils.isEmpty(result)) {
                    Logger.i(TAG, "get meizhi img but no response.");
                    callback.onLoadFailed();
                    isLoadingData = false;
                    return;
                }
//                Logger.i(TAG, "getMeizhiImg response=" + result);
                ArrayList<ContentItem> items = ParseRss.parseMeizhi(result);
                if (items == null || items.isEmpty()) {
                    Logger.i(TAG, "parse meizhi but no result.");
                    callback.onLoadFailed();
                    isLoadingData = false;
                    return;
                }
                if (mView instanceof IMainView) {
                    callback.onLoadSuccess();
                    if (!loadMore)
                        ((IMainView) mView).fillData(items);
                    else {
                        ((IMainView) mView).appendMoreData(items);
                    }
                } else {
                    callback.onLoadFailed();
                }
                isLoadingData = false;
            }
        }).start();
    }

    public boolean isLoadingData() {
        return isLoadingData;
    }

    /**
     * 请求妹纸信息的回调，用来控制 swiperefreshlayout
     */
    public interface LoadCallback {

        void onLoadSuccess();

        void onLoadFailed();
    }

}
