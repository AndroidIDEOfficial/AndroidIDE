package com.android.aaptcompiler

import com.android.aaptcompiler.ResourceFile.Type.ProtoXml
import com.android.aaptcompiler.testutils.parseNameOrFail
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Tests for the XmlProcessor, which parses XML files found in <source-set>/res/xml* directories.
 *
 * TODO(b/132800341): add a connected test to verify contents at runtime.
 * If you need to manually verify the correct behaviour, place the rest file under the res/xml
 * folder. To read contents you can use the XmlPullParser at runtime in an android project, for
 * example to verify the strings are parsed correctly you can query:
 *
 * val text = view.findViewById<android.widget.TextView>(R.id.text)
 * val xml = view.resources.getXml(R.xml.test)
 * var eventType = -1
 *
 * val builder = StringBuilder()
 * while (eventType != XmlResourceParser.END_DOCUMENT) {
 *     if (xml.eventType == XmlResourceParser.TEXT) {
 *         builder.append("${xml.name} = \"${xml.text}\"\n")
 *     }
 *
 *     eventType = xml.next()
 * }
 * text.text = "Found:\n${builder.toString()}"
 */
class XmlProcessorTest {

  lateinit var processor: XmlProcessor

  @Before
  fun beforeTest() {
    processor = XmlProcessor(Source(""), null)
  }

  private fun processTest(input: String, configDescription: String = ""): ResourceFile? {
    val file = ResourceFile(
      parseNameOrFail("layout/test"),
      parse(configDescription),
      Source(""),
      ProtoXml
    )
    try {
      processor.process(file, input.byteInputStream())
    } catch (e: Exception) {
      return null
    }
    return file
  }

  private fun getAttrName(inputFile: ResourceFile, attrNum: Int): ResourceName =
    inputFile.name.copy(entry = "${inputFile.name.pck}${'$'}${inputFile.name.entry}__$attrNum")

  @Test
  fun testEmpty() {
    assertThat(processTest("")).isNotNull()
  }

  @Test
  fun testCollectsIds() {
    val input = """
      <View xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/foo"
        text="@+id/bar">
        <SubView
          android:id="@+id/car"
          class="@+id/bar"/>
      </View>
    """.trimIndent()

    assertThat(processTest(input)).isNotNull()

    val collectedIds = processor.primaryFile.exportedSymbols
    assertThat(collectedIds).hasSize(3)

    assertThat(collectedIds[0]).isEqualTo(SourcedResourceName(parseNameOrFail("id/bar"), 3))
    assertThat(collectedIds[1]).isEqualTo(SourcedResourceName(parseNameOrFail("id/car"), 6))
    assertThat(collectedIds[2]).isEqualTo(SourcedResourceName(parseNameOrFail("id/foo"), 3))
  }

  @Test
  fun testNoCollectNonIds() {
    assertThat(processTest("""<View foo="@+string/foo"/>""")).isNotNull()

    assertThat(processor.primaryFile.exportedSymbols).isEmpty()

    val input = """
      <View xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/foo"
        text="@+string/bar"/>
    """.trimIndent()
    assertThat(processTest(input)).isNotNull()

    assertThat(processor.primaryFile.exportedSymbols).hasSize(1)
    assertThat(processor.primaryFile.exportedSymbols[0])
      .isEqualTo(SourcedResourceName(parseNameOrFail("id/foo"), 3))
  }

  @Test
  fun failOnInvalidIds() {
    assertThat(processTest("""<View foo="@+id/foo${'$'}bar"/>""")).isNull()
  }

  @Test
  fun checkNoSpaceAdded() {
      // XMLEventReader parser divides this string into two for some reason. When we're processing
      // it, we should make sure we join them without a new space, keeping the original string
      // intact.
      val stringDividedByReader = "1a(2d1r1u]1o2r3c4u1]1o2l1a(2l2u"
      val input = """<l>$stringDividedByReader</l>"""
      val inputFile = processTest(input)
      assertThat(inputFile).isNotNull()

      assertThat(processor.xmlResources).hasSize(1)

      val proto = processor.xmlResources[0].xmlProto
      assertThat(proto).isNotNull()
      assertThat(proto.hasElement()).isTrue()

      val element = proto.element
      assertThat(element.name).isEqualTo("l")
      assertThat(element.attributeList).hasSize(0)
      assertThat(element.childList).hasSize(1)

      // No extra spaces should be added in the middle of the string (even if XMLEventReader divides
      // it).
      assertThat(element.getChild(0).text).isEqualTo(stringDividedByReader)
  }

  @Test
  fun checkNoSpacesRemoved() {
      val contentWithSpaces = "   hello world! We meet again "
      val input = """<l>$contentWithSpaces</l>"""
      val inputFile = processTest(input)
      assertThat(inputFile).isNotNull()

      assertThat(processor.xmlResources).hasSize(1)

      val proto = processor.xmlResources[0].xmlProto
      assertThat(proto).isNotNull()
      assertThat(proto.hasElement()).isTrue()

      val element = proto.element
      assertThat(element.name).isEqualTo("l")
      assertThat(element.attributeList).hasSize(0)
      assertThat(element.childList).hasSize(1)

      // Extra spaces should be kept.
      assertThat(element.getChild(0).text).isEqualTo(contentWithSpaces)
  }

    @Test
    fun checkNoNewLineRemoved() {
        val contentWithNewLines = """
            <test>
                Text that is
                continued here.
            </test>""".trimIndent()

        val inputFile = processTest(contentWithNewLines)
        assertThat(inputFile).isNotNull()

        assertThat(processor.xmlResources).hasSize(1)

        val proto = processor.xmlResources[0].xmlProto
        assertThat(proto).isNotNull()
        assertThat(proto.hasElement()).isTrue()

        val element = proto.element
        assertThat(element.name).isEqualTo("test")
        assertThat(element.attributeList).hasSize(0)
        assertThat(element.childList).hasSize(1)


        // Indentation versus the parent node should be kept here. New lines should be respected.
        assertThat(element.getChild(0).text).isEqualTo("""
    Text that is
    continued here.
""")
    }

    @Test
    fun checkWhitespaceComboIsNotRemoved() {
        val contentWithNewLines = """
            
            hello      world ! 
            
            
We meet

       again
       """
        val input = """<l>$contentWithNewLines</l>"""
        val inputFile = processTest(input)
        assertThat(inputFile).isNotNull()

        assertThat(processor.xmlResources).hasSize(1)

        val proto = processor.xmlResources[0].xmlProto
        assertThat(proto).isNotNull()
        assertThat(proto.hasElement()).isTrue()

        val element = proto.element
        assertThat(element.name).isEqualTo("l")
        assertThat(element.attributeList).hasSize(0)
        assertThat(element.childList).hasSize(1)

        // We should keep the new lines and extra whitespaces, just as AAPT2 does in res/xml* files.
        assertThat(element.getChild(0).text).isEqualTo(contentWithNewLines)
    }

    @Test
    fun checkTextWithComment() {
        val textWithComment = """
                Text that is
                <!-- comment -->
                broken by child.""".trimIndent()

        val input = """<l>$textWithComment</l>"""
        val inputFile = processTest(input)
        assertThat(inputFile).isNotNull()

        assertThat(processor.xmlResources).hasSize(1)

        val proto = processor.xmlResources[0].xmlProto
        assertThat(proto).isNotNull()
        assertThat(proto.hasElement()).isTrue()

        val element = proto.element
        assertThat(element.name).isEqualTo("l")
        assertThat(element.attributeList).hasSize(0)
        assertThat(element.childList).hasSize(2)

        // New lines are kept (including new lines), only the comment is removed.
        assertThat(element.getChild(0).text).isEqualTo("Text that is\n")
        assertThat(element.getChild(1).text).isEqualTo("\nbroken by child.")
    }

  @Test
  fun testNoInlineXml() {
    val input = """
      <View xmlns:android="http://schemas.android.com/apk/res/android">
        <View android:text="hey">
          <View android:id="hi" />
        </View>
      </View>
    """.trimIndent()

    val inputFile = processTest(input)
    assertThat(inputFile).isNotNull()

    assertThat(processor.xmlResources).hasSize(1)

    val proto = processor.xmlResources[0].xmlProto
    assertThat(proto).isNotNull()
    assertThat(proto.hasElement()).isTrue()

    val element = proto.element
    assertThat(element.name).isEqualTo("View")
    assertThat(element.namespaceDeclarationList).hasSize(1)
    assertThat(element.attributeList).hasSize(0)
    assertThat(element.childList).hasSize(1)

    val namespace = element.namespaceDeclarationList[0]
    assertThat(namespace.uri).isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(namespace.prefix).isEqualTo("android")

    val childNode = element.childList[0]
    assertThat(childNode.hasElement()).isTrue()

    val child = childNode.element
    assertThat(child.name).isEqualTo("View")
    assertThat(child.namespaceDeclarationList).hasSize(0)
    assertThat(child.attributeList).hasSize(1)
    assertThat(child.childList).hasSize(1)

    val childAttr = child.attributeList[0]
    assertThat(childAttr.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(childAttr.name).isEqualTo("text")
    assertThat(childAttr.value).isEqualTo("hey")

    val grandchildNode = child.childList[0]
    assertThat(grandchildNode.hasElement()).isTrue()

    val grandchild = grandchildNode.element
    assertThat(grandchild.name).isEqualTo("View")
    assertThat(grandchild.namespaceDeclarationList).hasSize(0)
    assertThat(grandchild.attributeList).hasSize(1)
    assertThat(grandchild.childList).hasSize(0)

    val grandchildAttr = grandchild.attributeList[0]
    assertThat(grandchildAttr.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(grandchildAttr.name).isEqualTo("id")
    assertThat(grandchildAttr.value).isEqualTo("hi")
  }

  @Test
  fun testExtractOneXmlResource() {
    val input = """
      <View1 xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="android:text">
          <View2 android:text="hey">
            <View3 android:id="hi" />
          </View2>
        </aapt:attr>
      </View1>
    """.trimIndent()

    val inputFile = processTest(input)
    assertThat(inputFile).isNotNull()

    assertThat(processor.xmlResources).hasSize(2)

    val proto = processor.xmlResources[0].xmlProto
    assertThat(proto).isNotNull()
    assertThat(proto.hasElement()).isTrue()

    val element = proto.element
    assertThat(element.name).isEqualTo("View1")
    assertThat(element.namespaceDeclarationList).hasSize(2)
    assertThat(element.attributeList).hasSize(1)
    assertThat(element.childList).hasSize(0)

    val namespace1 = element.namespaceDeclarationList[0]
    assertThat(namespace1.uri).isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(namespace1.prefix).isEqualTo("android")

    val namespace2 = element.namespaceDeclarationList[1]
    assertThat(namespace2.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(namespace2.prefix).isEqualTo("aapt")

    // the aapt:attr should be pulled out as an attribute.
    // i.e. 'android:text="@layout/$test__0"'
    val attr = element.attributeList[0]
    assertThat(attr.namespaceUri).isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(attr.name).isEqualTo("text")
    assertThat(attr.value).isEqualTo("@${getAttrName(inputFile!!, 0)}")

    val outlinedProto = processor.xmlResources[1].xmlProto
    assertThat(outlinedProto).isNotNull()
    assertThat(outlinedProto.hasElement()).isTrue()

    val outlinedElement = outlinedProto.element
    assertThat(outlinedElement.name).isEqualTo("View2")
    // The outlined element inherits all active namespace declarations.
    assertThat(outlinedElement.namespaceDeclarationList).hasSize(2)
    assertThat(outlinedElement.attributeList).hasSize(1)
    assertThat(outlinedElement.childList).hasSize(1)

    val outlinedNamespace1 = outlinedElement.namespaceDeclarationList[0]
    assertThat(outlinedNamespace1.uri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlinedNamespace1.prefix).isEqualTo("android")

    val outlinedNamespace2 = outlinedElement.namespaceDeclarationList[1]
    assertThat(outlinedNamespace2.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(outlinedNamespace2.prefix).isEqualTo("aapt")

    val outlinedAttr = outlinedElement.attributeList[0]
    assertThat(outlinedAttr.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlinedAttr.name).isEqualTo("text")
    assertThat(outlinedAttr.value).isEqualTo("hey")

    val outlinedChildNode = outlinedElement.childList[0]
    assertThat(outlinedChildNode.hasElement()).isTrue()

    val outlinedChild = outlinedChildNode.element
    assertThat(outlinedChild.name).isEqualTo("View3")
    assertThat(outlinedChild.namespaceDeclarationList).hasSize(0)
    assertThat(outlinedChild.attributeList).hasSize(1)
    assertThat(outlinedChild.childList).hasSize(0)

    val outlinedChildAttr = outlinedChild.attributeList[0]
    assertThat(outlinedChildAttr.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlinedChildAttr.name).isEqualTo("id")
    assertThat(outlinedChildAttr.value).isEqualTo("hi")
  }

  @Test
  fun testExtractTwoSiblingResources() {
    val input = """
      <View1 xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="android:text">
          <View2 android:text="hey">
            <View3 android:id="hi" />
          </View2>
        </aapt:attr>

        <aapt:attr name="android:drawable">
          <vector />
        </aapt:attr>
      </View1>
    """.trimIndent()

    val inputFile = processTest(input)
    assertThat(inputFile).isNotNull()

    assertThat(processor.xmlResources).hasSize(3)

    val proto = processor.xmlResources[0].xmlProto
    assertThat(proto).isNotNull()
    assertThat(proto.hasElement()).isTrue()

    val element = proto.element
    assertThat(element.name).isEqualTo("View1")
    assertThat(element.namespaceDeclarationList).hasSize(2)
    assertThat(element.attributeList).hasSize(2)
    assertThat(element.childList).hasSize(0)

    val namespace1 = element.namespaceDeclarationList[0]
    assertThat(namespace1.uri).isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(namespace1.prefix).isEqualTo("android")

    val namespace2 = element.namespaceDeclarationList[1]
    assertThat(namespace2.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(namespace2.prefix).isEqualTo("aapt")

    // the aapt:attr should be pulled out as an attribute.
    // i.e. 'android:text="@layout/$test__0"'
    val attr1 = element.attributeList[0]
    assertThat(attr1.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(attr1.name).isEqualTo("text")
    assertThat(attr1.value).isEqualTo("@${getAttrName(inputFile!!, 0)}")

    // The second should be pulled out as well.
    // i.e. 'android:drawable="@layout/$test__1"'
    val attr2 = element.attributeList[1]
    assertThat(attr2.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(attr2.name).isEqualTo("drawable")
    assertThat(attr2.value).isEqualTo("@${getAttrName(inputFile, 1)}")

    val outlinedProto = processor.xmlResources[1].xmlProto
    assertThat(outlinedProto).isNotNull()
    assertThat(outlinedProto.hasElement()).isTrue()

    val outlinedElement = outlinedProto.element
    assertThat(outlinedElement.name).isEqualTo("View2")
    // The outlined element inherits all active namespace declarations.
    assertThat(outlinedElement.namespaceDeclarationList).hasSize(2)
    assertThat(outlinedElement.attributeList).hasSize(1)
    assertThat(outlinedElement.childList).hasSize(1)

    val outlinedNamespace1 = outlinedElement.namespaceDeclarationList[0]
    assertThat(outlinedNamespace1.uri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlinedNamespace1.prefix).isEqualTo("android")

    val outlinedNamespace2 = outlinedElement.namespaceDeclarationList[1]
    assertThat(outlinedNamespace2.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(outlinedNamespace2.prefix).isEqualTo("aapt")

    val outlinedAttr = outlinedElement.attributeList[0]
    assertThat(outlinedAttr.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlinedAttr.name).isEqualTo("text")
    assertThat(outlinedAttr.value).isEqualTo("hey")

    val outlinedChildNode = outlinedElement.childList[0]
    assertThat(outlinedChildNode.hasElement()).isTrue()

    val outlinedChild = outlinedChildNode.element
    assertThat(outlinedChild.name).isEqualTo("View3")
    assertThat(outlinedChild.namespaceDeclarationList).hasSize(0)
    assertThat(outlinedChild.attributeList).hasSize(1)
    assertThat(outlinedChild.childList).hasSize(0)

    val outlinedChildAttr = outlinedChild.attributeList[0]
    assertThat(outlinedChildAttr.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlinedChildAttr.name).isEqualTo("id")
    assertThat(outlinedChildAttr.value).isEqualTo("hi")

    val outlinedProto2 = processor.xmlResources[2].xmlProto
    assertThat(outlinedProto2).isNotNull()
    assertThat(outlinedProto2.hasElement()).isTrue()

    val outlined2Element = outlinedProto2.element
    assertThat(outlined2Element.name).isEqualTo("vector")
    assertThat(outlined2Element.namespaceDeclarationList).hasSize(2)
    assertThat(outlined2Element.attributeList).hasSize(0)
    assertThat(outlined2Element.childList).hasSize(0)

    val outlined2Namespace1 = outlinedElement.namespaceDeclarationList[0]
    assertThat(outlined2Namespace1.uri)
      .isEqualTo("http://schemas.android.com/apk/res/android")
    assertThat(outlined2Namespace1.prefix).isEqualTo("android")

    val outlined2Namespace2 = outlinedElement.namespaceDeclarationList[1]
    assertThat(outlined2Namespace2.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(outlined2Namespace2.prefix).isEqualTo("aapt")
  }

  @Test
  fun testExtractNestedXmlResource() {
    val input = """
      <base_root xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:aapt="http://schemas.android.com/aapt">
          <aapt:attr name="inline_xml">
              <inline_root>
                  <aapt:attr name="nested_inline_xml">
                      <nested_inline_root/>
                  </aapt:attr>
                  <aapt:attr name="another_nested_inline_xml">
                      <root/>
                  </aapt:attr>
              </inline_root>
          </aapt:attr>
          <aapt:attr name="turtles">
              <root1>
                  <aapt:attr name="all">
                      <root2>
                          <aapt:attr name="the">
                              <root3>
                                  <aapt:attr name="way">
                                      <root4>
                                          <aapt:attr name="down">
                                              <root5/>
                                          </aapt:attr>
                                      </root4>
                                  </aapt:attr>
                              </root3>
                          </aapt:attr>
                      </root2>
                  </aapt:attr>
              </root1>
          </aapt:attr>
      </base_root>
    """.trimIndent()

    val inputFile = processTest(input)
    assertThat(inputFile).isNotNull()

    // The primary file and the 8 outlined aapt:attr's
    assertThat(processor.xmlResources).hasSize(9)
  }

  @Test
  fun testExtractIntoAppAttr() {
    val input = """
      <parent xmlns:app="http://schemas.android.com/apk/res-auto"
          xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="app:foo">
            <child />
        </aapt:attr>
      </parent>
    """.trimIndent()

    val inputFile = processTest(input)
    assertThat(inputFile).isNotNull()

    assertThat(processor.xmlResources).hasSize(2)

    val proto = processor.xmlResources[0].xmlProto
    assertThat(proto).isNotNull()
    assertThat(proto.hasElement()).isTrue()

    val element = proto.element
    assertThat(element.name).isEqualTo("parent")
    assertThat(element.namespaceDeclarationList).hasSize(2)
    assertThat(element.attributeList).hasSize(1)
    assertThat(element.childList).hasSize(0)

    val namespace1 = element.namespaceDeclarationList[0]
    assertThat(namespace1.uri).isEqualTo("http://schemas.android.com/apk/res-auto")
    assertThat(namespace1.prefix).isEqualTo("app")

    val namespace2 = element.namespaceDeclarationList[1]
    assertThat(namespace2.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(namespace2.prefix).isEqualTo("aapt")

    val attr1 = element.attributeList[0]
    assertThat(attr1.namespaceUri)
      .isEqualTo("http://schemas.android.com/apk/res-auto")
    assertThat(attr1.name).isEqualTo("foo")
    assertThat(attr1.value).isEqualTo("@${getAttrName(inputFile!!, 0)}")
  }

  @Test
  fun testExtractIntoNoNamespaceAttr() {
    val input = """
      <parent xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="foo">
            <child />
        </aapt:attr>
      </parent>
    """.trimIndent()

    val inputFile = processTest(input)
    assertThat(inputFile).isNotNull()

    assertThat(processor.xmlResources).hasSize(2)

    val proto = processor.xmlResources[0].xmlProto
    assertThat(proto).isNotNull()
    assertThat(proto.hasElement()).isTrue()

    val element = proto.element
    assertThat(element.name).isEqualTo("parent")
    assertThat(element.namespaceDeclarationList).hasSize(1)
    assertThat(element.attributeList).hasSize(1)
    assertThat(element.childList).hasSize(0)

    val namespace = element.namespaceDeclarationList[0]
    assertThat(namespace.uri).isEqualTo("http://schemas.android.com/aapt")
    assertThat(namespace.prefix).isEqualTo("aapt")

    val attr1 = element.attributeList[0]
    assertThat(attr1.namespaceUri).isEqualTo("")
    assertThat(attr1.name).isEqualTo("foo")
    assertThat(attr1.value).isEqualTo("@${getAttrName(inputFile!!, 0)}")
  }

  @Test
  fun failAttrRoot() {
    val input = """
      <aapt:attr xmlns:aapt="http://schemas.android.com/aapt" name="foo">
        <child/>
      </aapt:attr>
    """.trimIndent()

    assertThat(processTest(input)).isNull()
  }

  @Test
  fun failConsecutiveNestedAttrDeclarations() {
    val input ="""
      <parent xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="foo">
          <aapt:attr name="bar">
            <child/>
          </aapt:attr>
        </aapt:attr>
      </parent>
    """.trimIndent()

    assertThat(processTest(input)).isNull()
  }

  @Test
  fun failToOverwriteExistingAttr() {
    val input = """
      <parent xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:aapt="http://schemas.android.com/aapt"
          android:drawable="@drawable/hi">
        <aapt:attr name="android:drawable">
          <vector/>
        </aapt:attr>
      </parent>
    """.trimIndent()

    assertThat(processTest(input)).isNull()
  }

  @Test
  fun failMultipleWritesToSameAttr() {
    val input = """
      <View1 xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="android:text">
          <View2 android:text="hey">
            <View3 android:id="hi" />
          </View2>
        </aapt:attr>
        <aapt:attr name="android:text">
          <View4 android:text="How's it going?">
            <View5 android:id="How are you?" />
          </View4>
        </aapt:attr>
      </View1>
    """.trimIndent()

    assertThat(processTest(input)).isNull()
  }

  @Test
  fun failEmptyAttr() {
    val input = """
      <View1 xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:aapt="http://schemas.android.com/aapt">
        <aapt:attr name="android:text">
        </aapt:attr>
      </View1>
    """.trimIndent()

    assertThat(processTest(input)).isNull()
  }
}
