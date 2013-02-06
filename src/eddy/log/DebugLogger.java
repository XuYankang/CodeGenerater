package eddy.log;

import java.util.Vector;


public class DebugLogger {
	private static DebugLogger  logger = null;
	private Vector<DebugLoggerListener> list = new Vector<DebugLoggerListener>();
	
	public static DebugLogger getLogger() {
		if(logger == null)
			logger = new DebugLogger();
		return logger;
	}

	public void addDebugLoggerListener(DebugLoggerListener l) {
		list.add(l);
	}
 
	public void log(String s) {
		for(int i = 0; i < list.size(); i++) {
			DebugLoggerListener l = (DebugLoggerListener) list.get(i);
			l.onLog(s);
		}
	}
	
	public void err(Exception e) {
		StringBuffer errorBuf = new StringBuffer();
		errorBuf.append(e.getClass() + ": " + e.getMessage());
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			errorBuf.append("\n  at " + trace[i]);
		}
		for(int i = 0; i < list.size(); i++) {
			DebugLoggerListener l = (DebugLoggerListener) list.get(i);
			l.onLog(errorBuf.toString());
		}
	}
}
