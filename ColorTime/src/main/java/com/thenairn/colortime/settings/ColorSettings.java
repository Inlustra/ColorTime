package com.thenairn.colortime.settings;

import android.content.Intent;
import android.content.SharedPreferences;

import com.thenairn.colortime.ColorWallpaperService;
import com.thenairn.reflectivesettings.view.ReflectiveSettingsActivity;

/**
 * Created by Tom on 04/09/2015.
 */
public class ColorSettings extends ReflectiveSettingsActivity {
    @Override
    protected String getPackage() {
        return "com.thenairn.colortime";
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent intent = new Intent(this, ColorWallpaperService.class);
    }
}
