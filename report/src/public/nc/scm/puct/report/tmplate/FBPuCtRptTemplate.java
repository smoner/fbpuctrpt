package nc.scm.puct.report.tmplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.scm.puct.report.temtable.FBPuCtRptTemptableUtils;
import nc.scm.puct.report.tmplate.source.FBPuCtRptConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldPreference;
import nc.scm.puct.report.tmplate.source.FBPuCtRptVO;
import nc.scm.puct.report.view.FBPuCtView;
import nc.scmmm.pub.scmpub.report.rptutil.SCMProviderMetaUtil;
import nc.scmmm.pub.scmpub.report.scale.SCMRptAbsScalePrcStrategy;
import nc.scmmm.pub.scmpub.report.smart.templet.SimpleAbsRptDataSetTemplet;
import nc.scmmm.vo.scmpub.report.entity.SCMRptColumnInfo;
import nc.scmmm.vo.scmpub.report.entity.SCMRptDataSet;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMField;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMProviderMetaData;
import nc.scmmm.vo.scmpub.report.viewfactory.define.SCMView;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.data.IRowSet;

import org.apache.commons.lang.StringUtils;

public class FBPuCtRptTemplate extends SimpleAbsRptDataSetTemplet {
	private boolean has_project =false;
	private boolean has_zbct =false;
	private boolean has_supply =false;
	private FBPuCtView view = null;
	// 来自查询对话框的查询条件vo
	public List<ConditionVO> condsFromQryDlgList;

	private void addHeadFields(SCMProviderMetaData metaData) {
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_ZBCT);
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_ADD);
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_FBCT);
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_NMNY);
	}

	private void createFields(SCMProviderMetaData metaData,
			FBPuCtRptFieldPreference[] fields) {
		for (FBPuCtRptFieldPreference field : fields) {
			SCMField scmfiled = SCMProviderMetaUtil.getDynamicField(
					field.getFieldname(), field.getFieldChnName(),
					field.getFieldType());
			metaData.addField(scmfiled);
		}
	}

	@Override
	protected SCMProviderMetaData getSCMRptMetaData() throws BusinessException {
		SCMProviderMetaData metaData = new SCMProviderMetaData();
		this.addHeadFields(metaData);
		return metaData;
	}

	@Override
	protected SCMView getSCMView() {
		if (!this.getScmQueryCondition().isHaveCondition()) {
			FBPuCtView initView = new FBPuCtView(this.getScmReportContext());
			initView.initDetails();
			return initView;
		}
		this.view = new FBPuCtView(this.getScmReportContext());
		this.view.initDetails();
		this.processWhere();
		this.view.setHas_project(has_project);
		this.view.setHas_supply(has_supply);
		this.view.setHas_zbct(has_zbct);
		return this.view;
	}

	private void processWhere() {
		ConditionVO[] generalConds = this.getScmQueryCondition()
				.getAllQueryConditions();
		StringBuilder where = new StringBuilder();
		this.transQryConds(generalConds, where);
		String sql = new ConditionVO().getSQLStr(this.condsFromQryDlgList
				.toArray(new ConditionVO[this.condsFromQryDlgList.size()]));
		if (StringUtils.isNotBlank(sql)) {
			where.append(" and ");
			where.append(sql);
		}
		// 设置默认的查询条件
		// where.append(this.getDefaultWhere());
		this.view.setPatchWhere(where.toString());
		this.view.addWherePart(where.toString());
	}

	private void transQryConds(ConditionVO[] generalConds, StringBuilder where) {
		this.condsFromQryDlgList = new ArrayList<ConditionVO>();
		for (ConditionVO condvo : generalConds) {
			// 填报主题
			if (condvo.getFieldCode().equals(FBPuCtRptFieldConstant.CTBZT)) {
				condvo.setFieldCode("ct_pu.pk_org");
				this.condsFromQryDlgList.add(condvo);
			}
			// 项目编码
			else if (condvo.getFieldCode().equals("projectcode")) {
//				this.has_project=true;
//				where.append(" and ");
//				where.append(FBPuCtRptConstant.TABLE_PROJECT+".project_code ");
//				if (condvo.getOperaCode().equals("=")) {
//					where.append(" = '" + condvo.getValue()+ "' ");
//				} 
//				else if (condvo.getOperaCode().contains("left like")) {
//					where.append("  like '%" + condvo.getValue()+ "%' ");
//				}
				where.append(" and ");
				if (condvo.getOperaCode().equals("=")) {
					where.append(" ct_pu.pk_ct_pu in ( select distinct ct_pu_b.pk_ct_pu from bd_project , ct_pu_b where  bd_project.pk_project = ct_pu_b.cbprojectid  and bd_project.project_code  =   '" + condvo.getValue()+ "'  ) ");
				} 
				else if (condvo.getOperaCode().contains("left like")) {
					where.append(" ct_pu.pk_ct_pu in ( select distinct ct_pu_b.pk_ct_pu from bd_project , ct_pu_b where  bd_project.pk_project = ct_pu_b.cbprojectid  and bd_project.project_code like '" + condvo.getValue()+ "%'  ) ");
				}
			}
			// 项目名称
			else if (condvo.getFieldCode().equals("projectname")) {
//				this.has_project=true;
//				where.append(" and ");
//				where.append(FBPuCtRptConstant.TABLE_PROJECT+".project_name ");
//				if (condvo.getOperaCode().equals("=")) {
//					where.append(" = '" + condvo.getValue()+ "' ");
//				} 
//				else if (condvo.getOperaCode().contains("left like")) {
//					where.append("  like '" + condvo.getValue()+ "%' ");
//				}
				where.append(" and ");
				if (condvo.getOperaCode().equals("=")) {
					where.append(" ct_pu.pk_ct_pu in ( select distinct ct_pu_b.pk_ct_pu from bd_project , ct_pu_b where  bd_project.pk_project = ct_pu_b.cbprojectid  and bd_project.project_name = '" + condvo.getValue()+ "'  ) ");
				} 
				else if (condvo.getOperaCode().contains("left like")) {
					where.append(" ct_pu.pk_ct_pu in ( select distinct ct_pu_b.pk_ct_pu from bd_project , ct_pu_b where  bd_project.pk_project = ct_pu_b.cbprojectid  and bd_project.project_name like '" + condvo.getValue()+ "%'  ) ");
				}
			}
			// 总包合同编码
			else if (condvo.getFieldCode().equals("xsvbillcode")) {
				if (condvo.getOperaCode().equals("=")) {
					where.append(" and ( " );
						where.append(" ct_pu.vdef5 =  '" + condvo.getValue() + "' " );
						where.append(" or ");
						where.append(" ct_pu.vdef15 = '" + condvo.getValue() + "' ");
					where.append(" ) ");
				} 
				else if (condvo.getOperaCode().contains("left like")) {
					where.append(" and ( " );
						where.append(" ct_pu.vdef5  like '"	+ condvo.getValue() + "%'" );
						where.append(" or ");
						where.append(" ct_pu.vdef15  like  '"+ condvo.getValue() + "%'  ");
					where.append(" ) ");
				}
			}
			// 总包合同名称
			else if (condvo.getFieldCode().equals("xsctname")) {
				this.has_zbct=true;
//				if (condvo.getOperaCode().equals("=")) {
//					where.append(" and ( " );
//						where.append(FBPuCtRptConstant.TABLE_SK+".ctname = '" + condvo.getValue()+ "' ");
//						where.append(" or ");
//						where.append(FBPuCtRptConstant.TABLE_XS+".ctname = '" + condvo.getValue()+ "' ");
//					where.append(" ) ");
//				} 
//				else if (condvo.getOperaCode().contains("left like")) {
//					where.append(" and ( " );
//						where.append(FBPuCtRptConstant.TABLE_SK+".ctname  like '" + condvo.getValue()+ "%' ");
//						where.append(" or ");
//						where.append(FBPuCtRptConstant.TABLE_XS+".ctname  like '" + condvo.getValue()+ "%' ");
//					where.append(" ) ");
//				}
				if (condvo.getOperaCode().equals("=")) {
					where.append(" and ( " );
						where.append(" ct_pu.vdef5 in  ( select distinct vbillcode from  fct_ar  where fct_ar.dr  = 0 and fct_ar.ctname  = '" + condvo.getValue()+ "' ) " );
						where.append(" or ");
						where.append(" ct_pu.vdef15 in ( select distinct vbillcode from  ct_sale where ct_sale.dr = 0 and ct_sale.ctname = '" + condvo.getValue() + "' ) ");
					where.append(" ) ");
				} 
				else if (condvo.getOperaCode().contains("left like")) {
					where.append(" and ( " );
						where.append(" ct_pu.vdef5 in  ( select distinct vbillcode from  fct_ar  where fct_ar.dr  = 0 and fct_ar.ctname   like '" + condvo.getValue() + "%' ) " );
						where.append(" or ");
						where.append(" ct_pu.vdef15 in ( select distinct vbillcode from  ct_sale where ct_sale.dr = 0 and ct_sale.ctname  like '" + condvo.getValue() + "%' ) ");
					where.append(" ) ");
				}
			}
			// 采购合同编码
			else if (condvo.getFieldCode().equals(CtPuVO.VBILLCODE)) {
				where.append(" and ");
				where.append("ct_pu.vbillcode");
				if (condvo.getOperaCode().equals("=")) {
					where.append(" = '" + condvo.getValue()+ "' ");
				} 
				else if (condvo.getOperaCode().contains("left like")) {
					where.append("  like '" + condvo.getValue()+ "%' ");
				}
			}
			// 采购合同名称
			else if (condvo.getFieldCode().equals(CtPuVO.CTNAME)) {
				where.append(" and ");
				where.append("ct_pu.ctname");
				if (condvo.getOperaCode().equals("=")) {
					where.append(" = '" + condvo.getValue()+ "' ");
				} 
				else if (condvo.getOperaCode().contains("left like")) {
					where.append("  like '" + condvo.getValue()+ "%' ");
				}
			}
			// 采购供应商
			else if (condvo.getFieldCode().equals("cvendorid")) {
				this.has_supply=true;
				where.append(" and ");
				where.append("ct_pu.cvendorid");
				if (condvo.getOperaCode().equals("=")) {
					where.append(" = '" + condvo.getValue()+ "' ");
				} 
				else if (condvo.getOperaCode().contains("in")) {
					where.append(" in " + condvo.getValue()+ " ");
				}
			}
			// 总包合同签订日期
			else if (condvo.getFieldCode().equals("subscribedate_xs")) {
				String str = condvo.getSQLStr();
				String str_xs=str.replace("subscribedate_xs", "ct_sale.dbilldate");
				String str_sk=str.replace("subscribedate_xs", "fct_ar.dbilldate");
//				if (condvo.getOperaCode().equals("=")) {
					where.append(" and ( " );
						where.append(" ct_pu.vdef5 in  ( select distinct vbillcode from  fct_ar  where fct_ar.dr  = 0 "+str_sk+" ) " );
						where.append(" or ");
						where.append(" ct_pu.vdef15 in ( select distinct vbillcode from  ct_sale where ct_sale.dr = 0 "+str_xs+" ) ");
					where.append(" ) ");
//				} 
//				else if (condvo.getOperaCode().contains("left like")) {
//					where.append(" and ( " );
//						where.append(" ct_pu.vdef5 in  ( select distinct vbillcode from  fct_ar  where fct_ar.dr  = 0 and fct_ar.ctname   like '" + condvo.getValue() + "%' ) " );
//						where.append(" or ");
//						where.append(" ct_pu.vdef15 in ( select distinct vbillcode from  ct_sale where ct_sale.dr = 0 and ct_sale.ctname  like '" + condvo.getValue() + "%' ) ");
//					where.append(" ) ");
//				}
				String ss = null;
			}
			// 采购合同签订日期
			else if (condvo.getFieldCode().equals("subscribedate")) {
				String str = condvo.getSQLStr();
				str=str.replace("dbilldate", "ct_pu.subscribedate");
				where.append(str);}
			// 采购合同单据日期
			else if (condvo.getFieldCode().equals("dbilldate")) {
				String str = condvo.getSQLStr();
				str=str.replace("dbilldate", "ct_pu.dbilldate");
				where.append(str);
//				 String[] dates = condvo.getValue().split(",");
//				 String beginDate = dates[0];
//				 String endDate = dates[1];
//				 if(StringUtils.isNotBlank(beginDate)&&!beginDate.toLowerCase().equals("isnull")){
//					 where.append(" and ");
//					 where.append(" ct_pu.dbilldate >= '"+beginDate+"' ");
//				 }
//				 if(StringUtils.isNotBlank(endDate)&&!"ISNULL".equals(endDate)){
//					 where.append(" and ");
//					 where.append(" ct_pu.dbilldate <= '"+endDate+"' ");
//				 }
			}
		}
	}
	
	@Override
	protected boolean isAddRowIndex() {
		return false;
	}
	private void createPkTempTables(){
		//创建合同主键和采购入库单表体主键的临时表
		DataAccessUtils querytool = new DataAccessUtils();
		StringBuffer insql = new StringBuffer("");
		insql.append(" select distinct tem_fbpuct_allct.pk_ct_pu pk_ct_pu, ic_purchasein_b.cgeneralbid  from  tem_fbpuct_allct   left join po_order_b on po_order_b.csourceid = tem_fbpuct_allct.pk_ct_pu  left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b  on ic_purchasein_b.csourcebillhid =  po_arriveorder_b.pk_arriveorder where ic_purchasein_b.cgeneralbid is not null and ic_purchasein_b.dr = 0 ");
		IRowSet is = querytool.query(insql.toString());
		String[][] inarrs = is.toTwoDimensionStringArray();
		FBPuCtRptTemptableUtils.createPkTempTab(inarrs, FBPuCtRptConstant.TEM_FBPUCT_IN);
		//创建合同主键和采购发票表体主键的临时表
		StringBuffer invoicesql = new StringBuffer("");
		invoicesql.append(" select distinct tem_fbpuct_allct.pk_ct_pu pk_ct_pu,po_invoice_b.pk_invoice_b   from  tem_fbpuct_allct  left join po_order_b on po_order_b.csourceid = tem_fbpuct_allct.pk_ct_pu  left join po_arriveorder_b   on po_arriveorder_b.csourceid = po_order_b.pk_order left join ic_purchasein_b  on ic_purchasein_b.csourcebillhid =  po_arriveorder_b.pk_arriveorder   left join po_invoice_b  on (   po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid    or po_invoice_b.csourceid = po_order_b.pk_order   ) where po_invoice_b.pk_invoice_b is not null and po_invoice_b.dr = 0 ");
		IRowSet invoiceis = querytool.query(invoicesql.toString());
		String[][] invoicearrs = invoiceis.toTwoDimensionStringArray();
		FBPuCtRptTemptableUtils.createPkTempTab(invoicearrs, FBPuCtRptConstant.TEM_FBPUCT_INVOICE);
		//创建合同主键和付款单表体主键的临时表
//		StringBuffer paysql = new StringBuffer("");
//		paysql.append(" select distinct tem_fbpuct_allct.pk_ct_pu pk_ct_pu,ap_payitem.pk_payitem   from  tem_fbpuct_allct   left join po_order_b on po_order_b.csourceid = tem_fbpuct_allct.pk_ct_pu  left join po_arriveorder_b   on po_arriveorder_b.csourceid = po_order_b.pk_order   left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid =   po_arriveorder_b.pk_arriveorder   left join po_invoice_b   on (   po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid  or po_invoice_b.csourceid = po_order_b.pk_order  )   left join ap_payableitem  on ap_payableitem.top_billid = po_invoice_b.pk_invoice  left join ap_payitem  on ap_payitem.top_billid = ap_payableitem.pk_payablebill where ap_payitem.pk_payitem  is not null and ap_payitem.dr = 0 ");
//		IRowSet payis = querytool.query(paysql.toString());
//		String[][] payarrs = payis.toTwoDimensionStringArray();
//		FBPuCtRptTemptableUtils.createPkTempTab(payarrs, FBPuCtRptConstant.TEM_FBPUCT_PAY);
	}
	/**
	 * 处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
	 * 
	 * @param dataset
	 */
	private void queryAllCt(SCMRptDataSet dataset) {
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		String sql = this.view.getViewSql();
		sql=sql.replace("distinct", "");
		String newsql = sql.substring(0, sql.indexOf("where"));
		
		StringBuffer sk_sb = new StringBuffer("");
		sk_sb.append(newsql);
		sk_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		sk_sb.append(" where  ");
		sk_sb.append(" ( ");
		// ---总包合同为收款合同，且收款合同不能同时为空----start--------------
		sk_sb.append(" ( ");
		sk_sb.append(" (  ( ct_pu.vdef5 = " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef5  and " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef5 <> '~' )");
		sk_sb.append(" or ");
		sk_sb.append(" ( ct_pu.vdef15 = " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15   and " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15 <> '~' ) ");
		
		sk_sb.append(") and (  ");
		sk_sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
		sk_sb.append(" ) ");
		sk_sb.append(" ) ");
		// ---总包合同为收款合同，且收款合同不能同时为空----end--------------
		sk_sb.append(" or ct_pu.pk_ct_pu= "	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "." + FBPuCtRptFieldConstant.PK_FBHT);
		sk_sb.append(" ) ");
		sk_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		sk_sb.append(" order by ct_pu.pk_org , ");
		
		
//		StringBuffer sk_sb = new StringBuffer("");
//		sk_sb.append(newsql);
//		sk_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
//		sk_sb.append(" where  ");
////		sk_sb.append(" ( ");
//		// ---总包合同为收款合同，且收款合同不能同时为空----start--------------
////		sk_sb.append(" ( ");
//		sk_sb.append(" ct_pu.vdef5 = " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef5   ");
//		sk_sb.append(" and ");
//		sk_sb.append(" nvl(ct_pu.vdef5,'1')!='1'  ");
//		sk_sb.append(" and ");
//		sk_sb.append(" ct_pu.vdef5 <> '~' ");
////		sk_sb.append(" and (  ");
////		sk_sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
////		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		sk_sb.append(" ) ");
////		sk_sb.append(" ) ");
//		// ---总包合同为收款合同，且收款合同不能同时为空----end--------------
////		sk_sb.append(" or ct_pu.pk_ct_pu= "	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "." + FBPuCtRptFieldConstant.PK_FBHT);
////		sk_sb.append(" ) ");
//		sk_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		
//		StringBuffer xs_sb = new StringBuffer("");
//		xs_sb.append(newsql);
//		xs_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
//		xs_sb.append(" where  ");
////		xs_sb.append(" ( ");
//		// ---总包合同为销售合同，且销售合同不能同时为空----start--------------
////		xs_sb.append(" ( ");
//		xs_sb.append("  ct_pu.vdef15 ="	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15  ");
//		sk_sb.append(" and ");
//		sk_sb.append(" nvl(ct_pu.vdef15,'1')!='1'  ");
//		sk_sb.append(" and ");
//		sk_sb.append(" ct_pu.vdef15 <> '~' ");
////		xs_sb.append(" and (  ");
////		xs_sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		xs_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
////		xs_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		xs_sb.append(" ) ");
////		xs_sb.append(" ) ");
//		// ---总包合同为销售合同，且销售合同不能同时为空----end--------------
////		xs_sb.append(" or ct_pu.pk_ct_pu= "
////				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "."
////				+ FBPuCtRptFieldConstant.PK_FBHT);
////		xs_sb.append(" ) ");
//		xs_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		
//		
//		StringBuffer ct_sb = new StringBuffer("");
//		ct_sb.append(newsql);
//		ct_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
//		ct_sb.append(" where  ");
//		ct_sb.append("  ct_pu.pk_ct_pu= "
//				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "."
//				+ FBPuCtRptFieldConstant.PK_FBHT);
//		ct_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		
//		StringBuffer sb = new StringBuffer("");
//		sb.append(sk_sb.toString());
//		sb.append(" union  ");
//		sb.append(xs_sb.toString());
//		sb.append(" union  ");
//		sb.append(ct_sb.toString());
		
		String query_sql =sk_sb.toString().replace("select ", " select distinct ");
		DataAccessUtils querytool = new DataAccessUtils();
//		throw new RuntimeException("dd");
		
		IRowSet is = querytool.query(query_sql);
		String[][] objs = is.toTwoDimensionStringArray();
		dataset.setDatas(objs);
	}
	/**
	 * 
	 * 处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
	 * 20170630  因根据合同找出总包合同后,再找出子合同,存在其他组织的,所以过滤掉
	 */
	private void queryAllCt_noother_org(SCMRptDataSet dataset) {
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		String sql = this.view.getViewSql();
		sql=sql.replace("distinct", "");
		String newsql = sql.substring(0, sql.indexOf("where"));
		
		StringBuffer sk_sb = new StringBuffer("");
		sk_sb.append(newsql);
		sk_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		sk_sb.append(" where  ");

		sk_sb.append(" ct_pu.pk_org in ( select pk_org from "	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + " )  ");
		sk_sb.append(" and ");
		sk_sb.append(" ( ");
		// ---总包合同为收款合同，且收款合同不能同时为空----start--------------
		sk_sb.append(" ( ");
		sk_sb.append(" (  ( ct_pu.vdef5 = " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef5  and " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef5 <> '~' )");
		sk_sb.append(" or ");
		sk_sb.append(" ( ct_pu.vdef15 = " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15   and " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15 <> '~' ) ");
		
		sk_sb.append(") and (  ");
		sk_sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
		sk_sb.append(" ) ");
		sk_sb.append(" ) ");
		// ---总包合同为收款合同，且收款合同不能同时为空----end--------------
		sk_sb.append(" or ct_pu.pk_ct_pu= "	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "." + FBPuCtRptFieldConstant.PK_FBHT);
		sk_sb.append(" ) ");
		sk_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		sk_sb.append(" order by ct_pu.pk_org , ");
		
		
//		StringBuffer sk_sb = new StringBuffer("");
//		sk_sb.append(newsql);
//		sk_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
//		sk_sb.append(" where  ");
////		sk_sb.append(" ( ");
//		// ---总包合同为收款合同，且收款合同不能同时为空----start--------------
////		sk_sb.append(" ( ");
//		sk_sb.append(" ct_pu.vdef5 = " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef5   ");
//		sk_sb.append(" and ");
//		sk_sb.append(" nvl(ct_pu.vdef5,'1')!='1'  ");
//		sk_sb.append(" and ");
//		sk_sb.append(" ct_pu.vdef5 <> '~' ");
////		sk_sb.append(" and (  ");
////		sk_sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
////		sk_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		sk_sb.append(" ) ");
////		sk_sb.append(" ) ");
//		// ---总包合同为收款合同，且收款合同不能同时为空----end--------------
////		sk_sb.append(" or ct_pu.pk_ct_pu= "	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "." + FBPuCtRptFieldConstant.PK_FBHT);
////		sk_sb.append(" ) ");
//		sk_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		
//		StringBuffer xs_sb = new StringBuffer("");
//		xs_sb.append(newsql);
//		xs_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
//		xs_sb.append(" where  ");
////		xs_sb.append(" ( ");
//		// ---总包合同为销售合同，且销售合同不能同时为空----start--------------
////		xs_sb.append(" ( ");
//		xs_sb.append("  ct_pu.vdef15 ="	+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15  ");
//		sk_sb.append(" and ");
//		sk_sb.append(" nvl(ct_pu.vdef15,'1')!='1'  ");
//		sk_sb.append(" and ");
//		sk_sb.append(" ct_pu.vdef15 <> '~' ");
////		xs_sb.append(" and (  ");
////		xs_sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		xs_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
////		xs_sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
////		xs_sb.append(" ) ");
////		xs_sb.append(" ) ");
//		// ---总包合同为销售合同，且销售合同不能同时为空----end--------------
////		xs_sb.append(" or ct_pu.pk_ct_pu= "
////				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "."
////				+ FBPuCtRptFieldConstant.PK_FBHT);
////		xs_sb.append(" ) ");
//		xs_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		
//		
//		StringBuffer ct_sb = new StringBuffer("");
//		ct_sb.append(newsql);
//		ct_sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
//		ct_sb.append(" where  ");
//		ct_sb.append("  ct_pu.pk_ct_pu= "
//				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "."
//				+ FBPuCtRptFieldConstant.PK_FBHT);
//		ct_sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
//		
//		StringBuffer sb = new StringBuffer("");
//		sb.append(sk_sb.toString());
//		sb.append(" union  ");
//		sb.append(xs_sb.toString());
//		sb.append(" union  ");
//		sb.append(ct_sb.toString());
		
		String query_sql =sk_sb.toString().replace("select ", " select distinct ");
		DataAccessUtils querytool = new DataAccessUtils();
//		throw new RuntimeException("dd");
		
		IRowSet is = querytool.query(query_sql);
		String[][] objs = is.toTwoDimensionStringArray();
		dataset.setDatas(objs);
	}

	/**
	 * 处理验收入库/进度金额、已收发票金额、付款金额
	 * 
	 * @param dataset
	 */
	private void processMny(SCMRptDataSet dataset,
			Map<String, FBPuCtRptVO> vomap, String data) {
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_ALLCT);
		//创建临时表
		this.createPkTempTables();
		// 处理累计
		processMnyByDateAndSuffix(vomap, null, null, "all");

		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
//		int day = c.get(Calendar.DAY_OF_MONTH);

		// Calendar c2 = Calendar.getInstance();
		// c2.set(2016, 5, 6);
		// int m=c2.get(Calendar.MONTH)+1;
		// int d=c2.get(Calendar.DAY_OF_MONTH);
		//
		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// int year1 = date.getYear();//年
		// int month1 = date.getMonth()+1;//月
		// int week1 = date.getDay(); //星期几
		// int day1 = date.getDate();//日
		// String curTime = formatter.format(date);

		// 处理截至上年末
		String date_end_last = (year) + "-01-01 00:00:00";
		processMnyByDateAndSuffix(vomap, null, date_end_last, "ly");

		// 处理本年累计
		String date_start_cur = year + "-01-01 00:00:00";
		String date_end_cur = (year + 1) + "-01-01 00:00:00";
		processMnyByDateAndSuffix(vomap, date_start_cur, date_end_cur, "");

		// 处理本年1到12月,小于等于当前月份
		for (int i = 1; i <= month; i++) {
			String date_start_ = null;
			String date_end_ = null;
			if (i < 9) {
				date_start_ = year + "-0" + i + "-01 00:00:00";
				date_end_ = year + "-0" + (i + 1) + "-01 00:00:00";
			} else if (i == 9) {
				date_start_ = year + "-09-01 00:00:00";
				date_end_ = year + "-10-01 00:00:00";
			} else if (i == 10 || i == 11) {
				date_start_ = year + "-" + i + "-01 00:00:00";
				date_end_ = year + "-" + (i + 1) + "-01 00:00:00";
			} else if (i == 12) {
				date_start_ = year + "-12-01 00:00:00";
				date_end_ = (year + 1) + "-01-01 00:00:00";
			}
			processMnyByDateAndSuffix(vomap, date_start_, date_end_,
					String.valueOf(i));
		}

		this.processMny2Dataset(dataset, vomap);
	}

	private void processMnyByDateAndSuffix(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		// 处理验收入库金额
		this.processMny_In(vomap, date_start, date_end, suffix);
		// 处理已收发票金额
		this.processMny_Invoice(vomap, date_start, date_end, suffix);
//		// 处理付款金额
//		this.processMny_Pay(vomap, date_start, date_end, suffix);
		// 处理付款金额
		this.processMny_Pay_new(vomap, date_start, date_end, suffix);
	}

	private void processMny2Dataset(SCMRptDataSet dataset,
			Map<String, FBPuCtRptVO> vomap) {
		if (vomap == null || vomap.size() <= 0) {
			return;
		}
		SCMRptColumnInfo columninfo = dataset.getColumninfo();
		Map<String, Integer> indexmap = columninfo.getColumnIndexMap();
		Object[][] arrs = dataset.getDatas();
		int num = arrs.length;
		int index_pk = indexmap.get(FBPuCtRptFieldConstant.PK_FBHT).intValue();
		for (int i = 0; i < num; i++) {
			String pk = (String) arrs[i][index_pk];
			FBPuCtRptVO vo = vomap.get(pk);
			if (vo != null) {
				for (FBPuCtRptFieldPreference field : FBPuCtRptConstant.SMART_FIELDS_NMNY) {
					String fieldname = field.getFieldname();
					int index = indexmap.get(fieldname).intValue();
					arrs[i][index] = vo.getValue(fieldname);
				}
			}
		}
	}

	/**
	 * 根据时间段和临时表数据汇总已入库金额
	 *优化前
	 * SELECT DISTINCT ct_pu.pk_ct_pu pk_ct_pu, SUM (ic_purchasein_b.nmny) nmny,
                SUM (ic_purchasein_b.ntaxmny) ntaxmny
           FROM ct_pu INNER JOIN tem_fbpuct_allct
                ON ct_pu.pk_ct_pu = tem_fbpuct_allct.pk_ct_pu
                LEFT JOIN po_order_b ON po_order_b.csourceid = ct_pu.pk_ct_pu
                LEFT JOIN po_arriveorder_b
                ON po_arriveorder_b.csourceid = po_order_b.pk_order
                LEFT JOIN ic_purchasein_b
                ON ic_purchasein_b.csourcebillhid =
                                               po_arriveorder_b.pk_arriveorder
                LEFT JOIN ic_purchasein_h
                ON ic_purchasein_h.cgeneralhid = ic_purchasein_b.cgeneralhid
          WHERE 1 = 1
            AND ic_purchasein_h.dbilldate < '2018-01-01 00:00:00'
            AND ic_purchasein_h.dbilldate >= '2017-01-01 00:00:00'
       GROUP BY ct_pu.pk_ct_pu
	 * 
	 * 优化后
	 * */
	private void processMny_In(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NINMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		/***------------------------优化前----start---------------------**/
//		sb.append(FBPuCtRptConstant.Sql_query_mny_in_1);
//		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
//				+ " on ct_pu.pk_ct_pu =");
//		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
//		sb.append(FBPuCtRptConstant.Sql_query_mny_in_3);
//		sb.append(" where 1=1 ");
//		String date = " ic_purchasein_h.dbilldate ";
//		if (StringUtils.isNotBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and  " + date + " < '" + date_end + "' and " + date
//					+ " >= '" + date_start + "' ");
//		} else if (StringUtils.isBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and " + date + "  < '" + date_end + "' ");
//		}
//		sb.append(FBPuCtRptConstant.Sql_group_by);
		/***------------------------优化前----end---------------------**/		

		/***------------------------优化后----start---------------------**/
		sb.append(" select distinct tem_fbpuct_in.pk_ct_pu pk_ct_pu, sum(ic_purchasein_b.nmny) nmny, sum(ic_purchasein_b.ntaxmny) ntaxmny  from tem_fbpuct_in  inner join ic_purchasein_b on ic_purchasein_b.cgeneralbid = tem_fbpuct_in.bid  inner join ic_purchasein_h  on ic_purchasein_h.cgeneralhid = ic_purchasein_b.cgeneralhid ");
		sb.append(" where 1=1 and ic_purchasein_b.dr =0 and ic_purchasein_h.dr = 0 ");
		String date = " ic_purchasein_h.dbilldate ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(" group by tem_fbpuct_in.pk_ct_pu ");		
		/***------------------------优化后----start---------------------**/
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				String inmny_all = arr[1];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(inmny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, inmny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, inmny_all);
				}
			}
		}
	}

	/**
	 * 根据时间段和临时表数据汇总已收发票金额
	 * 
	 * 优化前
	 * SELECT DISTINCT ct_pu.pk_ct_pu pk_ct_pu, SUM (po_invoice_b.nmny) nmny,
                SUM (po_invoice_b.ntaxmny) ntaxmny
           FROM ct_pu INNER JOIN tem_fbpuct_allct
                ON ct_pu.pk_ct_pu = tem_fbpuct_allct.pk_ct_pu
                LEFT JOIN po_order_b ON po_order_b.csourceid = ct_pu.pk_ct_pu
                LEFT JOIN po_arriveorder_b
                ON po_arriveorder_b.csourceid = po_order_b.pk_order
                LEFT JOIN ic_purchasein_b
                ON ic_purchasein_b.csourcebillhid =
                                               po_arriveorder_b.pk_arriveorder
                LEFT JOIN po_invoice_b
                ON (   po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid
                    OR po_invoice_b.csourceid = po_order_b.pk_order
                   )
                LEFT JOIN po_invoice
                ON po_invoice.pk_invoice = po_invoice_b.pk_invoice
          WHERE 1 = 1
            AND po_invoice.dbilldate < '2018-01-01 00:00:00'
            AND po_invoice.dbilldate >= '2017-01-01 00:00:00'
       GROUP BY ct_pu.pk_ct_pu
	 * 
	 * 优化后
	 * 
	 * */
	private void processMny_Invoice(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NINVOICEMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		/***------------------------优化前----start---------------------**/
//		sb.append(FBPuCtRptConstant.Sql_query_mny_invoice_1);
//		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
//				+ " on ct_pu.pk_ct_pu =");
//		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
//		sb.append(FBPuCtRptConstant.Sql_query_mny_invoice_3);
//		sb.append(" where 1=1 ");
//		String date = " po_invoice.dbilldate ";
//		if (StringUtils.isNotBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and  " + date + " < '" + date_end + "' and " + date
//					+ " >= '" + date_start + "' ");
//		} else if (StringUtils.isBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and " + date + "  < '" + date_end + "' ");
//		}
//		sb.append(FBPuCtRptConstant.Sql_group_by);
		/***------------------------优化前----end---------------------**/	
		
		/***------------------------优化后----start---------------------**/	
		sb.append(" select distinct tem_fbpuct_invoice.pk_ct_pu pk_ct_pu, sum(po_invoice_b.nmny) nmny,  sum(po_invoice_b.ntaxmny) ntaxmny  from tem_fbpuct_invoice  inner join po_invoice_b    on    po_invoice_b.pk_invoice_b = tem_fbpuct_invoice.bid  inner  join po_invoice   on po_invoice.pk_invoice = po_invoice_b.pk_invoice ");
		sb.append(" where 1=1 and po_invoice_b.dr =0 and po_invoice.dr = 0 ");
		String date = " po_invoice.dbilldate ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(" group by tem_fbpuct_invoice.pk_ct_pu ");	
		/***------------------------优化后----start---------------------**/
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				//这个表已开发票金额应该取含税金额，现在取的未税金额
//				String invoicemny_all = arr[1];
				String invoicemny_all = arr[2];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(invoicemny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, invoicemny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, invoicemny_all);
				}
			}
		}
	}

	/**
	 * 根据时间段和临时表数据汇总付款金额
	 * 
	 * 优化前
	 * SELECT DISTINCT ct_pu.pk_ct_pu pk_ct_pu, SUM (ap_payitem.local_notax_de) nmny,
                SUM (ap_payitem.local_money_de) ntaxmny
           FROM ct_pu INNER JOIN tem_fbpuct_allct
                ON ct_pu.pk_ct_pu = tem_fbpuct_allct.pk_ct_pu
                LEFT JOIN po_order_b ON po_order_b.csourceid = ct_pu.pk_ct_pu
                LEFT JOIN po_arriveorder_b
                ON po_arriveorder_b.csourceid = po_order_b.pk_order
                LEFT JOIN ic_purchasein_b
                ON ic_purchasein_b.csourcebillhid =
                                               po_arriveorder_b.pk_arriveorder
                LEFT JOIN po_invoice_b
                ON (   po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid
                    OR po_invoice_b.csourceid = po_order_b.pk_order
                   )
                LEFT JOIN ap_payableitem
                ON ap_payableitem.top_billid = po_invoice_b.pk_invoice
                LEFT JOIN ap_payitem
                ON ap_payitem.top_billid = ap_payableitem.pk_payablebill
                LEFT JOIN ap_paybill
                ON ap_paybill.pk_paybill = ap_payitem.pk_paybill
          WHERE 1 = 1
            AND ap_paybill.billdate < '2018-01-01 00:00:00'
            AND ap_paybill.billdate >= '2017-01-01 00:00:00'
       GROUP BY ct_pu.pk_ct_pu
	 * 
	 * 优化后
	 * 
	 * */
	private void processMny_Pay(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NPAYMENTMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		/***------------------------优化前----start---------------------**/
//		sb.append(FBPuCtRptConstant.Sql_query_mny_pay_1);
//		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
//				+ " on ct_pu.pk_ct_pu =");
//		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
//		sb.append(FBPuCtRptConstant.Sql_query_mny_pay_3);
//		sb.append(" where 1=1 ");
//		String date = " ap_paybill.billdate ";
//		if (StringUtils.isNotBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and  " + date + " < '" + date_end + "' and " + date
//					+ " >= '" + date_start + "' ");
//		} else if (StringUtils.isBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and " + date + "  < '" + date_end + "' ");
//		}
//		sb.append(FBPuCtRptConstant.Sql_group_by);
		/***------------------------优化前----end---------------------**/		

		/***------------------------优化后----start---------------------**/
		sb.append(" select distinct tem_fbpuct_pay.pk_ct_pu pk_ct_pu, sum(ap_payitem.local_notax_de) nmny, sum(ap_payitem.local_money_de) ntaxmny  from tem_fbpuct_pay  inner join ap_payitem  on ap_payitem.pk_payitem = tem_fbpuct_pay.bid  inner join ap_paybill   on ap_paybill.pk_paybill = ap_payitem.pk_paybill ");
		sb.append(" where 1=1 and ap_payitem.dr = 0 and ap_paybill.dr = 0 ");
		String date = " ap_paybill.billdate ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(" group by tem_fbpuct_pay.pk_ct_pu ");
		/***------------------------优化后----start---------------------**/
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				String paymentmny_all = arr[1];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(paymentmny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, paymentmny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, paymentmny_all);
				}
			}
		}
	}

	@Override
	protected SCMRptDataSet process(SCMRptDataSet dataset)
			throws BusinessException {
		if (!this.getScmQueryCondition().isHaveCondition()
				|| dataset.getCount() <= 0) {
			return dataset;
		}
		// 处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
		// 关于合同的过滤条件都在这处理
//		this.queryAllCt(dataset);
		this.queryAllCt_noother_org(dataset);
		Map<String, FBPuCtRptVO> vomap = new HashMap<String, FBPuCtRptVO>();
		// 处理累计的数据
		 this.processMny(dataset, vomap, null);
		
		//处理组织名称、项目名称、合同名称等内容
		processShowInfo(dataset);
		return dataset;
	}
	private void processShowInfo(SCMRptDataSet dataset){
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_FINAL);
		//处理组织名称
		Map<String,String> orgnamemap = new HashMap<String,String>();
		StringBuffer orgsql = new StringBuffer("");
		orgsql.append(" select distinct org_purchaseorg.name ,"+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_org from org_purchaseorg , "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" where org_purchaseorg.pk_purchaseorg = "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_org   ") ;
		DataAccessUtils tool = new DataAccessUtils();
		IRowSet orgrs = tool.query(orgsql.toString());
		if(orgrs!=null&&orgrs.size()>0){
			String[][] arr = orgrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				orgnamemap.put(ar[1],ar[0]);
			}
		}
		
		//处理项目编码和名称、合同名称
		Map<String,String> projectnamemap = new HashMap<String,String>();
		Map<String,String> projectcodemap = new HashMap<String,String>();
		StringBuffer projectsql = new StringBuffer("");
		projectsql.append(" select distinct bd_project.project_name ,bd_project.project_code,tem_fbpuct_final.pk_ct_pu from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,ct_pu_b,bd_project   where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu = ct_pu_b.pk_ct_pu and ct_pu_b.cbprojectid = bd_project.pk_project  ") ;
		IRowSet projectrs = tool.query(projectsql.toString());
		if(projectrs!=null&&projectrs.size()>0){
			String[][] arr = projectrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				projectnamemap.put(ar[2],ar[0]);
				projectcodemap.put(ar[2],ar[1]);
			}
		}
		//select distinct ct_pu.ctname,ct_pu.pk_ct_pu from tem_fbpuct_final ,ct_pu,ct_pu_b   where tem_fbpuct_final.pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.pk_ct_pu = tem_fbpuct_final.pk_ct_pu
		//处理合同名称
		Map<String,String> ctmap = new HashMap<String,String>();
		Map<String,String> versionmap = new HashMap<String,String>();
		StringBuffer ctsql = new StringBuffer("");
		ctsql.append(" select distinct  ct_pu.ctname,ct_pu.pk_ct_pu ,ct_pu.version from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,ct_pu,ct_pu_b   where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.pk_ct_pu = "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu  ") ;
		IRowSet ctrs = tool.query(ctsql.toString());
		if(ctrs!=null&&ctrs.size()>0){
			String[][] arr = ctrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				ctmap.put(ar[1],ar[0]);
				if(ar[2]!=null&&Integer.valueOf(ar[2]).intValue()>1){
					versionmap.put(ar[1],"是");
				}else{
					versionmap.put(ar[1],"否");
				}
			}
		}
		//处理合同成本
		Map<String,String> ctcbmap = new HashMap<String,String>();
		StringBuffer ctcbsql = new StringBuffer("");
		ctcbsql.append("  select distinct sum(ct_pu_b.norigmny) mny ,ct_pu_b.pk_ct_pu  from  "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+",ct_pu_b where   ct_pu_b.dr = 0 and ct_pu_b.pk_ct_pu = "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu group by ct_pu_b.pk_ct_pu ") ;
		IRowSet ctcbrs = tool.query(ctcbsql.toString());
		if(ctcbrs!=null&&ctcbrs.size()>0){
			String[][] arr = ctcbrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				ctcbmap.put(ar[1],ar[0]);
			}
		}
		
		//最终验收/完工日期
		Map<String,String> ctysdatemap = new HashMap<String,String>();
		StringBuffer ctysdatesql = new StringBuffer("");
		ctysdatesql.append(" select  distinct max(ic_purchasein_h.dbilldate) , tem_fbpuct_final.pk_ct_pu  from tem_fbpuct_final  left join po_order_b  on po_order_b.csourceid   = tem_fbpuct_final.pk_ct_pu  left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order         left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join ic_purchasein_h on ic_purchasein_h.cgeneralhid = ic_purchasein_b.cgeneralhid  group by tem_fbpuct_final.pk_ct_pu ") ;
		IRowSet ctysdaters = tool.query(ctysdatesql.toString());
		if(ctysdaters!=null&&ctysdaters.size()>0){
			String[][] arr = ctysdaters.toTwoDimensionStringArray();
			for(String[] ar :arr){
				ctysdatemap.put(ar[1],ar[0]);
			}
		}
		
		//处理供应商名称
		Map<String,String> supmap = new HashMap<String,String>();
		StringBuffer supsql = new StringBuffer("");
		supsql.append(" select distinct bd_supplier.name,"+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,bd_supplier   where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".cvendorid = bd_supplier.pk_supplier  ") ;
		IRowSet suprs = tool.query(supsql.toString());
		if(suprs!=null&&suprs.size()>0){
			String[][] arr = suprs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				supmap.put(ar[1],ar[0]);
			}
		}
		
		//处理条款约定ct_pu_term.vtermcontent、合同内容vdef19、合同类别vdef20
		Map<String,String> ht_tiaokuan_map = new HashMap<String,String>();
		Map<String,String> ht_neirong_map = new HashMap<String,String>();
		StringBuffer ht_sql = new StringBuffer("");
		ht_sql.append(" select distinct "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu , ct_pu.vdef19, ct_pu.vdef20, ct_pu_term.vtermcontent  from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,ct_pu ,ct_pu_term  where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu = ct_pu.pk_ct_pu and ct_pu.pk_ct_pu = ct_pu_term.pk_ct_pu and ct_pu_term.dr = 0 ") ;
		IRowSet ht_rs = tool.query(ht_sql.toString());
		if(ht_rs!=null&&ht_rs.size()>0){
			String[][] arr = ht_rs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				String pk_ct_pu = ar[0] ;
				ht_neirong_map.put(pk_ct_pu,ar[1]);
				if(ht_tiaokuan_map.containsKey(pk_ct_pu)){
					StringBuffer sb = new StringBuffer(ht_tiaokuan_map.get(pk_ct_pu));
					sb.append(ar[3]);
					String tk = sb.toString();
					if(StringUtils.isNotBlank(tk)&&tk.length()>255){
						ht_tiaokuan_map.put(pk_ct_pu, tk.substring(0,255));
					}else{
						ht_tiaokuan_map.put(pk_ct_pu, tk);
					}
				}else{
					String tk = ar[3];
					if(StringUtils.isNotBlank(tk)&&tk.length()>255){
						ht_tiaokuan_map.put(pk_ct_pu, tk.substring(0,255));
					}else{
						ht_tiaokuan_map.put(pk_ct_pu, tk);
					}
				}
			}
		}

		//处理总包合同名称:收款合同或销售合同,合同类别vdef20
		Map<String,String> xsnamemap = new HashMap<String,String>();
		//销售合同类别
		Map<String,String> xsht_leibie_map = new HashMap<String,String>();
		StringBuffer xsnamesql = new StringBuffer("");
		xsnamesql.append(" select distinct ct_sale.ctname,"+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu,bd_billtype.billtypename from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,ct_sale, bd_billtype  where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".vdef15 = ct_sale.vbillcode and bd_billtype.pk_billtypeid = ct_sale.ctrantypeid and ct_sale.dr=0   ") ;
		IRowSet xsnamers = tool.query(xsnamesql.toString());
		if(xsnamers!=null&&xsnamers.size()>0){
			String[][] arr = xsnamers.toTwoDimensionStringArray();
			for(String[] ar :arr){
				xsnamemap.put(ar[1],ar[0]);
				xsht_leibie_map.put(ar[1], ar[2]);
			}
		}
		Map<String,String> sknamemap = new HashMap<String,String>();
		//收款合同类别
		Map<String,String> skht_leibie_map = new HashMap<String,String>();
		StringBuffer sknamesql = new StringBuffer("");
		sknamesql.append(" select distinct fct_ar.ctname,"+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu,bd_billtype.billtypename  from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,fct_ar , bd_billtype  where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".vdef5 = fct_ar.vbillcode and bd_billtype.pk_billtypeid=fct_ar.ctrantypeid  and fct_ar.dr =0  ") ;
		IRowSet sknamers = tool.query(sknamesql.toString());
		if(sknamers!=null&&sknamers.size()>0){
			String[][] arr = sknamers.toTwoDimensionStringArray();
			for(String[] ar :arr){
				sknamemap.put(ar[1],ar[0]);
				skht_leibie_map.put(ar[1], ar[2]);
			}
		}
		
		SCMRptColumnInfo columninfo = dataset.getColumninfo();
		Map<String, Integer> indexmap = columninfo.getColumnIndexMap();
		Object[][] arrs = dataset.getDatas();
		int num = arrs.length;
		int index_pk = indexmap.get(FBPuCtRptFieldConstant.PK_FBHT).intValue();
		
		int index_pk_org = indexmap.get(FBPuCtRptFieldConstant.CTBZT).intValue();
		//采购发票头id
		int index_org_name = indexmap.get(FBPuCtRptFieldConstant.PK_HID_FP).intValue();
		//暂时用入库单的pk来当项目名称
		int index_project_code = indexmap.get(FBPuCtRptFieldConstant.PK_HID_IN).intValue();
		int index_project_name = indexmap.get(FBPuCtRptFieldConstant.CPROJECTID).intValue();
		//暂时用入库单的bpk来当合同名称
		int index_ctname = indexmap.get(FBPuCtRptFieldConstant.PK_BID_IN).intValue();
		//
		int index_version = indexmap.get(FBPuCtRptFieldConstant.BCHANGE).intValue();
		int index_htcb = indexmap.get(FBPuCtRptFieldConstant.NHTCB).intValue();
		int index_supply = indexmap.get(FBPuCtRptFieldConstant.CVENDORID).intValue();
		//最终验收/完工日期
		int index_ctysdate = indexmap.get(FBPuCtRptFieldConstant.DACCEPTANCECHECK).intValue();
		
		//总包合同编码
		int index_zbct_code_xs = indexmap.get(FBPuCtRptFieldConstant.PK_ZBHT_CODE_XS).intValue();
		int index_zbct_code_sk = indexmap.get(FBPuCtRptFieldConstant.PK_ZBHT_CODE_SK).intValue();
		
		int index_zbct_name_xs = indexmap.get(FBPuCtRptFieldConstant.PK_ZBHT_XS).intValue();
		//合同类别
		int index_ht_leibie = indexmap.get(FBPuCtRptFieldConstant.CHTTYPE).intValue();
		
		//计算合同进度
		int index_ninmny_all = indexmap.get(FBPuCtRptFieldConstant.NINMNY_ALL).intValue();
		int index_nhtcb = indexmap.get(FBPuCtRptFieldConstant.NHTCB).intValue();
		//进度
		int index_nrate_all = indexmap.get(FBPuCtRptFieldConstant.NRATE_ALL).intValue();
		
		//合同内容vdef19
		int index_ht_context = indexmap.get(FBPuCtRptFieldConstant.CHTCONTENT).intValue();
		
		//合同条款vdef20
		int index_ht_tiaokuan = indexmap.get(FBPuCtRptFieldConstant.CAGREEMENT).intValue();
		
		
		for (int i = 0; i < num; i++) {
			String pk = (String) arrs[i][index_pk];
			String pk_org = (String) arrs[i][index_pk_org];
			arrs[i][index_ht_context] = ht_neirong_map.get(pk);
			String httk = ht_tiaokuan_map.get(pk);
			if(StringUtils.isNotBlank(httk)&&httk.length()>100){
				arrs[i][index_ht_tiaokuan] = httk.substring(0, 100);
			}else{
				arrs[i][index_ht_tiaokuan] = httk;
			}
			arrs[i][index_org_name] = orgnamemap.get(pk_org);
			arrs[i][index_project_code] = projectcodemap.get(pk);
			arrs[i][index_project_name] = projectnamemap.get(pk);
			arrs[i][index_ctname] = ctmap.get(pk);
			arrs[i][index_version] = versionmap.get(pk);
			arrs[i][index_htcb] = ctcbmap.get(pk);
			arrs[i][index_supply] = supmap.get(pk);
			arrs[i][index_ctysdate] = ctysdatemap.get(pk);
			String zbctcode = (String)arrs[i][index_zbct_code_xs];
			if(StringUtils.isBlank(zbctcode)){
				arrs[i][index_zbct_code_xs]=arrs[i][index_zbct_code_sk];
				arrs[i][index_zbct_name_xs]=sknamemap.get(pk);
				arrs[i][index_ht_leibie]=skht_leibie_map.get(pk);
			}else{
				arrs[i][index_zbct_name_xs]=xsnamemap.get(pk);
				arrs[i][index_ht_leibie]=xsht_leibie_map.get(pk);
			}
			String ninmny_all = (String)arrs[i][index_ninmny_all];
			String nhtcb = (String)arrs[i][index_nhtcb];
			if(StringUtils.isNotBlank(ninmny_all)&&StringUtils.isNotBlank(nhtcb)){
				arrs[i][index_nrate_all]=(Object)new UFDouble(ninmny_all).div(new UFDouble(nhtcb)).multiply(100).toString();
			}else{
				arrs[i][index_nrate_all]=null;
			}
		}
	}

	@Override
	protected SCMRptAbsScalePrcStrategy getScalePrcStrategy() {
		return null;
	}
	

	


	/**
	 * 根据时间段和临时表数据汇总付款金额
	 * 
	 * 优化前
	 * SELECT DISTINCT ct_pu.pk_ct_pu pk_ct_pu, SUM (ap_payitem.local_notax_de) nmny,
                SUM (ap_payitem.local_money_de) ntaxmny
           FROM ct_pu INNER JOIN tem_fbpuct_allct
                ON ct_pu.pk_ct_pu = tem_fbpuct_allct.pk_ct_pu
                LEFT JOIN po_order_b ON po_order_b.csourceid = ct_pu.pk_ct_pu
                LEFT JOIN po_arriveorder_b
                ON po_arriveorder_b.csourceid = po_order_b.pk_order
                LEFT JOIN ic_purchasein_b
                ON ic_purchasein_b.csourcebillhid =
                                               po_arriveorder_b.pk_arriveorder
                LEFT JOIN po_invoice_b
                ON (   po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid
                    OR po_invoice_b.csourceid = po_order_b.pk_order
                   )
                LEFT JOIN ap_payableitem
                ON ap_payableitem.top_billid = po_invoice_b.pk_invoice
                LEFT JOIN ap_payitem
                ON ap_payitem.top_billid = ap_payableitem.pk_payablebill
                LEFT JOIN ap_paybill
                ON ap_paybill.pk_paybill = ap_payitem.pk_paybill
          WHERE 1 = 1
            AND ap_paybill.billdate < '2018-01-01 00:00:00'
            AND ap_paybill.billdate >= '2017-01-01 00:00:00'
       GROUP BY ct_pu.pk_ct_pu
	 * 
	 * 优化后
	 * 
	 * */
	private void processMny_Pay_new(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NPAYMENTMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		/***------------------------优化前----start---------------------**/
//		sb.append(FBPuCtRptConstant.Sql_query_mny_pay_1);
//		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
//				+ " on ct_pu.pk_ct_pu =");
//		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
//		sb.append(FBPuCtRptConstant.Sql_query_mny_pay_3);
//		sb.append(" where 1=1 ");
//		String date = " ap_paybill.billdate ";
//		if (StringUtils.isNotBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and  " + date + " < '" + date_end + "' and " + date
//					+ " >= '" + date_start + "' ");
//		} else if (StringUtils.isBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and " + date + "  < '" + date_end + "' ");
//		}
//		sb.append(FBPuCtRptConstant.Sql_group_by);
		/***------------------------优化前----end---------------------**/		

		/***------------------------优化后----start---------------------**/
		


		
//		//sb.append(" select distinct tem_fbpuct_pay.pk_ct_pu pk_ct_pu, sum(ap_payitem.local_notax_de) nmny, sum(ap_payitem.local_money_de) ntaxmny  from tem_fbpuct_pay  inner join ap_payitem  on ap_payitem.pk_payitem = tem_fbpuct_pay.bid  inner join ap_paybill   on ap_paybill.pk_paybill = ap_payitem.pk_paybill ");
//		sb.append(" select distinct tem_fbpuct_pay.pk_ct_pu pk_ct_pu, sum(ap_payitem.local_notax_de) nmny, sum(ap_payitem.local_money_de) ntaxmny  FROM  tem_fbpuct_allct tem_fbpuct_pay INNER JOIN ct_pu ON ct_pu.pk_ct_pu = tem_fbpuct_pay.pk_ct_pu INNER JOIN ap_payitem ON ap_payitem.contractno = ct_pu.vbillcode  INNER JOIN ap_paybill ON ap_paybill.pk_paybill = ap_payitem.pk_paybill  ");
//		sb.append(" where 1=1 and ct_pu.dr =0 and ap_payitem.dr = 0 and ap_paybill.dr = 0 ");
//		String date = " ap_paybill.billdate ";
//		if (StringUtils.isNotBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and  " + date + " < '" + date_end + "' and " + date
//					+ " >= '" + date_start + "' ");
//		} else if (StringUtils.isBlank(date_start)
//				&& StringUtils.isNotBlank(date_end)) {
//			sb.append(" and " + date + "  < '" + date_end + "' ");
//		}
//		sb.append(" group by tem_fbpuct_pay.pk_ct_pu ");
//		/***------------------------优化后----start---------------------**/
//		DataAccessUtils querytool = new DataAccessUtils();
//		IRowSet rs = querytool.query(sb.toString());
//		if (rs != null && rs.size() > 0) {
//			String[][] objs = rs.toTwoDimensionStringArray();
//			for (String[] arr : objs) {
//				String paymentmny_all = arr[1];
//				String pk_ct_pu = arr[0];
//				if (StringUtils.isBlank(paymentmny_all)) {
//					continue;
//				}
//				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
//				if (vo == null) {
//					vo = new FBPuCtRptVO();
//					vo.setValue(fieldname, paymentmny_all);
//					vomap.put(pk_ct_pu, vo);
//				} else {
//					vo.setValue(fieldname, paymentmny_all);
//				}
//			}
//		}
//		
//		
		/***------------------------优化后----start---------------------**/
		
		
		/***------------------------付款金额不再从ap_payitem里去,从ct_payplan中取----start---------------------**/
		
		sb.append(" SELECT DISTINCT tem_fbpuct_allct.pk_ct_pu pk_ct_pu, SUM (ct_payplan.naccumpayorgmny) nmny,  SUM (ct_payplan.naccumpaymny) ntaxmny");
		sb.append(" FROM tem_fbpuct_allct tem_fbpuct_allct INNER JOIN ct_pu  ON ct_pu.pk_ct_pu = tem_fbpuct_allct.pk_ct_pu ");
		sb.append("      INNER JOIN ct_payplan ON ct_payplan.pk_ct_pu =  ct_pu.pk_ct_pu           ");     
		sb.append(" WHERE 1 = 1");
		sb.append(" AND ct_pu.dr = 0");
		sb.append(" AND ct_payplan.dr = 0");
		
		String date = " ct_payplan.ts ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(" group by tem_fbpuct_allct.pk_ct_pu ");
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				String paymentmny_all = arr[1];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(paymentmny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, paymentmny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, paymentmny_all);
				}
			}
		}
	}

}
