package eddy.log;

import java.util.Date;

/**
 * 日志类
 * @author eddy
 *
 */
public class Log {
	private String who;
	private Date when;
	private String where;
	private String doWhat;
	private String type;//类型，存数据库或文件等
	private int level;//日志等级
	public String getDoWhat() {
		return doWhat;
	}
	public void setDoWhat(String doWhat) {
		this.doWhat = doWhat;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getWhen() {
		return when;
	}
	public void setWhen(Date when) {
		this.when = when;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getWho() {
		return who;
	}
	public void setWho(String who) {
		this.who = who;
	}
	
}
