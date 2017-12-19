package com.jecc.frameworke.rpc.api.impl;

import com.jecc.frameworke.rpc.annotation.RpcService;
import com.jecc.frameworke.rpc.api.IRpcCalc;
@RpcService(interfaceName="iRpcCalcImpl",interfaceClass=IRpcCalc.class)
public class IRpcCalcImpl implements IRpcCalc {

	@Override
	public int add(int a, int b) {
		return a + b;
	}

	@Override
	public int minus(int a, int b) {
		return a - b;
	}

	@Override
	public int multiply(int a, int b) {
		return a * b;
	}

	@Override
	public int divide(int a, int b) {
		return a / b;
	}

}
