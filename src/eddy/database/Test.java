package eddy.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	DatabaseConnection dbConnect = new DatabaseConnection();
	
	public Test() {
//		dbConnect.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		dbConnect.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=dfky_zbjyj;SelectMethod=cursor");
//		dbConnect.setUserName("sa");
//		dbConnect.setPassword("spring");
		
		dbConnect.setDriverName("com.mysql.jdbc.Driver");
		dbConnect.setUrl("jdbc:mysql://127.0.0.1:3306/gjsh");
		dbConnect.setUserName("root");
		dbConnect.setPassword("root");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();
//		t.testConnection();
		
		Database db = new DatabaseMySql(t.dbConnect);
		db.getAllDatabaseTables();
		
//		try {
//			DatabaseConnects dbConnect = new DatabaseConnects();
//			dbConnect.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			dbConnect.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=dfky_zbjyj;SelectMethod=cursor");
//			dbConnect.setUserName("sa");
//			dbConnect.setPassword("spring");
//			Connection connection = DBManager.getDBManager().getDBConnection(dbConnect);
//			DatabaseMetaData dbmd = connection.getMetaData();
//			
//			ResultSet rs = dbmd.getPrimaryKeys(null, null, "Files");
//			
//			while (rs.next()) {
//				System.out.println(rs);
//				String s = rs.getString(1);
//				System.out.println(s);
//				s = rs.getString(2);
//				System.out.println(s);
//				s = rs.getString(3);
//				System.out.println(s);
//				s = rs.getString(4);
//				System.out.println(s);
//				s = rs.getString(5);
//				System.out.println(s);
//				s = rs.getString(6);
//				System.out.println(s);
//			}
//			
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		

//		
//		ArrayList<DatebaseColumn> columnList = db.getTableColumns("Files");
//		String s = ClassGenerator.generateClassFromDateBaseColumns("Files", columnList);
//		System.out.println(s);
		
	}

	public void testConnection() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBManager.getDBManager().getDBConnection(dbConnect);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from users");
			while(rs.next()) {
				String s = rs.getString(1);
				System.out.println(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
}
