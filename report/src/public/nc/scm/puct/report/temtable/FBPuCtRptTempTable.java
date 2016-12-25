package nc.scm.puct.report.temtable;

import java.util.ArrayList;
import java.util.List;

import nc.bs.ic.pub.db.AbstractTempTab;
import nc.scm.puct.report.tmplate.source.FBPuCtRptConstant;
import nc.scm.puct.report.tmplate.source.FBPuCtRptFieldPreference;
import nc.vo.pub.JavaType;

public class FBPuCtRptTempTable extends AbstractTempTab {
	public FBPuCtRptTempTable(String tabname) {
		super(tabname, getFieldnames(), getFieldSqlTypes(), getFieldJavaTypes());
	}

	public static String[] getFieldnames() {
		List<String> filednames = new ArrayList<String>();
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ZBCT){
			filednames.add(field.getFieldname());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_FBCT){
			filednames.add(field.getFieldname());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ADD){
			filednames.add(field.getFieldname());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_NMNY){
			filednames.add(field.getFieldname());
		}
		return filednames.toArray(new String[0]);
	}
	public static String[] getFieldSqlTypes() {
		List<String> sqltypes = new ArrayList<String>();
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ZBCT){
			sqltypes.add(field.getSqltype());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_FBCT){
			sqltypes.add(field.getSqltype());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ADD){
			sqltypes.add(field.getSqltype());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_NMNY){
			sqltypes.add(field.getSqltype());
		}
		return sqltypes.toArray(new String[0]);
	}
	public static JavaType[] getFieldJavaTypes() {
		List<JavaType> javatypes = new ArrayList<JavaType>();
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ZBCT){
			javatypes.add(field.getFieldType());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_FBCT){
			javatypes.add(field.getFieldType());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_ADD){
			javatypes.add(field.getFieldType());
		}
		for(FBPuCtRptFieldPreference field:FBPuCtRptConstant.SMART_FIELDS_NMNY){
			javatypes.add(field.getFieldType());
		}
		return javatypes.toArray(new JavaType[0]);
	}

	public String cgeneralbid() {
		return this.aliasDotCol(0);
	}

	public String pk_temp() {
		return this.aliasDotCol(1);
	}

}
