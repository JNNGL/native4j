package com.jnngl.native4j.implementation.gcc;

import com.jnngl.native4j.implementation.util.GenerationUtil;
import com.jnngl.native4j.NativeLibrary;
import com.jnngl.native4j.annotation.LibraryContainer;
import com.jnngl.native4j.annotation.LoadLibrary;
import com.jnngl.native4j.annotation.Prototype;
import com.jnngl.native4j.implementation.NativeLibraryImplementationBuilder;
import com.jnngl.native4j.prototype.FunctionPrototype;
import com.jnngl.native4j.prototype.PrototypeParser;
import com.jnngl.native4j.prototype.PrototypeProcessorProvider;
import com.jnngl.native4j.prototype.PrototypeTypeProcessor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Mainly for development / testing purposes.
 */
public class GccNativeLibraryImplementationBuilder implements NativeLibraryImplementationBuilder {

  private static final Path TEMP_PATH;

  static {
    try {
      TEMP_PATH = Files.createTempDirectory("native4j-gcc-generated");
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private final StringBuilder builder = new StringBuilder();

  private final PrototypeProcessorProvider<StringBuilder, Void> processorProvider;
  private final PrototypeParser prototypeParser;
  private Class<?> interfaceClass;
  private List<String> libraries;

  public GccNativeLibraryImplementationBuilder(PrototypeProcessorProvider<StringBuilder, Void> processorProvider,
                                               PrototypeParser prototypeParser) {
    this.processorProvider = processorProvider;
    this.prototypeParser = prototypeParser;
  }

  @Override
  public void processClass(Class<? extends NativeLibrary> cls) {
    this.interfaceClass = cls;
    builder.append("#include <jni.h>\n");

    LibraryContainer libraryContainer = cls.getDeclaredAnnotation(LibraryContainer.class);
    LoadLibrary loadLibrary = cls.getDeclaredAnnotation(LoadLibrary.class);

    LoadLibrary[] libraries;
    if (libraryContainer != null) {
      libraries = libraryContainer.value();
    } else if (loadLibrary != null) {
      libraries = new LoadLibrary[]{loadLibrary};
    } else {
      libraries = new LoadLibrary[]{};
    }

    this.libraries = Arrays.stream(libraries).map(LoadLibrary::value).collect(Collectors.toList());
  }

  @Override
  public void processMethod(Method method) {
    String rawPrototype = method.getDeclaredAnnotation(Prototype.class).value();
    FunctionPrototype<StringBuilder, Void> prototype = prototypeParser.parseFunctionPrototype(method, rawPrototype, processorProvider);

    List<PrototypeTypeProcessor<StringBuilder, Void>> processors = prototype.getInput();

    String outputNativeType = prototype.getOutput().getNativeType();
    builder.append("extern ").append(outputNativeType)
        .append(" ").append(prototype.getName()).append("(")
        .append(IntStream.range(0, processors.size())
            .mapToObj(i -> processors.get(i).getNativeType() + " arg" + i)
            .collect(Collectors.joining(", ")))
        .append(");\n");

    builder.append("JNIEXPORT ").append(prototype.getOutput().getJniType())
        .append(" JNICALL Java_").append(GenerationUtil.getJniClassName(interfaceClass))
        .append("_n_1").append(method.getName()).append("(JNIEnv* env, jclass cls");
    if (processors.size() > 0) {
      builder.append(", ")
        .append(IntStream.range(0, processors.size())
          .mapToObj(i -> processors.get(i).getJniType() + " arg" + i)
          .collect(Collectors.joining(", ")));
    }
    builder.append(") {\n");

    for (int i = 0; i < processors.size(); i++) {
      processors.get(i).processInput(builder, i);
    }

    if (!outputNativeType.equals("void")) {
      builder.append(outputNativeType).append(" retval = ");
    }
    builder.append(prototype.getName()).append("(");
    for (int i = 0; i < processors.size(); i++) {
      processors.get(i).linkInput(builder, null, i);
      if (i < processors.size() - 1) {
        builder.append(", ");
      }
    }
    builder.append(");\n");

    for (int i = 0; i < processors.size(); i++) {
      processors.get(i).cleanUpInput(builder, null, i);
    }

    Void unused = prototype.getOutput().processOutput(builder);
    prototype.getOutput().linkOutput(builder, unused);

    builder.append("}\n");
  }

  @Override
  public Path build() {
    try {
      String filename = "glue" + System.currentTimeMillis() + "_" + System.nanoTime();
      Files.write(TEMP_PATH.resolve(filename + ".c"), builder.toString().getBytes(StandardCharsets.UTF_8));
      ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "gcc -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -I${JAVA_HOME}/include/darwin -O3 -fPIC -shared " + filename + ".c -o " + filename + ".so -L${USERDIR} " + libraries.stream().map(l -> "-l" + l).collect(Collectors.joining(" ")));
      processBuilder.environment().put("JAVA_HOME", System.getProperty("java.home"));
      processBuilder.environment().put("USERDIR", System.getProperty("user.dir"));
      processBuilder.inheritIO();
      processBuilder.directory(TEMP_PATH.toFile());
      processBuilder.start().waitFor(); // TODO: Write native library directly in binary form
      return TEMP_PATH.resolve(filename + ".so");
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
