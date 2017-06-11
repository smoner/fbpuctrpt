package nc.bs.framework.core;

import static nc.bs.framework.util.Messages.errInstFromFc;
import static nc.bs.framework.util.Messages.invalidHierarchy;
import static nc.bs.framework.util.Messages.notInterface;
import static nc.bs.framework.util.Messages.resoleRefErr;
import static nc.bs.framework.util.Messages.resolveRefErrCause;

import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import nc.bs.framework.aop.AspectManager;
import nc.bs.framework.component.ActiveComponent;
import nc.bs.framework.component.ContainerAwareComponent;
import nc.bs.framework.component.ContextAwareComponent;
import nc.bs.framework.component.FactoryComponent;
import nc.bs.framework.component.ServiceComponent;
import nc.bs.framework.core.common.CreateInfoBag;
import nc.bs.framework.core.common.FactoryParameter;
import nc.bs.framework.core.common.PropertyHolder;
import nc.bs.framework.core.common.RudeRef;
import nc.bs.framework.exception.ComponentException;
import nc.bs.framework.exception.InstException;
import nc.bs.framework.exception.InvalidException;
import nc.bs.framework.instantiator.AbstractInstantiator;
import nc.bs.framework.instantiator.AspectedCtorInstantiator;
import nc.bs.framework.instantiator.ConstantInstantiator;
import nc.bs.framework.instantiator.CtorInstantiator;
import nc.bs.framework.naming.Context;
import nc.bs.framework.util.Messages;
import nc.bs.framework.util.ResolveUtil;
import nc.bs.logging.Log;
import nc.vo.jcom.lang.StringUtil;

import org.granite.convert.ConverterManager;
import org.granite.lang.util.Clazzs;
import org.granite.lang.util.Objects;
import org.granite.lang.util.ThreadLocalBool;

/**
 * 
 * Created by UFSoft. User: ºÎ¹ÚÓî
 * 
 * Date: 2007-12-21 Time: ÏÂÎç02:26:14
 * 
 * @author hgy
 */
public abstract class AbstractMeta implements Meta, CreateInfoBag {
	public static final Object NON_INIT = new Object();

	private Log log = Log.getInstance(AbstractMeta.class);

	private String name;

	private String description;

	private int priority = DEF_PRI;

	private boolean singleton = true;

	private GenericContainer<?> container;

	private ComponentState state;

	private List<String> alias;

	private ArrayList<Class<?>> itfs;

	private ArrayList<PropertyHolder> properties;

	private Instantiator rawInstantiator;

	private boolean active;

	private boolean supportAlias;

	protected AtomicReference<Instantiator> instRef = new AtomicReference<Instantiator>();

	private List<Object> causes;

	private Objects objects;

	private ExtensionProcessor[] eps;

	private EnhancerManager em;

	private String retry;

	protected Map<Class<?>, Object> extensions;

	private boolean needContainerEnhance = true;

	private Class<?> implementation;

	private AspectMode aspectMode;

	private int rank;

	private boolean hasCtorDep;

	public AspectMode getAspectMode() {
		AspectMode am = aspectMode;
		Instantiator raw = getRawInstantiator();
		if (am == AspectMode.FULL) {

			if (!(raw instanceof CtorInstantiator)) {
				log.debug(name
						+ ": aspect mode must be delegate (constant or factory)");
				am = AspectMode.DELEGATE;
			} else {
				if ((getImplementation().getModifiers() & Modifier.FINAL) != 0) {
					am = AspectMode.DELEGATE;
				}
			}
		} else {
			if (this.getInterfaces().length == 0) {
				am = AspectMode.FULL;
			}
		}

		return am;
	}

	public void setAspectMode(AspectMode aspectMode) {
		this.aspectMode = aspectMode;
	}

	public Class<?> getImplementation() {
		return implementation;
	}

	public void setImplementation(Class<?> implementation) {
		this.implementation = implementation;
	}

	public AbstractMeta(GenericContainer<?> container) {
		setContainer(container);
		state = ComponentState.UNKNOWN;
		eps = new ExtensionProcessor[0];
		alias = new ArrayList<String>(4);
		itfs = new ArrayList<Class<?>>(4);
		properties = new ArrayList<PropertyHolder>();
		objects = new Objects(container.getExtension(ConverterManager.class));
		em = new SimpleEnhancerManager();
		causes = new LinkedList<Object>();
		this.extensions = new ConcurrentHashMap<Class<?>, Object>();
	}

	public boolean isSupportAlias() {
		return supportAlias;
	}

	public void setSupportAlias(boolean supportAlias) {
		this.supportAlias = supportAlias;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean flag) {
		this.active = flag;
	}

	public GenericContainer<?> getContainer() {
		return container;
	}

	public void setContainer(GenericContainer<?> container) {
		this.container = container;
	}

	public void addExtensionProcessor(ExtensionProcessor ep) {
		if (ep == null)
			return;
		ExtensionProcessor[] eps1 = new ExtensionProcessor[eps.length + 1];
		System.arraycopy(eps, 0, eps1, 0, eps.length);
		eps1[eps.length] = ep;
		eps = eps1;
	}

	public ExtensionProcessor[] getExtensionProcessors() {
		return eps;
	}

	public String getName() {
		if (name == null) {
			if (alias.size() > 0) {
				name = alias.get(0);
			}
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		if (priority == 0) {
			// only for compatibility
			this.priority = Priority.DEF_PRI;
		} else {
			this.priority = priority;
		}
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public ComponentState getState() {
		return state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getAlias() {
		return alias.toArray(new String[alias.size()]);
	}

	public void addAlia(String name) {
		if (!alias.contains(name)) {
			alias.add(name);
		}
	}

	public Class<?>[] getInterfaces() {
		return itfs.toArray(new Class[0]);
	}

	public void addInterface(Class<?> itf) {
		itfs.add(itf);
	}

	@Override
	public List<PropertyHolder> getPropertyHolders() {
		return properties;
	}

	public void addPropertyHolder(PropertyHolder property) {
		properties.add(property);
	}

	public void addAlias(String name) {
		alias.add(name);
	}

	private boolean isExposeInterface() {
		return getInterfaces() != null && getInterfaces().length > 0;
	}

	public void validate() {

		Class<?> implClass = getImplementation();

		if (getRawInstantiator() instanceof AbstractInstantiator) {

			AbstractInstantiator ai = (AbstractInstantiator) getRawInstantiator();

			FactoryParameter[] fps = ai.getParameters();

			if (fps.length > 0) {
				for (FactoryParameter fp : fps) {
					if (fp.getValue() instanceof RudeRef) {
						hasCtorDep = true;
					}
				}
			}
		}

		if (getRawInstantiator() instanceof CtorInstantiator) {
			implClass = ((CtorInstantiator) getRawInstantiator())
					.getImplementationClass();

		} else if (getRawInstantiator() instanceof ConstantInstantiator) {
			implClass = ((ConstantInstantiator) getRawInstantiator())
					.getConstant().getClass();
		}
		if (getName() == null) {
			if (isSupportAlias()) {
				for (Class<?> c : itfs) {
					addAlia(c.getName());
				}
			}

			if (getName() == null) {
				if (itfs.size() > 0) {
					setName(itfs.get(0).getName());
				} else if (implClass != null) {
					setName(implClass.getName());
				}
			}
		}

		if (!StringUtil.hasText(getName())) {
			throw new InvalidException("no name");
		}

		if (isExposeInterface()) {
			for (int i = 0; i < itfs.size(); i++) {
				if (!itfs.get(i).isInterface())
					throw new InvalidException(getName(), String.format(
							notInterface, getInterfaces()[i]));
				if (implClass != null
						&& !itfs.get(i).isAssignableFrom(implClass)) {
					throw new InvalidException(getName(), String.format(
							invalidHierarchy, implClass.getName(), itfs.get(i)
									.getName()));
				}
			}
		}

		if (!isActive()) {
			if (implClass != null) {
				if (ActiveComponent.class.isAssignableFrom(implClass)) {
					setActive(true);
					setSingleton(true);
				}
			}
		} else {
			setSingleton(true);
		}

	}

	protected boolean needAspectCtor() {
		AspectManager am = getContainer().getExtension(AspectManager.class);
		return am.hasAspectFor(this) && am.hasNonDecoratorAspectFor(this)
				&& getAspectMode() == AspectMode.FULL;
	}

	protected boolean noSuitableForAspect(Class<?> implClass) {
		if (Enhancer.class.isAssignableFrom(implClass)) {
			return true;
		}

		if ((implClass.getModifiers() & Modifier.FINAL) != 0) {
			return true;
		}

		return false;
	}

	public Instantiator getInstantiator() throws ComponentException {
		if (getState() == ComponentState.ERROR) {
			throw new InstException(getName(), getName(), getStateString());
		}
		Instantiator itor = instRef.get();
		if (itor == null) {
			Instantiator raw = rawInstantiator;
			if (needAspectCtor() && rawInstantiator instanceof CtorInstantiator) {
				CtorInstantiator ctor = (CtorInstantiator) rawInstantiator;
				Class<?> implClass = ctor.getImplementationClass();

				Class<?>[] itfs = getInterfaces();

				if (!noSuitableForAspect(implClass)) {
					raw = new AspectedCtorInstantiator(implClass, itfs,
							ctor.getParameters());
				}

			}
			if (singleton) {
				instRef.compareAndSet(null, new SingletonInstantiator(raw));
			} else {
				instRef.compareAndSet(null, new NonSingletonInstantiator(raw));
			}

			return instRef.get();

		}
		return itor;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object processFactoryComponent(String name, Object obj)
			throws ComponentException {
		Object ret = obj;
		if (obj instanceof FactoryComponent) {
			try {
				ret = ((FactoryComponent) obj).getComponent();
			} catch (Throwable thr) {
				errorInstantiate("create  component error as a factory component");
				throw new InstException(name, String.format(errInstFromFc,
						obj.getClass()), thr);
			}
		}

		if (ret instanceof ContextAwareComponent) {
			((ContextAwareComponent) ret)
					.setComponentContext(new ComponentContext(name, this,
							getContainer()));
		}

		if (ret instanceof ContainerAwareComponent) {
			((ContainerAwareComponent) ret).setContainer(getContainer());
		}
		return ret;
	}

	private void setProperty(String componentName, Object target,
			String propertyName, Object value) throws ComponentException {

		try {
			getObjects().setProperty(target, propertyName, value);
		} catch (Throwable thr) {
			errorInstantiate(String.format(Messages.populateErrCause,
					propertyName, value, thr.getMessage()));
			InstException exp = new InstException(componentName,
					Clazzs.getClassDetail(target.getClass()), thr);
			throw exp;
		}

	}

	public Objects getObjects() {
		return objects;
	}

	private void errorInstantiate(String cause) {
		if (isSingleton()) {
			state = ComponentState.ERROR;
			causes.add(cause);
		}

	}

	protected Object preEnhance(EnhancerManager em, Object temp) {
		Enhancer[] enhancers = em.getPreEnhancers();
		for (int i = 0; i < enhancers.length; i++) {
			if (enhancers[i].accept(AbstractMeta.this, temp)) {
				temp = enhancers[i].enhance(AbstractMeta.this, temp);
			}
		}
		return temp;
	}

	protected Object postEnhance(EnhancerManager em, Object temp) {
		Enhancer[] enhancers = em.getPostEnhancers();
		for (int i = 0; i < enhancers.length; i++) {
			if (enhancers[i].accept(AbstractMeta.this, temp)) {
				temp = enhancers[i].enhance(AbstractMeta.this, temp);
			}
		}
		return temp;
	}

	protected Object preEnhanceAtContainer(Object obj) {
		return preEnhance(container.getEnhancerManager(), obj);
	}

	protected Object postEnhanceAtContainer(Object obj) {
		return postEnhance(container.getEnhancerManager(), obj);
	}

	protected abstract class WrappedInstantiator implements Instantiator {
		private Instantiator raw;

		WrappedInstantiator(Instantiator raw) {
			this.raw = raw;
		}

		protected void notifyResolvedEvent(String property, Object value) {

		}

		protected Object rawInstantiate(Context ctx, String name, Object[] args) {
			Object rawObject;

			Object preEnhanced;

			Object postEnhanced;

			Object rawObject1;

			rawObject = raw.instantiate(ctx, name, args);
			if (rawObject == null) {
				log.error("expected???? the component raw object is null: "
						+ name);
			}
			rawObject1 = processFactoryComponent(name, rawObject);

			Object temp = rawObject1;

			if (needContainerEnhance()) {
				temp = preEnhanceAtContainer(temp);
			}

			temp = preEnhanceAtMeta(temp);
			populateObject(container, name, temp);
			preEnhanced = temp;

			temp = postEnhanceAtMeta(temp);

			if (needContainerEnhance()) {
				temp = postEnhanceAtContainer(temp);
			}

			postEnhanced = temp;
			doMisc(rawObject, rawObject1, preEnhanced, postEnhanced);

			return postEnhanced;
		}

		protected void doMisc(Object rawObject, Object rawInstancedObj,
				Object preEnhanced, Object postEnhanced) {
			if (((rawObject instanceof ServiceComponent))
					&& (rawInstancedObj != rawObject))
				startServiceComponent((ServiceComponent) rawObject);
		}

		private void startServiceComponent(ServiceComponent sc) {
			try {
				AbstractMeta.this.log
						.debug(String
								.format("service component: %s at container: %s begin start...",
										new Object[] {
												AbstractMeta.this.getName(),
												AbstractMeta.this.container
														.getName() }));
				if (!sc.isStarted())
					sc.start();
				AbstractMeta.this.log.debug(String.format(
						"service component: %s at container: %s end start",
						new Object[] { AbstractMeta.this.getName(),
								AbstractMeta.this.getContainer().getName() }));
			} catch (Throwable e) {
				AbstractMeta.this.log.error(String.format(
						"service component: %s at container: %s start error",
						new Object[] { AbstractMeta.this.getName(),
								AbstractMeta.this.getContainer().getName() }),
						e);
			}
		}

		protected Object preEnhanceAtMeta(Object obj) {
			return preEnhance(getEnhancerManager(), obj);
		}

		protected Object postEnhanceAtMeta(Object obj) {
			return postEnhance(getEnhancerManager(), obj);
		}

		protected void populateObject(GenericContainer<?> container,
				String name, Object obj) throws InstException {
			if (properties != null) {
				for (int i = 0; i < properties.size(); i++) {
					String propertyName = properties.get(i).getName();
					Object propValue = null;
					try {
						propValue = ResolveUtil.resolve(container.getContext(),
								properties.get(i).getValue());
					} catch (Throwable thr) {
						errorInstantiate(String.format(resolveRefErrCause,
								propertyName, properties.get(i).getValue(),
								thr.getMessage()));
						throw new InstException(container.getName(), name,
								String.format(resoleRefErr, propertyName,
										properties.get(i).getValue()), thr);
					}
					if (propValue instanceof Resolvable) {
						addResolvare((Resolvable) propValue, obj, propertyName);
						// continue;
					}

					setProperty(name, obj, propertyName, propValue);
				}

			}

		}

		protected void addResolvare(Resolvable r, Object obj,
				String propertyName) {
			r.addResolvare(new PropertyCircularResolvare(obj, propertyName));
		}

		protected class PropertyCircularResolvare implements Resolvare {
			private String propertyName;

			private Object target;

			public PropertyCircularResolvare(Object target, String propertyName) {
				this.propertyName = propertyName;
				this.target = target;
			}

			public void resolved(Object obj) throws ComponentException {

				setProperty(getName(), target, propertyName, obj);

				notifyResolvedEvent(propertyName, obj);
			}

		}

	}

	protected class SingletonInstantiator extends WrappedInstantiator {

		protected volatile Object singletonObj = NON_INIT;

		private AtomicReference<ImmatureObject> immatureObject = new AtomicReference<ImmatureObject>();

		private volatile boolean insting;

		private WeakReference<Thread> ctorThread = null;

		// Object rawObject;
		// Object preEnhanced;
		// Object postEnhanced;
		private ReentrantLock lock1 = new ReentrantLock();

		public SingletonInstantiator(Instantiator raw) {
			super(raw);
		}

		private int unresolvedCount;

		protected synchronized boolean ready() {
			return unresolvedCount == 0;
		}

		protected synchronized int getUnResolvedCount() {
			return unresolvedCount;
		}

		protected synchronized void addResolvare(Resolvable r, Object obj,
				String propertyName) {
			unresolvedCount++;
			r.addResolvare(new PropertyCircularResolvare(obj, propertyName));

		}

		protected synchronized void notifyResolvedEvent(String property,
				Object value) {
			unresolvedCount--;
			if (ready()) {
				insting = false;
				notifyAll();
			}
		}

		public Object instantiate(Context ctx, String name, Object[] args) {
			Object ret = null;

			long step = 1000;

			if (hasCtorDep) {
				step = 1000 * 6;
			}

			if (singletonObj == NON_INIT) {
				boolean locked = lock1.tryLock();
				if (!locked) {
					if (singletonObj == NON_INIT) {
						try {
							int i = 0;
							while (i < 10 && !locked) {
								locked = lock1.tryLock(step,
										TimeUnit.MICROSECONDS);
								i++;
							}
						} catch (Exception exp) {

						} finally {
							if (locked) {
								lock1.unlock();
								if (insting) {
									log.warn("wait for " + name
											+ " instantiated");
									synchronized (this) {
										try {
											wait(step);
										} catch (Exception e) {
										}
									}
								}
							}
						}

						if (singletonObj == NON_INIT) {
							StringBuffer sb = null;
							if (ctorThread != null) {
								Thread t = ctorThread.get();
								if (t != null) {
									sb = new StringBuffer();
									StackTraceElement[] sts = t.getStackTrace();

									for (StackTraceElement st : sts) {
										sb.append(st.toString());
									}
								}
							}
							log.warn("long time to construct, unmatured: "
									+ name + " " + locked);
							if (sb != null)
								log.warn("contruct thread dump: "
										+ sb.toString());
							ret = singletonObj == NON_INIT ? getImmatureObject()
									: singletonObj;
						} else {
							ret = singletonObj;
						}

					} else {
						ret = singletonObj;
					}
				} else {
					if (insting) {
						try {
							log.warn("circular dependency, now unmature: "
									+ name);
							ret = singletonObj == NON_INIT ? getImmatureObject()
									: singletonObj;
						} finally {
							lock1.unlock();
						}
					} else {
						if (singletonObj == NON_INIT) {
							ctorThread = new WeakReference<Thread>(
									Thread.currentThread());
							try {
								insting = true;

								state = ComponentState.CONSTRUCTING;
								ret = rawInstantiate(ctx, name, args);
								singletonObj = ret;
								insting = !ready();

								synchronized (immatureObject) {
									if (immatureObject.get() != null) {
										log.warn("circular dependency, make unmature to mature: "
												+ name);

										immatureObject.get().setMatureObject(
												singletonObj);
										immatureObject.set(null);
									}
								}
								state = ComponentState.READY;
							} catch (RuntimeException re) {
								insting = false;
								throw re;
							} catch (Error err) {
								insting = false;
								throw err;
							} catch (Throwable t) {
								insting = false;
								throw new InstException("instantiate error", t);
							} finally {
								ctorThread = null;
								lock1.unlock();
							}
						} else {
							ret = singletonObj;
							synchronized (this) {
								try {
									notifyAll();
								} catch (Exception ee) {
								}
							}
							lock1.unlock();
						}
					}
				}
			} else {
				ret = singletonObj;
			}
			return ret;
		}

		@Override
		protected void doMisc(Object rawObject, Object rawInstancedObj,
				Object preEnhanced, Object postEnhanced) {
			super.doMisc(rawObject, rawInstancedObj, preEnhanced, postEnhanced);
			// this.rawObject = rawObject;
			// this.preEnhanced = preEnhanced;
			// this.postEnhanced = postEnhanced;
		}

		private Object getImmatureObject() {
			synchronized (immatureObject) {
				if (singletonObj == NON_INIT) {
					if (immatureObject.get() == null) {
						immatureObject.set(ImmatureObjectFactory
								.createUnmature(AbstractMeta.this));
					}
					return immatureObject.get();
				} else {
					return singletonObj;
				}
			}
		}

	}

	protected class NonSingletonInstantiator extends WrappedInstantiator {
		private ThreadLocalBool insting = new ThreadLocalBool();

		private ThreadLocal<ImmatureObject> tlImmature = new ThreadLocal<ImmatureObject>();

		public NonSingletonInstantiator(Instantiator raw) {
			super(raw);
		}

		public Object instantiate(Context ctx, String name, Object[] args)
				throws ComponentException {
			Object ret = null;
			if (insting.getValue()) {
				ret = getImmatureObject();
			} else {
				try {
					insting.setValue(true);
					ret = rawInstantiate(ctx, name, args);
					if (tlImmature.get() != null) {
						tlImmature.get().setMatureObject(ret);
						tlImmature.set(null);
					}
				} finally {
					insting.setValue(false);
				}

			}
			return ret;
		}

		private ImmatureObject getImmatureObject() {
			if (tlImmature.get() == null) {
				tlImmature.set(ImmatureObjectFactory
						.createUnmature(AbstractMeta.this));
			}

			return tlImmature.get();
		}
	}

	public Instantiator getRawInstantiator() throws ComponentException {
		if (rawInstantiator == null)
			throw new ComponentException("no raw instantiator");

		return rawInstantiator;
	}

	public void setRawInstantiator(Instantiator inst) {
		rawInstantiator = inst;
		if (rawInstantiator instanceof ObjectsAware) {
			((ObjectsAware) rawInstantiator).setObjects(getObjects());
		}
	}

	public String getStateString() {
		if (state == ComponentState.ERROR) {
			StringBuffer sb = new StringBuffer();
			sb.append("state: " + getState()).append(" causes: ");
			for (int i = 0; i < causes.size(); i++) {
				sb.append(causes.get(i)).append('\n');
			}
			return sb.toString();
		} else {
			return state.toString();
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof AbstractMeta) {
			return getName().equals(((AbstractMeta) obj).getName())
					&& getRank() == ((AbstractMeta) obj).getRank();
		} else {
			return false;
		}
	}

	public int hashCode() {
		return getName().hashCode();
	}

	public String toString() {
		return getName();
	}

	public EnhancerManager getEnhancerManager() {
		return em;
	}

	public void setEnhancerManager(EnhancerManager factory) {
		if (factory == null) {
			throw new IllegalArgumentException("EnhancerManager can't be null");
		}
		this.em = factory;

	}

	public boolean needContainerEnhance() {
		return needContainerEnhance;
	}

	public void setNeedContainerEnhance(boolean flag) {
		this.needContainerEnhance = flag;
	}

	public String getRetry() {
		return retry;
	}

	public void setRetry(String retry) {
		this.retry = retry;
	}

	public <P> P getExtension(Class<P> extensionType) {
		Object obj = extensions.get(extensionType);
		if (null != obj) {
			return extensionType.cast(obj);
		}
		return null;
	}

	public <P> void setExtension(P extension, Class<P> extensionType) {
		extensions.put(extensionType, extension);
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
