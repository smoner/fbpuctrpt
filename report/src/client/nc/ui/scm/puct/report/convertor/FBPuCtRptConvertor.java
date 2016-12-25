package nc.ui.scm.puct.report.convertor;

import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.scmmm.pub.scmpub.report.adapter.SCMRptAbsSubConvertor;
import nc.scmmm.pubitf.scmpub.report.adapter.ISCMReportQueryInitializer;
import nc.ui.scm.puct.report.initializer.FBPuCtRptQueryInitializer;

import com.ufida.dataset.IContext;

public class FBPuCtRptConvertor extends SCMRptAbsSubConvertor {

	  @Override
	  protected void doBusinessProcess(IQueryCondition condition, IContext context) {
	  }

	  @Override
	  protected ISCMReportQueryInitializer getReportInitializer() {
	    return new FBPuCtRptQueryInitializer();
	  }

	}
