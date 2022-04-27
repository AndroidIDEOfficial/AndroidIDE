/******************************************************************************
 * Copyright (c) 2016-2019 TypeFox and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc;

import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.json.StreamMessageConsumer;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.eclipse.lsp4j.jsonrpc.messages.NotificationMessage;
import org.eclipse.lsp4j.jsonrpc.messages.RequestMessage;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseMessage;

import java.io.PrintWriter;
import java.time.Clock;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * A {@link MessageConsumer} that outputs logs in a format that can be parsed by the LSP Inspector.
 * https://microsoft.github.io/language-server-protocol/inspector/
 */
public class TracingMessageConsumer implements MessageConsumer {
	private static final Logger LOG = Logger.getLogger(TracingMessageConsumer.class.getName());

	private final MessageConsumer messageConsumer;
	private final Map<String, RequestMetadata> sentRequests;
	private final Map<String, RequestMetadata> receivedRequests;
	private final PrintWriter printWriter;
	private final Clock clock;
	private final DateTimeFormatter dateTimeFormatter;

	/**
	 * @param messageConsumer The {@link MessageConsumer} to wrap.
	 * @param sentRequests A map that keeps track of pending sent request data.
	 * @param receivedRequests A map that keeps track of pending received request data.
	 * @param printWriter Where to write the log to.
	 * @param clock The clock that is used to calculate timestamps and durations.
	 */
	public TracingMessageConsumer(
			MessageConsumer messageConsumer,
			Map<String, RequestMetadata> sentRequests,
			Map<String, RequestMetadata> receivedRequests,
			PrintWriter printWriter,
			Clock clock) {
		this(messageConsumer, sentRequests, receivedRequests, printWriter, clock, null);
	}

	/**
	 * @param messageConsumer The {@link MessageConsumer} to wrap.
	 * @param sentRequests A map that keeps track of pending sent request data.
	 * @param receivedRequests A map that keeps track of pending received request data.
	 * @param printWriter Where to write the log to.
	 * @param clock The clock that is used to calculate timestamps and durations.
	 * @param locale THe Locale to format the timestamps and durations, or <code>null</code> to use default locale.
	 */
	public TracingMessageConsumer(
			MessageConsumer messageConsumer,
			Map<String, RequestMetadata> sentRequests,
			Map<String, RequestMetadata> receivedRequests,
			PrintWriter printWriter,
			Clock clock,
			Locale locale) {
		this.messageConsumer = Objects.requireNonNull(messageConsumer);
		this.sentRequests = Objects.requireNonNull(sentRequests);
		this.receivedRequests = Objects.requireNonNull(receivedRequests);
		this.printWriter = Objects.requireNonNull(printWriter);
		this.clock = Objects.requireNonNull(clock);
		if (locale == null) {
			this.dateTimeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a").withZone(clock.getZone());
		} else {
			this.dateTimeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a", locale).withZone(clock.getZone());
		}
	}

	/**
	 * Constructs a log string for a given {@link Message}. The type of the {@link MessageConsumer}
	 * determines if we're sending or receiving a message. The type of the @{link Message} determines
	 * if it is a request, response, or notification.
	 */
	@Override
	public void consume(Message message) throws MessageIssueException, JsonRpcException {
		final Instant now = clock.instant();
		final String date = dateTimeFormatter.format(now);
		final String logString;

		if (messageConsumer instanceof StreamMessageConsumer) {
			logString = consumeMessageSending(message, now, date);
		} else if (messageConsumer instanceof RemoteEndpoint) {
			logString = consumeMessageReceiving(message, now, date);
		} else {
			LOG.log(WARNING, String.format("Unknown MessageConsumer type: %s", messageConsumer));
			logString = null;
		}

		if (logString != null) {
			printWriter.print(logString);
			printWriter.flush();
		}

		messageConsumer.consume(message);
	}

	private String consumeMessageSending(Message message, Instant now, String date) {
		if (message instanceof RequestMessage) {
			RequestMessage requestMessage = (RequestMessage) message;
			String id = requestMessage.getId();
			String method = requestMessage.getMethod();
			RequestMetadata requestMetadata = new RequestMetadata(method, now);
			sentRequests.put(id, requestMetadata);
			Object params = requestMessage.getParams();
			String paramsJson = MessageJsonHandler.toString(params);
			String format = "[Trace - %s] Sending request '%s - (%s)'\nParams: %s\n\n\n";
			return String.format(format, date, method, id, paramsJson);
		} else if (message instanceof ResponseMessage) {
			ResponseMessage responseMessage = (ResponseMessage) message;
			String id = responseMessage.getId();
			RequestMetadata requestMetadata = receivedRequests.remove(id);
			if (requestMetadata == null) {
				LOG.log(WARNING, String.format("Unmatched response message: %s", message));
				return null;
			}
			String method = requestMetadata.method;
			long latencyMillis = now.toEpochMilli() - requestMetadata.start.toEpochMilli();
			Object result = responseMessage.getResult();
			String resultJson = MessageJsonHandler.toString(result);
			String format =
					"[Trace - %s] Sending response '%s - (%s)'. Processing request took %sms\nResult: %s\n\n\n";
			return String.format(format, date, method, id, latencyMillis, resultJson);
		} else if (message instanceof NotificationMessage) {
			NotificationMessage notificationMessage = (NotificationMessage) message;
			String method = notificationMessage.getMethod();
			Object params = notificationMessage.getParams();
			String paramsJson = MessageJsonHandler.toString(params);
			String format = "[Trace - %s] Sending notification '%s'\nParams: %s\n\n\n";
			return String.format(format, date, method, paramsJson);
		} else {
			LOG.log(WARNING, String.format("Unknown message type: %s", message));
			return null;
		}
	}

	private String consumeMessageReceiving(Message message, Instant now, String date) {
		if (message instanceof RequestMessage) {
			RequestMessage requestMessage = (RequestMessage) message;
			String method = requestMessage.getMethod();
			String id = requestMessage.getId();
			RequestMetadata requestMetadata = new RequestMetadata(method, now);
			receivedRequests.put(id, requestMetadata);
			Object params = requestMessage.getParams();
			String paramsJson = MessageJsonHandler.toString(params);
			String format = "[Trace - %s] Received request '%s - (%s)'\nParams: %s\n\n\n";
			return String.format(format, date, method, id, paramsJson);
		} else if (message instanceof ResponseMessage) {
			ResponseMessage responseMessage = (ResponseMessage) message;
			String id = responseMessage.getId();
			RequestMetadata requestMetadata = sentRequests.remove(id);
			if (requestMetadata == null) {
				LOG.log(WARNING, String.format("Unmatched response message: %s", message));
				return null;
			}
			String method = requestMetadata.method;
			long latencyMillis = now.toEpochMilli() - requestMetadata.start.toEpochMilli();
			Object result = responseMessage.getResult();
			String resultJson = MessageJsonHandler.toString(result);
			Object error = responseMessage.getError();
			String errorJson = MessageJsonHandler.toString(error);
			String format = "[Trace - %s] Received response '%s - (%s)' in %sms\nResult: %s\nError: %s\n\n\n";
			return String.format(format, date, method, id, latencyMillis, resultJson, errorJson);
		} else if (message instanceof NotificationMessage) {
			NotificationMessage notificationMessage = (NotificationMessage) message;
			String method = notificationMessage.getMethod();
			Object params = notificationMessage.getParams();
			String paramsJson = MessageJsonHandler.toString(params);
			String format = "[Trace - %s] Received notification '%s'\nParams: %s\n\n\n";
			return String.format(format, date, method, paramsJson);
		} else {
			LOG.log(WARNING, String.format("Unknown message type: %s", message));
			return null;
		}
	}

	/** Data class for holding pending request metadata. */
	public static class RequestMetadata {
		final String method;
		final Instant start;

		public RequestMetadata(String method, Instant start) {
			this.method = method;
			this.start = start;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			RequestMetadata that = (RequestMetadata) o;
			return Objects.equals(method, that.method) && Objects.equals(start, that.start);
		}

		@Override
		public int hashCode() {
			return Objects.hash(method, start);
		}
	}
}
