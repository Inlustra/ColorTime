package com.thenairn.reflectivesettings.util;

import com.thenairn.reflectivesettings.entity.mutator.CheckboxPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.ListPreferenceMutator;
import com.thenairn.reflectivesettings.entity.mutator.PreferenceMutator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by thomas on 02/10/15.
 */
public class ReflectionsUtil {

    public static Field access(Field field) {
        field.setAccessible(true);
        return field;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Class<?> tmpClass = clazz;
        do {
            try {
                Field f = tmpClass.getDeclaredField(fieldName);
                return access(f);
            } catch (NoSuchFieldException e) {
                tmpClass = tmpClass.getSuperclass();
            }
        } while (tmpClass != null);

        throw new RuntimeException("Field '" + fieldName
                + "' not found on class " + clazz);
    }
}
