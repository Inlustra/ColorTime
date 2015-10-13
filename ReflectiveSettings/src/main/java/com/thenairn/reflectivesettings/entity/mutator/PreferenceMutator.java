package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.thenairn.reflectivesettings.annotation.ListField;
import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.lang.annotation.Annotation;

/**
 * Created by thomas on 13/10/15.
 */
public abstract class PreferenceMutator<A extends Annotation, P extends Preference> {

    public abstract Class<A> getAnnotationClass();

    protected abstract P getType(Context context);

    protected void setDefaults(P preference, SettingsPreference<A> settings) {
        preference.setKey(settings.getKey());
        preference.setTitle(settings.getTitle());
        preference.setSummary(settings.getSummary());
        preference.setPersistent(true);
        preference.setOnPreferenceChangeListener(onChange(settings));
    }


    public P get(Context context,
                 SharedPreferences sharedPreferences,
                 SettingsPreference<A> settingsPreference) {
        P pref = getType(context);
        initialize(pref, settingsPreference, sharedPreferences);
        setDefaults(pref, settingsPreference);
        return pref;
    }

    protected Preference.OnPreferenceChangeListener onChange(SettingsPreference pref) {
        return new OnChange(pref);
    }

    protected abstract void initialize(P preference, SettingsPreference<A> settings, SharedPreferences shared);


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
