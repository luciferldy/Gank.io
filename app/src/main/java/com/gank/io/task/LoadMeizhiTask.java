package com.gank.io.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gank.io.adapter.NewsAdapter;
import com.gank.io.util.ContentItem;
import com.gank.io.util.GetRss;
import com.gank.io.util.ParseRss;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lucifer on 16-1-5.
 */
public class LoadMeizhiTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String LOG_TAG = LoadMeizhiTask.class.getSimpleName();
    private HashMap<String, ArrayList<ContentItem>> mParseResults;
    private NewsAdapter adapter;
    private ArrayList<HashMap<String, String>> meiZhis;

    public LoadMeizhiTask(NewsAdapter adapter, ArrayList<HashMap<String, String>> meiZhis) {
        this.adapter = adapter;
        this.meiZhis = meiZhis;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String result = GetRss.getMeizhiImg("10/1");
        if (result == null) {
            return false;
        }
        mParseResults = ParseRss.parseMezhi(result);
        if (mParseResults == null) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            // 表示获取,解析成功
            ArrayList<ContentItem> items = mParseResults.get(ContentItem.MEI_ZHI);
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> item = new HashMap<>();
                item.put(ContentItem.URL, items.get(i).getUrl());
                item.put(ContentItem.PUBLISHED_AT, items.get(i).getPublishedAt());
                item.put(ContentItem.DESC, items.get(i).getDesc());
                meiZhis.add(item);
            }
            adapter.notifyDataSetChanged();
            meiZhis = null;
            adapter = null;
        } else {
            Log.d(LOG_TAG, "get or parse meizhi failed");
        }
    }
}
