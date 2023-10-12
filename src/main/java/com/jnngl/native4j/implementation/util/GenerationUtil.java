package com.jnngl.native4j.implementation.util;

public class GenerationUtil {

  public static String getGeneratedClassName(Class<?> cls) {
    return cls.getName().replace(".", "_") + "GeneratedImpl";
  }

  public static String getJniClassName(Class<?> cls) {
    return cls.getName().replace(".", "_1") + "GeneratedImpl";
  }
}
