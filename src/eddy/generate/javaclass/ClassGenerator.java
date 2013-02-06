package eddy.generate.javaclass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import resources.Resources;


import eddy.database.DatebaseColumn;
import eddy.frame.docpane.TextFileOperator;
import eddy.generate.util.FileCreator;
import eddy.log.DebugLogger;

public class ClassGenerator {
	
	public static String generateClassFromJavaClasslist(String className, ArrayList<JavaClass> javaClassList) {
		StringBuffer strbuf = new StringBuffer();
		int n = className.lastIndexOf(".");
		
		if(n > 0) {
			String packageName = className.substring(0, n);
			className = className.substring(n + 1);
			strbuf.append("package " + packageName + ";\n");
		}
		
		strbuf.append("public class " + className + " extends com.eddy.dao.base.BaseEntity {\n");
		strbuf.append("\n");
		
		for(JavaClass jc : javaClassList) {
			String fieldName = jc.getFieldName();
			String fieldClass = jc.getFieldClass();
			
			strbuf.append("\tprivate " + fieldClass + " " + fieldName + ";");
			if(jc.getComment() != null && !jc.getComment().trim().equals("")) {
				strbuf.append("\t//" + jc.getComment());
			}
			
			strbuf.append("\n");
		}
		
		strbuf.append("\n");
		
		for(JavaClass jc : javaClassList) {
			String fieldName = jc.getFieldName();
			String fieldClass = jc.getFieldClass();
			
			String c = fieldName.charAt(0) + "";
			c = c.toUpperCase();
			String UpfieldName = c + fieldName.substring(1);

			if(jc.getComment() != null && !jc.getComment().trim().equals("")) {
				strbuf.append("\t/**\n");
				strbuf.append("\t* 得到 " + jc.getComment() + "\n");
				strbuf.append("\t* @return " + jc.getComment() + " : " + fieldClass  + "\n");
				strbuf.append("\t*/\n");
			}
			
			strbuf.append("\tpublic " + fieldClass + " get" + UpfieldName + "() {");
			strbuf.append("\n\t");
			strbuf.append("\treturn this." + fieldName + ";\n\t}");
			strbuf.append("\n");
			
			if(jc.getComment() != null && !jc.getComment().trim().equals("")) {
				strbuf.append("\t/**\n");
				strbuf.append("\t * 设置 " + jc.getComment() + "\n");
				strbuf.append("\t * @param " + fieldName + ", " + jc.getComment() + " : " + fieldClass + "\n");
				strbuf.append("\t*/\n");
			}
			strbuf.append("\tpublic void set" + UpfieldName + "(" + fieldClass + " " + fieldName + ") {");
			strbuf.append("\n\t");
			strbuf.append("\tthis." + fieldName + " = " + fieldName + ";\n\t}");
			strbuf.append("\n");
			strbuf.append("\n");
		}
		
		strbuf.append("}");
		return strbuf.toString();
	}
	
	/**
	 * 生成JAVA类
	 * @param className 类名
	 * @param tableColumnList 数据库字体列表
	 * @return
	 */
	public static String generateClassFromDateBaseColumns(String className, ArrayList<DatebaseColumn> tableColumnList) {
		ArrayList<JavaClass> javaClassList = new ArrayList<JavaClass>();
		for (DatebaseColumn dc : tableColumnList) {
			JavaClass jc = DBColumnToJavaClass.createJavaClass(dc);
			javaClassList.add(jc);
		}
		
		return generateClassFromJavaClasslist(className, javaClassList);
	}
	
	public static String getJavaType(int type) {
		switch (type) {
		case java.sql.Types.DECIMAL:
		case java.sql.Types.INTEGER:
			return "Integer";
		case java.sql.Types.DATE:
			return "java.lang.Date";	
		default:
			return "Integer";
		}
	}
	
	@Deprecated
	public static void createDaoConfig(String srcPath) throws Exception {
		File f = new File(srcPath + "/com/dao");
		f.mkdirs();
		
		f = new File(srcPath + "/com/dao/DaoConfig.java");
		TextFileOperator tw = new TextFileOperator();
		tw.copyFile(Resources.getResources().getResourceStream("DaoConfig.java.templete"), f);
	}
	
	/**
	 * 生成实体类
	 * @param className
	 * @param selectFileNodePath
	 * @param jclist
	 * @throws IOException
	 */
	public static void createEty(String className, String selectFileNodePath, ArrayList<JavaClass> jclist) throws IOException {
		File file = FileCreator.createJavaFile(className, selectFileNodePath); 
		String classStr = generateClassFromJavaClasslist(className, jclist);
		TextFileOperator tw = new TextFileOperator();
		tw.writeToFile(file, classStr);
	}
	
	/**
	 * 生成DAO层
	 * @param ibatsiConfig
	 * @param etyClassName
	 * @param projectPath
	 * @throws IOException
	 */
	public static void createIfaceDao(IbatisConfigPane ibatsiConfig, String etyClassName, String projectPath) {
		try {
			File file = FileCreator.createJavaFile(ibatsiConfig.getDaoName(), projectPath + "/src");
			
			StringBuffer strbuf = new StringBuffer();
			
			String daoName = ibatsiConfig.getDaoName();
			int n = daoName.lastIndexOf(".");
			if(n > 0) {
				String packageName = daoName.substring(0, n);
				daoName = daoName.substring(n + 1);
				strbuf.append("package " + packageName + ";\n");
			}
			
			strbuf.append("\npublic interface " + daoName +" extends com.eddy.dao.base.BaseDao {\n");
			strbuf.append("}");
			
			TextFileOperator tw = new TextFileOperator();
			tw.writeToFile(file, strbuf.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().log(e.getMessage());
		}
	}
}
