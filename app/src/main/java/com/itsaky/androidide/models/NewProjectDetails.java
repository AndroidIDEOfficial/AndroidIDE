package com.itsaky.androidide.models;

public class NewProjectDetails {
	
	public String name;
	public String packageName;
	public int minSdk;
	public int targetSdk;
	
	public NewProjectDetails(){}

	public NewProjectDetails(String name, String packageName, int minSdk, int targetSdk) {
		this.name = name;
		this.packageName = packageName;
		this.minSdk = minSdk;
		this.targetSdk = targetSdk;
	}

	public NewProjectDetails setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public NewProjectDetails setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public String getPackageName() {
		return packageName;
	}

	public NewProjectDetails setMinSdk(int minSdk) {
		this.minSdk = minSdk;
		return this;
	}

	public int getMinSdk() {
		return minSdk;
	}

	public NewProjectDetails setTargetSdk(int targetSdk) {
		this.targetSdk = targetSdk;
		return this;
	}

	public int getTargetSdk() {
		return targetSdk;
	}
}
