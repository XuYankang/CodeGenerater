package eddy.generate.webmodel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import eddy.generate.config.ClassConfigGenerator;

/**
 * Web模块文件生成向导
 * @author Eddy
 *
 */
public class WebModelGenerateAssistant {
	private JFrame parent = null;
	private JDialog dialog = null;
	private JPanel assistantPane = new JPanel();
	private JTabbedPane tabPane;
	private String projectPath;
	
	public JComboBox entityCombo;
	
	public JTextField daoTextField = new JTextField();
	public JTextField NameSpaceTextField = new JTextField();
	
	public JTextField jsFileNameField = new JTextField();
	public JTextField searchFormField = new JTextField();
	public JTextField primaryKeyHiddenField = new JTextField();
	public JTextField editFormField = new JTextField();
	public JTextField gridField = new JTextField();
	
	public JTextField controllerNameField = new JTextField();
	public JTextField requestMappingField = new JTextField();
	
	private String entityFilePath = "";
	private boolean ok = false;
	
	public WebModelGenerateAssistant(JFrame parent, String projectPath) {
		this.projectPath = projectPath;
		dialog = new JDialog(parent);
		dialog.setTitle("WebModel向导");
		this.parent = parent;
		assistantPane.setLayout(new BorderLayout());
		assistantPane.add(createMainPane(), BorderLayout.CENTER);
		assistantPane.add(createOperatePane(), BorderLayout.SOUTH);
		dialog.setContentPane(assistantPane);
		dialog.setSize(750, 600);
		dialog.validate();
	}
	
	private JPanel createMainPane() {
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		
		tabPane = new JTabbedPane();
		tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabPane.setBackground(Color.gray);
		JScrollPane jsp;
		
		jsp = new JScrollPane(createSelEntityPane(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabPane.add("选择实体", jsp);
		
		jsp = new JScrollPane(createPagePane(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabPane.add("页面配置", jsp);
		
		jsp = new JScrollPane(createSpringMVCPane(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabPane.add("springMVC配置", jsp);
		
		mainPane.add(tabPane, BorderLayout.CENTER);
		return mainPane;
	}
	
	private JPanel createSpringMVCPane() {
		JPanel springMVCPane = new JPanel();
		springMVCPane.setLayout(null);
		int startY = 10;
		int startX = 30;
		int gapY = 40;
		
		JLabel lb = new JLabel("Controller Class Fullname");
		lb.setBounds(startX, startY, 190, 25);
		springMVCPane.add(lb);
		controllerNameField.setBounds(190, startY, 400, 25);
		springMVCPane.add(controllerNameField);
		
		lb = new JLabel("RequestMapping URL ");
		lb.setBounds(startX, startY+=gapY, 190, 25);
		springMVCPane.add(lb);
		requestMappingField.setBounds(190, startY, 400, 25);
		springMVCPane.add(requestMappingField);
		
		return springMVCPane;
	}
	
	
	private JPanel createPagePane() {
		JPanel pagePane = new JPanel();
		pagePane.setLayout(null);
		int startY = 10;
		int startX = 30;
		int gapY = 40;
		
		JLabel lb = new JLabel("JS");
		lb.setBounds(startX, startY, 100, 25);
		pagePane.add(lb);
		jsFileNameField.setBounds(100, startY, 400, 25);
		pagePane.add(jsFileNameField);
		
		lb = new JLabel("隐藏主键");
		lb.setBounds(startX, startY+=gapY, 100, 25);
		pagePane.add(lb);
		primaryKeyHiddenField.setBounds(100, startY, 400, 25);
		pagePane.add(primaryKeyHiddenField);
		
		lb = new JLabel("查询条件");
		lb.setBounds(startX, startY+=gapY, 100, 25);
		pagePane.add(lb);
		searchFormField.setBounds(100, startY, 400, 25);
		pagePane.add(searchFormField);
		
		lb = new JLabel("编辑属性");
		lb.setBounds(startX, startY+=gapY, 100, 25);
		pagePane.add(lb);
		editFormField.setBounds(100, startY, 400, 25);
		pagePane.add(editFormField);
		
		lb = new JLabel("Grid 列");
		lb.setBounds(startX, startY+=gapY, 100, 25);
		pagePane.add(lb);
		gridField.setBounds(100, startY, 400, 25);
		pagePane.add(gridField);
		
		return pagePane;
	}
	
	/**
	 * 生成选择实体面板
	 * @return
	 */
	private JPanel createSelEntityPane() {
		JPanel selEntityPane = new JPanel();
		selEntityPane.setLayout(null);
		int startY = 10;
		int startX = 30;
		int gapY = 40;
		
		JLabel lb = new JLabel("选择实体");
		lb.setBounds(startX, startY, 100, 25);
		selEntityPane.add(lb);
		entityCombo = new JComboBox();
		entityCombo.addItem("");
		loadEntityCombo();
		entityCombo.setBounds(100, startY, 400, 25);
		entityCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String selEntityId = (String) entityCombo.getSelectedItem();
					entityFilePath = ClassConfigGenerator.getEntityConfigPath(projectPath, selEntityId);
					SAXReader saxReader =  new SAXReader();
					Document doc = saxReader.read(new FileInputStream(new File(entityFilePath)));
					daoTextField.setText(doc.getRootElement().element("DaoName").getStringValue());
					NameSpaceTextField.setText(doc.getRootElement().element("NameSpace").getStringValue());
					
					String classFullName = doc.getRootElement().element("ClassName").getStringValue();
					String tableName = doc.getRootElement().element("TableName").getStringValue();
					int n = classFullName.lastIndexOf(".");
					String classShortName = classFullName.substring(n + 1);
					
					String f = tableName.charAt(0) + "";
					String controllerName = tableName.replaceFirst(f, f.toUpperCase()) + "Controller";
					controllerNameField.setText("com.ms.controller." + controllerName);
					requestMappingField.setText("/" + controllerName + "/");
					
					jsFileNameField.setText(tableName.replaceFirst(f, f.toUpperCase()) + ".js");
					
					List list = doc.getRootElement().element("Attributes").elements("filed");
					String tempStr = "";
					for(int j = 0; j < list.size(); j++) {
						Element node = (Element) list.get(j);
						if(node.attribute("PrimaryKey") != null && node.attribute("PrimaryKey").getStringValue().equals("true") ) {
							primaryKeyHiddenField.setText(node.attribute("name").getStringValue());
						}
						else {
							tempStr += node.attribute("name").getStringValue() + ",";
						}
					}
					
					editFormField.setText(tempStr);
					gridField.setText(tempStr);
					searchFormField.setText(tempStr);
					
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		selEntityPane.add(entityCombo);
		
		lb = new JLabel("DAOIface");
		lb.setBounds(startX, startY+=gapY, 100, 25);
		selEntityPane.add(lb);
		
		daoTextField.setBounds(100, startY, 400, 25);
		selEntityPane.add(daoTextField);
		
		lb = new JLabel("NameSpace");
		lb.setBounds(startX, startY+=gapY, 100, 25);
		selEntityPane.add(lb);
		
		NameSpaceTextField.setBounds(100, startY, 400, 25);
		selEntityPane.add(NameSpaceTextField);
		
		return selEntityPane;
	}
	
	private void loadEntityCombo() {
		try {
			List list = ClassConfigGenerator.getEntitiyList(projectPath);
			for(int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				entityCombo.addItem(node.attribute("name").getStringValue());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成下方的操作按钮
	 */
	private JPanel createOperatePane() {
		JPanel operatePane = new JPanel();
		JButton btn = new JButton("确定");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok = true;
				dialog.setVisible(false);
			}
		});
		operatePane.add(btn);
		
		btn = new JButton("取消");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		operatePane.add(btn);
		
		return operatePane;
	}
	
	public boolean showAssistantDialog() {
		dialog.setModal(true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = d.width/2 - dialog.getWidth()/2;
		int y = d.height/2 - dialog.getHeight()/2;
		
		if(parent != null) {
			Point pl = parent.getLocation();
			x = (int) pl.getX() + (parent.getWidth() - dialog.getWidth())/2;
			y = (int) pl.getY() + (parent.getHeight() - dialog.getHeight())/2;
		}
		
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		
		return ok;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getEntityFilePath() {
		return entityFilePath;
	}
	
}
