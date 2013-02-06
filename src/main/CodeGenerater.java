package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import eddy.frame.MainFrame;

/**
 * 代码生成器入口
 * @author Eddy
 *
 */
public class CodeGenerater {
	
	private static MainFrame myframe = new MainFrame();
	
	public static MainFrame getMainFrame() {
		return myframe;
	}
	
	private CodeGenerater() {
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				myframe.setSize(900,700);
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				myframe.setLocation(d.width/2 - myframe.getWidth()/2, d.height/2 - myframe.getHeight()/2);
				myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				myframe.setVisible(true);
			}
		});
	}
}
