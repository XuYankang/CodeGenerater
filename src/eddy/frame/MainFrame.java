package eddy.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import resources.Resources;
import eddy.frame.docpane.DocActionPerformer;
import eddy.frame.docpane.DocController;
import eddy.frame.docpane.DocEditPane;
import eddy.frame.navigator.NavigatorPane;

/**
 * 程度主窗体
 * @author Eddy
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private NavigatorPane navigatorPane = null;
	private DocEditPane editPane = null;
	private OutPutPane outPane = null;
	private DocActionPerformer docPerformaer;
	private Vector<SupportedLaF> supportedLaFs = new Vector<SupportedLaF>();

	static class SupportedLaF {
		String name;
		String lafClassName;

		SupportedLaF(String name, String lafClassName) {
			this.name = name;
			this.lafClassName = lafClassName;
		}
	}
	
	public MainFrame() {
		super("JAVA CodeGenerater");
		loadLookAndFeel();
		navigatorPane = new NavigatorPane();
		editPane = new DocEditPane();
		DocController.setDocActionner(editPane);
		outPane = new OutPutPane();
		docPerformaer = new DocActionPerformer(editPane, navigatorPane.getProjectTree());
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		JToolBar toolBar = null;
		try {
			toolBar = createToolBar();
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(toolBar != null) {
			pane.add(toolBar, BorderLayout.NORTH);
		}

		setJMenuBar(createMenuBar());
		
		JSplitPane jsplitO = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editPane.getTabPane(), outPane.getTabPane());
		jsplitO.setContinuousLayout(true);
		jsplitO.setOneTouchExpandable(true);
		jsplitO.setDividerLocation(508);
		jsplitO.setDividerSize(8);
		
		JSplitPane jsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				navigatorPane.getTabPane(), jsplitO);
		jsplit.setContinuousLayout(true);
		jsplit.setOneTouchExpandable(true);
		jsplit.setDividerLocation(230);
		jsplit.setDividerSize(8);
		pane.add(jsplit, BorderLayout.CENTER);
		setContentPane(pane);
		
		setUI();
		
//		addComponentListener(new WindowSnapper());
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {

	}
	
	/**
	 * 加载外观
	 */
	private void loadLookAndFeel() {
		UIManager.LookAndFeelInfo[] installedLafs = UIManager.getInstalledLookAndFeels();
		for (UIManager.LookAndFeelInfo lafInfo : installedLafs) {
			try {
				Class<?> lnfClass = Class.forName(lafInfo.getClassName());
				LookAndFeel laf = (LookAndFeel) lnfClass.newInstance();
				if(laf.isSupportedLookAndFeel()) {
					String name = lafInfo.getName();
					supportedLaFs.add(new SupportedLaF(name, lafInfo.getClassName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 设置外观
	 */
	private void setUI() {
		try {
			Class<?> lnfClass = Class.forName("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			if(lnfClass == null) 
				return;
			
			LookAndFeel laf = (LookAndFeel) lnfClass.newInstance();
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(MainFrame.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("look & feel");
		menu.setFont(Resources.getResources().getSysFont_CN());
		
		ButtonGroup lafGroup = new ButtonGroup();
		for(SupportedLaF slaf : supportedLaFs) {
			JRadioButtonMenuItem lafChk = new JRadioButtonMenuItem(slaf.name);
			lafChk.setActionCommand(slaf.lafClassName);
			lafChk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String name = e.getActionCommand();
					try {
						Class<?> lnfClass = Class.forName(name);
						LookAndFeel laf = (LookAndFeel) lnfClass.newInstance();
						UIManager.setLookAndFeel(laf);
						SwingUtilities.updateComponentTreeUI(MainFrame.this);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			lafGroup.add(lafChk);
			menu.add(lafChk);
		}
		menuBar.add(menu);
		
		menu = new JMenu("File");
		menu.setFont(Resources.getResources().getSysFont_CN());
		try {
			menu.add(createNewMenu());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		JMenuItem item = new JMenuItem("Open");
		item.setFont(Resources.getResources().getSysFont_CN());
		item.setIcon(new ImageIcon("resources/open.gif"));
		item.setActionCommand("openDoc");
		item.addActionListener(docPerformaer);
		menu.add(item);
		
		item = new JMenuItem("Save");
		item.setFont(Resources.getResources().getSysFont_CN());
		item.setIcon(new ImageIcon("resources/save.gif"));
		item.setActionCommand("saveDoc");
		item.addActionListener(docPerformaer);
		menu.add(item);
		
		item = new JMenuItem("Save as UTF-8");
		item.setFont(Resources.getResources().getSysFont_CN());
		item.setIcon(new ImageIcon("resources/save.gif"));
		item.setActionCommand("saveDocUTF8");
		item.addActionListener(docPerformaer);
		menu.add(item);
		
		menuBar.add(menu);
		
		menu = new JMenu("Project");
		item = new JMenuItem("Open Project...");
		item.setIcon(new ImageIcon("resources/open.gif"));
		item.setActionCommand("openProject");
		item.addActionListener(docPerformaer);
		menu.add(item);
		menuBar.add(menu);
		
		menu = new JMenu("View");
		menuBar.add(menu);
		
		menu = new JMenu("Database");
		menu.setFont(Resources.getResources().getSysFont_CN());
		
		
		menuBar.add(menu);
		
		return menuBar;
	}

	public JMenu createNewMenu() throws Exception {
		JMenu menu = new JMenu("new");
		menu.setFont(Resources.getResources().getSysFont_CN());
		
		JMenuItem item = new JMenuItem("File");
		item.setFont(Resources.getResources().getSysFont_CN());
		item.setIcon(new ImageIcon(ImageIO.read(Resources.getResources().getResourceStream("images/new.gif"))));
		item.setActionCommand("newDoc");
		item.addActionListener(docPerformaer);
		menu.add(item);
		
		item = new JMenuItem("project");
		item.setFont(Resources.getResources().getSysFont_CN());
		item.setIcon(new ImageIcon(ImageIO.read(Resources.getResources().getResourceStream("images/project.gif"))));
		item.setActionCommand("newProject");
		item.addActionListener(docPerformaer);
		menu.add(item);
		
		return menu;
	}
	
	public JToolBar createToolBar() throws Exception {
		JToolBar toolBar = new JToolBar();
		BufferedImage image = ImageIO.read(Resources.getResources().getResourceStream("images/new.gif"));
		JButton btn = new JButton(new ImageIcon(image));
		btn.setActionCommand("newDoc");
		btn.addActionListener(docPerformaer);
		toolBar.add(btn);
		
		image = ImageIO.read(Resources.getResources().getResourceStream("images/open.gif"));
		btn = new JButton(new ImageIcon(image));
		btn.setActionCommand("openDoc");
		btn.addActionListener(docPerformaer);
		toolBar.add(btn);
		
		image = ImageIO.read(Resources.getResources().getResourceStream("images/save.gif"));
		btn = new JButton(new ImageIcon(image));
		btn.setActionCommand("saveDoc");
		btn.addActionListener(docPerformaer);
		toolBar.add(btn);
		
		image = ImageIO.read(Resources.getResources().getResourceStream("images/project.gif"));
		btn = new JButton(new ImageIcon(image));
		btn.setToolTipText("new project");
		btn.setActionCommand("newProject");
		btn.addActionListener(docPerformaer);
		toolBar.add(btn);
		
		btn = new JButton("Test");
		btn.setToolTipText("Test");
		btn.setActionCommand("test");
		btn.addActionListener(docPerformaer);
		toolBar.add(btn);
		
		toolBar.setBorder(BorderFactory.createEtchedBorder());
		return toolBar;
	}
}
