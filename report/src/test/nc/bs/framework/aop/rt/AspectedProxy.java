package nc.bs.framework.aop.rt;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import nc.bs.framework.exception.FrameworkRuntimeException;
import nc.bs.framework.util.Attrs;

/**
 * 
 * @author ºÎ¹ÚÓî
 * 
 */
public class AspectedProxy implements InvocationHandler {

	private Object thisObject;

	private final AdviceHolderChain chain;

	private Attrs<Object> attrs;

	public AspectedProxy(Object a) {
		this.thisObject = a;
		chain = new AdviceHolderChain();
		attrs = new  Attrs<Object>();
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			AspectContextImpl.push(new AspectContextImpl(thisObject, proxy));
			if (method.getDeclaringClass() == AdviceHolderChainProvider.class) {
				if (method.getName().equals("getAdviceHolderChain")) {
					return chain;
				} else if (method.getName().equals("internal_attrs")) {
					return attrs;
				}
			}

			if (method.getName().equals("equals")
					&& method.getParameterTypes().length == 1) {
				Object value = args[0];
				AspectedProxy other = getAspectedProxy(value);
				if (other != null) {
					return thisObject.equals(other.thisObject);
				} else {
					return thisObject.equals(value);
				}

			} else if (method.getName().equals("hashCode")
					&& method.getParameterTypes().length == 0) {
				return thisObject.hashCode();

			} else if (method.getName().equals("toString")) {
				return thisObject.toString();
			}

			return new MethodProceedingJoinpoint(thisObject, proxy, method,
					args, chain.iterate()).proceed();
		} catch (Throwable thr) {
			if (thr instanceof RuntimeException) {
				throw (RuntimeException) thr;
			}
			if (thr instanceof Error) {
				throw (Error) thr;
			}
			Class<?>[] thrClasses = method.getExceptionTypes();
			if (thrClasses == null) {
				throw new FrameworkRuntimeException("not declared in throws",
						thr);
			} else {
				boolean isDeclared = false;
				for (Class<?> tc : thrClasses) {
					if (tc.isInstance(thr)) {
						isDeclared = true;
						break;
					}
				}
				if (!isDeclared) {
					throw new FrameworkRuntimeException(
							"not declare in throws clause", thr);
				}
				throw thr;
			}
		} finally {
			AspectContextImpl.pop();
		}
	}

	public AdviceHolderChain getAdviceHolderChain() {
		return chain;
	}

	public static AspectedProxy getAspectedProxy(Object obj) {
		if (Proxy.isProxyClass(obj.getClass())) {
			InvocationHandler ih = Proxy.getInvocationHandler(obj);
			if (ih instanceof AspectedProxy) {
				return (AspectedProxy) ih;
			}
		}
		return null;
	}

}
