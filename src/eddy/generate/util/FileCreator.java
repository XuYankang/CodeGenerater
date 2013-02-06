package eddy.generate.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import eddy.frame.docpane.TextFileOperator;

public class FileCreator {
	
	/**
	 * 生成JAVA类
	 * @param classFileName 全限定类名
	 * @param dirPath 源文件夹路径（最后不用带/）
	 * @throws IOException
	 */
	public static File createJavaFile(String classFileName, String dirPath) throws IOException {
		int n = classFileName.lastIndexOf(".");
		if(n > 0) {
			String packageName = classFileName.substring(0, n);
			packageName = packageName.replaceAll("\\.", "\\\\");
			File packDir = new File(dirPath + "\\" + packageName);
			packDir.mkdirs();
		}
		
		String classPathName = classFileName.replaceAll("\\.", "\\\\");
		classPathName += ".java";
		File newFile = new File(dirPath + "\\" + classPathName);
		if(newFile.createNewFile())
			return newFile;
		else
			return null;
	}
	
	/**
	 * 生成JAVA类
	 * @param classFileName 全限定类名
	 * @param dirPath 源文件夹路径（最后不用带/）
	 * @param context 文件内容
	 * @throws IOException
	 */
	public static File createJavaFile(String classFileName, String dirPath, String context) throws IOException {
		File file = createJavaFile(classFileName, dirPath);
		TextFileOperator tw = new TextFileOperator();
		tw.writeToFile(file, context);
		return file;
	}
	
	/**
	 * DOM4J保存XML文件
	 * @param doc Document对象
	 * @param filePath xml文件完整路径
	 * @throws Exception
	 */
	public static void saveXmlDoc(Document doc, String filePath) throws Exception {
		OutputFormat fmt = new OutputFormat();
		fmt.setEncoding("utf-8");
		fmt.setIndent(true); //设置是否缩进
		fmt.setIndent("\t"); //以空格方式实现缩进
		fmt.setNewlines(true); //设置是否换行
//		
		XMLWriter writer = new XMLWriter(fmt);
		OutputStream out = new FileOutputStream(filePath);
		writer.setOutputStream(out);
		writer.write(doc);
	}
	
}
