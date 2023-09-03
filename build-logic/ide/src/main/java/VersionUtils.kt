import org.gradle.api.GradleException
import java.io.BufferedInputStream
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

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

/**
 * @author Akash Yadav
 */
object VersionUtils {

  /**
   * The Sonatype snapshots repository.
   */
  const val SNAPSHOTS_REPO = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

  /**
   * The latest integration version name.
   */
  const val LATEST_INTEGRATION = "latest.integration"

  /**
   * Gets the latest snapshot version of the given artifact from the Sonatype snapshots repository.
   */
  @JvmStatic
  fun getLatestSnapshotVersion(artifact: String): String {
    val groupId = BuildConfig.packageName.replace('.', '/')
    val moduleMetadata = "${SNAPSHOTS_REPO}/$groupId/${artifact}/maven-metadata.xml"
    return try {
       BufferedInputStream(URI.create(moduleMetadata).toURL().openStream()).use { inputStream ->
        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val document = builder.parse(inputStream)

        val xPathFactory = XPathFactory.newInstance()
        val xPath = xPathFactory.newXPath()

        val latestVersion = xPath.evaluate("/metadata/versioning/latest", document)
        println("Found latest version of artifact '$artifact' : '$latestVersion'")
        return@use latestVersion
      }
    } catch (err: Throwable) {
      throw GradleException("Failed to download $moduleMetadata", err)
    }
  }
}