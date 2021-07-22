package com.itsaky.androidide.models.gradle;

import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.utils.Environment;
import java.io.File;
import org.json.JSONObject;

public class Artifact {
	
	public final String group;
	public final String name;
	public final String version;
	
	private Artifact () {
		this(null);
	}
	
	private Artifact(String artifact) {
		this(group(artifact), name(artifact), version(artifact));
	}
	
	private Artifact(String group, String name, String version) {
		if(group == null
		|| name == null
		|| version == null)
			throw new IllegalArgumentException(String.format("Artifact%s values cannot be null", toString()));
		this.group = group;
		this.name = name;
		this.version = version;
	}
	
	public boolean isValid() {
		return group != null
			&& name != null
			&& version != null
			&& !group.isEmpty()
			&& !name.isEmpty()
			&& !version.isEmpty();
	}
	
	public File folder() {
		return Environment.cachedFolderForArtifact(this);
	}
	
	public static Artifact from(JSONObject obj) {
		try {
			return Artifact.from(obj.getString("requested"));
		} catch (Throwable th) {
			return null;
		}
	}
	
	public static Artifact from(String artifact) {
		return new Artifact(artifact);
	}
	
	public static Artifact copy(Artifact src) {
		return new Artifact(src.group, src.name, src.version);
	}
	
	private static String group(String artifact) {
		return artifact.split(":")[0];
	}
	
	private static String name(String artifact) {
		return artifact.split(":")[1];
	}
	
	private static String version(String artifact) {
		return artifact.split(":")[2];
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Artifact) {
			Artifact that = (Artifact) obj;
			return this.group.equals(that.group)
			&& this.name.equals(that.name)
			&& this.version.equals(that.version);
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("[%s, %s, %s]", group, name, version);
	}
}
