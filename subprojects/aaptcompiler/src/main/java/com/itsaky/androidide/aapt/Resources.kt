package com.itsaky.androidide.aapt

import com.android.aaptcompiler.ResourceEntry
import com.android.aaptcompiler.ResourceGroup

/** @author Akash Yadav */

/** Returns all [ResourceEntry]s which match the given predicate. */
fun ResourceGroup.findEntries(
  entryId: Short? = null,
  test: (String) -> Boolean
): List<ResourceEntry> {
  val result = mutableListOf<ResourceEntry>()
  entries.forEach {
    if (test(it.key)) {
      val element =
        if (entryId != null) {
          it.value[entryId] ?: it.value[null]
        } else {
          it.value[it.value.firstKey()]
        }

      if (element != null) {
        result.add(element)
      }
    }
  }
  return result
}
