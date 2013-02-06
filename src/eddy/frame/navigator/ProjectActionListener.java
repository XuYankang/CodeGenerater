package eddy.frame.navigator;


public interface ProjectActionListener {
	
	/**
	 * �½���Ŀ
	 */
	public void newProject();
	
	/**
	 * ����Ŀ
	 */
	public void openProject();
	
	/**
	 * ���Ŀ¼
	 */
	public void addDir();

	/**
	 * ����ļ�
	 */
	public void addFile();
	
	/**
	 * �½�JAVA��
	 */
	public void newClass();
	
	/**
	 * �½�WebӦ��ģ��
	 */
	public void newWebModel();
	
	/**
	 * ɾ���ļ�
	 */
	public void deleteFile();
	
	/**
	 * �½��ļ�
	 */
	public void newFile();

	/**
	 * ����
	 */
	public void doTest();
}
