package uap.mw.trans;

import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import nc.bs.logging.Log;
import nc.bs.mw.tran.IIerpTransactionProp;
import uap.mw.trans.itf.IUAPTransactionManager;
import uap.mw.trans.itf.IUAPTransactionManagerProxy;

public class UAPTransactionManagerProxy implements IUAPTransactionManagerProxy {

	private final static Log log = Log
			.getInstance(IUAPTransactionManager.class);

	private static ConcurrentHashMap<Thread, IUAPTransactionManager> transactionMap = new ConcurrentHashMap<Thread, IUAPTransactionManager>();

	private static ThreadLocal<IUAPTransactionManager> tm_local = new ThreadLocal<IUAPTransactionManager>() {
		public void set(IUAPTransactionManager value) {
			super.set(value);
			transactionMap.put(Thread.currentThread(), value);
		};

		public void remove() {
			super.remove();
			transactionMap.remove(Thread.currentThread());
		};
	};

	private static UAPTransactionManagerProxy proxy = new UAPTransactionManagerProxy();

	public static ConcurrentHashMap<Thread, IUAPTransactionManager> getTransactionMap() {
		return transactionMap;
	}

	public static ThreadLocal<IUAPTransactionManager> getTransactionManagerContainer() {
		return tm_local;
	}

	public static IUAPTransactionManager getCurTransManager() {
		return tm_local.get();
	}

	public static UAPTransactionManagerProxy getInstance() {
		return proxy;
	}

	@Override
	public void begin(int transType, int isolateLevel)
			throws NotSupportedException, SystemException, NamingException,
			TransactionRequiredException {
		IUAPTransactionManager m_tranManager = tm_local.get();
		if (m_tranManager == null) {
			m_tranManager = new UAPTransactionManager();
			tm_local.set(m_tranManager);
		}

		m_tranManager.begin(transType);

		// m_tranManager.begin(transType, needRollBackToPoint());
	}

	// private boolean needRollBackToPoint() {
	// if (tm_local.get().getTranContext() != null
	// && tm_local.get().getTranContext().isJoined()) {
	// }
	// return false;
	// }

	// @Override
	public IUAPTransactionManager beginUserTransaction()
			throws NotSupportedException, SystemException,
			TransactionRequiredException, NamingException {
		IUAPTransactionManager m_tranManager = tm_local.get();
		if (m_tranManager == null) {
			m_tranManager = new UAPTransactionManager();
			m_tranManager.setThread(Thread.currentThread());
			tm_local.set(m_tranManager);

		}
		m_tranManager.begin(IIerpTransactionProp.TRANSACTION_REQUIRESNEW);
		return m_tranManager;
	}

	@Override
	public void end(Exception ex) {
		/** 所有相关的连接必须在真正提交时关闭 */
		IUAPTransactionManager m_tranManager = tm_local.get();
		try {
			if (ex != null) {
				if (m_tranManager.getTranContext().needRBPoint()) {
					if (!((UAPTransaction) (m_tranManager.getTranContext()
							.getTransaction())).getRollbackOnly()) {
						m_tranManager.rollBackToCurInvokePoint();
						return;
					}
					// m_tranManager.clearCurInvokeSavePoint();
				} else {
					m_tranManager.setCurTransRollBack();
				}
			}
			m_tranManager.commit();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	/**
	 * 当线程死掉后，调用本方法，回滚事务。
	 */
	public void endThreadDeath() {
		IUAPTransactionManager m_tranManager = tm_local.get();
		try {
			m_tranManager.rollbackAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		tm_local.remove();
	}

	public void setRollbackOnly() throws NamingException, RollbackException,
			HeuristicRollbackException, SystemException,
			HeuristicMixedException {
		IUAPTransactionManager tm = tm_local.get();
		if (tm != null) {
			tm.setCurTransRollBack();
		}

	}

	@Override
	public boolean getRollbackOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 当前线程是否在事务中
	 * 
	 * @return
	 */
	public static boolean isTrans() {
		IUAPTransactionManager m_tranManager = tm_local.get();
		if (m_tranManager != null) {
			return m_tranManager.getTransaction() != null;
		}
		return false;
	}
}
