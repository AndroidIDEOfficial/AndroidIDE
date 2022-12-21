/******************************************************************************
 * Copyright (c) 2016-2018 TypeFox and others.
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

import com.google.gson.GsonBuilder;

import org.eclipse.lsp4j.jsonrpc.json.ConcurrentMessageProcessor;
import org.eclipse.lsp4j.jsonrpc.json.JsonRpcMethod;
import org.eclipse.lsp4j.jsonrpc.json.JsonRpcMethodProvider;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.json.StreamMessageConsumer;
import org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.services.ServiceEndpoints;
import org.eclipse.lsp4j.jsonrpc.validation.ReflectiveMessageValidator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This is the entry point for applications that use LSP4J. A Launcher does all the wiring that is
 * necessary to connect your endpoint via an input stream and an output stream.
 *
 * @param <T> remote service interface type
 */
public interface Launcher<T> {

  /**
   * Create a new Launcher for a given local service object, a given remote interface and an input
   * and output stream.
   *
   * @param localService - the object that receives method calls from the remote service
   * @param remoteInterface - an interface on which RPC methods are looked up
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   */
  static <T> Launcher<T> createLauncher(
      Object localService, Class<T> remoteInterface, InputStream in, OutputStream out) {
    return new Builder<T>()
        .setLocalService(localService)
        .setRemoteInterface(remoteInterface)
        .setInput(in)
        .setOutput(out)
        .create();
  }

  /**
   * Create a new Launcher for a given local service object, a given remote interface and an input
   * and output stream, and set up message validation and tracing.
   *
   * @param localService - the object that receives method calls from the remote service
   * @param remoteInterface - an interface on which RPC methods are looked up
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   * @param validate - whether messages should be validated with the {@link
   *     ReflectiveMessageValidator}
   * @param trace - a writer to which incoming and outgoing messages are traced, or {@code null} to
   *     disable tracing
   */
  static <T> Launcher<T> createLauncher(
      Object localService,
      Class<T> remoteInterface,
      InputStream in,
      OutputStream out,
      boolean validate,
      PrintWriter trace) {
    return new Builder<T>()
        .setLocalService(localService)
        .setRemoteInterface(remoteInterface)
        .setInput(in)
        .setOutput(out)
        .validateMessages(validate)
        .traceMessages(trace)
        .create();
  }

  /**
   * Create a new Launcher for a given local service object, a given remote interface and an input
   * and output stream. Threads are started with the given executor service. The wrapper function is
   * applied to the incoming and outgoing message streams so additional message handling such as
   * validation and tracing can be included.
   *
   * @param localService - the object that receives method calls from the remote service
   * @param remoteInterface - an interface on which RPC methods are looked up
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   * @param executorService - the executor service used to start threads
   * @param wrapper - a function for plugging in additional message consumers
   */
  static <T> Launcher<T> createLauncher(
      Object localService,
      Class<T> remoteInterface,
      InputStream in,
      OutputStream out,
      ExecutorService executorService,
      Function<MessageConsumer, MessageConsumer> wrapper) {
    return createIoLauncher(localService, remoteInterface, in, out, executorService, wrapper);
  }

  /**
   * Create a new Launcher for a given local service object, a given remote interface and an input
   * and output stream. Threads are started with the given executor service. The wrapper function is
   * applied to the incoming and outgoing message streams so additional message handling such as
   * validation and tracing can be included.
   *
   * @param localService - the object that receives method calls from the remote service
   * @param remoteInterface - an interface on which RPC methods are looked up
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   * @param executorService - the executor service used to start threads
   * @param wrapper - a function for plugging in additional message consumers
   */
  static <T> Launcher<T> createIoLauncher(
      Object localService,
      Class<T> remoteInterface,
      InputStream in,
      OutputStream out,
      ExecutorService executorService,
      Function<MessageConsumer, MessageConsumer> wrapper) {
    return new Builder<T>()
        .setLocalService(localService)
        .setRemoteInterface(remoteInterface)
        .setInput(in)
        .setOutput(out)
        .setExecutorService(executorService)
        .wrapMessages(wrapper)
        .create();
  }

  /**
   * Create a new Launcher for a given local service object, a given remote interface and an input
   * and output stream. Threads are started with the given executor service. The wrapper function is
   * applied to the incoming and outgoing message streams so additional message handling such as
   * validation and tracing can be included. The {@code configureGson} function can be used to
   * register additional type adapters in the {@link GsonBuilder} in order to support protocol
   * classes that cannot be handled by Gson's reflective capabilities.
   *
   * @param localService - the object that receives method calls from the remote service
   * @param remoteInterface - an interface on which RPC methods are looked up
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   * @param executorService - the executor service used to start threads
   * @param wrapper - a function for plugging in additional message consumers
   * @param configureGson - a function for Gson configuration
   */
  static <T> Launcher<T> createIoLauncher(
      Object localService,
      Class<T> remoteInterface,
      InputStream in,
      OutputStream out,
      ExecutorService executorService,
      Function<MessageConsumer, MessageConsumer> wrapper,
      Consumer<GsonBuilder> configureGson) {
    return new Builder<T>()
        .setLocalService(localService)
        .setRemoteInterface(remoteInterface)
        .setInput(in)
        .setOutput(out)
        .setExecutorService(executorService)
        .wrapMessages(wrapper)
        .configureGson(configureGson)
        .create();
  }

  /**
   * Create a new Launcher for a given local service object, a given remote interface and an input
   * and output stream. Threads are started with the given executor service. The wrapper function is
   * applied to the incoming and outgoing message streams so additional message handling such as
   * validation and tracing can be included. The {@code configureGson} function can be used to
   * register additional type adapters in the {@link GsonBuilder} in order to support protocol
   * classes that cannot be handled by Gson's reflective capabilities.
   *
   * @param localService - the object that receives method calls from the remote service
   * @param remoteInterface - an interface on which RPC methods are looked up
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   * @param validate - whether messages should be validated with the {@link
   *     ReflectiveMessageValidator}
   * @param executorService - the executor service used to start threads
   * @param wrapper - a function for plugging in additional message consumers
   * @param configureGson - a function for Gson configuration
   */
  static <T> Launcher<T> createIoLauncher(
      Object localService,
      Class<T> remoteInterface,
      InputStream in,
      OutputStream out,
      boolean validate,
      ExecutorService executorService,
      Function<MessageConsumer, MessageConsumer> wrapper,
      Consumer<GsonBuilder> configureGson) {
    return new Builder<T>()
        .setLocalService(localService)
        .setRemoteInterface(remoteInterface)
        .setInput(in)
        .setOutput(out)
        .validateMessages(validate)
        .setExecutorService(executorService)
        .wrapMessages(wrapper)
        .configureGson(configureGson)
        .create();
  }

  /**
   * Create a new Launcher for a collection of local service objects, a collection of remote
   * interfaces and an input and output stream. Threads are started with the given executor service.
   * The wrapper function is applied to the incoming and outgoing message streams so additional
   * message handling such as validation and tracing can be included. The {@code configureGson}
   * function can be used to register additional type adapters in the {@link GsonBuilder} in order
   * to support protocol classes that cannot be handled by Gson's reflective capabilities.
   *
   * @param localServices - the objects that receive method calls from the remote services
   * @param remoteInterfaces - interfaces on which RPC methods are looked up
   * @param classLoader - a class loader that is able to resolve all given interfaces
   * @param in - input stream to listen for incoming messages
   * @param out - output stream to send outgoing messages
   * @param executorService - the executor service used to start threads
   * @param wrapper - a function for plugging in additional message consumers
   * @param configureGson - a function for Gson configuration
   */
  static Launcher<Object> createIoLauncher(
      Collection<Object> localServices,
      Collection<Class<?>> remoteInterfaces,
      ClassLoader classLoader,
      InputStream in,
      OutputStream out,
      ExecutorService executorService,
      Function<MessageConsumer, MessageConsumer> wrapper,
      Consumer<GsonBuilder> configureGson) {
    return new Builder<Object>()
        .setLocalServices(localServices)
        .setRemoteInterfaces(remoteInterfaces)
        .setClassLoader(classLoader)
        .setInput(in)
        .setOutput(out)
        .setExecutorService(executorService)
        .wrapMessages(wrapper)
        .configureGson(configureGson)
        .create();
  }

  // ------------------------------ Builder Class ------------------------------//

  /**
   * Start a thread that listens to the input stream. The thread terminates when the stream is
   * closed.
   *
   * @return a future that returns {@code null} when the listener thread is terminated
   */
  Future<Void> startListening();

  // ---------------------------- Interface Methods ----------------------------//

  /** Returns the proxy instance that implements the remote service interfaces. */
  T getRemoteProxy();

  /**
   * Returns the remote endpoint. Use this one to send generic {@code request} or {@code notify}
   * methods to the remote services.
   */
  RemoteEndpoint getRemoteEndpoint();

  /**
   * The launcher builder wires up all components for JSON-RPC communication.
   *
   * @param <T> remote service interface type
   */
  public static class Builder<T> {

    protected Collection<Object> localServices;
    protected Collection<Class<? extends T>> remoteInterfaces;
    protected InputStream input;
    protected OutputStream output;
    protected ExecutorService executorService;
    protected Function<MessageConsumer, MessageConsumer> messageWrapper;
    protected Function<Throwable, ResponseError> exceptionHandler;
    protected boolean validateMessages;
    protected Consumer<GsonBuilder> configureGson;
    protected ClassLoader classLoader;
    protected MessageTracer messageTracer;

    public Builder<T> setLocalService(Object localService) {
      this.localServices = Collections.singletonList(localService);
      return this;
    }

    public Builder<T> setLocalServices(Collection<Object> localServices) {
      this.localServices = localServices;
      return this;
    }

    public Builder<T> setRemoteInterface(Class<? extends T> remoteInterface) {
      this.remoteInterfaces = Collections.singletonList(remoteInterface);
      return this;
    }

    public Builder<T> setRemoteInterfaces(Collection<Class<? extends T>> remoteInterfaces) {
      this.remoteInterfaces = remoteInterfaces;
      return this;
    }

    public Builder<T> setInput(InputStream input) {
      this.input = input;
      return this;
    }

    public Builder<T> setOutput(OutputStream output) {
      this.output = output;
      return this;
    }

    public Builder<T> setExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
    }

    public Builder<T> setClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
      return this;
    }

    public Builder<T> wrapMessages(Function<MessageConsumer, MessageConsumer> wrapper) {
      this.messageWrapper = wrapper;
      return this;
    }

    public Builder<T> setExceptionHandler(Function<Throwable, ResponseError> exceptionHandler) {
      this.exceptionHandler = exceptionHandler;
      return this;
    }

    public Builder<T> validateMessages(boolean validate) {
      this.validateMessages = validate;
      return this;
    }

    public Builder<T> traceMessages(PrintWriter tracer) {
      if (tracer != null) {
        this.messageTracer = new MessageTracer(tracer);
      }
      return this;
    }

    public Builder<T> configureGson(Consumer<GsonBuilder> configureGson) {
      this.configureGson = configureGson;
      return this;
    }

    public Launcher<T> create() {
      // Validate input
      if (input == null) throw new IllegalStateException("Input stream must be configured.");
      if (output == null) throw new IllegalStateException("Output stream must be configured.");
      if (localServices == null)
        throw new IllegalStateException("Local service must be configured.");
      if (remoteInterfaces == null)
        throw new IllegalStateException("Remote interface must be configured.");

      // Create the JSON handler, remote endpoint and remote proxy
      MessageJsonHandler jsonHandler = createJsonHandler();
      RemoteEndpoint remoteEndpoint = createRemoteEndpoint(jsonHandler);
      T remoteProxy = createProxy(remoteEndpoint);

      // Create the message processor
      StreamMessageProducer reader = new StreamMessageProducer(input, jsonHandler, remoteEndpoint);
      MessageConsumer messageConsumer = wrapMessageConsumer(remoteEndpoint);
      ConcurrentMessageProcessor msgProcessor =
          createMessageProcessor(reader, messageConsumer, remoteProxy);
      ExecutorService execService =
          executorService != null ? executorService : Executors.newCachedThreadPool();
      return createLauncher(execService, remoteProxy, remoteEndpoint, msgProcessor);
    }

    /** Create the JSON handler for messages between the local and remote services. */
    protected MessageJsonHandler createJsonHandler() {
      Map<String, JsonRpcMethod> supportedMethods = getSupportedMethods();
      if (configureGson != null) return new MessageJsonHandler(supportedMethods, configureGson);
      else return new MessageJsonHandler(supportedMethods);
    }

    /** Gather all JSON-RPC methods from the local and remote services. */
    protected Map<String, JsonRpcMethod> getSupportedMethods() {
      Map<String, JsonRpcMethod> supportedMethods = new LinkedHashMap<>();
      // Gather the supported methods of remote interfaces
      for (Class<?> interface_ : remoteInterfaces) {
        supportedMethods.putAll(ServiceEndpoints.getSupportedMethods(interface_));
      }

      // Gather the supported methods of local services
      for (Object localService : localServices) {
        if (localService instanceof JsonRpcMethodProvider) {
          JsonRpcMethodProvider rpcMethodProvider = (JsonRpcMethodProvider) localService;
          supportedMethods.putAll(rpcMethodProvider.supportedMethods());
        } else {
          supportedMethods.putAll(ServiceEndpoints.getSupportedMethods(localService.getClass()));
        }
      }

      return supportedMethods;
    }

    /** Create the remote endpoint that communicates with the local services. */
    protected RemoteEndpoint createRemoteEndpoint(MessageJsonHandler jsonHandler) {
      MessageConsumer outgoingMessageStream = new StreamMessageConsumer(output, jsonHandler);
      outgoingMessageStream = wrapMessageConsumer(outgoingMessageStream);
      Endpoint localEndpoint = ServiceEndpoints.toEndpoint(localServices);
      RemoteEndpoint remoteEndpoint;
      if (exceptionHandler == null)
        remoteEndpoint = new RemoteEndpoint(outgoingMessageStream, localEndpoint);
      else
        remoteEndpoint = new RemoteEndpoint(outgoingMessageStream, localEndpoint, exceptionHandler);
      jsonHandler.setMethodProvider(remoteEndpoint);
      return remoteEndpoint;
    }

    protected MessageConsumer wrapMessageConsumer(MessageConsumer consumer) {
      MessageConsumer result = consumer;
      if (messageTracer != null) {
        result = messageTracer.apply(consumer);
      }
      if (validateMessages) {
        result = new ReflectiveMessageValidator(result);
      }
      if (messageWrapper != null) {
        result = messageWrapper.apply(result);
      }
      return result;
    }

    /** Create the proxy for calling methods on the remote service. */
    @SuppressWarnings("unchecked")
    protected T createProxy(RemoteEndpoint remoteEndpoint) {
      if (localServices.size() == 1 && remoteInterfaces.size() == 1) {
        return ServiceEndpoints.toServiceObject(remoteEndpoint, remoteInterfaces.iterator().next());
      } else {
        return (T)
            ServiceEndpoints.toServiceObject(
                remoteEndpoint, (Collection<Class<?>>) (Object) remoteInterfaces, classLoader);
      }
    }

    /** Create the message processor that listens to the input stream. */
    protected ConcurrentMessageProcessor createMessageProcessor(
        MessageProducer reader, MessageConsumer messageConsumer, T remoteProxy) {
      return new ConcurrentMessageProcessor(reader, messageConsumer);
    }

    protected Launcher<T> createLauncher(
        ExecutorService execService,
        T remoteProxy,
        RemoteEndpoint remoteEndpoint,
        ConcurrentMessageProcessor msgProcessor) {
      return new StandardLauncher<T>(execService, remoteProxy, remoteEndpoint, msgProcessor);
    }
  }
}
