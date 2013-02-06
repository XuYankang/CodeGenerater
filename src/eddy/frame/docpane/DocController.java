package eddy.frame.docpane;

import java.util.HashMap;
import java.util.Map;

/**
 * �ĵ��������ģ��򿪵������ĵ����ɸÿ������Ŀ���
 * @author Eddy
 *
 */
public class DocController {
	
	private static int index = 0;//�ĵ�ID��
	
	private static DocActionListener docActionner = null;//�ĵ�����ִ����
	
	/**
	 * �õ��ĵ�ִ����
	 * @return
	 */
	public static DocActionListener getDocActionner() {
		return docActionner;
	}

	/**
	 * �����ĵ�ִ��������ɴ��ļ����½��ļ�����ʾ�ļ��Ի���Ȳ���
	 * @param docActionner
	 */
	public static void setDocActionner(DocActionListener docActionner) {
		DocController.docActionner = docActionner;
	}

	/**
	 * �õ�ID���
	 * @return
	 */
	public static int getIndex() {
		return index++;
	}

	private Map<Integer, EditTextPane> editPaneMap = new HashMap<Integer, EditTextPane>();
	
	/**
	 * �õ��ĵ�MAP
	 * @return
	 */
	public Map<Integer, EditTextPane> getEditPaneMap() {
		return editPaneMap;
	}

	private static DocController controller = null;
	
	private DocController() {
	}
	
	/**
	 * �õ�������
	 * @return
	 */
	public static DocController getDocController() {
		if(controller == null)
			controller = new DocController();
		return controller;
	}
	
	/**
	 * �õ��ĵ�
	 * @param index ���
	 * @return EditTextPane
	 */
	public EditTextPane getEditTextPane(int index) {
		return editPaneMap.get(new Integer(index));
	}
	
	/**
	 * ����ĵ�
	 * @param index ���
	 * @param etc EditTextPane
	 */
	public void addEditTextPane(int index, EditTextPane etc) {
		editPaneMap.put(new Integer(index), etc);
	}
	
	/**
	 * ɾ���ĵ�
	 * @param etc EditTextPane
	 */
	public void removeEditTextPane(EditTextPane etc) {
		editPaneMap.remove(etc);
	}
}
