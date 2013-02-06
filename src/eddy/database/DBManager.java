package eddy.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
	private static DBManager manager = null;
//	private Connection db_Connection = null;
	private static Integer myID = new Integer(6666999);
	
	private DBManager() {
//		if(db_Connection == null)
//			db_Connection = DBManager.getOracleConnection();
//			db_Connection = DBManager.getSqlServerConnection();
	}
	
	public static DBManager getDBManager() {
		if (manager == null) {
			synchronized (myID) {
				if (manager == null)
					manager = new DBManager();
			}
		}
		return manager;
	}
	
	private static Connection getOracleConnection() {
		Connection cn = null;
		String drivername = "oracle.jdbc.driver.OracleDriver";			
		String url = "jdbc:oracle:thin:@192.168.0.28:1521:data";
		String username = "DfkyJFile";
		String password = "123";
		try {
			Class.forName(drivername);
			cn = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
	
	
	private static Connection getSqlServerConnection() {
		Connection cn = null;
		String drivername = "com.microsoft.sqlserver.jdbc.SQLServerDriver";			
		String url = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=dfky_zbjyj;SelectMethod=cursor";
		String username = "sa";
		String password = "spring";
		try {
			Class.forName(drivername);
			cn = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
	
	private static Connection getMySqlConnection() {
		Connection cn = null;
		String drivername = "org.gjt.mm.mysql.Driver";			
		String url = "jdbc:mysql://localhost:3306/eddytest";
		String username = "root";
		String password = "4566";
		try {
			Class.forName(drivername);
			cn = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
		}
		return cn;
	}
	
	public Connection getDBConnection(DatabaseConnection dbConnect) {
		Connection cn = null;
		try {
			Class.forName(dbConnect.getDriverName());
			cn = DriverManager.getConnection(dbConnect.getUrl(), dbConnect.getUserName(), dbConnect.getPassword());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
}
