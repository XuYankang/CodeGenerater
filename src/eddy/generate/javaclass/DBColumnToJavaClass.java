package eddy.generate.javaclass;

import eddy.database.DatebaseColumn;

public class DBColumnToJavaClass {
	
	/**
	 * 将数据库字段转换为JAVA类，主要是类型转换
	 * @param dc
	 * @return
	 */
	public static JavaClass createJavaClass(DatebaseColumn dc) {
		JavaClass jc = new JavaClass();
		switch (dc.getType()) {
		case java.sql.Types.VARCHAR:
		case java.sql.Types.NVARCHAR:
			jc.setFieldClass("String");
			break;
		case java.sql.Types.INTEGER:
			jc.setFieldClass("Integer");
			break;
		case java.sql.Types.DECIMAL:
		case java.sql.Types.NUMERIC:
			jc.setFieldClass("java.math.BigDecimal");
			break;	
		case java.sql.Types.TIMESTAMP:
		case java.sql.Types.TIME:
			jc.setFieldClass("Date");
			break;
		case java.sql.Types.BLOB:
		case java.sql.Types.CLOB:
		case java.sql.Types.BINARY:
		case java.sql.Types.VARBINARY:
		case java.sql.Types.LONGVARBINARY:	
			jc.setFieldClass("byte[]");
			break;
		default:
			jc.setFieldClass(dc.getColumnClassName());
			break;
		}
		
		jc.setIsNull(dc.getIsNull());
		jc.setPrimaryKey(dc.isPrimaryKey());
		jc.setComment(dc.getComment());
		
		String columnName = dc.getColumnName();
		String fc = columnName.charAt(0) + "";
		fc = fc.toLowerCase();
		String fieldName = fc + columnName.substring(1); 
		jc.setFieldName(fieldName);
		return jc;
	}
}
