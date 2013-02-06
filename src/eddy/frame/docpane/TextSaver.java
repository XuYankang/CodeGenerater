package eddy.frame.docpane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

/**
 * 文本文件保存,注意存储格式
 * @author Eddy
 *
 */
public class TextSaver extends Thread {
	private Document doc;
	private File f;
	private SaveOverListener saveOver = null;
	private String encoding = "";
	
	/**
	 * 默认以UTF-8格式存储
	 * @param f
	 * @param doc
	 * @param saveOver
	 */
	public TextSaver(File f, Document doc, SaveOverListener saveOver) {
		setPriority(4);
		this.f = f;
		this.doc = doc;
		this.saveOver = saveOver;
		this.encoding = "UTF-8";
	}

	public TextSaver(File f, Document doc, SaveOverListener saveOver, String encoding) {
		setPriority(4);
		this.f = f;
		this.doc = doc;
		this.saveOver = saveOver;
		this.encoding = encoding;
	}

	public void run() {
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), encoding); 
			Segment text = new Segment();
			text.setPartialReturn(true);
			int charsLeft = doc.getLength();
			int offset = 0;
			while (charsLeft > 0) {
				doc.getText(offset, Math.min(4096, charsLeft), text);
				out.write(text.array, text.offset, text.count);
				charsLeft -= text.count;
				offset += text.count;
			}
			out.flush();
			out.close();
			
			if(saveOver != null)
				saveOver.onSaveOver();
		} catch (IOException e) {
			final String msg = e.getMessage();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Could not save file: "
							+ msg, "Error saving file",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		} catch (BadLocationException e) {
			System.err.println(e.getMessage());
		}
	}
}