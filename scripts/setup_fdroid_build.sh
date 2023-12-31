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

versionName="$1" # Format: $major.$minor.$patch-$type-$abi e.g. 2.7.0-beta-arm64
versionCode="$2" # Format: $major$minor$patch e.g. 270

# Android defaultConfig
DEFCONFIG_REPLACE_BEGIN="FDROID_PREBUILD_DEFCONFIG_REPLACE_BEGIN"
DEFCONFIG_REPLACE_END="FDROID_PREBUILD_DEFCONFIG_REPLACE_END"

# settings.gradle
SETTINGS_GRADLE_REPLACE_BEGIN="FDROID_PREBUILD_SETTINGS_REPLACE_BEGIN"
SETTINGS_GRADLE_REPLACE_END="FDROID_PREBUILD_SETTINGS_REPLACE_END"

# Android productFlavors
FLAVORS_INSERT="FDROID_PREBUILD_FLAVORS_INSERT"

# File to update
build_gradle="build.gradle.kts"
gradle_props="../fdroid.properties"
settings_gradle="../settings.gradle.kts"

flavorDef="productFlavors {\n"

i=1
for arch in arm64-v8a armeabi-v7a x86_64; do
  flavorDef+="create(\"$arch\") { \n"
  flavorDef+="  versionNameSuffix = \"-fdroid\" \n"
  flavorDef+="  versionCode = 100 * $versionCode + $i \n"
  flavorDef+="}\n"
  i=$((i + 1))
done

flavorDef+="}"

# settings.gradle
settingsDef=$(
  cat <<-END
val propsFile = File(rootDir, "fdroid.properties")
if (!propsFile.exists() || !propsFile.isFile) {
  throw GradleException("fdroid.properties file does not exist or is not a file")
}

val properties = propsFile.let { props ->
    java.util.Properties().also {
      it.load(props.reader())
    }
  }

val isFDroidBuild = properties.getProperty("ide.build.fdroid", "true").toBoolean()

val fdroidVersionName = requireNotNull(properties.getProperty("ide.build.fdroid.version", null)) {
  "'ide.build.fdroid' is 'true' but no 'ide.build.fdroid.version' is defined!"
}

gradle.rootProject { project.setProperty("version", fdroidVersionName) }
END

)

# Replace properties in defaultConfig with constants
sed -i "/\/\/ @@$DEFCONFIG_REPLACE_BEGIN@@/,/\/\/ @@$DEFCONFIG_REPLACE_END@@/c\
    applicationId = \"com.itsaky.androidide\" \n \
    versionName = \"$versionName\" \n \
    versionCode = $versionCode \n" $build_gradle

# Add product flavors
sed -i "/\/\/ @@$FLAVORS_INSERT@@/c\
    $flavorDef" $build_gradle

# Remove the Nyx plugin from settings.gradle
awk -v begin="$SETTINGS_GRADLE_REPLACE_BEGIN" -v end="$SETTINGS_GRADLE_REPLACE_END" -v replacement="$settingsDef" '
    $0 ~ ("// @@" begin "@@") {
        printing = 1;
        print replacement;
        next;
    }
    printing && $0 ~ ("// @@" end "@@") {
        printing = 0;
        next;
    }
    !printing
' $settings_gradle >temp_settings.gradle && mv temp_settings.gradle $settings_gradle

# Write Gradle properties
echo "ide.build.fdroid=true" >"$gradle_props"
echo "ide.build.fdroid.version=${versionName}" >>"$gradle_props"
echo "ide.build.fdroid.vercode=${versionCode}" >>"$gradle_props"
