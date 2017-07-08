package nc.ui.scm.puct.report.initializer;

import nc.scmmm.ui.scmpub.report.action.SCMRptQueryConditionDLGInitializer;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.scmpub.query.refregion.QDeptFilter;
import nc.ui.scmpub.query.refregion.QMarbasclassFilter;
import nc.ui.scmpub.query.refregion.QMarterialFilter;
import nc.ui.scmpub.query.refregion.QMarterialoidFilter;
import nc.ui.scmpub.query.refregion.QPsndocFilter;
import nc.ui.scmpub.query.refregion.QSupplierFilter;
import nc.vo.pub.lang.UFBoolean;

@SuppressWarnings("restriction")
public class FBPuCtRptRefInitialzer extends SCMRptQueryConditionDLGInitializer {

	@Override
	public String getMainOrgField() {
		// return ExecPriceConstant.POORDER_PK_ORG;
		return "pk_org";
	}

	/**
	 * 适配查询部门参照 modify by zhangrqb 0202
	 */
	private void filterDept(QueryConditionDLGDelegator delegator,
			String deptcode) {
		QDeptFilter deptFilter = QDeptFilter.createQDeptFilterOfPU(delegator,
				deptcode);
		// new QDeptFilter(delegator, deptcode);
		deptFilter.setPk_orgCode(this.getMainOrgField());
		deptFilter.filter();
		deptFilter.addEditorListener();
	}

	private void filterMarClass(QueryConditionDLGDelegator delegator,
			String fieldcode) {
		QMarbasclassFilter filter = new QMarbasclassFilter(delegator, fieldcode);
		filter.setPk_orgCode(this.getMainOrgField());
		filter.filter();
		filter.addEditorListener();
	}

	private void filterMaterial(QueryConditionDLGDelegator delegator,
			String fieldcode) {
		QMarterialFilter materialFilter = new QMarterialFilter(delegator,
				this.getMainOrgField(), fieldcode);
		materialFilter.setbDiscount(UFBoolean.FALSE);
		materialFilter.setbFee(UFBoolean.FALSE);
		materialFilter.filter();
		materialFilter.addEditorListener();
	}

	private void filterMaterialoid(QueryConditionDLGDelegator delegator,
			String fieldcode) {
		QMarterialoidFilter materialFilter = new QMarterialoidFilter(delegator,
				this.getMainOrgField(), fieldcode);
		materialFilter.setbDiscount(UFBoolean.FALSE);
		materialFilter.setbFee(UFBoolean.FALSE);
		materialFilter.filter();
		materialFilter.addEditorListener();
	}

	private void filterOrg(QueryConditionDLGDelegator delegator) {
		delegator.registerNeedPermissionOrgFieldCode(this.getMainOrgField());
	}

	private void filterPsn(QueryConditionDLGDelegator delegator,
			String fieldcode) {
		QPsndocFilter bizid = QPsndocFilter.createQPsndocFilterOfPU(delegator,
				fieldcode);
		// new QPsndocFilter(delegator, fieldcode);
		bizid.setPk_orgCode(this.getMainOrgField());
		bizid.addEditorListener();
	}

	private void filterSupplier(QueryConditionDLGDelegator delegator,
			String fieldcode) {
		QSupplierFilter filter = new QSupplierFilter(delegator, fieldcode);
		filter.setPk_orgCode(this.getMainOrgField());
		filter.filter();
		filter.addEditorListener();
	}

	@Override
	protected void initialize(QueryConditionDLGDelegator condDLGDelegator) {
		super.initialize(condDLGDelegator);
//		condDLGDelegator
//				.registerICriteriaEditorValidator(new ExecPriceValidate());
	}

	@Override
	protected void initRef(QueryConditionDLGDelegator delegator) {
//		 this.filterOrg(delegator);
		// this.filterMaterialoid(delegator, ExecPriceQueryConst.MATERIALCODE);
		// this.filterMaterial(delegator, ExecPriceQueryConst.MATERIALCODE);
		// this.filterMarClass(delegator, ExecPriceQueryConst.MARBASCLASS);
		// this.filterSupplier(delegator, ExecPriceQueryConst.PK_SUPPLIER);
		// this.filterDept(delegator, ExecPriceQueryConst.PK_DEPT);
		// this.filterPsn(delegator, ExecPriceQueryConst.CEMPLOYEEID);
		return;
	}
}
