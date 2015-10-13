package com.thenairn.colortime.util.color;

/**
 * Created by Tom on 09/09/2015.
 */
public class HSL implements Cloneable {

    private final static int DEFAULT_AMOUNT = 30;

    private double hue;
    private double sat;
    private double lum;

    public HSL(double hue, double sat, double lum) {
        this.hue = hue;
        this.sat = sat;
        this.lum = lum;
    }

    public RGB toRGB() {
        double r;
        double g;
        double b;
        if (this.sat == 0) {
            r = g = b = Math.round(this.lum * 2.55);
        } else {
            this.hue /= 60;
            this.sat /= 100;
            this.lum /= 100;
            int i = (int) Math.floor(this.hue);
            double f = this.hue - i;
            double p = this.lum * (1 - this.sat);
            double q = this.lum * (1 - this.sat * f);
            double t = this.lum * (1 - this.sat * (1 - f));
            switch (i) {
                case 0:
                    r = this.lum;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = this.lum;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = this.lum;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = this.lum;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = this.lum;
                    break;
                default:
                    r = this.lum;
                    g = p;
                    b = q;
            }
            r = Math.round(r * 255);
            g = Math.round(g * 255);
            b = Math.round(b * 255);
        }
        return new RGB((int) r, (int) g, (int) b);
    }

    public HSL shift(double amount) {
        this.hue += amount;
        while (this.hue >= 360.0) this.hue -= 360.0;
        while (this.hue < 0.0) this.hue += 360.0;
        return this;
    }

    public HSL complement() {
        return this.shift(180);
    }

    public HSL opposite() {
        this.sat = 100 - this.sat;
        if (this.sat > 40 && this.sat < 60)
            this.sat = 75;
        this.lum = 100 - this.lum;
        if (this.lum > 40 && this.lum < 60)
            this.lum = 75;
        this.shift(180);
        return this;
    }

    private HSL[] analogous(double amount) {
        return new HSL[]{this.clone().shift(-amount), this.clone(), this.clone().shift(amount)};
    }

    public HSL[] analogous() {
        return this.analogous(DEFAULT_AMOUNT);
    }

    public HSL[] triad(double amount) {
        return new HSL[]{this.clone().shift(180 - amount), this.clone(), this.clone().shift(180 + amount)};
    }

    public HSL[] triad() {
        return this.triad(DEFAULT_AMOUNT);
    }

    public HSL[] secondary() {
        return new HSL[]{this.clone().shift(60), this.clone().shift(180), this.clone().shift(300)};
    }

    public HSL clone() {
        return new HSL(this.hue, this.sat, this.lum);
    }

    public HSL darken(double amount) {
        this.lum = Math.max(0, this.lum - amount);
        return this;
    }

    public HSL lighten(double amount) {
        this.lum = Math.min(100, this.lum += amount);
        return this;
    }

    public HSL desaturate(double amount) {
        this.sat = Math.max(0, this.sat - amount);
        return this;
    }

    public HSL saturate(double amount) {
        this.sat = Math.min(100, this.sat += amount);
        return this;
    }
}
