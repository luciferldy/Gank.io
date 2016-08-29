package com.gank.io.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Lucifer on 2016/8/29.
 * 微信分享工具类
 */
public class WechatUtils {

    /***
     * 分享给微信好友
     * @param context
     * @param photoPath
     */
    public static void shareToFriend(Context context, String photoPath) {

        if (!CommonUtils.isPackageInstalled(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(photoPath)) {
            Toast.makeText(context, "图片路径不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(photoPath);
        if (!file.exists()) {
            String tip = "文件不存在";
            Toast.makeText(context, tip + " path = " + photoPath, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }

    /**
     * 分享到微信朋友圈 com.tencent.mm 是微信的包名
     * @param context
     * @param text
     * @param photoPath
     */
    public static void shareToTimeLine(Context context, String text, String photoPath) {
        if (!CommonUtils.isPackageInstalled(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(photoPath)) {
            Toast.makeText(context, "图片路径不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(photoPath);
        if (!file.exists()) {
            String tip = "文件不存在";
            Toast.makeText(context, tip + " path = " + photoPath, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("Kdescription", text);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }
}
