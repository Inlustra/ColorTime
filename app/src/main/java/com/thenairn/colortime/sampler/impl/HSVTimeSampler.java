package com.thenairn.colortime.sampler.impl;

import android.graphics.Color;
import android.util.Log;

import com.thenairn.colortime.sampler.ColorSampler;
import com.thenairn.colortime.util.TimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by thomas on 03/09/15.
 */
public class HSVTimeSampler implements ColorSampler {

    @Override
    public int getColor() {
        float hue = (float) TimeUtil.scaleTime(TimeUnit.HOURS, 0, 360);
        float sat = (float) TimeUtil.scaleTime(TimeUnit.MINUTES, 0, 1);
        float val = (float) TimeUtil.scaleTime(TimeUnit.SECONDS, 0, 360);
        return Color.HSVToColor(new float[]{hue, sat, val});
    }
}
