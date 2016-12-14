package nc.scm.puct.report.view;

import java.util.Set;

import nc.scmmm.vo.scmpub.report.viewfactory.sql.PermissionTableInfo;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.SCMPermissionBeanSqlView;

public class FBPuCtView extends SCMPermissionBeanSqlView {

	private static final long serialVersionUID = -5633594163847083267L;

	@Override
	protected Set<PermissionTableInfo> initFixTableAlias() {
		return null;
	}

}
