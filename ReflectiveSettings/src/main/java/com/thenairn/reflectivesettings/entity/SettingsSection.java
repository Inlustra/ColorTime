package com.thenairn.reflectivesettings.entity;

import com.thenairn.reflectivesettings.util.Mutators;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 15/09/15.
 */
public class SettingsSection {

    private String title;
    private String summary;
    private String category;
    private boolean top;

    private int icon;

    private Map<String, SettingsCategory> categories;

    public SettingsSection(String title, String summary, int icon, boolean top, String category) {
        this.title = title;
        this.summary = Mutators.trimToNull(summary);
        this.icon = icon;
        this.categories = new HashMap<>();
        this.top = top;
        this.category = category;
    }

    public void add(SettingsPreference preference) {
        if (preference.getCategory() != null) {
            getCategory(preference.getCategory()).add(preference);
            return;
        }
        defaultCategory().add(preference);
    }

    private SettingsCategory defaultCategory;

    private SettingsCategory defaultCategory() {
        if (defaultCategory == null) {
            defaultCategory = new SettingsCategory(null);
            this.categories.put(title, defaultCategory);
        }
        return defaultCategory;
    }

    private SettingsCategory getCategory(String category) {
        SettingsCategory cat = this.categories.get(category);
        if (cat == null) {
            this.categories.put(category, cat = new SettingsCategory(category));
        }
        return cat;
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

    public boolean hasIcon() {
        return icon != -1;
    }

    public Collection<SettingsCategory> getCategories() {
        return categories.values();
    }

    public boolean isTop() {
        return top;
    }

    public String getCategory() {
        return category;
    }
}
