package com.gank.io.util;

import com.gank.io.model.ContentItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lucifer on 16-1-4.
 */
public class ParseRss {

    private static final String LOG_TAG = ParseRss.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String CATEGORY = "category";

    public static ArrayList<ContentItem> parseDailyContent(String result) {
        try {
            JSONObject allDate = new JSONObject(result);
            JSONObject results = allDate.getJSONObject(RESULTS);
            JSONArray categories = allDate.getJSONArray(CATEGORY);
            // get all categories
            ArrayList<ContentItem> itemsList = new ArrayList<>();
            for (int i = 0; i < categories.length(); i++) {
                // add the category name except girl
                String categoryName = categories.getString(i).trim();
                JSONArray newsItems = results.getJSONArray(categoryName);
                if (!categoryName.equals(ContentItem.MEI_ZHI)) {
                    ContentItem category = new ContentItem();
                    category.setType(categoryName);
                    category.setIsCategory(true);
                    itemsList.add(category);
                }
                // each category has its items
                for (int j = 0; j < newsItems.length(); j++) {
                    JSONObject item = newsItems.getJSONObject(j);
                    ContentItem contentItem = new ContentItem();
                    contentItem.setWho(item.getString(ContentItem.WHO).trim());
                    contentItem.setPublishedAt(DateUtils.toDate(item.getString(ContentItem.PUBLISHED_AT).trim()));
                    contentItem.setDesc(item.getString(ContentItem.DESC).trim());
                    contentItem.setType(item.getString(ContentItem.TYPE).trim());
                    contentItem.setUrl(item.getString(ContentItem.URL).trim());
                    contentItem.setUsed(item.getBoolean(ContentItem.USED));
                    contentItem.setObjectId(item.getString(ContentItem.OBJECT_ID).trim());
                    contentItem.setCreatedAt(DateUtils.toDate(item.getString(ContentItem.CREATE_AT).trim()));
                    contentItem.setSource(item.getString(ContentItem.SOURCE).trim());
                    contentItem.setIsCategory(false);
                    // if the item is girl, insert into first place
                    if (categoryName.equals(ContentItem.MEI_ZHI))
                        itemsList.add(0, contentItem);
                    else
                        itemsList.add(contentItem);
                }

            }
            return itemsList;
        } catch (JSONException e) {
            Logger.i(LOG_TAG, "parseDailyContent failed.");
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ContentItem> parseMeizhi(String result) {
        try {
            JSONObject totalData = new JSONObject(result);
            JSONArray results = totalData.getJSONArray(RESULTS);
            ArrayList<ContentItem> items = new ArrayList<>();
            for (int j = 0; j < results.length(); j++) {
                JSONObject item = results.getJSONObject(j);
                ContentItem contentItem = new ContentItem();
                contentItem.setWho(item.getString(ContentItem.WHO).trim());
                contentItem.setPublishedAt(DateUtils.toDate(item.getString(ContentItem.PUBLISHED_AT).trim()));
                contentItem.setDesc(item.getString(ContentItem.DESC).trim());
                contentItem.setType(item.getString(ContentItem.TYPE).trim());
                contentItem.setUrl(item.getString(ContentItem.URL).trim());
                contentItem.setUsed(item.getBoolean(ContentItem.USED));
                contentItem.setObjectId(item.getString(ContentItem.OBJECT_ID));
                contentItem.setCreatedAt(DateUtils.toDate(item.getString(ContentItem.CREATE_AT).trim()));
                contentItem.setSource(item.getString(ContentItem.SOURCE).trim());
                items.add(contentItem);
            }
            return items;
        } catch (JSONException e) {
            Logger.i(LOG_TAG, "parseMeizhi failed.");
            e.printStackTrace();
        }
        return null;
    }
}
