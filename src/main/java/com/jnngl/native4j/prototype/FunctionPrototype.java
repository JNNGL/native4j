package com.jnngl.native4j.prototype;

import java.util.List;

public class FunctionPrototype<W, R> {

  private final String name;
  private final PrototypeTypeProcessor<W, R> output;
  private final List<PrototypeTypeProcessor<W, R>> input;

  public FunctionPrototype(String name, PrototypeTypeProcessor<W, R> output,
                           List<PrototypeTypeProcessor<W, R>> input) {
    this.name = name;
    this.output = output;
    this.input = input;
  }

  public String getName() {
    return name;
  }

  public PrototypeTypeProcessor<W, R> getOutput() {
    return output;
  }

  public List<PrototypeTypeProcessor<W, R>> getInput() {
    return input;
  }
}
