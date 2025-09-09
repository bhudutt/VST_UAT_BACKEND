package com.hitech.dms.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on the method of the Controller that needs to ensure that
 * the interface is brushed and limited
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

	int maxCount(); // maximum number of visits

	int seconds(); // fixed time, unit: s

}
