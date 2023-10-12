package com.jnngl.native4j.prototype;

public interface PrototypeTypeProcessor<W, R> {

  R processInput(W w, int i);

  void linkInput(W w, R r, int i);

  default void cleanUpInput(W w, R r, int i) {
  }

  R processOutput(W w);

  void linkOutput(W w, R r);

  default String getNativeType() {
    return null;
  }

  default String getJniType() {
    return null;
  }
}
