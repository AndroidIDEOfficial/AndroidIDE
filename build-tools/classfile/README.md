# Class File Reader

This is a basic class file reader which supports reading class files with major version **61 and below**. Currently, we only read required information from the class file (up to the `super_class` entry).

## Usage

Create a `ClassFileReader` instance and call the `read()` method to start reading the class file.

```kotlin
// Kotlin
val reader = ClassFileReader(FileInputStream(File("/path/to/MyClass.class"))) 
val classFile = reader.read()
```

## Constant pool

The `IClassFile` provides access to the constant pool that is read from the class file.

The entries in the constant pool from class file is represented by the `IConstant` implementations. Each implementation has a `type` property which represents the type of the constant. It also contains the tag value of the constant type. The following table shows the constant type and its implementation in this class file reader.

| Constant type               | Implementation         |
| --------------------------- | ---------------------- |
| CONSTANT_Class              | `ClassConstant`        |
| CONSTANT_Fieldref           | `ReferenceConstant`    |
| CONSTANT_Methodref          | `ReferenceConstant`    |
| CONSTANT_InterfaceMethodref | `ReferenceConstant`    |
| CONSTANT_String             | `StringConstant`       |
| CONSTANT_Integer            | `IntegerConstant`      |
| CONSTANT_Float              | `FloatConstant`        |
| CONSTANT_Double             | `DoubleConstant`       |
| CONSTANT_NameAndType        | `NameAndTypeConstant`  |
| CONSTANT_Utf8               | `Utf8Constant`         |
| CONSTANT_MethodHandle       | `MethodHandleConstant` |
| CONSTANT_MethodType         | `MethodTypeConstant`   |
| CONSTANT_Dynamic            | `DynamicConstant`      |
| CONSTANT_InvokeDynamic      | `DynamicConstant`      |
| CONSTANT_Module             | `ModuleConstant`       |
| CONSTANT_Package            | `PackageConstant`      |


## References

The following sections were referred from the JDK 17 JVM specifications :

- [Chapter 4 - The Class File Format](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html).
- [Table 5.4.3.5-A. Bytecode Behaviors for Method Handles](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-5.html#jvms-5.4.3.5).