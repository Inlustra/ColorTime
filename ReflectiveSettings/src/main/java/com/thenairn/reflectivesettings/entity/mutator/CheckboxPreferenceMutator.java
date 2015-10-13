package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import com.thenairn.reflectivesettings.annotation.CheckboxField;
import com.thenairn.reflectivesettings.entity.SettingsPreference;

/**
 * Created by thomas on 13/10/15.
 */
public class CheckboxPreferenceMutator extends PreferenceMutator<CheckboxField, CheckBoxPreference> {
    @Override
    public Class<CheckboxField> getAnnotationClass() {
        return CheckboxField.class;
    }

    @Override
    protected CheckBoxPreference getType(Context context) {
        return new CheckBoxPreference(context);
    }

    @Override
    protected void initialize(CheckBoxPreference preference,
                              SettingsPreference<CheckboxField> settings,
                              SharedPreferences shared) {
        try {
            preference.setChecked(shared.getBoolean(settings.getKey(), (Boolean) settings.getField().get(null)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
