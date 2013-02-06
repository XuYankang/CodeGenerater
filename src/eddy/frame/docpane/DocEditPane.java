package eddy.frame.docpane;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import resources.Resources;
import eddy.frame.ButtonTabComponent;

/**
 * 多面板文档编辑，可以打开多个文档，并且可以选择关闭
 * @author Eddy
 *
 */
public class DocEditPane implements DocActionListener {
	JTabbedPane tabPane = new JTabbedPane();

	public DocEditPane() {
		tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabPane.setBackground(Color.gray);
	}

	public JTabbedPane getTabPane() {
		return this.tabPane;
	}

	public void newDoc(String docName) {
		JTextPane textPane = new JTextPane();
		textPane.setFont(Resources.getResources().getSysFont_CN());
		EditTextPane etp = new EditTextPane("", docName, textPane, false);
		textPane.getDocument().addDocumentListener(new DocumentActionPerformer(etp));
		
		JScrollPane jsp = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		tabPane.add(docName, jsp);
		int i = tabPane.getTabCount();
		int index = DocController.getIndex();
		tabPane.setTabComponentAt(i - 1, new ButtonTabComponent(tabPane, index));
		tabPane.setSelectedIndex(i - 1);
		DocController.getDocController().addEditTextPane(index, etp);
	}

	public File showOpenDialog() {
		File f = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		int r = chooser.showOpenDialog(tabPane);
		if (r != JFileChooser.APPROVE_OPTION)
			return null;
		String filename = chooser.getSelectedFile().getAbsolutePath();
		f = new File(filename);
		return f;
	}

	public boolean openDoc(File f) {
		return openDoc(f, "gbk");
	}
	
	@Override
	public boolean openDoc(File f, String encode) {
		JTextPane textPane = new JTextPane();
		textPane.setFont(Resources.getResources().getSysFont_CN());
		final EditTextPane etp = new EditTextPane(f.getPath(), f.getName(), textPane, false);
		JScrollPane jsp = new JScrollPane(textPane);
		tabPane.add(f.getName(), jsp);
		int i = tabPane.getTabCount();
		int index = DocController.getIndex();
		tabPane.setTabComponentAt(i - 1, new ButtonTabComponent(tabPane, index));
		tabPane.setSelectedIndex(i - 1);
		
		DocController.getDocController().addEditTextPane(index, etp);

		final StyledDocument doc = new DefaultStyledDocument();
		doc.addDocumentListener(new DocumentActionPerformer(etp));
		textPane.setStyledDocument(doc);
		
		TextOpener opner = new TextOpener(f, etp, encode);
		SwingUtilities.invokeLater(opner);
		opner.run();
		return true;
	}

	public void saveDoc() {
		int index = tabPane.getSelectedIndex();
		if(index < 0)
			return;
		
		ButtonTabComponent btc = (ButtonTabComponent) tabPane.getTabComponentAt(index);
		EditTextPane etp = DocController.getDocController().getEditTextPane(btc.getIndex());
		etp.saveDoc();
		tabPane.setTitleAt(index, etp.getFileName());
	}
	
	@Override
	public void saveDoc(String encoding) {
		int index = tabPane.getSelectedIndex();
		if(index < 0)
			return;
		ButtonTabComponent btc = (ButtonTabComponent) tabPane.getTabComponentAt(index);
		EditTextPane etp = DocController.getDocController().getEditTextPane(btc.getIndex());
		etp.saveDoc(encoding);
		tabPane.setTitleAt(index, etp.getFileName());
	}
}
