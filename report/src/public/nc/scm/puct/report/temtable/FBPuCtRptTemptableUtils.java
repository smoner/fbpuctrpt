package nc.scm.puct.report.temtable;

import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pubapp.pattern.data.IRowSet;

import org.apache.commons.lang.StringUtils;

public class FBPuCtRptTemptableUtils{
	  /** 创建临时表并填充数据 */
	  public static FBPuCtRptTempTable createTempTab(Object[][] objs,String tabname) {
		  FBPuCtRptTempTable temp = new FBPuCtRptTempTable(tabname);
	    temp.setAlias(tabname);
	    temp.insertData(objs);
	    return temp;
	  }
	  /** 创建合同主键和各单据表体主键的临时表并填充数据 */
	  public static FBPuCtRptPkTempTable createPkTempTab(Object[][] objs,String tabname) {
		  FBPuCtRptPkTempTable temp = new FBPuCtRptPkTempTable(tabname);
		  temp.setAlias(tabname);
		  temp.insertData(objs);
		  return temp;
	  }

	  public String[][] getDataBySql(String qrySql) {
	    if (StringUtils.isBlank(qrySql)) {
	      return null;
	    }
	    DataAccessUtils util = new DataAccessUtils();
	    IRowSet rs = util.query(qrySql);
	    if (null != rs) {
	      return rs.toTwoDimensionStringArray();
	    }
	    return null;
	  }
	}