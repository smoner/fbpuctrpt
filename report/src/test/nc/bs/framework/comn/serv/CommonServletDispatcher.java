package nc.bs.framework.comn.serv;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.rmi.http.server.HttpRMIContext;
import nc.bs.framework.rmi.http.server.HttpRMIHandler;
import nc.bs.framework.rmi.server.RMIHandler;
import nc.bs.logging.Log;

/**
 * 
 * @author He Guan Yu
 * 
 * @date 2010-11-13
 * 
 */
public class CommonServletDispatcher extends HttpServlet {

	private static final long serialVersionUID = 964275566221035296L;

	private static final Log log = Log
			.getInstance(CommonServletDispatcher.class);

	private transient RMIHandler rmiHandler;

	public void init() throws ServletException {
		log.debug("ServletDispatcher.initing......");
		rmiHandler = new HttpRMIHandler();
		log.debug("ServletDispatcher.inited");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		throw new ServletException("not supported");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			rmiHandler.handle(new HttpRMIContext(request, response));
		} catch (Throwable e) {
			log.error("remote service error", e);
		}
	}

	public void destroy() {
	}
}