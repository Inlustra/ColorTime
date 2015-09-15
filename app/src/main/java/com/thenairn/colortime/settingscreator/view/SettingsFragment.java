package com.thenairn.colortime.settingscreator.view;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.thenairn.colortime.R;
import com.thenairn.colortime.settingscreator.entity.SettingsCategory;
import com.thenairn.colortime.settingscreator.entity.SettingsPreference;
import com.thenairn.colortime.settingscreator.entity.SettingsSection;

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
        setPreferenceScreen(screen);
        for (SettingsCategory cat : section.getCategories()) {
            screen.addPreference(createCategory(cat));
        }
    }

    public PreferenceCategory createCategory(SettingsCategory settings) {
        Log.e("SettingsFragment", "Adding Category!");
        PreferenceCategory category = new PreferenceCategory(screen.getContext());
        category.setTitle(settings.getTitle());
        for (SettingsPreference pref : settings.getPreferences()) {
            category.addPreference(createPreference(pref));
        }
        return category;
    }

    private Preference createPreference(SettingsPreference pref) {
        Log.e("SettingsFragment", "Adding Preference!");
        Class<?> clazz = pref.getField().getType();
        if (clazz.isAssignableFrom(Boolean.class) ||
                clazz.isAssignableFrom(boolean.class)) {
            return createCheckbox(pref);
        }
        Log.e("CALLED","SAFAFFASFSAFFAA");
        return null;
    }

    private Preference createCheckbox(SettingsPreference pref) {
        Log.e("SettingsFragment", "Adding Checkbox!");
        CheckBoxPreference checkBoxPref = new CheckBoxPreference(screen.getContext());
        checkBoxPref.setKey(pref.getKey());
        checkBoxPref.setTitle(pref.getTitle());
        checkBoxPref.setSummary(pref.getSummary());
        try {
            checkBoxPref.setChecked(shared.getBoolean(pref.getKey(), (Boolean) pref.getField().get(null)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return addChange(checkBoxPref, pref);
    }

    public Preference addChange(Preference preference, SettingsPreference settings) {
        preference.setOnPreferenceChangeListener(new OnChange(settings));
        return preference;
    }

    public static SettingsFragment fromSection(SettingsSection section) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.section = section;
        return fragment;
    }

    private static class OnChange implements Preference.OnPreferenceChangeListener {

        private SettingsPreference pref;

        public OnChange(SettingsPreference pref) {
            this.pref = pref;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                pref.getField().set(null, newValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
