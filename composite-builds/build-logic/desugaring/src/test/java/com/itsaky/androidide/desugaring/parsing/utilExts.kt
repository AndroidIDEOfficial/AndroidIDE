package com.itsaky.androidide.desugaring.parsing

internal inline fun <reified ExceptionT : Throwable> assertThrows(crossinline throwingAction: () -> Unit) {
  org.junit.Assert.assertThrows(ExceptionT::class.java) { throwingAction() }
}