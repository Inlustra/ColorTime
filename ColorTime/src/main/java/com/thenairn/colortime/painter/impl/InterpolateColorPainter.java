package com.thenairn.colortime.painter.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.util.ColorInterpolator;
import com.thenairn.reflectivesettings.annotation.SettingsHeader;

/**
 * Created by thomas on 03/09/15.
 */
public class InterpolateColorPainter implements ColorPainter {

    private int color = 0x0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void paint(final SurfaceHolder holder, final int color, long delta) {
        final int from = InterpolateColorPainter.this.color;
        System.out.println(delta);
        new ColorInterpolator(from, color, (int) delta, 10) {

            @Override
            public void run(final int color) {
                handler.post(new Runnable() {
                    public void run() {
                        paintColor(holder, color);
                    }
                });
            }
        }.start();
        this.color = color;
    }

    private void paintColor(SurfaceHolder holder, int color) {
        if (holder == null) return;
        Canvas canvas = holder.lockCanvas();
        canvas.drawRGB(Color.red(color), Color.green(color), Color.blue(color));
        holder.unlockCanvasAndPost(canvas);
    }
}
