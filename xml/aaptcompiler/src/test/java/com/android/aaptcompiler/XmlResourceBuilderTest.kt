package com.android.aaptcompiler

import com.android.aaptcompiler.ResourceFile.Type.ProtoXml
import com.android.aaptcompiler.testutils.parseNameOrFail
import com.google.common.truth.Truth
import org.junit.Test

class XmlResourceBuilderTest {

  private val fakeFile = ResourceFile(
    parseNameOrFail("layout/foo"),
    ConfigDescription(),
    Source(""),
    ProtoXml
  )

  @Test
  fun testBasicXmlFlattening() {
    /*
     * <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
     *   android:layout_width="match_parent"
     *   android:layout_height="match_parent"
     *   android:orientation="vertical"/>
     */
    val proto = XmlResourceBuilder(fakeFile)
      .startElement("LinearLayout", "")
      .addNamespaceDeclaration("http://schemas.android.com/apk/res/android", "android")
      .addAttribute("layout_width", "http://schemas.android.com/apk/res/android", "match_parent")
      .addAttribute("layout_height", "http://schemas.android.com/apk/res/android", "match_parent")
      .endElement()
      .build()
      .xmlProto

    Truth.assertThat(proto.hasElement()).isTrue()
    val root = proto.getElement()
    Truth.assertThat(root.getNamespaceUri()).isEmpty()
    Truth.assertThat(root.getName()).isEqualTo("LinearLayout")
    val nsDeclarations = root.getNamespaceDeclarationList()
    Truth.assertThat(nsDeclarations).hasSize(1)
    val androidNs = nsDeclarations[0]
    Truth.assertThat(androidNs.getUri()).isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(androidNs.getPrefix()).isEqualTo("android")
    val attributes = root.getAttributeList()
    Truth.assertThat(attributes).hasSize(2)
    val attribute1 = attributes[0]
    Truth.assertThat(attribute1.getName()).isEqualTo("layout_width")
    Truth.assertThat(attribute1.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(attribute1.getValue()).isEqualTo("match_parent")
    val attribute2 = attributes[1]
    Truth.assertThat(attribute2.getName()).isEqualTo("layout_height")
    Truth.assertThat(attribute2.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(attribute2.getValue()).isEqualTo("match_parent")
    Truth.assertThat(root.getChildList()).hasSize(0)
  }

  @Test
  fun testNestedXmlFlattening() {
    /*
     * <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
     *   android:layout_width="match_parent"
     *   android:layout_height="match_parent"
     *   android:orientation="vertical">
     *   <TextView
     *     android:layout_width="wrap_content"
     *     android:layout_height="wrap_content"
     *     android:text="@string/connect_request"/>
     *   <Button
     *     android:id="@+id/confirm"
     *     android:layout_width="wrap_content"
     *     android:layout_height="wrap_content"
     *     android:text="@string/confirm_button"/>
     *    <Button
     *     android:id="@+id/confirm"
     *     android:layout_width="wrap_content"
     *     android:layout_height="wrap_content"
     *     android:text="@string/cancel_button"/>
     * </LinearLayout>
     */

    val proto = XmlResourceBuilder(fakeFile)
      .startElement("LinearLayout", "")
      .addNamespaceDeclaration("http://schemas.android.com/apk/res/android", "android")
      .addAttribute("layout_width", "http://schemas.android.com/apk/res/android", "match_parent")
      .addAttribute("layout_height", "http://schemas.android.com/apk/res/android", "match_parent")
      .startElement("TextView", "")
      .addAttribute("layout_width", "http://schemas.android.com/apk/res/android", "wrap_content")
      .addAttribute("layout_height", "http://schemas.android.com/apk/res/android", "wrap_content")
      .addAttribute(
        "text", "http://schemas.android.com/apk/res/android", "@string/connect_request")
      .endElement()
      .startElement("Button", "")
      .addAttribute("id", "http://schemas.android.com/apk/res/android", "@+id/confirm")
      .addAttribute("layout_width", "http://schemas.android.com/apk/res/android", "wrap_content")
      .addAttribute("layout_height", "http://schemas.android.com/apk/res/android", "wrap_content")
      .addAttribute("text", "http://schemas.android.com/apk/res/android", "@string/confirm_button")
      .endElement()
      .startElement("Button", "")
      .addAttribute("id", "http://schemas.android.com/apk/res/android", "@+id/cancel")
      .addAttribute("layout_width", "http://schemas.android.com/apk/res/android", "wrap_content")
      .addAttribute("layout_height", "http://schemas.android.com/apk/res/android", "wrap_content")
      .addAttribute("text", "http://schemas.android.com/apk/res/android", "@string/cancel_button")
      .endElement()
      .endElement()
      .build()
      .xmlProto

    Truth.assertThat(proto.hasElement()).isTrue()
    val root = proto.getElement()
    Truth.assertThat(root.getNamespaceUri()).isEmpty()
    Truth.assertThat(root.getName()).isEqualTo("LinearLayout")
    val nsDeclarations = root.getNamespaceDeclarationList()
    Truth.assertThat(nsDeclarations).hasSize(1)
    val androidNs = nsDeclarations[0]
    Truth.assertThat(androidNs.getUri()).isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(androidNs.getPrefix()).isEqualTo("android")
    val attributes = root.getAttributeList()
    Truth.assertThat(attributes).hasSize(2)
    val attribute1 = attributes[0]
    Truth.assertThat(attribute1.getName()).isEqualTo("layout_width")
    Truth.assertThat(attribute1.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(attribute1.getValue()).isEqualTo("match_parent")
    val attribute2 = attributes[1]
    Truth.assertThat(attribute2.getName()).isEqualTo("layout_height")
    Truth.assertThat(attribute2.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(attribute2.getValue()).isEqualTo("match_parent")
    val childList = root.getChildList()
    Truth.assertThat(childList).hasSize(3)

    Truth.assertThat(childList[0].hasElement()).isTrue()
    val child1 = childList[0].getElement()
    Truth.assertThat(child1.getNamespaceUri()).isEmpty()
    Truth.assertThat(child1.getName()).isEqualTo("TextView")
    Truth.assertThat(child1.getNamespaceDeclarationList()).hasSize(0)
    val child1Attributes = child1.getAttributeList()
    Truth.assertThat(child1Attributes).hasSize(3)
    val child1Attr1 = child1Attributes[0]
    Truth.assertThat(child1Attr1.getName()).isEqualTo("layout_width")
    Truth.assertThat(child1Attr1.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child1Attr1.getValue()).isEqualTo("wrap_content")
    val child1Attr2 = child1Attributes[1]
    Truth.assertThat(child1Attr2.getName()).isEqualTo("layout_height")
    Truth.assertThat(child1Attr2.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child1Attr2.getValue()).isEqualTo("wrap_content")
    val child1Attr3 = child1Attributes[2]
    Truth.assertThat(child1Attr3.getName()).isEqualTo("text")
    Truth.assertThat(child1Attr3.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child1Attr3.getValue()).isEqualTo("@string/connect_request")

    Truth.assertThat(childList[1].hasElement()).isTrue()
    val child2 = childList[1].getElement()
    Truth.assertThat(child2.getNamespaceUri()).isEmpty()
    Truth.assertThat(child2.getName()).isEqualTo("Button")
    Truth.assertThat(child2.getNamespaceDeclarationList()).hasSize(0)
    val child2Attributes = child2.getAttributeList()
    Truth.assertThat(child2Attributes).hasSize(4)
    val child2Attr1 = child2Attributes[0]
    Truth.assertThat(child2Attr1.getName()).isEqualTo("id")
    Truth.assertThat(child2Attr1.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child2Attr1.getValue()).isEqualTo("@+id/confirm")
    val child2Attr2 = child2Attributes[1]
    Truth.assertThat(child2Attr2.getName()).isEqualTo("layout_width")
    Truth.assertThat(child2Attr2.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child2Attr2.getValue()).isEqualTo("wrap_content")
    val child2Attr3 = child2Attributes[2]
    Truth.assertThat(child2Attr3.getName()).isEqualTo("layout_height")
    Truth.assertThat(child2Attr3.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child2Attr3.getValue()).isEqualTo("wrap_content")
    val child2Attr4 = child2Attributes[3]
    Truth.assertThat(child2Attr4.getName()).isEqualTo("text")
    Truth.assertThat(child2Attr4.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child2Attr4.getValue()).isEqualTo("@string/confirm_button")

    Truth.assertThat(childList[2].hasElement()).isTrue()
    val child3 = childList[2].getElement()
    Truth.assertThat(child3.getNamespaceUri()).isEmpty()
    Truth.assertThat(child3.getName()).isEqualTo("Button")
    Truth.assertThat(child3.getNamespaceDeclarationList()).hasSize(0)
    val child3Attributes = child3.getAttributeList()
    Truth.assertThat(child3Attributes).hasSize(4)
    val child3Attr1 = child3Attributes[0]
    Truth.assertThat(child3Attr1.getName()).isEqualTo("id")
    Truth.assertThat(child3Attr1.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child3Attr1.getValue()).isEqualTo("@+id/cancel")
    val child3Attr2 = child3Attributes[1]
    Truth.assertThat(child3Attr2.getName()).isEqualTo("layout_width")
    Truth.assertThat(child3Attr2.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child3Attr2.getValue()).isEqualTo("wrap_content")
    val child3Attr3 = child3Attributes[2]
    Truth.assertThat(child3Attr3.getName()).isEqualTo("layout_height")
    Truth.assertThat(child3Attr3.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child3Attr3.getValue()).isEqualTo("wrap_content")
    val child3Attr4 = child3Attributes[3]
    Truth.assertThat(child3Attr4.getName()).isEqualTo("text")
    Truth.assertThat(child3Attr4.getNamespaceUri())
      .isEqualTo("http://schemas.android.com/apk/res/android")
    Truth.assertThat(child3Attr4.getValue()).isEqualTo("@string/cancel_button")
  }

  @Test
  fun testText() {
    /*
     * <test>text</test>
     */
    var proto = XmlResourceBuilder(fakeFile)
      .startElement("test", "")
      .addText("text")
      .endElement()
      .build()
      .xmlProto

    Truth.assertThat(proto.hasElement()).isTrue()
    var root = proto.getElement()
    Truth.assertThat(root.getNamespaceUri()).isEmpty()
    Truth.assertThat(root.getName()).isEqualTo("test")
    Truth.assertThat(root.getNamespaceDeclarationList()).hasSize(0)
    Truth.assertThat(root.getAttributeList()).hasSize(0)
    Truth.assertThat(root.getChildList()).hasSize(1)

    Truth.assertThat(root.getChildList()[0].hasElement()).isFalse()
    Truth.assertThat(root.getChildList()[0].getText()).isEqualTo("text")

    // Consecutive text elements should be treated as a single element. XMLEventReader parses those
    // as one event, even though they're divided by new lines.
    /*
     * <test>
     *   Text that is
     *   continued here.
     * </test>
     */
    proto = XmlResourceBuilder(fakeFile)
      .startElement("test", "")
      .addText("\nText that is\n continued here.")
      .endElement()
      .build()
      .xmlProto

    Truth.assertThat(proto.hasElement()).isTrue()
    root = proto.getElement()
    Truth.assertThat(root.getNamespaceUri()).isEmpty()
    Truth.assertThat(root.getName()).isEqualTo("test")
    Truth.assertThat(root.getNamespaceDeclarationList()).hasSize(0)
    Truth.assertThat(root.getAttributeList()).hasSize(0)
    Truth.assertThat(root.getChildList()).hasSize(1)

    Truth.assertThat(root.getChildList()[0].hasElement()).isFalse()
    Truth.assertThat(root.getChildList()[0].getText()).isEqualTo("\nText that is\n continued here.")

    // Text elements should be considered separate if they have anything inbetween them.
    /*
     * <test>
     *   Text that is
     *   <!-- comment -->
     *   broken by comment.
     * </test>
     */
    proto = XmlResourceBuilder(fakeFile)
      .startElement("test", "")
      .addText("Text that is")
      .addComment()
      .addText("broken by comment.")
      .endElement()
      .build()
      .xmlProto

    Truth.assertThat(proto.hasElement()).isTrue()
    root = proto.getElement()
    Truth.assertThat(root.getNamespaceUri()).isEmpty()
    Truth.assertThat(root.getName()).isEqualTo("test")
    Truth.assertThat(root.getNamespaceDeclarationList()).hasSize(0)
    Truth.assertThat(root.getAttributeList()).hasSize(0)
    Truth.assertThat(root.getChildList()).hasSize(2)

    Truth.assertThat(root.getChildList()[0].hasElement()).isFalse()
    Truth.assertThat(root.getChildList()[0].getText()).isEqualTo("Text that is")
    Truth.assertThat(root.getChildList()[1].hasElement()).isFalse()
    Truth.assertThat(root.getChildList()[1].getText()).isEqualTo("broken by comment.")

    /*
     * <test>
     *   Text that is
     *   <child/>
     *   broken by child.
     * </test>
     */
    proto = XmlResourceBuilder(fakeFile)
      .startElement("test", "")
      .addText("Text that is")
      .startElement("child", "")
      .endElement()
      .addText("broken by child.")
      .endElement()
      .build()
      .xmlProto

    Truth.assertThat(proto.hasElement()).isTrue()
    root = proto.getElement()
    Truth.assertThat(root.getNamespaceUri()).isEmpty()
    Truth.assertThat(root.getName()).isEqualTo("test")
    Truth.assertThat(root.getNamespaceDeclarationList()).hasSize(0)
    Truth.assertThat(root.getAttributeList()).hasSize(0)
    Truth.assertThat(root.getChildList()).hasSize(3)

    Truth.assertThat(root.getChildList()[0].hasElement()).isFalse()
    Truth.assertThat(root.getChildList()[0].getText()).isEqualTo("Text that is")

    Truth.assertThat(root.getChildList()[1].hasElement()).isTrue()

    Truth.assertThat(root.getChildList()[2].hasElement()).isFalse()
    Truth.assertThat(root.getChildList()[2].getText()).isEqualTo("broken by child.")
  }

  @Test
  fun testNamespaceContext() {
    /*
      <LinearLayout xmlns:android="androidOuter">
        <ListView xmlns:android="androidInner" xmlns:aapt="aapt">
        </ListView>
        <ListView2 xmlns:android="androidInner2">
          <View xmlns:android="androidInnerInner" xmlns:something="something/something"/>
        </ListView2>
      </LinearLayout>
    */
    val builder = XmlResourceBuilder(fakeFile)
    val nsContext = builder.namespaceContext

    builder.startElement("LinearLayout", "")
      .addNamespaceDeclaration("androidOuter", "android")

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidOuter")

    builder.startElement("ListView", "")
    builder.addNamespaceDeclaration("androidInner", "android")
    builder.addNamespaceDeclaration("aapt", "aapt")

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android", "aapt"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidInner")
    Truth.assertThat(nsContext.uriForPrefix("aapt")).isEqualTo("aapt")

    builder.endElement()

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidOuter")

    builder.startElement("ListView2", "")
      .addNamespaceDeclaration("androidInner2", "android")

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidInner2")

    builder.startElement("View", "")
      .addNamespaceDeclaration("androidInnerInner", "android")
      .addNamespaceDeclaration("something/something", "something")

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android", "something"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidInnerInner")
    Truth.assertThat(nsContext.uriForPrefix("something")).isEqualTo("something/something")

    builder.endElement()

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidInner2")

    builder.endElement()

    Truth.assertThat(nsContext.namespacePrefixes()).isEqualTo(setOf("android"))
    Truth.assertThat(nsContext.uriForPrefix("android")).isEqualTo("androidOuter")

    builder.endElement()

    Truth.assertThat(builder.isFinished()).isTrue()
    Truth.assertThat(nsContext.namespacePrefixes()).isEmpty()
  }
}
