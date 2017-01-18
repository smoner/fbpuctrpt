package nc.ui.scm.puct.report.initializer;

import java.util.ArrayList;
import java.util.List;

import nc.scm.puct.report.tmplate.source.FBPuCtRptConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldPreference;
import nc.scmmm.pub.scmpub.report.scale.SCMRptAbsScalePrcStrategy;
import nc.scmmm.vo.scmpub.report.entity.scale.SCMReportScaleMetaRegister;

public class FBPuCtRptScaleStrategy extends SCMRptAbsScalePrcStrategy {

	private static final long serialVersionUID = 1568908640452106395L;

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
		  List<String> fieldnames = new ArrayList<String>();
		  fieldnames.add(FBPuCtRptFieldConstant.NHTMNY);
		  fieldnames.add(FBPuCtRptFieldConstant.NHTCB);
		  for(FBPuCtRptFieldPreference f :FBPuCtRptConstant.SMART_FIELDS_NMNY){
//			  if(!FBPuCtRptFieldConstant.NRATE_ALL.equals(f.getFieldname())){
				  fieldnames.add(f.getFieldname());
//			  }
		  }
		  scmRptScaleRegister.setConstantDigits(fieldnames.toArray(new String[0]), 2);
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
