package eddy.generate.javaclass.ibatis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import eddy.database.DatabaseTable;
import eddy.frame.docpane.TextFileOperator;
import eddy.generate.javaclass.IbatisConfigPane;
import eddy.generate.javaclass.JavaClass;
import eddy.log.DebugLogger;

public class IBatisGenerator_MyBatis implements IBatisGenerator {
	@Override
	public void createSqlFile(IbatisConfigPane ibatsiConfig, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist, String sqlFilePath) throws IOException {
		File tempFile = new File(sqlFilePath);
		tempFile.createNewFile();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		strBuf.append("\n<!DOCTYPE mapper    PUBLIC \"-//ibatis.apache.org//DTD Mapper 3.0//EN\"    \"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd\">");
		strBuf.append("\n<mapper namespace=\"" + ibatsiConfig.getDaoName() + "\">");
		
		if(ibatsiConfig.getInsertSqlID() != null) {
			String insertSql = createInsertSql(ibatsiConfig.getInsertSqlID(), className, dbTable, jclist);
			strBuf.append("\n" + insertSql);
		}
		
		if(ibatsiConfig.getSelectSqlID() != null) {
			String selectSql = createSelectSql(ibatsiConfig.getSelectSqlID(), className, dbTable, jclist);
			strBuf.append("\n" + selectSql);
		}
		
		if(ibatsiConfig.getUpdateSqlID() != null) {
			String selectSql = createUpdateSql(ibatsiConfig.getUpdateSqlID(), className, dbTable, jclist);
			strBuf.append("\n" + selectSql);
		}
		
		String ts = createSelectByIdSql(className, dbTable);
		strBuf.append("\n" + ts);
		
		if(ibatsiConfig.getDeleteSqlID() != null) {
			String selectSql = createDeleteSql(ibatsiConfig.getDeleteSqlID(), className, dbTable, jclist);
			strBuf.append("\n" + selectSql);
		}
		
		if(ibatsiConfig.getSelectLimitCountSqlID() != null) {
			String selectLimitCountSql = createSelectLimitCountSql(ibatsiConfig.getSelectLimitCountSqlID(), className, dbTable, jclist);
			strBuf.append("\n" + selectLimitCountSql);
		}

		if(ibatsiConfig.getSelectByLimitSqlID() != null) {
			String selectByLimitSql = createSelectByLimitSql(ibatsiConfig.getSelectByLimitSqlID(), className, dbTable, jclist);
			strBuf.append("\n" + selectByLimitSql);
		}
		
		strBuf.append("\n</mapper>");
		TextFileOperator tw = new TextFileOperator();
		tw.writeToFile(tempFile, strBuf.toString());
	}
	
	@Override
	public String createDeleteSql(String deleteId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t<delete id=\"" + deleteId + "\" parameterType=\"int\">");
		strBuf.append("\n\t\tDELETE FROM " + dbTable.getTableName().toLowerCase() + " WHERE id=#{id}");
		strBuf.append("\n\t</delete>");
		return strBuf.toString();
	}

	@Override
	public String createInsertSql(String insertId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t<insert id=\"insert\" parameterType=\"" + className + "\">");
		strBuf.append("\n\t\tinsert into " + dbTable.getTableName().toLowerCase() + "(\n\t\t\t");
		JavaClass jc = null;
		for(int i = 0; i < jclist.size(); i++) {
			jc = jclist.get(i);
			strBuf.append(jc.getFieldName());
			if(i < jclist.size() - 1)
				strBuf.append(",");
		}
		strBuf.append(")");
		strBuf.append("\n\t\tvalues (\n\t\t\t");
		for(int i = 0; i < jclist.size(); i++) {
			jc = jclist.get(i);
			strBuf.append( "#{" + jc.getFieldName() + "}");
			if(i < jclist.size() - 1)
				strBuf.append(",");
		}
		strBuf.append("\n\t\t)");
		strBuf.append("\n\t</insert>");
		return strBuf.toString();
	}

	@Override
	public String createSelectByIdSql(String className, DatabaseTable dbTable) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t<select id=\"selectById\" parameterType=\"int\" resultType=\"" + className + "\">");
		strBuf.append("\n\t\tselect * from " + dbTable.getTableName().toLowerCase() + " where id=#{id}");
		strBuf.append("\n\t</select>");
		
		return strBuf.toString();
	}

	@Override
	public String createSelectByLimitSql(String selectByLimitSqlID, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t<select id=\"" + selectByLimitSqlID + "\" parameterType=\""+className + "\" resultType=\"" + className + "\">");
		strBuf.append("\n\t\tSELECT * from " + dbTable.getTableName().toLowerCase());
		strBuf.append("\n\t\tWHERE 1=1 ");
		JavaClass jc = null;
		for(int i = 0; i < jclist.size(); i++) {
			jc = jclist.get(i);
			strBuf.append("\n\t\t");
			strBuf.append("<if test=\"" + jc.getFieldName() + " != null ");
			if(jc.getFieldClass().endsWith("String"))
				strBuf.append(" and " + jc.getFieldName() + " != ''" );
			strBuf.append("\">");
			strBuf.append(" AND " + jc.getFieldName() + "=#{" + jc.getFieldName() + "}");
			strBuf.append("</if>");
		}
		
		strBuf.append("\n\t\t<if test=\"extLimit.sort != null\">order by ${extLimit.sort} ${extLimit.dir}</if>");
		strBuf.append("\n\t\t<if test=\"extLimit.limit != null\"> limit ${extLimit.start}, ${extLimit.limit}</if>");
		strBuf.append("\n\t</select>");
		return strBuf.toString();
	}

	@Override
	public String createSelectLimitCountSql(String SelectLimitCountSql, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t<select id=\"" + SelectLimitCountSql + "\" parameterType=\""+className + "\" resultType=\"java.lang.Integer\">");
		strBuf.append("\n\t\tSELECT count(1) from " + dbTable.getTableName().toLowerCase());
		strBuf.append("\n\t\tWHERE 1=1 ");
		JavaClass jc = null;
		for(int i = 0; i < jclist.size(); i++) {
			jc = jclist.get(i);
			strBuf.append("\n\t\t");
			strBuf.append("<if test=\"" + jc.getFieldName() + " != null ");
			if(jc.getFieldClass().endsWith("String"))
				strBuf.append(" and " + jc.getFieldName() + " != ''" );
			strBuf.append("\">");
			strBuf.append(" AND " + jc.getFieldName() + "=#{" + jc.getFieldName() + "}");
			strBuf.append("</if>");
		}
		strBuf.append("\n\t</select>");
		return strBuf.toString();
	}

	@Override
	public String createSelectSql(String selectId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t<select id=\"" + selectId + "\" parameterType=\""+className + "\" resultType=\"" + className + "\">");
		strBuf.append("\n\t\tSELECT * from " + dbTable.getTableName().toLowerCase());
		strBuf.append("\n\t\tWHERE 1=1 ");
		JavaClass jc = null;
		for(int i = 0; i < jclist.size(); i++) {
			jc = jclist.get(i);
			strBuf.append("\n\t\t");
			strBuf.append("<if test=\"" + jc.getFieldName() + " != null ");
			if(jc.getFieldClass().endsWith("String"))
				strBuf.append(" and " + jc.getFieldName() + " != ''" );
			strBuf.append("\">");
			strBuf.append(" AND " + jc.getFieldName() + "=#{" + jc.getFieldName() + "}");
			strBuf.append("</if>");
		}
		strBuf.append("\n\t</select>");
		return strBuf.toString();
	}

	@Override
	public String createUpdateSql(String updateId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist) {
		List<JavaClass> notNullList = new ArrayList<JavaClass>();
		for(JavaClass jc : jclist) {
			if(jc.isPrimaryKey() || jc.getIsNull() == ResultSetMetaData.columnNoNulls)
				notNullList.add(jc);
		}
		
		List<JavaClass> primaryLeyList = new ArrayList<JavaClass>();
		for(JavaClass jc : jclist) {
			if(jc.isPrimaryKey())
				primaryLeyList.add(jc);
		}
		
		StringBuffer updateBuf = new StringBuffer();
		updateBuf.append("\n\t<update id=\"" + updateId + "\" parameterType=\""+className + "\">");
		updateBuf.append("\n\t\tUPDATE " + dbTable.getTableName().toLowerCase() + " SET");
		JavaClass jc = null;
		for(int i = 0; i < jclist.size(); i++) {
			jc = jclist.get(i);
			updateBuf.append("\n\t\t");
			updateBuf.append("<if test=\"" + jc.getFieldName() + " != null\">");
			updateBuf.append(jc.getFieldName() + "=#{" + jc.getFieldName() + "}" + ",");
			updateBuf.append("</if>");
		}
		
		for(int i = 0; i < primaryLeyList.size(); i++) {
			jc = primaryLeyList.get(i);
			updateBuf.append("\n\t\t " + jc.getFieldName() + "=#{" + jc.getFieldName() + "}");
			if(i != primaryLeyList.size() - 1)
				updateBuf.append(", ");
		}
		
		updateBuf.append("\n\t\tWHERE");
		for(int i = 0; i < primaryLeyList.size(); i++) {
			jc = primaryLeyList.get(i);
			updateBuf.append("\n\t\t ");
			if(i > 0)
				updateBuf.append("AND ");
			updateBuf.append(jc.getFieldName() + "=#{" + jc.getFieldName() + "}");
		}
		updateBuf.append("\n\t</update>");
		return updateBuf.toString();
	}

	public void createConfigFile(String filePath) throws Exception {
		StringBuffer context = new StringBuffer();
		context.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		context.append("<!DOCTYPE configuration   PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"   \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n");
		context.append("<configuration>");
		context.append("\t<mappers>");
		context.append("\t</mappers>");
		context.append("</configuration>");
		File file = new File(filePath);
		TextFileOperator tw = new TextFileOperator();
		tw.writeToFile(file, context.toString());
	}

	@Override
	public void addResourceToSqlMapFile(String projectPath, String resourceFilePath) {
		try {
			String configFilePath = projectPath + "\\WebRoot\\WEB-INF\\mybatis-config\\config-mappers.xml";
			File file = new File(configFilePath);
			if(!file.exists()) {
				createConfigFile(configFilePath);
			}
			
			SAXReader saxReader =  new SAXReader();
			Document doc = saxReader.read(new FileInputStream(file));
			Element root = doc.getRootElement();
			Element mappers = root.element("mappers");
			mappers.addElement("mapper").addAttribute("resource", resourceFilePath);
			
			OutputFormat fmt = new OutputFormat();
			fmt.setEncoding("utf-8");
			fmt.setIndent(true); //设置是否缩进
			fmt.setIndent("\t"); //以空格方式实现缩进
			fmt.setNewlines(true); //设置是否换行
			
			XMLWriter writer = new XMLWriter(fmt);
			OutputStream out = new FileOutputStream(configFilePath);
			writer.setOutputStream(out);
			writer.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().err(e);
		}
	}
}
