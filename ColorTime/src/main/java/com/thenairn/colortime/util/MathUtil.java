package com.thenairn.colortime.util;

import android.graphics.Color;

/**
 * Created by Tom on 03/09/2015.
 */
public class MathUtil {

    public static double scale(double unscaledNumber, double minAllowed, double maxAllowed, double min, double max) {
        return (maxAllowed - minAllowed) * (unscaledNumber - min) / (max - min) + minAllowed;
    }
}
