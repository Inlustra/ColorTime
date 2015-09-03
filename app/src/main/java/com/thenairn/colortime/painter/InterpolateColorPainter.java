package com.thenairn.colortime.painter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * Created by thomas on 03/09/15.
 */
public class InterpolateColorPainter implements ColorPainter {

    private int color = 0x0;

    @Override
    public void paint(SurfaceHolder holder, int color) {
        new InterpolateThread(holder, this.color, color, 100).start();
    }

    private void paintColor(SurfaceHolder holder, int color) {
        Canvas canvas = holder.lockCanvas();
        canvas.drawRGB(Color.red(color), Color.green(color), Color.blue(color));
        holder.unlockCanvasAndPost(canvas);
    }

    private class InterpolateThread extends Thread {
        SurfaceHolder holder;
        int color;
        int to;
        int time;

        private InterpolateThread(SurfaceHolder holder, int color, int to, int time) {

        }

        @Override
        public void run() {
            for (int i = 0; i < 255; i++) {
                handler.post(new Runnable() {
                    public void run() {
                        screen.setBackgroundColor(Color.argb(255, i, i, i));
                    }
                });
                // next will pause the thread for some time
                try {
                    sleep(10);
                } catch {
                    break;
                }
            }
        }
    }
}
