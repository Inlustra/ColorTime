package com.thenairn.reflectivesettings.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 15/09/15.
 */
public class SettingsSection {

    private String title;
    private String summary;

    private int icon;

    private Map<String, SettingsCategory> categories;

    public SettingsSection(String title, String summary, int icon) {
        this.title = title;
        this.summary = summary;
        this.icon = icon;
        this.categories = new HashMap<>();
    }

    public void add(SettingsPreference preference) {
        if (!preference.getCategory().isEmpty()) {
            categories.get(preference.getCategory()).add(preference);
            return;
        }
        defaultCategory().add(preference);
    }

    private SettingsCategory defaultCategory;

    private SettingsCategory defaultCategory() {
        if (defaultCategory == null) {
            defaultCategory = new SettingsCategory(title);
            this.categories.put(title, defaultCategory);
        }
        return defaultCategory;
    }


    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getIcon() {
        return icon;
    }

    public Collection<SettingsCategory> getCategories() {
        return categories.values();
    }
}
