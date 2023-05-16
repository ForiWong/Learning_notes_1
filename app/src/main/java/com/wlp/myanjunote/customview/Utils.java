package com.wlp.myanjunote.customview;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by wlp on 2023/1/16
 * Description:
 */
public class Utils {
    public static float dp2px(float value/*, Context context*/) {
        //return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }

}