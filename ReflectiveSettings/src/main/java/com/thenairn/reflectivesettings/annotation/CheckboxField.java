package com.thenairn.reflectivesettings.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by thomas on 12/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CheckboxField {
    String key();

    String title() default "";

    String titleId() default "";

    String summary() default "";

    String summaryId() default "";

    String category() default "";

    String categoryId() default "";
}
