package com.android.aaptcompiler

import com.android.aaptcompiler.android.ResStringPool
import com.android.aaptcompiler.buffer.BigBuffer
import com.google.common.truth.Truth
import org.junit.Assert.assertThrows
import org.junit.Ignore
import org.junit.Test
import java.nio.ByteBuffer

class StringPoolTest {

  @Test
  fun testInsertOneString() {
    val pool = StringPool()

    val ref = pool.makeRef("wut")
    Truth.assertThat(ref.value()).isEqualTo("wut")
  }

  @Test
  fun testInsertTwoStrings() {
    val pool = StringPool()

    val ref1 = pool.makeRef("wut")
    val ref2 = pool.makeRef("hey")

    Truth.assertThat(ref1.value()).isEqualTo("wut")
    Truth.assertThat(ref2.value()).isEqualTo("hey")
  }

  @Test
  fun testInsertDuplicateStrings() {
    val pool = StringPool()

    val ref1 = pool.makeRef("wut")
    val ref2 = pool.makeRef("wut")

    Truth.assertThat(ref1.value()).isEqualTo("wut")
    Truth.assertThat(ref2.value()).isEqualTo("wut")
    Truth.assertThat(pool.size()).isEqualTo(1)
  }

  @Test
  fun testStringsWithDifferentPriorityAreNotDuplicates() {
    val pool = StringPool()

    val ref1 = pool.makeRef("wut", StringPool.Context(0x81010001L))
    val ref2 = pool.makeRef("wut", StringPool.Context(0x81010002L))

    Truth.assertThat(ref1.value()).isEqualTo("wut")
    Truth.assertThat(ref2.value()).isEqualTo("wut")
    Truth.assertThat(pool.size()).isEqualTo(2)
  }

  @Test
  fun testMaintainInsertionOrderIndex() {
    val pool = StringPool()

    val ref1 = pool.makeRef("z")
    val ref2 = pool.makeRef("a")
    val ref3 = pool.makeRef("m")

    Truth.assertThat(ref1.index()).isEqualTo(0)
    Truth.assertThat(ref2.index()).isEqualTo(1)
    Truth.assertThat(ref3.index()).isEqualTo(2)
  }

  @Test
  @Ignore("b/303074426") // TODO(@daniellabar): fix pruning and reference management.
  fun pruneStringsWithNoReferences() {
    // TEST HERE
  }

  @Test
  fun sortAndMaintainIndexesInStringReferences() {
    val pool = StringPool()

    val ref1 = pool.makeRef("z")
    val ref2 = pool.makeRef("a")
    val ref3 = pool.makeRef("m")

    Truth.assertThat(ref1.value()).isEqualTo("z")
    Truth.assertThat(ref1.index()).isEqualTo(0)

    Truth.assertThat(ref2.value()).isEqualTo("a")
    Truth.assertThat(ref2.index()).isEqualTo(1)

    Truth.assertThat(ref3.value()).isEqualTo("m")
    Truth.assertThat(ref3.index()).isEqualTo(2)

    pool.sort()

    Truth.assertThat(ref1.value()).isEqualTo("z")
    Truth.assertThat(ref1.index()).isEqualTo(2)

    Truth.assertThat(ref2.value()).isEqualTo("a")
    Truth.assertThat(ref2.index()).isEqualTo(0)

    Truth.assertThat(ref3.value()).isEqualTo("m")
    Truth.assertThat(ref3.index()).isEqualTo(1)
  }

  @Test
  fun deduplicateStillWorksWithSort() {
    val pool = StringPool()

    val ref1 = pool.makeRef("z")
    val ref2 = pool.makeRef("a")
    val ref3 = pool.makeRef("m")

    pool.sort()

    val ref4 = pool.makeRef("z")
    val ref5 = pool.makeRef("a")
    val ref6 = pool.makeRef("m")

    Truth.assertThat(ref1.index()).isEqualTo(ref4.index())
    Truth.assertThat(ref2.index()).isEqualTo(ref5.index())
    Truth.assertThat(ref3.index()).isEqualTo(ref6.index())
  }

  @Test
  fun testAddStyles() {
    val pool = StringPool()

    val ref = pool.makeRef(StyleString("android", listOf(Span("b", 2, 6))))
    Truth.assertThat(ref.index()).isEqualTo(0)
    Truth.assertThat(ref.value()).isEqualTo("android")
    Truth.assertThat(ref.spans()).hasSize(1)

    val span = ref.spans()[0]
    Truth.assertThat(span.name.value()).isEqualTo("b")
    Truth.assertThat(span.firstChar).isEqualTo(2)
    Truth.assertThat(span.lastChar).isEqualTo(6)
  }

  @Test
  fun doNotDeduplicateStyleWithString() {
    val pool = StringPool()

    val ref = pool.makeRef("android")
    val styleRef = pool.makeRef(StyleString("android", listOf()))

    Truth.assertThat(ref.index()).isNotEqualTo(styleRef.index())
  }

  @Test
  fun testSortingKeepsStylesAndStringsSeparate() {
    val pool = StringPool()

    val ref1 = pool.makeRef(StyleString("beta", listOf()))
    val ref2 = pool.makeRef("alpha")
    val ref3 = pool.makeRef(StyleString("alpha", listOf()))

    Truth.assertThat(ref2.index()).isNotEqualTo(ref3.index())

    pool.sort()

    Truth.assertThat(ref3.index()).isEqualTo(0)
    Truth.assertThat(ref1.index()).isEqualTo(1)
    Truth.assertThat(ref2.index()).isEqualTo(2)
  }

  @Test
  fun flattenEmptyStringPoolUtf8() {
    val pool = StringPool()

    val buffer = BigBuffer(1028)

    pool.flattenUtf8(buffer, null)
    Truth.assertThat(buffer.size).isEqualTo(28)

    val test = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    Truth.assertThat(test.stringPoolSize).isEqualTo(0)
  }

  @Test
  fun flattenOddCharactersUtf16() {
    val pool = StringPool()
    pool.makeRef("\u093f")

    val buffer = BigBuffer(1028)

    pool.flattenUtf16(buffer, null)
    // header size (28) + string array (4) + string length (2) + string itself (2) +
    // null terminator (2) + padding (2)
    Truth.assertThat(buffer.size).isEqualTo(40)

    val test = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    Truth.assertThat(test.strings).hasSize(1)
    val string = test.strings[0]
    Truth.assertThat(string).isEqualTo("\u093f")
  }

  @Test
  fun flattenPool() {
    val pool = StringPool()

    val ref1 = pool.makeRef("hello")
    val ref2 = pool.makeRef("goodbye")
    val ref3 = pool.makeRef(LONG_STRING)
    val ref4 = pool.makeRef("")
    val ref5 = pool.makeRef(StyleString("style", listOf(Span("b", 0, 1), Span("i", 2, 3))))

    Truth.assertThat(ref5.index()).isEqualTo(0)

    Truth.assertThat(ref1.index()).isEqualTo(1)
    Truth.assertThat(ref2.index()).isEqualTo(2)
    Truth.assertThat(ref3.index()).isEqualTo(3)
    Truth.assertThat(ref4.index()).isEqualTo(4)

    val utf8Buffer = BigBuffer(1024)
    val utf16Buffer = BigBuffer(1024)

    pool.flattenUtf8(utf8Buffer, null)
    pool.flattenUtf16(utf16Buffer, null)

    // Test both buffers.
    val utf8Test = ResStringPool.get(ByteBuffer.wrap(utf8Buffer.toBytes()), utf8Buffer.size)
    val utf16Test = ResStringPool.get(ByteBuffer.wrap(utf16Buffer.toBytes()), utf16Buffer.size)

    // size = number of direct string refs (5) + number of refs created by spans.
    Truth.assertThat(utf8Test.strings).hasSize(7)
    Truth.assertThat(utf16Test.strings).hasSize(7)

    Truth.assertThat(utf8Test.strings[0]).isEqualTo("style")
    Truth.assertThat(utf16Test.strings[0]).isEqualTo("style")

    Truth.assertThat(utf8Test.strings[1]).isEqualTo("hello")
    Truth.assertThat(utf16Test.strings[1]).isEqualTo("hello")

    Truth.assertThat(utf8Test.strings[2]).isEqualTo("goodbye")
    Truth.assertThat(utf16Test.strings[2]).isEqualTo("goodbye")

    Truth.assertThat(utf8Test.strings[3]).isEqualTo(LONG_STRING)
    Truth.assertThat(utf16Test.strings[3]).isEqualTo(LONG_STRING)

    Truth.assertThat(utf8Test.strings[4]).isEqualTo("")
    Truth.assertThat(utf16Test.strings[4]).isEqualTo("")

    Truth.assertThat(utf8Test.styles).hasSize(1)
    Truth.assertThat(utf16Test.styles).hasSize(1)

    val utf8Style = utf8Test.styles[0]
    val utf16Style = utf16Test.styles[0]

    Truth.assertThat(utf8Test.strings[utf8Style[0].name.index]).isEqualTo("b")
    Truth.assertThat(utf16Test.strings[utf16Style[0].name.index]).isEqualTo("b")

    Truth.assertThat(utf8Style[0].firstChar).isEqualTo(0)
    Truth.assertThat(utf16Style[0].firstChar).isEqualTo(0)

    Truth.assertThat(utf8Style[0].lastChar).isEqualTo(1)
    Truth.assertThat(utf16Style[0].lastChar).isEqualTo(1)

    Truth.assertThat(utf8Test.strings[utf8Style[1].name.index]).isEqualTo("i")
    Truth.assertThat(utf16Test.strings[utf16Style[1].name.index]).isEqualTo("i")

    Truth.assertThat(utf8Style[1].firstChar).isEqualTo(2)
    Truth.assertThat(utf16Style[1].firstChar).isEqualTo(2)

    Truth.assertThat(utf8Style[1].lastChar).isEqualTo(3)
    Truth.assertThat(utf16Style[1].lastChar).isEqualTo(3)
  }

    @Test
    fun testMaxEncodingLengthUTF8() {
      val pool1 = StringPool()
      pool1.makeRef("Why, hello!")
      var utf8Buffer = BigBuffer(1024)
      pool1.flattenUtf8(utf8Buffer, null)
      val utf8Test = ResStringPool.get(ByteBuffer.wrap(utf8Buffer.toBytes()), utf8Buffer.size)

      Truth.assertThat(utf8Test.strings).hasSize(1)
      Truth.assertThat(utf8Test.strings[0]).isEqualTo("Why, hello!")

      val pool2 = StringPool()
      val superLongString = String(CharArray(33000) {'a'})

      pool2.makeRef("This fits1")
      pool2.makeRef(superLongString)
      pool2.makeRef("This fits2")

      utf8Buffer = BigBuffer(1024)
      val utf8Exception = assertThrows(Exception::class.java) {
        pool2.flattenUtf8(utf8Buffer, null)
      }

      Truth.assertThat(utf8Exception.message).contains(
          "String of size 33000 bytes is too large to encode using UTF-8 (32767 bytes).")
    }

  @Test
  fun testMaxEncodingLengthUTF16() {
    val pool1 = StringPool()

    pool1.makeRef("Why, hello!")

    var utf16Buffer = BigBuffer(1024)

    pool1.flattenUtf16(utf16Buffer, null)

    var utf16Test = ResStringPool.get(ByteBuffer.wrap(utf16Buffer.toBytes()), utf16Buffer.size)

    Truth.assertThat(utf16Test.strings).hasSize(1)

    Truth.assertThat(utf16Test.strings[0]).isEqualTo("Why, hello!")

    val pool2 = StringPool()
    val superLongString = String(CharArray(33000) {'a'})

    pool2.makeRef("This fits1")
    pool2.makeRef(superLongString)
    pool2.makeRef("This fits2")

    utf16Buffer = BigBuffer(1024)
    pool2.flattenUtf16(utf16Buffer, null)
    utf16Test = ResStringPool.get(ByteBuffer.wrap(utf16Buffer.toBytes()), utf16Buffer.size)

    Truth.assertThat(utf16Test.strings).hasSize(3)
    Truth.assertThat(utf16Test.strings[0]).isEqualTo("This fits1")
    Truth.assertThat(utf16Test.strings[1]).isEqualTo(superLongString)
    Truth.assertThat(utf16Test.strings[2]).isEqualTo("This fits2")
  }

  companion object {
    const val LONG_STRING = "バッテリーを長持ちさせるため、バッテリーセーバーは端末のパフォーマンスを抑" +
      "え、バイブレーション、位置情報サービス、大半のバックグラウンドデータを制限" +
      "します。メール、SMSや、同期を使 " +
      "用するその他のアプリは、起動しても更新されないことがあります。バッテリーセ" +
      "ーバーは端末の充電中は自動的にOFFになります。"
  }
}
