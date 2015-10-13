package com.thenairn.reflectivesettings.util;

import android.preference.Preference;

import com.thenairn.reflectivesettings.entity.mutator.CheckboxPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.ListPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.PreferenceMutator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by thomas on 13/10/15.
 */
public class Mutators {

    private static final Map<Class<? extends Annotation>,
            PreferenceMutator<? extends Annotation, ? extends Preference>>
            mutators = new HashMap<>();

    static {
        registerMutator(new ListPreferenceMutator());
        registerMutator(new CheckboxPreferenceMutator());
    }

    public static void registerMutator(PreferenceMutator<
            ? extends Annotation,
            ? extends Preference> mutator) {
        mutators.put(mutator.getAnnotationClass(), mutator);
    }

    public static Set<Class<? extends Annotation>> getTypes() {
        return mutators.keySet();
    }

    public static PreferenceMutator<? extends Annotation, ? extends Preference> get(Class clazz) {
        return mutators.get(clazz);
    }

}
