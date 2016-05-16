package com.cqgk.shennong.shop.helper;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by duke on 15/12/23.
 */
public class NumberFormatHelper {

    private static int default_digits = 2;

    private static NumberFormat format;

    public static NumberFormat getFormat() {
        return getFormat(default_digits,true);
    }

    public static NumberFormat getFormat(int Digits,boolean showLocale) {
        if(showLocale) {
            format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        }else{
            format = NumberFormat.getCurrencyInstance();
        }
        format.setMaximumFractionDigits(Digits);
        return format;
    }

}
