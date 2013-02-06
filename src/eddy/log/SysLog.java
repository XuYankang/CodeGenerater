package eddy.log;

/**
 * 系统日志类<br>
 * 记录系统相关的操作
 * @author eddy
 *
 */
public class SysLog extends Log{
	private String sql;//系统执行的SQL语句
	private String describle;//操作的描述，如登录，退出，查询等
	public String getDescrible() {
		return describle;
	}
	public void setDescrible(String describle) {
		this.describle = describle;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
}
