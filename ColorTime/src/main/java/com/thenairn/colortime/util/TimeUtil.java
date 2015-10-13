package com.thenairn.colortime.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by thomas on 03/09/15.
 */
public class TimeUtil {


    public static double scaleTime(TimeUnit unit, double min, double max) {
        return MathUtil.scale(current(unit), min, max, 0, unitMax(unit));
    }

    public static double current(TimeUnit unit) {
        TimeUnit a = unitAbove(unit);
        long above = a.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        long millis = TimeUnit.MILLISECONDS.convert(above, a);
        if(unit == SECONDS) {
            return (double)(System.currentTimeMillis() - millis) / 1000;
        }
        return unit.convert(System.currentTimeMillis() - millis, TimeUnit.MILLISECONDS);
    }

    public static double currentSeconds(TimeUnit unit) {
        TimeUnit a = unitAbove(unit);
        long above = a.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        long millis = TimeUnit.MILLISECONDS.convert(above, a);
        return unit.convert(System.currentTimeMillis() - millis, TimeUnit.MILLISECONDS);
    }

    public static double scaleTime(TimeUnit unit, double min, double max, long millis) {
        return MathUtil.scale(unit.convert(millis, TimeUnit.MILLISECONDS), min, max, 0, unitMax(unit));
    }

    public static int unitMax(TimeUnit unit) {
        switch (unit) {
            case DAYS:
                return 30;
            case HOURS:
                return 24;
            case SECONDS:
            case MINUTES:
                return 60;
            case MILLISECONDS:
                return 100;
            case MICROSECONDS:
            case NANOSECONDS:
                return 1000;
            default:
                return 100;

        }
    }

    public static TimeUnit unitAbove(TimeUnit unit) {
        switch (unit) {
            case HOURS:
                return DAYS;
            case MINUTES:
                return HOURS;
            case SECONDS:
                return MINUTES;
            case MILLISECONDS:
                return SECONDS;
            case MICROSECONDS:
                return MILLISECONDS;
            case NANOSECONDS:
                return MICROSECONDS;
            default:
                return DAYS;
        }
    }

}
