package com.gank.io.util;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Lucifer on 2016/12/21.
 */

public class CallableHashMap {

    HashMap<String, String> mHashMap;

    public CallableHashMap() {
        super();
        mHashMap = new HashMap<>();
    }

    public String put(String key, String value, Callback callback) {
        if (!mHashMap.keySet().contains(key))
            callback.update(key, value);
        return mHashMap.put(key, value);
    }

    public String get(String key) {
        return mHashMap.get(key);
    }

    public Set<String> keySet() {
        return mHashMap.keySet();
    }

    public interface Callback {
        void update(String key, String value);
        void failed();
    }
}
