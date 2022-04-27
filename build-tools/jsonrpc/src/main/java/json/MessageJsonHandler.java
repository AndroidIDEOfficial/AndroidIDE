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
package org.eclipse.lsp4j.jsonrpc.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.lsp4j.jsonrpc.MessageIssueException;
import org.eclipse.lsp4j.jsonrpc.json.adapters.CollectionTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.json.adapters.EitherTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.json.adapters.EnumTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.json.adapters.MessageTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.json.adapters.ThrowableTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.json.adapters.TupleTypeAdapters;
import org.eclipse.lsp4j.jsonrpc.messages.CancelParams;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.eclipse.lsp4j.jsonrpc.messages.MessageIssue;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

/**
 * A wrapper around Gson that includes configuration required for JSON-RPC messages.
 */
public class MessageJsonHandler {
	
	public static final JsonRpcMethod CANCEL_METHOD = JsonRpcMethod.notification("$/cancelRequest", CancelParams.class);

	private final Gson gson;
	
	private final Map<String, JsonRpcMethod> supportedMethods;
	
	private MethodProvider methodProvider;
	
	/**
	 * @param supportedMethods - a map used to resolve RPC methods in {@link #getJsonRpcMethod(String)}
	 */
	public MessageJsonHandler(Map<String, JsonRpcMethod> supportedMethods) {
		this.supportedMethods = supportedMethods;
		this.gson = getDefaultGsonBuilder().create();
	}
	
	/**
	 * @param supportedMethods - a map used to resolve RPC methods in {@link #getJsonRpcMethod(String)}
	 * @param configureGson - a function that contributes to the GsonBuilder created by {@link #getDefaultGsonBuilder()}
	 */
	public MessageJsonHandler(Map<String, JsonRpcMethod> supportedMethods, Consumer<GsonBuilder> configureGson) {
		this.supportedMethods = supportedMethods;
		GsonBuilder gsonBuilder = getDefaultGsonBuilder();
		configureGson.accept(gsonBuilder);
		this.gson = gsonBuilder.create();
	}
	
	/**
	 * Create a {@link GsonBuilder} with default settings for parsing JSON-RPC messages.
	 */
	public GsonBuilder getDefaultGsonBuilder() {
		return new GsonBuilder()
			.registerTypeAdapterFactory(new CollectionTypeAdapter.Factory())
			.registerTypeAdapterFactory(new ThrowableTypeAdapter.Factory())
			.registerTypeAdapterFactory(new EitherTypeAdapter.Factory())
			.registerTypeAdapterFactory(new TupleTypeAdapters.TwoTypeAdapterFactory())
			.registerTypeAdapterFactory(new EnumTypeAdapter.Factory())
			.registerTypeAdapterFactory(new MessageTypeAdapter.Factory(this));
	}
	
	public Gson getGson() {
		return gson;
	}
	
	/**
	 * Resolve an RPC method by name.
	 */
	public JsonRpcMethod getJsonRpcMethod(String name) {
		JsonRpcMethod result = supportedMethods.get(name);
		if (result != null)
			return result;
		else if (CANCEL_METHOD.getMethodName().equals(name))
			return CANCEL_METHOD;
		return null;
	}
	
	public MethodProvider getMethodProvider() {
		return methodProvider;
	}
	
	public void setMethodProvider(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}
	
	public Message parseMessage(CharSequence input) throws JsonParseException {
		StringReader reader = new StringReader(input.toString());
		return parseMessage(reader);
	}
	
	public Message parseMessage(Reader input) throws JsonParseException {
		JsonReader jsonReader = new JsonReader(input);
		Message message = gson.fromJson(jsonReader, Message.class);
		
		if (message != null) {
			// Check whether the input has been fully consumed
			try {
				if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
					MessageIssue issue = new MessageIssue("JSON document was not fully consumed.", ResponseErrorCode.ParseError.getValue());
					throw new MessageIssueException(message, issue);
				}
			} catch (MalformedJsonException e) {
				MessageIssue issue = new MessageIssue("Message could not be parsed.", ResponseErrorCode.ParseError.getValue(), e);
				throw new MessageIssueException(message, issue);
			} catch (IOException e) {
				throw new JsonIOException(e);
			}
		}
		return message;
	}
	
	public String serialize(Message message) {
		StringWriter writer = new StringWriter();
		serialize(message, writer);
		return writer.toString();
	}
	
	public void serialize(Message message, Writer output) throws JsonIOException {
		gson.toJson(message, Message.class, output);
	}
	
	
	private static MessageJsonHandler toStringInstance;
	
	/**
	 * Perform JSON serialization of the given object using the default configuration of JSON-RPC messages
	 * enhanced with the pretty printing option.
	 */
	public static String toString(Object object) {
		if (toStringInstance == null) {
			toStringInstance = new MessageJsonHandler(Collections.emptyMap(), gsonBuilder -> {
				gsonBuilder.setPrettyPrinting();
			});
		}
		return toStringInstance.gson.toJson(object);
	}
	
}
