package com.thenairn.reflectivesettings.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by thomas on 15/09/15.
 */
public class SettingsPreference<T extends Annotation> {

    private Class<T> annotationType;
    private final Field field;
    private final String key;
    private final String title;

    private String summary;
    private String category;

    public SettingsPreference(Class<T> annotationType,
                              Field field,
                              String key,
                              String title,
                              String summary,
                              String category) {
        this.annotationType = annotationType;
        this.field = field;
        this.key = key;
        this.title = title;
        this.summary = summary;
        this.category = category;
    }

    public Field getField() {
        return field;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getCategory() {
        return category;
    }

    public Class<T> getAnnotationType() {
        return annotationType;
    }

    public T getAnnotation() {
        return field.getAnnotation(annotationType);
    }
}
