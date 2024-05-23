package com.android.aaptcompiler

import com.android.aapt.Resources
import com.android.aaptcompiler.android.ResValue
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResourceUtilsTest {

  @Test
  fun testParseBool() {
    assertThat(parseAsBool("true")).isTrue()
    assertThat(parseAsBool("TRUE")).isTrue()
    assertThat(parseAsBool("True")).isTrue()

    assertThat(parseAsBool("false")).isFalse()
    assertThat(parseAsBool("FALSE")).isFalse()
    assertThat(parseAsBool("False")).isFalse()

    assertThat(parseAsBool(" False\n ")).isFalse()
  }

  @Test
  fun testParseResourceName() {
    var result = parseResourceName("android:color/foo")
    assertThat(result).isNotNull()
    assertThat(result?.resourceName)
      .isEqualTo(ResourceName("android", AaptResourceType.COLOR, "foo"))
    assertThat(result?.isPrivate).isFalse()

    result = parseResourceName("color/foo")
    assertThat(result).isNotNull()
    assertThat(result?.resourceName)
      .isEqualTo(ResourceName("", AaptResourceType.COLOR, "foo"))
    assertThat(result?.isPrivate).isFalse()

    result = parseResourceName("*android:string/foo")
    assertThat(result).isNotNull()
    assertThat(result?.resourceName)
      .isEqualTo(ResourceName("android", AaptResourceType.STRING, "foo"))
    assertThat(result?.isPrivate).isTrue()

    result = parseResourceName("")
    assertThat(result).isNull()
  }

  @Test
  fun testParseReferenceWithNoPackage() {
    val result = parseReference("@color/bar")
    assertThat(result).isNotNull()
    assertThat(result?.reference?.name)
      .isEqualTo(ResourceName("", AaptResourceType.COLOR, "bar"))
    assertThat(result?.createNew).isFalse()
    assertThat(result?.reference?.isPrivate).isFalse()
  }

  @Test
  fun testParseReferenceWithPackage() {
    val result = parseReference("@android:integer/foo")
    assertThat(result).isNotNull()
    assertThat(result?.reference?.name)
      .isEqualTo(ResourceName("android", AaptResourceType.INTEGER, "foo"))
    assertThat(result?.createNew).isFalse()
    assertThat(result?.reference?.isPrivate).isFalse()
  }

  @Test
  fun testParseReferenceWithSurroundingWhitespace() {
    val result = parseReference("\t @android:integer/foo\n \n\t")
    assertThat(result).isNotNull()
    assertThat(result?.reference?.name)
      .isEqualTo(ResourceName("android", AaptResourceType.INTEGER, "foo"))
    assertThat(result?.createNew).isFalse()
    assertThat(result?.reference?.isPrivate).isFalse()
  }

  @Test
  fun testParseAutoCreateIdReference() {
    val result = parseReference("@+android:id/foo")
    assertThat(result).isNotNull()
    assertThat(result?.reference?.name)
      .isEqualTo(ResourceName("android", AaptResourceType.ID, "foo"))
    assertThat(result?.createNew).isTrue()
    assertThat(result?.reference?.isPrivate).isFalse()
  }

  @Test
  fun testParsePrivateReference() {
    val result = parseReference("@*android:id/foo")
    assertThat(result).isNotNull()
    assertThat(result?.reference?.name)
      .isEqualTo(ResourceName("android", AaptResourceType.ID, "foo"))
    assertThat(result?.createNew).isFalse()
    assertThat(result?.reference?.isPrivate).isTrue()
  }

  @Test
  fun testFailToParseAutoCreateNonIdReference() {
    val result = parseReference("@+android:color/foo")
    assertThat(result).isNull()
  }

  @Test
  fun testParseAttributeReference() {
    assertThat(parseAttributeReference("?android")).isNotNull()
    assertThat(parseAttributeReference("?android:foo")).isNotNull()
    assertThat(parseAttributeReference("?attr/foo")).isNotNull()
    assertThat(parseAttributeReference("?android:attr/foo")).isNotNull()

    assertThat(parseAttributeReference("?style/foo")).isNull()
    assertThat(parseAttributeReference("?android:style/foo")).isNull()
    assertThat(parseAttributeReference("?android:")).isNull()
    assertThat(parseAttributeReference("?android:attr/")).isNull()
    assertThat(parseAttributeReference("?:attr/")).isNull()
    assertThat(parseAttributeReference("?:attr/foo")).isNull()
    assertThat(parseAttributeReference("?:/")).isNull()
    assertThat(parseAttributeReference("?:/foo")).isNull()
    assertThat(parseAttributeReference("?attr/")).isNull()
    assertThat(parseAttributeReference("?/foo")).isNull()
  }

  @Test
  fun testNullIsEmptyReference() {
    assertThat(makeNull()).isEqualTo(Reference())
    assertThat(tryParseNullOrEmpty("@null")).isEqualTo(Reference())
  }

  @Test
  fun testEmptyIsBinaryPrimitive() {
    assertThat(makeEmpty())
      .isEqualTo(BinaryPrimitive(ResValue(ResValue.DataType.NULL, ResValue.NullFormat.EMPTY)))
    assertThat(tryParseNullOrEmpty("@empty"))
      .isEqualTo(BinaryPrimitive(ResValue(ResValue.DataType.NULL, ResValue.NullFormat.EMPTY)))
  }

  @Test
  fun testItemsWithWhitespaceAreParsedCorrectly() {
    var result =
      tryParseItemForAttribute("  12\n   ", Resources.Attribute.FormatFlags.INTEGER_VALUE)
    assertThat(result).isNotNull()
    assertThat(result)
      .isEqualTo(BinaryPrimitive(ResValue(ResValue.DataType.INT_DEC, 12)))
    result =
      tryParseItemForAttribute("  true\n   ", Resources.Attribute.FormatFlags.BOOLEAN_VALUE)
    assertThat(result).isNotNull()
    assertThat(result)
      .isEqualTo(BinaryPrimitive(ResValue(ResValue.DataType.INT_BOOLEAN, -1)))

    val expectedFloat = 12.0f
    result =
      tryParseItemForAttribute("  12.0\n  ", Resources.Attribute.FormatFlags.FLOAT_VALUE)
    assertThat(result).isNotNull()
    assertThat(result)
      .isEqualTo(BinaryPrimitive(ResValue(ResValue.DataType.FLOAT, expectedFloat.toRawBits())))
  }
}