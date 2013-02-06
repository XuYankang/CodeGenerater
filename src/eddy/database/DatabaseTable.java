package eddy.database;

import java.util.ArrayList;

public class DatabaseTable {
	private String tableName;
	private String tableCat;
	private String tableSchem;
	private ArrayList<String> primaryKeys = new ArrayList<String>();//Ö÷¼ü
	private ArrayList<String> importKeys = new ArrayList<String>();//Íâ¼ü
	
	public void addPrimaryKeys(String columnName) {
		primaryKeys.add(columnName);
	}
	
	public void addImportKeys(String columnName) {
		importKeys.add(columnName);
	}
	
	public ArrayList<String> getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(ArrayList<String> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public ArrayList<String> getImportKeys() {
		return importKeys;
	}
	public void setImportKeys(ArrayList<String> importKeys) {
		this.importKeys = importKeys;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableCat() {
		return tableCat;
	}
	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}
	public String getTableSchem() {
		return tableSchem;
	}
	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}
	
	public String toString() {
		return this.tableName;
	}
}
