package com.gank.io.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.gank.io.model.ContentItem;

import java.lang.reflect.Field;

/**
 * Created by Lucifer on 2016/7/17.
 * include some common utils to use.
 */
public class CommonUtils {

    private static int statusBarHeight = 0;

    /**
     * get the statusbar height
     * @param context
     * @return
     */
    public static int getStatusbarHeight(Context context) {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 复制内容到粘贴板
     * @param context
     * @param content
     */
    public static void copyText(Context context, String content) {
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(ContentItem.URL, content);
        cbm.setPrimaryClip(clipData);
    }

    /**
     * 获得剪贴板中的纯文本
     * @param context
     * @return
     */
    public static String getPasteText(Context context) {
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cbm.hasPrimaryClip() && cbm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData clipData = cbm.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);
            if (!TextUtils.isEmpty(item.getText())) {
                return item.getText().toString();
            }
        }
        return "";
    }

}
