package eddy.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eddy.log.DebugLogger;


public class DatabaseMySql extends Database{

	public DatabaseMySql(DatabaseConnection dbConnect) {
		super(dbConnect);
	}

	public ArrayList<DatabaseTable> getAllDatabaseTables() {
		ArrayList<DatabaseTable> databaseTableList = new ArrayList<DatabaseTable>();
		try {
			Connection connection = DBManager.getDBManager().getDBConnection(dbConnect);
			DatabaseMetaData dbmd = connection.getMetaData();
			String[] types = { "TABLE" };
			ResultSet rset = dbmd.getTables(null, null, null, types);
			while (rset.next()) {
				String name = rset.getString("TABLE_NAME");
				DatabaseTable dt = new DatabaseTable();
				dt.setTableName(name);
				
				ResultSet rs2 = dbmd.getPrimaryKeys(null, null, name);
				while (rs2.next()) {
					String columnName = rs2.getString(4);
					dt.addPrimaryKeys(columnName);
				}
				rs2.close();
				databaseTableList.add(dt);
			}
			
			rset.close();
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
		return databaseTableList;
	}


	public Map<String, String> getTableColumnsComments(String tableName) {
		try {
			Map<String, String> commentsMap = new HashMap<String, String>();
			Connection connection = DBManager.getDBManager().getDBConnection(dbConnect);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("Select COLUMN_NAME , DATA_TYPE , COLUMN_COMMENT from INFORMATION_SCHEMA.COLUMNS where table_name = '" + tableName + "'");
			while(rs.next()) {
				String COLUMN_NAME = rs.getString("COLUMN_NAME");
				String comment = rs.getString("COLUMN_COMMENT");
				commentsMap.put(COLUMN_NAME, comment);
			}
			
			rs.close();
			statement.close();
			connection.close();
			
			return commentsMap;
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
			return null;
		}
		
	}
}
