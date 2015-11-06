package com.thenairn.reflectivesettings.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

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
public class SettingsFragment extends PreferenceFragment {

    private SettingsSection section;
    private PreferenceScreen screen;
    private SharedPreferences shared;

    public SettingsSection getSection() {
        return section;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = getPreferenceManager().createPreferenceScreen(getActivity());
        shared = PreferenceManager.getDefaultSharedPreferences(screen.getContext());
        for (SettingsCategory cat : section.getCategories()) {
            screen.addPreference(createCategory(cat));
        }

        setPreferenceScreen(screen);
    }

    public PreferenceCategory createCategory(SettingsCategory settings) {
        PreferenceCategory category = new PreferenceCategory(getActivity());
        try {
            Field preferenceManager = ReflectionsUtil.getField(category.getClass(), "mPreferenceManager");
            preferenceManager.setAccessible(true);
            preferenceManager.set(category, getPreferenceManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        category.setTitle(settings.getTitle());
        for (SettingsPreference pref : settings.getPreferences()) {
            PreferenceMutator mutator = Mutators.getFromFieldType(pref.getMutator());
            category.addPreference(mutator.get(screen.getContext(), shared, pref));
        }
        return category;
    }


    public static SettingsFragment fromSection(SettingsSection section) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.section = section;
        return fragment;
    }

    private PreferenceManager prefInstance;

    public PreferenceManager getPreferenceManager() {
        if (prefInstance == null) {
            try {
                prefInstance = (PreferenceManager) ReflectionsUtil.getField(this.getClass(),
                        "mPreferenceManager").get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return prefInstance;
    }


}
