package com.thenairn.colortime.settingscreator.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tom on 09/09/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ListField {
    String key();

    String title() default "";

    String titleId() default "";

    String summary() default "";

    String summaryId() default "";

    String category() default "";

    String categoryId() default "";

}
