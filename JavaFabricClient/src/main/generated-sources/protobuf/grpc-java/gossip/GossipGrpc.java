package gossip;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * Gossip
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.5.0)",
    comments = "Source: gossip/message.proto")
public final class GossipGrpc {

  private GossipGrpc() {}

  public static final String SERVICE_NAME = "gossip.Gossip";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<gossip.Message.Envelope,
      gossip.Message.Envelope> METHOD_GOSSIP_STREAM =
      io.grpc.MethodDescriptor.<gossip.Message.Envelope, gossip.Message.Envelope>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "gossip.Gossip", "GossipStream"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              gossip.Message.Envelope.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              gossip.Message.Envelope.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<gossip.Message.Empty,
      gossip.Message.Empty> METHOD_PING =
      io.grpc.MethodDescriptor.<gossip.Message.Empty, gossip.Message.Empty>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "gossip.Gossip", "Ping"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              gossip.Message.Empty.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              gossip.Message.Empty.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GossipStub newStub(io.grpc.Channel channel) {
    return new GossipStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GossipBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GossipBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GossipFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GossipFutureStub(channel);
  }

  /**
   * <pre>
   * Gossip
   * </pre>
   */
  public static abstract class GossipImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * GossipStream is the gRPC stream used for sending and receiving messages
     * </pre>
     */
    public io.grpc.stub.StreamObserver<gossip.Message.Envelope> gossipStream(
        io.grpc.stub.StreamObserver<gossip.Message.Envelope> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_GOSSIP_STREAM, responseObserver);
    }

    /**
     * <pre>
     * Ping is used to probe a remote peer's aliveness
     * </pre>
     */
    public void ping(gossip.Message.Empty request,
        io.grpc.stub.StreamObserver<gossip.Message.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PING, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GOSSIP_STREAM,
            asyncBidiStreamingCall(
              new MethodHandlers<
                gossip.Message.Envelope,
                gossip.Message.Envelope>(
                  this, METHODID_GOSSIP_STREAM)))
          .addMethod(
            METHOD_PING,
            asyncUnaryCall(
              new MethodHandlers<
                gossip.Message.Empty,
                gossip.Message.Empty>(
                  this, METHODID_PING)))
          .build();
    }
  }

  /**
   * <pre>
   * Gossip
   * </pre>
   */
  public static final class GossipStub extends io.grpc.stub.AbstractStub<GossipStub> {
    private GossipStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GossipStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GossipStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GossipStub(channel, callOptions);
    }

    /**
     * <pre>
     * GossipStream is the gRPC stream used for sending and receiving messages
     * </pre>
     */
    public io.grpc.stub.StreamObserver<gossip.Message.Envelope> gossipStream(
        io.grpc.stub.StreamObserver<gossip.Message.Envelope> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_GOSSIP_STREAM, getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Ping is used to probe a remote peer's aliveness
     * </pre>
     */
    public void ping(gossip.Message.Empty request,
        io.grpc.stub.StreamObserver<gossip.Message.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PING, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Gossip
   * </pre>
   */
  public static final class GossipBlockingStub extends io.grpc.stub.AbstractStub<GossipBlockingStub> {
    private GossipBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GossipBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GossipBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GossipBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping is used to probe a remote peer's aliveness
     * </pre>
     */
    public gossip.Message.Empty ping(gossip.Message.Empty request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PING, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Gossip
   * </pre>
   */
  public static final class GossipFutureStub extends io.grpc.stub.AbstractStub<GossipFutureStub> {
    private GossipFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GossipFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GossipFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GossipFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping is used to probe a remote peer's aliveness
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<gossip.Message.Empty> ping(
        gossip.Message.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PING, getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_GOSSIP_STREAM = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GossipImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GossipImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((gossip.Message.Empty) request,
              (io.grpc.stub.StreamObserver<gossip.Message.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GOSSIP_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.gossipStream(
              (io.grpc.stub.StreamObserver<gossip.Message.Envelope>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class GossipDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return gossip.Message.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GossipGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GossipDescriptorSupplier())
              .addMethod(METHOD_GOSSIP_STREAM)
              .addMethod(METHOD_PING)
              .build();
        }
      }
    }
    return result;
  }
}
