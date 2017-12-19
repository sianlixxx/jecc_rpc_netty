package com.jecc.frameworke.rpc.consumer.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.jecc.frameworke.rpc.api.msgbean.InvokerMsg;
import com.jecc.frameworke.rpc.api.msgbean.ResponseMsg;
import com.jecc.frameworke.rpc.codec.ProtoDecoder;
import com.jecc.frameworke.rpc.codec.ProtoEncoder;

public class RpcProxy {

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<?> clazz) {
		MethodProxy methodProxy = new MethodProxy(clazz);
		T result =(T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},methodProxy);
		return result;
	}
}

class MethodProxy implements InvocationHandler {

	private Class<?> clazz;

	public MethodProxy(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		InvokerMsg msg = new InvokerMsg();
		msg.setClassName(this.clazz.getName());
		msg.setMethodname(method.getName());
		msg.setParames(method.getParameterTypes());
		msg.setValues(args);
		final RpcProxyHandler comsumerHandler = new RpcProxyHandler();
		Object responseMsg=null;
		// 配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						protected void initChannel(SocketChannel socketChannel)
								throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new ProtoDecoder(ResponseMsg.class));
							pipeline.addLast(new ProtoEncoder(InvokerMsg.class));
							pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
							pipeline.addLast(comsumerHandler);
						}
					});

			// 发起异步连接
			ChannelFuture sync = bootstrap.connect("127.0.0.1", 9090).sync();
			Channel channel = sync.channel();
			channel.writeAndFlush(msg).sync();
			// 等待客户端链路关闭
			sync.channel().closeFuture().sync();
			responseMsg=sync.get();
		} finally {
			// 优雅退出,释放NIO线程组
			group.shutdownGracefully();
		}
		return responseMsg;
	}

}
