#!/bin/bash
#
#  This file is part of AndroidIDE.
#
#  AndroidIDE is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  AndroidIDE is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
#

set -eu

# Arguments are positional
# 1 - version name
# 2 - version code

versionName="$1"
versionCode="$2"

# Android defaultConfig
DEFCONFIG_REPLACE_BEGIN="FDROID_PREBUILD_DEFCONFIG_REPLACE_BEGIN"
DEFCONFIG_REPLACE_END="FDROID_PREBUILD_DEFCONFIG_REPLACE_END"

# Android productFlavors
FLAVORS_INSERT="FDROID_PREBUILD_FLAVORS_INSERT"

# File to update
target_file="build.gradle.kts"

flavorDef="productFlavors {\n"

i=1
for arch in arm64-v8a armeabi-v7a x86_64; do
  flavorDef+="create(\"$arch\") { \n"
  flavorDef+="  versionNameSuffix = \"$arch\" \n"
  flavorDef+="  versionCode = 100 * $versionCode + $i \n"
  flavorDef+="}\n"
  i=$(( i + 1))
done

flavorDef+="}"

# Replace properties in defaultConfig with constants
sed -i "/\/\/ @@$DEFCONFIG_REPLACE_BEGIN@@/,/\/\/ @@$DEFCONFIG_REPLACE_END@@/c\
    applicationId = \"com.itsaky.androidide\" \n \
    versionName = \"$versionName\" \n \
    versionCode = $versionCode \n" $target_file

# Add product flavors
sed -i "/\/\/ @@$FLAVORS_INSERT@@/c\
    $flavorDef" $target_file