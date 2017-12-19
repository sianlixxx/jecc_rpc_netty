package com.jecc.frameworke.rpc.api.msgbean;

import java.io.Serializable;

public class ResponseMsg  implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1399274791284007205L;
	/**
	 * 返回值类型
	 */
	private Class<?>  returnType;
	private Object data;

	// getter/setter
	public ResponseMsg() {
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response{" + "data=" + data + '}';
	}



	public Class<?> getReturnType() {
		return returnType;
	}



	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
}
