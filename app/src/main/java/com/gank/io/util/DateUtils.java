package com.gank.io.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lucifer on 2016/7/18.
 */
public class DateUtils {

    private static String TAG = DateUtils.class.getSimpleName();

    public static final String YEAR = "year";
    public static final String MONTH ="month";
    public static final String DAY = "day";
    public static final String DATE = "date";

    /**
     * Date to String
     * @param date
     * @return
     */
    public static String toDate(Date date) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            return format.format(date);
        } catch (Exception e) {
            Logger.i(TAG, "date to string failed.");
            e.printStackTrace();
            return "";
        }

    }

    /**
     * String to Date
     * @param date
     * @return
     */
    public static Date toDate(String date) {
        Date mDate = new Date();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            mDate = format.parse(date);
        } catch (ParseException e) {
            Logger.i(TAG, "string to date failed, string=" + date);
        }
        return mDate;
    }
}
