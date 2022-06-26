# Java Language server

Some classes are not included in the jar files. Instead, their actual source code is included. This
is because I had to modify them for Android.

# ClassBuilder

ClassBuilder is included in this module because it uses the `java.compiler` API (for building source
code) and I did not want to create a new library module for that single classs.