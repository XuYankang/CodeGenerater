package eddy.frame;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import resources.Resources;

import eddy.log.DebugLogger;
import eddy.log.DebugLoggerListener;

/**
 * Êä³ö´°¿Ú
 * @author Eddy
 *
 */
public class OutPutPane implements DebugLoggerListener {
	private JTabbedPane tabPane = new JTabbedPane();
	private JTextArea textArea = new JTextArea(12, 12);
	public OutPutPane() {
		DebugLogger.getLogger().addDebugLoggerListener(this);
		JScrollPane jsp = new JScrollPane(textArea);
		tabPane.setFont(Resources.getResources().getSysFont_CN());
		tabPane.add("Êä³ö", jsp);
//		tabPane.setTabComponentAt(0, new ButtonTabComponent(tabPane, 0));
	}
	
	public JTabbedPane getTabPane() {
		return this.tabPane;
	}

	public void onLog(String msg) {
		textArea.append("\n" + msg);
	}
}
