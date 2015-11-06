package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;

import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.lang.reflect.Field;

/**
 * Created by thomas on 13/10/15.
 */
public class ListPreferenceMutator extends PreferenceMutator<ListPreference> {

    @Override
    public ListPreference getPreference(Context context) {
        return new ListPreference(context);
    }

    @Override
    public Class[] getFieldTypes() {
        return new Class[]{Enum.class, String[].class};
    }

    @Override
    protected void setDefaults(ListPreference preference, SettingsPreference settings) {
        super.setDefaults(preference, settings);
        populateFromEnum(preference, settings);
    }

    @Override
    protected void initPreference(ListPreference preference, SettingsPreference settings, SharedPreferences shared) {
        try {
            String str = shared.getString(settings.getKey(), settings.getField().get(null).toString());
            preference.setValue(str);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initField(Field field, String key, SharedPreferences shared) {
        for (Enum enumeration : getEnums(field)) {
            try {
                if (enumeration.name().equals(shared.getString(key, String.valueOf(field.get(null))))) {
                    field.set(null, enumeration);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Preference.OnPreferenceChangeListener onChange(SettingsPreference pref) {
        return new OnChange(pref);
    }

    private void populateFromEnum(ListPreference preference, SettingsPreference settings) {
        Enum[] enums = getEnums(settings);
        CharSequence[] values = new CharSequence[enums.length];
        CharSequence[] strings = new CharSequence[enums.length];
        for (int i = 0; i < enums.length; i++) {
            strings[i] = enums[i].toString();
            values[i] = enums[i].name();
        }
        preference.setEntries(strings);
        preference.setEntryValues(values);
    }

    private static Enum[] getEnums(SettingsPreference preference) {
        return getEnums(preference.getField());
    }

    private static Enum[] getEnums(Field field) {
        Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
        return enumType.getEnumConstants();
    }


    protected static class OnChange implements Preference.OnPreferenceChangeListener {

        private SettingsPreference pref;
        private Enum[] enums;


        public OnChange(SettingsPreference pref) {
            this.pref = pref;
            this.enums = getEnums(pref);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                for (Enum enumeration : enums) {
                    if (enumeration.name().equals(newValue)) {
                        pref.getField().set(null, enumeration);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
