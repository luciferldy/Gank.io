package com.gank.io.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lucifer on 2016/8/29.
 */
public class ShareUtils {

    private static final String LOG_TAG = ShareUtils.class.getSimpleName();

    /**
     * 分享文字
     * @param context
     * @param text
     */
    public static void shareText(Context context, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享单张图片
     * @param context
     * @param photoPath
     */
    public static void shareSingleImage(Context context, String photoPath) {

        if (TextUtils.isEmpty(photoPath)) {
            Logger.i(LOG_TAG, "photoPath is empty.");
            return;
        }
        File imgFile = new File(photoPath);
        if (!imgFile.exists()) {
            Logger.i(LOG_TAG, "photo file is not existed.");
            return;
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imgFile));
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));

    }

    /**
     * 分享多张图片
     * @param context
     * @param paths
     */
    public static void shareMultipleImage(Context context, String... paths) {

        if (paths == null) {
            Logger.i(LOG_TAG, "paths is null.");
            return;
        }

        ArrayList<Uri> uriList = new ArrayList<>();
        for (String path : paths) {
            if (TextUtils.isEmpty(path)) {
                continue;
            }
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                continue;

            }
            uriList.add(Uri.fromFile(imgFile));
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));

    }
}
