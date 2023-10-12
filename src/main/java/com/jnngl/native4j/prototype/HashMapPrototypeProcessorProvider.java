package com.jnngl.native4j.prototype;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class HashMapPrototypeProcessorProvider<W, R> implements PrototypeProcessorProvider<W, R> {

  private static final class PrototypeProcessorKey {

    private final Class<?> cls;
    private final String nativeType;

    public PrototypeProcessorKey(Class<?> cls, String nativeType) {
      this.cls = cls;
      this.nativeType = nativeType;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      PrototypeProcessorKey that = (PrototypeProcessorKey) o;
      return Objects.equals(cls, that.cls) && Objects.equals(nativeType, that.nativeType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(cls, nativeType);
    }
  }

  private final Map<PrototypeProcessorKey, Supplier<PrototypeTypeProcessor<W, R>>> processorMap = new HashMap<>();

  @Override
  public PrototypeTypeProcessor<W, R> getProcessor(Class<?> j, String n) {
    return processorMap.get(new PrototypeProcessorKey(j, n)).get();
  }

  public HashMapPrototypeProcessorProvider<W, R> register(
      Class<?> cls, String nativeType, Supplier<PrototypeTypeProcessor<W, R>> supplier) {
    processorMap.put(new PrototypeProcessorKey(cls, nativeType), supplier);
    return this;
  }

  public HashMapPrototypeProcessorProvider<W, R> register(
      Class<?> cls, String nativeType, PrototypeTypeProcessor<W, R> processor) {
    return register(cls, nativeType, () -> processor);
  }
}
