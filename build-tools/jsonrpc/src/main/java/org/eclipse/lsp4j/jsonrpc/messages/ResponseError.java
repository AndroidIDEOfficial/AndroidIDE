/******************************************************************************
 * Copyright (c) 2016 TypeFox and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc.messages;

import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.json.adapters.JsonElementTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

import com.google.gson.annotations.JsonAdapter;

public class ResponseError {

	/**
	 * A number indicating the error type that occurred.
	 */
	@NonNull
	private int code;

	@NonNull
	public int getCode() {
		return this.code;
	}

	public void setCode(@NonNull int code) {
		this.code = code;
	}

	public void setCode(ResponseErrorCode code) {
		this.setCode(code.getValue());
	}

	/**
	 * A string providing a short description of the error.
	 */
	@NonNull
	private String message;

	@NonNull
	public String getMessage() {
		return this.message;
	}

	public void setMessage(@NonNull String message) {
		this.message = message;
	}

	/**
	 * A Primitive or Structured value that contains additional information
	 * about the error. Can be omitted.
	 */
	@JsonAdapter(JsonElementTypeAdapter.Factory.class)
	private Object data;

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResponseError() {
	}

	public ResponseError(ResponseErrorCode code, String message, Object data) {
		this(code.getValue(), message, data);
	}

	public ResponseError(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	@Override
	public String toString() {
		return MessageJsonHandler.toString(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponseError other = (ResponseError) obj;
		if (other.code != this.code)
			return false;
		if (this.message == null) {
			if (other.message != null)
				return false;
		} else if (!this.message.equals(other.message))
			return false;
		if (this.data == null) {
			if (other.data != null)
				return false;
		} else if (!this.data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.code;
		result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
		result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
		return result;
	}

}
