package nc.itf.framework.ejb;

import nc.bs.logging.Logger;

/**
 * @author hgy
 * 
 *         business service ejb wrapper Created by EJBGenerator based on
 *         velocity technology
 */

public class CMTProxy_Local extends nc.bs.mw.naming.BeanBase implements
		nc.itf.framework.ejb.CMTProxyEjbObject {

	public CMTProxy_Local() {
		super();
	}

	private nc.itf.framework.ejb.CMTProxyEjbBean _getBeanObject()
			throws java.rmi.RemoteException {
		return (nc.itf.framework.ejb.CMTProxyEjbBean) getEjb();
	}

	public java.lang.Object delegate_RequiresNew(java.lang.Object arg0,
			java.lang.reflect.Method arg1, java.lang.Object[] arg2)
			throws Exception {
		// Logger.error("---------CMTProxy_Local---delegate_RequiresNew");
		beforeCallMethod(nc.itf.framework.ejb.CMTProxy_Method_Const_Local.Method_delegate_RequiresNew$Object_arg0$Method_arg1$ObjectS_arg2);
		Exception er = null;
		java.lang.Object o = null;
		try {
			nc.bs.framework.core.TxMark.enterTx("RequiresNew");
			o = (java.lang.Object) _getBeanObject().delegate_RequiresNew(arg0,
					arg1, arg2);
		} catch (Exception e) {
			er = e;
			nc.bs.framework.core.TxMark.recordCause(er);
		} catch (Throwable thr) {
			nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
			er = new nc.bs.framework.exception.FrameworkEJBException(
					"Fatal unknown error", thr);
			nc.bs.framework.core.TxMark.recordCause(er);
		}
		try {
			afterCallMethod(
					nc.itf.framework.ejb.CMTProxy_Method_Const_Local.Method_delegate_RequiresNew$Object_arg0$Method_arg1$ObjectS_arg2,
					er);
		} catch (java.rmi.RemoteException remoteException) {
			nc.bs.logging.Logger.error(
					"HGY: Unexpected error when call afterCallMethod",
					remoteException);
		} finally {
			nc.bs.framework.core.TxMark.leaveTx("RequiresNew");
		}
		if (null != er) {
			throw er;
		}
		return o;
	}

	public java.lang.Object delegate(java.lang.Object arg0,
			java.lang.reflect.Method arg1, java.lang.Object[] arg2)
			throws Exception {
		// Logger.error("---------CMTProxy_Local---delegate");
		beforeCallMethod(nc.itf.framework.ejb.CMTProxy_Method_Const_Local.Method_delegate$Object_arg0$Method_arg1$ObjectS_arg2);
		Exception er = null;
		java.lang.Object o = null;
		try {
			nc.bs.framework.core.TxMark.enterTx("Requires");
			o = (java.lang.Object) _getBeanObject().delegate(arg0, arg1, arg2);
		} catch (Exception e) {
			er = e;
			nc.bs.framework.core.TxMark.recordCause(er);
		} catch (Throwable thr) {
			nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
			er = new nc.bs.framework.exception.FrameworkEJBException(
					"Fatal unknown error", thr);
		}
		try {
			afterCallMethod(
					nc.itf.framework.ejb.CMTProxy_Method_Const_Local.Method_delegate$Object_arg0$Method_arg1$ObjectS_arg2,
					er);
			nc.bs.framework.core.TxMark.recordCause(er);
		} catch (java.rmi.RemoteException remoteException) {
			nc.bs.logging.Logger.error(
					"HGY: Unexpected error when call afterCallMethod",
					remoteException);
		} finally {
			nc.bs.framework.core.TxMark.leaveTx("Requires");
		}
		if (null != er) {
			throw er;
		}
		return o;
	}
}