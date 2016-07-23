package com.gank.io.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.gank.io.model.ContentItem;
import com.gank.io.ui.view.IBaseView;
import com.gank.io.ui.view.IMainView;
import com.gank.io.util.DateUtils;
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
                HashMap<String, ArrayList<ContentItem>> mParseResults = ParseRss.parseMezhi(result);
                if (mParseResults == null || mParseResults.isEmpty()) {
                    Logger.i(TAG, "parse meizhi but no result.");
                    return;
                }
                ArrayList<ContentItem> items = mParseResults.get(ContentItem.MEI_ZHI);
                ArrayList<HashMap<String, String>> meiZhis = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    HashMap<String, String> item = new HashMap<>();
                    item.put(ContentItem.URL, items.get(i).getUrl());
                    item.put(ContentItem.PUBLISHED_AT, DateUtils.toDate(items.get(i).getPublishedAt()));
                    Logger.i(TAG, "meizhi published at=" + DateUtils.toDate(items.get(i).getPublishedAt()));
                    item.put(ContentItem.DESC, items.get(i).getDesc());
                    meiZhis.add(item);
                }
                if (mView instanceof IMainView)
                    ((IMainView) mView).fillData(meiZhis);
            }
        }).start();
    }

}
