package eddy.generate.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import eddy.frame.docpane.TextFileOperator;

public class FileCreator {
	
	/**
	 * ����JAVA��
	 * @param classFileName ȫ�޶�����
	 * @param dirPath Դ�ļ���·��������ô�/��
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
	 * ����JAVA��
	 * @param classFileName ȫ�޶�����
	 * @param dirPath Դ�ļ���·��������ô�/��
	 * @param context �ļ�����
	 * @throws IOException
	 */
	public static File createJavaFile(String classFileName, String dirPath, String context) throws IOException {
		File file = createJavaFile(classFileName, dirPath);
		TextFileOperator tw = new TextFileOperator();
		tw.writeToFile(file, context);
		return file;
	}
	
	/**
	 * DOM4J����XML�ļ�
	 * @param doc Document����
	 * @param filePath xml�ļ�����·��
	 * @throws Exception
	 */
	public static void saveXmlDoc(Document doc, String filePath) throws Exception {
		OutputFormat fmt = new OutputFormat();
		fmt.setEncoding("utf-8");
		fmt.setIndent(true); //�����Ƿ�����
		fmt.setIndent("\t"); //�Կո�ʽʵ������
		fmt.setNewlines(true); //�����Ƿ���
//		
		XMLWriter writer = new XMLWriter(fmt);
		OutputStream out = new FileOutputStream(filePath);
		writer.setOutputStream(out);
		writer.write(doc);
	}
	
}
