package com.jnngl.native4j;

import com.jnngl.native4j.annotation.LoadLibrary;
import com.jnngl.native4j.annotation.Prototype;

@LoadLibrary("hello")
public interface HelloLibrary extends NativeLibrary {

  @Prototype("void print_message(const char*)")
  void printMessage(String message);
}
