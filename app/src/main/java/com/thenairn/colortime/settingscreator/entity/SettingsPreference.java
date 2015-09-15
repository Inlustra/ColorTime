package com.thenairn.colortime.settingscreator.entity;

import java.lang.reflect.Field;

/**
 * Created by thomas on 15/09/15.
 */
public class SettingsPreference {

    private final Field field;
    private final String key;
    private final String title;

    private String summary;
    private String category;

    public SettingsPreference(Field field,
                              String key,
                              String title,
                              String summary,
                              String category) {
        this.field = field;
        this.key = key;
        this.title = title;
        this.summary = summary;
        this.category = category;
    }

    public Field getField() {
        return field;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getCategory() {
        return category;
    }
}
