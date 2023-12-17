package com.itsaky.androidide.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itsaky.androidide.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

  private var _binding: FragmentStatisticsBinding? = null
  private val binding: FragmentStatisticsBinding
    get() = checkNotNull(_binding) { "Fragment has been destroyed" }

  val statOptIn: Boolean
    get() = binding.statOptIn.isChecked

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View {
    _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  companion object {

    @JvmStatic
    fun newInstance() = StatisticsFragment()
  }
}