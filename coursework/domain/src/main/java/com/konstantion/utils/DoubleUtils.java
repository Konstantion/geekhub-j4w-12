package com.konstantion.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleUtils {
    private DoubleUtils() {
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    /**
     * Take percent of double value, <b style='color:red'>percent</b> should be between 0 and 100
     */
    public static double percent(double value, double percent) {
        return value * (percent / 100.0);
    }
}
