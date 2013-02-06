package eddy.frame.navigator;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import resources.Resources;

/**
 * 左边的导航面板
 * 
 * @author Eddy
 * 
 */
public class NavigatorPane {
	private JTabbedPane tabPane = new JTabbedPane();
	private FileTree systemFileTree;
	private ProjectTreeView projectTree;
	public ProjectTreeView getProjectTree() {
		return projectTree;
	}

	public NavigatorPane() {
		tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabPane.setBackground(Color.gray);
		tabPane.setFont(Resources.getResources().getSysFont_CN());
		showFileTree();
		showProjectTree();
		tabPane.setSelectedIndex(1);
	}

	public JTabbedPane getTabPane() {
		return this.tabPane;
	}

	/**
	 * 显示文件资源树
	 */
	public void showFileTree() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		systemFileTree = new FileTree("My computer");
		JScrollPane jsp = new JScrollPane(systemFileTree.createFileTree());
		
		tabPane.add("explorer", jsp);
		int i = tabPane.getTabCount();
		tabPane.setSelectedIndex(i - 1);
	}

	/**
	 * 显示用户项目树
	 */
	public void showProjectTree() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		projectTree = new ProjectTreeView();
		JScrollPane jsp = new JScrollPane(projectTree.getPojectTree());
		tabPane.add("My project", jsp);
		int i = tabPane.getTabCount();
		tabPane.setSelectedIndex(i - 1);
	}
}
