package com.gank.io.util;

import com.gank.io.model.ContentItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by lucifer on 16-1-4.
 */
public class ParseRss {

    private static final String LOG_TAG = ParseRss.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String CATEGORY = "category";

    public static HashMap<String, ArrayList<ContentItem>> parseDailyContent(String result) {
        try {
            JSONObject totalData = new JSONObject(result);
            JSONObject myResults = totalData.getJSONObject(RESULTS);
            JSONArray myCategory = totalData.getJSONArray(CATEGORY);
            // init linkedhashmap
            HashMap<String, ArrayList<ContentItem>> mResults = new LinkedHashMap<>();
            // get all categories
            for (int i = 0; i < myCategory.length(); i++) {
                JSONArray items = myResults.getJSONArray(myCategory.getString(i));
                ArrayList<ContentItem> itemsList = new ArrayList<>();
                // each categories has its items
                for (int j = 0; j < items.length(); j++) {
                    JSONObject item = items.getJSONObject(j);
                    ContentItem contentItem = new ContentItem();
                    contentItem.setWho(item.getString(ContentItem.WHO));
                    contentItem.setPublishedAt(DateUtils.toDate(item.getString(ContentItem.PUBLISHED_AT)));
                    contentItem.setDesc(item.getString(ContentItem.DESC));
                    contentItem.setType(item.getString(ContentItem.TYPE));
                    contentItem.setUrl(item.getString(ContentItem.URL));
                    contentItem.setUsed(item.getBoolean(ContentItem.USED));
                    contentItem.setObjectId(item.getString(ContentItem.OBJECT_ID));
                    contentItem.setCreatedAt(DateUtils.toDate(item.getString(ContentItem.CREATE_AT)));
                    contentItem.setSource(item.getString(ContentItem.SOURCE));
                    itemsList.add(contentItem);
                }
                mResults.put(myCategory.getString(i), itemsList);
            }
            return mResults;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, ArrayList<ContentItem>> parseMezhi(String result) {
        try {
            JSONObject totalData = new JSONObject(result);
            JSONArray myResults = totalData.getJSONArray(RESULTS);
            HashMap<String, ArrayList<ContentItem>> mResults = new HashMap<>();
            // init

            ArrayList<ContentItem> itemsList = new ArrayList<>();
            for (int j = 0; j < myResults.length(); j++) {
                JSONObject item = myResults.getJSONObject(j);
                ContentItem contentItem = new ContentItem();
                contentItem.setWho(item.getString(ContentItem.WHO));
                contentItem.setPublishedAt(DateUtils.toDate(item.getString(ContentItem.PUBLISHED_AT)));
                contentItem.setDesc(item.getString(ContentItem.DESC));
                contentItem.setType(item.getString(ContentItem.TYPE));
                contentItem.setUrl(item.getString(ContentItem.URL));
                contentItem.setUsed(item.getBoolean(ContentItem.USED));
                contentItem.setObjectId(item.getString(ContentItem.OBJECT_ID));
                contentItem.setCreatedAt(DateUtils.toDate(item.getString(ContentItem.CREATE_AT)));
                contentItem.setSource(item.getString(ContentItem.SOURCE));
                itemsList.add(contentItem);
            }
            mResults.put(ContentItem.MEI_ZHI, itemsList);
            return mResults;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
