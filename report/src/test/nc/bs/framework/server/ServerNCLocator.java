package nc.bs.framework.server;

import java.util.Properties;

import nc.bs.framework.common.ComponentMetaVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.Resolvable;
import nc.bs.framework.exception.ComponentException;
import nc.bs.framework.naming.ListableContext;
import nc.bs.logging.Logger;

/**
 * Created by UFSoft.
 * User: ºÎ¹ÚÓî
 * Date: 2005-1-22
 * Time: 15:12:06
 */
public class ServerNCLocator extends NCLocator implements ListableContext {

	public Object lookup(String name) throws ComponentException {
		Object obj = BusinessAppServer.getInstance().getServerContext().lookup(name);
		for (int i = 0; i < 3 && (obj instanceof Resolvable); i++) {
			try {
				Logger.warn("wait componet construct finished:" + name);
				Thread.sleep(20);
				obj = BusinessAppServer.getInstance().getServerContext().lookup(name);
			} catch (InterruptedException e) {
			}
		}
		if (obj instanceof Resolvable) {
			throw new ComponentException(name, " component now is constructing...");
		}
		return obj;
	}

	public String[] listNames() {
		return ((AbstractContext) BusinessAppServer.getInstance().getServerContext()).listNames();
	}

	public String[] listNames(Class<?> clazz) {
		return ((AbstractContext) BusinessAppServer.getInstance().getServerContext()).listNames(clazz);
	}

	public ComponentMetaVO getComponentMetaVO(String name) throws ComponentException {
		return ((AbstractContext) BusinessAppServer.getInstance().getServerContext()).getComponentMetaVO(name);
	}

	protected void init(Properties env) {
	}

}