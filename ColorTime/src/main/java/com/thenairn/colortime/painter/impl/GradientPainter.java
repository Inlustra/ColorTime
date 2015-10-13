package com.thenairn.colortime.painter.impl;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.util.color.RGB;

/**
 * Created by Tom on 09/09/2015.
 */
public class GradientPainter implements ColorPainter {

    private int color = 0x0;
    private Paint paint;
    private Handler handler = new Handler(Looper.getMainLooper());

    public GradientPainter() {
        paint = new Paint();
        paint.setDither(true);
    }

    @Override
    public void paint(final SurfaceHolder holder, final int color, long delta) {
        paintColor(holder, color);
        this.color = color;
    }

    private void paintColor(SurfaceHolder holder, int color) {
        if (holder == null) return;
        Canvas canvas = holder.lockCanvas();
        LinearGradient gradient = new LinearGradient(0, 0, canvas.getWidth(), canvas.getHeight(),
                new int[]{color, new RGB(color).toHSL().complement().toRGB().toHex()}, new float[]{0f, 1f}, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawPaint(paint);
        holder.unlockCanvasAndPost(canvas);
    }
}