package com.jnngl.native4j.implementation.gcc;

import com.jnngl.native4j.implementation.gcc.processor.GccJdouble2DoubleProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccJfloat2FloatProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccJlong2LongProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccJshort2ShortProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccVoidProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccJbyte2ByteProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccJint2IntProcessor;
import com.jnngl.native4j.implementation.gcc.processor.GccJstring2ConstCharProcessor;
import com.jnngl.native4j.prototype.HashMapPrototypeProcessorProvider;

public class GccPrototypeProcessorProvider extends HashMapPrototypeProcessorProvider<StringBuilder, Void> {

  public GccPrototypeProcessorProvider() {
    this.register(byte.class, "signed char", new GccJbyte2ByteProcessor())
        .register(short.class, "short", new GccJshort2ShortProcessor())
        .register(int.class, "int", new GccJint2IntProcessor())
        .register(long.class, "long long", new GccJlong2LongProcessor())
        .register(float.class, "float", new GccJfloat2FloatProcessor())
        .register(double.class, "double", new GccJdouble2DoubleProcessor())
        .register(String.class, "const char*", new GccJstring2ConstCharProcessor())
        .register(void.class, "void", new GccVoidProcessor());
  }
}
