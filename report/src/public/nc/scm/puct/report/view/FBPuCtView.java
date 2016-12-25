package nc.scm.puct.report.view;

import java.util.Set;

import nc.pub.pp.rept.execprice.constant.ExecPriceConstant;
import nc.pub.pp.rept.execprice.enumration.ExecPriceBillSrcEnum;
import nc.pub.pp.rept.execprice.view.SqlObject;
import nc.scm.puct.report.tmplate.source.FBPuCtRptConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldPreference;
import nc.scmmm.pub.scmpub.report.baseutil.SCMStringUtil;
import nc.scmmm.vo.scmpub.report.pub.ISCMReportContext;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.PermissionTableInfo;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.SCMBeanPath;
import nc.scmmm.vo.scmpub.report.viewfactory.sql.SCMPermissionBeanSqlView;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pp.util.StringUtils;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pub.JavaType;
import nc.vo.to.m4552.view.PuInvoiceViewVO;

public class FBPuCtView extends SCMPermissionBeanSqlView {

	private static final long serialVersionUID = -5633594163847083267L;
	private String patchWhere = "";
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
		  return super.getViewSql();
//	    if (null == this.sqlObject) {
//	      return super.getViewSql();
//	    }
//	    return this.getViewSqlForFirstQuery();
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
		  String[] ctnames = new String[]{FBPuCtRptFieldConstant.CTBZT,FBPuCtRptFieldConstant.CPROJECTID,FBPuCtRptFieldConstant.PK_ZBHT_SK,FBPuCtRptFieldConstant.PK_ZBHT_CODE_SK,FBPuCtRptFieldConstant.PK_ZBHT_XS,FBPuCtRptFieldConstant.PK_ZBHT_CODE_XS};
	    this.addSelectFields(SCMBeanPath.DOT, ctnames,ctnames);
	    this.addSelDefineFields(new FBPuCtRptFieldPreference[]{new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTCODE,FBPuCtRptFieldConstant.CPROJECTCODE,JavaType.String,FBPuCtRptConstant.SQLTYPE_DOUBLE)});
	    this.addSelDefineFields(FBPuCtRptConstant.SMART_FIELDS_FBCT);
	    this.addSelDefineFields(FBPuCtRptConstant.SMART_FIELDS_NMNY);
	  }
	  
	  private void addSelDefineFields(FBPuCtRptFieldPreference[] fields) {
		    for (FBPuCtRptFieldPreference field : fields) {
		      if (JavaType.String.equals(field.getFieldType())) {
		        this.addExpress( null , field.getFieldname(), JavaType.String);
		      }
		      else if (JavaType.UFDouble.equals(field.getFieldType())
		          || JavaType.Integer.equals(field.getFieldType())) {
		        this.addExpress("", field.getFieldname(), field.getFieldType());
		      }else {
			    this.addExpress("null", field.getFieldname(), field.getFieldType());
		      }
		    }
		  }
}
