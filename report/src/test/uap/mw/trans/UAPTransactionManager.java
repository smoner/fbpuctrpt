package uap.mw.trans;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import nc.bs.logging.Log;
import nc.bs.mw.tran.IIerpTransactionProp;
import nc.bs.mw.tran.IerpTransactionManager;
import uap.mw.ds.DBConnection;
import uap.mw.trans.itf.IUAPTransactionManager;
import uap.mw.trans.util.DBLogger;
import uap.mw.trans.util.TransactionContextType;

/**
 * 线程安全，无需同步
 * 
 * @author dingxm
 * 
 */
class UAPTransactionManager implements IUAPTransactionManager,
		javax.transaction.TransactionManager {
	private static Log log = Log.getInstance(IerpTransactionManager.class);
	private Thread curThread;
	/** 事务运行的最大时间 */
	private int m_nTranTimeOut;

	/**
	 * transaction的类型 ，为适配javax.transaction.TransactionManager
	 * 添加，供begin方法使用，启新事务会覆盖旧的
	 */
	private int m_tranProp;

	@SuppressWarnings("serial")
	private LinkedList<UAPTransactionContext> tranStack = new LinkedList<UAPTransactionContext>() {

		public UAPTransactionContext pop() {
			UAPTransactionContext curContext = super.pop();
			if (curContext.getTransType() == TransactionContextType.SOURCE
					|| curContext.getTransType() == TransactionContextType.NULL) {
				resumeCurTrans();
			}
			return curContext;
		};

		public void push(UAPTransactionContext item) {
			if (item.getTransType() == TransactionContextType.SOURCE
					|| item.getTransType() == TransactionContextType.NULL) {
				suspendCurTrans();
			}
			super.push(item);
		};

	};

	public void begin(int transType) throws NotSupportedException,
			SystemException {
		switch (transType) {
		case IIerpTransactionProp.TRANSACTION_REQUIRED:
			if (tranStack.isEmpty()) {
				createTransaction(TransactionContextType.SOURCE);
			} else {
				createTransaction(TransactionContextType.JOINED);
			}
			break;
		case IIerpTransactionProp.TRANSACTION_REQUIRESNEW:
			createTransaction(TransactionContextType.SOURCE);
			break;
		case IIerpTransactionProp.TRANSACTION_MANDATORY:
			if (tranStack.isEmpty()) {
				throw new SystemException();
			} else {
				createTransaction(TransactionContextType.JOINED);
			}
			break;
		case IIerpTransactionProp.TRANSACTION_NEVER:
			if (!tranStack.isEmpty()) {
				throw new SystemException();
			}
			createTransaction(TransactionContextType.NULL);
			break;
		case IIerpTransactionProp.TRANSACTION_SUPPORTS:
			if (!tranStack.isEmpty()) {
				createTransaction(TransactionContextType.NULL);
			} else {
				createTransaction(TransactionContextType.JOINED);
			}
			break;
		case IIerpTransactionProp.TRANSACTION_NOTSUPPORTED:
			createTransaction(TransactionContextType.NULL);
			break;
		case IIerpTransactionProp.TRANSACTION_NESTED: // EJB3.0规范可以不支持
			createTransaction(TransactionContextType.JOINED);
			try {
				setCurInvokeSavePoint();
			} catch (SQLException e) {
				throw new NotSupportedException("savePoint error!");
			}
			break;
		default:
			throw new NotSupportedException("trans type error!");
		}
	}

	// /**
	// * @deprecated
	// * @param transType
	// * @param needRoolBackPoint
	// * @throws NotSupportedException
	// * @throws SystemException
	// */
	// public void begin1(int transType, boolean needRoolBackPoint)
	// throws NotSupportedException, SystemException {
	//
	// if (isNewTrans(transType)) {
	// createTransaction(TransactionContextType.SOURCE);
	// } else if (isJoinTrans(transType)) {
	// createTransaction(TransactionContextType.JOINED);
	// if (needRoolBackPoint) {
	// try {
	// setCurInvokeSavePoint();
	// } catch (SQLException e) {
	// throw new NotSupportedException("savePoint error!");
	// }
	// }
	// } else if (noTrans(transType)) {
	// // doNothing
	// } else {
	// throw new NotSupportedException("trans type error!");
	// }
	// }

	private void setCurInvokeSavePoint() throws SQLException {
		UAPTransactionContext cotext = getTranContext();
		cotext.setNeedRBPoint(true);
		UAPTransaction trans = (UAPTransaction) getTransaction();
		Map<String, DBConnection> connMap = trans.getConnectionMap();
		Map<String, Savepoint> savePointMap = new HashMap<String, Savepoint>(4,
				1f);
		for (String dsName : connMap.keySet()) {
			DBConnection conn = connMap.get(dsName);
			Savepoint point = conn.setSavepoint();
			savePointMap.put(dsName, point);
		}
		cotext.setSavePointMap(savePointMap);
	}

	@Override
	public void rollBackToCurInvokePoint() throws SQLException {
		UAPTransactionContext curTransContext = tranStack.pop();
		UAPTransaction trans = (UAPTransaction) getTransaction();
		Map<String, Savepoint> savePointMap = curTransContext.getSavePointMap();
		Map<String, DBConnection> connMap = trans.getConnectionMap();
		for (String dsName : connMap.keySet()) {
			DBConnection conn = connMap.get(dsName);
			Savepoint sPoint = savePointMap.get(dsName);
			if (sPoint != null) {
				conn.realRollback(sPoint);
			} else {
				conn.realRollback();
			}
		}
	}

	private UAPTransactionContext createTransaction(
			TransactionContextType transType) throws SystemException {
		UAPTransaction uapTran = null;
		if (transType == TransactionContextType.SOURCE) {
			uapTran = new UAPTransaction();
		}
		if (transType == TransactionContextType.JOINED) {
			if (tranStack.isEmpty()) {
				throw new SystemException("no source Transaction,can not join ");
			}
			uapTran = (UAPTransaction) getTranContext().getTransaction();
		} else {
			uapTran = new UAPTransaction();
		}

		UAPTransactionContext tranText = new UAPTransactionContext(uapTran);
		tranText.setTransType(transType);
		tranStack.push(tranText);
		return tranText;

	}

	private void suspendCurTrans() {
		if (!tranStack.isEmpty()) {
			UAPTransactionContext transContext = tranStack.peek();
			UAPTransaction uapTran = (UAPTransaction) transContext
					.getTransaction();
			uapTran.suspendAllConnection();
		}
	}

	private void resumeCurTrans() {
		if (!tranStack.isEmpty()) {
			UAPTransactionContext transContext = tranStack.peek();
			UAPTransaction uapTran = (UAPTransaction) transContext
					.getTransaction();
			uapTran.resumeAllConnection();
		}
	}

	// private boolean isJoinTrans(int prop) {
	// if (noTrans(prop) || isNewTrans(prop)) {
	// return false;
	// }
	// return true;
	// }
	//
	// private boolean noTrans(int prop) {
	// // if (tranStack.size() == 0) {
	// // if (prop == IIerpTransactionProp.TRANSACTION_SUPPORTS) {
	// // return true;
	// // }
	// // }
	// return false;
	// }
	//
	// private boolean isNewTrans(int prop) {
	// if (isUserTransaction()) {
	// return true;
	// } else {
	// if (tranStack.isEmpty()) {
	// return prop == IIerpTransactionProp.TRANSACTION_REQUIRED
	// || prop == IIerpTransactionProp.TRANSACTION_REQUIRESNEW;
	// } else {
	// return prop == IIerpTransactionProp.TRANSACTION_REQUIRESNEW;
	// }
	// }
	// }

	@Override
	public void commit() throws RollbackException, IllegalStateException,
			SystemException, HeuristicRollbackException, SecurityException,
			HeuristicMixedException {
		UAPTransactionContext curTransContext = tranStack.pop();
		if (curTransContext != null) {
			if (!curTransContext.isNullTrans() && !curTransContext.isJoined()) {
				try {
					Transaction curTran = curTransContext.getTransaction();
					List<Synchronization> synList = ((UAPTransaction) curTran)
							.getSynchronization();
					if (synList != null && synList.size() > 0) {
						// try {
						for (Synchronization syn : synList) {
							syn.beforeCompletion();
						}
						// } catch (Exception e) {
						// Logger.error("beforeCompletion error", e);
						// curTran.rollback();
						// return;
						// }
					}
					try {
						curTran.commit();
					} catch (Exception e) {
						log.error("commit error", e);
					}
					if (synList != null && synList.size() > 0) {
						for (Synchronization s : synList) {
							s.afterCompletion(curTran.getStatus());
						}
					}
				} finally {
					if (tranStack.isEmpty()) {
						removeCurTransManager();
					}
				}
			}
		}
	}

	private void removeCurTransManager() {
		UAPTransactionManagerProxy.getTransactionManagerContainer().remove();
	}

	@Override
	public int getStatus() throws SystemException {
		return getTransaction().getStatus();
	}

	@Override
	public Thread getThread() {
		return curThread;
	}

	@Override
	public UAPTransactionContext getTranContext() {
		return tranStack.isEmpty() ? null : tranStack.peek();
	}

	@Override
	public Transaction getTransaction() {
		if (getTranContext() == null) {
			return null;
		}
		return getTranContext().getTransaction();
	}

	@Override
	public boolean isUserTransaction() {
		// return m_bUserTransaction;
		return false;
	}

	@Override
	/** 只会滚当前的Transaction */
	public void rollbackBMT() throws IllegalStateException, SystemException,
			SecurityException {
		UAPTransactionContext curTranContext = tranStack.pop();
		Transaction curTransaction = curTranContext.getTransaction();
		if (curTranContext.getTransaction() != null) {
			if (!curTranContext.isJoined() && !curTranContext.isNullTrans()) {
				try {
					if (curTransaction != null)
						curTransaction.rollback();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				} finally {
					if (tranStack.isEmpty()) {
						removeCurTransManager();
					}
				}
			}
		}
	}

	@Override
	public void rollbackAll() throws IllegalStateException, SystemException,
			SecurityException {

		UAPTransactionContext curContext;
		while (!tranStack.isEmpty()) {
			curContext = tranStack.pop();
			if (curContext.getTransaction() != null && !curContext.isJoined()
					&& !curContext.isNullTrans()) {
				try {
					curContext.getTransaction().rollback();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		removeCurTransManager();
	}

	@Override
	public void setCurTransRollBack() throws IllegalStateException,
			SystemException {
		UAPTransactionContext context = getTranContext();
		Transaction trans = context.getTransaction();
		if (trans != null) {
			trans.setRollbackOnly();
		}
	}

	@Override
	public void setThread(Thread thread) {
		curThread = thread;
	}

	/**
	 * -----------------------------javax.transaction.TransactionManager--------
	 * -------------------------------------
	 */

	@Override
	public void setRollbackOnly() throws IllegalStateException, SystemException {
		setCurTransRollBack();
	}

	@Override
	public void setTranProp(int prop) {
		m_tranProp = prop;
	}

	@Override
	public void begin() throws NotSupportedException, SystemException {
		begin(m_tranProp);
	}

	@Override
	public void resume(Transaction arg0) throws IllegalStateException,
			InvalidTransactionException, SystemException {
		if (arg0 instanceof UAPTransaction) {
			((UAPTransaction) arg0).resumeAllConnection();
		} else {
			try {
				arg0.enlistResource(null);
			} catch (RollbackException e) {
				DBLogger.error("", e);
			}
		}
	}

	@Override
	public void rollback() throws IllegalStateException, SecurityException,
			SystemException {
		rollbackAll();
	}

	@Override
	public void setTransactionTimeout(int arg0) throws SystemException {
		m_nTranTimeOut = arg0;

	}

	public long getTransactionTimeout() {
		return m_nTranTimeOut;
	}

	@Override
	public Transaction suspend() throws SystemException {
		UAPTransaction uapTran = null;
		if (!tranStack.isEmpty()) {
			UAPTransactionContext transContext = tranStack.peek();
			uapTran = (UAPTransaction) transContext.getTransaction();
			uapTran.suspendAllConnection();
		}
		return uapTran;
	}

}
