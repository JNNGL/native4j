package com.jnngl.native4j.implementation.objectwebasm.processor;

import com.jnngl.native4j.prototype.PrototypeTypeProcessor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmVoidProcessor implements PrototypeTypeProcessor<MethodVisitor, Void> {

  @Override
  public Void processInput(MethodVisitor methodVisitor, int i) {
    throw new IllegalStateException();
  }

  @Override
  public void linkInput(MethodVisitor methodVisitor, Void unused, int i) {
    throw new IllegalStateException();
  }

  @Override
  public Void processOutput(MethodVisitor methodVisitor) {
    return null;
  }

  @Override
  public void linkOutput(MethodVisitor methodVisitor, Void unused) {
    methodVisitor.visitInsn(Opcodes.RETURN);
  }
}
