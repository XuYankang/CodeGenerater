package eddy.frame.navigator;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * �����չ����رմ���
 * @author Eddy
 *
 */
public class DirExpansionListener implements TreeExpansionListener {
	
	private DefaultTreeModel treeModel = null;
	
	public DirExpansionListener(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}
	
	public void treeCollapsed(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
		Object obj = treeNode.getUserObject();
		if(obj instanceof FileNode)
			treeNode.removeAllChildren();
		treeNode.add(new DefaultMutableTreeNode("���ڼ���"));
	}

	public void treeExpanded(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
		Object obj = treeNode.getUserObject();
		if (obj instanceof FileNode) {
			final FileNode fnode = (FileNode) obj;
			Thread runner = new Thread() {
				public void run() {
					if (fnode != null && fnode.expand(treeNode)) {
						Runnable runnable = new Runnable() {
							public void run() {
								treeModel.reload(treeNode);
							}
						};
						SwingUtilities.invokeLater(runnable);
					}
				}
			};
			runner.start();
		}
	}
}
