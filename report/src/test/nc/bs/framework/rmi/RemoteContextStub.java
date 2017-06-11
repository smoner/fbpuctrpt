package nc.bs.framework.rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nc.bs.framework.common.ComponentMetaVO;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.core.common.ConfigMetaVO;
import nc.bs.framework.core.common.IConfigMetaService;
import nc.bs.framework.core.conf.Configuration;
import nc.bs.framework.exception.ComponentException;
import nc.bs.framework.exception.ComponentNotFoundException;
import nc.bs.framework.naming.Context;
import nc.bs.logging.Logger;

/**
 * 
 * @author He Guan Yu
 * 
 * @date May 7, 2010
 * 
 */
public class RemoteContextStub implements Context {
    private Address dispatchUrl;

    private String module;

    private ConfigMetaVO configMetaVO;

    private Context remoteMetaContext;

    private Map<String, ComponentMetaVO> namedMetasMap = new HashMap<String, ComponentMetaVO>();

    private Map<String, Object> proxyMap = new HashMap<String, Object>();

    private BlockingQueue<ComponentMetaVO> queue = new LinkedBlockingQueue<ComponentMetaVO>();

    private RemoteAddressSelector defRas;

    public RemoteContextStub(String dispatchURL, String module) throws ComponentException {

        ComponentMetaVO metaVO = new ComponentMetaVO();
        metaVO.setInterfaces(new String[] { Context.class.getName() });

        if (module != null) {
            metaVO.setModule(module);
            metaVO.setName(Context.REMOTE_META_SERVICE + "." + module);
        } else {
            metaVO.setName(Context.REMOTE_META_SERVICE);
        }

        namedMetasMap.put(metaVO.getName(), metaVO);

        Address url = null;
        try {
            url = new Address(dispatchURL);
        } catch (MalformedURLException e) {
            throw new ComponentException("invalid url: " + dispatchURL);
        }

        defRas = new SimpleRemoteAddressSelector(url);

        remoteMetaContext = (Context) RemoteProxyFactory.getDefault().createRemoteProxy(
                RemoteContextStub.class.getClassLoader(), metaVO, defRas);

        proxyMap.put(metaVO.getName(), remoteMetaContext);
        this.dispatchUrl = url;
        this.module = module;
        init();

    }

    public Object lookup(String name) {
        Object so = proxyMap.get(name);
        if (so != null) {
            return so;
        }
        ComponentMetaVO metaVO = getMetaOnDemand(name);

        if (metaVO == null) {
            throw new ComponentNotFoundException(name, "no remote componnet found from server");
        }
        so = proxyMap.get(metaVO.getName());
        if (so != null) {
            return so;

        }
        RemoteAddressSelector ras = new GroupBasedRemoteAddressSelector(getRealTarget(metaVO), getServerGroup(metaVO));

        so = RemoteProxyFactory.getDefault().createRemoteProxy(RemoteContextStub.class.getClassLoader(), metaVO, ras);

        proxyMap.put(metaVO.getName(), so);

        return so;

    }

    private String getServerGroup(ComponentMetaVO metaVO) {
        if (metaVO.getModule() == null || metaVO.getModule().equals("BizAppServer")) {
            return null;
        }
        if (configMetaVO == null) {
            return null;
        }

        String sg = configMetaVO.getConfiguration().getServiceAtGroupName(metaVO.getModule(), metaVO.getName());
        if (sg == null && metaVO.getModule() != null) {
            sg = configMetaVO.getConfiguration().getModuleAtGroupName(metaVO.getModule());
        }

        return sg == null ? "default" : sg;
    }

    private Address getRealTarget(ComponentMetaVO metaVO) {
        if (configMetaVO == null) {
            return dispatchUrl;
        }

        Configuration config = configMetaVO.getConfiguration();
        if (config != null) {
            String sg = config.getServiceAtWebServer(metaVO.getName());

            if (sg == null && metaVO.getModule() != null) {
                sg = config.getModuleAtWebServer(metaVO.getModule());
            }

            String u = config.getWebServerConnectorUrl(sg);
            if (u != null) {
                try {
                    return new Address(u);
                } catch (MalformedURLException e) {
                    return dispatchUrl;
                }
            }
        }

        return dispatchUrl;
    }

    private ComponentMetaVO getLocalMetaVO(String name) {
        return namedMetasMap.get(name);
    }

    private ComponentMetaVO getMetaOnDemand(String name) {
        ComponentMetaVO metaVO = getLocalMetaVO(name);
        if (metaVO == null) {
            Object obj = remoteMetaContext.lookup(name);
            ComponentMetaVO[] metas = null;
            if (obj.getClass().isArray()) {
                metas = (ComponentMetaVO[]) obj;
            } else {
                metas = new ComponentMetaVO[1];
                metas[0] = (ComponentMetaVO) obj;
            }
            metaVO = metas[0];
            indexMetas(metas);
        }
        return metaVO;
    }

    private void indexMetas(ComponentMetaVO[] metas) {
        for (int i = 0; i < metas.length; i++) {
            if (metas[i] != null) {
                if (namedMetasMap.get(metas[i].getName()) == null) {
                    synchronized (namedMetasMap) {
                        if (namedMetasMap.get(metas[i].getName()) != null) {
                            continue;
                        }

                        namedMetasMap.put(metas[i].getName(), metas[i]);
                        if (!"BizAppServer".equals(metas[i].getModule())) {
                            queue.offer(metas[i]);
                        }
                        String[] alias = metas[i].getAlias();
                        if (alias != null) {
                            for (int j = 0; j < alias.length; j++) {
                                namedMetasMap.put(alias[j], metas[i]);
                            }
                        }

                    }
                }
            }
        }

    }
    

    private void init() {
        if (RuntimeEnv.getInstance().isRunningInServer()) {
            return;
        }
        try {
            IConfigMetaService object = (IConfigMetaService) lookup(IConfigMetaService.class.getName());

            configMetaVO = object.getConfigMetaVO();

        } catch (Exception exp) {
            Logger.debug(exp.getMessage()
                    + (exp.getCause() == null ? "" : ", root cause: " + exp.getCause().getMessage()));
        }

        if (configMetaVO != null) {
            String version = configMetaVO.getVersion();
            
            final File clientHome = new File(System.getProperty("client.code.dir", System.getProperty("user.home")));
            saveCfg(new File(clientHome, version + ".ucfg"), configMetaVO.getConfiguration());
            final File f = new File(clientHome, version + ".umv");

            loadFromFile(f);
            Thread synThread = new Thread() {
                public void run() {
                    RandomAccessFile raf;
                    try {
                        raf = new RandomAccessFile(f, "rwd");
                        if (f.length() > 0)
                            raf.seek(raf.length());
                    } catch (Exception e) {
                        Logger.warn("init error", e);
                        return;
                    }

                    try {
                        while (true) {
                            ComponentMetaVO vo;
                            vo = queue.take();
                            byte[] bytes = getBytes(vo);
                            raf.writeInt(bytes.length);
                            raf.write(bytes);
                        }
                    } catch (InterruptedException ie) {

                    } catch (IOException e) {
                        Logger.warn("init error", e);
                    } finally {
                        if (raf != null) {
                            try {
                                raf.close();
                                raf = null;
                            } catch (IOException e1) {

                            }
                        }
                    }

                }

            };
            synThread.setDaemon(true);
            synThread.start();
        } else {
            final File clientHome = new File(System.getProperty("client.code.dir", System.getProperty("user.home")));
            File[] fs = clientHome.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".umv");

                }

            });

            File f = null;
            for (File f1 : fs) {
                if (f == null || f1.lastModified() > f.lastModified()) {
                    f = f1;
                }
            }

            if (f != null) {
                loadFromFile(f);

                String version = f.getName().substring(0, f.getName().lastIndexOf("."));

                File f1 = new File(f.getParentFile(), version + ".ucfg");
                if (f1.exists()) {
                    Configuration cfg = loadCfg(f1);
                    if (cfg != null) {
                        configMetaVO = new ConfigMetaVO();
                        configMetaVO.setVersion(version);
                        configMetaVO.setConfiguration(cfg);
                    }
                }
            }

        }
    }

    private void saveCfg(File f, Configuration cfg) {
        FileOutputStream fout = null;
        ObjectOutputStream objOut = null;
        try {
            fout = new FileOutputStream(f);
            objOut = new ObjectOutputStream(fout);
            objOut.writeObject(configMetaVO.getConfiguration());
            objOut.flush();
        } catch (Exception e2) {

        } finally {
            try {
                if (fout != null)
                    fout.close();
            } catch (IOException e) {
            } finally {
                if (objOut != null)
                    try {
                        objOut.close();
                    } catch (IOException e) {
                    }
            }
        }
    }

    private Configuration loadCfg(File f) {
        FileInputStream fin = null;
        ObjectInputStream objIn = null;
        try {
            fin = new FileInputStream(f);
            objIn = new ObjectInputStream(fin);
            return (Configuration) objIn.readObject();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException e) {
            } finally {
                if (objIn != null)
                    try {
                        objIn.close();
                    } catch (IOException e) {
                    }
            }
        }
    }

    private void loadFromFile(File f) {
        if (f.exists()) {
            ArrayList<ComponentMetaVO> list = new ArrayList<ComponentMetaVO>();
            RandomAccessFile fin = null;
            try {
                fin = new RandomAccessFile(f, "r");
                fin.seek(0);
                while (true) {
                    if (fin.length() - fin.getFilePointer() < 4) {
                        break;
                    }
                    int len = fin.readInt();
                    if (len > 1024 * 1024 * 8) {
                        break;
                    }
                    byte[] bytes = new byte[len];
                    if (len > 0) {
                        int currentLen = fin.read(bytes);
                        if (currentLen == len) {
                            ComponentMetaVO vo = toComponentMetaVO(bytes);
                            if (vo != null) {
                                list.add(vo);
                            }
                        } else {
                            break;
                        }

                    } else {
                        break;
                    }
                }

            } catch (Exception e) {
                Logger.error("", e);
            } finally {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException e) {

                    }
                }
            }

            for (ComponentMetaVO vo : list) {
                if (namedMetasMap.get(vo.getName()) == null) {
                    namedMetasMap.put(vo.getName(), vo);
                    String[] alias = vo.getAlias();
                    if (alias != null) {
                        for (int j = 0; j < alias.length; j++) {
                            namedMetasMap.put(alias[j], vo);
                        }
                    }
                }
            }

        }

    }

    private ComponentMetaVO toComponentMetaVO(byte[] s) {
        ByteArrayInputStream bin = new ByteArrayInputStream(s);
        ObjectInputStream objIn;
        try {
            objIn = new ObjectInputStream(bin);
            return (ComponentMetaVO) objIn.readObject();
        } catch (Exception e) {
            Logger.error("deserialize component meta vo error", e);
            return null;
        }

    }

    private byte[] getBytes(ComponentMetaVO vo) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            ObjectOutputStream output = new ObjectOutputStream(bout);
            output.writeObject(vo);
            output.close();
            return bout.toByteArray();
        } catch (IOException e) {
            Logger.error("serialize component meta vo error", e);
            return null;
        }

    }

    public String getModule() {
        return module;
    }

    public Address getDispatchUrl() {
        return dispatchUrl;
    }

    public String toString() {
        return dispatchUrl + (module == null ? "" : "{" + module + "}");
    }

}
