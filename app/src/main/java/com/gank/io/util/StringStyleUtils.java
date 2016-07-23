package com.gank.io.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;

import com.gank.io.R;
import com.gank.io.model.ContentItem;

/**
 * Created by Lucifer on 2016/7/18.
 */
public class StringStyleUtils {

    public static SpannableString format(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(), 0);
        return spannableString;
    }

    public static CharSequence getGankInfoSequence(Context context, ContentItem item) {
        SpannableStringBuilder builder = new SpannableStringBuilder(item.getDesc()).append(
                StringStyleUtils.format(context, " (via. " + item.getWho() + ")", R.style.ViaTextAppearance)
        );
        return builder.subSequence(0, builder.length());
    }
}
