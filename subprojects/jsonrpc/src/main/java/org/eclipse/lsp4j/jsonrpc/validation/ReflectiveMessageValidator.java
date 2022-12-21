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
package org.eclipse.lsp4j.jsonrpc.validation;

import com.google.gson.JsonElement;
import com.itsaky.androidide.utils.ILogger;

import org.eclipse.lsp4j.jsonrpc.JsonRpcException;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.MessageIssueException;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.eclipse.lsp4j.jsonrpc.messages.MessageIssue;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Validates messages and forwards them to other message consumers. In case an issue is found, a
 * {@link MessageIssueException} is thrown.
 */
public class ReflectiveMessageValidator implements MessageConsumer {

  private static final ILogger LOG = ILogger.newInstance("ReflectiveMessageValidator");

  private final MessageConsumer delegate;

  /** When created with this constructor, the validator acts as a message sink. */
  public ReflectiveMessageValidator() {
    this.delegate = null;
  }

  /** Forward messages to the given consumer unless an issue is found. */
  public ReflectiveMessageValidator(MessageConsumer delegate) {
    this.delegate = delegate;
  }

  @Override
  public void consume(Message message) throws MessageIssueException, JsonRpcException {
    List<MessageIssue> issues = validate(message);
    if (!issues.isEmpty()) {
      // Sort the messages in order to get a stable order (otherwise it depends on the JVM's
      // reflection implementation)
      Collections.sort(issues, (issue1, issue2) -> issue1.getText().compareTo(issue2.getText()));
      throw new MessageIssueException(message, issues);
    } else if (delegate != null) {
      delegate.consume(message);
    }
  }

  /** Check whether the given object is valid. If it is not valid, its issues are not reported. */
  public boolean isValid(Object object) {
    List<MessageIssue> issues = validate(object);
    return issues.isEmpty();
  }

  protected List<MessageIssue> validate(Object object) {
    List<MessageIssue> result = new ArrayList<>();
    try {
      validate(object, result, new LinkedList<>(), new LinkedList<>());
    } catch (Exception e) {
      LOG.error("Error during message validation: " + e.getMessage(), e);
      result.add(
          new MessageIssue(
              "Message validation failed, please check the logs of the remote endpoint.",
              ResponseErrorCode.InvalidParams.getValue()));
    }
    return result;
  }

  /** Validate all fields of the given object. */
  protected void validate(
      Object object,
      List<MessageIssue> issues,
      Deque<Object> objectStack,
      Deque<Object> accessorStack)
      throws Exception {
    if (object == null
        || object instanceof Enum<?>
        || object instanceof String
        || object instanceof Number
        || object instanceof Boolean
        || object instanceof JsonElement
        || object instanceof Throwable) {
      return;
    }
    if (objectStack.contains(object)) {
      issues.add(
          new MessageIssue(
              "An element of the message has a direct or indirect reference to itself."
                  + " Path: "
                  + createPathString(accessorStack),
              ResponseErrorCode.InvalidParams.getValue()));
      return;
    }
    objectStack.push(object);
    if (object instanceof List<?>) {
      ListIterator<?> iter = ((List<?>) object).listIterator();
      while (iter.hasNext()) {
        accessorStack.push(iter.nextIndex());
        Object element = iter.next();
        if (element == null) {
          issues.add(
              new MessageIssue(
                  "Lists must not contain null references."
                      + " Path: "
                      + createPathString(accessorStack),
                  ResponseErrorCode.InvalidParams.getValue()));
        }
        validate(element, issues, objectStack, accessorStack);
        accessorStack.pop();
      }
    } else if (object instanceof Either<?, ?>) {
      Either<?, ?> either = (Either<?, ?>) object;
      if (either.isLeft()) {
        validate(either.getLeft(), issues, objectStack, accessorStack);
      } else if (either.isRight()) {
        validate(either.getRight(), issues, objectStack, accessorStack);
      } else {
        issues.add(
            new MessageIssue(
                "An Either instance must not be empty."
                    + " Path: "
                    + createPathString(accessorStack),
                ResponseErrorCode.InvalidParams.getValue()));
      }
    } else {
      for (Method method : object.getClass().getMethods()) {
        if (isGetter(method)) {
          accessorStack.push(method);
          Object value = method.invoke(object);
          if (value == null && method.getAnnotation(NonNull.class) != null) {
            issues.add(
                new MessageIssue(
                    "The accessor '"
                        + method.getDeclaringClass().getSimpleName()
                        + "."
                        + method.getName()
                        + "()' must return a non-null value."
                        + " Path: "
                        + createPathString(accessorStack),
                    ResponseErrorCode.InvalidParams.getValue()));
          }
          validate(value, issues, objectStack, accessorStack);
          accessorStack.pop();
        }
      }
    }
    objectStack.pop();
  }

  protected String createPathString(Deque<Object> accessorStack) {
    StringBuilder result = new StringBuilder("$");
    Iterator<Object> resultIter = accessorStack.descendingIterator();
    while (resultIter.hasNext()) {
      Object accessor = resultIter.next();
      if (accessor instanceof Method) result.append('.').append(getPropertyName((Method) accessor));
      else if (accessor instanceof Integer) result.append('[').append(accessor).append(']');
      else result.append(accessor);
    }
    return result.toString();
  }

  protected boolean isGetter(Method method) {
    return method.getParameterCount() == 0
        && method.getName().startsWith("get")
        && method.getDeclaringClass() != Object.class
        && Modifier.isPublic(method.getModifiers())
        && !Modifier.isStatic(method.getModifiers());
  }

  protected String getPropertyName(Method method) {
    String methodName = method.getName();
    if (methodName.startsWith("get") && methodName.length() > 3)
      return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    else return methodName;
  }
}
