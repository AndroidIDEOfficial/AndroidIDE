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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.updatePaddingRelative
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.BuildConfig
import com.itsaky.androidide.R
import com.itsaky.androidide.adapters.SimpleIconTitleDescriptionAdapter
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.app.EdgeToEdgeIDEActivity
import com.itsaky.androidide.app.configuration.IDEBuildConfigProvider
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.databinding.ActivityAboutBinding
import com.itsaky.androidide.models.IconTitleDescriptionItem
import com.itsaky.androidide.models.SimpleIconTitleDescriptionItem
import com.itsaky.androidide.utils.BuildInfoUtils
import com.itsaky.androidide.utils.flashSuccess
import com.itsaky.androidide.utils.resolveAttr

class AboutActivity : EdgeToEdgeIDEActivity() {

  private var _binding: ActivityAboutBinding? = null
  private val binding: ActivityAboutBinding
    get() = checkNotNull(_binding) {
      "Activity has been destroyed"
    }

  override fun bindLayout(): View {
    _binding = ActivityAboutBinding.inflate(layoutInflater)
    return _binding!!.root
  }

  companion object {

    private var id = 0
    private val ACTION_WEBSITE = id++
    private val ACTION_EMAIL = id++
    private val ACTION_TG_CHANNEL = id++
    private val ACTION_TG_GROUP = id++
    private val ACTION_CONTRIBUTE = id++
    private val ACTION_CONTRIBUTORS = id++
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.apply {

      setSupportActionBar(toolbar)
      supportActionBar!!.setDisplayHomeAsUpEnabled(true)
      supportActionBar!!.setTitle(R.string.about)
      toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

      aboutHeader.apply {
        ideVersion.text = createVersionText()
        ideVersion.isClickable = true
        ideVersion.isFocusable = true
        ideVersion.setBackgroundResource(R.drawable.bg_ripple)
        ideVersion.setOnClickListener {
          ClipboardUtils.copyText(BuildInfoUtils.getBuildInfoHeader())
          flashSuccess(R.string.copied)
        }
      }

      socials.apply {
        sectionTitle.setText(R.string.title_socials)
        sectionItems.adapter = AboutSocialItemsAdapter(createSocialItems(), ::handleActionClick)
      }

      misc.apply {
        sectionTitle.setText(R.string.title_misc)
        sectionItems.adapter = AboutSocialItemsAdapter(createMiscItems(), ::handleActionClick)
      }
    }
  }

  override fun onApplySystemBarInsets(insets: Insets) {
    binding.toolbar.apply {
      setPaddingRelative(
        paddingStart + insets.left,
        paddingTop,
        paddingEnd + insets.right,
        paddingBottom
      )
    }
  }

  private fun handleActionClick(action: SimpleIconTitleDescriptionItem) {
    when (action.id) {
      ACTION_WEBSITE -> app.openWebsite()
      ACTION_EMAIL -> app.emailUs()
      ACTION_TG_GROUP -> app.openTelegramGroup()
      ACTION_TG_CHANNEL -> app.openTelegramChannel()
      ACTION_CONTRIBUTE -> app.openUrl(BaseApplication.CONTRIBUTOR_GUIDE_URL)
      ACTION_CONTRIBUTORS -> startActivity(Intent(this, ContributorsActivity::class.java))
    }
  }

  private fun createSocialItems(): List<IconTitleDescriptionItem> {
    return mutableListOf<IconTitleDescriptionItem>().apply {
      add(
        createSimpleIconTextItem(
          this@AboutActivity,
          ACTION_WEBSITE,
          R.drawable.ic_website,
          R.string.about_option_website,
          BuildInfo.PROJECT_SITE
        )
      )
      add(
        createSimpleIconTextItem(
          this@AboutActivity,
          ACTION_EMAIL,
          R.drawable.ic_email,
          R.string.about_option_email,
          BaseApplication.EMAIL
        )
      )
      add(
        createSimpleIconTextItem(
          this@AboutActivity,
          ACTION_TG_GROUP,
          R.drawable.ic_telegram,
          R.string.discussions_on_telegram,
          BaseApplication.TELEGRAM_GROUP_URL
        )
      )
      add(
        createSimpleIconTextItem(
          this@AboutActivity,
          ACTION_TG_CHANNEL,
          R.drawable.ic_telegram,
          R.string.official_tg_channel,
          BaseApplication.TELEGRAM_CHANNEL_URL
        )
      )
    }
  }

  private fun createMiscItems(): List<IconTitleDescriptionItem> {
    return mutableListOf<IconTitleDescriptionItem>().apply {
      add(
        SimpleIconTitleDescriptionItem.create(
          this@AboutActivity,
          ACTION_CONTRIBUTE,
          R.drawable.ic_code,
          R.string.title_contribute,
          R.string.summary_contribute
        )
      )
      add(
        SimpleIconTitleDescriptionItem.create(
          this@AboutActivity,
          ACTION_CONTRIBUTORS,
          R.drawable.ic_heart_outline,
          R.string.title_contributors,
          R.string.summary_contributors
        )
      )
    }
  }

  private fun createSimpleIconTextItem(
    context: Context,
    id: Int,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    description: CharSequence
  ): SimpleIconTitleDescriptionItem {
    return SimpleIconTitleDescriptionItem(
      id,
      ContextCompat.getDrawable(context, icon),
      ContextCompat.getString(context, title),
      description
    )
  }

  /**
   * Create the version name string that should be displayed to the user.
   *
   * Format of the version name string is :
   *
   * `v[version-name]-[variant] ([build-type]/[[UN]OFFICIAL])`
   */
  @Suppress("KDocUnresolvedReference")
  private fun createVersionText(): CharSequence {
    val builder = SpannableStringBuilder()
    builder.append("v")
    builder.append(BuildInfo.VERSION_NAME_SIMPLE)
    builder.append("-")
    builder.append(IDEBuildConfigProvider.getInstance().cpuAbiName)
    builder.append(" ")

    val colorPositive = ContextCompat.getColor(this, R.color.color_success)
    val colorNegative = ContextCompat.getColor(this, R.color.color_error)

    appendBuildType(builder, colorPositive, colorNegative)

    return builder
  }

  private fun appendBuildType(
    builder: SpannableStringBuilder,
    @ColorInt
    colorPositive: Int,
    @ColorInt
    colorNegative: Int
  ) {
    @Suppress("KotlinConstantConditions")
    var color = if (BuildConfig.BUILD_TYPE != "release") {
      colorNegative
    } else {
      colorPositive
    }

    builder.append("(")
    appendForegroundSpan(builder, BuildConfig.BUILD_TYPE, color)

    val isOfficialBuild = BuildInfoUtils.isOfficialBuild(this)

    color = if (isOfficialBuild) {
      colorPositive
    } else {
      colorNegative
    }

    builder.append("/")
    appendForegroundSpan(
      builder,
      BuildInfoUtils.getBuildType(this).lowercase(),
      color
    )

    builder.append(")")
  }

  private fun appendForegroundSpan(
    builder: SpannableStringBuilder,
    text: CharSequence,
    color: Int
  ) {
    builder.append(
      text,
      ForegroundColorSpan(color),
      SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  class AboutSocialItemsAdapter(
    items: List<IconTitleDescriptionItem>,
    private val onClickListener: (SimpleIconTitleDescriptionItem) -> Unit
  ) : SimpleIconTitleDescriptionAdapter(items) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      super.onBindViewHolder(holder, position)
      val binding = holder.binding
      val item = getItem(position) as SimpleIconTitleDescriptionItem
      val dp8 = SizeUtils.dp2px(8f)
      binding.icon.updatePaddingRelative(dp8, dp8, dp8, dp8)
      binding.title.setTextAppearance(R.style.TextAppearance_Material3_TitleSmall)

      binding.description.maxLines = 3
      binding.description.setTextAppearance(R.style.TextAppearance_Material3_BodySmall)
      binding.description.setTextColor(binding.description.context.resolveAttr(R.attr.colorPrimary))

      binding.root.isClickable = true
      binding.root.isFocusable = true
      binding.root.setBackgroundResource(R.drawable.bg_ripple)
      binding.root.setOnClickListener {
        onClickListener(item)
      }
    }
  }
}
