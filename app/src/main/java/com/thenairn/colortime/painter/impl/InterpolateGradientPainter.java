package com.thenairn.colortime.painter.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.util.ColorInterpolator;
import com.thenairn.colortime.util.color.RGB;

/**
 * Created by thomas on 03/09/15.
 */
public class InterpolateGradientPainter implements ColorPainter {

    private int color = 0x0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ColorInterpolator interp;
    @Override
    public void paint(final SurfaceHolder holder, final int color, long delta) {
        final int from = this.color;
        if(interp != null) interp.end();
        interp = new ColorInterpolator(from, color, 200, 10) {

            @Override
            public void run(final int color) {
                handler.post(new Runnable() {
                    public void run() {
                        paintColor(holder, color);
                    }
                });
            }
        };
        interp.start();
        this.color = color;
    }

    private void paintColor(SurfaceHolder holder, int color) {
        if (holder == null) return;
        Canvas canvas = holder.lockCanvas();
        LinearGradient gradient = new LinearGradient(0, 0, canvas.getWidth(), canvas.getHeight(),
                new int[]{color, new RGB(color).toHSL().complement().toRGB().toHex()}, new float[]{0f, 1f}, Shader.TileMode.REPEAT);
        Paint p = new Paint();
        p.setDither(true);
        p.setShader(gradient);
        canvas.drawPaint(p);
        holder.unlockCanvasAndPost(canvas);
    }
}
