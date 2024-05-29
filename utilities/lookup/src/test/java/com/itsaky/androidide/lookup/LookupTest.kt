/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.lookup

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lookup.Lookup.Key
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class LookupTest {

  @Test
  fun `test with class`() {
    val lookup = Lookup.getDefault()

    // Test register
    var service = TestServiceImpl()
    lookup.register(TestService::class.java, service)
    lookup.lookup(TestService::class.java).apply {
      assertThat(this).isNotNull()
      assertThat(this).isEqualTo(service)
    }

    // Test update
    service = TestServiceImpl()
    lookup.update(TestService::class.java, service)
    lookup.lookup(TestService::class.java).apply {
      assertThat(this).isNotNull()
      assertThat(this).isEqualTo(service)
    }

    lookup.unregister(TestService::class.java)

    lookup.lookup(TestService::class.java).apply { assertThat(this).isNull() }
  }

  @Test
  fun `test with key`() {
    val lookup = Lookup.getDefault()
    val key = Key<TestService>()

    // Test register
    var service = TestServiceImpl()
    lookup.register(key, service)
    lookup.lookup(key).apply {
      assertThat(this).isNotNull()
      assertThat(this).isEqualTo(service)
    }
  
    // Service not registered with the Class object. Must return null.
    lookup.lookup(TestService::class.java).apply { assertThat(this).isNull() }

    // Test update
    service = TestServiceImpl()
    lookup.update(key, service)
    lookup.lookup(key).apply {
      assertThat(this).isNotNull()
      assertThat(this).isEqualTo(service)
    }

    // Service not registered with the Class object. Must return null.
    lookup.lookup(TestService::class.java).apply { assertThat(this).isNull() }

    lookup.unregister(key)

    lookup.lookup(key).apply { assertThat(this).isNull() }
  }

  interface TestService {
    fun doTest()
  }

  class TestServiceImpl : TestService {
    override fun doTest() {
      println("From test service")
    }
  }
}
