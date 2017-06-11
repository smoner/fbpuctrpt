package nc.bs.framework.ejb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.ejb.EJBException;

import nc.bs.framework.core.TxMark;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.framework.util.Messages;
import nc.bs.logging.Logger;

import org.granite.lang.util.WeakValueMap;

/**
 * 
 * @author ºÎ¹ÚÓî
 * 
 *         Date: 2005-01-21 Time: 10:07:30
 */
abstract public class AbstractEJBServiceHandler implements InvocationHandler {
    protected static Map<String, Method> namedEJBMethods = new WeakValueMap<String, Method>(512);

    protected String name;

    protected AbstractEJBServiceHandler(String name) {
        this.name = name;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ejb = null;
        try {
            ejb = getBServiceEJB();
            return invoke(proxy, ejb, name, method, args);
        } finally {
            removeBServiceEJB(ejb);
        }
    }

    abstract protected Object getBServiceEJB() throws Throwable;

    abstract protected void removeBServiceEJB(Object bsEJB) throws Throwable;

    protected static Object invoke(Object proxy, Object target, String name, Method method, Object[] args)
            throws Throwable {
        Method m = method;
        Class<?>[] expTypes = m.getExceptionTypes();
        boolean hasError = false;
        Throwable thr = null;
        long nowTime = System.currentTimeMillis();
        boolean dg = Logger.isDebugEnabled();
        try {
            if (dg)
                Logger.debug(String.format(Messages.beginMethodFormat, methodToString(m), target, arrayToString(args)));
            Object retObject = m.invoke(target, args);
            return retObject;
        } catch (InvocationTargetException e) {
            hasError = true;
            if (e.getTargetException() != null) {
                thr = e.getTargetException();
            } else if (e.getCause() != null) {
                thr = e.getCause();
            } else {
                thr = e;
            }
        } catch (Throwable e) {
            hasError = true;
            thr = e;
        } finally {
            if (dg) {
                long takeTime = System.currentTimeMillis() - nowTime;
                if (hasError) {
                    Logger.debug(String.format(Messages.errorEndMethodFormat, methodToString(m), target,// arrayToString(args),
                            takeTime));
                } else
                    Logger.debug(String.format(Messages.endMethodFormat, methodToString(m), target,// arrayToString(args),
                            takeTime));
            }
        }

        String thrName = thr.getClass().getName();
        if ((thr instanceof EJBException || thrName.startsWith("com.ibm.websphere.") || thrName
                .startsWith("com.ibm.ejs.")) && TxMark.getCause() != null) {
            Logger.error(methodToString(m) + " transaction is already rollbacked", thr);
            thr = TxMark.getCause();
        }

        while (thr instanceof EJBException) {
            Logger.error("transaction already rollacked: " + methodToString(m));
            EJBException e = (EJBException) thr;
            if (e.getCausedByException() != null) {
                thr = e.getCausedByException();
            } else if (e.getCause() != null) {
                thr = e.getCause();
            } else {
                Logger.error("---javax.ejb.EJBException cause is null, exception maybe handled error---");
                break;
            }
        }

        for (int i = 0; i < expTypes.length; i++) {
            if (expTypes[i].isAssignableFrom(thr.getClass()))
                throw thr;
        }
        handleUnexpectedThrowable(target, m, args, thr);
        return null;
    }

    private static String arrayToString(Object[] args) {
        if (args != null && args.length > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append('[');
            try {
                sb.append(args[0]);
            } catch (Exception e) {
                sb.append("UNKNOWN:" + args[0].getClass());
            }
            for (int i = 1; i < args.length; i++) {
                sb.append(',');
                try {
                    sb.append(args[i]);
                } catch (Exception e) {
                    sb.append("UNKNOWN:" + args[i].getClass());
                }
                if (sb.length() > Logger.ARG_MAXSIZE) {
                    sb.setLength(Logger.ARG_MAXSIZE);
                    sb.append("...");
                    break;
                }
            }
            sb.append(']');
            return sb.toString();
        } else
            return "";

    }

    private static String methodToString(Method m) {
        StringBuffer sb = new StringBuffer();
        sb.append(m.getDeclaringClass().getName()).append('.');
        sb.append(m.getName());
        sb.append('(');
        Class<?>[] types = m.getParameterTypes();
        if (types != null && types.length > 0) {
            sb.append(types[0].getName());
            for (int i = 1; i < types.length; i++) {
                sb.append(',');
                sb.append(types[i].getName());

            }
        }
        sb.append(')');
        return sb.toString();

    }

    private static boolean verifyThrowable(Throwable thr) {
        if (thr == null)
            return true;
        String pkgName = thr.getClass().getPackage().getName();

        if (pkgName.startsWith("com.ibm.") || pkgName.startsWith("weblogic.")
                || (pkgName.startsWith("javax.") && !pkgName.startsWith("javax.xml")))
            return false;

        return verifyThrowable(thr.getCause());
    }

    private static void handleUnexpectedThrowable(Object target, Method m, Object[] args, Throwable thr)
            throws Throwable {

        if (thr instanceof Error) {
            if (!verifyThrowable(thr)) {
                long freeMemory = Runtime.getRuntime().freeMemory();
                long totalMemory = Runtime.getRuntime().totalMemory();
                Logger.error(String.format(Messages.invokeErrorMsgFormat, methodToString(m), target,
                        arrayToString(args), Long.valueOf(freeMemory), Long.valueOf(totalMemory)), thr);
                throw new FrameworkEJBException(thr.getMessage());
            } else {
                throw (Error) thr;
            }
        } else if (thr instanceof RuntimeException) {
            if (!verifyThrowable(thr)) {
                Logger.error(
                        String.format(Messages.invokeExpMsgFormat, methodToString(m), target, arrayToString(args)), thr);
                throw new FrameworkEJBException(thr.getMessage());
            } else {
                throw (RuntimeException) thr;
            }
        } else {
            Logger.error(String.format(Messages.invokeExpMsgFormat, methodToString(m), target, arrayToString(args)),
                    thr);
            throw new FrameworkEJBException(thr.getMessage(), thr);
        }
    }

}