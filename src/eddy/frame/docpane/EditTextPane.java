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
 * 打开和新建的文档类
 * @author Eddy
 *
 */
public class EditTextPane {
	private String filePath;//文件路径
	private String fileName;//文件名称
	private JTextPane textpane;//显示的JTextPane
	private boolean neddSave = false;
	
	public EditTextPane(String filePath, String fileName, JTextPane textpane, boolean neddSave) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.textpane = textpane;
		this.neddSave = neddSave;
	}
	
	/**
	 * 判断文件是否需要保存
	 * @return 0:保存，1：不保存, 2：取消
	 */
	public int canClose() {
		if(!neddSave)
			return 1;
		
		int ret = JOptionPane.showConfirmDialog(textpane, "是否需要保存?", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
		return ret;
	}
	
	/**
	 * 保存文件
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
	 * 设置文档中Tab键的宽度
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
