package nc.scm.puct.report.tmplate.source;

import nc.vo.ct.purdaily.entity.CtPuVO;

public class FBPuCtRptFieldConstant {
	  //“填报主体"
	public static final String CTBZT =CtPuVO.PK_ORG;
	  //“项目pk"
	public static final String CPROJECTID=CtPuVO.CPROJECTID;
//	//“项目编码"
//	public static final String CPROJECTCODE="cprojectcode";


	//收款合同pk
	public static final String PK_ZBHT_SK=CtPuVO.VDEF4;
	//收款合同编码
	public static final String PK_ZBHT_CODE_SK=CtPuVO.VDEF5;
	//销售合同PK
	public static final String PK_ZBHT_XS=CtPuVO.VDEF16;
	//合同编码
	public static final String PK_ZBHT_CODE_XS=CtPuVO.VDEF15;


	//“合同pk"
	public static final String PK_FBHT=CtPuVO.PK_CT_PU;
	//“合同编码"
	public static final String PK_FBHT_CODE=CtPuVO.VBILLCODE;
	//合同本位币
	public static final String CCURRENCYID=CtPuVO.CCURRENCYID;
	//“采购分包供应商"
	public static final String CVENDORID=CtPuVO.CVENDORID;
	//“合同类别"
	public static final String CHTTYPE="chttype";
	//“合同内容"
	public static final String CHTCONTENT="chtcontent";
	//“合同金额"
	public static final String NHTMNY=CtPuVO.NTOTALTAXMNY;
	//“合同成本"
	public static final String NHTCB="nhtcb";
	//“是否变更"
	public static final String BCHANGE="bchange";
	//“合同签订日期"
	public static final String DSIGN=CtPuVO.SUBSCRIBEDATE;
	//“合同签订日期"
	public static final String VERSION=CtPuVO.VERSION;
	//“合同付款约定"
	public static final String CAGREEMENT="cagreement";
	//“最终验收日期"
	public static final String DACCEPTANCECHECK="dacceptancecheck";

	
	

  	//采购入库单日期和金额
	//入库单表头id
	public static final String PK_HID_IN="pk_hid_in";
	//入库单表体id
	public static final String PK_BID_IN="pk_bid_in";
	//采购入库单单据日期
	public static final String DBILLDATE_IN="dbilldate_in";
	//采购入库单无税金额
	public static final String NMNY_IN="nmny_in";
	//采购入库单价税合计
	public static final String NTAXMNY_IN="ntaxmny_in";
	
    
    //采购发票日期和金额
	//采购发票表头id
	public static final String PK_HID_FP="pk_hid_fp";
	//采购发票表体id
	public static final String PK_BID_FP="pk_bid_fp";
	//采购发票单据日期
	public static final String DBILLDATE_FP="dbilldate_fp";
	//采购发票无税金额
	public static final String NMNY_PF="nmny_pf";
	//采购发票价税合计
	public static final String NTAXMNY_PF="ntaxmny_pf";
    
    //付款单日期和金额
	//付款单表头id
	public static final String PK_HID_FK="pk_hid_fk";
	//付款单表体id
	public static final String PK_BID_FK="pk_bid_fk";
	//付款单发票单据日期
	public static final String DBILLDATE_FK="dbilldate_fk";
	//付款单无税金额
	public static final String NMNY_FK="nmny_fk";
	//付款单价税合计
	public static final String NTAXMNY_FK="ntaxmny_fk";
	
	
	

	//“验收入库/进度金额"
	public static final String NINMNY_ALL="ninmny_all";
	//“合同进度"
	public static final String NRATE_ALL="nrate_all";
	//“已收发票金额"
	public static final String NINVOICEMNY_ALL="ninvoicemny_all";
	//“付款金额"
	public static final String NPAYMENTMNY_ALL="npaymentmny_all";


	//“验收入库/进度金额"
	public static final String NINMNY_LY="ninmny_ly";
	//“已收发票金额"
	public static final String NINVOICEMNY_LY="ninvoicemny_ly";
	//“付款金额"
	public static final String NPAYMENTMNY_LY="npaymentmny_ly";


	//“验收入库/进度金额"
	public static final String NINMNY_="ninmny_";
	//“已收发票金额"
	public static final String NINVOICEMNY_="ninvoicemny_";
	//“付款金额"
	public static final String NPAYMENTMNY_="npaymentmny_";


	//“验收入库/进度金额"
	public static final String NINMNY_1="ninmny_1";
	//“已收发票金额"
	public static final String NINVOICEMNY_1="ninvoicemny_1";
	//“付款金额"
	public static final String NPAYMENTMNY_1="npaymentmny_1";


	//“验收入库/进度金额"
	public static final String NINMNY_2="ninmny_2";
	//“已收发票金额"
	public static final String NINVOICEMNY_2="ninvoicemny_2";
	//“付款金额"
	public static final String NPAYMENTMNY_2="npaymentmny_2";


	//“验收入库/进度金额"
	public static final String NINMNY_3="ninmny_3";
	//“已收发票金额"
	public static final String NINVOICEMNY_3="ninvoicemny_3";
	//“付款金额"
	public static final String NPAYMENTMNY_3="npaymentmny_3";


	//“验收入库/进度金额"
	public static final String NINMNY_4="ninmny_4";
	//“已收发票金额"
	public static final String NINVOICEMNY_4="ninvoicemny_4";
	//“付款金额"
	public static final String NPAYMENTMNY_4="npaymentmny_4";


	//“验收入库/进度金额"
	public static final String NINMNY_5="ninmny_5";
	//“已收发票金额"
	public static final String NINVOICEMNY_5="ninvoicemny_5";
	//“付款金额"
	public static final String NPAYMENTMNY_5="npaymentmny_5";


	//“验收入库/进度金额"
	public static final String NINMNY_6="ninmny_6";
	//“已收发票金额"
	public static final String NINVOICEMNY_6="ninvoicemny_6";
	//“付款金额"
	public static final String NPAYMENTMNY_6="npaymentmny_6";


	//“验收入库/进度金额"
	public static final String NINMNY_7="ninmny_7";
	//“已收发票金额"
	public static final String NINVOICEMNY_7="ninvoicemny_7";
	//“付款金额"
	public static final String NPAYMENTMNY_7="npaymentmny_7";


	public static final String NINMNY_8="ninmny_8";
	//“验收入库/进度金额"
	public static final String NINVOICEMNY_8="ninvoicemny_8";
	//“已收发票金额"
	public static final String NPAYMENTMNY_8="npaymentmny_8";
	//“付款金额"


	//“验收入库/进度金额"
	public static final String NINMNY_9="ninmny_9";
	//“已收发票金额"
	public static final String NINVOICEMNY_9="ninvoicemny_9";
	//“付款金额"
	public static final String NPAYMENTMNY_9="npaymentmny_9";


	//“验收入库/进度金额"
	public static final String NINMNY_10="ninmny_10";
	//“已收发票金额"
	public static final String NINVOICEMNY_10="ninvoicemny_10";
	//“付款金额"
	public static final String NPAYMENTMNY_10="npaymentmny_10";


	//“验收入库/进度金额"
	public static final String NINMNY_11="ninmny_11";
	//“已收发票金额"
	public static final String NINVOICEMNY_11="ninvoicemny_11";
	//“付款金额"
	public static final String NPAYMENTMNY_11="npaymentmny_11";


	//“验收入库/进度金额"
	public static final String NINMNY_12="ninmny_12";
	//“已收发票金额"
	public static final String NINVOICEMNY_12="ninvoicemny_12";
	//“付款金额"
	public static final String NPAYMENTMNY_12="npaymentmny_12";
	
	
}
