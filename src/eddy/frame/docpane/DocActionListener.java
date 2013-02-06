package eddy.frame.docpane;

import java.io.File;

/**
 * 文件基本操作
 * @author Eddy
 *
 */
public interface DocActionListener {
	
	/**
	 * 打开文件
	 * @param f 文件File
	 * @return
	 */
	public boolean openDoc(File f);
	
	/**
	 * 打开文件
	 * @param f
	 * @param encoding 编码格式
	 * @return
	 */
	public boolean openDoc(File f, String encoding);
	
	/**
	 * 保存文件
	 */
	public void saveDoc();
	
	/**
	 * 保存文件
	 */
	public void saveDoc(String encoding);
	
	/**
	 * 新建文件
	 * @param title
	 */
	public void newDoc(String title);
	
	/**
	 * 显示打开文件对话框
	 * @return
	 */
	public File showOpenDialog();
}
