package nc.bs.mw.naming;

import java.rmi.RemoteException;

import javax.ejb.EJBLocalObject;

import nc.bs.logging.Logger;
import nc.middleware.tran.IContainerTransProxy;
import nc.middleware.tran.IerpUserTransaction;
import uap.mw.trans.UAPUserTransanction;

public class BeanBase implements EJBLocalObject, IMethodTransectionType {

	/** ���浱ǰ������TransactionType */
	private int currentMethodTransectionType = 0;

	/** EJBBean ��ŵı��� */
	private Object m_Ejb;

	/** EJBHome������ */
	private javax.ejb.EJBLocalHome m_EJBHome;

	/** Transaction Manager Proxy ��Transaction ���н����Ķ��� */
	private IContainerTransProxy m_IerpTransactionManagerProxy;

	/** ʵ��UserTransaction�Ķ��� */
	private UAPUserTransanction m_IerpUserTransaction;

	/** ���һ�η��ʵ�ʱ�䣬����ʱ�䲻�ûᱻϵͳ����� */
	private long lastCallTime;

	/**
	 * BeanBase ������ע�⡣
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
	 * Interface�ķ������ý�����������Դ�ͷţ�TRSACTION �ύ���̡�
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
			/** BEAN_MANAGE_TRANSACTION ���Transaction�İ� */
			getIerpUserTransaction().unbindCurrentThread();
		}
		Logger.info("End Transaction(" + methodId + ")");
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.afterCallMethod over");

	}

	/**
	 * ���κ�Session����ǰ���ñ������� �������ж�CMT , BMP ,�Լ�����Transaction Service
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
			/** CMT �ͷ��� ��� */
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
			/** BMT ��Bean��� */
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
	 * ������������Դ �������ڣ�(2001-2-27 10:09:03)
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
	 * ������ֻ���Ű�Handle���͵��ͻ��� getHandle ����ע�⡣
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
	 * getRollbackOnly ����ע�⡣
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
	 * isIdentical ����ע�⡣
	 */

	public boolean isIdentical(javax.ejb.EJBLocalObject arg1) {
		nc.bs.mw.itf.MwTookit
				.setThreadState("nc.bs.mw.naming.BeanBase.isIdentical ");
		return true;
	}

	/**
	 * 
	 * ����ǰ��ejb�ط���ӦΪϵͳThreadDeath����ʱ��<br>
	 * ��Ҫǿ���ͷ���Դ��
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
	 * remove ����ע�⡣
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
	 * setRollbackOnly ����ע�⡣
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
	 * ȡ�����һ�ε��õ�ʱ��
	 */
	public long getLastCallTime() {
		return lastCallTime;
	}

	/**
	 * ϵͳ�ڲ����á��������һ�ε��ñ������ʱ��<br>
	 * 
	 * @param newLastCallTime
	 *            long ���һ�ε��ñ������ʱ��,���м��������_S���Զ����ñ�����
	 */
	public void setLastCallTime(long newLastCallTime) {
		lastCallTime = newLastCallTime;
	}

}