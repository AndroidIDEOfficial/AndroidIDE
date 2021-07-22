package com.itsaky.androidide.interfaces;

public interface Filter<T> {
	
	boolean accept(T t);
}
