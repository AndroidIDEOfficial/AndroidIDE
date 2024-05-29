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

package com.itsaky.androidide.lsp.xml.providers.completion

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.xml.CompletionHelper
import com.itsaky.androidide.lsp.xml.CompletionHelperImpl
import com.itsaky.androidide.lsp.xml.XMLLSPTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class LayoutAttributeCompletionProviderTest : CompletionHelper by CompletionHelperImpl() {

  @Before
  fun setup() {
    XMLLSPTest.initProjectIfNeeded()
  }

  @Test // prefix: 's'
  fun `attributes from superclasses must be included`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsFromSuperclass")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("android:singleLine") // From TextView
      assertThat(items).contains("android:scrollbars") // from View
    }
  }

  @Test // prefix: 'l'
  fun `attributes from parent's margin layout params must be included`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsFromLayoutParams")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("android:lines") // From TextView
      assertThat(items).contains("android:layout_weight") // from LinearLayout.LayoutParams
      assertThat(items).contains("android:layout_margin") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginLeft") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginTop") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginRight") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginBottom") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginStart") // from ViewGroup.MarginLayoutParams
    }
  }
  
  @Test // prefix: 'android:l'
  fun `attributes must be completed when namespace is specified as well`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsWithNamespace")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("android:lines") // From TextView
      assertThat(items).contains("android:layout_weight") // from LinearLayout.LayoutParams
      assertThat(items).contains("android:layout_margin") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginLeft") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginTop") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginRight") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginBottom") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginStart") // from ViewGroup.MarginLayoutParams
    }
  }
  
  @Test // prefix: 'android:margin'
  fun `attributes must be completed with a partial prefix`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsWithPartialName")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("android:layout_margin") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginLeft") // from ViewGroup.MarginLayoutParams
      assertThat(items).contains("android:layout_marginTop") // from ViewGroup.MarginLayoutParams
    }
  }
  
  @Test // prefix: 'padding'
  fun `duplicate attributes must not be included`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestNoDuplicateAttrs")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("android:paddingLeft") // from View
      assertThat(items).contains("android:paddingRight") // from View
      assertThat(items).contains("android:paddingTop") // from View
      assertThat(items).contains("android:paddingBottom") // from View
      assertThat(items).contains("android:paddingStart") // from View
      assertThat(items).contains("android:paddingEnd") // from View
      assertThat(items).doesNotContain("android:padding")
    }
  }
  
  @Test // prefix: 'layout'
  fun `attributes from all defined namespaces must be completed`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsWithMultipleNamespaces")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("material:layout_constraintEnd_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintEnd_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintHorizontal_bias") // From ConstraintLayout
      
    }
  }
  
  @Test // prefix: 'material:layout'
  fun `attributes from the defined namespace must be completed`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsWithDefinedNamespace")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("material:layout_constraintEnd_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintEnd_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintHorizontal_bias") // From ConstraintLayout
      
      // Attributes no other attributes must be included
      assertThat(items.filter { !it.startsWith("material:") }).isEmpty()
    }
  }
  
  @Test // prefix: 'layout'
  fun `attributes from defined auto namespace must be completed`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsWithMultipleNamespacesAuto")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("material:layout_constraintEnd_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintEnd_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintHorizontal_bias") // From ConstraintLayout
      
    }
  }
  
  @Test // prefix: 'material:layout'
  fun `attributes from the defined auto namespace must be completed`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsWithDefinedNamespaceAuto")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()
      assertThat(items).contains("material:layout_constraintEnd_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintEnd_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toEndOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintStart_toStartOf") // From ConstraintLayout
      assertThat(items).contains("material:layout_constraintHorizontal_bias") // From ConstraintLayout
      
      // Attributes no other attributes must be included
      assertThat(items.filter { !it.startsWith("material:") }).isEmpty()
    }
  }
}
