package com.thenairn.colortime;

import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.painter.SimpleColorPainter;
import com.thenairn.colortime.sampler.ColorSampler;
import com.thenairn.colortime.sampler.impl.HSVTimeSampler;

/**
 * Created by thomas on 03/09/15.
 */
public class ColorWallpaperService extends WallpaperService {
    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new ColorWallpaperEngine();
    }

    public class ColorWallpaperEngine extends WallpaperService.Engine {

        private SurfaceHolder holder;
        private Handler handler;

        private WallpaperRunnable wallpapered = new WallpaperRunnable();

        public ColorWallpaperEngine() {
            handler = new Handler();
            wallpapered.setPainter(new SimpleColorPainter());
            wallpapered.setSampler(new HSVTimeSampler());
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }


        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                handler.postDelayed(wallpapered, 1000);
            } else {
                handler.removeCallbacks(wallpapered);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            handler.removeCallbacks(wallpapered);
        }

        private class WallpaperRunnable implements Runnable {

            private ColorSampler sampler;
            private ColorPainter painter;

            public void setSampler(ColorSampler sampler) {
                this.sampler = sampler;
            }

            public void setPainter(ColorPainter painter) {
                this.painter = painter;
            }

            @Override
            public void run() {
                System.out.println("Called! "+sampler.getColor());
                painter.paint(holder, sampler.getColor());
            }
        }
    }
}

