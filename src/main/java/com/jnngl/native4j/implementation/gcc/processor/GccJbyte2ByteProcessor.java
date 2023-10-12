package com.jnngl.native4j.implementation.gcc.processor;

import com.jnngl.native4j.prototype.PrototypeTypeProcessor;

public class GccJbyte2ByteProcessor implements PrototypeTypeProcessor<StringBuilder, Void> {

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
    builder.append("return (jbyte) retval;\n");
  }

  @Override
  public String getNativeType() {
    return "signed char";
  }

  @Override
  public String getJniType() {
    return "jbyte";
  }
}
