package nc.scm.puct.report.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ibm.db2.jcc.am.df;

import nc.scm.puct.report.tmplate.source.FBPuCtRptConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldPreference;
import nc.scmmm.pub.scmpub.report.baseutil.SCMStringUtil;
import nc.scmmm.vo.scmpub.report.pub.ISCMReportContext;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.PermissionTableInfo;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.SCMBeanPath;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.SCMPermissionBeanSqlView;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.JavaType;

public class FBPuCtView extends SCMPermissionBeanSqlView {
	private static final long serialVersionUID = -5633594163847083267L;
	private String patchWhere = "";
	private boolean has_project =false;
	private boolean has_zbct =false;
	private boolean has_supply =false;

	public boolean isHas_project() {
		return has_project;
	}

	public boolean isHas_zbct() {
		return has_zbct;
	}

	public boolean isHas_supply() {
		return has_supply;
	}

	public void setHas_project(boolean has_project) {
		this.has_project = has_project;
	}

	public void setHas_zbct(boolean has_zbct) {
		this.has_zbct = has_zbct;
	}

	public void setHas_supply(boolean has_supply) {
		this.has_supply = has_supply;
	}

	public FBPuCtView(
	      ISCMReportContext context) {
	    super(new CtPuVO(), context);
	  }

	public String getPatchWhere() {
		return patchWhere;
	}

	public void setPatchWhere(String patchWhere) {
		this.patchWhere = patchWhere;
	}

	@Override
	protected Set<PermissionTableInfo> initFixTableAlias() {
		return null;
	}

	  @Override
	  public String getViewSql() {
		    StringBuilder bf = new StringBuilder(" select distinct ");
		    bf.append(this.getSelectFieldsPart());
		    bf.append(" from ");
		    bf.append(this.getViewFromPart());
//		    if(this.isHas_project()){
//		    	bf.append(" , ct_pu_b , bd_project ");
////		    	bf.append(" ct_pu_b.pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.dr = 0 and  bd_project.pk_project = ct_pu_b.pk_project and bd_project.dr = 0  ");
//		    }
//		    if(this.isHas_zbct()){
//		    	bf.append(" inner join ct_pu_b on ct_pu_b.pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.dr = 0 ");
//		    	bf.append(" inner join bd_project on bd_project.pk_project = ct_pu_b.pk_project and bd_project.dr = 0 ");
//		    }
//		    if(this.isHas_supply()){
//		    	bf.append(" inner join ct_pu_b on ct_pu_b.pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.dr = 0 ");
//		    	bf.append(" inner join bd_project on bd_project.pk_project = ct_pu_b.pk_project and bd_project.dr = 0 ");
//		    }
		    bf.append(" where ");
//		    if(this.isHas_project()){
//		    	bf.append("  ct_pu_b.pk_ct_pu = ct_pu.pk_ct_pu and  bd_project.pk_project = ct_pu_b.cbprojectid and bd_project.dr = 0 and ct_pu_b.dr = 0 ");
//		    	bf.append(" and ");
//		    }
		    bf.append(this.getViewWherePart());
		    if (this.isGroup()) {
		      bf.append(" group by ");
		      bf.append(this.getGroupFieldsPart());
		      if (!SCMStringUtil.isSEmptyOrNull(this.getHaving())) {
		        bf.append(" having ");
		        bf.append(this.getHaving());
		      }
		    }
		    if (this.isOrder()) {
		      bf.append(" order by ");
		      bf.append(this.getOrderFieldsPart());
		    }
		    return bf.toString();
		  }

	  public String getViewSqlForFirstQuery() {
//	    String headtable = null;
//	    String bodytable = null;
//	    String headpk = null;
//	    if (this.billsrc == ExecPriceBillSrcEnum.POINVOICE) {
//	      headtable = ExecPriceConstant.POINVOICE_H;
//	      bodytable = ExecPriceConstant.POINVOICE_B;
//	      headpk = PuInvoiceViewVO.PK_INVOICE;
//	    }
//	    else {
//	      headtable = ExecPriceConstant.POORDER_H;
//	      bodytable = ExecPriceConstant.POORDER_B;
//	      headpk = OrderItemVO.PK_ORDER;
//	    }
//
	    StringBuilder bf = new StringBuilder("");
//
//	    bf.append(" select  ");
//	    bf.append(this.sqlObject.getMatSql());
//	    bf.append(" from  ( ");
//
//	    /** ---子查询A--start--- */
//	    bf.append(" select ");
//	    bf.append(this.getSelectFieldsPart());
//	    bf.append(" from ");
//	    bf.append(this.getViewFromPart());
//	    bf.append(" where ");
//	    bf.append(this.getViewWherePart());
//	    if (this.isGroup()) {
//	      bf.append(" group by ");
//	      bf.append(this.getGroupFieldsPart());
//	      if (!SCMStringUtil.isSEmptyOrNull(this.getHaving())) {
//	        bf.append(" having ");
//	        bf.append(this.getHaving());
//	      }
//	    }
//	    if (this.isOrder()) {
//	      bf.append(" order by ");
//	      bf.append(this.getOrderFieldsPart());
//	    }
//	    /** ---子查询A--end--- */
//	    bf.append(" ) " + this.sqlObject.getMatAlias());
//	    bf.append(" inner join ( ");
//
//	    /** ---子查询B--start--- */
//	    bf.append(" select distinct ");
//
//	    bf.append(headtable + ".corigcurrencyid corigcurrencyid, ");
//	    bf.append(bodytable + ".pk_srcmaterial pk_srcmaterial, ");
//	    bf.append(headtable + ".pk_org  pk_org , ");
//	    bf.append(" bd_materialfi.costprice referencecost, ");
//	    bf.append(" bd_materialfi.planprice planprice ");
//	    bf.append(" from ");
//	    bf.append(headtable + " " + headtable);
//	    bf.append(" inner join " + bodytable + " " + bodytable);
//	    bf.append(" on ");
//	    bf.append(headtable + "." + headpk + " = " + bodytable + "." + headpk);
//	    bf.append(" inner join bd_material_v bd_material_v ");
//	    bf.append(" on ");
//	    bf.append(" bd_material_v.pk_source = " + bodytable + ".pk_srcmaterial ");
//	    bf.append(" left join bd_materialfi bd_materialfi ");
//	    bf.append(" on ");
//	    bf.append(" bd_material_v.pk_material = bd_materialfi.pk_material ");
//	    bf.append(" and bd_materialfi.pk_org = " + headtable + ".pk_org ");
//
//	    bf.append(" where ");
//	    String sql = this.getViewWherePart();
//	    if (StringUtils.isNotBlank(sql)) {
//	      // bf.append(sql.replaceFirst("bd_material.pk_marbasclass",
//	      // "bd_material_v.pk_marbasclass"));
//	      bf.append(sql.replaceAll("bd_material", "bd_material_v"));
//	    }
//
//	    /** ---子查询B--end--- */
//	    bf.append(" ) " + this.sqlObject.getOrgAlias());
//	    bf.append(" on ");
//	    bf.append(this.sqlObject.getMatAlias() + SqlObject.dot
//	        + "corigcurrencyid = " + this.sqlObject.getOrgAlias() + SqlObject.dot
//	        + "corigcurrencyid");
//	    bf.append(" and ");
//	    bf.append(this.sqlObject.getMatAlias() + SqlObject.dot
//	        + "pk_srcmaterial = " + this.sqlObject.getOrgAlias() + SqlObject.dot
//	        + "pk_srcmaterial ");

	    return bf.toString();
	  }

	  public void initDetails() {
		  List<String> ctnames =new ArrayList<String>();
		  for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ZBCT){
				  ctnames.add(field.getFieldname());
		  }
	    this.addSelectFields(SCMBeanPath.DOT, ctnames.toArray(new String[0]),ctnames.toArray(new String[0]));
	    this.addSelDefineFields(FBPuCtRptConstant.SMART_FIELDS_ADD);
	    this.addSelDefineFields(FBPuCtRptConstant.SMART_FIELDS_FBCT);
	    this.addSelDefineFields(FBPuCtRptConstant.SMART_FIELDS_NMNY);
	  }
	  
	  private void addSelDefineFields(FBPuCtRptFieldPreference[] fields) {
		    for (FBPuCtRptFieldPreference field : fields) {
		      if (JavaType.String.equals(field.getFieldType())) {
		        this.addExpress( "null" , field.getFieldname(), JavaType.String);
		      }
//		      else if (JavaType.UFDouble.equals(field.getFieldType())
//		          || JavaType.Integer.equals(field.getFieldType())) {
//		        this.addExpress("", field.getFieldname(), field.getFieldType());
//		      }
		      else {
			    this.addExpress("null", field.getFieldname(), field.getFieldType());
		      }
		    }
		  }
}
