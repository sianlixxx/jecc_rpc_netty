package com.jecc.frameworke.rpc.api.msgbean;

import java.io.Serializable;

public class InvokerMsg  implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1730936903315057935L;
	private String className; //类
	private String methodname;//函数名
	private Class<?>[] parames;//参数列表
	private Object[] values;//参数类型
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public Class<?>[] getParames() {
		return parames;
	}
	public void setParames(Class<?>[] parames) {
		this.parames = parames;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
	
}
