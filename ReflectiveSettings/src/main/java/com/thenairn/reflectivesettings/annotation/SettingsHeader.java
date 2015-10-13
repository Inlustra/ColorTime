package com.thenairn.reflectivesettings.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by thomas on 15/09/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingsHeader {
    String title() default "";

    String titleId() default "";

    String iconId() default "";

    String summary() default "";

    String summaryId() default "";
}
