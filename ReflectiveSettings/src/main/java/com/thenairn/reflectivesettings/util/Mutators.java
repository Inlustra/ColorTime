package com.thenairn.reflectivesettings.util;

import android.preference.Preference;
import android.util.Log;

import com.thenairn.reflectivesettings.entity.mutator.CheckboxPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.DatePreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.ListPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.NumberPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.PreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.TextPreferenceMutator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 13/10/15.
 */
public class Mutators {

    private static final Map<Class, PreferenceMutator<? extends Preference>> mutators = new HashMap<>();
    private static final Map<Class, Class<PreferenceMutator>> defaults = new HashMap<>();

    static {
        registerMutator(new ListPreferenceMutator());
        registerMutator(new CheckboxPreferenceMutator());
        registerMutator(new TextPreferenceMutator());
        registerMutator(new NumberPreferenceMutator());
        registerMutator(new DatePreferenceMutator());
    }

    public static void registerMutator(PreferenceMutator<? extends Preference> mutator) {
        Class mclazz = mutator.getClass();
        mutators.put(mclazz, mutator);
        for (Class clazz : mutator.getFieldTypes()) {
            defaults.put(clazz, mclazz);
        }
    }

    public static PreferenceMutator<? extends Preference> getFromFieldType(Class<?> clazz) {
        for (Map.Entry<Class, Class<PreferenceMutator>> e : defaults.entrySet()) {
            Log.e("TEST", e.getKey().getSimpleName() + " " + clazz.getSimpleName() + " " + e.getKey().isAssignableFrom(clazz) + " " + clazz.isAssignableFrom(e.getKey()));
            if (e.getKey().isAssignableFrom(clazz))
                return mutators.get(e.getValue());
        }
        throw new NullPointerException("Not default mutator found for: " + clazz);
    }

    public static PreferenceMutator<? extends Preference> getByClass(Class<PreferenceMutator> type) {
        return mutators.get(type);
    }

    public static PreferenceMutator getMutator(Class mutator, Class fieldType) {
        if (mutator == PreferenceMutator.class) {
            return Mutators.getFromFieldType(fieldType);
        } else {
            return Mutators.getByClass(mutator);
        }
    }

    public static String trimToNull(String in) {
        if (in != null) {
            in = in.trim();
            if (in.isEmpty()) return null;
        }
        return in;
    }

}
