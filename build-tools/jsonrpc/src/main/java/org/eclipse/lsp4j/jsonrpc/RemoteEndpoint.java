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
package org.eclipse.lsp4j.jsonrpc;

import com.itsaky.androidide.utils.ILogger;

import org.eclipse.lsp4j.jsonrpc.json.MessageConstants;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.json.MethodProvider;
import org.eclipse.lsp4j.jsonrpc.messages.CancelParams;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.eclipse.lsp4j.jsonrpc.messages.MessageIssue;
import org.eclipse.lsp4j.jsonrpc.messages.NotificationMessage;
import org.eclipse.lsp4j.jsonrpc.messages.RequestMessage;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseMessage;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * An endpoint that can be used to send messages to a given {@link MessageConsumer} by calling
 * {@link #request(String, Object)} or {@link #notify(String, Object)}. When connected to a {@link
 * MessageProducer}, this class forwards received messages to the local {@link Endpoint} given in
 * the constructor.
 */
public class RemoteEndpoint
    implements Endpoint, MessageConsumer, MessageIssueHandler, MethodProvider {

  private static final ILogger LOG = ILogger.newInstance("RemoteEndpoint");

  public static final Function<Throwable, ResponseError> DEFAULT_EXCEPTION_HANDLER =
      (throwable) -> {
        if (throwable instanceof ResponseErrorException) {
          return ((ResponseErrorException) throwable).getResponseError();
        } else if ((throwable instanceof CompletionException
                || throwable instanceof InvocationTargetException)
            && throwable.getCause() instanceof ResponseErrorException) {
          return ((ResponseErrorException) throwable.getCause()).getResponseError();
        } else {
          return fallbackResponseError("Internal error", throwable);
        }
      };
  private final MessageConsumer out;
  private final Endpoint localEndpoint;
  private final Function<Throwable, ResponseError> exceptionHandler;
  private final AtomicInteger nextRequestId = new AtomicInteger();
  private final Map<String, PendingRequestInfo> sentRequestMap = new LinkedHashMap<>();
  private final Map<String, CompletableFuture<?>> receivedRequestMap = new LinkedHashMap<>();
  /**
   * @param out - a consumer that transmits messages to the remote service
   * @param localEndpoint - the local service implementation
   */
  public RemoteEndpoint(MessageConsumer out, Endpoint localEndpoint) {
    this(out, localEndpoint, DEFAULT_EXCEPTION_HANDLER);
  }

  /**
   * @param out - a consumer that transmits messages to the remote service
   * @param localEndpoint - the local service implementation
   * @param exceptionHandler - an exception handler that should never return null.
   */
  public RemoteEndpoint(
      MessageConsumer out,
      Endpoint localEndpoint,
      Function<Throwable, ResponseError> exceptionHandler) {
    if (out == null) throw new NullPointerException("out");
    if (localEndpoint == null) throw new NullPointerException("localEndpoint");
    if (exceptionHandler == null) throw new NullPointerException("exceptionHandler");
    this.out = out;
    this.localEndpoint = localEndpoint;
    this.exceptionHandler = exceptionHandler;
  }

  private static ResponseError fallbackResponseError(String header, Throwable throwable) {
    LOG.error(header + ": " + throwable.getMessage(), throwable);
    ResponseError error = new ResponseError();
    error.setMessage(header + ".");
    error.setCode(ResponseErrorCode.InternalError);
    ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
    PrintWriter stackTraceWriter = new PrintWriter(stackTrace);
    throwable.printStackTrace(stackTraceWriter);
    stackTraceWriter.flush();
    error.setData(stackTrace.toString());
    return error;
  }

  /** Send a request to the remote endpoint. */
  @Override
  public CompletableFuture<Object> request(String method, Object parameter) {
    final RequestMessage requestMessage = createRequestMessage(method, parameter);
    final CompletableFuture<Object> result =
        new CompletableFuture<Object>() {
          @Override
          public boolean cancel(boolean mayInterruptIfRunning) {
            sendCancelNotification(requestMessage.getRawId());
            return super.cancel(mayInterruptIfRunning);
          }
        };
    synchronized (sentRequestMap) {
      // Store request information so it can be handled when the response is received
      sentRequestMap.put(requestMessage.getId(), new PendingRequestInfo(requestMessage, result));
    }

    try {
      // Send the request to the remote service
      out.consume(requestMessage);
    } catch (Exception exception) {
      // The message could not be sent, e.g. because the communication channel was closed
      result.completeExceptionally(exception);
    }
    return result;
  }

  /** Send a notification to the remote endpoint. */
  @Override
  public void notify(String method, Object parameter) {
    NotificationMessage notificationMessage = createNotificationMessage(method, parameter);
    try {
      out.consume(notificationMessage);
    } catch (Exception exception) {
      ILogger.Priority priority =
          JsonRpcException.indicatesStreamClosed(exception)
              ? ILogger.Priority.INFO
              : ILogger.Priority.WARNING;
      LOG.log(priority, "Failed to send notification message.", exception);
    }
  }

  protected RequestMessage createRequestMessage(String method, Object parameter) {
    RequestMessage requestMessage = new RequestMessage();
    requestMessage.setId(String.valueOf(nextRequestId.incrementAndGet()));
    requestMessage.setMethod(method);
    requestMessage.setParams(parameter);
    return requestMessage;
  }

  protected void sendCancelNotification(Either<String, Number> id) {
    CancelParams cancelParams = new CancelParams();
    cancelParams.setRawId(id);
    notify(MessageJsonHandler.CANCEL_METHOD.getMethodName(), cancelParams);
  }

  protected NotificationMessage createNotificationMessage(String method, Object parameter) {
    NotificationMessage notificationMessage = new NotificationMessage();
    notificationMessage.setJsonrpc(MessageConstants.JSONRPC_VERSION);
    notificationMessage.setMethod(method);
    notificationMessage.setParams(parameter);
    return notificationMessage;
  }

  @Override
  public void consume(Message message) {
    if (message instanceof NotificationMessage) {
      NotificationMessage notificationMessage = (NotificationMessage) message;
      handleNotification(notificationMessage);
    } else if (message instanceof RequestMessage) {
      RequestMessage requestMessage = (RequestMessage) message;
      handleRequest(requestMessage);
    } else if (message instanceof ResponseMessage) {
      ResponseMessage responseMessage = (ResponseMessage) message;
      handleResponse(responseMessage);
    } else {
      LOG.warn("Unkown message type.", message);
    }
  }

  @Override
  public void handle(Message message, List<MessageIssue> issues) {
    if (issues.isEmpty()) {
      throw new IllegalArgumentException("The list of issues must not be empty.");
    }

    if (message instanceof RequestMessage) {
      RequestMessage requestMessage = (RequestMessage) message;
      handleRequestIssues(requestMessage, issues);
    } else if (message instanceof ResponseMessage) {
      ResponseMessage responseMessage = (ResponseMessage) message;
      handleResponseIssues(responseMessage, issues);
    } else {
      logIssues(message, issues);
    }
  }

  protected void handleRequestIssues(RequestMessage requestMessage, List<MessageIssue> issues) {
    ResponseError errorObject = new ResponseError();
    if (issues.size() == 1) {
      MessageIssue issue = issues.get(0);
      errorObject.setMessage(issue.getText());
      errorObject.setCode(issue.getIssueCode());
      errorObject.setData(issue.getCause());
    } else {
      if (requestMessage.getMethod() != null)
        errorObject.setMessage(
            "Multiple issues were found in '" + requestMessage.getMethod() + "' request.");
      else errorObject.setMessage("Multiple issues were found in request.");
      errorObject.setCode(ResponseErrorCode.InvalidRequest);
      errorObject.setData(issues);
    }
    out.consume(createErrorResponseMessage(requestMessage, errorObject));
  }

  protected void handleResponseIssues(ResponseMessage responseMessage, List<MessageIssue> issues) {
    PendingRequestInfo requestInfo;
    synchronized (sentRequestMap) {
      requestInfo = sentRequestMap.remove(responseMessage.getId());
    }
    if (requestInfo == null) {
      // We have no pending request information that matches the id given in the response
      LOG.warn("Unmatched response message: " + responseMessage);
      logIssues(responseMessage, issues);
    } else {
      requestInfo.future.completeExceptionally(new MessageIssueException(responseMessage, issues));
    }
  }

  protected void logIssues(Message message, List<MessageIssue> issues) {
    for (MessageIssue issue : issues) {
      String logMessage =
          "Issue found in " + message.getClass().getSimpleName() + ": " + issue.getText();
      LOG.warn(logMessage, issue.getCause());
    }
  }

  protected ResponseMessage createErrorResponseMessage(
      RequestMessage requestMessage, ResponseError errorObject) {
    ResponseMessage responseMessage = createResponseMessage(requestMessage);
    responseMessage.setError(errorObject);
    return responseMessage;
  }

  protected ResponseMessage createResponseMessage(RequestMessage requestMessage) {
    ResponseMessage responseMessage = new ResponseMessage();
    responseMessage.setRawId(requestMessage.getRawId());
    responseMessage.setJsonrpc(MessageConstants.JSONRPC_VERSION);
    return responseMessage;
  }

  @Override
  public String resolveMethod(String requestId) {
    synchronized (sentRequestMap) {
      PendingRequestInfo requestInfo = sentRequestMap.get(requestId);
      if (requestInfo != null) {
        return requestInfo.requestMessage.getMethod();
      }
    }
    return null;
  }

  protected void handleResponse(ResponseMessage responseMessage) {
    PendingRequestInfo requestInfo;
    synchronized (sentRequestMap) {
      requestInfo = sentRequestMap.remove(responseMessage.getId());
    }
    if (requestInfo == null) {
      // We have no pending request information that matches the id given in the response
      LOG.warn("Unmatched response message: " + responseMessage);
    } else if (responseMessage.getError() != null) {
      // The remote service has replied with an error
      requestInfo.future.completeExceptionally(
          new ResponseErrorException(responseMessage.getError()));
    } else {
      // The remote service has replied with a result object
      requestInfo.future.complete(responseMessage.getResult());
    }
  }

  protected void handleNotification(NotificationMessage notificationMessage) {
    if (!handleCancellation(notificationMessage)) {
      // Forward the notification to the local endpoint
      try {
        localEndpoint.notify(notificationMessage.getMethod(), notificationMessage.getParams());
      } catch (Exception exception) {
        LOG.warn("Notification threw an exception: " + notificationMessage, exception);
      }
    }
  }

  /**
   * Cancellation is handled inside this class and not forwarded to the local endpoint.
   *
   * @return {@code true} if the given message is a cancellation notification, {@code false} if it
   *     can be handled by the local endpoint
   */
  protected boolean handleCancellation(NotificationMessage notificationMessage) {
    if (MessageJsonHandler.CANCEL_METHOD.getMethodName().equals(notificationMessage.getMethod())) {
      Object cancelParams = notificationMessage.getParams();
      if (cancelParams != null) {
        if (cancelParams instanceof CancelParams) {
          synchronized (receivedRequestMap) {
            String id = ((CancelParams) cancelParams).getId();
            CompletableFuture<?> future = receivedRequestMap.get(id);
            if (future != null) future.cancel(true);
            else LOG.warn("Unmatched cancel notification for request id " + id);
          }
          return true;
        } else {
          LOG.warn(
              "Cancellation support is disabled, since the '"
                  + MessageJsonHandler.CANCEL_METHOD.getMethodName()
                  + "' method has been registered explicitly.");
        }
      } else {
        LOG.warn("Missing 'params' attribute of cancel notification.");
      }
    }
    return false;
  }

  protected void handleRequest(RequestMessage requestMessage) {
    CompletableFuture<?> future;
    try {
      // Forward the request to the local endpoint
      future = localEndpoint.request(requestMessage.getMethod(), requestMessage.getParams());
    } catch (Throwable throwable) {
      // The local endpoint has failed handling the request - reply with an error response
      ResponseError errorObject = exceptionHandler.apply(throwable);
      if (errorObject == null) {
        errorObject =
            fallbackResponseError(
                "Internal error. Exception handler provided no error object", throwable);
      }
      out.consume(createErrorResponseMessage(requestMessage, errorObject));
      if (throwable instanceof Error) throw (Error) throwable;
      else return;
    }

    final String messageId = requestMessage.getId();
    synchronized (receivedRequestMap) {
      receivedRequestMap.put(messageId, future);
    }
    future
        .thenAccept(
            (result) -> {
              // Reply with the result object that was computed by the local endpoint
              out.consume(createResultResponseMessage(requestMessage, result));
            })
        .exceptionally(
            (Throwable t) -> {
              // The local endpoint has failed computing a result - reply with an error response
              ResponseMessage responseMessage;
              if (isCancellation(t)) {
                String message =
                    "The request (id: "
                        + messageId
                        + ", method: '"
                        + requestMessage.getMethod()
                        + "') has been cancelled";
                ResponseError errorObject =
                    new ResponseError(ResponseErrorCode.RequestCancelled, message, null);
                responseMessage = createErrorResponseMessage(requestMessage, errorObject);
              } else {
                ResponseError errorObject = exceptionHandler.apply(t);
                if (errorObject == null) {
                  errorObject =
                      fallbackResponseError(
                          "Internal error. Exception handler provided no error object", t);
                }
                responseMessage = createErrorResponseMessage(requestMessage, errorObject);
              }
              out.consume(responseMessage);
              return null;
            })
        .thenApply(
            (obj) -> {
              synchronized (receivedRequestMap) {
                receivedRequestMap.remove(messageId);
              }
              return null;
            });
  }

  protected ResponseMessage createResultResponseMessage(
      RequestMessage requestMessage, Object result) {
    ResponseMessage responseMessage = createResponseMessage(requestMessage);
    responseMessage.setResult(result);
    return responseMessage;
  }

  protected boolean isCancellation(Throwable t) {
    if (t instanceof CompletionException) {
      return isCancellation(t.getCause());
    }
    return (t instanceof CancellationException);
  }

  /**
   * Information about requests that have been sent and for which no response has been received yet.
   */
  private static class PendingRequestInfo {
    RequestMessage requestMessage;
    CompletableFuture<Object> future;

    PendingRequestInfo(RequestMessage requestMessage2, CompletableFuture<Object> future2) {
      this.requestMessage = requestMessage2;
      this.future = future2;
    }
  }
}
