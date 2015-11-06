package com.thenairn.reflectivesettings.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.thenairn.reflectivesettings.SettingsInitializer;
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
public abstract class ReflectiveSettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferenceScreen screen;
    private SharedPreferences shared;
    private SettingsInitializer creator;

    private SettingsInitializer getCreator() {
        if (creator == null) {
            creator = SettingsInitializer.forPackage(getPackage(), getApplicationContext());
        }
        return creator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = getPreferenceManager().createPreferenceScreen(this);
        shared = PreferenceManager.getDefaultSharedPreferences(this);
        for (SettingsSection section : getCreator().getMainPreferences()) {
            for (SettingsCategory category : section.getCategories()) {
                if (category.getTitle() != null) {
                    screen.addPreference(createCategory(category));
                    continue;
                }
                for (SettingsPreference pref : category.getPreferences()) {
                    screen.addPreference(createPreference(pref));
                }
            }
        }
        for (int i = 0; i < getCreator().getSections().size(); i++) {
            SettingsSection section = getCreator().getSection(i);
            PreferenceScreen screen2 = getPreferenceManager().createPreferenceScreen(this);
            screen2.setTitle(section.getTitle());
            screen2.setSummary(section.getSummary());
            if (section.hasIcon())
                screen2.setIcon(section.getIcon());
            for (SettingsCategory category : section.getCategories()) {
                if (category.getTitle() != null) {
                    screen2.addPreference(createCategory(category));
                    continue;
                }
                for (SettingsPreference pref : category.getPreferences()) {
                    screen2.addPreference(createPreference(pref));
                }
            }
            screen.addPreference(screen2);
        }
        setPreferenceScreen(screen);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
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
            category.addPreference(createPreference(pref));
        }
        return category;
    }

    public Preference createPreference(SettingsPreference pref) {
        PreferenceMutator mutator;
        Class type = pref.getMutator();
        if (type == PreferenceMutator.class) {
            mutator = Mutators.getFromFieldType(pref.getField().getType());
        } else {
            mutator = Mutators.getByClass(type);
        }
        return mutator.get(this, shared, pref);
    }

}