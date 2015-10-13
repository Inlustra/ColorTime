package com.thenairn.colortime.sampler;

import android.graphics.Color;

/**
 * Created by thomas on 03/09/15.
 */
public interface ColorSampler {
    int getColor(long delta);
    void destroy();
}
