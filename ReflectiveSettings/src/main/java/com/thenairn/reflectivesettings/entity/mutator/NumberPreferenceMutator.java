package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;

import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.lang.reflect.Field;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

/**
 * Created by thomas on 06/11/15.
 */
public class NumberPreferenceMutator extends PreferenceMutator<EditTextPreference> {

    @Override
    protected EditTextPreference getPreference(Context context) {
        return new EditTextPreference(context);
    }

    @Override
    public Class[] getFieldTypes() {
        return new Class[]{int.class, Integer.class, float.class, Float.class, Double.class, double.class, short.class, Short.class, long.class, Long.class};
    }

    private boolean isDecimal(Class assignType) {
        return double.class.isAssignableFrom(assignType) || Double.class.isAssignableFrom(assignType)
                || Float.class.isAssignableFrom(assignType) || float.class.isAssignableFrom(assignType);
    }

    @Override
    public void initField(Field field, String key, SharedPreferences shared) {
        Class assignType = field.getType();

        try {
            String newValue = shared.getString(key, String.valueOf(field.get(null)));
            if (Float.class.isAssignableFrom(assignType) || float.class.isAssignableFrom(assignType)) {
                field.set(null, Float.parseFloat(newValue));
            } else if (double.class.isAssignableFrom(assignType) || Double.class.isAssignableFrom(assignType)) {
                field.set(null, Double.parseDouble(newValue));
            } else if (int.class.isAssignableFrom(assignType) || Integer.class.isAssignableFrom(assignType)) {
                field.set(null, Integer.parseInt(newValue));
            } else if (short.class.isAssignableFrom(assignType) || Short.class.isAssignableFrom(assignType)) {
                field.set(null, Short.parseShort(newValue));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initPreference(EditTextPreference preference, SettingsPreference settings, SharedPreferences shared) {
        Class assignType = settings.getField().getType();
        int type = TYPE_CLASS_NUMBER;
        if (isDecimal(assignType))
            type |= TYPE_NUMBER_FLAG_DECIMAL;
        preference.getEditText().setRawInputType(type);
        preference.getEditText().setInputType(type);
        try {
            preference.setText(shared.getString(settings.getKey(), String.valueOf(settings.getField().get(null))));
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
                    if (Float.class.isAssignableFrom(assignType) || float.class.isAssignableFrom(assignType)) {
                        pref.getField().set(null, Float.parseFloat((String) newValue));
                    } else if (double.class.isAssignableFrom(assignType) || Double.class.isAssignableFrom(assignType)) {
                        pref.getField().set(null, Double.parseDouble((String) newValue));
                    } else if (int.class.isAssignableFrom(assignType) || Integer.class.isAssignableFrom(assignType)) {
                        pref.getField().set(null, Integer.parseInt((String) newValue));
                    } else if (short.class.isAssignableFrom(assignType) || Short.class.isAssignableFrom(assignType)) {
                        pref.getField().set(null, Short.parseShort((String) newValue));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            }
        };
    }
}
