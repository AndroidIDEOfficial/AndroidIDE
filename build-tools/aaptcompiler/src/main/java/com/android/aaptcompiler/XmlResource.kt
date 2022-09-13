package com.android.aaptcompiler

import com.android.aapt.Resources

data class XmlResource (
  val file: ResourceFile, val xmlProto: Resources.XmlNode)