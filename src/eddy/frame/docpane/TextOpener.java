package eddy.frame.docpane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import eddy.log.DebugLogger;

public class TextOpener implements Runnable {
	private EditTextPane etp;
	private File f;
	private String encoding = "";
	
	public TextOpener(File f, EditTextPane etp) {
		this.f = f;
		this.etp = etp;
		this.encoding = "GBK";
	}

	public TextOpener(File f, EditTextPane etp, String encoding) {
		this.f = f;
		this.etp = etp;
		this.encoding = encoding;
	}
	
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
			etp.getTextpane().read(br, f.getName());
			br.close();
			DebugLogger.getLogger().log("Loaded: " + f.getName());
			etp.setTabs(4);
			etp.setNeddSave(false);
		} catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().err(e);
		}
	}
}
