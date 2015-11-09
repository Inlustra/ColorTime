package com.thenairn.colortime.sampler.impl;

import android.graphics.Color;
import android.util.Log;

import com.thenairn.colortime.sampler.ColorSampler;
import com.thenairn.colortime.util.TimeUtil;
import com.thenairn.reflectivesettings.annotation.SettingsField;
import com.thenairn.reflectivesettings.annotation.SettingsHeader;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by thomas on 03/09/15.
 */
@SettingsHeader(title = "Time Sampler", summary = "Settings")
public class HSVTimeSampler implements ColorSampler {

    @SettingsField(title = "Maximum Value", summary = "Between 0 and 100, the lightest the color can be", key = "hsvt_maxAmount")
    private static int maxVal = 80;
    @SettingsField(title = "Minimum Value", summary = "Between 0 and 100, the darkest the color can be", key = "hsvt_minAmount")
    private static int minVal = 20;

    @SettingsField(title = "Hue scale", summary = "Choose what the color scales on", key = "hsvt_hue")
    private static Scale shue = Scale.Hours;
    @SettingsField(title = "Saturation scale", summary = "Choose what the saturation scales on", key = "hsvt_sat")
    private static Scale ssat = Scale.Minutes;
    @SettingsField(title = "Value scale", summary = "Choose what the brightness scales on", key = "hsvt_val")
    private static Scale sval = Scale.Seconds;

    @Override
    public int getColor(long delta) {
        float min = (float) minVal / 100;
        float max = (float) maxVal / 100;
        float hue = (float) TimeUtil.scaleTime(shue.timeUnit, 0, 360);
        float sat = (float) TimeUtil.scaleTime(ssat.timeUnit, min, max);
        float val = (float) TimeUtil.scaleTime(sval.timeUnit, min, max);
        int color = Color.HSVToColor(new float[]{hue, sat, val});
        return color;
    }

    @Override
    public void destroy() {
    }

    public enum Scale {
        Hours(TimeUnit.HOURS), Minutes(TimeUnit.MINUTES), Seconds(TimeUnit.SECONDS), Milliseconds(TimeUnit.MILLISECONDS);

        TimeUnit timeUnit;

        Scale(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }
}
