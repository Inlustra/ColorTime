package com.thenairn.reflectivesettings.entity.mutator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;

import com.thenairn.reflectivesettings.annotation.ListField;
import com.thenairn.reflectivesettings.entity.SettingsPreference;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 13/10/15.
 */
public class ListPreferenceMutator extends PreferenceMutator<ListField, ListPreference> {

    @Override
    public Class<ListField> getAnnotationClass() {
        return ListField.class;
    }

    @Override
    public ListPreference getType(Context context) {
        return new ListPreference(context);
    }

    @Override
    protected void setDefaults(ListPreference preference, SettingsPreference<ListField> settings) {
        super.setDefaults(preference, settings);
        if (settings.getField().getType().isEnum()) {
            populateFromEnum(preference, settings);
            return;
        }
        Log.e("ListPreferenceMutator", "Could not populate list, return type was not enum.");
    }

    @Override
    protected void initialize(ListPreference preference, SettingsPreference<ListField> settings, SharedPreferences shared) {
        try {
            String str = shared.getString(settings.getKey(), settings.getField().get(null).toString());
            preference.setValue(str);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Preference.OnPreferenceChangeListener onChange(SettingsPreference pref) {
        return new OnChange(pref);
    }

    private void populateFromEnum(ListPreference preference, SettingsPreference<ListField> settings) {
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
        Class<? extends Enum> enumType = (Class<? extends Enum>) preference.getField().getType();
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
