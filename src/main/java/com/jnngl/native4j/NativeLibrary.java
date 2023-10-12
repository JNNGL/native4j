package com.jnngl.native4j;

import com.jnngl.native4j.implementation.gcc.GccNativeLibraryImplementationBuilder;
import com.jnngl.native4j.implementation.gcc.GccPrototypeProcessorProvider;
import com.jnngl.native4j.implementation.objectwebasm.AsmLibraryInterfaceImplementationBuilder;
import com.jnngl.native4j.implementation.objectwebasm.AsmPrototypeProcessorProvider;
import com.jnngl.native4j.prototype.PrototypeParser;

import java.lang.reflect.Method;

public interface NativeLibrary {

  void loadLibrary();

  static <T extends NativeLibrary> T create(Class<T> cls) throws ReflectiveOperationException {
    PrototypeParser prototypeParser = new PrototypeParser();

    GccPrototypeProcessorProvider nativeProcessorProvider = new GccPrototypeProcessorProvider();
    GccNativeLibraryImplementationBuilder nativeBuilder = new GccNativeLibraryImplementationBuilder(nativeProcessorProvider, prototypeParser);
    nativeBuilder.processClass(cls);
    for (Method method : cls.getDeclaredMethods()) {
      nativeBuilder.processMethod(method);
    }

    AsmPrototypeProcessorProvider processorProvider = new AsmPrototypeProcessorProvider();
    AsmLibraryInterfaceImplementationBuilder builder = new AsmLibraryInterfaceImplementationBuilder(processorProvider, prototypeParser);
    builder.processClass(cls, nativeBuilder.build());
    for (Method method : cls.getDeclaredMethods()) {
      builder.processMethod(method);
    }

    return (T) builder.build().getConstructor().newInstance();
  }
}
