package com.android.aaptcompiler

import com.android.SdkConstants
import jaxp.sun.xml.internal.stream.XMLInputFactoryImpl
import jaxp.xml.stream.XMLEventReader
import jaxp.xml.stream.events.StartElement

internal val xmlInputFactory = XMLInputFactoryImpl()

const val SCHEMA_PUBLIC_PREFIX = SdkConstants.URI_PREFIX
const val SCHEMA_PRIVATE_PREFIX = "http://schemas.android.com/apk/prv/res/"
const val SCHEMA_AUTO = SdkConstants.AUTO_URI
const val AAPT_ATTR_URI = "http://schemas.android.com/aapt"

// Result of extracting a package name from a namespace URI declaration
data class ExtractedPackage(val packageName: String, val isPrivate: Boolean)

private val EMPTY_PACKAGE = ExtractedPackage("", false)

fun extractPackageFromUri(namespaceUri: String): ExtractedPackage? {
  return when {
    namespaceUri.startsWith(SCHEMA_PUBLIC_PREFIX) -> {
      val packageName = namespaceUri.substring(SCHEMA_PUBLIC_PREFIX.length)
      if (packageName.isEmpty()) {
        return null
      }
      ExtractedPackage(packageName, false)
    }
    namespaceUri.startsWith(SCHEMA_PRIVATE_PREFIX) -> {
      val packageName = namespaceUri.substring(SCHEMA_PRIVATE_PREFIX.length)
      if (packageName.isEmpty()) {
        return null
      }
      ExtractedPackage(packageName, true)
    }
    namespaceUri == SCHEMA_AUTO -> ExtractedPackage("", true)
    else -> null
  }
}

fun constructPackageUri(alias: String, isPrivate: Boolean) = if (isPrivate) {
  SCHEMA_PRIVATE_PREFIX + alias
} else {
  SCHEMA_PUBLIC_PREFIX + alias
}

fun transformPackageAlias(element: StartElement, alias: String): ExtractedPackage? {
  if (alias.isEmpty()) {
    return EMPTY_PACKAGE
  }
  val uri = element.getNamespaceURI(alias) ?: return null
  return extractPackageFromUri(uri)
}

fun resolvePackage(element: StartElement, ref: Reference) {
  if (ref.name != ResourceName.EMPTY) {
    val transformedPackage = transformPackageAlias(element, ref.name.pck!!) ?: return

    ref.name = ref.name.copy(pck = transformedPackage.packageName)
    ref.isPrivate = ref.isPrivate || transformedPackage.isPrivate
  }
}

/**
 * Polls the {@code eventReader} till the reader is after the corresponding end element of
 * {@code element}.
 *
 * <p> It is assumed that {@code element} is the last startElement read from
 * the reader.
 *
 * @param element The start of the element to which we want to reach the end of.
 * @param eventReader The eventReader to be moved to the end of the corresponding
 *   {@code EndElement}.
 */
internal fun walkToEndOfElement(element: StartElement, eventReader: XMLEventReader) {
  var depth = 1
  while (eventReader.hasNext()) {
    val event = eventReader.nextEvent()

    if (event.isStartElement) {
      ++depth
    } else if (event.isEndElement) {
      --depth
      if (depth == 0) {
        // Sanity check.
        assert(event.asEndElement().name.localPart == element.name.localPart)
        break
      }
    }
  }
}
