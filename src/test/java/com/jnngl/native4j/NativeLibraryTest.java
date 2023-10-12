package com.jnngl.native4j;

import org.junit.jupiter.api.Test;

public class NativeLibraryTest {

  @Test
  public void helloLibraryTest() throws ReflectiveOperationException {
    HelloLibrary library = NativeLibrary.create(HelloLibrary.class);
    library.loadLibrary();
    library.printMessage("Hello, world!");
  }
}
