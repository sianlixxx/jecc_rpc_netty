package com.jecc.frameworke.rpc.api.impl;

import com.jecc.frameworke.rpc.annotation.RpcService;
import com.jecc.frameworke.rpc.api.IRpcHello;

@RpcService(interfaceName="iRpcHelloImpl",interfaceClass=IRpcHello.class)
public class IRpcHelloImpl implements IRpcHello{

	@Override
	public String hello(String name) {
		return "欢迎您："+name;
	}

}
