package com.thenairn.colortime.util.color;

import android.graphics.Color;

/**
 * Created by Tom on 09/09/2015.
 */
public class RGB {
    private int red;
    private int green;
    private int blue;

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public RGB(int color) {
        this.red = Color.red(color);
        this.green = Color.green(color);
        this.blue = Color.blue(color);
    }

    public int toHex() {
        return Color.rgb(this.red, this.green, this.blue);
    }

    public HSL toHSL() {
        double h, s, l;
        double max = max(this.red, this.green, this.blue);
        double dif = max - min(this.red, this.green, this.blue);
        s = (max == 0.0) ? 0 : (100 * dif / max);
        if (s == 0) h = 0;
        else if (this.red == max) h = 60.0 * (this.green - this.blue) / dif;
        else if (this.green == max) h = 120.0 + 60.0 * (this.blue - this.red) / dif;
        else h = 240.0 + 60.0 * (this.red - this.green) / dif;
        if (h < 0.0) h += 360.0;
        l = Math.round(max * 100 / 255);
        h = Math.round(h);
        s = Math.round(s);
        return new HSL(h, s, l);
    }

    public static double max(double... n) {
        int i = 0;
        double max = n[i];
        while (++i < n.length)
            if (n[i] > max)
                max = n[i];

        return max;
    }

    public static double min(double... numbers) {
        double min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            min = (min <= numbers[i]) ? min : numbers[i];
        }
        return min;
    }
}
