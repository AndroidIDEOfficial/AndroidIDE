package com.itsaky.androidide.testing.common

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A [TestRule] which only runs the tests in a CI environment.
 */
class CIOnlyTestRule(
  var isEnabled: Boolean = true
) : TestRule {

  override fun apply(base: Statement, description: Description?): Statement {
    if (!isEnabled) {
      return base
    }

    return object : Statement() {
      override fun evaluate() {
        if (isCi()) {
          base.evaluate()
        }
      }
    }
  }

  private fun isCi() = System.getenv("CI").toBoolean()
}