package com.thenairn.colortime.sampler.impl;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.thenairn.colortime.settingscreator.SettingsConfigurable;
import com.thenairn.colortime.util.MathUtil;
import com.thenairn.colortime.util.TimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tom on 04/09/2015.
 */
public class LightSampler extends SensorSampler {

    @SettingsConfigurable
    private boolean enabled;

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_LIGHT;
    }

    @Override
    protected int getColor(SensorEvent event) {
        if(event == null) {
            return 0;
        }
        float light = event.values[0];
        float hue = (float) TimeUtil.scaleTime(TimeUnit.SECONDS, 0, 360);
        float sat = (float) MathUtil.scale(light,0.1,0.9,0,300);
        float val = (float) MathUtil.scale(light, 0.1, 0.9, 0, 300);
        return Color.HSVToColor(new float[]{hue, sat, val});
    }
}
