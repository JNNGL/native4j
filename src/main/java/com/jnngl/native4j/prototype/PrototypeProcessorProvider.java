package com.jnngl.native4j.prototype;

public interface PrototypeProcessorProvider<W, R> {

  PrototypeTypeProcessor<W, R> getProcessor(Class<?> j, String n);
}
