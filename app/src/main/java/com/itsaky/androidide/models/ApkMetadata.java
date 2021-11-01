package com.itsaky.androidide.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ApkMetadata {
    
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

    public void setVersion(int version) {
        this.version = version;
    }

    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public int getVersion() {
        return version;
    }

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getVariantName() {
        return variantName;
    }

    public List<Element> getElements() {
        return elements;
    }

    public String getElementType() {
        return elementType;
    }

    public static class ArtifactType {
        
        public static String TYPE_APK = "APK";
        
        @SerializedName("type")
        private String type;
        
        @SerializedName("kind")
        private String kind;
        
        public void setType(String type) {
            this.type = type;
        }
        
        public void setKind(String kind) {
            this.kind = kind;
        }
        
        public String getType() {
            return type;
        }
        
        public String getKind() {
            return kind;
        }
        
        @Override
        public String toString() {
            return "["+getClass().getCanonicalName()+"]"
             + "\ntype=" + type
             + ",\nkind=" + kind;
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
        
        public void setType(String type) {
            this.type = type;
        }
        
        public void setFilters(List<Object> filters) {
            this.filters = filters;
        }
        
        public void setAttributes(List<Object> attributes) {
            this.attributes = attributes;
        }
        
        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }
        
        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
        
        public void setOutputFile(String outputFile) {
            this.outputFile = outputFile;
        }
        
        public String getType() {
            return type;
        }
        
        public List<Object> getFilters() {
            return filters;
        }
        
        public List<Object> getAttributes() {
            return attributes;
        }
        
        public int getVersionCode() {
            return versionCode;
        }
        
        public String getVersionName() {
            return versionName;
        }
        
        public String getOutputFile() {
            return outputFile;
        }
        
        @Override
        public String toString() {
            return "["+getClass().getCanonicalName()+"]"
             + "\ntype=" + type
             + ",\nfilters=" + filters
             + ",\nattributes=" + attributes
             + ",\nversionCode=" + versionCode
             + ",\nversionName=" + versionName
             + ",\noutputFile=" + outputFile;
        }
    }

    @Override
    public String toString() {
        return "["+getClass().getCanonicalName()+"]"
         + "\nversion=" + version
         + ",\nartifactType=" + artifactType
         + ",\napplicationId=" + applicationId
         + ",\nvariantName=" + variantName
         + ",\nelements=" + elements
         + ",\nelementType=" + elementType;
    }

}
