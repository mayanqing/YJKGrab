package com.romens.yjkgrab.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by myq on 15-12-9.
 */
public class DimenHelper {
    private static DisplayMetrics metrics;

    public static int dp(float px, Context context) {

        return (int) (0.5f + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getMetrics(context)));
    }

    public static int sp(float px, Context context) {

        return (int) (0.5f + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, getMetrics(context)));
    }

    private static DisplayMetrics getMetrics(Context context) {
        if (metrics == null) {
            synchronized (DimenHelper.class) {
                if (metrics == null) {
                    metrics = context.getApplicationContext().getResources().getDisplayMetrics();
                }
            }
        }
        return metrics;
    }


}
