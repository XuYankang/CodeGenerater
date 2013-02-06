package eddy.database;

public class DatabaseFactory {
	public static Database createDatabase(DatabaseConnection dbConnect) {
		if(dbConnect.getDriverName().contains("OracleDriver")) {
			return new DatabaseOracle(dbConnect);
		}
		else if(dbConnect.getDriverName().contains("SQLServerDriver")) {
			return new DatabaseSqlServer(dbConnect);
		} 
		else if(dbConnect.getDriverName().contains("mysql")) {
			return new DatabaseMySql(dbConnect);
		} 
		else if(dbConnect.getDriverName().contains("postgresql")) {
			return new DatabasePostgreSql(dbConnect);
		}
		return null;
	}
}
