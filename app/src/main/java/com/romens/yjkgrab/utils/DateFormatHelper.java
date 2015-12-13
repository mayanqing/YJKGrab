package com.romens.yjkgrab.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by myq on 15-12-11.
 */
public class DateFormatHelper {
    public static final String DEFAUL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PARTTEN="yyyy-MM-dd";
    public static String formatDate(Date date) {
        return formatDate(DEFAUL_PATTERN, date);
    }

    public static String formatDate(String pattern, Date date) {
        if (date == null)
            return "";
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCuttentTime(String pattern) {
        return formatDate(pattern, new Date());
    }

    public static String getCuttentTime() {
        return getCuttentTime(DEFAUL_PATTERN);
    }
}
