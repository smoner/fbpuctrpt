package nc.scm.puct.report.tmplate.source;

import nc.vo.pub.JavaType;

public class FBPuCtRptConstant {
	  public final static String SQLTYPE_DOUBLE ="number(28,8)";
	  public final static String SQLTYPE_STRING ="varchar2(200)";
	  /** 表体显示字段 - 语义模型 - 自定义字段 */
	  public static final FBPuCtRptFieldPreference[] SMART_FIELDS_ZBCT =
	      new FBPuCtRptFieldPreference[] {
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.String),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.Integer),
//	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.PK_SUPPLIER,"", JavaType.UFDouble),
		  //	
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CTBZT,"填报主体", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTID,"项目pk", JavaType.String,SQLTYPE_STRING),
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.CPROJECTCODE,"项目编码", JavaType.String,SQLTYPE_STRING),
	        
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
	        new FBPuCtRptFieldPreference(FBPuCtRptFieldConstant.VERSION,"合同签订日期", JavaType.UFDouble,SQLTYPE_DOUBLE),
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
