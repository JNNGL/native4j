package com.jnngl.native4j.implementation.objectwebasm;

import com.jnngl.native4j.implementation.objectwebasm.processor.AsmDoubleProcessor;
import com.jnngl.native4j.implementation.objectwebasm.processor.AsmFloatProcessor;
import com.jnngl.native4j.implementation.objectwebasm.processor.AsmIntProcessor;
import com.jnngl.native4j.implementation.objectwebasm.processor.AsmLongProcessor;
import com.jnngl.native4j.implementation.objectwebasm.processor.AsmString2ConstCharProcessor;
import com.jnngl.native4j.implementation.objectwebasm.processor.AsmVoidProcessor;
import com.jnngl.native4j.prototype.HashMapPrototypeProcessorProvider;

import org.objectweb.asm.MethodVisitor;

public class AsmPrototypeProcessorProvider extends HashMapPrototypeProcessorProvider<MethodVisitor, Void> {

  public AsmPrototypeProcessorProvider() {
    this.register(int.class, "int", new AsmIntProcessor())
        .register(byte.class, "byte", new AsmIntProcessor())
        .register(short.class, "short", new AsmIntProcessor())
        .register(boolean.class, "bool", new AsmIntProcessor())
        .register(char.class, "char", new AsmIntProcessor())
        .register(float.class, "float", new AsmFloatProcessor())
        .register(long.class, "long", new AsmLongProcessor())
        .register(double.class, "double", new AsmDoubleProcessor())
        .register(void.class, "void", new AsmVoidProcessor())
        .register(String.class, "const char*", new AsmString2ConstCharProcessor());
  }
}
