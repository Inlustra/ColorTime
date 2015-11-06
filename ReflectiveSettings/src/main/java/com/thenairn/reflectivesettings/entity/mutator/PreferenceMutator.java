package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.lang.reflect.Field;

/**
 * Created by thomas on 13/10/15.
 */
public abstract class PreferenceMutator<P extends Preference> {

    protected abstract P getPreference(Context context);

    public abstract Class[] getFieldTypes();

    protected void setDefaults(P preference, SettingsPreference settings) {
        preference.setKey(settings.getKey());
        preference.setTitle(settings.getTitle());
        preference.setSummary(settings.getSummary());
        preference.setPersistent(true);
        preference.setOnPreferenceChangeListener(onChange(settings));
    }


    public P get(Context context,
                 SharedPreferences sharedPreferences,
                 SettingsPreference settingsPreference) {
        P pref = getPreference(context);
        initPreference(pref, settingsPreference, sharedPreferences);
        setDefaults(pref, settingsPreference);
        return pref;
    }

    protected Preference.OnPreferenceChangeListener onChange(SettingsPreference pref) {
        return new OnChange(pref);
    }

    public void initField(Field field, String key, SharedPreferences shared) {
        try {
            field.set(null, shared.getString(key, (String) field.get(null)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract void initPreference(P preference, SettingsPreference settings, SharedPreferences shared);


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
