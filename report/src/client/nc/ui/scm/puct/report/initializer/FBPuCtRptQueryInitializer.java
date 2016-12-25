package nc.ui.scm.puct.report.initializer;

import nc.scmmm.pub.scmpub.report.adjustor.SCMRptDefaultAdjustor;
import nc.scmmm.pub.scmpub.report.scale.SCMRptAbsScalePrcStrategy;
import nc.scmmm.pubitf.scmpub.report.adapter.ISCMReportQueryInitializer;
import nc.vo.pub.lang.UFBoolean;

public class FBPuCtRptQueryInitializer implements ISCMReportQueryInitializer {

	  private static final long serialVersionUID = 508939410896285038L;

	  @Override
	  public SCMRptAbsScalePrcStrategy getScalePrcStrategy() {
	    return new FBPuCtRptScaleStrategy();
	  }

	  @Override
	  public SCMRptDefaultAdjustor getSCMRptDefaultAdjustor() {
//	    return new ExecPriceRptAdjustor();
	     return null;
	  }

	  @Override
	  public UFBoolean isPowerEnable() {
	    return UFBoolean.TRUE;
	  }

	}
