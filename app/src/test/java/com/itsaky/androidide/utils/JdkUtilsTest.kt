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

package com.itsaky.androidide.utils

import android.app.Application
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class JdkUtilsTest {

  companion object {

    const val JDK_17_PROPS =
      """
        Property settings:
    file.encoding = UTF-8
    file.separator = /
    java.class.path =
    java.class.version = 61.0
    java.home = /data/data/com.itsaky.androidide/files/usr/opt/openjdk-17.0
    java.io.tmpdir = /data/data/com.itsaky.androidide/files/usr/tmp/
    java.library.path = /data/data/com.itsaky.androidide/files/usr/java/packages/lib
        /data/data/com.itsaky.androidide/files/usr/lib
    java.runtime.name = OpenJDK Runtime Environment
    java.runtime.version = 17-internal+0-adhoc.root.src
    java.specification.name = Java Platform API Specification
    java.specification.vendor = Oracle Corporation
    java.specification.version = 17
    java.vendor = N/A
    java.vendor.url = https://openjdk.java.net/
    java.vendor.url.bug = https://bugreport.java.com/bugreport/
    java.version = 17-internal
    java.version.date = 2021-09-14
    java.vm.compressedOopsMode = 32-bit
    java.vm.info = mixed mode
    java.vm.name = OpenJDK 64-Bit Server VM
    java.vm.specification.name = Java Virtual Machine Specification
    java.vm.specification.vendor = Oracle Corporation
    java.vm.specification.version = 17
    java.vm.vendor = Oracle Corporation
    java.vm.version = 17-internal+0-adhoc.root.src
    jdk.debug = release
    line.separator = \n
    native.encoding = UTF-8
    os.arch = aarch64
    os.name = Linux
    os.version = 4.9.305-ððð
    path.separator = :
    sun.arch.data.model = 64
    sun.boot.library.path = /data/data/com.itsaky.androidide/files/usr/opt/openjdk-17.0/lib
    sun.cpu.endian = little
    sun.io.unicode.encoding = UnicodeLittle
    sun.java.launcher = SUN_STANDARD
    sun.jnu.encoding = UTF-8
    sun.management.compiler = HotSpot 64-Bit Tiered Compilers
    sun.stderr.encoding = UTF-8
    sun.stdout.encoding = UTF-8
    user.dir = /data/data/com.itsaky.androidide/files/home
    user.home = /data/data/com.itsaky.androidide/files/home
    user.language = en
    user.name = u0_a248

openjdk version "17-internal" 2021-09-14
OpenJDK Runtime Environment (build 17-internal+0-adhoc.root.src)
OpenJDK 64-Bit Server VM (build 17-internal+0-adhoc.root.src, mixed mode)
      """

    const val JDK_21_PROPS =
      """
        Property settings:
    file.encoding = UTF-8
    file.separator = /
    java.class.path =
    java.class.version = 65.0
    java.home = /data/data/com.itsaky.androidide/files/usr/opt/openjdk-21.0.1
    java.io.tmpdir = /data/data/com.itsaky.androidide/files/usr/tmp/
    java.library.path = /data/data/com.itsaky.androidide/files/usr/java/packages/lib
        /data/data/com.itsaky.androidide/files/usr/lib
    java.runtime.name = OpenJDK Runtime Environment
    java.runtime.version = 21.0.1-internal-adhoc.root.src
    java.specification.name = Java Platform API Specification
    java.specification.vendor = Oracle Corporation
    java.specification.version = 21
    java.vendor = N/A
    java.vendor.url = https://openjdk.org/
    java.vendor.url.bug = https://bugreport.java.com/bugreport/
    java.version = 21.0.1-internal
    java.version.date = 2023-10-17
    java.vm.compressedOopsMode = 32-bit
    java.vm.info = mixed mode
    java.vm.name = OpenJDK 64-Bit Server VM
    java.vm.specification.name = Java Virtual Machine Specification
    java.vm.specification.vendor = Oracle Corporation
    java.vm.specification.version = 21
    java.vm.vendor = Oracle Corporation
    java.vm.version = 21.0.1-internal-adhoc.root.src
    jdk.debug = release
    line.separator = \n
    native.encoding = UTF-8
    os.arch = aarch64
    os.name = Linux
    os.version = 4.9.305-ððð
    path.separator = :
    stderr.encoding = UTF-8
    stdout.encoding = UTF-8
    sun.arch.data.model = 64
    sun.boot.library.path = /data/data/com.itsaky.androidide/files/usr/opt/openjdk-21.0.1/lib
    sun.cpu.endian = little
    sun.io.unicode.encoding = UnicodeLittle
    sun.java.launcher = SUN_STANDARD
    sun.jnu.encoding = UTF-8
    sun.management.compiler = HotSpot 64-Bit Tiered Compilers
    user.dir = /data/data/com.itsaky.androidide/files/home
    user.home = /data/data/com.itsaky.androidide/files/home
    user.language = en
    user.name = u0_a248

openjdk version "21.0.1-internal" 2023-10-17
OpenJDK Runtime Environment (build 21.0.1-internal-adhoc.root.src)
OpenJDK 64-Bit Server VM (build 21.0.1-internal-adhoc.root.src, mixed mode)
      """

    const val JDK_PROPS_EMPTY = ""
    const val JDK_PROPS_INVALID = """
      abcdef = ghijk
      java.ver = 17-internal
      java.hme = /somewhere/
    """
  }

  @Test
  fun `test parsing java home for JDK 17`() {
    val dist = JdkUtils.readDistFromProps(JDK_17_PROPS)
    assertThat(dist).isNotNull()
    assertThat(dist!!.javaVersion).isEqualTo("17-internal")
    assertThat(dist.javaHome).isEqualTo(
      "/data/data/com.itsaky.androidide/files/usr/opt/openjdk-17.0"
    )
  }

  @Test
  fun `test parsing java home for JDK 21`() {
    val dist = JdkUtils.readDistFromProps(JDK_21_PROPS)
    assertThat(dist).isNotNull()
    assertThat(dist!!.javaVersion).isEqualTo("21.0.1-internal")
    assertThat(dist.javaHome).isEqualTo(
      "/data/data/com.itsaky.androidide/files/usr/opt/openjdk-21.0.1"
    )
  }

  @Test
  fun `test parsing invalid props`() {
    val dist = JdkUtils.readDistFromProps(JDK_PROPS_INVALID)
    assertThat(dist).isNull()
  }

  @Test
  fun `test parsing empty props`() {
    val dist = JdkUtils.readDistFromProps(JDK_PROPS_EMPTY)
    assertThat(dist).isNull()
  }
}