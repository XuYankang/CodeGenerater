package eddy.frame.docpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import eddy.frame.navigator.ProjectActionListener;
import eddy.log.DebugLogger;

public class DocActionPerformer implements ActionListener {

	private DocActionListener docActioner = null;
	private ProjectActionListener projectActioner = null;
	
	public DocActionPerformer(DocActionListener docActioner, ProjectActionListener projectActioner) {
		this.docActioner = docActioner;
		this.projectActioner = projectActioner;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("newDoc")) {
			docActioner.newDoc("ÐÂÎÄµµ");
		}
		else if(e.getActionCommand().equals("openDoc")) {
			File f = docActioner.showOpenDialog();
			if(f != null)
				docActioner.openDoc(f);
		}
		else if(e.getActionCommand().equals("saveDoc")) {
			docActioner.saveDoc();
		}
		else if(e.getActionCommand().equals("saveDocUTF8")) {
			docActioner.saveDoc("UTF-8");
		}
		else if(e.getActionCommand().equals("newProject")) {
			projectActioner.newProject();
		}
		else if(e.getActionCommand().equals("openProject")) {
			projectActioner.openProject();
		}
		else if(e.getActionCommand().equals("test")) {
			projectActioner.doTest();
		}
		
		DebugLogger.getLogger().log(e.getActionCommand());
	}
}
