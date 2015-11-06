package com.thenairn.colortime.sampler;

import com.thenairn.colortime.sampler.impl.HSVTimeSampler;
import com.thenairn.colortime.sampler.impl.LightSampler;

/**
 * Created by thomas on 06/11/15.
 */
public enum SamplerType {

    HSVTimeSampler(new HSVTimeSampler(), "Time Based Sampling"),
    SensorSampler(new HSVTimeSampler(), "Sensor based Sampling"),
    LightSampler(new LightSampler(), "Light Based Sampling");

    private final ColorSampler sampler;
    private final String name;

    SamplerType(ColorSampler sampler) {
        this(sampler, null);
    }

    SamplerType(ColorSampler sampler, String name) {
        this.sampler = sampler;
        this.name = name == null ? super.name() : name;
    }

    public ColorSampler getSampler() {
        return sampler;
    }

    @Override
    public String toString() {
        return name;
    }
}