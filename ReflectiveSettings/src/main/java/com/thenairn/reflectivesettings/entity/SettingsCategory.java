package com.thenairn.reflectivesettings.entity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by thomas on 15/09/15.
 */
public class SettingsCategory {

    private final String title;

    private Set<SettingsPreference> preferences;

    public SettingsCategory(String title) {
        this.title = title;
        this.preferences = new LinkedHashSet<>();
    }

    public void add(SettingsPreference preference) {
        this.preferences.add(preference);
    }

    public String getTitle() {
        return title;
    }

    public Set<SettingsPreference> getPreferences() {
        return preferences;
    }
}
