package com.itsaky.androidide.desugaring.parsing

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.desugaring.dsl.MethodOpcode
import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer
import com.itsaky.androidide.desugaring.internal.parsing.InsnParser
import com.itsaky.androidide.desugaring.internal.parsing.InsnParser.ParseException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class InsnParserTest {

  @Test
  fun `test empty input`() {
    val insnStr = ""
    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns).isEmpty()
  }

  @Test
  fun `test basic types`() {
    val insnStr = """
    invoke-virtual Ljava/lang/String;->length()I
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredString;->length(Ljava/lang/String;)I
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns[0].methodDescriptor).isEqualTo("()I")
    assertThat(insns[0].toMethodDescriptor).isEqualTo("(Ljava/lang/String;)I")

    val insnStr2 = """
    invoke-virtual Ljava/lang/String;->charAt(I)C
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredString;->charAt(Ljava/lang/String;I)C
      ;;
  """.trimIndent()

    val parser2 = InsnParser(InsnLexer(insnStr2))
    val insns2 = parser2.parse()

    assertThat(insns2[0].methodDescriptor).isEqualTo("(I)C")
    assertThat(insns2[0].toMethodDescriptor).isEqualTo("(Ljava/lang/String;I)C")
  }

  @Test
  fun `test reference types`() {
    val insnStr = """
    invoke-virtual Ljava/lang/Object;->clone()Ljava/lang/Object;
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredObject;->clone(Ljava/lang/Object;)Ljava/lang/Object;
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns[0].methodDescriptor).isEqualTo("()Ljava/lang/Object;")
    assertThat(insns[0].toMethodDescriptor).isEqualTo("(Ljava/lang/Object;)Ljava/lang/Object;")

    val insnStr2 = """
    invoke-virtual Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredString;->concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      ;;
  """.trimIndent()

    val parser2 = InsnParser(InsnLexer(insnStr2))
    val insns2 = parser2.parse()

    assertThat(insns2[0].methodDescriptor).isEqualTo("(Ljava/lang/String;)Ljava/lang/String;")
    assertThat(insns2[0].toMethodDescriptor).isEqualTo("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;")
  }

  @Test
  fun `test single-dimensional arrays`() {
    val insnStr = """
    invoke-virtual Ljava/lang/String;->someArr()[I
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredArrays;->someArr([I)I
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns[0].methodDescriptor).isEqualTo("()[I")
    assertThat(insns[0].toMethodDescriptor).isEqualTo("([I)I")

    val insnStr2 = """
    invoke-static Ljava/util/Arrays;->sort([I)V
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/util/DesugaredArrays;->sort([I)V
      ;;
  """.trimIndent()

    val parser2 = InsnParser(InsnLexer(insnStr2))
    val insns2 = parser2.parse()

    assertThat(insns2[0].methodDescriptor).isEqualTo("([I)V")
    assertThat(insns2[0].toMethodDescriptor).isEqualTo("([I)V")
  }

  @Test
  fun `test multi-dimensional arrays`() {
    val insnStr = """
    invoke-virtual Ljava/lang/String;->length()I
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredArrays;->length([[I)I
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns[0].methodDescriptor).isEqualTo("()I")
    assertThat(insns[0].toMethodDescriptor).isEqualTo("([[I)I")

    val insnStr2 = """
    invoke-static Ljava/util/Arrays;->deepEquals([[I)V
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/util/DesugaredArrays;->deepEquals([[I)V
      ;;
  """.trimIndent()

    val parser2 = InsnParser(InsnLexer(insnStr2))
    val insns2 = parser2.parse()

    assertThat(insns2[0].methodDescriptor).isEqualTo("([[I)V")
    assertThat(insns2[0].toMethodDescriptor).isEqualTo("([[I)V")
  }

  @Test
  fun `test array of reference types`() {
    val insnStr = """
    invoke-virtual Ljava/lang/String;->clone()Ljava/lang/Object;
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredObject;->clone([Ljava/lang/String;)Ljava/lang/Object;
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns[0].methodDescriptor).isEqualTo("()Ljava/lang/Object;")
    assertThat(insns[0].toMethodDescriptor).isEqualTo("([Ljava/lang/String;)Ljava/lang/Object;")

    val insnStr2 = """
    invoke-static Ljava/util/Arrays;->asList([Ljava/lang/String;)Ljava/util/List;
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/util/DesugaredArrays;->asList([Ljava/lang/String;)Ljava/util/List;
      ;;
  """.trimIndent()

    val parser2 = InsnParser(InsnLexer(insnStr2))
    val insns2 = parser2.parse()

    assertThat(insns2[0].methodDescriptor).isEqualTo("([Ljava/lang/String;)Ljava/util/List;")
    assertThat(insns2[0].toMethodDescriptor).isEqualTo("([Ljava/lang/String;)Ljava/util/List;")
  }

  @Test
  fun `test primitive arrays as method parameters`() {
    val insnStr = """
    invoke-static Ljava/lang/Math;->max([I)I
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredMath;->max([I)I
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns[0].methodDescriptor).isEqualTo("([I)I")
    assertThat(insns[0].toMethodDescriptor).isEqualTo("([I)I")

    val insnStr2 = """
    invoke-virtual Ljava/lang/String;->toCharArray()[C
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/lang/DesugaredString;->toCharArray(Ljava/lang/String;)[C
      ;;
  """.trimIndent()

    val parser2 = InsnParser(InsnLexer(insnStr2))
    val insns2 = parser2.parse()

    assertThat(insns2[0].methodDescriptor).isEqualTo("()[C")
    assertThat(insns2[0].toMethodDescriptor).isEqualTo("(Ljava/lang/String;)[C")
  }


  @Test
  fun `test invalid opcode`() {
    val insnStr = """
    invalid-opcode Ljava/lang/Object;->toString()Ljava/lang/String;
      =>
    invoke-static Ljava/lang/Object;->toString()Ljava/lang/String;
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    assertThrows<ParseException> { parser.parse() }
  }

  @Test
  fun `test invalid class name`() {
    val insnStr = """
    invoke-virtual LInvalid.ClassName;->invalidMethod()V
      =>
    invoke-static LSomeOther..Class;->validMethod()V
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    assertThrows<ParseException> { parser.parse() }
  }

  @Test
  fun `test invalid method descriptor`() {
    val insnStr = """
    invoke-virtual Ljava/lang/Object;->toString(InvalidDesc)V
      =>
    invoke-static Ljava/lang/Object;->toString()Ljava/lang/String;
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    assertThrows<ParseException> { parser.parse() }
  }

  @Test
  fun `test missing semicolons`() {
    val insnStr = """
    invoke-virtual Ljava/lang/Object;->toString()Ljava/lang/String;
    =>
    invoke-static Ljava/lang/Object;->toString()Ljava/lang/String;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    assertThrows<ParseException> { parser.parse() }
  }

  @Test
  fun `test extra semicolons`() {
    val insnStr = """
    invoke-virtual Ljava/lang/Object;->toString()Ljava/lang/String;;
      =>
    invoke-static Ljava/lang/Object;->toString()Ljava/lang/String;;
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    assertThrows<ParseException> { parser.parse() }
  }

  @Test
  fun `test simple instruction parse`() {
    val insnStr = """
      invoke-virtual Ljava/io/InputStream;->readNBytes(I)[B
        =>
      invoke-static Lcom/itsaky/androidide/desugaring/sample/java/io/DesugaredInputStream;->readNBytes(Ljava/io/InputStream;I)V
        ;;
    """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns).isNotNull()
    assertThat(insns).hasSize(1)

    val insn = insns[0]

    assertThat(insn).isNotNull()
    assertThat(insn.requireOpcode).isEqualTo(MethodOpcode.INVOKEVIRTUAL)
    assertThat(insn.fromClass).isEqualTo("java.io.InputStream")
    assertThat(insn.methodName).isEqualTo("readNBytes")
    assertThat(insn.methodDescriptor).isEqualTo("(I)[B")
    assertThat(insn.toOpcode).isEqualTo(MethodOpcode.INVOKESTATIC)
    assertThat(insn.toClass).isEqualTo(
      "com.itsaky.androidide.desugaring.sample.java.io.DesugaredInputStream")
    assertThat(insn.toMethod).isEqualTo("readNBytes")
    assertThat(insn.toMethodDescriptor).isEqualTo("(Ljava/io/InputStream;I)V")
  }

  @Test
  fun `test parsing multiple instructions`() {
    val insnStr = """
    invoke-virtual Ljava/io/InputStream;->readNBytes(I)[B
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/io/DesugaredInputStream;->readNBytes([Ljava/io/InputStream;I)V
      ;;
    invoke-static Ljava/lang/Math;->random()D
      =>
    invoke-static Lcom/itsaky/androidide/desugaring/sample/java/util/DesugaredMath;->random()D
      ;;
  """.trimIndent()

    val parser = InsnParser(InsnLexer(insnStr))
    val insns = parser.parse()

    assertThat(insns).hasSize(2)

    insns[0].also { insn ->
      assertThat(insn).isNotNull()
      assertThat(insn.requireOpcode).isEqualTo(MethodOpcode.INVOKEVIRTUAL)
      assertThat(insn.fromClass).isEqualTo("java.io.InputStream")
      assertThat(insn.methodName).isEqualTo("readNBytes")
      assertThat(insn.methodDescriptor).isEqualTo("(I)[B")
      assertThat(insn.toOpcode).isEqualTo(MethodOpcode.INVOKESTATIC)
      assertThat(insn.toClass).isEqualTo(
        "com.itsaky.androidide.desugaring.sample.java.io.DesugaredInputStream")
      assertThat(insn.toMethod).isEqualTo("readNBytes")
      assertThat(insn.toMethodDescriptor).isEqualTo(
        "([Ljava/io/InputStream;I)V")
    }

    insns[1].also { insn ->
      assertThat(insn).isNotNull()
      assertThat(insn.requireOpcode).isEqualTo(MethodOpcode.INVOKESTATIC)
      assertThat(insn.fromClass).isEqualTo("java.lang.Math")
      assertThat(insn.methodName).isEqualTo("random")
      assertThat(insn.methodDescriptor).isEqualTo("()D")
      assertThat(insn.toOpcode).isEqualTo(MethodOpcode.INVOKESTATIC)
      assertThat(insn.toClass).isEqualTo(
        "com.itsaky.androidide.desugaring.sample.java.util.DesugaredMath")
      assertThat(insn.toMethod).isEqualTo("random")
      assertThat(insn.toMethodDescriptor).isEqualTo("()D")
    }
  }
}
