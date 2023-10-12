package com.jnngl.native4j.implementation;

import com.jnngl.native4j.NativeLibrary;

import java.lang.reflect.Method;
import java.nio.file.Path;

public interface NativeLibraryImplementationBuilder {

  void processClass(Class<? extends NativeLibrary> cls);

  void processMethod(Method method);

  Path build();
}
