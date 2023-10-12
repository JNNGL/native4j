package com.jnngl.native4j.implementation.gcc.processor;

import com.jnngl.native4j.prototype.PrototypeTypeProcessor;

public class GccJstring2ConstCharProcessor implements PrototypeTypeProcessor<StringBuilder, Void> {

  @Override
  public Void processInput(StringBuilder builder, int i) {
    builder.append("const char* cstr").append(i).append(" = (*env)->GetStringUTFChars(env, arg").append(i).append(", 0);\n");
    return null;
  }

  @Override
  public void linkInput(StringBuilder builder, Void unused, int i) {
    builder.append("cstr").append(i);
  }

  @Override
  public void cleanUpInput(StringBuilder builder, Void unused, int i) {
    builder.append("(*env)->ReleaseStringUTFChars(env, arg").append(i).append(", cstr").append(i).append(");\n");
  }

  @Override
  public Void processOutput(StringBuilder builder) {
    return null;
  }

  @Override
  public void linkOutput(StringBuilder builder, Void unused) {
    builder.append("return (*env)->NewStringUTF(env, retval);\n");
  }

  @Override
  public String getNativeType() {
    return "const char*";
  }

  @Override
  public String getJniType() {
    return "jstring";
  }
}
