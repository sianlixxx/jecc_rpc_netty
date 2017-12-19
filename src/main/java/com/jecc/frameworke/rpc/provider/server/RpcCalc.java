package com.jecc.frameworke.rpc.provider.server;

import com.jecc.frameworke.rpc.provider.registry.RpcRegistry;

public class RpcCalc {
	public static void main(String[] args) {
		RpcRegistry server = new RpcRegistry(9090);
		server.start();

	}
}
