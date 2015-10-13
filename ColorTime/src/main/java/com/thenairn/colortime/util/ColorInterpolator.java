package com.thenairn.colortime.util;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.view.SurfaceHolder;

/**
 * Created by Tom on 03/09/2015.
 */
public abstract class ColorInterpolator extends Thread {

    private static final ArgbEvaluator eval = new ArgbEvaluator();
    private int to, from;
    private int duration;
    private int iterations;
    private int sleep;
    private int current = 0;
    private volatile boolean end = false;

    public ColorInterpolator(int to, int from, int duration, int iterations) {
        this.to = to;
        this.from = from;

        this.duration = duration;
        this.iterations = iterations;
        this.sleep = duration / iterations;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            if(end){
                return;
            }
            float fraction = (float) i / (float) iterations;
            int color = interpolateColor(to, from, fraction);
            run(color);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void run(int color);

    /**
     * Returns an interpoloated color, between <code>a</code> and <code>b</code>
     */
    private int interpolateColor(int a, int b, float proportion) {
        return (Integer) eval.evaluate(proportion, a, b);
    }

    public void end() {
        this.end = true;
    }
}
