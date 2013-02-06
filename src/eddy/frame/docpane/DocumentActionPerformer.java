package eddy.frame.docpane;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentActionPerformer implements DocumentListener {

	private EditTextPane etp;
	
	public DocumentActionPerformer(EditTextPane etp) {
		this.etp = etp;
	}
	
	public void changedUpdate(DocumentEvent arg0) {
		etp.setNeddSave(true);
	}

	public void insertUpdate(DocumentEvent arg0) {
		etp.setNeddSave(true);
	}

	public void removeUpdate(DocumentEvent arg0) {
		etp.setNeddSave(true);
	}
}
