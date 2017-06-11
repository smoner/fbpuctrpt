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

	/** 事务的当前状态 */
	private int m_status = javax.transaction.Status.STATUS_NO_TRANSACTION;

	/** 事务中的connection，datasourceName -> Connection 单线程内，无需同步 */
	private Map<String, DBConnection> connecionMap = new HashMap<String, DBConnection>(
			4, 1f);

	/** 事务中的同步对象 */
	private List<Synchronization> m_ierpSynchronization = new ArrayList<Synchronization>();
	private Map<Object, Object> resourceMap = new HashMap<Object, Object>();

	/** 事务所关联的线程 */
	private java.lang.Thread m_tranThread;
	/** 事务的RollbackOnly标志 */
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
	// * IerpTransaction 构造子。
	// */
	// public UAPTransaction(Thread thread) {
	// super();
	// m_tranThread = thread;
	// }

	/**
	 * 提交事务。
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
					// if (shouldResetconection(ierpcon)) {// !(BMT && 服务未结束) }
				} catch (Exception e1) {
					DBLogger.error(
							e1.getMessage() + ":" + conn.getDataSourceName(),
							e1);
					hasException = true;
				} finally {
					// 链接回池
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
	// * 释放数据连接资源,(IerpConnection.close() )
	// */
	// public boolean delistConnResource(String dataSourceName, DBConnection
	// conn) {
	// connecionMap.remove(dataSourceName);
	// return true;
	// }

	/**
	 * 添加数据连接资源，在getConnection时被调用
	 */
	public boolean enlistConnResource(String dataSourceName, DBConnection conn) {
		connecionMap.put(dataSourceName, conn);
		return true;
	}

	// /**
	// * 取得事务Context。
	// *
	// * @return nc.bs.mw.tran.IerpTransactionContext
	// */
	// public UAPTransactionContext getContext() {
	// return m_tranContext;
	// }

	/**
	 * 取得回滚标志。
	 */
	public boolean getRollbackOnly() {
		return m_bRollbackOnly;
	}

	public String getKey() {
		return transID;
	}

	/**
	 * 取得事务的当前状态。
	 */
	public int getStatus() {
		// ::TODO
		return m_status;
	}

	/**
	 * 取得同步器。
	 * 
	 * @return javax.transaction.Synchronization
	 */
	public List<Synchronization> getSynchronization() {
		return m_ierpSynchronization;
	}

	/**
	 * 取得使用本事务的线程。
	 * 
	 * @return java.lang.Thread
	 */
	public java.lang.Thread getThread() {
		return m_tranThread;
	}

	/**
	 * 注册同步器。
	 */
	public void registerSynchronization(javax.transaction.Synchronization arg1)
			throws javax.transaction.RollbackException, IllegalStateException,
			javax.transaction.SystemException {
		m_ierpSynchronization.add(arg1);
	}

	/**
	 * 回滚事务。
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
				// 链接回池
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
	// * 设置事务文本。
	// *
	// * @param newContext
	// * nc.bs.mw.tran.IerpTransactionContext
	// */
	// public void setContext(UAPTransactionContext newContext) {
	// m_tranContext = newContext;
	// }

	/**
	 * 设置回滚标志。
	 */
	public void setRollbackOnly() throws IllegalStateException,
			javax.transaction.SystemException {
		m_bRollbackOnly = true;
		m_status = javax.transaction.Status.STATUS_MARKED_ROLLBACK;
	}

	/**
	 * 设置同步器。
	 * 
	 * @param newSynchronization
	 *            javax.transaction.Synchronization
	 */
	// public void setSynchronization(
	// javax.transaction.Synchronization newSynchronization) {
	// // m_ierpSynchronization = newSynchronization;
	// }

	/**
	 * 设置使用本事务的线程。
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

	// 暂停所有连接
	public void suspendAllConnection() {
		for (DBConnection conn : getConnectionMap().values()) {
			conn.changeStates(ConnectionStatus.INUSE_SUSPEND);
		}
	}

	// 恢复所有链接
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
