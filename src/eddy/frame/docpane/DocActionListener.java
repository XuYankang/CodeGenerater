package eddy.frame.docpane;

import java.io.File;

/**
 * �ļ���������
 * @author Eddy
 *
 */
public interface DocActionListener {
	
	/**
	 * ���ļ�
	 * @param f �ļ�File
	 * @return
	 */
	public boolean openDoc(File f);
	
	/**
	 * ���ļ�
	 * @param f
	 * @param encoding �����ʽ
	 * @return
	 */
	public boolean openDoc(File f, String encoding);
	
	/**
	 * �����ļ�
	 */
	public void saveDoc();
	
	/**
	 * �����ļ�
	 */
	public void saveDoc(String encoding);
	
	/**
	 * �½��ļ�
	 * @param title
	 */
	public void newDoc(String title);
	
	/**
	 * ��ʾ���ļ��Ի���
	 * @return
	 */
	public File showOpenDialog();
}
