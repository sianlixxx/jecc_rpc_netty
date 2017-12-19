package com.jecc.frameworke.rpc.consumer.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jecc.frameworke.rpc.api.msgbean.ResponseMsg;
import com.jecc.frameworke.rpc.util.SerializableUtils;

public class RpcProxyHandler extends ChannelInboundHandlerAdapter {
	private final static Logger logger =LoggerFactory.getLogger(RpcProxyHandler.class);
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ResponseMsg responsMsg=(ResponseMsg)msg;
		Class<?> clazz=responsMsg.getReturnType();
		
		System.out.println("result:"+responsMsg.getData());
		logger.info("result:",responsMsg.getData());
		 logger.debug("recv from server:[%s]", responsMsg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
	}

}
