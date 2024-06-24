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
import com.itsaky.androidide.xml.findAndroidHome
import jaxp.xml.stream.XMLStreamException
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.ByteArrayInputStream
import java.io.File

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
class ApiVersionParserTest {

  val apiVersionXml by lazy {
    val platformsDir = File(findAndroidHome(), "platforms")
    val platformDir = platformsDir.listFiles()!!.asSequence().sortedByDescending {
      // android-30, android-31, android-32, android-33, etc
      it.name.substring("android-".length).toIntOrNull() ?: 0
    }.first()

    platformDir.resolve("data/api-versions.xml")
  }

  private lateinit var parser: CollectingApiVersionsParser

  @Before
  fun setup() {
    parser = CollectingApiVersionsParser()
  }

  @Test
  fun `test basic parse`() {
    parser.parse(apiVersionXml.inputStream().buffered())

    val manifestInfo = parser.getClassInfo("android/Manifest")
    assertThat(manifestInfo).isNotNull()
    assertThat(manifestInfo?.since).isEqualTo(1)
    assertThat(manifestInfo?.deprecatedIn).isEqualTo(0)
    assertThat(manifestInfo?.removedIn).isEqualTo(0)
  }

  @Test
  fun testSingleClassParsing() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    parser.parse(inputStream)

    val classInfo = parser.getClassInfo("com.example.ClassA")
    assertThat(classInfo).isNotNull()
    assertThat(classInfo!!.since).isEqualTo(1)
    assertThat(classInfo.deprecatedIn).isEqualTo(0)
    assertThat(classInfo.removedIn).isEqualTo(0)
  }

  @Test
  fun testSingleClassWithMembersParsing() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0">
                    <field name="fieldA" since="2" deprecated="0" removed="0"/>
                    <method name="methodA" since="3" deprecated="0" removed="0"/>
                </class>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    parser.parse(inputStream)

    val classInfo = parser.getClassInfo("com.example.ClassA")
    assertThat(classInfo).isNotNull()
    assertThat(classInfo!!.since).isEqualTo(1)

    val fieldInfo = parser.getMemberInfo("com.example.ClassA", "fieldA")
    assertThat(fieldInfo).isNotNull()
    assertThat(fieldInfo!!.since).isEqualTo(2)

    val methodInfo = parser.getMemberInfo("com.example.ClassA", "methodA")
    assertThat(methodInfo).isNotNull()
    assertThat(methodInfo!!.since).isEqualTo(3)
  }

  @Test
  fun testMultipleClassesParsing() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0"/>
                <class name="com.example.ClassB" since="2" deprecated="0" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    parser.parse(inputStream)

    val classAInfo = parser.getClassInfo("com.example.ClassA")
    assertThat(classAInfo).isNotNull()
    assertThat(classAInfo!!.since).isEqualTo(1)

    val classBInfo = parser.getClassInfo("com.example.ClassB")
    assertThat(classBInfo).isNotNull()
    assertThat(classBInfo!!.since).isEqualTo(2)
  }

  @Test
  fun testClassWithDeprecatedAndRemovedAttributes() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="30" removed="34"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    parser.parse(inputStream)

    val classInfo = parser.getClassInfo("com.example.ClassA")
    assertThat(classInfo).isNotNull()
    assertThat(classInfo!!.since).isEqualTo(1)
    assertThat(classInfo.deprecatedIn).isEqualTo(30)
    assertThat(classInfo.removedIn).isEqualTo(34)
  }

  @Test
  fun testClassWithInvalidAttributes() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="256" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    try {
      parser.parse(inputStream)
      assertThat(true).isFalse() // Should not reach here
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessageThat().contains("Invalid version")
    }
  }

  @Test
  fun testMissingNameAttribute() {
    val xml = """
            <api version="34">
                <class since="1" deprecated="0" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    try {
      parser.parse(inputStream)
      assertThat(true).isFalse() // Should not reach here
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessageThat().contains("Missing name attribute")
    }
  }

  @Test
  fun testDuplicateClassEntry() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0"/>
                <class name="com.example.ClassA" since="2" deprecated="0" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    try {
      parser.parse(inputStream)
      assertThat(true).isFalse() // Should not reach here
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessageThat().contains("Duplicate class entry")
    }
  }

  @Test
  fun testDuplicateMemberEntry() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0">
                    <field name="fieldA" since="2" deprecated="0" removed="0"/>
                    <field name="fieldA" since="3" deprecated="0" removed="0"/>
                </class>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    try {
      parser.parse(inputStream)
      assertThat(true).isFalse() // Should not reach here
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessageThat().contains("Duplicate field entry in class com.example.ClassA")
    }
  }

  @Test
  fun testMultipleApiElements() {
    val xml = """
            <api version="33">
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0"/>
            </api>
            <api version="34">
                <class name="com.example.ClassB" since="2" deprecated="0" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())

    val parser = CollectingApiVersionsParser()
    assertThrows(
      "The markup in the document following the root element must be well-formed.",
      XMLStreamException::class.java
    ) {
      parser.parse(inputStream)
    }
  }

  @Test
  fun testNestedClassElements() {
    val xml = """
            <api version="34">
                <class name="com.example.OuterClass" since="1" deprecated="0" removed="0">
                    <class name="com.example.OuterClass.InnerClass" since="2" deprecated="0" removed="0"/>
                </class>
            </api>
        """.trimIndent()

    val inputStream = ByteArrayInputStream(xml.toByteArray())

    assertThrows("<class> elements cannot be nested", IllegalStateException::class.java) {
      parser.parse(inputStream)
    }
  }

  @Test
  fun testBoundaryAttributeValues() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="255" removed="0"/>
                <class name="com.example.ClassB" since="1" deprecated="0" removed="255"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())
    parser.parse(inputStream)

    val classAInfo = parser.getClassInfo("com.example.ClassA")
    assertThat(classAInfo).isNotNull()
    assertThat(classAInfo!!.deprecatedIn).isEqualTo(255)

    val classBInfo = parser.getClassInfo("com.example.ClassB")
    assertThat(classBInfo).isNotNull()
    assertThat(classBInfo!!.removedIn).isEqualTo(255)
  }

  @Test
  fun testOutOfBoundaryAttributeValues() {
    val xml = """
            <api version="34">
                <class name="com.example.ClassA" since="1" deprecated="256" removed="0"/>
                <class name="com.example.ClassB" since="1" deprecated="0" removed="256"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())
    assertThrows("Invalid version: 1, 256, 0", IllegalStateException::class.java) {
      parser.parse(inputStream)
    }
  }

  @Test
  fun testUnexpectedElements() {
    val xml = """
            <api version="34">
                <unexpectedElement name="unexpected" since="1" deprecated="0" removed="0"/>
                <class name="com.example.ClassA" since="1" deprecated="0" removed="0"/>
            </api>
        """.trimIndent()
    val inputStream = ByteArrayInputStream(xml.toByteArray())
    parser.parse(inputStream)

    val classInfo = parser.getClassInfo("com.example.ClassA")
    assertThat(classInfo).isNotNull()
    assertThat(classInfo!!.since).isEqualTo(1)
  }

  @Test
  fun testEmptyXmlFile() {
    val xml = ""
    val inputStream = ByteArrayInputStream(xml.toByteArray())
    parser.parse(inputStream)
  }

  @Test
  fun testLargeInputFile() {
    val xmlBuilder = StringBuilder()
    xmlBuilder.append("<api version=\"34\">")
    for (i in 1..1000) {
      xmlBuilder.append("<class name=\"com.example.Class$i\" since=\"${(i % 254) + 1}\" />")
    }
    xmlBuilder.append("</api>")
    val inputStream = ByteArrayInputStream(xmlBuilder.toString().toByteArray())

    val parser = CollectingApiVersionsParser()
    parser.parse(inputStream)

    for (i in 1..1000) {
      val classInfo = parser.getClassInfo("com.example.Class$i")
      assertThat(classInfo).isNotNull()
      assertThat(classInfo!!.since).isEqualTo((i % 254) + 1)
      assertThat(classInfo.deprecatedIn).isEqualTo(0)
      assertThat(classInfo.removedIn).isEqualTo(0)
    }
  }
}