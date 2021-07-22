package com.itsaky.androidide.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.content.Context;

public class ProjectTemplate {
	
	private int id;
	private String name;
	private String description;
	
	@DrawableRes
	private int imageId;
	
	public ProjectTemplate() {
	}

	public ProjectTemplate(int id, String name, String description, int imageId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
	}
	
	public ProjectTemplate setId(int id) {
		this.id = id;
		return this;
	}

	public int getId() {
		return id;
	}
	
	public ProjectTemplate setName(Context ctx, @StringRes int name) {
		return setName(ctx.getString(name));
	}

	public ProjectTemplate setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}
	
	public ProjectTemplate setDescription(Context ctx, @StringRes int desc) {
		return setDescription(ctx.getString(desc));
	}

	public ProjectTemplate setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ProjectTemplate setImageId(@DrawableRes int imageId) {
		this.imageId = imageId;
		return this;
	}

	public int getImageId() {
		return imageId;
	}
}
