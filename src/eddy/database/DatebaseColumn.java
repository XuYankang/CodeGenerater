package eddy.database;

public class DatebaseColumn {
	private int type = 0;
	private int displaySize = 0;
	private String columnClassName = "";
	private String columnName = "";
	
	private String columnTypeName = "";
	private String columnLabel = "";
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

	public String toString() {
//		String s = "[";
//		s += "columnLabel:" + columnLabel + ",";
//		s += "columnName:" + columnName + ",";
//		s += "columnTypeName:" + columnTypeName + ",";
//		s += "type:" + type + ",";
//		s += "displaySize:" + displaySize + ",";
//		s += "columnClassName:" + columnClassName;
//		s += "]";
		return columnName;
	}
	
	public String getColumnClassName() {
		return columnClassName;
	}
	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}
	public int getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	public String getColumnLabel() {
		return columnLabel;
	}
	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
