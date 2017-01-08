package nc.scm.puct.report.tmplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import nc.scmmm.vo.scmpub.report.entity.SCMRptColumnInfo;
import nc.scmmm.vo.scmpub.report.entity.SCMRptDataSet;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMField;
import nc.scmmm.vo.scmpub.report.entity.metadata.SCMProviderMetaData;
import nc.scmmm.vo.scmpub.report.viewfactory.define.SCMView;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.data.IRowSet;

import org.apache.commons.lang.StringUtils;

public class FBPuCtRptTemplate extends SimpleAbsRptDataSetTemplet {
	private FBPuCtView view = null;
	// 来自查询对话框的查询条件vo
	public List<ConditionVO> condsFromQryDlgList;

	private void addHeadFields(SCMProviderMetaData metaData) {
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_ZBCT);
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_ADD);
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_FBCT);
		this.createFields(metaData, FBPuCtRptConstant.SMART_FIELDS_NMNY);
	}

	private void createFields(SCMProviderMetaData metaData,
			FBPuCtRptFieldPreference[] fields) {
		for (FBPuCtRptFieldPreference field : fields) {
			SCMField scmfiled = SCMProviderMetaUtil.getDynamicField(
					field.getFieldname(), field.getFieldChnName(),
					field.getFieldType());
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
			FBPuCtView initView = new FBPuCtView(this.getScmReportContext());
			initView.initDetails();
			return initView;
		}
		//
		// this.tranMap = this.getSCMReportTransMap();
		// this.timezone = this.tranMap.getLocalTimeZone();
		// if (null != this.tranMap.get(StockExecConstant.LINKQUERY)) {
		// this.isLinkQuery = UFBoolean.TRUE;
		// }
		// // 为后面的期间临时表设置开始结束时间
		// this.handlerAnalyDate(this.tranMap);
		// // 单据来源
		// this.billsrc = this.getBillSrc();
		//
		// if (this.billsrc == ExecPriceBillSrcEnum.POORDER) {
		// this.headalias = ExecPriceConstant.POORDER_H;
		// this.bodyalias = ExecPriceConstant.POORDER_B;
		// }
		// else {
		// this.headalias = ExecPriceConstant.POINVOICE_H;
		// this.bodyalias = ExecPriceConstant.POINVOICE_B;
		// }
		// this.view =
		// new ExecPriceRptView(this.getScmReportContext(), this.billsrc, true);
		this.view = new FBPuCtView(this.getScmReportContext());
		this.view.initDetails();
		this.processWhere();
		//
		// this.view.initSelectDetails(this.headalias, this.bodyalias,
		// this.pricetype);
		//
		// String patchGroup =
		// this.bodyalias + "." + ExecPriceDispData.PK_SRCMATERIAL + " , "
		// + this.headalias + "." + ExecPriceDispData.CORIGCURRENCYID;
		// this.view.setPatchGroup(patchGroup);
		// this.processInnerJoin();
		// return this.view;
		return this.view;
	}

	private void processWhere() {
		ConditionVO[] generalConds = this.getScmQueryCondition()
				.getAllQueryConditions();
		StringBuilder where = new StringBuilder();
		this.transQryConds(generalConds, where);
		String sql = new ConditionVO().getSQLStr(this.condsFromQryDlgList
				.toArray(new ConditionVO[this.condsFromQryDlgList.size()]));
		if (StringUtils.isNotBlank(sql)) {
			where.append(" and ");
			where.append(sql);
		}
		// 设置默认的查询条件
		// where.append(this.getDefaultWhere());
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
					where.append(" and ct_pu.ctname = '" + condvo.getValue()
							+ "'  ");
				} else if (condvo.getOperaCode().contains("left like")) {
					where.append(" and  ct_pu.cname left like  '"
							+ condvo.getValue() + "%' ");
				}
			}
			// // 总包合同签订日期
			// else if
			// (condvo.getFieldCode().equals(ExecPriceQueryConst.MATERIALCODE))
			// {
			// condvo.setFieldCode(this.bodyalias + "."
			// + ExecPriceDispData.PK_MATERIAL);
			// this.condsFromQryDlgList.add(condvo);
			// }
			// //
			// else if
			// (condvo.getFieldCode().equals(ExecPriceQueryConst.MATERIALNAME))
			// {
			// if (condvo.getOperaCode().equals("in")) {
			// where.append(" and bd_material.name in " + condvo.getValue() +
			// "");
			// }
			// else if (condvo.getOperaCode().equals("=")) {
			// where.append(" and bd_material.name = '"
			// + SCMESAPI.sqlEncodeGeneral(condvo.getValue()) + "' ");
			// }
			// else if (condvo.getOperaCode().contains("left like")) {
			// where.append(" and bd_material.name like '"
			// + SCMESAPI.sqlEncodeGeneral(condvo.getValue()) + "%' ");
			// }
			// continue;
			// }
			// // 分析区间:非其他
			// else if
			// (condvo.getFieldCode().equals(ExecPriceQueryConst.ANALYAREA)
			// && !condvo.getValue().equals(
			// String.valueOf(ExecPricePeriodTypeEnum.OTHER))) {
			// this.analyArea = Integer.valueOf(condvo.getValue()).intValue();
			// } // 分析区间：其他
			// else if
			// (condvo.getFieldCode().equals(ExecPriceQueryConst.ANALYAREA)
			// && condvo.getValue().equals(
			// String.valueOf(ExecPricePeriodTypeEnum.OTHER))) {
			// for (ConditionVO innerConVO : generalConds) {
			// if (innerConVO.getFieldCode().equals(ExecPriceQueryConst.DAYS)) {
			// this.analyArea =
			// Integer.valueOf("33" + innerConVO.getValue()).intValue();
			// }
			// }
			// }
			// // 天数
			// else if (condvo.getFieldCode().equals(ExecPriceQueryConst.DAYS)
			// && condvo.getValue().equals(
			// String.valueOf(ExecPricePeriodTypeEnum.OTHER))) {
			// }
			// // 价格取值
			// else if
			// (condvo.getFieldCode().equals(ExecPriceQueryConst.PRICETYPE)) {
			// this.pricetype = Integer.valueOf(condvo.getValue()).intValue();
			// // // 含税, 原币价税合计非负
			// // if (this.pricetype == StockExecPriceTypeEnum.TAX) {
			// // where.append("and not( coalesce(" + this.bodyalias + "."
			// // + StockExecConstant.NORIGTAXMNY + ",0) < 0) ");
			// // }
			// // // 不含税, 原币无税金额非负
			// // else if (this.pricetype == StockExecPriceTypeEnum.UNTAX) {
			// // where.append("and not( coalesce(" + this.bodyalias + "."
			// // + StockExecConstant.NORIGMNY + ",0) < 0) ");
			// // }
			// }
			//
			// 单据日期
			else if (condvo.getFieldCode().equals(CtPuVO.DBILLDATE)) {
				// String[] dates = condvo.getValue().split(",");
				// this.beginDate = new UFDate(dates[0]);
				// this.endDate = new UFDate(dates[1]);
				// //
				// String filedcode = condvo.getFieldCode();
				// // condvo.setFieldCode(this.headalias + "." + filedcode);
				this.condsFromQryDlgList.add(condvo);
			}
			// 总包合同编码
			else if (condvo.getFieldCode().equals("zbvbillcode")) {
				if (condvo.getOperaCode().equals("=")) {
					where.append(" and ( ct_pu.vdef5 = '" + condvo.getValue()
							+ "' or ct_pu.vdef15 = '" + condvo.getValue()
							+ "' ) ");
				} else if (condvo.getOperaCode().contains("left like")) {
					where.append(" and ( ct_pu.vdef5 left like '"
							+ condvo.getValue()
							+ "%' or ct_pu.vdef15 left like  '"
							+ condvo.getValue() + "%' ) ");
				}
			}
			// 采购合同签订日期
			else if (condvo.getFieldCode().equals("fbctqddate")) {
				condvo.setFieldCode("ct_pu." + CtPuVO.SUBSCRIBEDATE);
				this.condsFromQryDlgList.add(condvo);
			}
			// // 表头：采购组织、采购部门、业务流程、币种、单据日期
			// else if (condvo.getFieldCode().equals(ExecPriceQueryConst.PK_ORG)
			// || condvo.getFieldCode().equals(ExecPriceQueryConst.PK_DEPT)
			// || condvo.getFieldCode().equals(ExecPriceQueryConst.PK_BUSITYPE)
			// ||
			// condvo.getFieldCode().equals(ExecPriceQueryConst.CORIGCURRENCYID))
			// {
			// String filedcode = condvo.getFieldCode();
			// condvo.setFieldCode(this.headalias + "." + filedcode);
			// this.condsFromQryDlgList.add(condvo);
			// }
			// // 供应商
			else if (condvo.getFieldCode().equals(CtPuVO.CVENDORID)) {
				// String filedcode = condvo.getFieldCode();
				// condvo.setFieldCode(this.bodyalias + "." + filedcode);
				this.condsFromQryDlgList.add(condvo);
			}else{
				if(condvo.getFieldCode().indexOf("ct_pu_b")>=0){
					this.view.setHas_body_condition(true);
				}
				this.condsFromQryDlgList.add(condvo);
			}
		}
	}

	@Override
	protected boolean isAddRowIndex() {
		return false;
	}

	/**
	 * 处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
	 * 
	 * @param dataset
	 */
	private void queryAllCt(SCMRptDataSet dataset) {
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		String sql = this.view.getViewSql();
		String newsql = sql.substring(0, sql.indexOf("where"));
		StringBuffer sb = new StringBuffer("");
		sb.append(newsql);
		sb.append(" , " + FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS);
		sb.append(" where  ");
		sb.append(" ( ");

		// ---总包合同为销售合同或者收款合同，且销售合同或收款合同不能同时为空----start--------------
		sb.append(" ( ");
		sb.append(" ( ct_pu.vdef5 = "
				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS
				+ ".vdef5 or ct_pu.vdef15 ="
				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + ".vdef15 ) ");
		sb.append(" and (  ");
		sb.append("  ( nvl(ct_pu.vdef5,'1')='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
		sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')='1' ) ");
		sb.append(" or ( nvl(ct_pu.vdef5,'1')!='1' and nvl(ct_pu.vdef15,'1')!='1' ) ");
		sb.append(" ) ");
		sb.append(" ) ");
		// ---总包合同为销售合同或者收款合同，且销售合同或收款合同不能同时为空----end--------------

		sb.append(" or ct_pu.pk_ct_pu= "
				+ FBPuCtRptConstant.TEM_FBPUCT_FROM_CONDITIONS + "."
				+ FBPuCtRptFieldConstant.PK_FBHT);
		sb.append(" ) ");
		sb.append(" and ct_pu.dr =0  and ct_pu.blatest='Y' ");
		String query_sql =sb.toString().replace("select ", " select distinct ");
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet is = querytool.query(query_sql);
		String[][] objs = is.toTwoDimensionStringArray();
		dataset.setDatas(objs);
	}

	/**
	 * 处理验收入库/进度金额、已收发票金额、付款金额
	 * 
	 * @param dataset
	 */
	private void processMny(SCMRptDataSet dataset,
			Map<String, FBPuCtRptVO> vomap, String data) {
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_ALLCT);
		// 处理累计
		processMnyByDateAndSuffix(vomap, null, null, "all");

		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
//		int day = c.get(Calendar.DAY_OF_MONTH);

		// Calendar c2 = Calendar.getInstance();
		// c2.set(2016, 5, 6);
		// int m=c2.get(Calendar.MONTH)+1;
		// int d=c2.get(Calendar.DAY_OF_MONTH);
		//
		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// int year1 = date.getYear();//年
		// int month1 = date.getMonth()+1;//月
		// int week1 = date.getDay(); //星期几
		// int day1 = date.getDate();//日
		// String curTime = formatter.format(date);

		// 处理截至上年末
		String date_end_last = (year) + "-01-01 00:00:00";
		processMnyByDateAndSuffix(vomap, null, date_end_last, "ly");

		// 处理本年累计
		String date_start_cur = year + "-01-01 00:00:00";
		String date_end_cur = (year + 1) + "-01-01 00:00:00";
		processMnyByDateAndSuffix(vomap, date_start_cur, date_end_cur, "");

		// 处理本年1到12月,小于等于当前月份
		for (int i = 1; i <= month; i++) {
			String date_start_ = null;
			String date_end_ = null;
			if (i < 9) {
				date_start_ = year + "-0" + i + "-01 00:00:00";
				date_end_ = year + "-0" + (i + 1) + "-01 00:00:00";
			} else if (i == 9) {
				date_start_ = year + "-09-01 00:00:00";
				date_end_ = year + "-10-01 00:00:00";
			} else if (i == 10 || i == 11) {
				date_start_ = year + "-" + i + "-01 00:00:00";
				date_end_ = year + "-" + (i + 1) + "-01 00:00:00";
			} else if (i == 12) {
				date_start_ = year + "-12-01 00:00:00";
				date_end_ = (year + 1) + "-01-01 00:00:00";
			}
			processMnyByDateAndSuffix(vomap, date_start_, date_end_,
					String.valueOf(i));
		}

		this.processMny2Dataset(dataset, vomap);
	}

	private void processMnyByDateAndSuffix(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		// 处理验收入库金额
		this.processMny_In(vomap, date_start, date_end, suffix);
		// 处理已收发票金额
		this.processMny_Invoice(vomap, date_start, date_end, suffix);
		// 处理付款金额
		this.processMny_Pay(vomap, date_start, date_end, suffix);
	}

	private void processMny2Dataset(SCMRptDataSet dataset,
			Map<String, FBPuCtRptVO> vomap) {
		if (vomap == null || vomap.size() <= 0) {
			return;
		}
		SCMRptColumnInfo columninfo = dataset.getColumninfo();
		Map<String, Integer> indexmap = columninfo.getColumnIndexMap();
		Object[][] arrs = dataset.getDatas();
		int num = arrs.length;
		int index_pk = indexmap.get(FBPuCtRptFieldConstant.PK_FBHT).intValue();
		for (int i = 0; i < num; i++) {
			String pk = (String) arrs[i][index_pk];
			FBPuCtRptVO vo = vomap.get(pk);
			if (vo != null) {
				for (FBPuCtRptFieldPreference field : FBPuCtRptConstant.SMART_FIELDS_NMNY) {
					String fieldname = field.getFieldname();
					int index = indexmap.get(fieldname).intValue();
					arrs[i][index] = vo.getValue(fieldname);
				}
			}
		}
	}

	/**
	 * 根据时间段和临时表数据汇总已入库金额
	 * */
	private void processMny_In(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NINMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		sb.append(FBPuCtRptConstant.Sql_query_mny_in_1);
		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
				+ " on ct_pu.pk_ct_pu =");
		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
		sb.append(FBPuCtRptConstant.Sql_query_mny_in_3);
		sb.append(" where 1=1 ");
		String date = " ic_purchasein_h.dbilldate ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(FBPuCtRptConstant.Sql_group_by);
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				String inmny_all = arr[1];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(inmny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, inmny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, inmny_all);
				}
			}
		}
	}

	/**
	 * 根据时间段和临时表数据汇总已收发票金额
	 * */
	private void processMny_Invoice(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NINVOICEMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		sb.append(FBPuCtRptConstant.Sql_query_mny_invoice_1);
		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
				+ " on ct_pu.pk_ct_pu =");
		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
		sb.append(FBPuCtRptConstant.Sql_query_mny_invoice_3);
		sb.append(" where 1=1 ");
		String date = " po_invoice.dbilldate ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(FBPuCtRptConstant.Sql_group_by);
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				String invoicemny_all = arr[1];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(invoicemny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, invoicemny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, invoicemny_all);
				}
			}
		}
	}

	/**
	 * 根据时间段和临时表数据汇总付款金额
	 * */
	private void processMny_Pay(Map<String, FBPuCtRptVO> vomap,
			String date_start, String date_end, String suffix) {
		String fieldname = FBPuCtRptFieldConstant.NPAYMENTMNY_ + suffix;
		StringBuffer sb = new StringBuffer("");
		sb.append(FBPuCtRptConstant.Sql_query_mny_pay_1);
		sb.append(" inner join  " + FBPuCtRptConstant.TEM_FBPUCT_ALLCT
				+ " on ct_pu.pk_ct_pu =");
		sb.append(FBPuCtRptConstant.TEM_FBPUCT_ALLCT + ".pk_ct_pu ");
		sb.append(FBPuCtRptConstant.Sql_query_mny_pay_3);
		sb.append(" where 1=1 ");
		String date = " ap_paybill.billdate ";
		if (StringUtils.isNotBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and  " + date + " < '" + date_end + "' and " + date
					+ " >= '" + date_start + "' ");
		} else if (StringUtils.isBlank(date_start)
				&& StringUtils.isNotBlank(date_end)) {
			sb.append(" and " + date + "  < '" + date_end + "' ");
		}
		sb.append(FBPuCtRptConstant.Sql_group_by);
		DataAccessUtils querytool = new DataAccessUtils();
		IRowSet rs = querytool.query(sb.toString());
		if (rs != null && rs.size() > 0) {
			String[][] objs = rs.toTwoDimensionStringArray();
			for (String[] arr : objs) {
				String paymentmny_all = arr[1];
				String pk_ct_pu = arr[0];
				if (StringUtils.isBlank(paymentmny_all)) {
					continue;
				}
				FBPuCtRptVO vo = vomap.get(pk_ct_pu);
				if (vo == null) {
					vo = new FBPuCtRptVO();
					vo.setValue(fieldname, paymentmny_all);
					vomap.put(pk_ct_pu, vo);
				} else {
					vo.setValue(fieldname, paymentmny_all);
				}
			}
		}
	}

	@Override
	protected SCMRptDataSet process(SCMRptDataSet dataset)
			throws BusinessException {
		if (!this.getScmQueryCondition().isHaveCondition()
				|| dataset.getCount() <= 0) {
			return dataset;
		}
		// 处理总包合同（销售合同或收款合同），把总包合同下的合同全部查询出来
		// 关于合同的过滤条件都在这处理
		this.queryAllCt(dataset);
		Map<String, FBPuCtRptVO> vomap = new HashMap<String, FBPuCtRptVO>();
		// 处理累计的数据
//		 this.processMny(dataset, vomap, null);
		
		//处理组织名称、项目名称、合同名称等内容
		processShowInfo(dataset);
		return dataset;
	}
	private void processShowInfo(SCMRptDataSet dataset){
		FBPuCtRptTemptableUtils.createTempTab(dataset.getDatas(),
				FBPuCtRptConstant.TEM_FBPUCT_FINAL);
		//处理组织名称
		Map<String,String> orgnamemap = new HashMap<String,String>();
		StringBuffer orgsql = new StringBuffer("");
		orgsql.append(" select distinct org_purchaseorg.name ,"+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_org from org_purchaseorg , "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" where org_purchaseorg.pk_purchaseorg = "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_org   ") ;
		DataAccessUtils tool = new DataAccessUtils();
		IRowSet orgrs = tool.query(orgsql.toString());
		if(orgrs!=null&&orgrs.size()>0){
			String[][] arr = orgrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				orgnamemap.put(ar[1],ar[0]);
			}
		}
		
		//处理项目编码和名称、合同名称
		Map<String,String> projectnamemap = new HashMap<String,String>();
		Map<String,String> projectcodemap = new HashMap<String,String>();
		StringBuffer projectsql = new StringBuffer("");
		projectsql.append(" select distinct bd_project.project_name ,bd_project.project_code,tem_fbpuct_final.pk_ct_pu from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,ct_pu_b,bd_project   where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu = ct_pu_b.pk_ct_pu and ct_pu_b.cbprojectid = bd_project.pk_project  ") ;
		IRowSet projectrs = tool.query(projectsql.toString());
		if(projectrs!=null&&projectrs.size()>0){
			String[][] arr = projectrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				projectnamemap.put(ar[2],ar[0]);
				projectcodemap.put(ar[2],ar[1]);
			}
		}
		//select distinct ct_pu.ctname,ct_pu.pk_ct_pu from tem_fbpuct_final ,ct_pu,ct_pu_b   where tem_fbpuct_final.pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.pk_ct_pu = tem_fbpuct_final.pk_ct_pu
		//处理合同名称
		Map<String,String> ctmap = new HashMap<String,String>();
		Map<String,String> versionmap = new HashMap<String,String>();
		StringBuffer ctsql = new StringBuffer("");
		ctsql.append(" select distinct  ct_pu.ctname,ct_pu.pk_ct_pu ,ct_pu.version from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,ct_pu,ct_pu_b   where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu = ct_pu.pk_ct_pu and ct_pu_b.pk_ct_pu = "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu  ") ;
		IRowSet ctrs = tool.query(ctsql.toString());
		if(ctrs!=null&&ctrs.size()>0){
			String[][] arr = ctrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				ctmap.put(ar[1],ar[0]);
				if(ar[2]!=null&&Integer.valueOf(ar[2]).intValue()>1){
					versionmap.put(ar[1],"是");
				}else{
					versionmap.put(ar[1],"否");
				}
			}
		}
		//处理合同成本
		Map<String,String> ctcbmap = new HashMap<String,String>();
		StringBuffer ctcbsql = new StringBuffer("");
		ctcbsql.append("  select distinct sum(ct_pu_b.norigmny) mny ,ct_pu_b.pk_ct_pu  from  "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+",ct_pu_b where ct_pu_b.pk_ct_pu = "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu group by ct_pu_b.pk_ct_pu ") ;
		IRowSet ctcbrs = tool.query(ctcbsql.toString());
		if(ctcbrs!=null&&ctcbrs.size()>0){
			String[][] arr = ctcbrs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				ctcbmap.put(ar[1],ar[0]);
			}
		}
		
		//最终验收/完工日期
		Map<String,String> ctysdatemap = new HashMap<String,String>();
		StringBuffer ctysdatesql = new StringBuffer("");
		ctysdatesql.append(" select  distinct max(ic_purchasein_h.dbilldate) , tem_fbpuct_final.pk_ct_pu  from tem_fbpuct_final  left join po_order_b  on po_order_b.csourceid   = tem_fbpuct_final.pk_ct_pu  left join po_arriveorder_b  on po_arriveorder_b.csourceid = po_order_b.pk_order         left join ic_purchasein_b   on ic_purchasein_b.csourcebillhid = po_arriveorder_b.pk_arriveorder   left join ic_purchasein_h on ic_purchasein_h.cgeneralhid = ic_purchasein_b.cgeneralhid  group by tem_fbpuct_final.pk_ct_pu ") ;
		IRowSet ctysdaters = tool.query(ctysdatesql.toString());
		if(ctysdaters!=null&&ctysdaters.size()>0){
			String[][] arr = ctysdaters.toTwoDimensionStringArray();
			for(String[] ar :arr){
				ctysdatemap.put(ar[1],ar[0]);
			}
		}
		
		//处理供应商名称
		Map<String,String> supmap = new HashMap<String,String>();
		StringBuffer supsql = new StringBuffer("");
		supsql.append(" select distinct bd_supplier.name,"+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".pk_ct_pu from "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+" ,bd_supplier   where "+FBPuCtRptConstant.TEM_FBPUCT_FINAL+".cvendorid = bd_supplier.pk_supplier  ") ;
		IRowSet suprs = tool.query(supsql.toString());
		if(suprs!=null&&suprs.size()>0){
			String[][] arr = suprs.toTwoDimensionStringArray();
			for(String[] ar :arr){
				supmap.put(ar[1],ar[0]);
			}
		}

		
		SCMRptColumnInfo columninfo = dataset.getColumninfo();
		Map<String, Integer> indexmap = columninfo.getColumnIndexMap();
		Object[][] arrs = dataset.getDatas();
		int num = arrs.length;
		int index_pk = indexmap.get(FBPuCtRptFieldConstant.PK_FBHT).intValue();
		
		int index_pk_org = indexmap.get(FBPuCtRptFieldConstant.CTBZT).intValue();
		//采购发票头id
		int index_org_name = indexmap.get(FBPuCtRptFieldConstant.PK_HID_FP).intValue();
		//暂时用入库单的pk来当项目名称
		int index_project_code = indexmap.get(FBPuCtRptFieldConstant.PK_HID_IN).intValue();
		int index_project_name = indexmap.get(FBPuCtRptFieldConstant.CPROJECTID).intValue();
		//暂时用入库单的bpk来当合同名称
		int index_ctname = indexmap.get(FBPuCtRptFieldConstant.PK_BID_IN).intValue();
		//
		int index_version = indexmap.get(FBPuCtRptFieldConstant.BCHANGE).intValue();
		int index_htcb = indexmap.get(FBPuCtRptFieldConstant.NHTCB).intValue();
		int index_supply = indexmap.get(FBPuCtRptFieldConstant.CVENDORID).intValue();
		//最终验收/完工日期
		int index_ctysdate = indexmap.get(FBPuCtRptFieldConstant.DACCEPTANCECHECK).intValue();
		
		//总包合同编码
		int index_zbct_code_xs = indexmap.get(FBPuCtRptFieldConstant.PK_ZBHT_CODE_XS).intValue();
		int index_zbct_code_sk = indexmap.get(FBPuCtRptFieldConstant.PK_ZBHT_CODE_SK).intValue();
		
		for (int i = 0; i < num; i++) {
			String pk = (String) arrs[i][index_pk];
			String pk_org = (String) arrs[i][index_pk_org];
			arrs[i][index_org_name] = orgnamemap.get(pk_org);
			arrs[i][index_project_code] = projectcodemap.get(pk);
			arrs[i][index_project_name] = projectnamemap.get(pk);
			arrs[i][index_ctname] = ctmap.get(pk);
			arrs[i][index_version] = versionmap.get(pk);
			arrs[i][index_htcb] = ctcbmap.get(pk);
			arrs[i][index_supply] = supmap.get(pk);
			arrs[i][index_ctysdate] = ctysdatemap.get(pk);
			String zbctcode = (String)arrs[i][index_zbct_code_xs];
			if(StringUtils.isBlank(zbctcode)){
				arrs[i][index_zbct_code_xs]=arrs[i][index_zbct_code_sk];
			}
		}
	}

	@Override
	protected SCMRptAbsScalePrcStrategy getScalePrcStrategy() {
		return null;
	}

}
