package nc.scm.puct.report.tmplate.source;

import nc.vo.pub.JavaType;

public class FBPuCtRptConstant {
	/**��Ӧ�̱���*/
	public final static String TABLE_SUPPLY="bd_supplier";
	/**��Ŀ����*/
	public final static String TABLE_PROJECT="bd_project";
	/**��ͬ��ͷ����*/
	public final static String TABLE_CT="ct_pu";
	/**��ͬ�������*/
	public final static String TABLE_CT_B="ct_pu_b";
	/**�տ��ͬ����*/
	public final static String TABLE_SK="fct_ar";
	/**���ۺ�ͬ����*/
	public final static String TABLE_XS="ct_sale";
	
	  public final static String SQLTYPE_DOUBLE ="number(28,8)";
	  public final static String SQLTYPE_STRING ="varchar2(200)";
	  public final static String TEM_FBPUCT_FROM_CONDITIONS ="tem_fbpuct_from_conditions";
	  public final static String TEM_FBPUCT_FINAL ="tem_fbpuct_final";
	  public final static String TEM_FBPUCT_IN ="tem_fbpuct_in";
	  public final static String TEM_FBPUCT_INVOICE ="tem_fbpuct_invoice";
	  public final static String TEM_FBPUCT_PAY ="tem_fbpuct_pay";
	  
	  //������sql
	  public final static String LEFT_JOIN_ORDER =" left join po_order_b  on po_order_b.csourceid   = ct_pu.pk_ct_pu ";
	  public final static String LEFT_JOIN_ARRIVE =" left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order ";
	  public final static String LEFT_JOIN_IN =" left join ic_purchasein_b  on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder ";
	  public final static String LEFT_JOIN_INVOICE =" left join po_invoice_b  on (po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid	or   po_invoice_b.csourceid = po_order_b.pk_order ) ";
	  public final static String LEFT_JOIN_PAYABLEITEM =" left join ap_payableitem    on ap_payableitem.top_billid   = po_invoice_b.pk_invoice ";
	  public final static String LEFT_JOIN_PAYITEM =" left join ap_payitem        on ap_payitem.top_billid 	= ap_payableitem.pk_payablebill ";
	  
	  //ͳ���ֶ�
	  public final static String SELECT_MNY_IN =" ic_purchasein_b.nmny , ic_purchasein_b.ntaxmny ";
	  public final static String SELECT_MNYS_INVOICE =" po_invoice_b.pk_invoice ,po_invoice_b.pk_invoice_b  ";
	  public final static String SELECT_MNY_PAYITEM =" ap_payitem.pk_paybill ,ap_payitem.pk_payitem ";
	  
	  public final static String SELECT_FIELDS_ORDER =" po_order_b.pk_order,po_order_b.pk_order_b  ";
	  public final static String SELECT_FIELDS_ARRIVE =" po_arriveorder_b.pk_arriveorder_b , po_arriveorder_b.pk_arriveorder ";
	  public final static String SELECT_FIELDS_IN =" ic_purchasein_b.cgeneralhid ,ic_purchasein_b.cgeneralbid ";
	  public final static String SELECT_FIELDS_INVOICE =" po_invoice_b.pk_invoice ,po_invoice_b.pk_invoice_b  ";
	  public final static String SELECT_FIELDS_PAYABLEITEM =" ap_payableitem.pk_payablebill  ,ap_payableitem.pk_payableitem ";
	  public final static String SELECT_FIELDS_PAYITEM =" ap_payitem.pk_paybill ,ap_payitem.pk_payitem ";
	  
	  
	  //��ѯ�ɹ������
	  public final static String Sql_query_mny_in_1 =" select  distinct  ct_pu.pk_ct_pu pk_ct_pu , sum(ic_purchasein_b.nmny) nmny ,   sum(ic_purchasein_b.ntaxmny) ntaxmny  from  ct_pu  ";
	  public final static String Sql_query_mny_in_3 =" left join po_order_b   on po_order_b.csourceid   = ct_pu.pk_ct_pu   left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join ic_purchasein_h on ic_purchasein_h.cgeneralhid = ic_purchasein_b.cgeneralhid   " ;
	  
	  //��ѯ���շ�Ʊ���
	  public final static String Sql_query_mny_invoice_1 =" select  distinct  ct_pu.pk_ct_pu pk_ct_pu ,   sum(po_invoice_b.nmny) nmny ,  sum(po_invoice_b.ntaxmny)  ntaxmny   from  ct_pu  ";
	  public final static String Sql_query_mny_invoice_3 =" left join po_order_b   on po_order_b.csourceid   = ct_pu.pk_ct_pu   left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join po_invoice_b      on (po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid    or   po_invoice_b.csourceid = po_order_b.pk_order )  left join po_invoice on po_invoice.pk_invoice = po_invoice_b.pk_invoice   " ;
	  
	  //��ѯ������
	  public final static String Sql_query_mny_pay_1 =" select  distinct  ct_pu.pk_ct_pu pk_ct_pu ,  sum(ap_payitem.local_notax_de) nmny , sum(ap_payitem.local_money_de ) ntaxmny   from  ct_pu  ";
	  public final static String Sql_query_mny_pay_3 =" left join po_order_b   on po_order_b.csourceid   = ct_pu.pk_ct_pu   left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join po_invoice_b      on (po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid    or   po_invoice_b.csourceid = po_order_b.pk_order )  left join ap_payableitem    on ap_payableitem.top_billid   = po_invoice_b.pk_invoice left join ap_payitem   on ap_payitem.top_billid = ap_payableitem.pk_payablebill  left join ap_paybill on ap_paybill.pk_paybill = ap_payitem.pk_paybill  " ;
	  
	  //��������
	  public final static String Sql_group_by = "  group by ct_pu.pk_ct_pu  " ;
	  
	  /**�����ܰ���ͬ��ѯ�������е��Ӻ�ͬ������û��ͳ������**/
	  public final static String TEM_FBPUCT_ALLCT ="tem_fbpuct_allct";
	  public final static String CT_PU ="ct_pu";
	  /** ������ʾ�ֶ� - ����ģ�� - �Զ����ֶ� */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_ZBCT =
	      new FBPuCtRptFieldPreference[] {
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CTBZT,"�����", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTID,"��Ŀpk", JavaType.String,SQLTYPE_STRING),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTCODE,"��Ŀ����", JavaType.String,SQLTYPE_STRING),
	        
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
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.VERSION,"��ͬ�汾", JavaType.UFDouble,SQLTYPE_DOUBLE),
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
