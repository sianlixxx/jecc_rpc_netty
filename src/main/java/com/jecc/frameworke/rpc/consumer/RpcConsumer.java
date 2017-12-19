package com.jecc.frameworke.rpc.consumer;

import com.jecc.frameworke.rpc.api.IRpcHello;
import com.jecc.frameworke.rpc.consumer.proxy.RpcProxy;


public class RpcConsumer {
	public static void main(String[] args) {
	
		IRpcHello iRpcHello=RpcProxy.create(IRpcHello.class);
		
		String result=iRpcHello.hello("李栋");
		System.out.println("========== sayHello ========="+result);
		String result2=iRpcHello.hello("jecc");
		System.out.println("========== echoStudent ========="+result2);
	}
}
