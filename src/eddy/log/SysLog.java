package eddy.log;

/**
 * ϵͳ��־��<br>
 * ��¼ϵͳ��صĲ���
 * @author eddy
 *
 */
public class SysLog extends Log{
	private String sql;//ϵͳִ�е�SQL���
	private String describle;//���������������¼���˳�����ѯ��
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
