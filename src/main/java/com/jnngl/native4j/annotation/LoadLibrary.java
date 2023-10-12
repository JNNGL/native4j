package com.jnngl.native4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(LibraryContainer.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadLibrary {

  String value();
}