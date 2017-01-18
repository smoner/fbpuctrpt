package nc.scm.puct.report.tmplate.source;

import nc.vo.pub.JavaType;

public class FBPuCtRptConstant {
	/**供应商表名*/
	public final static String TABLE_SUPPLY="bd_supplier";
	/**项目表名*/
	public final static String TABLE_PROJECT="bd_project";
	/**合同表头表名*/
	public final static String TABLE_CT="ct_pu";
	/**合同表体表名*/
	public final static String TABLE_CT_B="ct_pu_b";
	/**收款合同表名*/
	public final static String TABLE_SK="fct_ar";
	/**销售合同表名*/
	public final static String TABLE_XS="ct_sale";
	
	  public final static String SQLTYPE_DOUBLE ="number(28,8)";
	  public final static String SQLTYPE_STRING ="varchar2(200)";
	  public final static String TEM_FBPUCT_FROM_CONDITIONS ="tem_fbpuct_from_conditions";
	  public final static String TEM_FBPUCT_FINAL ="tem_fbpuct_final";
	  public final static String TEM_FBPUCT_IN ="tem_fbpuct_in";
	  public final static String TEM_FBPUCT_INVOICE ="tem_fbpuct_invoice";
	  public final static String TEM_FBPUCT_PAY ="tem_fbpuct_pay";
	  
	  //左连接sql
	  public final static String LEFT_JOIN_ORDER =" left join po_order_b  on po_order_b.csourceid   = ct_pu.pk_ct_pu ";
	  public final static String LEFT_JOIN_ARRIVE =" left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order ";
	  public final static String LEFT_JOIN_IN =" left join ic_purchasein_b  on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder ";
	  public final static String LEFT_JOIN_INVOICE =" left join po_invoice_b  on (po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid	or   po_invoice_b.csourceid = po_order_b.pk_order ) ";
	  public final static String LEFT_JOIN_PAYABLEITEM =" left join ap_payableitem    on ap_payableitem.top_billid   = po_invoice_b.pk_invoice ";
	  public final static String LEFT_JOIN_PAYITEM =" left join ap_payitem        on ap_payitem.top_billid 	= ap_payableitem.pk_payablebill ";
	  
	  //统计字段
	  public final static String SELECT_MNY_IN =" ic_purchasein_b.nmny , ic_purchasein_b.ntaxmny ";
	  public final static String SELECT_MNYS_INVOICE =" po_invoice_b.pk_invoice ,po_invoice_b.pk_invoice_b  ";
	  public final static String SELECT_MNY_PAYITEM =" ap_payitem.pk_paybill ,ap_payitem.pk_payitem ";
	  
	  public final static String SELECT_FIELDS_ORDER =" po_order_b.pk_order,po_order_b.pk_order_b  ";
	  public final static String SELECT_FIELDS_ARRIVE =" po_arriveorder_b.pk_arriveorder_b , po_arriveorder_b.pk_arriveorder ";
	  public final static String SELECT_FIELDS_IN =" ic_purchasein_b.cgeneralhid ,ic_purchasein_b.cgeneralbid ";
	  public final static String SELECT_FIELDS_INVOICE =" po_invoice_b.pk_invoice ,po_invoice_b.pk_invoice_b  ";
	  public final static String SELECT_FIELDS_PAYABLEITEM =" ap_payableitem.pk_payablebill  ,ap_payableitem.pk_payableitem ";
	  public final static String SELECT_FIELDS_PAYITEM =" ap_payitem.pk_paybill ,ap_payitem.pk_payitem ";
	  
	  
	  //查询采购入库金额
	  public final static String Sql_query_mny_in_1 =" select  distinct  ct_pu.pk_ct_pu pk_ct_pu , sum(ic_purchasein_b.nmny) nmny ,   sum(ic_purchasein_b.ntaxmny) ntaxmny  from  ct_pu  ";
	  public final static String Sql_query_mny_in_3 =" left join po_order_b   on po_order_b.csourceid   = ct_pu.pk_ct_pu   left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join ic_purchasein_h on ic_purchasein_h.cgeneralhid = ic_purchasein_b.cgeneralhid   " ;
	  
	  //查询已收发票金额
	  public final static String Sql_query_mny_invoice_1 =" select  distinct  ct_pu.pk_ct_pu pk_ct_pu ,   sum(po_invoice_b.nmny) nmny ,  sum(po_invoice_b.ntaxmny)  ntaxmny   from  ct_pu  ";
	  public final static String Sql_query_mny_invoice_3 =" left join po_order_b   on po_order_b.csourceid   = ct_pu.pk_ct_pu   left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join po_invoice_b      on (po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid    or   po_invoice_b.csourceid = po_order_b.pk_order )  left join po_invoice on po_invoice.pk_invoice = po_invoice_b.pk_invoice   " ;
	  
	  //查询付款金额
	  public final static String Sql_query_mny_pay_1 =" select  distinct  ct_pu.pk_ct_pu pk_ct_pu ,  sum(ap_payitem.local_notax_de) nmny , sum(ap_payitem.local_money_de ) ntaxmny   from  ct_pu  ";
	  public final static String Sql_query_mny_pay_3 =" left join po_order_b   on po_order_b.csourceid   = ct_pu.pk_ct_pu   left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order  left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join po_invoice_b      on (po_invoice_b.csourceid = ic_purchasein_b.cgeneralhid    or   po_invoice_b.csourceid = po_order_b.pk_order )  left join ap_payableitem    on ap_payableitem.top_billid   = po_invoice_b.pk_invoice left join ap_payitem   on ap_payitem.top_billid = ap_payableitem.pk_payablebill  left join ap_paybill on ap_paybill.pk_paybill = ap_payitem.pk_paybill  " ;
	  
	  //分组条件
	  public final static String Sql_group_by = "  group by ct_pu.pk_ct_pu  " ;
	  
	  /**根据总包合同查询出了所有的子合同，但是没有统计数据**/
	  public final static String TEM_FBPUCT_ALLCT ="tem_fbpuct_allct";
	  public final static String CT_PU ="ct_pu";
	  /** 表体显示字段 - 语义模型 - 自定义字段 */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_ZBCT =
	      new FBPuCtRptFieldPreference[] {
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CTBZT,"填报主体", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTID,"项目pk", JavaType.String,SQLTYPE_STRING),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTCODE,"项目编码", JavaType.String,SQLTYPE_STRING),
	        
	        //总包合同信息
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_XS,"合同pk", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_CODE_XS,"合同编码", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_SK,"合同pk", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_ZBHT_CODE_SK,"合同编码", JavaType.String,SQLTYPE_STRING),
	        
	        //采购分包合同信息-3个
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_FBHT,"合同pk", JavaType.String,SQLTYPE_STRING),	
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CCURRENCYID,"合同本位币", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_FBHT_CODE,"合同编码", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CVENDORID,"采购分包供应商", JavaType.String,SQLTYPE_STRING),
	        //采购合同的含税金额
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NHTMNY,"合同金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DSIGN,"合同签订日期", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.VERSION,"合同版本", JavaType.UFDouble,SQLTYPE_DOUBLE),
	      };
	  /** 表体显示字段 - 语义模型 - 自定义字段 */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_FBCT =
	      new FBPuCtRptFieldPreference[] {
	        //采购分包合同信息
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CHTTYPE,"合同类别", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CHTCONTENT,"合同内容", JavaType.String,SQLTYPE_STRING),
	        //采购合同的未税金额
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NHTCB,"合同成本", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        //根据合同的版本号来判断
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.BCHANGE,"是否变更", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CAGREEMENT,"合同付款约定", JavaType.String,SQLTYPE_STRING),
	        //这个采购合同的最后一次采购入库单的入库时间(到年月就可以)
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DACCEPTANCECHECK,"最终验收日期", JavaType.UFDate,SQLTYPE_STRING),
	      };
	  

	  /** 表体显示字段 - 语义模型 - 自定义字段 */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_ADD =
	      new FBPuCtRptFieldPreference[] {
		  	//采购入库单日期和金额
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_HID_IN,"入库单表头id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_BID_IN,"入库单表体id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DBILLDATE_IN,"采购入库单单据日期", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NMNY_IN,"采购入库单无税金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NTAXMNY_IN,"采购入库单价税合计", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //采购发票日期和金额
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_HID_FP,"采购发票表头id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_BID_FP,"采购发票表体id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DBILLDATE_FP,"采购发票单据日期", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NMNY_PF,"采购发票无税金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NTAXMNY_PF,"采购发票价税合计", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //付款单日期和金额
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_HID_FK,"付款单表头id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_BID_FK,"付款单表体id", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.DBILLDATE_FK,"付款单单据日期", JavaType.UFDate,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NMNY_FK,"付款单无税金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NTAXMNY_FK,"付款单价税合计", JavaType.UFDouble,SQLTYPE_DOUBLE),
	      };

	  /** 表体显示字段 - 语义模型 - 自定义字段 */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_NMNY =
	      new FBPuCtRptFieldPreference[] {
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.String),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.Integer),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.UFDouble),
	        
	        //累计
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_ALL,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NRATE_ALL,"合同进度", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_ALL,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_ALL,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //截至上年末累计
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_LY,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_LY,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_LY,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),

	        //本年累计
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //1月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_1,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_1,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_1,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //2月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_2,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_2,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_2,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //3月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_3,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_3,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_3,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //4月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_4,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_4,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_4,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //5月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_5,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_5,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_5,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //6月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_6,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_6,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_6,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //7月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_7,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_7,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_7,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //8月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_8,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_8,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_8,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //9月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_9,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_9,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_9,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //10月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_10,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_10,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_10,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //11月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_11,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_11,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_11,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        
	        //12月
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINMNY_12,"验收入库/进度金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NINVOICEMNY_12,"已收发票金额", JavaType.UFDouble,SQLTYPE_DOUBLE),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.NPAYMENTMNY_12,"付款金额", JavaType.UFDouble,SQLTYPE_DOUBLE)
	      };
}
