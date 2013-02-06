package resources;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import eddy.database.DatabaseConnection;
import eddy.xml.XMLConfiger;

public class Resources {
//	public static String[] extNames = {".txt", ".java", ".js",".html",".log", ".xml", 
//		".ini", ".project", ".classpath", ".settings", ".prefs"};
	
	public static List<String> extNameList = new ArrayList<String>();
	static {
		extNameList.add(".txt");
		extNameList.add(".java");
		extNameList.add(".js");
		extNameList.add(".html");
		extNameList.add(".log");
		extNameList.add(".xml");
		extNameList.add(".project");
		extNameList.add(".classpath");
		extNameList.add(".settings");
		extNameList.add(".prefs");
	}
	
	private static Resources resources = null; 
	
	private Resources() {
	}
	
	public static Resources getResources() {
		if(resources == null)
			resources = new Resources();
		return resources;
	}
	
	public InputStream getResourceStream(String fileName) {
		return Resources.class.getResourceAsStream(fileName);
	}

	/**
	 * 中文字体
	 * @return
	 */
	public Font getSysFont_CN() {
		return new Font("宋体", Font.PLAIN, 14);
	}
	
	/**
	 * 英文字体
	 * @return
	 */
	public Font getSysFont_EN() {
		return new Font("Courier New", Font.PLAIN, 14);
	}

	/**
	 * 判断文件类型是否是已知类型
	 * @param extName
	 * @return
	 */
	public boolean isOpenFileType(String extName) {
		if(extNameList.contains(extName.toLowerCase()))
			return true;
		
		return false;
	}
	
	public void saveDBConnection(DatabaseConnection dbConnection) {
		XMLConfiger xmlConfiger = new XMLConfiger(new File("DatabaseConnection.xml").getPath());
		try {
			Node rootNode = xmlConfiger.getNode("/connections");
			Element newConnection = xmlConfiger.getDocument().createElement("connection");
			
			Element tempNode = xmlConfiger.getDocument().createElement("DriverName");
			tempNode.setTextContent(dbConnection.getDriverName());
			newConnection.appendChild(tempNode);
			
			tempNode = xmlConfiger.getDocument().createElement("url");
			tempNode.setTextContent(dbConnection.getUrl());
			newConnection.appendChild(tempNode);
				
			tempNode = xmlConfiger.getDocument().createElement("UserName");
			tempNode.setTextContent(dbConnection.getUserName());
			newConnection.appendChild(tempNode);
			
			tempNode = xmlConfiger.getDocument().createElement("Password");
			tempNode.setTextContent(dbConnection.getPassword());
			newConnection.appendChild(tempNode);
			
			rootNode.appendChild(newConnection);
			xmlConfiger.saveConfiger();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到数据库连接
	 * @return
	 */
	public List<DatabaseConnection> getDBConnections() {
		List<DatabaseConnection> dbList = new ArrayList<DatabaseConnection>();
		
		XMLConfiger xmlConfiger = new XMLConfiger(new File("DatabaseConnection.xml").getPath());
		try {
			
			Object connectionCounts = xmlConfiger.evaluate("count(/connections/connection)");
			if(connectionCounts == null && connectionCounts instanceof String)
				return dbList;
			
			int count = Integer.parseInt((String) connectionCounts);
			for(int i = 1; i <= count; i++) {
				String DriverName = xmlConfiger.getNodeValue("/connections/connection[" + i +"]/DriverName");
				String url = xmlConfiger.getNodeValue("/connections/connection[" + i +"]/url");
				String UserName = xmlConfiger.getNodeValue("/connections/connection[" + i +"]/UserName");
				String Password = xmlConfiger.getNodeValue("/connections/connection[" + i +"]/Password");
				
				DatabaseConnection dbConnect = new DatabaseConnection();
				dbConnect.setDriverName(DriverName);
				dbConnect.setUrl(url);
				dbConnect.setUserName(UserName);
				dbConnect.setPassword(Password);
				dbList.add(dbConnect);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbList;
	}
	
}
