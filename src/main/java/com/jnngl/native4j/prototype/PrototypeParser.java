package com.jnngl.native4j.prototype;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PrototypeParser {

  public <W, R> FunctionPrototype<W, R> parseFunctionPrototype(
      Method method, String prototype, PrototypeProcessorProvider<W, R> processorProvider) {
    String[] data = prototype.split("\\)", -1)[0].split("\\(", -1);
    data[1] = data[1].trim();
    String[] returnTypeAndName = data[0].split(" ");
    String name = returnTypeAndName[returnTypeAndName.length - 1];

    StringBuilder returnTypeBuilder = new StringBuilder();
    for (int i = 0; i < returnTypeAndName.length - 1; i++) {
      String part = returnTypeAndName[i];
      if (part.isEmpty()) {
        continue;
      }

      returnTypeBuilder.append(part).append(" ");
    }

    int index = name.lastIndexOf("*");
    if (index >= 0) {
      name = name.substring(index + 1);
      returnTypeBuilder.append(new String(new char[index + 1]).replace("\0", "*"));
    }

    String returnType = returnTypeBuilder.toString().trim();
    PrototypeTypeProcessor<W, R> returnTypeProcessor =
        processorProvider.getProcessor(method.getReturnType(), returnType);

    String[] parameterTypes = data[1].isEmpty() ? new String[0] : data[1].split(",");
    Class<?>[] methodParameterTypes = method.getParameterTypes();
    if (parameterTypes.length != methodParameterTypes.length) {
      throw new IllegalArgumentException("Prototype parameter types length does not match method parameters length");
    }

    List<PrototypeTypeProcessor<W, R>> processors = new ArrayList<>(parameterTypes.length);
    for (int i = 0; i < parameterTypes.length; i++) {
      String nativeType = parameterTypes[i].replaceAll("\\s+", " ").replace(" *", "*").trim();
      processors.add(processorProvider.getProcessor(methodParameterTypes[i], nativeType));
    }

    return new FunctionPrototype<>(name, returnTypeProcessor, processors);
  }
}
