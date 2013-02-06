package eddy.frame.navigator;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 文件结点类
 * @author Eddy
 *
 */
public class FileNode {
	protected File myfile;
	
	public File getMyfile() {
		return myfile;
	}

	public FileNode() {
		myfile = new File(".");
	}
	
	public FileNode(File file) {
		myfile = file;
	}
	
	public String toString() {
		return myfile.getName().length() > 0 ? myfile.getName() : myfile.getPath();
	}

	/**
	 * 展开树结点
	 * @param treeNode
	 */
	public boolean expand(DefaultMutableTreeNode parentNode) {
		parentNode.removeAllChildren();
		
		File[] filelist = myfile.listFiles();
		if(filelist == null)
			return true;
		
		for (int k = 0; k < filelist.length; k++) {
			FileNode fileNode = new FileNode(filelist[k]);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileNode);
			parentNode.add(node);
			
			File[] subfilelist = filelist[k].listFiles();
			if(subfilelist != null) {
				node.add(new DefaultMutableTreeNode("正在加载"));
			}
		}
		
		return true;
	}
}
