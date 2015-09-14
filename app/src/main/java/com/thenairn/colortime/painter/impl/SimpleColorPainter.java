package com.thenairn.colortime.painter.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;

/**
 * Created by thomas on 03/09/15.
 */
public class SimpleColorPainter implements ColorPainter {

    @Override
    public void paint(SurfaceHolder holder, int color, long delta) {
        Canvas canvas = holder.lockCanvas();
        canvas.drawRGB(Color.red(color), Color.green(color), Color.blue(color));
        holder.unlockCanvasAndPost(canvas);
    }
}
