package nc.ui.scm.puct.report.initializer;

import java.util.Map;

import nc.pub.pp.rept.stockexec.templet.StockExecQueryInitializer;
import nc.scmmm.ui.scmpub.report.action.SCMRptQueryConditionDLGInitializer;
import nc.scmmm.ui.scmpub.report.adapter.ISCMReportQueryUIInitializer;
import nc.vo.pub.ISuperVO;

@SuppressWarnings("restriction")
public class FBPuCtRptUIInitialzer extends StockExecQueryInitializer
implements ISCMReportQueryUIInitializer {


	private static final long serialVersionUID = -7682897658603594150L;

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

}
