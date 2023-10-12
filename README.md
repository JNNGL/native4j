### Идея

Удобная библиотека для вызова нативных функций, с генерацией прямых 
биндингов JNI в рантайме, с упором на высокую производительность самих вызовов.

### Минимальный пример

hello.c
```c
// gcc -shared -fPIC -O3 hello.c -o libhello.so
#include <stdio.h>

void print_message(const char* message) {
  printf("Message: %s\n", message);
}
```

#### Annotation-Based

HelloLibrary.java
```java
@LoadLibrary("hello")
public interface HelloLibrary extends NativeLibrary {

  @Prototype("void print_message(const char*)")
  void printMessage(String message);
}
```

Main.java
```java
public class Main {
  public static void main(String[] args) {
    HelloLibrary library = NativeLibrary.create(HelloLibrary.class);
    library.loadLibrary();
    library.printMessage("Hello, world!");
  }
}
```

#### Builder

SomeInterface.java (возможно из другой библиотеки)
```java
public interface SomeInterface {
  void printMessage(String str);
}
```

Main.java:
```java
public class Main() {
  public static void main(String[] args) {
    SomeInterface i = NativeLibrary.builder()
        .of(SomeInterface.class)
        .linkLibrary("hello")
        // В случае, если методов с одним названием 
        // несколько, использоавть дескриптор метода
        .withPrototype("printMessage", "void print_message(const char*)")
        .buildAndLoad();
    
    i.printMessage("Hello, world!");
  }
}
```

### TODO

----

 - Генерировать нативную связку напрямую в бинарном виде.
 - Builder для нативных библиотек (см. пример Builder)
 - Связки для более сложный типов (массивы, структуры<->pojo, коллбеки, указатели, etc)
   - Использовать для этих целей Critical и Unsafe?
 - Annotation Processor для генерации биндингов в compile-time.
 - Вызовы нативных функций через Panama на новых версиях.
 - Сделать библиотеку более умной
   - Научить ее анализировать хеадеры
     - Обнаружение тайпдефов/дефайнов типов
     - Автоматический поиск прототипов
   - Генерация оптимальной реализации учитывая контекст
 - Какой-либо интерфейс для возможности вмешаться в генерацию кода
   - Различные аннотации для проверок агрументов/выходного значения
   - Компилировать и встраивать код из аннотации?

### Текущая реализация и возможности

На данный момент байткод генерируется через библиотеку ObjectWebAsm, 
нативный код генерируется через GCC (позор), поддерживаются только примитивы.
</br>
Иными словами библиотека находится только на начальном этапе.