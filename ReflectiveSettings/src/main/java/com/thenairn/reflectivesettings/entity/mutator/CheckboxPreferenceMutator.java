package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;

import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.lang.reflect.Field;

/**
 * Created by thomas on 13/10/15.
 */
public class CheckboxPreferenceMutator extends PreferenceMutator<CheckBoxPreference> {

    @Override
    protected CheckBoxPreference getPreference(Context context) {
        return new CheckBoxPreference(context);
    }

    @Override
    public Class[] getFieldTypes() {
        return new Class[]{Boolean.class, boolean.class};
    }

    @Override
    public void initField(Field field, String key, SharedPreferences shared) {
        try {
            field.set(null, shared.getBoolean(key, (boolean) field.get(null)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initPreference(CheckBoxPreference preference,
                                  SettingsPreference settings,
                                  SharedPreferences shared) {
        try {
            preference.setChecked(shared.getBoolean(settings.getKey(), (boolean) settings.getField().get(null)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
