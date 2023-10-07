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

package com.itsaky.androidide.activities

import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R
import com.itsaky.androidide.models.MainScreenAction
import com.itsaky.androidide.templates.BooleanParameter
import com.itsaky.androidide.templates.EnumParameter
import com.itsaky.androidide.templates.ITemplateProvider
import com.itsaky.androidide.templates.StringParameter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

  @Rule
  @JvmField
  var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

  @Test
  fun testMainFragmentActionsAreShown() {
    val recyclerView = onView(allOf(
      withId(R.id.actions),
      childAtPosition(
        withId(R.id.coordinator),
        3)))

    recyclerView.check(matches(isDisplayed()))

    for (action in MainScreenAction.all()) {
      val button = onView(
        allOf(withText(action.text),
          withParent(allOf(withId(R.id.actions),
            withParent(withId(R.id.coordinator)))),
          isDisplayed()))
      button.check(matches(isDisplayed()))
    }
  }

  @Test
  fun testTemplateWizardUi() {

    onView(withText(R.string.get_started)).check(matches(isCompletelyDisplayed()))

    val recyclerView = onView(allOf(
      withId(R.id.actions),
      childAtPosition(
        withId(R.id.coordinator),
        3)))

    recyclerView.check(matches(isDisplayed()))

    val action = MainScreenAction.all().find { it.id == MainScreenAction.ACTION_CREATE_PROJECT }
    assertThat(action).isNotNull()

    action!!

    val createProject = onView(allOf(withText(action.text), isDisplayed()))

    createProject.check(matches(isCompletelyDisplayed()))
    createProject.perform(click())

    val exitButton = onView(
      allOf(withId(R.id.exit_button), isCompletelyDisplayed())
    )

    exitButton.check(matches(isCompletelyDisplayed()))

    val templates = ITemplateProvider.getInstance().getTemplates()
    for (template in templates) {
      val templateTitle = onView(
        allOf(withId(R.id.template_name), withText(template.templateName),
          withParent(
            withParent(instanceOf(CardView::class.java))),
          isCompletelyDisplayed()))

      templateTitle.check(matches(isCompletelyDisplayed()))
    }

    val template = templates.first()
    val firstTemplateItem = onView(
      allOf(withId(R.id.template_name), withText(template.templateName),
        withParent(
          withParent(instanceOf(CardView::class.java))),
        isCompletelyDisplayed()))

    firstTemplateItem.perform(click())

    val previousButton = onView(
      allOf(withId(R.id.previous), withText(R.string.previous), isCompletelyDisplayed()))

    val createProjectFinishButton = onView(
      allOf(withId(R.id.finish), withText(R.string.create_project),
        isCompletelyDisplayed()))

    previousButton.check(matches(isCompletelyDisplayed()))
    createProjectFinishButton.check(matches(isCompletelyDisplayed()))

    for (parameter in template.parameters) {
      val selector = when (parameter) {
        is StringParameter -> withText(parameter.value)
        is BooleanParameter -> allOf(withText(parameter.name),
          if (parameter.value) isChecked() else isNotChecked())

        is EnumParameter -> withText(parameter.getDisplayName())
        else -> throw IllegalStateException("Unknown parameter type: $parameter")
      }
      val widget = onView(selector)
      widget.check(matches(isCompletelyDisplayed()))
    }

    previousButton.perform(click())

    Thread.sleep(1000L)

    exitButton.check(matches(isCompletelyDisplayed()))
    exitButton.perform(click())

    Thread.sleep(1000L)

    onView(withText(R.string.get_started)).check(matches(isCompletelyDisplayed()))
  }

  private fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
      }

      public override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return parent is ViewGroup && parentMatcher.matches(parent)
            && view == parent.getChildAt(position)
      }
    }
  }

}
