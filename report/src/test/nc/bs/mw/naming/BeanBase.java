package nc.bs.mw.naming;

import java.rmi.RemoteException;

import javax.ejb.EJBLocalObject;

import nc.bs.logging.Logger;
import nc.middleware.tran.IContainerTransProxy;
import nc.middleware.tran.IerpUserTransaction;
import uap.mw.trans.UAPUserTransanction;

public class BeanBase implements EJBLocalObject, IMethodTransectionType {

	/** 保存当前方法的TransactionType */
	private int currentMethodTransectionType = 0;

	/** EJBBean 存放的变量 */
	private Object m_Ejb;

	/** EJBHome的引用 */
	private javax.ejb.EJBLocalHome m_EJBHome;

	/** Transaction Manager Proxy 和Transaction 进行交互的对象 */
	private IContainerTransProxy m_IerpTransactionManagerProxy;

	/** 实现UserTransaction的对象 */
	private UAPUserTransanction m_IerpUserTransaction;

	/** 最后一次访问的时间，当长时间不用会被系统清理掉 */
	private long lastCallTime;

	/**
	 * BeanBase 构造子注解。
	 */
	public BeanBase() {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.BeanBase()");

		currentMethodTransectionType = 0;
		// registerBeanBase();
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.BeanBase() over");
	}

	/**
	 * Interface的方法调用结束，进行资源释放，TRSACTION 提交过程。
	 */
	protected void afterCallMethod(int methodId, Exception exception)
			throws RemoteException {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.afterCallMethod");

		boolean isCmt = ((HomeBase) getEJBLocalHome()).getEJBBeanDescriptor()
				.isCmt();
		if (isCmt) {
			getIerpTransactionManagerProxy().end(exception);
			setIerpTransactionManagerProxy(null);

		} else {
			/** BEAN_MANAGE_TRANSACTION 解除Transaction的绑定 */
			getIerpUserTransaction().unbindCurrentThread();
		}
		Logger.info("End Transaction(" + methodId + ")");
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.afterCallMethod over");

	}

	/**
	 * 在任何Session方法前调用本方法， 本方法判断CMT , BMP ,以及调用Transaction Service
	 */
	protected void beforeCallMethod(int methodId) {
		/** register EJB */
		Logger.info("Begin Transaction(" + methodId + ")");
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.beforeCallMethod");
		setLastCallTime(System.currentTimeMillis());
		boolean isCmt = ((HomeBase) getEJBLocalHome()).getEJBBeanDescriptor()
				.isCmt();
		if (isCmt) {
			/** CMT 和方法 相关 */
			try {

				currentMethodTransectionType = getMethodTransectionType(methodId);
				int isolateLevel = getMethodIsolateLevelType(methodId);
				// setIerpTransactionManagerProxy(new
				// IerpTransactionManagerProxy());
				setIerpTransactionManagerProxy(TransactionFactory.getTMProxy());
				getIerpTransactionManagerProxy().begin(
						currentMethodTransectionType, isolateLevel);
			} catch (Exception e) {
				Logger.error("BeforeCallMethod", e);
			}
		} else {
			/** BMT 和Bean相关 */
			if (getIerpUserTransaction() == null) {
				setIerpTransactionManagerProxy(null);
				// setIerpUserTransaction(new IerpUserTransaction());
				setIerpUserTransaction(TransactionFactory.getUTransaction());
				// getIerpUserTransaction().setIerpTransactionManagerProxy(
				// new IerpTransactionManagerProxy());
				// ::TODO
				// getIerpUserTransaction().setIerpTransactionManagerProxy(TransactionFactory.getTMProxy());
			}
			getIerpUserTransaction().bindToCurrentThread();
		}

		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.beforeCallMethod over");
	}

	public java.lang.Object getPrimaryKey() {
		return null;
	}

	/**
	 * 解除所有相关资源 创建日期：(2001-2-27 10:09:03)
	 */
	public void destory() {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.destory ");
		if (getIerpUserTransaction() != null) {
			getIerpUserTransaction().destoryThisTransaction();
		}
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.destory over ");
	}

	public void finalize() throws Throwable {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.finalize ");
		try {
			if (((HomeBase) getEJBLocalHome()).getEJBMetaData().isSession()) {
				remove();
			}
		} catch (Exception e) {
		}
		super.finalize();
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.finalize over ");
	}

	public Object getEjb() {
		return m_Ejb;
	}

	public javax.ejb.EJBLocalHome getEJBLocalHome() {
		return m_EJBHome;
	}

	public java.util.Properties getEnvironment() {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.getEnvironment ");
		return ((HomeBase) m_EJBHome).getEnv();
	}

	/**
	 * 管理器只负着把Handle传送到客户端 getHandle 方法注解。
	 */

	public javax.ejb.Handle getHandle() {
		// return m_EjbHandle;
		return null;
	}

	private IContainerTransProxy getIerpTransactionManagerProxy() {
		return m_IerpTransactionManagerProxy;
	}

	public UAPUserTransanction getIerpUserTransaction() {
		return m_IerpUserTransaction;
	}

	private int getMethodIsolateLevelType(int methodId) {
		HomeBase hb = (HomeBase) getEJBLocalHome();
		int id = methodId - IMethodTransectionType.BUSENESS_METHOD_BASE;
		int methodTransectionType = hb.getIsolateLevel()[id];
		return methodTransectionType;
	}

	private int getMethodTransectionType(int methodId) {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.getMethodTransectionType ");
		HomeBase hb = (HomeBase) getEJBLocalHome();
		int id = methodId - IMethodTransectionType.BUSENESS_METHOD_BASE;
		int methodTransectionType = hb.getBusinessMethodTransectoinType()[id];
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.getMethodTransectionType over");
		return methodTransectionType;
	}

	/**
	 * getRollbackOnly 方法注解。
	 */
	public boolean getRollbackOnly() {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.getRollbackOnly ");
		return m_IerpTransactionManagerProxy.getRollbackOnly();
	}

	public javax.transaction.UserTransaction getUserTransaction() {
		return getIerpUserTransaction();
	}

	/**
	 * isIdentical 方法注解。
	 */

	public boolean isIdentical(javax.ejb.EJBLocalObject arg1) {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.isIdentical ");
		return true;
	}

	/**
	 * 
	 * 当当前的ejb地方法应为系统ThreadDeath发生时，<br>
	 * 需要强制释放资源。
	 * 
	 */
	public void methodEndWithThreadDeath() {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.MethodEndWithThreadDeath ");
		if (m_IerpTransactionManagerProxy != null) {
			m_IerpTransactionManagerProxy.endThreadDeath();
		}
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.MethodEndWithThreadDeath over ");
	}

	/**
	 * remove 方法注解。
	 */
	public void remove() throws javax.ejb.RemoveException {
		nc.bs.mw.itf.MwTookit.setThreadState("nc.bs.mw.naming.BeanBase.remove");

		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.remove over");
	}

	protected void setEJBLocalHome(javax.ejb.EJBLocalHome newEJBHome) {
		m_EJBHome = newEJBHome;
	}

	private void setIerpTransactionManagerProxy(
			IContainerTransProxy newIerpTransactionManagerProxy) {
		m_IerpTransactionManagerProxy = newIerpTransactionManagerProxy;
	}

	private void setIerpUserTransaction(
			UAPUserTransanction newIerpUserTransaction) {
		m_IerpUserTransaction = newIerpUserTransaction;
	}

	/**
	 * setRollbackOnly 方法注解。
	 */
	public void setRollbackOnly() {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.setRollbackOnly");
		try {
			m_IerpTransactionManagerProxy.setRollbackOnly();
		} catch (Exception e) {
		}
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.setRollbackOnly over");
	}

	/**
	 * called by HomeBase
	 * 
	 */
	public void setSessionBean(javax.ejb.SessionBean newSessionBean) {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.setSessionBean");
		m_Ejb = newSessionBean;
		if (newSessionBean != null) {
			// ligang remark
			/*
			 * newSessionBean.setSessionContext(this); if (newSessionBean
			 * instanceof javax.ejb.SessionSynchronization) {
			 * setSessionSynchronization((javax.ejb.SessionSynchronization)
			 * newSessionBean); }
			 */
			// ligang remark end
		}
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.setSessionBean over");
	}

	/**
	 * 取得最后一次调用的时间
	 */
	public long getLastCallTime() {
		return lastCallTime;
	}

	/**
	 * 系统内部调用。设置最后一次调用本对象的时间<br>
	 * 
	 * @param newLastCallTime
	 *            long 最后一次调用本对象的时间,在中间件产生的_S中自动调用本方法
	 */
	public void setLastCallTime(long newLastCallTime) {
		lastCallTime = newLastCallTime;
	}

}