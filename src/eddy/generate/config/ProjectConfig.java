package eddy.generate.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import resources.Resources;
import eddy.frame.docpane.TextFileOperator;
import eddy.frame.navigator.NewProjectDialog;
import eddy.log.DebugLogger;

public class ProjectConfig {
	
	public static String projectPath = "";
	public static String projectName = "";
	public static String IbatisType = "";
	public static String MVCType = "";
	public static String DefaultConnecton = "";
	
	public static void loadProjectSetting(String oldProjectPath) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(oldProjectPath + "/" + "/gen.project"));
			ProjectConfig.projectPath = prop.getProperty("projectPath");
			ProjectConfig.projectName = prop.getProperty("projectName");
			ProjectConfig.IbatisType = prop.getProperty("IbatisType");
			ProjectConfig.DefaultConnecton = prop.getProperty("DefaultConnecton");
			ProjectConfig.MVCType  = prop.getProperty("MVCType");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成工程配置文件
	 * @param newProjectDialog
	 */
	public static void createProjectSetting(NewProjectDialog newProjectDialog) {
		try {
			String projectDirection = newProjectDialog.getProjectDirection();
			String projectName = newProjectDialog.getProjectName();
			String projectPath = projectDirection + "/" + projectName; 
			
			ProjectConfig.projectPath = projectPath;
			ProjectConfig.projectName = projectName;
			ProjectConfig.IbatisType = newProjectDialog.getIbatisType();
			ProjectConfig.DefaultConnecton = newProjectDialog.getDefaultConnection().getUrl();
			ProjectConfig.MVCType  = newProjectDialog.getMVCType();
			
			Properties prop = new Properties();
			prop.put("projectPath", projectPath);
			prop.put("projectName", projectName);
			prop.put("IbatisType", newProjectDialog.getIbatisType());
			prop.put("MVCType", newProjectDialog.getMVCType());
			prop.put("DefaultConnecton", newProjectDialog.getDefaultConnection().getUrl());
			File f = new File(projectPath);	
			f.mkdir();
			
			f = new File(projectPath + "/gen.project");
			if(f.exists())
				f.delete();
			f.getParentFile().mkdirs();
			f.createNewFile();
			OutputStream fos = new FileOutputStream(f);
			prop.store(fos, "project setting...");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成配置目录及相关文件
	 * @param projectPath
	 */
	public static void createConfig(String projectPath) {
		File f = new File(projectPath + "/src");
		f.mkdirs();
		
		f = new File(projectPath + "/WebRoot/WEB-INF/mybatis-config");
		f.mkdirs();
		
		f = new File(projectPath + "/WebRoot/WEB-INF/spring-config");
		f.mkdirs();
		
		f = new File(projectPath + "/.settings");
		f.mkdirs();
		
		f = new File(projectPath + "/WebRoot/WEB-INF");
		f.mkdirs();
		
		try {
			TextFileOperator tw = new TextFileOperator();
			
			//1. 生成mybatis mapper文件
			f = new File(projectPath + "/WebRoot/WEB-INF/mybatis-config/config-mappers.xml");
			f.createNewFile();
			tw.copyFile(Resources.getResources().getResourceStream("mybatis/config-mappers.xml.templete"), f);
			
			//2. 生成spring配置
			f = new File(projectPath + "/WebRoot/WEB-INF/spring-config/applicationContext.xml");
			f.createNewFile();
			tw.copyFile(Resources.getResources().getResourceStream("spring/applicationContext.xml.templete"), f);
			
			f = new File(projectPath + "/WebRoot/WEB-INF/spring-config/springmvc-servlet.xml");
			f.createNewFile();
			tw.copyFile(Resources.getResources().getResourceStream("spring/springmvc-servlet.xml.templete"), f);
			
			//4. 生成web.xml
			f = new File(projectPath + "/WebRoot/WEB-INF/web.xml");
			f.createNewFile();
			tw.copyFile(Resources.getResources().getResourceStream("web/web.xml.templete"), f);
			
			ClassConfigGenerator.generateMainConfig(projectPath);
		} catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
	}
}
