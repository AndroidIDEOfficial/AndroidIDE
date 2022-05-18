/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.models;

import static com.itsaky.androidide.utils.ILogger.newInstance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.JSONUtility;
import com.itsaky.androidide.utils.ListingFileRedirect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@SuppressWarnings("unused")
public class ApkMetadata {

  private static final ILogger LOG = newInstance("ApkMetadata");

  @SerializedName("version")
  private int version;

  @SerializedName("artifactType")
  private ArtifactType artifactType;

  @SerializedName("applicationId")
  private String applicationId;

  @SerializedName("variantName")
  private String variantName;

  @SerializedName("elements")
  private List<Element> elements;

  @SerializedName("elementType")
  private String elementType;

  @Nullable
  public static File findApkFile(@NonNull File listingFIle) {
    try {

      // This sometimes might be a redirect to the listing file
      // So we have to handle this case too.
      final var redirectedFile = ListingFileRedirect.maybeExtractRedirectedFile(listingFIle);
      if (redirectedFile != null) {
        listingFIle = redirectedFile;
      }

      final var dir = listingFIle.getParentFile();
      final var metadata =
          JSONUtility.gson.fromJson(new FileReader(listingFIle), ApkMetadata.class);
      if (!isValid(metadata)) {
        LOG.warn("Invalid APK metadata:", metadata);
        return null;
      }

      for (Element element : metadata.getElements()) {
        if (element == null || element.getOutputFile() == null) {
          LOG.warn("No output file specified in APK metadata element:", element);
          continue;
        }

        if (element.getOutputFile().endsWith(".apk")) {
          final File apk = new File(dir, element.getOutputFile());
          if (apk.exists() && apk.isFile()) {
            LOG.info("Found apk in metadata:", apk);
            return apk;
          }
        }
      }

      return null;
    } catch (FileNotFoundException e) {
      LOG.error("Metadata file not found...", e);
      return null;
    }
  }

  private static boolean isValid(ApkMetadata metadata) {

    // Null checks
    if (metadata == null
        || metadata.getArtifactType() == null
        || metadata.getArtifactType().getType() == null
        || metadata.getElements() == null) {
      LOG.warn("APK metadata null check failed. Metadata:", metadata);
      return false;
    }

    final ArtifactType type = metadata.getArtifactType();
    final List<Element> elements = metadata.getElements();

    if (!type.getType().equals(ArtifactType.TYPE_APK)) {
      LOG.warn("Artifact is not of type APK. Metadata:", metadata);
      return false;
    }

    if (elements.isEmpty()) {
      LOG.warn("No output elements found for metadata:", metadata);
      return false;
    }

    boolean atLeastOneApk = false;
    for (Element element : elements) {
      if (element == null
          || element.getOutputFile() == null
          || !element.getOutputFile().endsWith(".apk")) {
        LOG.warn("Skipping output element because file is not APK:", element);
        continue;
      }

      if (element.getOutputFile().endsWith(".apk")) {
        atLeastOneApk = true;
        break;
      }
    }

    LOG.debug("Output metadata validation succeeded");
    return atLeastOneApk;
  }

  public List<Element> getElements() {
    return elements;
  }

  public ArtifactType getArtifactType() {
    return artifactType;
  }

  public void setArtifactType(ArtifactType artifactType) {
    this.artifactType = artifactType;
  }

  public void setElements(List<Element> elements) {
    this.elements = elements;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getVariantName() {
    return variantName;
  }

  public void setVariantName(String variantName) {
    this.variantName = variantName;
  }

  public String getElementType() {
    return elementType;
  }

  public void setElementType(String elementType) {
    this.elementType = elementType;
  }

  @NonNull
  @Override
  public String toString() {
    return "ApkMetadata{"
        + "version="
        + version
        + ", artifactType="
        + artifactType
        + ", applicationId='"
        + applicationId
        + '\''
        + ", variantName='"
        + variantName
        + '\''
        + ", elements="
        + elements
        + ", elementType='"
        + elementType
        + '\''
        + '}';
  }

  public static class ArtifactType {

    public static String TYPE_APK = "APK";

    @SerializedName("type")
    private String type;

    @SerializedName("kind")
    private String kind;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getKind() {
      return kind;
    }

    public void setKind(String kind) {
      this.kind = kind;
    }

    @NonNull
    @Override
    public String toString() {
      return "ArtifactType{" + "type='" + type + '\'' + ", kind='" + kind + '\'' + '}';
    }
  }

  public static class Element {

    @SerializedName("type")
    private String type;

    @SerializedName("filters")
    private List<Object> filters;

    @SerializedName("attributes")
    private List<Object> attributes;

    @SerializedName("versionCode")
    private int versionCode;

    @SerializedName("versionName")
    private String versionName;

    @SerializedName("outputFile")
    private String outputFile;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<Object> getFilters() {
      return filters;
    }

    public void setFilters(List<Object> filters) {
      this.filters = filters;
    }

    public List<Object> getAttributes() {
      return attributes;
    }

    public void setAttributes(List<Object> attributes) {
      this.attributes = attributes;
    }

    public int getVersionCode() {
      return versionCode;
    }

    public void setVersionCode(int versionCode) {
      this.versionCode = versionCode;
    }

    public String getVersionName() {
      return versionName;
    }

    public void setVersionName(String versionName) {
      this.versionName = versionName;
    }

    public String getOutputFile() {
      return outputFile;
    }

    public void setOutputFile(String outputFile) {
      this.outputFile = outputFile;
    }

    @NonNull
    @Override
    public String toString() {
      return "Element{"
          + "type='"
          + type
          + '\''
          + ", filters="
          + filters
          + ", attributes="
          + attributes
          + ", versionCode="
          + versionCode
          + ", versionName='"
          + versionName
          + '\''
          + ", outputFile='"
          + outputFile
          + '\''
          + '}';
    }
  }
}
