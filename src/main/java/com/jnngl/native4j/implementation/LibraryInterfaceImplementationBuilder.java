package com.jnngl.native4j.implementation;

import com.jnngl.native4j.NativeLibrary;

import java.lang.reflect.Method;
import java.nio.file.Path;

public interface LibraryInterfaceImplementationBuilder {

  void processClass(Class<? extends NativeLibrary> cls, Path glueLibrary);

  void processMethod(Method method);

  Class<?> build();
}
