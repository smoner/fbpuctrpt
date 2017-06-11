package nc.bs.framework.aop.rt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.aop.Behavior;
import nc.bs.framework.aop.ProceedingJoinpoint;
import nc.bs.logging.Logger;

/**
 * 
 * @author ºÎ¹ÚÓî
 * 
 */
public class MethodProceedingJoinpoint extends MethodJoinpoint implements
		ProceedingJoinpoint {

	private Iterator<AdviceHolderChain> chainItr;

	private Invoker invoker;

	private boolean dealTx;

	public MethodProceedingJoinpoint(final Object thisObject, Object aspected,
			final Method method, final Object[] args, Invoker invoker,
			Iterator<AdviceHolderChain> chainItr) {
		super(thisObject, aspected, method, args);
		if (invoker == null) {
			invoker = new Invoker() {
				public Object invoke() throws Throwable {
					return method.invoke(thisObject, args);
				}

			};
			this.invoker = invoker;
		} else {
			this.invoker = invoker;
		}
		this.chainItr = chainItr;
		this.dealTx = nc.bs.framework.core.TxMark.newTx.get();
		Logger.error("----------dealTx:" + dealTx);
		nc.bs.framework.core.TxMark.newTx.set(false);
	}

	public MethodProceedingJoinpoint(final Object thisObject, Object aspected,
			final Method method, final Object[] args, Invoker invoker,
			Iterator<AdviceHolderChain> chainItr, boolean dealTx) {
		super(thisObject, aspected, method, args);
		if (invoker == null) {
			invoker = new Invoker() {
				public Object invoke() throws Throwable {
					return method.invoke(thisObject, args);
				}

			};
			this.invoker = invoker;
		} else {
			this.invoker = invoker;
		}
		this.chainItr = chainItr;
		this.dealTx = dealTx;
//		Logger.error("----------dealTx:" + dealTx);
	}

	public MethodProceedingJoinpoint(final Object thisObject, Object aspected,
			final Method method, final Object[] args,
			Iterator<AdviceHolderChain> chainItr) {
		super(thisObject, aspected, method, args);
		invoker = new Invoker() {
			@Override
			public Object invoke() throws Throwable {
				return method.invoke(thisObject, args);
			}

		};
		this.chainItr = chainItr;
		this.dealTx = nc.bs.framework.core.TxMark.newTx.get();
//		Logger.error("----------dealTx:" + dealTx);
		nc.bs.framework.core.TxMark.newTx.set(false);
	}

	@SuppressWarnings("unchecked")
	public <T> T proceed() throws Throwable {
		if (chainItr != null && chainItr.hasNext()) {
			AdviceHolderChain chain = chainItr.next();

			List<AdviceHolder> befores = chain.getBefores();

			List<AdviceHolder> afters = chain.getAfters();

			if (befores.size() > 0) {
				MethodJoinpoint jp = new MethodJoinpoint(thisObject, aspected,
						method, args);
				try {
					for (AdviceHolder b : befores) {
						if (b.getBehaviorMode() == Behavior.Mode.TRANSACTION
								&& !dealTx)
							continue;
						b.invoke(jp);
					}
				} catch (Throwable thr1) {
					jp = new MethodJoinpoint(thisObject, aspected, method,
							args, thr1);
					for (AdviceHolder a : chain.getAfters()) {
						try {
							if (a.getBehaviorMode() == Behavior.Mode.TRANSACTION
									&& !dealTx)
								continue;
							if (a.getKind() == AdviceHolder.AFTER_THROWING) {
								a.invoke(jp);
							} else if (a.getKind() == AdviceHolder.AFTER) {
								a.invoke(jp);
							}
						} catch (Throwable thr2) {
							jp.throwable = thr2;
						}
					}

					throw jp.throwable;
				}
			}

			Object ret = null;
			int afterIndex = 0;
			try {
				if (chain.getAroundAdviceHolder() != null) {
					ret = chain.getAroundAdviceHolder().invoke(
							new MethodProceedingJoinpoint(getThis(), aspected,
									getStaticPart(), getArgs(), invoker,
									chainItr, this.dealTx));
					chainItr = null;
				} else {
					ret = proceed();
				}
				if (afterIndex < afters.size()) {
					MethodJoinpoint jp = new MethodJoinpoint(thisObject,
							aspected, ret, method, args);
					while (afterIndex < afters.size()) {
						try {
							AdviceHolder a = chain.getAfters().get(afterIndex);
							if (a.getBehaviorMode() == Behavior.Mode.TRANSACTION
									&& !dealTx)
								continue;
							if (a.getKind() == AdviceHolder.AFTER) {
								a.invoke(jp);
							} else if (a.getKind() == AdviceHolder.AFTER_RETURN) {
								a.invoke(jp);
							}
						} finally {
							afterIndex++;
						}
					}
				}

				return (T) ret;

			} catch (Throwable thr3) {
				MethodJoinpoint jp = new MethodJoinpoint(thisObject, aspected,
						method, args, thr3);
				while (afterIndex < afters.size()) {
					try {
						AdviceHolder a = chain.getAfters().get(afterIndex);
						if (a.getBehaviorMode() == Behavior.Mode.TRANSACTION
								&& !dealTx)
							continue;
						if (a.getKind() == AdviceHolder.AFTER) {
							a.invoke(jp);
						} else if (a.getKind() == AdviceHolder.AFTER_THROWING) {
							a.invoke(jp);
						}
					} catch (Throwable thr4) {
						jp.throwable = thr4;
					} finally {
						afterIndex++;
					}
				}
				throw jp.throwable;
			}

		} else {
			try {
				return (T) invoker.invoke();
			} catch (InvocationTargetException te) {
				throw te.getTargetException();
			}
		}

	}

	// private TransactionProcessComponent getTxComponet() {
	// if (factory == null)
	// factory = (RemoteProcessComponetFactory) NCLocator.getInstance()
	// .lookup("RemoteProcessComponetFactory");
	// if (factory == null)
	// return null;
	// TransactionProcessComponent txComp = (TransactionProcessComponent)
	// factory
	// .getThreadScopePostProcess("transaction");
	// if (txComp == null)
	// txComp = new TransactionProcessComponent();
	// return txComp;
	// }
	//
	// private void addTxComponet(String name,
	// TransactionProcessComponent txComponent) {
	// if (factory == null)
	// factory = (RemoteProcessComponetFactory) NCLocator.getInstance()
	// .lookup("RemoteProcessComponetFactory");
	// if (factory != null)
	// factory.addThreadScopePostProcess(name + "_transaction",
	// txComponent);
	// }

	public static interface Invoker {
		public Object invoke() throws Throwable;
	}
}
