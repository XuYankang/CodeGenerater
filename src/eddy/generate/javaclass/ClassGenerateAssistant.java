package eddy.generate.javaclass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resources.Resources;


import eddy.database.ConnectionConfiger;
import eddy.database.Database;
import eddy.database.DatabaseConnection;
import eddy.database.DatabaseFactory;
import eddy.database.DatabaseTable;
import eddy.database.DatebaseColumn;
import eddy.generate.config.ProjectConfig;
import eddy.log.DebugLogger;

/**
 * Java类文件生成向导
 * 
 * @author Eddy
 * 
 */
public class ClassGenerateAssistant {
	private JFrame parent = null;
	private JDialog dialog = null;
	private boolean ok = false;
	private JPanel assistantPane = new JPanel();
	private JButton finishBtn = null;

	private JPanel leftPane = new JPanel();
	private JPanel centerPane = new JPanel();
	private JPanel operatePane = null;

	private JTextField classNameTxt = new JTextField();

	private JPanel databasePane = null;
	private JPanel classSetPane = null;
	private JPanel ibatisSetPane = null;

	private DefaultListModel stepListMode;

	private JComboBox tablenameCombo = new JComboBox();// 表名列表
	private JComboBox dbConnectionCombo = new JComboBox();// 数据库连接列表

	private JList availableColumns;
	private DefaultListModel availableListMode;
	private JList includeColumns;
	private DefaultListModel includeListMode;
	private JList stepList;

	private IbatisConfigPane ibatisConfiger = new IbatisConfigPane();

	private JavaClassTableModel classTableModel = new JavaClassTableModel();

	public ClassGenerateAssistant(JFrame parent) {
		dialog = new JDialog(parent);
		dialog.setTitle("代码生成向导");
		this.parent = parent;
		stepListMode = new DefaultListModel();
		stepListMode.addElement("1.   数据库及表选择");
		stepListMode.addElement("2.   类详细设置");
		stepListMode.addElement("3.   ibatis配置");
		stepList = new JList(stepListMode);
		stepList.setEnabled(false);
		this.createOperatePane();
		this.initDatabasePane();
		this.initClassSetPane();
		this.initIbatisSetPane();

		loadDBConnections();

		assistantPane.setLayout(new BorderLayout());
		initLeftPane();

		centerPane.setBackground(new Color(236, 233, 216));
		assistantPane.add(leftPane, BorderLayout.WEST);
		assistantPane.add(centerPane, BorderLayout.CENTER);
		assistantPane.add(operatePane, BorderLayout.SOUTH);
		dialog.setContentPane(assistantPane);

		centerPane.setLayout(new BorderLayout());

		stepList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int index = stepList.getSelectedIndex();
				assistantPane.removeAll();
				JPanel tempP = null;
				if (index == 0) {
					tempP = databasePane;
				} else if (index == 1) {
					classTableModel.clearData();
					for (int i = 0; i < includeListMode.getSize(); i++) {
						Object obj = includeListMode.getElementAt(i);
						if (obj instanceof DatebaseColumn) {
							DatebaseColumn dc = (DatebaseColumn) obj;
							JavaClass jc = DBColumnToJavaClass.createJavaClass(dc);
							classTableModel.addClass(jc);
						}
					}
					classTableModel.fireTableDataChanged();
					DatabaseTable dt = (DatabaseTable) tablenameCombo.getSelectedItem();
					String f = dt.getTableName().charAt(0) + "";
					String etyName = dt.getTableName().replaceFirst(f, f.toUpperCase()) + "Ety";
					classNameTxt.setText("com.mini.cms.admin.dao.entity." + etyName);
					tempP = classSetPane;
				} else if (index == 2) {
					DatabaseTable dt = (DatabaseTable) tablenameCombo.getSelectedItem();
					ArrayList<String> pklist = dt.getPrimaryKeys();
					if (pklist.size() == 0) {// 没有主键，不能生成插入SQL

					}
					
					String f = dt.getTableName().charAt(0) + "";
					String daoName = dt.getTableName().replaceFirst(f, f.toUpperCase()) + "Dao";
					ibatisConfiger.setDaoName("com.mini.cms.admin.dao.mapper." + daoName);
					ibatisConfiger.setSqlFileName("sql/default/" + dt.getTableName() + ".xml");
					ibatisConfiger.setNameSpace("default." + dt.getTableName());

					tempP = ibatisSetPane;
				}

				assistantPane.add(leftPane, BorderLayout.WEST);
				assistantPane.add(tempP, BorderLayout.CENTER);
				assistantPane.add(operatePane, BorderLayout.SOUTH);
				assistantPane.revalidate();
				dialog.setContentPane(assistantPane);
				dialog.validate();
			}
		});
		stepList.setSelectedIndex(0);
		dialog.setSize(750, 600);
	}
	
	private void loadDBConnections() {
		dbConnectionCombo.addItem("");
		List<DatabaseConnection> dbList = Resources.getResources().getDBConnections();
		DatabaseConnection defaultConnection = null;
		for (DatabaseConnection db : dbList) {
			if(db.getUrl().equalsIgnoreCase(ProjectConfig.DefaultConnecton))
				defaultConnection = db;
			dbConnectionCombo.addItem(db);
		}
		dbConnectionCombo.addItem("new...");

		dbConnectionCombo.addItemListener(new ItemListener() {// 更改数据连接
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() != ItemEvent.SELECTED)
					return;
				
				tablenameCombo.removeAllItems();
				availableListMode.removeAllElements();

				try {
					Object selObj = dbConnectionCombo.getSelectedItem();

					if (selObj instanceof String && ((String) selObj).equals("new...")) {
						ConnectionConfiger configer = new ConnectionConfiger();
						configer.showConnectionDialog();
						DatabaseConnection newDB = configer.getDBConnection();

						DatabaseConnection tempDB = null;
						for (int i = 0; i < dbConnectionCombo.getItemCount(); i++) {
							Object obj = dbConnectionCombo.getItemAt(i);
							if (newDB.equals(obj)) {
								tempDB = (DatabaseConnection) obj;
								break;
							}
						}
						if (tempDB == null) {
							tempDB = newDB;
							dbConnectionCombo.insertItemAt(newDB, dbConnectionCombo.getItemCount() - 1);
							Resources.getResources().saveDBConnection(tempDB);
						}

						dbConnectionCombo.setSelectedItem(tempDB);
					}

					boolean isIn = selObj instanceof DatabaseConnection;
					if (!isIn) {
						return;
					}

					DatabaseConnection dbConnect = (DatabaseConnection) selObj;
					Database db = DatabaseFactory.createDatabase(dbConnect);
					ArrayList<DatabaseTable> databaseTables = db.getAllDatabaseTables();

					for (DatabaseTable dt : databaseTables)
						tablenameCombo.addItem(dt);
				} catch (Exception ex) {
					ex.printStackTrace();
					DebugLogger.getLogger().log(ex.getMessage());
				}
			}
		});
		dbConnectionCombo.setSelectedItem(defaultConnection);//设置默认选中记录
		dbConnectionCombo.setToolTipText(dbConnectionCombo.getSelectedItem().toString());
	}

	/**
	 * 初始化左边的步骤提示框
	 */
	private void initLeftPane() {
		leftPane = new JPanel();
		leftPane.setLayout(new BorderLayout());
		leftPane.setPreferredSize(new Dimension(200, 200));
		// leftPane.setBackground(Color.WHITE);
		JPanel p = new JPanel();

		p.setLayout(null);

		JLabel lb = new JLabel("步骤");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(20, 10, 150, 20);
		p.add(lb);
		//		
		lb = new JLabel("-----------------------------------------------------------");
		lb.setBounds(0, 20, 500, 40);
		p.add(lb);

		stepList.setBounds(10, 60, 180, 400);
		p.add(stepList);
		leftPane.add(p, BorderLayout.CENTER);
	}

	/**
	 * 生成下方的操作按钮，后退、前进等
	 */
	private void createOperatePane() {
		operatePane = new JPanel();

		JButton btn = new JButton("back");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selindex = stepList.getSelectedIndex();
				if (selindex > 0)
					stepList.setSelectedIndex(selindex - 1);
			}
		});
		operatePane.add(btn);

		btn = new JButton("next");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selindex = stepList.getSelectedIndex();
				if (selindex < 2)
					stepList.setSelectedIndex(selindex + 1);
			}
		});
		operatePane.add(btn);

		finishBtn = new JButton("finish");
		finishBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok = true;
				dialog.setVisible(false);
			}
		});
		operatePane.add(finishBtn);

		btn = new JButton("cancel");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		operatePane.add(btn);

		operatePane.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public String getClassName() {
		return classNameTxt.getText();
	}

	/**
	 * 初始化选择数据库面板
	 */
	private void initDatabasePane() {
		databasePane = new JPanel();
		databasePane.setLayout(null);
		databasePane.setBackground(new Color(236, 233, 216));
		JLabel lb = new JLabel("1. 数据库及表选择");
		lb.setBounds(10, 10, 200, 20);
		databasePane.add(lb);

		lb = new JLabel("--------------------------------------------------------------------------------------------" + "------------------------------");
		lb.setBounds(10, 20, 500, 40);
		databasePane.add(lb);

		lb = new JLabel("数据库连接");
		lb.setBounds(10, 80, 100, 25);
		databasePane.add(lb);

		dbConnectionCombo.setBounds(100, 80, 400, 25);
		databasePane.add(dbConnectionCombo);

		lb = new JLabel("选择表");
		lb.setBounds(10, 120, 100, 25);
		databasePane.add(lb);

		tablenameCombo.setBounds(100, 120, 400, 25);
		tablenameCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selObj = dbConnectionCombo.getSelectedItem();
				boolean isIn = selObj instanceof DatabaseConnection;
				if (!isIn) {
					return;
				}

				DatabaseConnection dbConnect = (DatabaseConnection) selObj;
				Database db = DatabaseFactory.createDatabase(dbConnect);

				DatabaseTable dt = (DatabaseTable) tablenameCombo.getSelectedItem();
				if (dt == null)
					return;
				ArrayList<DatebaseColumn> columnlist = db.getTableColumns(dt.getTableName());
				availableListMode.removeAllElements();
				includeListMode.removeAllElements();
				for (DatebaseColumn dc : columnlist) {
					availableListMode.addElement(dc);
				}
			}
		});

		databasePane.add(tablenameCombo);

		lb = new JLabel("可选择列");
		lb.setBounds(10, 160, 100, 20);
		databasePane.add(lb);

		availableListMode = new DefaultListModel();
		availableColumns = new JList(availableListMode);
		availableColumns.setBorder(BorderFactory.createEtchedBorder());

		JScrollPane p = new JScrollPane(availableColumns);
		p.setBounds(10, 185, 200, 300);
		databasePane.add(p);

		lb = new JLabel("已选择列");
		lb.setBounds(300, 160, 100, 20);
		databasePane.add(lb);

		includeListMode = new DefaultListModel();

		includeColumns = new JList(includeListMode);
		includeColumns.setBorder(BorderFactory.createEtchedBorder());
		p = new JScrollPane(includeColumns);
		p.setBounds(300, 185, 200, 300);
		databasePane.add(p);

		JButton btn = new JButton(">");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object o = ClassGenerateAssistant.this.availableColumns.getSelectedValue();
				ClassGenerateAssistant.this.includeListMode.addElement(o);
				ClassGenerateAssistant.this.availableListMode.removeElement(o);
			}
		});

		btn.setBounds(229, 240, 50, 25);
		databasePane.add(btn);

		btn = new JButton(">>");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] objs = ClassGenerateAssistant.this.availableListMode.toArray();
				for (Object o : objs)
					ClassGenerateAssistant.this.includeListMode.addElement(o);
				ClassGenerateAssistant.this.availableListMode.removeAllElements();
			}
		});
		btn.setBounds(229, 280, 50, 25);
		databasePane.add(btn);

		btn = new JButton("<");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object o = ClassGenerateAssistant.this.includeColumns.getSelectedValue();
				ClassGenerateAssistant.this.availableListMode.addElement(o);
				ClassGenerateAssistant.this.includeListMode.removeElement(o);
			}
		});
		btn.setBounds(229, 320, 50, 25);
		databasePane.add(btn);

		btn = new JButton("<<");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] objs = ClassGenerateAssistant.this.includeListMode.toArray();
				for (Object o : objs)
					ClassGenerateAssistant.this.availableListMode.addElement(o);
				ClassGenerateAssistant.this.includeListMode.removeAllElements();
			}
		});
		btn.setBounds(229, 360, 50, 25);
		databasePane.add(btn);
	}

	/**
	 * 初始化类设置面板
	 */
	public void initClassSetPane() {
		classSetPane = new JPanel();
		classSetPane.setLayout(null);
		classSetPane.setBackground(new Color(236, 233, 216));
		JLabel lb = new JLabel("2. 类详细设置");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(10, 10, 200, 20);
		classSetPane.add(lb);

		lb = new JLabel("--------------------------------------------------------------------------------------------" + "------------------------------");
		lb.setBounds(10, 20, 500, 40);
		classSetPane.add(lb);

		lb = new JLabel("类名称：");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(10, 60, 80, 25);
		classSetPane.add(lb);

		classNameTxt.setBounds(80, 60, 430, 30);
		classNameTxt.setFont(Resources.getResources().getSysFont_EN());
		classSetPane.add(classNameTxt);

		lb = new JLabel("类属性及类型");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(10, 100, 200, 20);
		classSetPane.add(lb);

		JTable table = new JTable(classTableModel);
		table.setFont(Resources.getResources().getSysFont_EN());
		table.setRowHeight(25);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setBounds(10, 120, 500, 380);
		classSetPane.add(scrollpane);
	}

	/**
	 * 初始化ibatis配置面板
	 */
	public void initIbatisSetPane() {
		ibatisSetPane = new JPanel();
		ibatisSetPane.setLayout(null);
		JLabel lb = new JLabel("3. ibatis配置");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(10, 10, 200, 20);
		ibatisSetPane.add(lb);

		ibatisConfiger.setBounds(10, 10, 500, 700);
		ibatisSetPane.add(ibatisConfiger);
	}

	/**
	 * 显示向导对话框
	 * 
	 * @return
	 */
	public boolean showAssistantDialog() {
		dialog.setModal(true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = d.width / 2 - dialog.getWidth() / 2;
		int y = d.height / 2 - dialog.getHeight() / 2;

		if (parent != null) {
			Point pl = parent.getLocation();
			x = (int) pl.getX() + (parent.getWidth() - dialog.getWidth()) / 2;
			y = (int) pl.getY() + (parent.getHeight() - dialog.getHeight()) / 2;
		}

		dialog.setLocation(x, y);
		dialog.setVisible(true);
		return ok;
	}

	public IbatisConfigPane getIbatisConfiger() {
		return ibatisConfiger;
	}

	public DatabaseTable getSelectDatabaseTable() {
		return (DatabaseTable) tablenameCombo.getSelectedItem();
	}

	/**
	 * 得到选择的JAVA实体的列
	 * 
	 * @return
	 */
	public ArrayList<JavaClass> getSelectClassFields() {
		return classTableModel.getJavaClassList();
	}

}
