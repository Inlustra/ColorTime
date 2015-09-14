package com.thenairn.colortime.painter;

import android.graphics.Color;
import android.view.SurfaceHolder;

import com.thenairn.colortime.sampler.ColorSampler;

/**
 * Created by thomas on 03/09/15.
 */
public interface ColorPainter {
    void paint(SurfaceHolder holder, int color, long delta);
}
