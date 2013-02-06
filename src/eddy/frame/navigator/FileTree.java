package eddy.frame.navigator;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import resources.Resources;


import eddy.frame.docpane.DocController;
import eddy.log.DebugLogger;

/**
 * 资源文件树
 * @author Eddy
 *
 */
public class FileTree {

	private DefaultTreeModel treeModel = null;

	private File[] filelist = null;
	private String rootName = "";
	private JTree tree = null;

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public FileTree(String rootName) {
		filelist = File.listRoots();
		this.rootName = rootName;
	}

	public FileTree(File rootFile, String rootName) {
		filelist = rootFile.listFiles();
		this.rootName = rootName;
	}

	/**
	 * 生成文件资源导航树
	 * 
	 * @return
	 */
	public JTree createFileTree() {
		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootName);
		for (int k = 0; k < filelist.length; k++) {
			FileNode fileNode = new FileNode(filelist[k]);
			node = new DefaultMutableTreeNode(fileNode);
			top.add(node);
			node.add(new DefaultMutableTreeNode("正在加载"));
		}

		treeModel = new DefaultTreeModel(top);
		tree = new JTree(treeModel);
		tree.setFont(Resources.getResources().getSysFont_CN());
		tree.addTreeExpansionListener(new DirExpansionListener(treeModel));
		tree.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if(path == null) return;
					
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
					if(!treeNode.isLeaf()) return;
					
					Object userObj = treeNode.getUserObject();
					boolean isI = userObj instanceof FileNode; 
					if(!isI)
						return;
					
					FileNode fileNode = (FileNode) treeNode.getUserObject();
					File f = fileNode.getMyfile();
					
					if(f.isDirectory())
						return;
					
					String extname = f.getName();
					if(extname == null || extname.equals(""))
						return;
					
					int n = extname.lastIndexOf(".");
					if(n >= 0) 
					extname = extname.substring(n);
					
					if(!Resources.getResources().isOpenFileType(extname)) {
						int ret = JOptionPane.showConfirmDialog(null, "未知文件类型，确定打开?", "提示", JOptionPane.YES_NO_OPTION);
						if(ret == JOptionPane.NO_OPTION)
							return;
					}
					
					DebugLogger.getLogger().log(extname);
					
					DocController.getDocActionner().openDoc(f);
					DebugLogger.getLogger().log(fileNode.toString());
				}
			}
		});

		return tree;
	}

	public static DefaultMutableTreeNode addFileTree(File file, DefaultMutableTreeNode top) {
		File[] filelist = file.listFiles();
		DefaultMutableTreeNode node = null;
		for (int k = 0; k < filelist.length; k++) {
			FileNode fileNode = new FileNode(filelist[k]);
			node = new DefaultMutableTreeNode(fileNode);
			top.add(node);
			
			File fff = filelist[k];
			if(fff.listFiles() != null)
				node.add(new DefaultMutableTreeNode("正在加载"));
		}

		return node;
	}

	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}
}
