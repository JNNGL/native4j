package com.jnngl.native4j.implementation.binary;

import com.jnngl.native4j.NativeLibrary;
import com.jnngl.native4j.implementation.NativeLibraryImplementationBuilder;

import java.lang.reflect.Method;
import java.nio.file.Path;

public class BinaryNativeLibraryImplementationBuilder implements NativeLibraryImplementationBuilder {

  @Override
  public void processClass(Class<? extends NativeLibrary> cls) {
    // Stub.
  }

  @Override
  public void processMethod(Method method) {
    // Stub.
  }

  @Override
  public Path build() {
    throw new UnsupportedOperationException("stub");
  }
}
