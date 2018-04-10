package com.whl.cornerstone.zookeeper.drm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by whling on 2018/4/12.
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DRM
{
  String key() default "";
  
  boolean persistent() default false;
  
  boolean overwrite() default true;
}
