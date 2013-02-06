package eddy.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import eddy.log.DebugLogger;

/**
 * 数据库DAO处理
 * @author Eddy
 *
 */
public abstract class Database {

	DatabaseConnection dbConnect;
	
	public Database(DatabaseConnection dbConnect) {
		this.dbConnect = dbConnect;
	}
	
	/**
	 * 得到数据库的所有表名
	 * @return
	 */
	public abstract ArrayList<DatabaseTable> getAllDatabaseTables();
	
	/**
	 * 得到表的字段的备注信息
	 * @return
	 */
	public abstract Map<String, String> getTableColumnsComments(String tableName);
	
	
	/**
	 * 得到表的所有列名称
	 * @param tableName
	 * @return
	 */
	public ArrayList<DatebaseColumn> getTableColumns(String tableName) {
		//FIXME 确定自增列的处理
		ArrayList<DatebaseColumn> tableColumnList = new ArrayList<DatebaseColumn>();
		try {
			Connection connection = DBManager.getDBManager().getDBConnection(dbConnect);
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("select * from " + tableName +" where 1 = 2");
			ResultSetMetaData metaData = resultSet.getMetaData();
			DatabaseMetaData dbmd = connection.getMetaData();
			
			Map<String, String> columnCommentmap = getTableColumnsComments(tableName);
			
			int numberOfColumns = metaData.getColumnCount();
			for (int column = 0; column < numberOfColumns; column++) {
				DatebaseColumn dc = new DatebaseColumn();
				dc.setColumnClassName(metaData.getColumnClassName(column + 1));
				dc.setType(metaData.getColumnType(column + 1));
				dc.setColumnLabel(metaData.getColumnLabel(column + 1));
				dc.setColumnName(metaData.getColumnName(column + 1));
				dc.setDisplaySize(metaData.getColumnDisplaySize(column + 1));
				dc.setIsNull(metaData.isNullable(column + 1));
				
				if(columnCommentmap != null) {
					String comment = columnCommentmap.get(dc.getColumnName());
					dc.setComment(comment);
				}
				
				tableColumnList.add(dc);
			}
			
			ResultSet rs2 = dbmd.getPrimaryKeys(null, null, tableName);
			while(rs2.next()) {
				String pkColumnName = rs2.getString(4);
				for(DatebaseColumn dc : tableColumnList) {
					if(dc.getColumnName().equalsIgnoreCase(pkColumnName))
						dc.setPrimaryKey(true);
				}
			}
			
			resultSet.close();
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
		return tableColumnList;
	}
	
	public static void main(String[] args) {
		try {
			DatabaseConnection dbConnect = new DatabaseConnection();
			dbConnect.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			dbConnect.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=dfkyJFile;SelectMethod=cursor");
			dbConnect.setUserName("sa");
			dbConnect.setPassword("spring");
			
//			dbConnect.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			dbConnect.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=dfky_zbjyj;SelectMethod=cursor");
//			dbConnect.setUserName("sa");
//			dbConnect.setPassword("spring");
			
			Connection connection = DBManager.getDBManager().getDBConnection(dbConnect);
			DatabaseMetaData dbmd = connection.getMetaData();
			String[] types = { "TABLE" };
			ResultSet rs = dbmd.getTables(null, "dbo", null, types);
			while (rs.next()) {
				String name = rs.getString("TABLE_NAME");
				System.out.print(name + " ");
				System.out.print(rs.getString("TABLE_CAT") + " ");
				System.out.print(rs.getString("TABLE_SCHEM") + " ");
				System.out.print(rs.getString("TABLE_NAME") + " ");
				System.out.print(rs.getString("TABLE_TYPE") + " ");
				System.out.print(rs.getString("REMARKS") + " ");
				System.out.println();
			}
			
			rs = dbmd.getSchemas();
			while (rs.next()) {
				String name = rs.getString(1);
				System.out.println("CCCC " + name);
			}
			
			rs.close();
			connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
