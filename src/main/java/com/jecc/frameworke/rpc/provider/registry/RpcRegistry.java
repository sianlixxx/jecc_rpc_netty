package com.jecc.frameworke.rpc.provider.registry;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import com.jecc.frameworke.rpc.api.msgbean.InvokerMsg;
import com.jecc.frameworke.rpc.api.msgbean.ResponseMsg;
import com.jecc.frameworke.rpc.codec.ProtoDecoder;
import com.jecc.frameworke.rpc.codec.ProtoEncoder;

public class RpcRegistry {

	private int port;

	public RpcRegistry(int port) {
		this.port = port;
	}

	// 起动netty
	public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					 .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 对请求消息进行解码
                            sc.pipeline().addLast(new ProtoDecoder(InvokerMsg.class));
                            // 对返回给客户端的响应消息进行编码
                            sc.pipeline().addLast(new ProtoEncoder(ResponseMsg.class));
                            // 加入TCP粘包解码器
                            sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                            // 将获取到的serviceBean交给服务器处理器处理
                            sc.pipeline().addLast(new RegistryHandler());
                        }
                    });
			// 绑定端口,等待同步成功
			ChannelFuture sync = bootstrap.bind(port).sync();
			// 等待服务监听端口关闭
			sync.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 优雅退出,释放线程资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

}
