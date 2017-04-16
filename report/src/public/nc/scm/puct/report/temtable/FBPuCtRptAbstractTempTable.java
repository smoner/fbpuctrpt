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
	   * AbstractTempTab �Ĺ�����
	   * 
	   * @param name
	   *          ����
	   * @param columns
	   *          ����
	   * @param columnTypes
	   *          �����ͣ���char(20)
	   * @param javaTypes
	   *          �磺JavaType.String
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
	   * �����������������ش���������ֶ����ơ�
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @param i
	   *          �кţ���0��ʼ
	   * @return �磺tt1.id1�����û�б�������ֱ�ӷ��ء�����.������
	   *         <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 ����01:38:30
	   */
	  public String aliasDotCol(int i) {
	    if (StringUtil.isEmptyWithTrim(this.alias)) {
	      return this.tabDotCol(i);
	    }

	    return this.alias + "." + this.columns[i];
	  }

	  /**
	   * ����������������¡���ֻ࣬���ڸı��������δ�����µ����ݿ��Ҳ�����ԭ������
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @param newAlias
	   *          ��¡������ʱ��ı���
	   * @return <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-10-19 ����04:47:57
	   */
	  public AbstractTempTab cloneWithAlias(String newAlias) {
	    AbstractTempTab tt = null;
	    try {
	      tt = (AbstractTempTab) this.clone();
	    }
	    catch (CloneNotSupportedException e) {
	      // ��־�쳣
	      ExceptionUtils.wrappException(e);
	      return null;
	    }
	    tt.setAlias(newAlias);
	    return tt;
	  }

	  /**
	   * ������������������������
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @param i
	   *          �кţ���0��ʼ
	   * @return <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 ����01:39:59
	   */
	  public String col(int i) {
	    return this.columns[i];
	  }

	  /**
	   * @return alias �����
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
	   * @return name ��ʱ����
	   */
	  public String getName() {
	    return this.name;
	  }

	  /**
	   * ������������������ʱ���в������ݡ�
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @param data
	   *          ��ά�б�����ݱ�
	   *          <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 ����04:36:02
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
	   * ������������������ʱ���в������ݡ�
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @param data
	   *          ��ά�б�����ݱ�
	   *          <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 ����04:36:49
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
	   * ���������������õ�"���� ����"�ִ���
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @return <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 ����04:39:14
	   */
	  public String nameAsAlais() {
	    if (StringUtil.isEmptyWithTrim(this.alias)) {
	      return this.name + " " + this.name;
	    }

	    return this.name + " " + this.alias;
	  }

	  /**
	   * @param alias
	   *          Ҫ���õ� alias
	   */
	  public void setAlias(String alias) {
	    this.alias = alias;
	  }

	  public void setColumns(String[] columns) {
	    this.columns = columns;
	  }

	  /**
	   * �����������������ش��������ֶ����ơ�
	   * <p>
	   * <b>����˵��</b>
	   * 
	   * @param i
	   *          �кţ���0��ʼ
	   * @return �磺tmp_001.id1
	   *         <p>
	   * @since 6.0
	   * @author zhaoyha
	   * @time 2010-7-9 ����01:38:30
	   */
	  public String tabDotCol(int i) {
	    return this.name + "." + this.columns[i];
	  }

}
