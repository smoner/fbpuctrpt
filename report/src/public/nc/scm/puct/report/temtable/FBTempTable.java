package nc.scm.puct.report.temtable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.database.DBTool;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pub.JavaType;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.Log;
import nc.vo.pubapp.pattern.pub.AssertUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class FBTempTable {
	  /**
	   * ������ʱ��
	   * 
	   * @param tablename ����
	   * @param columns ����
	   * @param columnTypes ������
	   * @return ��ʱ����
	   */
	  public String getTempTable(String tablename, String[] columns,
	      String[] columnTypes) {
	    return this.getTempTable(tablename, columns, columnTypes, null);
	  }

	  /**
	   * ������ʱ���Ҳ�������
	   * 
	   * @param tablename ����
	   * @param columns ����
	   * @param columnTypes ������
	   * @param types Ҫ������ʱ������ݵ���������
	   * @param data Ҫ������ʱ�������
	   * @return ��ʱ����
	   */
	  public String getTempTable(String tablename, String[] columns,
	      String[] columnTypes, JavaType[] types, List<List<Object>> data) {
	    this.validate(columns, columnTypes, types);

	    String name = this.getTempTable(tablename, columns, columnTypes);
	    if (data.size() == 0) {
	      return name;
	    }

	    this.insertData(columns, columnTypes, types, data, name);
	    return name;
	  }

	  /**
	   * ������ʱ��
	   * 
	   * @param tablename ����
	   * @param columns ����
	   * @param columnTypes ������
	   * @param indexColumns ��������
	   * @return ��ʱ����
	   */
	  public String getTempTable(String tablename, String[] columns,
	      String[] columnTypes, String[] indexColumns) {
	    DBTool tool = new DBTool();
	    SqlBuilder sql = new SqlBuilder();
	    int length = columns.length;
	    for (int i = 0; i < length; i++) {
	      sql.append(columns[i]);
	      sql.append(" ");
	      sql.append(columnTypes[i]);
	      sql.append(",");
	    }
	    sql.append(" ts char(19)");

	    String index = null;
	    if (indexColumns != null) {
	      SqlBuilder indexSql = new SqlBuilder();
	      for (String column : indexColumns) {
	        indexSql.append(column);
	        indexSql.append(",");
	      }
	      indexSql.deleteLastChar();
	      index = indexSql.toString();
	    }
	    String name = null;
	    nc.bs.mw.sqltrans.TempTable tt = new nc.bs.mw.sqltrans.TempTable();
	    Connection connection = null;
	    try {
	      connection = tool.getConnection();
	      name = tt.createTempTable(connection, tablename, sql.toString(), index);
	    }
	    catch (SQLException ex) {
	      ExceptionUtils.wrappException(ex);
	    }
	    finally {
	      if (connection != null) {
	        try {
	          connection.close();
	        }
	        catch (SQLException ex) {
	          Log.error(ex);
	        }
	      }
	    }

	    return name;
	  }

	  /**
	   * ������ʱ���Ҳ�������
	   * 
	   * @param tablename ����
	   * @param columns ����
	   * @param columnTypes ������
	   * @param types Ҫ������ʱ������ݵ���������
	   * @param data Ҫ������ʱ�������
	   * @return ��ʱ����
	   */
	  public String getTempTable(String tablename, String[] columns,
	      String[] columnTypes, String[] indexColumns, JavaType[] types,
	      List<List<Object>> data) {
	    this.validate(columns, columnTypes, types);

	    String name =
	        this.getTempTable(tablename, columns, columnTypes, indexColumns);
	    if (data.size() == 0) {
	      return name;
	    }

	    this.insertData(columns, columnTypes, types, data, name);
	    return name;
	  }

	  private void insertData(String[] columns, String[] columnTypes,
	      JavaType[] types, List<List<Object>> data, String name) {
	    SqlBuilder sql = new SqlBuilder();
	    sql.append(" insert into ");
	    sql.append(name);
	    sql.startParentheses();

	    SqlBuilder valueSql = new SqlBuilder();
	    int length = columns.length;
	    for (int i = 0; i < length; i++) {
	      sql.append(columns[i]);
	      sql.append(",");

	      valueSql.append("?,");
	    }
	    sql.deleteLastChar();
	    valueSql.deleteLastChar();

	    sql.endParentheses();

	    sql.append(" values");
	    sql.startParentheses();
	    sql.append(valueSql.toString());
	    sql.endParentheses();

	    // ����ֵת��Ϊ~
	    this.processNullData(columnTypes, data);

	    DataAccessUtils dao = new DataAccessUtils(false);
	    dao.update(sql.toString(), types, data);
	  }

	  private void processNullData(String[] columnTypes, List<List<Object>> data) {
	    List<Integer> list = new ArrayList<Integer>();
	    int length = columnTypes.length;
	    for (int i = 0; i < length; i++) {
	      if (columnTypes[i].equalsIgnoreCase("varchar(20)")
	          || columnTypes[i].equalsIgnoreCase("varchar(101)")
	          || columnTypes[i].equalsIgnoreCase("varchar(36)")) {
	        list.add(Integer.valueOf(i));
	      }
	    }
	    // û����Ҫת������
	    if (list.size() == 0) {
	      return;
	    }
	    for (List<Object> row : data) {
	      for (Integer index : list) {
	        Object obj = row.get(index.intValue());
	        if (obj == null) {
	          row.set(index.intValue(), "~");
	        }
	      }
	    }
	  }

	  @SuppressWarnings("null")
	  private void validate(String[] columns, String[] columnTypes, JavaType[] types) {
	    AssertUtils.assertValue(columns != null, "");
	    AssertUtils.assertValue(columnTypes != null, "");
	    AssertUtils.assertValue(types != null, "");
	    AssertUtils.assertValue(columns.length == types.length, "");
	    AssertUtils.assertValue(columns.length == columnTypes.length, "");
	  }
}
