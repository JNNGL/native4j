package com.jnngl.native4j.implementation.gcc.processor;

import com.jnngl.native4j.prototype.PrototypeTypeProcessor;

public class GccVoidProcessor implements PrototypeTypeProcessor<StringBuilder, Void> {

  @Override
  public Void processInput(StringBuilder builder, int i) {
    throw new IllegalStateException();
  }

  @Override
  public void linkInput(StringBuilder builder, Void unused, int i) {
    throw new IllegalStateException();
  }

  @Override
  public Void processOutput(StringBuilder builder) {
    return null;
  }

  @Override
  public void linkOutput(StringBuilder builder, Void unused) {

  }

  @Override
  public String getNativeType() {
    return "void";
  }

  @Override
  public String getJniType() {
    return "void";
  }
}
