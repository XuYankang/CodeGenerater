package eddy.database;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 数据库连接配置对话框
 * @author Eddy
 *
 */
public class ConnectionConfiger {

	JPanel connectionPanel;
	JLabel userNameLabel;
	JTextField userNameField;
	JLabel passwordLabel;
	JTextField passwordField;
	JTextArea queryTextArea;
	JLabel serverLabel;
	JTextField urlField;
	JLabel driverLabel;
	JTextField driverField;
	
	public void showConnectionDialog() {
		JScrollPane tableAggregate;
		String ConnectTitle = "Connection Information";
		String[] ConnectOptionNames = { "Connect" };

		tableAggregate = new JScrollPane();

		JPanel connectionPanel = createConnectionPane();
		
		connectionPanel.setLayout(new BoxLayout(connectionPanel,
				BoxLayout.X_AXIS));

		JPanel namePanel = new JPanel(false);
		namePanel.setLayout(new GridLayout(0, 1));

		int ret = JOptionPane.showOptionDialog(tableAggregate, connectionPanel,
				ConnectTitle, JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, ConnectOptionNames,
				ConnectOptionNames[0]);
		if(ret == 0) {
			System.out.println("SSSSSSSSSs");
		}
	}

	public  JPanel createConnectionPane() {
		userNameLabel = new JLabel("User name: ", JLabel.RIGHT);
		userNameField = new JTextField("sa");

		passwordLabel = new JLabel("Password: ", JLabel.RIGHT);
		passwordField = new JTextField("spring");

		serverLabel = new JLabel("Database URL: ", JLabel.RIGHT);
		urlField = new JTextField("jdbc:sqlserver://192.168.0.87:1433;DatabaseName=dfkyCNPC;SelectMethod=cursor");

		driverLabel = new JLabel("Driver: ", JLabel.RIGHT);
		driverField = new JTextField("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		connectionPanel = new JPanel(false);
		connectionPanel.setLayout(new BoxLayout(connectionPanel,
				BoxLayout.X_AXIS));

		JPanel namePanel = new JPanel(false);
		namePanel.setLayout(new GridLayout(0, 1));
		namePanel.add(serverLabel);
		namePanel.add(driverLabel);
		namePanel.add(userNameLabel);
		namePanel.add(passwordLabel);

		JPanel fieldPanel = new JPanel(false);
		fieldPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.add(urlField);
		fieldPanel.add(driverField);
		fieldPanel.add(userNameField);
		fieldPanel.add(passwordField);
		

		connectionPanel.add(namePanel);
		connectionPanel.add(fieldPanel);
		return connectionPanel;
	}
	
	public DatabaseConnection getDBConnection() {
		DatabaseConnection dbConnect = new DatabaseConnection();
		dbConnect.setDriverName(driverField.getText());
		dbConnect.setUrl(urlField.getText());
		dbConnect.setUserName(userNameField.getText());
		dbConnect.setPassword(passwordField.getText());
		return dbConnect;
	}
	
	public static void main(String[] asd) {
		ConnectionConfiger c = new ConnectionConfiger();
		c.showConnectionDialog();
	}
}
