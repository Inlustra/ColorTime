package com.thenairn.colortime.settingscreator.annotation;

/**
 * Created by thomas on 12/10/15.
 */
public @interface CheckboxField {
    String key();

    String title() default "";

    String titleId() default "";

    String summary() default "";

    String summaryId() default "";

    String category() default "";

    String categoryId() default "";
}
