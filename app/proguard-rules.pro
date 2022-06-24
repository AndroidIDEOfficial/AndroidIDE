-ignorewarnings

-dontwarn **
-dontnote **
-dontobfuscate

# keep javac classes
-keep class com.sun.** { *; }

# Android builder model interfaces
-keep class com.android.** { *; }

# Tooling API classes
-keep class com.itsaky.androidide.tooling.** { *; }

# Builder model implementations
-keep class com.itsaky.androidide.builder.model.** { *; }

# JSONRpc
-keep class org.eclipse.lsp4j.** { *; }