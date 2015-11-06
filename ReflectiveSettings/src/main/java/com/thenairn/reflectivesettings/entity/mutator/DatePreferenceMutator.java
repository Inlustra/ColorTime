package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;

import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.lang.reflect.Field;
import java.util.Date;

import static android.text.InputType.TYPE_CLASS_DATETIME;

/**
 * Created by thomas on 13/10/15.
 */
public class DatePreferenceMutator extends PreferenceMutator<EditTextPreference> {

    @Override
    protected EditTextPreference getPreference(Context context) {
        return new EditTextPreference(context);
    }

    @Override
    public Class[] getFieldTypes() {
        return new Class[]{Date.class};
    }


    @Override
    protected void initPreference(EditTextPreference preference, SettingsPreference settings, SharedPreferences shared) {
        preference.getEditText().setRawInputType(TYPE_CLASS_DATETIME);
        try {
            preference.setText(shared.getString(settings.getKey(), String.valueOf(((Date) settings.getField().get(null)).getTime())));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initField(Field field, String key, SharedPreferences shared) {
        String share = shared.getString(key, null);
        if (share == null) {
            return;
        }
        try {
            field.set(null, new Date(share));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Preference.OnPreferenceChangeListener onChange(final SettingsPreference pref) {
        return new Preference.OnPreferenceChangeListener() {
            Class assignType = pref.getField().getType();

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {

                    pref.getField().set(null, new Date((String) newValue));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            }
        };
    }

}
