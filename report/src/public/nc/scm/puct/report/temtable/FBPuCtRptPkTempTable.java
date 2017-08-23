package nc.scm.puct.report.temtable;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.JavaType;

public class FBPuCtRptPkTempTable extends FBPuCtRptAbstractTempTable {
	public FBPuCtRptPkTempTable(String tabname) {
		super(tabname, getFieldnames(), getFieldSqlTypes(),getIndexFieldnames(), getFieldJavaTypes());
	}

	public static String[] getIndexFieldnames() {
		List<String> filednames = new ArrayList<String>();
		filednames.add("pk_ct_pu");
		filednames.add("bid");
		return filednames.toArray(new String[0]);
	}

	public static String[] getFieldnames() {
		List<String> filednames = new ArrayList<String>();
		filednames.add("pk_ct_pu");
		filednames.add("bid");
		return filednames.toArray(new String[0]);
	}
	public static String[] getFieldSqlTypes() {
		List<String> sqltypes = new ArrayList<String>();
		sqltypes.add("varchar2(200)");
		sqltypes.add("varchar2(200)");
		return sqltypes.toArray(new String[0]);
	}
	public static JavaType[] getFieldJavaTypes() {
		List<JavaType> javatypes = new ArrayList<JavaType>();
		javatypes.add(JavaType.String);
		javatypes.add(JavaType.String);
		return javatypes.toArray(new JavaType[0]);
	}

	public String pk_ct_pu() {
		return this.aliasDotCol(0);
	}

	public String bid() {
		return this.aliasDotCol(1);
	}

}
