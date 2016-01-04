package com.gank.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lucifer on 16-1-4.
 */
public class GetRss {

    private static final String RSS_URL = "http://gank.io/feed";

    public static InputStream getRssContent() {
        try {
            URL url = new URL(RSS_URL);
            HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(5000);
            urlCon.setRequestMethod("GET");
            if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return urlCon.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
