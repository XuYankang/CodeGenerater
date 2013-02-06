package eddy.generate.javaclass;

public class JavaClass {
	private String fieldClass = "";
	private String fieldName = "";
	private String comment = "";
	private int isNull = 0;
	private boolean isPrimaryKey = false;
	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public int getIsNull() {
		return isNull;
	}
	public void setIsNull(int isNull) {
		this.isNull = isNull;
	}
	public String getFieldClass() {
		return fieldClass;
	}
	public void setFieldClass(String fieldClass) {
		this.fieldClass = fieldClass;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
