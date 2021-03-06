package com.jecc.frameworke.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释一个类为Rpc服务类的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
	
    String interfaceName();
    
    Class<?> interfaceClass() default void.class;

    String version() default "";
}
