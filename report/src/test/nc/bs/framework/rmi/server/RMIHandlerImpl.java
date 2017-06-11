package nc.bs.framework.rmi.server;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.common.UserExit;
import nc.bs.framework.comn.NetObjectInputStream;
import nc.bs.framework.comn.NetObjectOutputStream;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.comn.Result;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.framework.core.conf.Configuration;
import nc.bs.framework.exception.ComponentException;
import nc.bs.framework.exception.FrameworkSecurityException;
import nc.bs.framework.execute.ThreadFactoryManager;
import nc.bs.framework.naming.Context;
import nc.bs.framework.server.util.ConfigurationUtil;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.vo.pub.BusinessRuntimeException;

import org.granite.io.FastByteArrayOutputStream;

/**
 * 
 * @author He Guan Yu
 * 
 * @date Oct 20, 2010
 * 
 */
public class RMIHandlerImpl implements RMIHandler {

    private static final String CONTENT_TYPE = "application/x-java-serialized-object";

    protected Log log = Log.getInstance(getClass());

    private Context remoteCtx;

    private Map<String, Context> ctxMap = new HashMap<String, Context>(128);

    private RemoteProcessComponetFactory factory = null;

    private Map<String, AtomicInteger> limitConcurrent = new HashMap<String, AtomicInteger>();

    private int MAX_QPS = getConfiguration().getMaxConcurrentTimes();// Integer.valueOf(System.getProperty("MaxConn","99"));
                                                                     // //99;

    private Configuration configuration;

    public RMIHandlerImpl() {
        Properties props = new Properties();
        props.setProperty(NCLocator.LOCATOR_PROVIDER_PROPERTY, "nc.bs.framework.server.RemoteNCLocator");
        remoteCtx = NCLocator.getInstance(props);
        // 每秒钟清除一次
        Executors.newScheduledThreadPool(1, ThreadFactoryManager.newThreadFactory()).scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        Logger.debug("reinit user concurrent call number.");
                        if (limitConcurrent.size() >= 500) {
                            limitConcurrent.clear();
                        }
                        for (AtomicInteger count : limitConcurrent.values()) {
                            count.set(0);
                        }
                    }
                }, 1, 1, TimeUnit.SECONDS);

        try {
            factory = (RemoteProcessComponetFactory) NCLocator.getInstance().lookup("RemoteProcessComponetFactory");
        } catch (Throwable throwable) {
            log.warn("RemoteCallPostProcess is not found");
        }

    }

    public void handle(RMIContext rmiCtx) throws IOException {
        try {
            rmiCtx.setOutputContentType(CONTENT_TYPE);
            beforeDoHandle(rmiCtx);
            doHandle(rmiCtx);
        } finally {
            postRemoteProcess(rmiCtx);
            afterDoHandle(rmiCtx);
            Logger.setUserLevel(null);
            InvocationInfoProxy.getInstance().reset();
            RuntimeEnv.getInstance().setThreadRunningInServer(true);
        }
    }

    private void postRemoteProcess(RMIContext rmiCtx) {
        Result result = rmiCtx.getResult();

        if (result == null || factory == null) {
            return;
        }
        try {
            if (result.appexception != null) {
                factory.postErrorProcess(result.appexception);
            } else {
                factory.postProcess();
            }
            factory.clearThreadScopePostProcess();
        } catch (Throwable thr) {
            log.error("unexpected exception when post remote process", thr);
        }

        NetStreamContext.setToken(null);

    }

    protected void beforeDoHandle(RMIContext rmiCtx) {

    }

    protected void afterDoHandle(RMIContext rmiCtx) {

    }

    protected void beforeWriteClient(RMIContext rmiCtx) {

    }

    protected void afterWriteClient(RMIContext rmiCtx) {

    }

    private void writeNetwork(RMIContext rmiCtx, byte[] resultData, int from, int length) throws IOException {
        beforeWriteNetWork(rmiCtx, length);
        try {
            rmiCtx.setOutputContentLength(length + 4);
            NetObjectOutputStream.writeInt(rmiCtx.getOutputStream(), length);
            rmiCtx.getOutputStream().write(resultData, from, length);
        } finally {
            rmiCtx.getOutputStream().flush();
            afterWriteNetWork(rmiCtx, length);
        }
    }

    private void preRemoteProcess() {
        if (factory != null) {
            factory.preProcess();
        }
    }

    private void doHandle(RMIContext rmiCtx) throws IOException {
        Result result = new Result();
        byte[] data = readFromNetWrok(rmiCtx);

        try {
            readInvocationInfo(rmiCtx, data);
            boolean legal = isLegalConcurrent(rmiCtx);
            if (!legal) {
                throw new FrameworkSecurityException(" had  over limit concurrent");
            }
            preRemoteProcess();
            result.result = invokeBeanMethod(rmiCtx);
        } catch (Throwable ite) {
            Throwable appException = extractException(ite);
            if (verifyThrowable(appException)) {
                result.appexception = appException;
            } else {
                Logger.error("unexpected exception", appException);
                result.appexception = new BusinessRuntimeException(appException.getMessage());
            }
        }
        data = null;
        rmiCtx.setResult(result);

        try {
            beforeWriteClient(rmiCtx);
            writeResult(rmiCtx);
        } finally {
            afterWriteClient(rmiCtx);
        }
    }

    private boolean isLegalConcurrent(RMIContext rmiCtx) {
        if (MAX_QPS < 1) {
            return true;
        }
        try {
            InvocationInfo info = rmiCtx.getInvocationInfo();
            String userCode = info.getUserCode();
            if (userCode == null || UserExit.DEFAULT_USERID_VALUE.equals(userCode)) {
                return true;
            }
            AtomicInteger integer = limitConcurrent.get(userCode);
            if (integer != null) {
                integer.incrementAndGet();
                Logger.error("isLegalConcurrent:time:" + integer.intValue());
                if (integer.get() >= MAX_QPS) {
                    {
                        Logger.error("over limit concurrent,user :" + userCode);

                        // to do record log,waiting for logAPI
                        return false;
                    }
                }
            } else {
                Logger.error("AtomicInteger is null:");
                AtomicInteger count = new AtomicInteger(1);
                limitConcurrent.put(userCode, count);
            }
            return true;
        } catch (Exception e) {// 防止并发出错
            Logger.error("limit concurrent error", e);
            return true;
        }
    }

    private byte[] readFromNetWrok(RMIContext rmiCtx) throws IOException {
        int readLen = -1;
        beforeReadNetWork(rmiCtx);
        try {
            InputStream in = rmiCtx.getInputStream();
            int len = NetObjectInputStream.readInt(in);

            byte[] bytes = new byte[len];

            readLen = in.read(bytes);

            while (readLen < len) {
                int tmpLen = in.read(bytes, readLen, len - readLen);
                if (tmpLen < 0)
                    break;
                readLen += tmpLen;
            }

            if (readLen < len) {
                throw new EOFException("read object error,read " + readLen + " but expect: " + len);
            }

            return bytes;
        } finally {
            afterReadNetWork(rmiCtx, readLen);
        }

    }

    private InvocationInfo readInvocationInfo(RMIContext rmiCtx, byte[] bytes) throws IOException,
            ClassNotFoundException {
        beforeReadInvocationInfo(rmiCtx, bytes);
//        bytes = new byte[0];
        try {
            NetObjectInputStream objIn = new NetObjectInputStream(new ByteArrayInputStream(bytes));
            rmiCtx.setCompressed(objIn.isCompressed());
            rmiCtx.setEncrypted(objIn.isEncrypted());
            rmiCtx.setEncryptType(objIn.getEncryptType());
            rmiCtx.setTransKey(objIn.getTransKey());
            InvocationInfo invInfo = null;
            invInfo = (InvocationInfo) objIn.readObject();
            Logger.setUserLevel(invInfo.getLogLevel());
            Logger.init(invInfo.getServiceName());
            invInfo.fix();
            rmiCtx.setInvocationInfo(invInfo);
            String callId = invInfo.getCallId();
            Logger.putMDC("serial", callId);
            String ds = invInfo.getUserDataSource();
            if (ds == null)
                ds = "design";
            Logger.putMDC("datasource", ds);
            Logger.putMDC("user", invInfo.getUserId());
            objIn.close();
            return invInfo;
        } finally {
            afterReadInvocationInfo(rmiCtx, bytes);
        }
    }

    protected void afterReadInvocationInfo(RMIContext rmiCtx, byte[] bytes) {

    }

    protected void beforeReadInvocationInfo(RMIContext rmiCtx, byte[] bytes) {

    }

    private void writeResult(RMIContext rmiCtx) throws IOException {
        Result result = rmiCtx.getResult();
        FastByteArrayOutputStream bout = null;

        if (rmiCtx.getResult().appexception != null) {
            Logger.error("the exception to client", result.appexception);
        }

        try {
            bout = serilaizeObject(rmiCtx);
        } catch (IOException e) {
            Logger.error("error when serialize object to client", e);
            result.result = null;
            result.appexception = e;
            bout = serilaizeObject(rmiCtx);
        }

        writeNetwork(rmiCtx, bout.array, 0, bout.length);

    }

    private Object invokeBeanMethod(RMIContext rmiCtx) throws Throwable {

        InvocationInfo invInfo = rmiCtx.getInvocationInfo();
        String module = invInfo.getModule();
        String service = invInfo.getServiceName();
        String mn = invInfo.getMethodName();
        Class<?>[] pts = invInfo.getParametertypes();
        Object[] ps = invInfo.getParameters();
        Object o = null;
        beforeInvokeBeanMethod(rmiCtx);
        try {
            if (module == null) {
                o = remoteCtx.lookup(service);
            } else {
                Context moduleCtx = ctxMap.get(module);
                if (moduleCtx == null) {
                    Properties props = new Properties();
                    props.put(NCLocator.TARGET_MODULE, module);
                    props.put(NCLocator.LOCATOR_PROVIDER_PROPERTY, "nc.bs.framework.server.ModuleNCLocator");
                    moduleCtx = NCLocator.getInstance(props);
                    ctxMap.put(module, moduleCtx);
                }
                o = moduleCtx.lookup(service);
            }

            Method bm = o.getClass().getMethod(mn, pts);
            bm.setAccessible(true);
            Object result;
            result = bm.invoke(o, ps);
            return result;
        } catch (ComponentException exp) {
            Logger.error("component lookup error", exp);
            throw exp;
        } finally {
            afterInvokeBeanMethod(rmiCtx);
        }
    }

    protected void beforeInvokeBeanMethod(RMIContext ctx) {

    }

    protected void afterInvokeBeanMethod(RMIContext ctx) {

    }

    protected void beforeReadNetWork(RMIContext rmiCtx) throws IOException {

    }

    protected void afterReadNetWork(RMIContext rmiCtx, int readSize) throws IOException {

    }

    private FastByteArrayOutputStream serilaizeObject(RMIContext rmiCtx) throws IOException {
        beforeSerializeResult(rmiCtx);
        FastByteArrayOutputStream bout = null;
        NetObjectOutputStream objOut = null;
        try {
            bout = new FastByteArrayOutputStream();
            objOut = new NetObjectOutputStream(bout, rmiCtx.isCompressed(), rmiCtx.isEncrypted(),
                    rmiCtx.getEncryptType(), rmiCtx.getTransKey());
            objOut.writeObject(rmiCtx.getResult());
            return bout;
        } finally {
            afterSerializeResult(rmiCtx, bout == null ? -1 : bout.length);
            if (objOut != null)
                objOut.close();
        }
    }

    protected void beforeSerializeResult(RMIContext rmiCtx) {

    }

    protected void afterSerializeResult(RMIContext rmiCtx, int size) {

    }

    protected void beforeWriteNetWork(RMIContext rmiCtx, int length) throws IOException {

    }

    protected void afterWriteNetWork(RMIContext rmiCtx, int length) throws IOException {

    }

    private static boolean verifyThrowable(Throwable thr) {
        if (thr == null)
            return true;
        String pkgName = thr.getClass().getPackage().getName();

        if (pkgName.startsWith("com.ibm.") || pkgName.startsWith("oracle.jdbc.") || pkgName.startsWith("weblogic.")
                || pkgName.startsWith("nc.jdbc.framework.exception")||pkgName.startsWith("javax.ejb"))
            return false;
        return verifyThrowable(thr.getCause());
    }

    private static Throwable extractException(Throwable exp) {
        if (exp instanceof InvocationTargetException) {
            return extractException(exp.getCause());
        } else
            return exp;
    }

    private Configuration getConfiguration() {
        if (configuration == null) {
            configuration = ConfigurationUtil.getConfiguration(RuntimeEnv.getInstance().getNCHome());
            configuration.getServerName();
        }
        return configuration;
    }
}
