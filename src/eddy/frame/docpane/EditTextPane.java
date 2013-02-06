package eddy.frame.docpane;

import java.awt.FontMetrics;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 * �򿪺��½����ĵ���
 * @author Eddy
 *
 */
public class EditTextPane {
	private String filePath;//�ļ�·��
	private String fileName;//�ļ�����
	private JTextPane textpane;//��ʾ��JTextPane
	private boolean neddSave = false;
	
	public EditTextPane(String filePath, String fileName, JTextPane textpane, boolean neddSave) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.textpane = textpane;
		this.neddSave = neddSave;
	}
	
	/**
	 * �ж��ļ��Ƿ���Ҫ����
	 * @return 0:���棬1��������, 2��ȡ��
	 */
	public int canClose() {
		if(!neddSave)
			return 1;
		
		int ret = JOptionPane.showConfirmDialog(textpane, "�Ƿ���Ҫ����?", "��ʾ", JOptionPane.YES_NO_CANCEL_OPTION);
		return ret;
	}
	
	/**
	 * �����ļ�
	 */
	public void saveDoc() {
		saveDoc("UTF-8");
	}
	
	public void saveDoc(String encoding) {
		if(filePath == null || filePath.equals("")) {
			JFileChooser chooser = new JFileChooser();
			int r = chooser.showSaveDialog(textpane);
			if (r != JFileChooser.APPROVE_OPTION)
				return;
			File chooseFile = chooser.getSelectedFile();
			fileName = chooseFile.getName(); 
			filePath = chooseFile.getPath();
		}
		
		Thread saver = new TextSaver(new File(filePath), textpane.getDocument(), null, encoding);
		saver.start();
		neddSave = false;
	}
	
	/**
	 * �����ĵ���Tab���Ŀ��
	 * @param charactersPerTab
	 */
	public void setTabs(int charactersPerTab) {
		FontMetrics fm = textpane.getFontMetrics(textpane.getFont());
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * charactersPerTab;
		TabStop[] tabs = new TabStop[10];
		for(int j = 0; j < tabs.length; j++) {
			int tab = j + 1;
			tabs[j] = new TabStop(tab * tabWidth);
		}
		
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = textpane.getDocument().getLength();
		textpane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public JTextPane getTextpane() {
		return textpane;
	}
	public void setTextpane(JTextPane textpane) {
		this.textpane = textpane;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isNeddSave() {
		return neddSave;
	}
	public void setNeddSave(boolean neddSave) {
		this.neddSave = neddSave;
	}
}
