package nc.bs.framework.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nc.itf.framework.ejb.CMTProxy;

/**
 * 
 * Created by UFSoft.
 * User: ºÎ¹ÚÓî
 *
 * Date: 2007-11-1
 * Time: ÉÏÎç10:10:33
 *
 */
public class CMTProxyImpl implements CMTProxy {

    public Object delegate(Object target, Method m, Object[] args) throws Exception {
        Object ret;
        try {
            ret = m.invoke(target, args);
            return ret;
        } catch (InvocationTargetException e) {
        	if(e.getTargetException() instanceof Exception) {
        		throw (Exception)e.getTargetException();
        	} else {
        		throw e;
        	}
          
        }

    }

    public Object delegate_RequiresNew(Object target, Method m, Object[] args) throws Exception {
        try {
            Object ret = m.invoke(target, args);
            return ret;
        } catch (InvocationTargetException e) {
        	if(e.getTargetException() instanceof Exception) {
        		throw (Exception)e.getTargetException();
        	} else {
        		throw e;
        	}
          
        }

    }

}
