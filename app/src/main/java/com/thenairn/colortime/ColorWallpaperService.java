package com.thenairn.colortime;

import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.painter.impl.GradientPainter;
import com.thenairn.colortime.sampler.ColorSampler;
import com.thenairn.colortime.sampler.impl.HSVTimeSampler;
import com.thenairn.colortime.settingscreator.annotation.CheckboxField;
import com.thenairn.colortime.settingscreator.annotation.ListField;
import com.thenairn.colortime.settingscreator.annotation.SettingsHeader;

/**
 * Created by thomas on 03/09/15.
 */
@SettingsHeader(titleId = "settings_title")
public class ColorWallpaperService extends WallpaperService {

    @CheckboxField(title = "Something else", key = "Disabling this will enable light sensor")
    private static boolean time = true;
    @CheckboxField(title = "settings_title", key = "Disabling this will enable simple painter")
    private static boolean gradient = false;


    @Override
    public WallpaperService.Engine onCreateEngine() {
        Config.setContext(getApplicationContext());
        Config.setService(this);
        return new ColorWallpaperEngine();
    }

    public class ColorWallpaperEngine extends WallpaperService.Engine {

        private SurfaceHolder holder;
        private Handler handler;

        private WallpaperRunnable wallpapered = new WallpaperRunnable();

        public ColorWallpaperEngine() {
            handler = new Handler();
            wallpapered.setPainter(new GradientPainter());
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
                wallpapered.run();
            } else {
                wallpapered.stop();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }

        private class WallpaperRunnable implements Runnable {

            private ColorSampler sampler;
            private ColorPainter painter;
            private volatile boolean registered = false;
            private long lastrun = System.currentTimeMillis();

            public void setSampler(ColorSampler sampler) {
                this.sampler = sampler;
            }

            public void setPainter(ColorPainter painter) {
                this.painter = painter;
            }

            @Override
            public void run() {
                long now = System.currentTimeMillis();
                long delta = now - this.lastrun;
                painter.paint(holder, sampler.getColor(delta), delta);
                this.lastrun = now;
                runDelayed();
            }

            public void runDelayed() {
                handler.postDelayed(this, 10);
                registered = true;
            }

            public void stop() {
                if (registered) {
                    handler.removeCallbacks(this);
                    sampler.destroy();
                }
                registered = false;
            }

        }
    }
}

