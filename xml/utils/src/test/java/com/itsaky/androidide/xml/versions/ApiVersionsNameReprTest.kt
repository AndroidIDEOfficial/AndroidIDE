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

package com.itsaky.androidide.xml.versions

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.xml.internal.versions.DefaultApiVersions
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class ApiVersionsNameReprTest {

  private fun someVersion() = ApiVersion(1)

  @Test
  fun `test class name flattening`() {
    val versions = DefaultApiVersions()
    versions.putClass("pck/outer", someVersion())
    versions.putClass("pck/pck/outer", someVersion())
    versions.putClass("pck/outer\$inner", someVersion())
    versions.putClass("pck/pck/outer\$inner", someVersion())

    assertThat(versions.classInfo("pck.outer")).isNotNull()
    assertThat(versions.classInfo("pck.pck.outer")).isNotNull()
    assertThat(versions.classInfo("pck/outer")).isNotNull()
    assertThat(versions.classInfo("pck/pck/outer")).isNotNull()
    assertThat(versions.classInfo("pck.outer\$inner")).isNotNull()
    assertThat(versions.classInfo("pck.pck.outer\$inner")).isNotNull()
    assertThat(versions.classInfo("pck/outer\$inner")).isNotNull()
    assertThat(versions.classInfo("pck/pck/outer\$inner")).isNotNull()

    assertThat(versions.classInfo("pck.outer.inner")).isNull()
    assertThat(versions.classInfo("pck.pck.outer.inner")).isNull()
    assertThat(versions.classInfo("pck/outer/inner")).isNull()
    assertThat(versions.classInfo("pck/pck/outer/inner")).isNull()
  }

  @Test
  fun `test method queries with method identifiers`() {
    val versions = DefaultApiVersions()
    versions.putMember("pck/outer", "some(I)", someVersion())
    versions.putMember("pck/outer", "some(II)", someVersion())
    versions.putMember("pck/outer", "some(Ljava/lang/String;)", someVersion())
    versions.putMember("pck/outer", "some(ILjava/lang/String;)", someVersion())
    versions.putMember("pck/outer", "some(Ljava/lang/String;I)", someVersion())
    versions.putMember("pck/outer", "some(Lpck/outer\$inner;I)", someVersion())

    assertThat(versions.memberInfo("pck/outer", "some(I)")).isNotNull()
    assertThat(versions.memberInfo("pck/outer", "some(II)")).isNotNull()
    assertThat(versions.memberInfo("pck/outer", "some(Ljava/lang/String;)")).isNotNull()
    assertThat(versions.memberInfo("pck/outer", "some(ILjava/lang/String;)")).isNotNull()
    assertThat(versions.memberInfo("pck/outer", "some(Ljava/lang/String;I)")).isNotNull()

    assertThat(versions.memberInfo("pck/outer", "some(F)")).isNull()
    assertThat(versions.memberInfo("pck/outer", "some(III)")).isNull()
    assertThat(versions.memberInfo("pck/outer", "some(IILjava/lang/String;)")).isNull()
    assertThat(versions.memberInfo("pck/outer", "some(FLjava/lang/String;)")).isNull()
    assertThat(versions.memberInfo("pck/outer", "some(BLjava/lang/String;I)")).isNull()
  }
}
