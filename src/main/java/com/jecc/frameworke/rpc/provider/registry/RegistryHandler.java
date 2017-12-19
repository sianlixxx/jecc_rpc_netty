package com.jecc.frameworke.rpc.provider.registry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jecc.frameworke.rpc.annotation.RpcService;
import com.jecc.frameworke.rpc.api.msgbean.InvokerMsg;
import com.jecc.frameworke.rpc.api.msgbean.ResponseMsg;
import com.jecc.frameworke.rpc.util.ClassUtils;
import com.jecc.frameworke.rpc.util.ReflectionUtils;

public class RegistryHandler extends ChannelInboundHandlerAdapter {
	private final static Logger logger =LoggerFactory.getLogger(RegistryHandler.class);
	private Map<String, Object> registryMap = new ConcurrentHashMap<String, Object>();
	private Set<Class<?>> classSet = new HashSet<>();

	public RegistryHandler() {
		scannerClass("com.jecc.frameworke.rpc.api");
		doRegister();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Object result = new Object();
		InvokerMsg request = (InvokerMsg) msg;
		Method	method=null;
		if (registryMap.containsKey(request.getClassName())) {
			Object service = registryMap.get(request.getClassName());
			   Class<?> clazz = service.getClass();
			method = clazz.getDeclaredMethod(request.getMethodname(), request.getParames());
		    method.setAccessible(true);
			result = method.invoke(service, request.getValues());
			logger.info("result:",result);
		}
		ResponseMsg responseMsg=new ResponseMsg();
		responseMsg.setData(result);
	//	responseMsg.setReturnType(method.getReturnType());
		ctx.write(responseMsg);
		ctx.flush();
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	private void doRegister() {
		for (Class<?> clazz : classSet) {
			 RpcService rpcService =clazz.getAnnotation(RpcService.class);
             String serviceName =clazz.getInterfaces()[0].getName();
             Class<?> interfaceClass=rpcService.interfaceClass();
             String version = rpcService.version();
             if (version != null && !"".equals(version.trim())) {
                 serviceName = serviceName + "-" + version;
             }
             Object obj = ReflectionUtils.newInstance(clazz);
             // 注册服务
             registryMap.put(serviceName, obj);
		}
	}

	private void scannerClass(String baseScanPackage) {
		Set<Class<?>> CLASS_SET = ClassUtils.getClassSet(baseScanPackage);
		for (Class<?> clazz : CLASS_SET) {
			if (clazz.isAnnotationPresent(RpcService.class)) {
				classSet.add(clazz);
			}
		}
	}

}
