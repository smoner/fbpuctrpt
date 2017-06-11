package uap.mw.trans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;

import uap.mw.ds.DBConnection;
import uap.mw.trans.util.ConnectionStatus;
import uap.mw.trans.util.DBLogger;

public class UAPTransaction implements javax.transaction.Transaction {

	/** ����ĵ�ǰ״̬ */
	private int m_status = javax.transaction.Status.STATUS_NO_TRANSACTION;

	/** �����е�connection��datasourceName -> Connection ���߳��ڣ�����ͬ�� */
	private Map<String, DBConnection> connecionMap = new HashMap<String, DBConnection>(
			4, 1f);

	/** �����е�ͬ������ */
	private List<Synchronization> m_ierpSynchronization = new ArrayList<Synchronization>();
	private Map<Object, Object> resourceMap = new HashMap<Object, Object>();

	/** �������������߳� */
	private java.lang.Thread m_tranThread;
	/** �����RollbackOnly��־ */
	private boolean m_bRollbackOnly = false;

	private long startTime = -1L;
	private long endTime = -1L;
	private boolean transRun = false;

	private String transID = "";

	public UAPTransaction() {
		super();
		m_status = javax.transaction.Status.STATUS_ACTIVE;
		m_tranThread = Thread.currentThread();
		transID = UUID.randomUUID().toString();
		transStart();
	}

	private void transStart() {
		startTime = System.currentTimeMillis();
		transRun = true;

	}

	private void transEnd() {
		endTime = System.currentTimeMillis();
		transRun = false;
	}

	// /**
	// * IerpTransaction �����ӡ�
	// */
	// public UAPTransaction(Thread thread) {
	// super();
	// m_tranThread = thread;
	// }

	/**
	 * �ύ����
	 */
	public void commit() throws java.lang.SecurityException,
			javax.transaction.SystemException,
			javax.transaction.RollbackException,
			javax.transaction.HeuristicMixedException,
			javax.transaction.HeuristicRollbackException {
		if (m_bRollbackOnly) {
			rollback();
		} else {
			m_status = javax.transaction.Status.STATUS_COMMITTING;
			boolean hasException = false;
			for (DBConnection conn : connecionMap.values()) {
				try {
					conn.realCommit();
					// if (shouldResetconection(ierpcon)) {// !(BMT && ����δ����) }
				} catch (Exception e1) {
					DBLogger.error(
							e1.getMessage() + ":" + conn.getDataSourceName(),
							e1);
					hasException = true;
				} finally {
					// ���ӻس�
					try {
						conn.backPool();
					} catch (SQLException e) {
						DBLogger.error(e.getMessage(), e);
					}
				}
				m_status = javax.transaction.Status.STATUS_COMMITTED;
			}
			connecionMap.clear();
			transEnd();
			if (hasException) {
				throw new SystemException("commit error,Please check log");
			}
		}
	}

	// /**
	// * �ͷ�����������Դ,(IerpConnection.close() )
	// */
	// public boolean delistConnResource(String dataSourceName, DBConnection
	// conn) {
	// connecionMap.remove(dataSourceName);
	// return true;
	// }

	/**
	 * �������������Դ����getConnectionʱ������
	 */
	public boolean enlistConnResource(String dataSourceName, DBConnection conn) {
		connecionMap.put(dataSourceName, conn);
		return true;
	}

	// /**
	// * ȡ������Context��
	// *
	// * @return nc.bs.mw.tran.IerpTransactionContext
	// */
	// public UAPTransactionContext getContext() {
	// return m_tranContext;
	// }

	/**
	 * ȡ�ûع���־��
	 */
	public boolean getRollbackOnly() {
		return m_bRollbackOnly;
	}

	public String getKey() {
		return transID;
	}

	/**
	 * ȡ������ĵ�ǰ״̬��
	 */
	public int getStatus() {
		// ::TODO
		return m_status;
	}

	/**
	 * ȡ��ͬ������
	 * 
	 * @return javax.transaction.Synchronization
	 */
	public List<Synchronization> getSynchronization() {
		return m_ierpSynchronization;
	}

	/**
	 * ȡ��ʹ�ñ�������̡߳�
	 * 
	 * @return java.lang.Thread
	 */
	public java.lang.Thread getThread() {
		return m_tranThread;
	}

	/**
	 * ע��ͬ������
	 */
	public void registerSynchronization(javax.transaction.Synchronization arg1)
			throws javax.transaction.RollbackException, IllegalStateException,
			javax.transaction.SystemException {
		m_ierpSynchronization.add(arg1);
	}

	/**
	 * �ع�����
	 */
	public void rollback() throws javax.transaction.SystemException {
		m_status = javax.transaction.Status.STATUS_ROLLING_BACK;

		boolean hasException = false;
		for (String dsName : connecionMap.keySet()) {
			DBConnection conn = connecionMap.get(dsName);
			try {
				conn.realRollback();
			} catch (SQLException e) {
				DBLogger.error(
						e.getMessage() + ":ds:" + conn.getDataSourceName(), e);
				hasException = true;
			} finally {
				// ���ӻس�
				try {
					conn.backPool();
				} catch (SQLException e) {
					DBLogger.error(e);
				}
			}
		}
		connecionMap.clear();
		m_status = javax.transaction.Status.STATUS_ROLLEDBACK;
		transEnd();
		if (hasException) {
			throw new SystemException("rollback error , please check log");
		}
	}

	// /**
	// * ���������ı���
	// *
	// * @param newContext
	// * nc.bs.mw.tran.IerpTransactionContext
	// */
	// public void setContext(UAPTransactionContext newContext) {
	// m_tranContext = newContext;
	// }

	/**
	 * ���ûع���־��
	 */
	public void setRollbackOnly() throws IllegalStateException,
			javax.transaction.SystemException {
		m_bRollbackOnly = true;
		m_status = javax.transaction.Status.STATUS_MARKED_ROLLBACK;
	}

	/**
	 * ����ͬ������
	 * 
	 * @param newSynchronization
	 *            javax.transaction.Synchronization
	 */
	// public void setSynchronization(
	// javax.transaction.Synchronization newSynchronization) {
	// // m_ierpSynchronization = newSynchronization;
	// }

	/**
	 * ����ʹ�ñ�������̡߳�
	 * 
	 * @param newThread
	 *            java.lang.Thread
	 */
	public void setThread(java.lang.Thread newThread) {
		m_tranThread = newThread;
	}

	@Override
	public boolean delistResource(XAResource arg0, int arg1)
			throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean enlistResource(XAResource arg0)
			throws IllegalStateException, RollbackException, SystemException {
		// TODO Auto-generated method stub
		return false;
	}

	public DBConnection getConnectionFromCurTrans(String dataSourceName) {
		return connecionMap.get(dataSourceName);
	}

	public Map<String, DBConnection> getConnectionMap() {
		return connecionMap;
	}

	// ��ͣ��������
	public void suspendAllConnection() {
		for (DBConnection conn : getConnectionMap().values()) {
			conn.changeStates(ConnectionStatus.INUSE_SUSPEND);
		}
	}

	// �ָ���������
	public void resumeAllConnection() {
		for (DBConnection conn : getConnectionMap().values()) {
			conn.changeStates(ConnectionStatus.INUSE);
		}
	}

	public Map<Object, Object> getResourceMap() {
		// if (getStatus() == javax.transaction.Status.STATUS_ACTIVE) {
		// throw new TransRuntimeException("transtype status error");
		// }
		return resourceMap;
	}
}
