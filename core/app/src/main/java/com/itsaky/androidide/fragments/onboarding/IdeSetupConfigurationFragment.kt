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

import android.annotation.SuppressLint
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
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import com.github.appintro.SlidePolicy
import com.itsaky.androidide.R
import com.itsaky.androidide.databinding.LayoutOnboardngSetupConfigBinding
import com.itsaky.androidide.models.IdeSetupArgument
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.tasks.runOnUiThread
import com.itsaky.androidide.utils.ConnectionInfo
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.getConnectionInfo

/**
 * @author Akash Yadav
 */
class IdeSetupConfigurationFragment : OnboardingFragment(), SlidePolicy {

  private var _content: LayoutOnboardngSetupConfigBinding? = null
  private val content: LayoutOnboardngSetupConfigBinding
    get() = checkNotNull(_content) { "Fragment has been destroyed" }

  private var backgroundDataRestrictionReceiver: BroadcastReceiver? = null
  private var networkStateChangeCallback: NetworkCallback? = null

  companion object {

    @JvmStatic
    fun newInstance(context: Context): IdeSetupConfigurationFragment {
      return IdeSetupConfigurationFragment().also {
        it.arguments = Bundle().apply {
          putCharSequence(KEY_ONBOARDING_TITLE, context.getString(R.string.title_install_tools))
          putCharSequence(KEY_ONBOARDING_SUBTITLE,
            context.getString(R.string.subtitle_install_tools))
          putCharSequence(KEY_ONBOARDING_EXTRA_INFO,
            Html.fromHtml(context.getString(R.string.msg_install_tools),
              Html.FROM_HTML_MODE_COMPACT))
        }
      }
    }
  }

  @SuppressLint("PrivateResource")
  override fun createContentView(parent: ViewGroup, attachToParent: Boolean) {
    _content = LayoutOnboardngSetupConfigBinding.inflate(layoutInflater, parent, attachToParent)

    content.apply {
      noConnection.root.setText(R.string.msg_no_internet)
      cellularConnection.root.setText(R.string.msg_connected_to_cellular)
      meteredConnection.root.setText(R.string.msg_connected_to_metered_connection)
      backgroundDataRestricted.root.setText(R.string.msg_disable_background_data_restriction)

      autoInstallSwitch.setOnCheckedChangeListener { button, isChecked ->
        button.setText(
          if (isChecked) R.string.action_auto_install else R.string.action_manual_install)
        sdkVersionLayout.isEnabled = isChecked
        jdkVersionLayout.isEnabled = isChecked
        installGit.isEnabled = isChecked
        installOpenssh.isEnabled = isChecked
      }

      val sdkVersions = SdkVersion.entries.map { "SDK ${it.version}" }.reversed()
      sdkVersion.setText(sdkVersions[0])
      sdkVersion.setAdapter(ArrayAdapter(
        requireContext(),
        com.google.android.material.R.layout.m3_auto_complete_simple_item,
        sdkVersions)
      )

      val jdkVersions = JdkVersion.entries.map { "JDK ${it.version}" }
      jdkVersion.setText(jdkVersions[0])
      jdkVersion.setAdapter(ArrayAdapter(
        requireContext(),
        com.google.android.material.R.layout.m3_auto_complete_simple_item,
        jdkVersions)
      )
    }

    updateConnectionStatus()
  }

  fun isAutoInstall(): Boolean = content.autoInstallSwitch.isChecked

  fun buildIdeSetupArguments(): Array<String> {
    val args = mutableListOf<String>()
    args.setArgument(IdeSetupArgument.INSTALL_DIR, Environment.HOME.absolutePath)
    args.setArgument(IdeSetupArgument.SDK_VERSION,
      SdkVersion.fromDisplayName(content.sdkVersion.text).version)
    args.setArgument(IdeSetupArgument.JDK_VERSION,
      JdkVersion.fromDisplayName(content.jdkVersion.text).version)
    args.setArgument(IdeSetupArgument.ASSUME_YES)
    if (content.installGit.isChecked) {
      args.setArgument(IdeSetupArgument.WITH_GIT)
    }
    if (content.installOpenssh.isChecked) {
      args.setArgument(IdeSetupArgument.WITH_OPENSSH)
    }
    return args.toTypedArray()
  }

  private fun MutableList<String>.setArgument(argument: IdeSetupArgument, value: Any? = null) {
    val strVal = value?.toString() ?: ""
    if (argument.requiresValue && strVal.isBlank()) {
      throw IllegalArgumentException("${argument.name} requires a value")
    }

    add(argument.argumentName)
    add(strVal)
  }

  override fun onStart() {
    super.onStart()
    updateConnectionStatus()
    monitorNetworkState()
  }

  override fun onStop() {
    super.onStop()
    removeNetworkMonitors()
  }

  private fun updateConnectionStatus(networkCapabilities: NetworkCapabilities? = null) =
    updateConnectionStatus(getConnectionInfo(requireContext(), networkCapabilities))

  private fun updateConnectionStatus(connectionInfo: ConnectionInfo) = runOnUiThread {
    content.noConnection.root.isVisible = false
    content.cellularConnection.root.isVisible = false
    content.meteredConnection.root.isVisible = false
    content.backgroundDataRestricted.root.isVisible = false

    if (connectionInfo === ConnectionInfo.UNKNOWN || !connectionInfo.isConnected) {
      showNoConnectionWarning()
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
    content.backgroundDataRestricted.root.apply {
      setText(R.string.msg_disable_background_data_restriction)
      isVisible = true
    }
  }

  private fun addMeteredConnectionWarning() {
    content.meteredConnection.root.apply {
      setText(R.string.msg_connected_to_metered_connection)
      isVisible = true
    }
  }

  private fun addCellularTransportWarning() {
    content.cellularConnection.root.apply {
      setText(R.string.msg_connected_to_cellular)
      isVisible = true
    }
  }

  private fun showNoConnectionWarning() {
    val msg = SpannableStringBuilder(getString(R.string.msg_no_internet))
    msg.append(" ")
    msg.append(getString(R.string.action_open_settings), URLSpan(""),
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    content.noConnection.root.apply {
      isVisible = true
      setOnClickListener {
        it.context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    backgroundDataRestrictionReceiver = null
    networkStateChangeCallback = null
  }

  override val isPolicyRespected: Boolean
    get() = getConnectionInfo(requireContext()).isConnected

  override fun onUserIllegallyRequestedNextPage() {
    flashError(string.msg_no_internet)
  }

  private fun monitorNetworkState() {
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

  private fun removeNetworkMonitors() {
    networkStateChangeCallback?.also {
      requireContext().getSystemService<ConnectivityManager>()?.unregisterNetworkCallback(it)
      networkStateChangeCallback = null
    }

    backgroundDataRestrictionReceiver?.also {
      requireContext().unregisterReceiver(it)
      backgroundDataRestrictionReceiver = null
    }
  }

}