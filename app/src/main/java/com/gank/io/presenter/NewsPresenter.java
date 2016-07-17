package com.gank.io.presenter;

import android.app.Activity;
import android.util.Log;

import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IBaseView;
import com.gank.io.util.GetRss;
import com.gank.io.util.Logger;
import com.gank.io.util.ParseRss;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lucifer on 2016/7/16.
 */
public class NewsPresenter extends BasePresenter {

    private static final String TAG = NewsPresenter.class.getSimpleName();

    public NewsPresenter(Activity activity, IBaseView view) {
        super(activity, view);
    }

    /**
     * load the daily news
     * @param date
     */
    public void loadNews(final String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String results = GetRss.getRssContent(date);
                if (results == null) {
                    Logger.i(TAG, "getRssContent but no response.");
                    return;
                }
                HashMap<String, ArrayList<ContentItem>> mContents  = ParseRss.parseDailyContent(results);
                if (mContents == null) {
                    Logger.i(TAG, "parseDailyContent but no result.");
                    return;
                }
            }
        }).start();

    }
}
