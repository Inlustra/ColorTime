package com.thenairn.colortime.util;

import java.lang.reflect.Field;

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
