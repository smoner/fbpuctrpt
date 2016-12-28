package nc.scm.puct.report.tmplate;

import java.util.ArrayList;
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
import nc.scmmm.vo.scmpub.report.entity.SCMRptDataSet;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMField;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMProviderMetaData;
import nc.scmmm.vo.scmpub.report.viewfactory.define.SCMView;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.data.IRowSet;

import org.apache.commons.lang.StringUtils;

public class FBPuCtRptTemplate  extends SimpleAbsRptDataSetTemplet{
		private FBPuCtView view =null;  
		// 来自查询对话框的查询条件vo
	  public List<ConditionVO> condsFromQryDlgList;
	
	  private void addHeadFields(SCMProviderMetaData metaData) {
		    this.createFields(metaData,  FBPuCtRptConstant.SMART_FIELDS_ZBCT);
		    this.createFields(metaData,  FBPuCtRptConstant.SMART_FIELDS_ADD);
		    this.createFields(metaData,  FBPuCtRptConstant.SMART_FIELDS_FBCT);
		    this.createFields(metaData,  FBPuCtRptConstant.SMART_FIELDS_NMNY);
	  	}
	  private void createFields(SCMProviderMetaData metaData,
			  FBPuCtRptFieldPreference[] fields) {
		    for (FBPuCtRptFieldPreference field : fields) {
		      SCMField scmfiled =
		          SCMProviderMetaUtil.getDynamicField(field.getFieldname(),
		              field.getFieldChnName(), field.getFieldType());
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
	    	FBPuCtView initView =
	            new FBPuCtView(this.getScmReportContext());
	        initView.initDetails();
	        return initView;
	      }
//
//	      this.tranMap = this.getSCMReportTransMap();
//	      this.timezone = this.tranMap.getLocalTimeZone();
//	      if (null != this.tranMap.get(StockExecConstant.LINKQUERY)) {
//	        this.isLinkQuery = UFBoolean.TRUE;
//	      }
//	      // 为后面的期间临时表设置开始结束时间
//	      this.handlerAnalyDate(this.tranMap);
//	      // 单据来源
//	      this.billsrc = this.getBillSrc();
//
//	      if (this.billsrc == ExecPriceBillSrcEnum.POORDER) {
//	        this.headalias = ExecPriceConstant.POORDER_H;
//	        this.bodyalias = ExecPriceConstant.POORDER_B;
//	      }
//	      else {
//	        this.headalias = ExecPriceConstant.POINVOICE_H;
//	        this.bodyalias = ExecPriceConstant.POINVOICE_B;
//	      }
//	      this.view =
//	          new ExecPriceRptView(this.getScmReportContext(), this.billsrc, true);
	    this.view =
	    		new FBPuCtView(this.getScmReportContext());
	    this.view.initDetails();
	    this.processWhere();
//
//	      this.view.initSelectDetails(this.headalias, this.bodyalias, this.pricetype);
//
//	      String patchGroup =
//	          this.bodyalias + "." + ExecPriceDispData.PK_SRCMATERIAL + " , "
//	              + this.headalias + "." + ExecPriceDispData.CORIGCURRENCYID;
//	      this.view.setPatchGroup(patchGroup);
//	      this.processInnerJoin();
//	      return this.view;
		return this.view;
	    }

	  private void processWhere() {
	    ConditionVO[] generalConds = this.getScmQueryCondition().getAllQueryConditions();
	    StringBuilder where = new StringBuilder();
	    this.transQryConds(generalConds, where);
	    String sql =
	        new ConditionVO().getSQLStr(this.condsFromQryDlgList
	            .toArray(new ConditionVO[this.condsFromQryDlgList.size()]));
	    if (StringUtils.isNotBlank(sql)) {
	      where.append(" and ");
	      where.append(sql);
	    }
	    // 设置默认的查询条件
//	    where.append(this.getDefaultWhere());
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
		      // 采购合同编码
		      else if (condvo.getFieldCode().equals(CtPuVO.VBILLCODE)) {
		        condvo.setFieldCode("ct_pu.vbillcode");
		        this.condsFromQryDlgList.add(condvo);
		      }
		      // 采购合同名称
		      else if (condvo.getFieldCode().equals(CtPuVO.CTNAME)) {
		        if (condvo.getOperaCode().equals("=")) {
		          where.append(" and ct_pu.ctname = '"
		              + condvo.getValue() + "'  ");
		        }
		        else if (condvo.getOperaCode().contains("left like")) {
		          where.append(" and  ct_pu.cname left like  '"
		              + condvo.getValue() + "%' ");
		        }
		      }
//		      // 总包合同签订日期
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.MATERIALCODE)) {
//		        condvo.setFieldCode(this.bodyalias + "."
//		            + ExecPriceDispData.PK_MATERIAL);
//		        this.condsFromQryDlgList.add(condvo);
//		      }
//		      // 
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.MATERIALNAME)) {
//		        if (condvo.getOperaCode().equals("in")) {
//		          where.append(" and bd_material.name in " + condvo.getValue() + "");
//		        }
//		        else if (condvo.getOperaCode().equals("=")) {
//		          where.append(" and bd_material.name = '"
//		              + SCMESAPI.sqlEncodeGeneral(condvo.getValue()) + "' ");
//		        }
//		        else if (condvo.getOperaCode().contains("left like")) {
//		          where.append(" and bd_material.name like '"
//		              + SCMESAPI.sqlEncodeGeneral(condvo.getValue()) + "%' ");
//		        }
//		        continue;
//		      }
//		      // 分析区间:非其他
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.ANALYAREA)
//		          && !condvo.getValue().equals(
//		              String.valueOf(ExecPricePeriodTypeEnum.OTHER))) {
//		        this.analyArea = Integer.valueOf(condvo.getValue()).intValue();
//		      } // 分析区间：其他
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.ANALYAREA)
//		          && condvo.getValue().equals(
//		              String.valueOf(ExecPricePeriodTypeEnum.OTHER))) {
//		        for (ConditionVO innerConVO : generalConds) {
//		          if (innerConVO.getFieldCode().equals(ExecPriceQueryConst.DAYS)) {
//		            this.analyArea =
//		                Integer.valueOf("33" + innerConVO.getValue()).intValue();
//		          }
//		        }
//		      }
//		      // 天数
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.DAYS)
//		          && condvo.getValue().equals(
//		              String.valueOf(ExecPricePeriodTypeEnum.OTHER))) {
//		      }
//		      // 价格取值
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.PRICETYPE)) {
//		        this.pricetype = Integer.valueOf(condvo.getValue()).intValue();
//		        // // 含税, 原币价税合计非负
//		        // if (this.pricetype == StockExecPriceTypeEnum.TAX) {
//		        // where.append("and not( coalesce(" + this.bodyalias + "."
//		        // + StockExecConstant.NORIGTAXMNY + ",0) < 0) ");
//		        // }
//		        // // 不含税, 原币无税金额非负
//		        // else if (this.pricetype == StockExecPriceTypeEnum.UNTAX) {
//		        // where.append("and not( coalesce(" + this.bodyalias + "."
//		        // + StockExecConstant.NORIGMNY + ",0) < 0) ");
//		        // }
//		      }
//
		      // 单据日期
		      else if (condvo.getFieldCode().equals(CtPuVO.DBILLDATE)) {
//		        String[] dates = condvo.getValue().split(",");
//		        this.beginDate = new UFDate(dates[0]);
//		        this.endDate = new UFDate(dates[1]);
////
//		        String filedcode = condvo.getFieldCode();
////		        condvo.setFieldCode(this.headalias + "." + filedcode);
		        this.condsFromQryDlgList.add(condvo);
		      }
		      // 总包合同编码
		      else if (condvo.getFieldCode().equals("zbvbillcode")) {
		        if (condvo.getOperaCode().equals("=")) {
			          where.append(" and ( ct_pu.vdef5 = '"
			              + condvo.getValue() + "' or ct_pu.vdef15 = '"
			              + condvo.getValue() + "' ) ");
			        } 
			        else if (condvo.getOperaCode().contains("left like")) {
			          where.append(" and ( ct_pu.vdef5 left like '"
				              + condvo.getValue() + "%' or ct_pu.vdef15 left like  '"
				              + condvo.getValue() + "%' ) ");
				        }
			      }
		      // 采购合同签订日期
		      else if (condvo.getFieldCode().equals("fbctqddate")) {
		        condvo.setFieldCode("ct_pu." + CtPuVO.SUBSCRIBEDATE);
		        this.condsFromQryDlgList.add(condvo);
		      }
//		      // 表头：采购组织、采购部门、业务流程、币种、单据日期
//		      else if (condvo.getFieldCode().equals(ExecPriceQueryConst.PK_ORG)
//		          || condvo.getFieldCode().equals(ExecPriceQueryConst.PK_DEPT)
//		          || condvo.getFieldCode().equals(ExecPriceQueryConst.PK_BUSITYPE)
//		          || condvo.getFieldCode().equals(ExecPriceQueryConst.CORIGCURRENCYID)) {
//		        String filedcode = condvo.getFieldCode();
//		        condvo.setFieldCode(this.headalias + "." + filedcode);
//		        this.condsFromQryDlgList.add(condvo);
//		      }
//		      // 供应商
		      else if (condvo.getFieldCode().equals(CtPuVO.CVENDORID)) {
//		        String filedcode = condvo.getFieldCode();
//		        condvo.setFieldCode(this.bodyalias + "." + filedcode);
		        this.condsFromQryDlgList.add(condvo);
//		      }
		    }
		    }
		  }
	  
	  
	  
	@Override
	protected boolean isAddRowIndex() {
		return false;
	}

	/**
	 * 处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
	 * @param dataset
	 */
	private void queryAllCt(SCMRptDataSet dataset){
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(), FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		String sql = this.view.getViewSql();
		String newsql = sql.substring(0, sql.indexOf("where"));
		StringBuffer sb = new StringBuffer("");
		sb.append(newsql);
		sb.append(" , "+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		sb.append(" where  ");
		sb.append(" ( ");
		
		//---总包合同为销售合同或者收款合同，且销售合同或收款合同不能同时为空----start--------------
		sb.append(" ( ");
		sb.append(" ( ct_pu.vdef5 = "+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS+".vdef5 or ct_pu.vdef15 ="+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS+".vdef15 ) " );
			sb.append(" and (  ");
			sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
			sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
			sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
			sb.append(" ) ");
		sb.append(" ) ");
		//---总包合同为销售合同或者收款合同，且销售合同或收款合同不能同时为空----end--------------
		
		sb.append(" or ct_pu.pk_ct_pu= "+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS+"."+FBPuCtRptFieldConstant.PK_FBHT);
		sb.append(" ) ");
		sb.append(" and ct_pu.dr =0 and ct_pu.blatest='Y' ");
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet is =querytool.query(sb.toString());
		String[][] objs =is.toTwoDimensionStringArray();
		dataset.setDatas(objs);
	}

	/**
	 * 处理验收入库/进度金额、已收发票金额、付款金额
	 * @param dataset
	 */
	private void processMny(SCMRptDataSet dataset,Map<String,FBPuCtRptVO> vomap,String data){
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(), FBPuCtRptConstant.TEM_FBPUCT_ALLCT);
		String sql = this.view.getViewSql();
		String newsql = sql.substring(0, sql.indexOf("where"));
		StringBuffer sb = new StringBuffer("");
		sb.append(newsql);
		sb.append(" , "+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		sb.append(" where  ");
		sb.append(" ( ");
		
		//---总包合同为销售合同或者收款合同，且销售合同或收款合同不能同时为空----start--------------
		sb.append(" ( ");
		sb.append(" ( ct_pu.vdef5 = "+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS+".vdef5 or ct_pu.vdef15 ="+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS+".vdef15 ) " );
			sb.append(" and (  ");
			sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
			sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
			sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
			sb.append(" ) ");
		sb.append(" ) ");
		//---总包合同为销售合同或者收款合同，且销售合同或收款合同不能同时为空----end--------------
		
		sb.append(" or ct_pu.pk_ct_pu= "+FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS+"."+FBPuCtRptFieldConstant.PK_FBHT);
		sb.append(" ) ");
		sb.append(" and ct_pu.dr =0 and ct_pu.blatest='Y' ");
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet is =querytool.query(sb.toString());
		String[][] objs =is.toTwoDimensionStringArray();
		dataset.setDatas(objs);
	}
	@Override
	protected SCMRptDataSet process(SCMRptDataSet dataset)
			throws BusinessException {
	    if (!this.getScmQueryCondition().isHaveCondition()||dataset.getCount()<=0) {
	        return dataset;
	      }
	    //处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
	    this.queryAllCt(dataset);
	    Map<String,FBPuCtRptVO> vomap = new HashMap<String,FBPuCtRptVO>();
	    //处理累计的数据
	    this.
//
//	      List<SCMRptRowData> dataList = new ArrayList<SCMRptRowData>();
//	      List<SCMRptRowData> nullDataList = new ArrayList<SCMRptRowData>();
//	      AnalyseTypeObject analyseTypeObject = new AnalyseTypeObject();
//	      this.analysetype =
//	          Integer.parseInt(this.tranMap.getConditionVO(
//	              ExecPriceQueryConst.ANALYSETYPE).getValue());
//	      analyseTypeObject.setAnalyseField(this
//	          .setAnalyseTypeFieldValue(this.analysetype));
//	      // 税率查询参数
//	      List<PurpRptRateQueryParam> taxRateQueryParamList =
//	          new ArrayList<PurpRptRateQueryParam>();
//
//	      for (int row = 0; row < dataset.getCount(); row++) {
//	        SCMRptRowData rowdata = dataset.getRow(row);
//	        // 构造税率查询参数
//	        this.buildTaxRateQueryParam(rowdata, taxRateQueryParamList);
//
//	      }
//
//	      // 期间计算
//	      ExecPriceRptView periodView =
//	          new ExecPriceRptView(this.getScmReportContext(), this.billsrc, false);
//	      ExecPricePeriodCalculator periodCal =
//	          new ExecPricePeriodCalculator(periodView, this, this.timezone);
//	      SCMRptDataSet newDataset = periodCal.process(dataset);
//
//	      if (newDataset != null && newDataset.getCount() > 0) {
//	        this.setRowDataList(dataList, newDataset, analyseTypeObject);
//	      }
//
//	      this.setAnalyseTypeValue(nullDataList, newDataset, analyseTypeObject);
//	      dataList.addAll(nullDataList);
//	      dataset.clearData();
//	      if (dataList.size() < 1) {
//	        dataset.addRowDatas(null);
//	      }
//	      else {
//	        // 查询税率，并填充到数据集中。
//	        this.fillInTaxRate(dataList, taxRateQueryParamList);
//	        dataset.addRowDatas(dataList.toArray(new SCMRptRowData[dataList.size()]));
//	      }
	      return dataset;
	    }

	@Override
	protected SCMRptAbsScalePrcStrategy getScalePrcStrategy() {
		return null;
	}

}
