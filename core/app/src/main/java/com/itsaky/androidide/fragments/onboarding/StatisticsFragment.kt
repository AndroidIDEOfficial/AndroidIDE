package com.itsaky.androidide.fragments.onboarding

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.itsaky.androidide.R
import com.itsaky.androidide.app.IDEApplication
import com.itsaky.androidide.databinding.LayoutOnboardingStatisticsBinding

class StatisticsFragment : OnboardingFragment() {

  private var _content: LayoutOnboardingStatisticsBinding? = null
  private val content: LayoutOnboardingStatisticsBinding
    get() = checkNotNull(_content) { "Fragment has been destroyed" }

  companion object {

    @JvmStatic
    fun newInstance(context: Context): StatisticsFragment {
      return StatisticsFragment().apply {
        arguments = Bundle().apply {
          putCharSequence(KEY_ONBOARDING_TITLE,
            context.getString(R.string.title_androidide_statistics))
          putCharSequence(KEY_ONBOARDING_SUBTITLE,
            context.getString(R.string.idepref_stats_optIn_summary))
        }
      }
    }
  }

  override fun createContentView(parent: ViewGroup, attachToParent: Boolean) {
    _content = LayoutOnboardingStatisticsBinding.inflate(layoutInflater, parent, attachToParent)
    content.statOptIn.isChecked = true
  }

  override fun onDestroy() {
    super.onDestroy()
    _content = null
  }

  fun updateStatOptInStatus() {
    com.itsaky.androidide.preferences.internal.StatPreferences.statOptIn =
      _content?.statOptIn?.isChecked ?: false

    IDEApplication.instance.reportStatsIfNecessary()
  }
}