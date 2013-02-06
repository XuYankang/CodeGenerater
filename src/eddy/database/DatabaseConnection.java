package eddy.database;

/**
 * 数据库连接对象
 * @author Eddy
 *
 */
public class DatabaseConnection {
	private String driverName = "";
	private String url = "";
	private String userName = "";
	private String password = "";
	
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean equals(Object obj) {
		if(obj instanceof DatabaseConnection) {
			DatabaseConnection compDB = (DatabaseConnection) obj;
			if(!compDB.getDriverName().equals(this.getDriverName()))
				return false;
			
			if(!compDB.getUrl().equals(this.getUrl()))
				return false;
			
			if(!compDB.getUserName().equals(this.getUserName()))
				return false;
			
			if(!compDB.getPassword().equals(this.getPassword()))
				return false;
			
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "DB[" + url + "][" + userName + "]";
	}
}
