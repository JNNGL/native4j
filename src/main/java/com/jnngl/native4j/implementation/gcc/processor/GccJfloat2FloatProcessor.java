package com.jnngl.native4j.implementation.gcc.processor;

import com.jnngl.native4j.prototype.PrototypeTypeProcessor;

public class GccJfloat2FloatProcessor implements PrototypeTypeProcessor<StringBuilder, Void> {

  @Override
  public Void processInput(StringBuilder builder, int i) {
    return null;
  }

  @Override
  public void linkInput(StringBuilder builder, Void unused, int i) {
    builder.append("arg").append(i);
  }

  @Override
  public Void processOutput(StringBuilder builder) {
    return null;
  }

  @Override
  public void linkOutput(StringBuilder builder, Void unused) {
    builder.append("return (jfloat) retval;\n");
  }

  @Override
  public String getNativeType() {
    return "float";
  }

  @Override
  public String getJniType() {
    return "jfloat";
  }
}
