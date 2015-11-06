package com.thenairn.reflectivesettings.annotation;

import com.thenairn.reflectivesettings.entity.mutator.PreferenceMutator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Tom on 09/09/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SettingsField {
    String key();

    String title() default "";

    String titleId() default "";

    String summary() default "";

    String summaryId() default "";

    String category() default "";

    String categoryId() default "";

    Class<PreferenceMutator> type() default PreferenceMutator.class;
}
