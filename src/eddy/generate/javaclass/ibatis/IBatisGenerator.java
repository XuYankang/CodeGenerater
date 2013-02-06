package eddy.generate.javaclass.ibatis;

import java.io.IOException;
import java.util.ArrayList;

import eddy.database.DatabaseTable;
import eddy.generate.javaclass.IbatisConfigPane;
import eddy.generate.javaclass.JavaClass;

public interface IBatisGenerator {
	
	/**
	 * 添加到配置文件
	 * @param projectPath
	 * @param resourceFilePath
	 */
	void addResourceToSqlMapFile(String projectPath, String resourceFilePath);
	
	/**
	 * 生成SQL文件
	 * @param ibatsiConfig
	 * @param className
	 * @param dbTable
	 * @param jclist
	 * @param sqlFilePath
	 * @throws IOException
	 */
	void createSqlFile(IbatisConfigPane ibatsiConfig, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist, String sqlFilePath) throws IOException;
	
	String createSelectByLimitSql(String selectByLimitSqlID, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist);
	
	String createSelectLimitCountSql(String SelectLimitCountSql, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist);
	
	String createInsertSql(String insertId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist);
	
	String createSelectByIdSql(String className, DatabaseTable dbTable);
	
	String createSelectSql(String selectId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist);
	
	String createUpdateSql(String updateId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist);
	
	String createDeleteSql(String deleteId, String className, DatabaseTable dbTable, ArrayList<JavaClass> jclist);
	
	
}
