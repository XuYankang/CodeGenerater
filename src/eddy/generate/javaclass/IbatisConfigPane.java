package eddy.generate.javaclass;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eddy.generate.config.ProjectConfig;

import resources.Resources;

/**
 * ibatis配置面板
 * @author Eddy
 *
 */
public class IbatisConfigPane extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JCheckBox insertChk = new JCheckBox("插入Sql");
	private JCheckBox deleteChk = new JCheckBox("删除Sql");
	private JCheckBox updateChk = new JCheckBox("修改Sql");
	private JCheckBox selectChk = new JCheckBox("查询Sql");
	
	private JCheckBox selectLimitCountChk = new JCheckBox("记录数Sql");
	private JCheckBox selectByLimitChk = new JCheckBox("分页查询Sql");
	
	private String insertSqlID = "insert";
	private String deleteSqlID = "deleteById";
	private String updateSqlID = "updateById";
	private String selectSqlID = "selectByEntity";
	
	private String selectLimitCountSqlID = "selectLimitCount";
	private String selectByLimitSqlID = "selectByLimit";
	
	private JTextField daoTxtField = new JTextField();
	private JTextField implTxtField = new JTextField();
	private JTextField sqlFileNameTxt = new JTextField();
	private JTextField nameSpaceTxt = new JTextField();
	
//	private JComboBox visionBox;
	
	public IbatisConfigPane() {
		setLayout(null);
		
		JLabel lb = new JLabel(
				"--------------------------------------------------------------------------------------------"
						+ "------------------------------");
		lb.setBounds(10, 20, 500, 40);
		add(lb);
		
		lb = new JLabel("选择功能:");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(10, 50, 500, 25);
		add(lb);
		
		int startX = 10;
		int startY = 40;
		int gap = 60;
		
		lb = new JLabel("dao:");
		lb.setFont(Resources.getResources().getSysFont_EN());
		lb.setBounds(startX, startY+=gap, 50, 30);
		add(lb);
		
		daoTxtField.setBounds(startX + 70, startY, 400, 30);
		daoTxtField.setFont(Resources.getResources().getSysFont_EN());
		add(daoTxtField);
		
		lb = new JLabel("sql文件:");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(startX, startY+=gap, 70, 30);
		add(lb);
		sqlFileNameTxt.setBounds(startX + 70, startY, 400, 30);
		sqlFileNameTxt.setFont(Resources.getResources().getSysFont_EN());
		add(sqlFileNameTxt);

		lb = new JLabel("命名空间:");
		lb.setFont(Resources.getResources().getSysFont_CN());
		lb.setBounds(startX, startY+=gap, 70, 30);
		add(lb);
		nameSpaceTxt.setBounds(startX + 70, startY, 400, 30);
		nameSpaceTxt.setFont(Resources.getResources().getSysFont_EN());
		add(nameSpaceTxt);
		
		
		insertChk.setBounds(startX, startY+=gap, 100, 30);
		insertChk.setFont(Resources.getResources().getSysFont_CN());
		insertChk.setSelected(true);
		add(insertChk);
		int txtWidth = 300;
		
		deleteChk.setBounds(startX + 100, startY, txtWidth, 30);
		deleteChk.setFont(Resources.getResources().getSysFont_CN());
		deleteChk.setSelected(true);
		add(deleteChk);
		
		updateChk.setBounds(startX + 100 + 100, startY, txtWidth, 30);
		updateChk.setFont(Resources.getResources().getSysFont_CN());
		updateChk.setSelected(true);
		add(updateChk);
//		
		selectChk.setBounds(startX, startY+=gap, txtWidth, 30);
		selectChk.setFont(Resources.getResources().getSysFont_CN());
		selectChk.setSelected(true);
		add(selectChk);
		
		selectLimitCountChk.setBounds(startX + 100, startY, txtWidth, 30);
		selectLimitCountChk.setFont(Resources.getResources().getSysFont_CN());
		selectLimitCountChk.setSelected(true);
		add(selectLimitCountChk);
		
		selectByLimitChk.setBounds(startX + 100 + 100, startY, txtWidth, 30);
		selectByLimitChk.setFont(Resources.getResources().getSysFont_CN());
		selectByLimitChk.setSelected(true);
		add(selectByLimitChk);
		
	}
	
	public String getNameSpace() {
		return nameSpaceTxt.getText();
	}
	
	public void setNameSpace(String nameSpace) {
		nameSpaceTxt.setText(nameSpace);
	}
	
	public String getSqlFileName() {
		return this.sqlFileNameTxt.getText();
	}
	
	public void setSqlFileName(String name) {
		this.sqlFileNameTxt.setText(name);
	}
	
	public String getImplName() {
		return this.implTxtField.getText();
	}
	
	public void setImplName(String name) {
		this.implTxtField.setText(name);
	}
	
	public String getDaoName() {
		return this.daoTxtField.getText();
	}
	
	public void setDaoName(String name) {
		this.daoTxtField.setText(name);
	}
	
	public String getInsertSqlID() {
		if(insertChk.isSelected())
			return insertSqlID;
		return null;
	}

	public void setInsertSqlID(String insertSqlID) {
		this.insertSqlID = insertSqlID;
	}
	
	public String getUpdateSqlID() {
		if(updateChk.isSelected())
			return updateSqlID;
		return null;
	}

	public void setUpdateSqlID(String updateSqlID) {
		this.updateSqlID = updateSqlID;
	}

	public String getDeleteSqlID() {
		if(deleteChk.isSelected())
			return deleteSqlID;
		return null;
	}

	public void setDeleteSqlID(String deleteSqlID) {
		this.deleteSqlID = deleteSqlID;
	}
	
	public String getSelectSqlID() {
		if(selectChk.isSelected())
			return selectSqlID;
		return null;
	}

	public void setSelectSqlID(String selectSqlID) {
		this.selectSqlID = selectSqlID;
	}

	public String getSelectLimitCountSqlID() {
		if(selectLimitCountChk.isSelected())
			return selectLimitCountSqlID;
		return null;
	}

	public void setSelectLimitCountSqlID(String selectLimitCountSqlID) {
		this.selectLimitCountSqlID = selectLimitCountSqlID;
	}

	public String getSelectByLimitSqlID() {
		if(selectByLimitChk.isSelected()) 
			return selectByLimitSqlID;
		return null;
	}

	public void setSelectByLimitSqlID(String selectByLimitSqlID) {
		this.selectByLimitSqlID = selectByLimitSqlID;
	}
	
	public String getSelectVision() {
		return "mybatis";
	}
	
}
