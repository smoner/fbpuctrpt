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
//	    // ���ҽ��ɹ� ���
//	    scmRptScaleRegister.setMnyDigits(ExecPriceDispData.CORIGCURRENCYID,
//	        this.getMnyKey());
//	    // �ɱ����ο��ɱ�
//	    scmRptScaleRegister.setCostPriceDigits(this.getCostMnyKey());
//	    // �۸���߱��� ����ͱ��� ��ƽ������ �����±��� ���ƻ��� ���ɹ�ִ�м۸�
//	    // scmRptScaleRegister.setPriceDigits(this.getCostPriceKey());
//	    // ���ձ������� �۸� ���� add by zhangrqb 20141223 �������� 20150115
//	    scmRptScaleRegister.setPriceDigits(ExecPriceDispData.CORIGCURRENCYID,
//	        this.getCostPriceKey());
//	    // �ɹ�����
//	    scmRptScaleRegister
//	        .setNumDigits(this.getCassunitKey(), this.getAssNumKey());
//	    // ˰��
//	    scmRptScaleRegister.setTaxRateDigits(this.getTaxRateStrFields());
	  }


	}
