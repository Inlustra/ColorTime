package com.thenairn.reflectivesettings;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by thomas on 15/09/15.
 */
public class SettingsBuilder {

    private final Context context;

    public SettingsBuilder(Context context) {
        this.context = context;
    }

    public String parseString(String string) {
        return context.getResources().getString(this.parseIdentifier(string, "string"));
    }

    public int parseIdentifier(String string, String type) {
        return context.getResources().getIdentifier(string, type, context.getPackageName());
    }

    public int parseDrawable(String string) {
        return parseIdentifier(string, "drawable");
    }

    public String parseOrDefault(String stringid, String string) {
        if (!stringid.isEmpty()) {
            return parseString(stringid);
        }
        return string;
    }

    public int parseDrawableOrDefault(String drawableid, int defaultId) {
        if (!drawableid.isEmpty()) {
            return parseDrawable(drawableid);
        }
        return defaultId;
    }
}
