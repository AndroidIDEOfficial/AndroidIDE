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

package com.itsaky.androidide.fragments.onboarding

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.URLSpan
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import com.blankj.utilcode.util.SizeUtils
import com.github.appintro.SlidePolicy
import com.google.android.material.button.MaterialButton
import com.itsaky.androidide.R
import com.itsaky.androidide.databinding.LayoutChipErrorBinding
import com.itsaky.androidide.databinding.LayoutChipWarningBinding
import com.itsaky.androidide.models.DefaultOnboardingItem
import com.itsaky.androidide.resources.R.color
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.tasks.runOnUiThread
import com.itsaky.androidide.utils.ConnectionInfo
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.getConnectionInfo

/**
 * @author Akash Yadav
 */
class OnboardingToolInstallationFragment : OnboardingMultiActionFragment(), SlidePolicy {

  private var cellularConnectionWarningView: LayoutChipWarningBinding? = null
  private var meteredConnectionWarningView: LayoutChipWarningBinding? = null
  private var backgroundDataRestrictedWarningView: LayoutChipWarningBinding? = null
  private var noConnectionErrorView: LayoutChipErrorBinding? = null

  private var backgroundDataRestrictionReceiver: BroadcastReceiver? = null
  private var networkStateChangeCallback: NetworkCallback? = null

  companion object {

    @JvmStatic
    fun newInstance(context: Context): OnboardingToolInstallationFragment {
      return OnboardingToolInstallationFragment().also {
        it.arguments = Bundle().apply {
          putCharSequence(KEY_ONBOARDING_TITLE, context.getString(R.string.title_install_tools))
          putCharSequence(KEY_ONBOARDING_SUBTITLE,
            context.getString(R.string.subtitle_install_tools))
          putCharSequence(KEY_ONBOARDING_EXTRA_INFO,
            Html.fromHtml(context.getString(R.string.msg_install_tools),
              Html.FROM_HTML_MODE_COMPACT))

          val arr = arrayOf(
            DefaultOnboardingItem("jdk", context.getString(string.tool_jdk_title),
              context.getString(string.tool_jdk_summary),
              drawable.ic_java_colored),
            DefaultOnboardingItem("sdk", context.getString(string.toolg_sdk_title),
              context.getString(string.tool_sdk_summary), R.drawable.ic_android,
              ContextCompat.getColor(context, color.color_android))
          )
          putParcelableArray(KEY_ACTION_ITEMS, arr)
        }
      }
    }
  }

  override fun createContentView(parent: ViewGroup, attachToParent: Boolean) {
    super.createContentView(parent, attachToParent)
    recyclerView?.adapter = createAdapter()
    updateConnectionStatus()
  }

  override fun onStart() {
    super.onStart()

    val connectivityManager = requireContext().getSystemService<ConnectivityManager>() ?: return
    networkStateChangeCallback?.also {
      connectivityManager.registerDefaultNetworkCallback(it)
    }

    networkStateChangeCallback = object : NetworkCallback() {

      override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
      ) {
        updateConnectionStatus(networkCapabilities)
      }

      override fun onLost(network: Network) {
        updateConnectionStatus(ConnectionInfo.UNKNOWN)
      }
    }

    val networkRequest = NetworkRequest.Builder()
      .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
      .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
      .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
      .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
      .build()
    connectivityManager.registerNetworkCallback(networkRequest, networkStateChangeCallback!!)

    backgroundDataRestrictionReceiver?.also {
      try {
        requireContext().unregisterReceiver(it)
      } catch (err: Throwable) { /*ignored*/
      }
    }

    backgroundDataRestrictionReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        updateConnectionStatus()
      }
    }

    requireContext().registerReceiver(backgroundDataRestrictionReceiver!!,
      IntentFilter(ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED))
  }

  override fun onStop() {
    super.onStop()

    networkStateChangeCallback?.also {
      requireContext().getSystemService<ConnectivityManager>()?.unregisterNetworkCallback(it)
      networkStateChangeCallback = null
    }

    backgroundDataRestrictionReceiver?.also {
      requireContext().unregisterReceiver(it)
      backgroundDataRestrictionReceiver = null
    }
  }

  private fun View.removeFromParent() {
    (parent as? ViewGroup?)?.removeView(this)
  }

  private fun updateConnectionStatus(networkCapabilities: NetworkCapabilities? = null) =
    updateConnectionStatus(getConnectionInfo(requireContext(), networkCapabilities))

  private fun updateConnectionStatus(connectionInfo: ConnectionInfo) = runOnUiThread {
    noConnectionErrorView?.root?.removeFromParent()
    cellularConnectionWarningView?.root?.removeFromParent()
    meteredConnectionWarningView?.root?.removeFromParent()
    backgroundDataRestrictedWarningView?.root?.removeFromParent()

    if (connectionInfo === ConnectionInfo.UNKNOWN || !connectionInfo.isConnected) {
      addNotConnectedWarning()
      return@runOnUiThread
    }

    if (connectionInfo.isCellularTransport) {
      addCellularTransportWarning()
    }

    if (connectionInfo.isMeteredConnection && !connectionInfo.isCellularTransport) {
      addMeteredConnectionWarning()
    }

    if (connectionInfo.isBackgroundDataRestricted) {
      addBackgroundDataRestrictedWarning()
    }
  }

  private fun addBackgroundDataRestrictedWarning() {
    backgroundDataRestrictedWarningView = backgroundDataRestrictedWarningView
      ?: LayoutChipWarningBinding.inflate(layoutInflater, binding.contentContainer, false)
    configureWarningOrErrorView(backgroundDataRestrictedWarningView!!.root,
      getString(R.string.msg_disable_background_data_restriction))
  }

  private fun addMeteredConnectionWarning() {
    meteredConnectionWarningView = meteredConnectionWarningView ?: LayoutChipWarningBinding.inflate(
      layoutInflater, binding.contentContainer, false)
    configureWarningOrErrorView(meteredConnectionWarningView!!.root,
      getString(R.string.msg_connected_to_metered_connection))
  }

  private fun addCellularTransportWarning() {
    cellularConnectionWarningView = cellularConnectionWarningView
      ?: LayoutChipWarningBinding.inflate(layoutInflater, binding.contentContainer, false)

    configureWarningOrErrorView(cellularConnectionWarningView!!.root,
      getString(R.string.msg_connected_to_cellular))
  }

  private fun addNotConnectedWarning() {
    noConnectionErrorView = noConnectionErrorView ?: LayoutChipErrorBinding.inflate(layoutInflater,
      binding.contentContainer, false)
    val msg = SpannableStringBuilder(getString(R.string.msg_no_internet))
    msg.append(" ")
    msg.append(getString(R.string.action_open_settings), URLSpan(""),
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    noConnectionErrorView!!.root.setOnClickListener {
      it.context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }

    configureWarningOrErrorView(noConnectionErrorView!!.root, msg)
  }

  private fun configureWarningOrErrorView(root: MaterialButton, msg: CharSequence) {
    root.apply {
      text = msg
      updateLayoutParams<MarginLayoutParams> {
        val dp8 = SizeUtils.dp2px(4f)
        setMargins(dp8)

        width = ViewGroup.LayoutParams.MATCH_PARENT
      }

      if (parent == null) {
        binding.contentContainer.addView(this, 0)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    backgroundDataRestrictionReceiver = null
    networkStateChangeCallback = null

    cellularConnectionWarningView = null
    meteredConnectionWarningView = null
    backgroundDataRestrictedWarningView = null
    noConnectionErrorView = null
  }

  override val isPolicyRespected: Boolean
    get() = getConnectionInfo(requireContext()).isConnected

  override fun onUserIllegallyRequestedNextPage() {
    flashError(string.msg_no_internet)
  }
}