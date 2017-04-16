package nc.ui.scm.puct.report.initializer;

import java.util.Map;

import nc.scmmm.pub.scmpub.report.adjustor.SCMRptDefaultAdjustor;
import nc.scmmm.pub.scmpub.report.scale.SCMRptAbsScalePrcStrategy;
import nc.scmmm.ui.scmpub.report.action.SCMRptQueryConditionDLGInitializer;
import nc.scmmm.ui.scmpub.report.adapter.ISCMReportQueryUIInitializer;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;

@SuppressWarnings("restriction")
public class FBPuCtRptUIInitialzer implements ISCMReportQueryUIInitializer {

	private static final long serialVersionUID = 4937644023732245941L;

	@Override
	public SCMRptQueryConditionDLGInitializer getQueryDLGInitializer() {
		return new FBPuCtRptRefInitialzer();
	}

	@Override
	public Map<String, String> getQueryTmpColumnMapping() {
		return null;
	}

	@Override
	public Class<? extends ISuperVO> getQueryTmpMainClass() {
		return null;
	}

	@Override
	public SCMRptAbsScalePrcStrategy getScalePrcStrategy() {
		return new FBPuCtRptScaleStrategy();
	}

	@Override
	public SCMRptDefaultAdjustor getSCMRptDefaultAdjustor() {
//		return new StockExecRptAdjustor();
		return null;
	}

	@Override
	public UFBoolean isPowerEnable() {
		return UFBoolean.TRUE;
	}
}
