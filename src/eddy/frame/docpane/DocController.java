package eddy.frame.docpane;

import java.util.HashMap;
import java.util.Map;

/**
 * 文档控制中心，打开的所有文档都由该控制中心控制
 * @author Eddy
 *
 */
public class DocController {
	
	private static int index = 1;//文档ID号
	
	private static DocActionListener docActionner = null;//文档操作执行器
	
	/**
	 * 得到文档执行器
	 * @return
	 */
	public static DocActionListener getDocActionner() {
		return docActionner;
	}

	/**
	 * 设置文档执行器，完成打开文件、新建文件、显示文件对话框等操作
	 * @param docActionner
	 */
	public static void setDocActionner(DocActionListener docActionner) {
		DocController.docActionner = docActionner;
	}

	/**
	 * 得到ID序号
	 * @return
	 */
	public static int getIndex() {
		return index++;
	}

	private Map<Integer, EditTextPane> editPaneMap = new HashMap<Integer, EditTextPane>();
	private Map<String, Integer> openFileIndexMap = new HashMap<String, Integer>();
	
	/**
	 * 得到文档MAP
	 * @return
	 */
	public Map<Integer, EditTextPane> getEditPaneMap() {
		return editPaneMap;
	}
	
	public Map<String, Integer> getFileIndexMap() {
		return openFileIndexMap;
	}

	private static DocController controller = null;
	
	private DocController() {
	}
	
	/**
	 * 得到控制器
	 * @return
	 */
	public static DocController getDocController() {
		if(controller == null)
			controller = new DocController();
		return controller;
	}
	
	/**
	 * 得到文档
	 * @param index 序号
	 * @return EditTextPane
	 */
	public EditTextPane getEditTextPane(int index) {
		return editPaneMap.get(new Integer(index));
	}
	
	public int getFileIndex(String filePath) {
		Integer index = openFileIndexMap.get(filePath);
		return index == null ? 0 : index;
	}
	
	/**
	 * 添加文档
	 * @param index 序号
	 * @param etc EditTextPane
	 */
	public void addEditTextPane(int index, EditTextPane etc) {
		openFileIndexMap.put(etc.getFilePath(), index);
		editPaneMap.put(new Integer(index), etc);
	}
	
	/**
	 * 删除文档
	 * @param etc EditTextPane
	 */
	public void removeEditTextPane(EditTextPane etc) {
		openFileIndexMap.remove(etc.getFilePath());
		editPaneMap.remove(etc);
	}
}
