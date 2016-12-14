package nc.scm.puct.report.tmplate;

import nc.scmmm.pub.scmpub.report.scale.SCMRptAbsScalePrcStrategy;
import nc.scmmm.pub.scmpub.report.smart.templet.SimpleAbsRptDataSetTemplet;
import nc.scmmm.vo.scmpub.report.entity.SCMRptDataSet;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMProviderMetaData;
import nc.scmmm.vo.scmpub.report.viewfactory.define.SCMView;
import nc.vo.pub.BusinessException;

public class FBPuCtRptTemplate  extends SimpleAbsRptDataSetTemplet{
	  private void addHeadFields(SCMProviderMetaData metaData) {
		    SCMProviderMetaUtil.getSCMProviderMetaData(metaData,
		        new OrderHeaderVO().getMetaData(), ExecPriceConstant.HEAD_META);
		    ExecPriceFieldPreference[] headFields = ExecPriceConstant.SMART_HEAD_SELDEF;
		    this.createFields(metaData, headFields);
		  }
	  private void addBodyFields(SCMProviderMetaData metaData) {
		    SCMProviderMetaUtil.getSCMProviderMetaData(metaData,
		        new OrderItemVO().getMetaData(), ExecPriceConstant.BODY_META);
		    ExecPriceFieldPreference[] bodyFields = ExecPriceConstant.SMART_BODY_SELDEF;
		    this.createFields(metaData, bodyFields);
		  }
	@Override
	protected SCMProviderMetaData getSCMRptMetaData() throws BusinessException {
	    SCMProviderMetaData metaData = new SCMProviderMetaData();
	    this.addHeadFields(metaData);
	    this.addBodyFields(metaData);
	    return metaData;
	  }

	@Override
	protected SCMView getSCMView() {
	    if (!this.getScmQueryCondition().isHaveCondition()) {
	        ExecPriceRptView initView =
	            new ExecPriceRptView(this.getScmReportContext());
	        initView.initDetails();
	        return initView;
	      }
//
//	      this.tranMap = this.getSCMReportTransMap();
//	      this.timezone = this.tranMap.getLocalTimeZone();
//	      if (null != this.tranMap.get(StockExecConstant.LINKQUERY)) {
//	        this.isLinkQuery = UFBoolean.TRUE;
//	      }
//	      // Ϊ������ڼ���ʱ�����ÿ�ʼ����ʱ��
//	      this.handlerAnalyDate(this.tranMap);
//	      // ������Դ
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
//	      this.processWhere();
//
//	      this.view.initSelectDetails(this.headalias, this.bodyalias, this.pricetype);
//
//	      String patchGroup =
//	          this.bodyalias + "." + ExecPriceDispData.PK_SRCMATERIAL + " , "
//	              + this.headalias + "." + ExecPriceDispData.CORIGCURRENCYID;
//	      this.view.setPatchGroup(patchGroup);
//	      this.processInnerJoin();
//	      return this.view;
		
		return null;
	    }

	@Override
	protected boolean isAddRowIndex() {
		return false;
	}

	@Override
	protected SCMRptDataSet process(SCMRptDataSet dataset)
			throws BusinessException {
//	    if (!this.getScmQueryCondition().isHaveCondition()) {
//	        return dataset;
//	      }
//
//	      List<SCMRptRowData> dataList = new ArrayList<SCMRptRowData>();
//	      List<SCMRptRowData> nullDataList = new ArrayList<SCMRptRowData>();
//	      AnalyseTypeObject analyseTypeObject = new AnalyseTypeObject();
//	      this.analysetype =
//	          Integer.parseInt(this.tranMap.getConditionVO(
//	              ExecPriceQueryConst.ANALYSETYPE).getValue());
//	      analyseTypeObject.setAnalyseField(this
//	          .setAnalyseTypeFieldValue(this.analysetype));
//	      // ˰�ʲ�ѯ����
//	      List<PurpRptRateQueryParam> taxRateQueryParamList =
//	          new ArrayList<PurpRptRateQueryParam>();
//
//	      for (int row = 0; row < dataset.getCount(); row++) {
//	        SCMRptRowData rowdata = dataset.getRow(row);
//	        // ����˰�ʲ�ѯ����
//	        this.buildTaxRateQueryParam(rowdata, taxRateQueryParamList);
//
//	      }
//
//	      // �ڼ����
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
//	        // ��ѯ˰�ʣ�����䵽���ݼ��С�
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
