package nc.scm.puct.report.tmplate.source;

import nc.vo.pub.JavaType;

public class FBPuCtRptConstant {
	  public final static String SQLTYPE_DOUBLE ="number(28,8)";
	  public final static String SQLTYPE_STRING ="varchar2(200)";
	  /** ������ʾ�ֶ� - ����ģ�� - �Զ����ֶ� */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_ZBCT =
	      new FBPuCtRptFieldPreference[] {
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.String),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.Integer),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.UFDouble),
		  //	
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CTBZT,"�����", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTID,"��Ŀpk", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTCODE,"��Ŀ����", JavaType.String,SQLTYPE_STRING),
	        
	        //�ܰ���ͬ��Ϣ
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_XS,"��ͬpk", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_CODE_XS,"��ͬ����", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_SK,"��ͬpk", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_CODE_SK,"��ͬ����", JavaType.String,SQLTYPE_STRING),
	        
	        //�ɹ��ְ���ͬ��Ϣ-3��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_FBHT,"��ͬpk", JavaType.String,SQLTYPE_STRING),	
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CCURRENCYID,"��ͬ��λ��", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_FBHT_CODE,"��ͬ����", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CVENDORID,"�ɹ��ְ���Ӧ��", JavaType.String,SQLTYPE_STRING),
	        //�ɹ���ͬ�ĺ�˰���
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NHTMNY,"��ͬ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DSIGN,"��ͬǩ������", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.VERSION,"��ͬǩ������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	      };
	  /** ������ʾ�ֶ� - ����ģ�� - �Զ����ֶ� */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_FBCT =
	      new FBPuCtRptFieldPreference[] {
	        //�ɹ��ְ���ͬ��Ϣ
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CHTTYPE,"��ͬ���", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CHTCONTENT,"��ͬ����", JavaType.String,SQLTYPE_STRING),
	        //�ɹ���ͬ��δ˰���
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NHTCB,"��ͬ�ɱ�", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        //���ݺ�ͬ�İ汾�����ж�
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.BCHANGE,"�Ƿ���", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CAGREEMENT,"��ͬ����Լ��", JavaType.String,SQLTYPE_STRING),
	        //����ɹ���ͬ�����һ�βɹ���ⵥ�����ʱ��(�����¾Ϳ���)
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DACCEPTANCECHECK,"������������", JavaType.UFDate,SQLTYPE_STRING),
	      };
	  

	  /** ������ʾ�ֶ� - ����ģ�� - �Զ����ֶ� */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_ADD =
	      new FBPuCtRptFieldPreference[] {
		  	//�ɹ���ⵥ���ںͽ��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_HID_IN,"��ⵥ��ͷid", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_BID_IN,"��ⵥ����id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DBILLDATE_IN,"�ɹ���ⵥ��������", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NMNY_IN,"�ɹ���ⵥ��˰���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NTAXMNY_IN,"�ɹ���ⵥ��˰�ϼ�", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //�ɹ���Ʊ���ںͽ��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_HID_FP,"�ɹ���Ʊ��ͷid", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_BID_FP,"�ɹ���Ʊ����id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DBILLDATE_FP,"�ɹ���Ʊ��������", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NMNY_PF,"�ɹ���Ʊ��˰���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NTAXMNY_PF,"�ɹ���Ʊ��˰�ϼ�", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //������ںͽ��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_HID_FK,"�����ͷid", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_BID_FK,"�������id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DBILLDATE_FK,"�����������", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NMNY_FK,"�����˰���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NTAXMNY_FK,"�����˰�ϼ�", JavaType.UFDouble,SQLTYPE_DOUBLE),
	      };

	  /** ������ʾ�ֶ� - ����ģ�� - �Զ����ֶ� */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_NMNY =
	      new FBPuCtRptFieldPreference[] {
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.String),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.Integer),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.UFDouble),
	        
	        //�ۼ�
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_ALL,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NRATE_ALL,"��ͬ����", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_ALL,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_ALL,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //��������ĩ�ۼ�
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_LY,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_LY,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_LY,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),

	        //�����ۼ�
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //1��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_1,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_1,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_1,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //2��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_2,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_2,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_2,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //3��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_3,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_3,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_3,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //4��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_4,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_4,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_4,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //5��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_5,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_5,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_5,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //6��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_6,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_6,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_6,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //7��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_7,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_7,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_7,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //8��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_8,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_8,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_8,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //9��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_9,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_9,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_9,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //10��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_10,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_10,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_10,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //11��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_11,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_11,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_11,"������", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //12��
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_12,"�������/���Ƚ��", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_12,"���շ�Ʊ���", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_12,"������", JavaType.UFDouble,SQLTYPE_DOUBLE)
	      };
}
