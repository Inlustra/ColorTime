package com.thenairn.colortime;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.thenairn.colortime.painter.ColorPainter;
import com.thenairn.colortime.painter.PainterType;
import com.thenairn.colortime.sampler.ColorSampler;
import com.thenairn.colortime.sampler.SamplerType;
import com.thenairn.reflectivesettings.SettingsInitializer;
import com.thenairn.reflectivesettings.annotation.SettingsField;
import com.thenairn.reflectivesettings.annotation.SettingsHeader;

/**
 * Created by thomas on 03/09/15.
 */
@SettingsHeader(headerTop = true)
public class ColorWallpaperService extends WallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ColorWallpaperEngine engine;

    @SettingsField(title = "Choose Painter", summary = "Defines how to paint the colors", key = "painter")
    private static PainterType painter = PainterType.Gradient;
    @SettingsField(title = "Choose Sampler", summary = "Defines how the painter gets its colors", key = "sampler")
    private static SamplerType sampler = SamplerType.HSVTimeSampler;

    @Override
    public WallpaperService.Engine onCreateEngine() {
        SettingsInitializer.forPackage("com.thenairn.colortime", this);
        Config.setContext(getApplicationContext());
        Config.setService(this);
        return engine = new ColorWallpaperEngine();
    }

    private SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (engine == null) return;
        engine.reset();
    }


    public class ColorWallpaperEngine extends WallpaperService.Engine {

        private SurfaceHolder holder;
        private Handler handler;

        private WallpaperRunnable wallpapered = new WallpaperRunnable();

        public ColorWallpaperEngine() {
            handler = new Handler();
            reset();
        }

        private void reset() {
            wallpapered.setPainter(painter.getPainter());
            wallpapered.setSampler(sampler.getSampler());
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

