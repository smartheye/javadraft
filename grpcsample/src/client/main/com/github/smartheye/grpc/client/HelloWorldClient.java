package com.github.smartheye.grpc.client;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.smartheye.grpc.server.internal.GreeterGrpc;
import com.github.smartheye.grpc.server.internal.HelloReply;
import com.github.smartheye.grpc.server.internal.HelloRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class HelloWorldClient {
	  private static final Logger logger = LogManager.getLogger(HelloWorldClient.class.getName());

	  private final ManagedChannel channel;
	  private final GreeterGrpc.GreeterBlockingStub blockingStub;

	  /** Construct client connecting to HelloWorld server at {@code host:port}. */
	  public HelloWorldClient(String host, int port) {
	    this(ManagedChannelBuilder.forAddress(host, port)
	        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
	        // needing certificates.
	        .usePlaintext(true)
	        .build());
	  }

	  /** Construct client for accessing RouteGuide server using the existing channel. */
	  HelloWorldClient(ManagedChannel channel) {
	    this.channel = channel;
	    blockingStub = GreeterGrpc.newBlockingStub(channel);
	  }

	  public void shutdown() throws InterruptedException {
	    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	  }

	  /** Say hello to server. */
	  public void greet(String name) {
	    logger.info("Will try to greet " + name + " ...");
	    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
	    HelloReply response;
	    try {
	      response = blockingStub.sayHello(request);
	    } catch (StatusRuntimeException e) {
	      logger.warn("RPC failed: {0}", e.getStatus());
	      return;
	    }
	    logger.info("Greeting: " + response.getMessage());
	  }

	  /**
	   * Greet server. If provided, the first element of {@code args} is the name to use in the
	   * greeting.
	   */
	  public static void main(String[] args) throws Exception {
	    HelloWorldClient client = new HelloWorldClient("localhost", 50051);
	    try {
	      /* Access a service running on the local machine on port 50051 */
	      String user = "world";
	      if (args.length > 0) {
	        user = args[0]; /* Use the arg as the name to greet if provided */
	      }
	      client.greet(user);
	    } finally {
	      client.shutdown();
	    }
	  }
	}