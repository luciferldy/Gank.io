package com.gank.io.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.gank.io.model.ContentItem;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * 返回 fresco 缓存在本地的文件
     * @param cacheKey
     * @return
     */
    public static File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    /**
     * 拷贝文件
     * @param src
     * @param dst
     */
    public static boolean copyFile(File src, File dst) {
        if (src == null || !src.exists() || dst == null)
            return false;
        try {
            InputStream is = new FileInputStream(src); //读入原文件
            FileOutputStream fos = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ( (length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            is.close();
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拷贝整个文件夹
     * @param oldPath
     * @param newPath
     */
    public static void copyFolder(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath))
            return;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath+File.separator+file[i]);
                }
                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
