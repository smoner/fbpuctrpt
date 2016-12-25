package nc.ui.scm.puct.report.action;

import nc.scmmm.ui.scmpub.report.action.SCMRptAbsQueryAction;
import nc.scmmm.ui.scmpub.report.adapter.ISCMReportQueryUIInitializer;
import nc.ui.scm.puct.report.initializer.FBPuCtRptUIInitialzer;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;

public class FBPuCtRptQueryAction extends SCMRptAbsQueryAction {

	  @Override
	  protected void doBusinessProcess(BaseQueryCondition condition,
	      IContext context) {
	  }

	  @Override
	  protected ISCMReportQueryUIInitializer getReportInitializer() {
	    return new FBPuCtRptUIInitialzer();
	  }
	}