package eddy.frame.navigator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

import resources.Resources;


import eddy.database.ConnectionConfiger;
import eddy.database.DatabaseConnection;
import eddy.log.DebugLogger;

public class DBConnectionCombo extends JComboBox {
	
	private static final long serialVersionUID = -8124155654357042621L;

	public DBConnectionCombo() {
		this.addItem("");

		List<DatabaseConnection> dbList = Resources.getResources().getDBConnections();
		for (DatabaseConnection db : dbList) {
			this.addItem(db);
		}

		this.addItem("new...");

		this.addActionListener(new ActionListener() {// 更改数据连接
			public void actionPerformed(ActionEvent e) {
				try {
					Object selObj = getSelectedItem();

					if (selObj instanceof String && ((String) selObj).equals("new...")) {
						ConnectionConfiger configer = new ConnectionConfiger();
						configer.showConnectionDialog();
						DatabaseConnection newDB = configer.getDBConnection();

						DatabaseConnection tempDB = null;
						for (int i = 0; i < getItemCount(); i++) {
							Object obj = getItemAt(i);
							if (newDB.equals(obj)) {
								tempDB = (DatabaseConnection) obj;
								break;
							}
						}
						if (tempDB == null) {
							tempDB = newDB;
							insertItemAt(newDB, getItemCount() - 1);
							Resources.getResources().saveDBConnection(tempDB);
						}

						setSelectedItem(tempDB);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					DebugLogger.getLogger().log(ex.getMessage());
				}
			}
		});

		this.setToolTipText(this.getSelectedItem().toString());
	}
}
