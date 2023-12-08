package com.itsaky.androidide.fragments.onboarding

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

  @StringRes
  private var title: Int? = null

  @StringRes
  private var message: Int? = null

  @RawRes
  private var icon: Int? = null

  var name: String? = null

  private var binding: FragmentOnboardingBinding? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View {
    binding = FragmentOnboardingBinding.inflate(inflater, container, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding?.apply {
      tvMessage.movementMethod = LinkMovementMethod.getInstance()
      title?.let(this@OnboardingFragment::getString).also(tvTitle::setText)
      message?.let { HtmlCompat.fromHtml(getString(it), HtmlCompat.FROM_HTML_MODE_COMPACT) }.also(tvMessage::setText)
      icon?.let(animationView::setAnimation)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  companion object {
    @JvmStatic
    fun newInstance(@StringRes title: Int, @StringRes message: Int, @RawRes icon: Int, name: String) =
      OnboardingFragment().apply {
        this.title = title
        this.message = message
        this.icon = icon
        this.name = name
      }
  }
}