package com.thenairn.colortime.painter.impl;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.util.color.HSL;
import com.thenairn.colortime.util.color.RGB;
import com.thenairn.reflectivesettings.annotation.SettingsField;
import com.thenairn.reflectivesettings.annotation.SettingsHeader;

/**
 * Created by Tom on 09/09/2015.
 */
@SettingsHeader(title = "Gradient Painter", summary = "Testing summary")
public class GradientPainter implements ColorPainter {

    private Paint paint;

    @SettingsField(title = "Shift", summary = "Move the second color around the spectrum", key = "shift")
    private static boolean shift = true;
    @SettingsField(title = "Shift Amount", summary = "The amount to move it around the spectrum (180 is complement)", key = "shiftAmount")
    private static double shiftAmount = 180;
    @SettingsField(title = "Darken", summary = "Darken the second color", key = "darken")
    private static boolean darken = false;
    @SettingsField(title = "Darken Amount", summary = "The amount to darken the color by (between 0 - 100)", key = "darkenAmount")
    private static double darkenAmount = 30;
    @SettingsField(title = "Lighten", summary = "Lighten the second color", key = "lighten")
    private static boolean lighten = false;
    @SettingsField(title = "Lighten Amount", summary = "The amount to lighten the color by (between 0 - 100)", key = "lightenAmount")
    private static double lightenAmount = 30;


    public GradientPainter() {
        paint = new Paint();
        paint.setDither(true);
    }

    @Override
    public void paint(final SurfaceHolder holder, final int color, long delta) {
        paintColor(holder, color);
    }

    private void paintColor(SurfaceHolder holder, int color) {
        if (holder == null) return;
        Canvas canvas = holder.lockCanvas();
        LinearGradient gradient = new LinearGradient(0, 0, canvas.getWidth(), canvas.getHeight(),
                new int[]{color, change(new RGB(color).toHSL()).toRGB().toHex()}, new float[]{0f, 1f}, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawPaint(paint);
        holder.unlockCanvasAndPost(canvas);
    }

    private HSL change(HSL hsl) {
        if (darken) hsl.darken(darkenAmount);
        if (shift) hsl.shift(shiftAmount);
        if (lighten) hsl.lighten(lightenAmount);
        return hsl;
    }
}