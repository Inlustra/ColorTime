package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;

import com.thenairn.reflectivesettings.entity.SettingsPreference;

/**
 * Created by thomas on 06/11/15.
 */
public class TextPreferenceMutator extends PreferenceMutator<EditTextPreference> {

    @Override
    protected EditTextPreference getPreference(Context context) {
        return new EditTextPreference(context);
    }

    @Override
    public Class[] getFieldTypes() {
        return new Class[]{String.class, CharSequence.class};
    }

    @Override
    protected void initPreference(EditTextPreference preference, SettingsPreference settings, SharedPreferences shared) {
        try {
            preference.setText(shared.getString(settings.getKey(), (String) settings.getField().get(null)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
