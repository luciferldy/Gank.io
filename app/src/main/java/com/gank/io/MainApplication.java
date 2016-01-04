package com.gank.io;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by lucifer on 16-1-4.
 */
public class MainApplication extends Application {

    private static final String LOG_TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        Fresco.initialize(getApplicationContext());
    }
}
