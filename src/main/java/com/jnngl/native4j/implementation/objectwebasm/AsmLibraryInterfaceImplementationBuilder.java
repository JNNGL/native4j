package com.jnngl.native4j.implementation.objectwebasm;

import com.jnngl.native4j.NativeLibrary;
import com.jnngl.native4j.annotation.LibraryContainer;
import com.jnngl.native4j.annotation.LoadLibrary;
import com.jnngl.native4j.annotation.Prototype;
import com.jnngl.native4j.implementation.util.ByteClassLoader;
import com.jnngl.native4j.implementation.util.GenerationUtil;
import com.jnngl.native4j.prototype.FunctionPrototype;
import com.jnngl.native4j.prototype.PrototypeParser;
import com.jnngl.native4j.implementation.LibraryInterfaceImplementationBuilder;
import com.jnngl.native4j.prototype.PrototypeProcessorProvider;
import com.jnngl.native4j.prototype.PrototypeTypeProcessor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;

public class AsmLibraryInterfaceImplementationBuilder implements LibraryInterfaceImplementationBuilder {

  private static final ByteClassLoader CLASS_LOADER = new ByteClassLoader();

  private final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
  private final PrototypeProcessorProvider<MethodVisitor, Void> processorProvider;
  private final PrototypeParser prototypeParser;
  private Class<?> interfaceClass;

  public AsmLibraryInterfaceImplementationBuilder(PrototypeProcessorProvider<MethodVisitor, Void> processorProvider,
                                                  PrototypeParser prototypeParser) {
    this.processorProvider = processorProvider;
    this.prototypeParser = prototypeParser;
  }

  @Override
  public void processClass(Class<? extends NativeLibrary> cls, Path glueLibrary) {
    interfaceClass = cls;
    classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
        GenerationUtil.getGeneratedClassName(cls), null, "java/lang/Object",
        new String[]{cls.getName().replace(".", "/")});

    MethodVisitor constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
    constructor.visitVarInsn(Opcodes.ALOAD, 0);
    constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    constructor.visitMaxs(0, 0);
    constructor.visitInsn(Opcodes.RETURN);

    MethodVisitor method = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "loadLibrary", "()V", null, null);

    LibraryContainer libraryContainer = cls.getDeclaredAnnotation(LibraryContainer.class);
    LoadLibrary loadLibrary = cls.getDeclaredAnnotation(LoadLibrary.class);

    LoadLibrary[] libraries;
    if (libraryContainer != null) {
      libraries = libraryContainer.value();
    } else if (loadLibrary != null) {
      libraries = new LoadLibrary[]{loadLibrary};
    } else {
      libraries = new LoadLibrary[]{};
    }

    for (LoadLibrary library : libraries) {
      method.visitLdcInsn(library.value());
      method.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "loadLibrary", "(Ljava/lang/String;)V", false);
    }

    method.visitLdcInsn(glueLibrary.toAbsolutePath().toString());
    method.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "load", "(Ljava/lang/String;)V", false);

    method.visitInsn(Opcodes.RETURN);
    method.visitMaxs(0, 0);
    method.visitEnd();
  }

  @Override
  public void processMethod(Method method) {
    String nativeDescriptor = Type.getMethodDescriptor(method);
    String nativeMethodName = "n_" + method.getName();
    classWriter.visitMethod(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_NATIVE, nativeMethodName, nativeDescriptor, null, null).visitEnd();

    String descriptor = Type.getMethodDescriptor(method);
    MethodVisitor visitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), descriptor, null, null);

    String rawPrototype = method.getDeclaredAnnotation(Prototype.class).value();
    FunctionPrototype<MethodVisitor, Void> prototype = prototypeParser.parseFunctionPrototype(method, rawPrototype, processorProvider);

    List<PrototypeTypeProcessor<MethodVisitor, Void>> processors = prototype.getInput();
    for (int i = 0; i < processors.size(); i++) {
      processors.get(i).processInput(visitor, i);
    }

    for (int i = 0; i < processors.size(); i++) {
      processors.get(i).linkInput(visitor, null, i);
    }

    visitor.visitMethodInsn(Opcodes.INVOKESTATIC, GenerationUtil.getGeneratedClassName(interfaceClass), nativeMethodName, nativeDescriptor, false);

    for (int i = 0; i < processors.size(); i++) {
      processors.get(i).cleanUpInput(visitor, null, i);
    }

    Void unused = prototype.getOutput().processOutput(visitor);
    prototype.getOutput().linkOutput(visitor, unused);

    visitor.visitMaxs(0, 0);
    visitor.visitEnd();
  }

  @Override
  public Class<?> build() {
    if (interfaceClass == null) {
      return null;
    }

    classWriter.visitEnd();

    String className = GenerationUtil.getGeneratedClassName(interfaceClass);
    return CLASS_LOADER.loadClass(className, classWriter.toByteArray());
  }
}
