package com.thenairn.colortime.sampler.impl;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.thenairn.colortime.Config;
import com.thenairn.colortime.sampler.ColorSampler;
import com.thenairn.colortime.util.TimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tom on 04/09/2015.
 */
public abstract class SensorSampler implements ColorSampler {

    private Sensorer sensorer = new Sensorer();

    protected abstract int getSensorType();

    protected abstract int getColor(SensorEvent event);

    private SensorEvent event;

    @Override
    public int getColor(long delta) {
        sensorer.register();
        return getColor(event);
    }

    @Override
    public void destroy() {
        sensorer.unregister();
    }

    private class Sensorer implements SensorEventListener {

        private boolean registered = false;

        private void register() {
            if (!registered) {
                registered = true;
                SensorManager manager = ((SensorManager) Config.getContext()
                        .getSystemService(Context.SENSOR_SERVICE));
                manager.registerListener(this,
                        manager.getDefaultSensor(getSensorType()),
                        SensorManager.SENSOR_DELAY_GAME);
            }

        }

        private void unregister() {
            if (registered) {
                registered = false;
                ((SensorManager) Config.getContext().getSystemService(Context.SENSOR_SERVICE))
                        .unregisterListener(this);
            }
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            event = sensorEvent;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
