package com.thenairn.reflectivesettings.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.thenairn.reflectivesettings.SettingsCreator;
import com.thenairn.reflectivesettings.entity.SettingsCategory;
import com.thenairn.reflectivesettings.entity.SettingsPreference;
import com.thenairn.reflectivesettings.entity.SettingsSection;
import com.thenairn.reflectivesettings.entity.mutator.PreferenceMutator;
import com.thenairn.reflectivesettings.util.Mutators;
import com.thenairn.reflectivesettings.util.ReflectionsUtil;

import java.lang.reflect.Field;

/**
 * Created by thomas on 15/09/15.
 */
public abstract class ReflectiveSettingsActivity extends PreferenceActivity {

    private PreferenceScreen screen;
    private SharedPreferences shared;
    private SettingsCreator creator;

    private SettingsCreator getCreator() {
        if (creator == null) {
            creator = new SettingsCreator(getPackage(), getApplicationContext());
        }
        return creator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = getPreferenceManager().createPreferenceScreen(this);
        shared = PreferenceManager.getDefaultSharedPreferences(this);
        for (int i = 0; i < getCreator().getSections().size(); i++) {
            SettingsSection section = creator.getSection(i);
            for (SettingsCategory category : section.getCategories()) {
                screen.addPreference(createCategory(category));
            }
        }
        setPreferenceScreen(screen);
    }

    protected abstract String getPackage();


    public PreferenceCategory createCategory(SettingsCategory settings) {
        PreferenceCategory category = new PreferenceCategory(this);
        try {
            Field preferenceManager = ReflectionsUtil.getField(category.getClass(), "mPreferenceManager");
            preferenceManager.setAccessible(true);
            preferenceManager.set(category, getPreferenceManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        category.setTitle(settings.getTitle());
        for (SettingsPreference pref : settings.getPreferences()) {
            PreferenceMutator mutator = Mutators.get(pref.getAnnotationType());
            category.addPreference(mutator.get(this, shared, pref));
        }
        return category;
    }
}