package com.thenairn.colortime;

import android.content.Context;

/**
 * Created by Tom on 04/09/2015.
 */
public class Config {
    private static Context context;
    private static ColorWallpaperService service;

    public static Context getContext() {
        return context;
    }

    static void setContext(Context context) {
        Config.context = context;
    }

    public static ColorWallpaperService getService() {
        return service;
    }

    static void setService(ColorWallpaperService service) {
        Config.service = service;
    }
}
