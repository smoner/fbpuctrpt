package nc.bs.mw.naming;

import nc.bs.logging.Log;
import nc.bs.mw.itf.MwTookit;
import nc.middleware.tran.IContainerTransProxy;
import nc.middleware.tran.IerpTransactionManager;
import nc.middleware.tran.NCDataSource;
import uap.mw.trans.UAPUserTransanction;

public class TransactionFactory {
	private static final Log log = Log.getInstance(TransactionFactory.class);
	private static Class<?> transactionManagerProxyClass = null;
	private static Class<?> transactionManagerClass = null;
	private static Class<?> userTransactionClass = null;
	private static Class<?> xADataSourceClass = null;

	public static IContainerTransProxy getTMProxy() {

		synchronized (TransactionFactory.class) {
			if (transactionManagerProxyClass == null) {
				// String TMProxy_classname = MwTookit.getRuntimeEnviroment()
				// .getMiddleProperty().getMiddleParameter().TransactionManagerProxyClass;
				String TMProxy_classname = "uap.mw.trans.UAPTransactionManagerProxy";
				try {
					transactionManagerProxyClass = Class
							.forName(TMProxy_classname);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(
							"load transaction manager proxy class error: "
									+ TMProxy_classname);
				}
			}
		}
		try {
			return (IContainerTransProxy) transactionManagerProxyClass
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(
					"create transaction manager proxy error", e);
		}
	}

	public static IerpTransactionManager getTManager() {

		synchronized (TransactionFactory.class) {
			if (transactionManagerClass == null) {
				String TManager_classname = MwTookit.getRuntimeEnviroment()
						.getMiddleProperty().getMiddleParameter().TransactionManagerClass;
				try {
					transactionManagerClass = Class.forName(TManager_classname);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(
							"load transaction manager  class error: "
									+ TManager_classname);
				}
			}

		}

		try {
			return (IerpTransactionManager) transactionManagerClass
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("create transaction manager error", e);
		}
	}

	public static UAPUserTransanction getUTransaction() {
		synchronized (TransactionFactory.class) {
			if (userTransactionClass == null) {
				// String UT_classname = MwTookit.getRuntimeEnviroment()
				// .getMiddleProperty().getMiddleParameter().UserTransactionClass;

				String UT_classname = "uap.mw.trans.UAPUserTransanction";
				try {
					userTransactionClass = Class.forName(UT_classname);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(
							"load user transaction manager  class error: "
									+ UT_classname);
				}
			}
		}
		try {
			return (UAPUserTransanction) userTransactionClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("create user transaction manager error",
					e);
		}
	}

	public static NCDataSource getXADataSource() {
		synchronized (TransactionFactory.class) {
			if (xADataSourceClass == null) {
				// String xads_classname = MwTookit.getRuntimeEnviroment()
				// .getMiddleProperty().getMiddleParameter().XADataSourceClass;

				String xads_classname = "uap.mw.ds.UAPDataSource";
				try {
					xADataSourceClass = Class.forName(xads_classname);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(
							"load xADate source class error: " + xads_classname);
				}
			}

		}
		try {
			return (NCDataSource) xADataSourceClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("create xADate source class error", e);
		}
	}

}
