package nc.scm.puct.report.temtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.ic.pub.db.AbstractTempTab;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.JavaType;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

public class FBPuCtRptAbstractTempTable {
	  private String alias;

	  private String[] columns;

	  private String[] columnTypes;

	  private JavaType[] javaTypes;

	  private String name;

	  /**
	   * AbstractTempTab 的构造子
	   * 
	   * @param name
	   *          表名
	   * @param columns
	   *          列名
	   * @param columnTypes
	   *          列类型，如char(20)
	   * @param javaTypes
	   *          如：JavaType.String
	   */
	  public FBPuCtRptAbstractTempTable(
	      String name, String[] columns, String[] columnTypes, JavaType[] javaTypes) {
	    this.name = name;
	    this.columns = columns;
	    this.columnTypes = columnTypes;
	    this.javaTypes = javaTypes;
	    FBTempTable tt = new FBTempTable();
	    this.name =
	        tt.getTempTable(name, columns, columnTypes, javaTypes,
	            new ArrayList<List<Object>>());
	  }

	  /**
	   * 方法功能描述：返回带表别名的字段名称。
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param i
	   *          列号，从0开始
	   * @return 如：tt1.id1，如果没有别名，则直接返回”表名.列名“
	   *         <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 下午01:38:30
	   */
	  public String aliasDotCol(int i) {
	    if (StringUtil.isEmptyWithTrim(this.alias)) {
	      return this.tabDotCol(i);
	    }

	    return this.alias + "." + this.columns[i];
	  }

	  /**
	   * 方法功能描述：克隆此类，只用于改变别名，并未生成新的数据库表，也不清除原有数据
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param newAlias
	   *          克隆出的临时表的别名
	   * @return <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-10-19 下午04:47:57
	   */
	  public AbstractTempTab cloneWithAlias(String newAlias) {
	    AbstractTempTab tt = null;
	    try {
	      tt = (AbstractTempTab) this.clone();
	    }
	    catch (CloneNotSupportedException e) {
	      // 日志异常
	      ExceptionUtils.wrappException(e);
	      return null;
	    }
	    tt.setAlias(newAlias);
	    return tt;
	  }

	  /**
	   * 方法功能描述：返回列名。
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param i
	   *          列号，从0开始
	   * @return <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 下午01:39:59
	   */
	  public String col(int i) {
	    return this.columns[i];
	  }

	  /**
	   * @return alias 表别名
	   */
	  public String getAlias() {
	    return this.alias;
	  }

	  /**
	   * @return columns
	   */
	  public String[] getColumns() {
	    return this.columns;
	  }

	  /**
	   * @return columnTypes
	   */
	  public String[] getColumnTypes() {
	    return this.columnTypes;
	  }

	  /**
	   * @return name 临时表名
	   */
	  public String getName() {
	    return this.name;
	  }

	  /**
	   * 方法功能描述：向临时表中插入数据。
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param data
	   *          二维列表的数据表
	   *          <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 下午04:36:02
	   */
	  public void insertData(List<List<Object>> data) {
	    if (CollectionUtils.isEmpty(data)) {
	      return;
	    }
	    FBTempTable tt = new FBTempTable();
	    this.name =
	        tt.getTempTable(this.name, this.columns, this.columnTypes,
	            this.javaTypes, data);
	  }

	  /**
	   * 方法功能描述：向临时表中插入数据。
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param data
	   *          二维列表的数据表
	   *          <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 下午04:36:49
	   */
	  public void insertData(Object[][] data) {
	    if (ArrayUtils.isEmpty(data)) {
	      return;
	    }
	    List<List<Object>> ldata = new ArrayList<List<Object>>();
	    for (Object[] row : data) {
	      ldata.add(Arrays.asList(row));
	    }
	    this.insertData(ldata);
	  }

	  /**
	   * 方法功能描述：得到"表名 别名"字串。
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @return <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 下午04:39:14
	   */
	  public String nameAsAlais() {
	    if (StringUtil.isEmptyWithTrim(this.alias)) {
	      return this.name + " " + this.name;
	    }

	    return this.name + " " + this.alias;
	  }

	  /**
	   * @param alias
	   *          要设置的 alias
	   */
	  public void setAlias(String alias) {
	    this.alias = alias;
	  }

	  public void setColumns(String[] columns) {
	    this.columns = columns;
	  }

	  /**
	   * 方法功能描述：返回带表名的字段名称。
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param i
	   *          列号，从0开始
	   * @return 如：tmp_001.id1
	   *         <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 下午01:38:30
	   */
	  public String tabDotCol(int i) {
	    return this.name + "." + this.columns[i];
	  }

}
