package eddy.frame.navigator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import main.CodeGenerater;
import resources.Resources;
import eddy.frame.docpane.DocController;
import eddy.generate.config.ClassConfigGenerator;
import eddy.generate.config.ProjectConfig;
import eddy.generate.javaclass.ClassGenerateAssistant;
import eddy.generate.javaclass.ClassGenerator;
import eddy.generate.javaclass.IbatisConfigPane;
import eddy.generate.javaclass.JavaClass;
import eddy.generate.javaclass.ibatis.IBatisGenerator_MyBatis;
import eddy.generate.webmodel.JSGeneratorN;
import eddy.generate.webmodel.WebModelGenerateAssistant;
import eddy.generate.webmodel.mvc.ActionGenerator;
import eddy.generate.webmodel.mvc.ActionGenerator_Spring;
import eddy.log.DebugLogger;

/**
 * 项目目录树
 * @author Eddy
 *
 */
public class ProjectTreeView extends ProjectActionAdapter {
	private JTree pojectTree = null;
	private JPopupMenu popMenu = null;
	private ProjectActionPerformer projectperformer = null;
	private DefaultTreeModel pojectFileTreeModel = null;
	private FileNode selectFileNode = null;
	
	public ProjectTreeView() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("My project");
		pojectFileTreeModel = new DefaultTreeModel(top);
		
		pojectTree = new JTree(pojectFileTreeModel);
		
		projectperformer = new ProjectActionPerformer();
		
		pojectTree.setFont(Resources.getResources().getSysFont_CN());
		pojectTree.addTreeExpansionListener(new DirExpansionListener(pojectFileTreeModel));
		
		popMenu = createPopuMenu();
		
		pojectTree.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				TreePath path = pojectTree.getPathForLocation(me.getX(), me.getY());
				if(path == null) return;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
				
				Object obj = treeNode.getUserObject();
				if(!(obj instanceof FileNode))
					return;
				
				selectFileNode = (FileNode)treeNode.getUserObject();
				
				if(me.getButton() == 3) {//右键
					pojectTree.setSelectionPath(path);
					popMenu.show(pojectTree, me.getX(), me.getY());
					if (me.isPopupTrigger()) {
						popMenu.show(me.getComponent(), me.getX(), me.getY());
				    }
				}
				
				if (me.getClickCount() == 2) {
					if(!treeNode.isLeaf()) return;
					File f = selectFileNode.getMyfile();
					openFile(f);
				}
			}
		});
	}
	
	/**
	 * 打开文件
	 * @param f
	 */
	private void openFile(File f) {
		if(f.isDirectory())
			return;
		
		String extname = f.getName();
		if(extname == null || extname.equals(""))
			return;
		
		int n = extname.lastIndexOf(".");
		if(n >= 0) 
		extname = extname.substring(n);
		
		if(!Resources.getResources().isOpenFileType(extname)) {
			int ret = JOptionPane.showConfirmDialog(null, "unknown file type，you sure open?", "waring", JOptionPane.YES_NO_OPTION);
			if(ret == JOptionPane.NO_OPTION)
				return;
		}
		
		DocController.getDocActionner().openDoc(f);
	}
	
	private void openFileUtf8(Object source) {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pojectTree.getSelectionPath().getLastPathComponent();
		FileNode fileNode = (FileNode) treeNode.getUserObject();
		File f = fileNode.getMyfile();
		DocController.getDocActionner().openDoc(f, "utf-8");
	}
	
	/**
	 * 生成弹出菜单
	 * @return
	 */
	private JPopupMenu createPopuMenu() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem item = new JMenuItem("new floppy");
		item.setActionCommand("newDirection");
		item.addActionListener(projectperformer);
		popup.add(item);
		
		item = new JMenuItem("delete");
		item.setActionCommand("deleteFile");
		item.addActionListener(projectperformer);
		popup.add(item);
		
		JMenu menu = new JMenu("new");
		
		item = new JMenuItem("class");
		item.setActionCommand("newClass");
		item.addActionListener(projectperformer);
		menu.add(item);
		
		item = new JMenuItem("WebModel");
		item.setActionCommand("newWebModel");
		item.addActionListener(projectperformer);
		menu.add(item);
		
		item = new JMenuItem("other file");
		item.setActionCommand("newFile");
		item.addActionListener(projectperformer);
		menu.add(item);
		
		popup.add(menu);
		
		item = new JMenuItem("open as UTF-8");
		item.setActionCommand("openFileUtf8");
		item.addActionListener(projectperformer);
		popup.add(item);
		
		return popup;
	}
	
	public JTree getPojectTree() {
		return pojectTree;
	}
	
	class ProjectActionPerformer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("newDirection")) {
				addDir();
			}
			else if(e.getActionCommand().equals("newClass")) {
				if(selectFileNode.getMyfile().getName().equals("src"))
					newClass();
			}
			else if(e.getActionCommand().equals("newWebModel")) {
				if(selectFileNode.getMyfile().getName().equals("WebRoot"))
					newWebModel();
			}
			else if(e.getActionCommand().equals("newFile")) {
				newFile();
			}
			else if(e.getActionCommand().equals("deleteFile")) {
				deleteFile();
			}
			else if(e.getActionCommand().equalsIgnoreCase("openFileUtf8")) {
				openFileUtf8(e.getSource());
			}
		}
	}
	
	public void addDir() {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pojectTree.getSelectionPath().getLastPathComponent();
		FileNode fileNode = (FileNode) treeNode.getUserObject();
		File f = fileNode.getMyfile();
		
		if(f == null)
			return;
		
		String s = JOptionPane.showInputDialog(null, "floppy name");
		if(s == null || s.equals(""))
			return;
		
		File newDir = new File(f.getPath() + "\\" + s);
		boolean b = newDir.mkdir();
		DebugLogger.getLogger().log(f.getPath() + "\\" + s + "  " + b);
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileNode(newDir));
		pojectFileTreeModel.insertNodeInto(newNode, treeNode, treeNode.getChildCount());
		TreeNode[] nodes = pojectFileTreeModel.getPathToRoot(newNode);
		TreePath path = new TreePath(nodes);
		pojectTree.scrollPathToVisible(path);
	}
	
	public void deleteFile() {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pojectTree.getSelectionPath().getLastPathComponent();
		FileNode fileNode = (FileNode) treeNode.getUserObject();
		File f = fileNode.getMyfile();
		boolean b = f.delete();
		
		if(b) {
			pojectFileTreeModel.removeNodeFromParent(treeNode);
			DebugLogger.getLogger().log("delete file" + f.getName() + "成功！");
		}
		else
			DebugLogger.getLogger().log("delete file" + f.getName() + "失败！");
	}
	
	public void newFile() {
		String s = JOptionPane.showInputDialog(null, "file name");
		if(s == null || s.equals(""))
			return;
			
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pojectTree.getSelectionPath().getLastPathComponent();
		FileNode fileNode = (FileNode) treeNode.getUserObject();
		File f = fileNode.getMyfile();
		File newFile = new File(f.getPath() + "\\" + s);
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileNode(newFile));
		pojectFileTreeModel.insertNodeInto(newNode, treeNode, treeNode.getChildCount());
		TreeNode[] nodes = pojectFileTreeModel.getPathToRoot(newNode);
		TreePath path = new TreePath(nodes);
		pojectTree.scrollPathToVisible(path);
		
		openFile(newFile);
	}
	
	public void newClass() {
		try {
			ClassGenerateAssistant cga = new ClassGenerateAssistant(CodeGenerater.getMainFrame());
			if(!cga.showAssistantDialog())
				return;
			
			ArrayList<JavaClass> jclist = cga.getSelectClassFields();
			
			//1. 生成Java实体文件并打开
			final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pojectTree.getSelectionPath().getLastPathComponent();
			FileNode fileNode = (FileNode) treeNode.getUserObject();
			File selectNodeFile = fileNode.getMyfile();
			ClassGenerator.createEty(cga.getClassName(), selectNodeFile.getPath(), jclist);
			
			//TODO 生成DAO层
			String projectPath = selectNodeFile.getParentFile().getPath();
			ClassGenerator.createIfaceDao(cga.getIbatisConfiger(), cga.getClassName(), projectPath);
//			ClassGenerator.createIfaceDaoImpl(cga.getIbatisConfiger(), cga.getClassName(), projectPath);
			
			//2. 生成对应的SQL文件
			IbatisConfigPane ibatsiConfig = cga.getIbatisConfiger();
			String sqlFileName = ibatsiConfig.getSqlFileName();
			
			String mybatisRootPath = projectPath + "\\WebRoot\\WEB-INF\\mybatis-config";
			
			int n = sqlFileName.lastIndexOf("/");
			if(n > 0) {
				String str = sqlFileName.substring(0, n);
				File tempFile = new File(mybatisRootPath + "\\" + str);
				tempFile.mkdirs();
			}
			
			//生成SQL文件并向
			IBatisGenerator_MyBatis ibatisGenerator = new IBatisGenerator_MyBatis();
			ibatisGenerator.createSqlFile(ibatsiConfig, cga.getClassName(), cga.getSelectDatabaseTable(), jclist, mybatisRootPath + "\\" + sqlFileName);
//			ibatisGenerator.addResourceToSqlMapFile(projectPath, sqlFileName);
			
			//生成XML配置文件
			ClassConfigGenerator.generateClassConfig(ibatsiConfig, cga.getClassName(), cga.getSelectDatabaseTable(), jclist, projectPath);
			
			//打开树结点
			
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileNode(new File(projectPath + "\\src")));
			pojectFileTreeModel.insertNodeInto(newNode, treeNode, treeNode.getChildCount());
			TreeNode[] nodes = pojectFileTreeModel.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			pojectTree.scrollPathToVisible(path);
		} catch (IOException e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
	}
	
	public void newWebModel() {
		try {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pojectTree.getSelectionPath().getLastPathComponent();
			FileNode fileNode = (FileNode) treeNode.getUserObject();
			File selectNodeFile = fileNode.getMyfile();
			String projectPath = selectNodeFile.getParentFile().getPath();
				
			WebModelGenerateAssistant wa = new WebModelGenerateAssistant(CodeGenerater.getMainFrame(), projectPath);
			if(!wa.showAssistantDialog())
				return;
			
			//1. 生成ACTION
			ActionGenerator ag = new ActionGenerator_Spring(wa, projectPath);
			ag.generateAction();
			
			//3. 生成JS
//			JSGenerator jg = new JSGenerator(wa, projectPath);
//			jg.generateJSFile();
			JSGeneratorN jg = new JSGeneratorN(wa, projectPath);
			jg.generateJSFile();
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
	}
	
	public void openProject() {
		JFileChooser fileChooser = new JFileChooser(new File("."));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int r = fileChooser.showSaveDialog(null);
		
		if (r != JFileChooser.APPROVE_OPTION) 
			return;
		
		File f = fileChooser.getSelectedFile();
		//打开树结点
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) pojectFileTreeModel.getRoot();
		FileNode projectFileNode = new FileNode(f);
		DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(projectFileNode);
		pojectFileTreeModel.insertNodeInto(projectNode, root, root.getChildCount());
		projectFileNode.expand(projectNode);
		TreePath path = new TreePath(projectNode.getPath());
		pojectTree.scrollPathToVisible(path);
		ProjectConfig.loadProjectSetting(f.getPath());
	}
	
	public void doTest() {
		try {
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void newProject() {
		try {
			//显示工程配置对话框
			NewProjectDialog newProjectDialog = new NewProjectDialog(CodeGenerater.getMainFrame());
			if(!newProjectDialog.showDialog())
				return;
			
			String projectDirection = newProjectDialog.getProjectDirection();
			String projectName = newProjectDialog.getProjectName();
			String projectPath = projectDirection + "/" + projectName; 
			
			ProjectConfig.createProjectSetting(newProjectDialog);
			ProjectConfig.createConfig(projectPath);
			
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) pojectFileTreeModel.getRoot();
			
			File f = new File(projectPath);
			DefaultMutableTreeNode newProjectNode = new DefaultMutableTreeNode(new FileNode(f));
			
			f = new File(projectPath + "/src");
			f.mkdir();
			
			DefaultMutableTreeNode tempNode = null;
			
			tempNode = new DefaultMutableTreeNode(new FileNode(f));
			newProjectNode.add(tempNode);
			
			pojectFileTreeModel.insertNodeInto(newProjectNode, root, root.getChildCount());
			TreeNode[] nodes = pojectFileTreeModel.getPathToRoot(newProjectNode);
			TreePath path = new TreePath(nodes);
			pojectTree.scrollPathToVisible(path);
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
	}
}
