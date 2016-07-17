package com.gank.io.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lucifer on 16-1-4.
 */
public class GetRss {

    private static final String TAG = GetRss.class.getSimpleName();
    private static final String API_URL = "http://gank.io/api/day/";
    private static final String API_MEIZHI_URL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";

        public static String getRssContent(String date) {
        try {
            URL url = new URL(API_URL + date);
            HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(5000);
            urlCon.setRequestMethod("GET");
            if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return getStringFromInputStream(urlCon.getInputStream());
            } else {
                Logger.i(TAG, "request rss content failed");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getMeizhiImg(String postfix) {
        try {
            URL url = new URL(API_MEIZHI_URL + postfix);
            HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(5000);
            urlCon.setRequestMethod("GET");
            if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return getStringFromInputStream(urlCon.getInputStream());
            } else {
                Logger.i(TAG, "request rss mezhi image failed");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0 , len);
        }
        is.close();
        String result = os.toString();
        os.close();
        return result;
    }
}
