package nc.ui.scm.puct.report.initializer;

import nc.pub.pp.rept.execprice.constant.ExecPriceDispData;
import nc.scmmm.pub.scmpub.report.scale.SCMRptAbsScalePrcStrategy;
import nc.scmmm.vo.scmpub.report.entity.scale.SCMReportScaleMetaRegister;

public class FBPuCtRptScaleStrategy extends SCMRptAbsScalePrcStrategy {

	private static final long serialVersionUID = -8355999342446606623L;
//
//	@Override
//	  public String[] getAssNumKey() {
//	    return new String[] {
//	      ExecPriceDispData.STOCKNUM
//	    };
//	  }
//
//	  @Override
//	  public String getCassunitKey() {
//	    return "this.pk_srcmaterial.pk_measdoc";
//	  }
//
//	  @Override
//	  public String[] getCostMnyKey() {
//	    return new String[] {
//	      ExecPriceDispData.REFERENCECOST
//	    };
//	  }
//
//	  @Override
//	  public String[] getCostPriceKey() {
//	    return new String[] {
//	      ExecPriceDispData.MAXPRICE, ExecPriceDispData.MINPRICE,
//	      ExecPriceDispData.AVGPRICE, ExecPriceDispData.NEWPRICE,
//	      ExecPriceDispData.PLANPRICE, ExecPriceDispData.STOCKPRICE
//	    };
//	  }
//
//	  @Override
//	  public String[] getMnyKey() {
//	    return new String[] {
//	      ExecPriceDispData.STOCKMNY
//	    };
//	  }
//
//	  @Override
//	  public String[] getTaxRateStrFields() {
//	    return new String[] {
//	      ExecPriceDispData.NTAXRATE
//	    };
//	  }
//
	  @Override
	  protected void registerScaleProcess(
	      SCMReportScaleMetaRegister scmRptScaleRegister) {
//	    // 本币金额：采购 金额
//	    scmRptScaleRegister.setMnyDigits(ExecPriceDispData.CORIGCURRENCYID,
//	        this.getMnyKey());
//	    // 成本金额：参考成本
//	    scmRptScaleRegister.setCostPriceDigits(this.getCostMnyKey());
//	    // 价格：最高报价 、最低报价 、平均报价 、最新报价 、计划价 、采购执行价格
//	    // scmRptScaleRegister.setPriceDigits(this.getCostPriceKey());
//	    // 按照币种设置 价格 精度 add by zhangrqb 20141223 精度适配 20150115
//	    scmRptScaleRegister.setPriceDigits(ExecPriceDispData.CORIGCURRENCYID,
//	        this.getCostPriceKey());
//	    // 采购数量
//	    scmRptScaleRegister
//	        .setNumDigits(this.getCassunitKey(), this.getAssNumKey());
//	    // 税率
//	    scmRptScaleRegister.setTaxRateDigits(this.getTaxRateStrFields());
	  }


	}
