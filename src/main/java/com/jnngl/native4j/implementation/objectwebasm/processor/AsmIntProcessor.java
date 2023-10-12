package com.jnngl.native4j.implementation.objectwebasm.processor;

import com.jnngl.native4j.prototype.PrototypeTypeProcessor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmIntProcessor implements PrototypeTypeProcessor<MethodVisitor, Void> {

  @Override
  public Void processInput(MethodVisitor methodVisitor, int i) {
    return null;
  }

  @Override
  public void linkInput(MethodVisitor methodVisitor, Void unused, int i) {
    methodVisitor.visitVarInsn(Opcodes.ILOAD, i + 1);
  }

  @Override
  public Void processOutput(MethodVisitor methodVisitor) {
    return null;
  }

  @Override
  public void linkOutput(MethodVisitor methodVisitor, Void unused) {
    methodVisitor.visitInsn(Opcodes.IRETURN);
  }
}
