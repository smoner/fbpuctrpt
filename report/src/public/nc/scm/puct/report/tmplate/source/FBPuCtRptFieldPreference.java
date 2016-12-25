package nc.scm.puct.report.tmplate.source;

import nc.vo.pub.JavaType;

public class FBPuCtRptFieldPreference {
	  private String fieldChnName;

	  private String fieldname;

	  private JavaType fieldType;
	  
	  private String sqltype;

	  public FBPuCtRptFieldPreference(String fieldname, String fieldChnName,
	      JavaType javaType,String sqltype) {
	    this.fieldname = fieldname;
	    this.fieldChnName = fieldChnName;
	    this.fieldType = javaType;
	    this.sqltype = sqltype;
	  }

	  public String getFieldChnName() {
	    return this.fieldChnName;
	  }

	  public String getSqltype() {
		return sqltype;
	}

	public String getFieldname() {
	    return this.fieldname;
	  }

	  public JavaType getFieldType() {
	    return this.fieldType;
	  }


}
