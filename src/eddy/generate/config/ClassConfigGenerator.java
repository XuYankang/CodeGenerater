package eddy.generate.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mysql.jdbc.ResultSetMetaData;

import eddy.database.DatabaseTable;
import eddy.generate.javaclass.IbatisConfigPane;
import eddy.generate.javaclass.JavaClass;
import eddy.generate.util.FileCreator;

public class ClassConfigGenerator {
	
	/**
	 * 
	 * @param projectPath 项目路径
	 */
	public static void generateMainConfig(String projectPath) {
		try {
			Document doc = org.dom4j.DocumentHelper.createDocument();  
			doc.addElement("entities");
			FileCreator.saveXmlDoc(doc, projectPath + "/.settings/entities.xml");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getEntityConfigPath(String projectPath, String entityName) throws Exception {
		String mainConfigPath = projectPath + "/.settings/entities.xml";
		SAXReader saxReader =  new SAXReader();
		Document doc = saxReader.read(new FileInputStream(new File(mainConfigPath)));
		Element node = doc.getRootElement().elementByID(entityName);
		return node.attribute("resource").getStringValue();
	}
	
	public static List getEntitiyList(String projectPath) throws Exception {
		String mainConfigPath = projectPath + "/.settings/entities.xml";
		SAXReader saxReader =  new SAXReader();
		Document doc = saxReader.read(new FileInputStream(new File(mainConfigPath)));
		return doc.getRootElement().elements("entity");
	}
	
	private static void addToMainConfig(String projectPath, String entityName, String entityConfigPath) throws Exception {
		String mainConfigPath = projectPath + "/.settings/entities.xml";
		SAXReader saxReader =  new SAXReader();
		Document doc = saxReader.read(new FileInputStream(new File(mainConfigPath)));
		Element root = doc.getRootElement();
		root.addElement("entity").addAttribute("ID", entityName).addAttribute("name", entityName).addAttribute("resource", entityConfigPath);
		FileCreator.saveXmlDoc(doc, mainConfigPath);
	}
	
	
	/**
	 * 对每个类生成的临时配置文件
	 * @param ibatsiConfig
	 * @param className
	 * @param dbTable
	 * @param jclist
	 * @param filePath
	 */
	public static void generateClassConfig(IbatisConfigPane ibatsiConfig, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist, String projectPath) {
		try {
			Document doc = org.dom4j.DocumentHelper.createDocument();  
			
			Element rootElement = doc.addElement("class");
			rootElement.addAttribute("type", className);
			
			rootElement.addElement("ClassName").setText(className);
			rootElement.addElement("TableName").setText(dbTable.getTableName().toLowerCase());
			rootElement.addElement("DaoName").setText(ibatsiConfig.getDaoName());
			rootElement.addElement("DaoImplName").setText(ibatsiConfig.getImplName());
			rootElement.addElement("NameSpace").setText(ibatsiConfig.getNameSpace());
			
			Element attEl = rootElement.addElement("Attributes");
			for(JavaClass jc : jclist) {
				Element aEl = attEl.addElement("filed");
				aEl.addAttribute("name", jc.getFieldName()).addAttribute("type", jc.getFieldClass());
				if(jc.isPrimaryKey()) {
					aEl.addAttribute("PrimaryKey", "true");
				}
				
				if(jc.getIsNull() == ResultSetMetaData.columnNullable) {
					aEl.addAttribute("nullable", "true");
				}
				else {
					aEl.addAttribute("nullable", "false");
				}
			}
			
			String filePath = projectPath + "\\.settings\\" + className + ".xml";
			FileCreator.saveXmlDoc(doc, filePath);
			addToMainConfig(projectPath, className, filePath);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
