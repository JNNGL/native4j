package com.jnngl.native4j.implementation.util;

public class ByteClassLoader extends ClassLoader {

  public Class<?> loadClass(String name, byte[] data) {
    Class<?> cls = defineClass(name, data, 0, data.length);
    resolveClass(cls);
    return cls;
  }
}
