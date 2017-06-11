package nc.bs.framework.ejb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import nc.itf.framework.ejb.CMTProxy;

/**
 * 
 * Created by UFSoft. User: ºÎ¹ÚÓî
 * 
 * Date: 2007-11-1 Time: ÏÂÎç04:07:42
 * 
 */
public class CMTEJBServiceHandler implements InvocationHandler {
	
	private CMTProxy cmtProxy;

	private Object wrapped;

	public CMTEJBServiceHandler(CMTProxy proxy, Object wrapped) {
		this.cmtProxy = proxy;
		this.wrapped = wrapped;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		if (method.getName().endsWith("_RequiresNew")) {
			return cmtProxy.delegate_RequiresNew(wrapped, method, args);
		} else {
			return cmtProxy.delegate(wrapped, method, args);
		}

	}

}