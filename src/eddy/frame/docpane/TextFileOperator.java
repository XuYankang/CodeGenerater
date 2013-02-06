package eddy.frame.docpane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文本文件操作类
 * @author Eddy
 *
 */
public class TextFileOperator {
	
	/**
	 * 
	 * @param toFile 写入的文件
	 * @param info 信息 默认以UTF-8格式存储
	 * @throws IOException
	 */
	public void writeToFile(File toFile, String info) throws IOException {
		FileOutputStream fos = new FileOutputStream(toFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
		osw.write(info);
		osw.close();
	}
	
	/**
	 * 
	 * @param toFile 写入的文件
	 * @param info 信息
	 * @param encoding 编码方式
	 * @throws IOException
	 */
	public void writeToFile(File toFile, String info, String encoding) throws IOException {
		FileOutputStream fos = new FileOutputStream(toFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, encoding); 
		osw.write(info);
		osw.close();
	}
	
	/**
	 * 读取文件
	 * @param fromFile
	 * @return
	 * @throws Exception
	 */
	public StringBuffer readFile(InputStream ins) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		StringBuffer fileBuf = new StringBuffer();
		String line = null;
		while((line = reader.readLine()) != null) {
			fileBuf.append(line + "\n");
		}
		reader.close();
		return fileBuf;
	}
	
	/**
	 * 拷贝文本文件
	 * @param fromFile 源文件
	 * @param toFile 目标文件
	 * @throws Exception
	 */
	public void copyFile(File fromFile, File toFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fromFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(toFile));
		
		String line = null;
		while((line = reader.readLine()) != null) {
			writer.write(line + "\n");
		}
		reader.close();
		writer.close();
	}
	
	public void copyFile(InputStream ins, File toFile) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		BufferedWriter writer = new BufferedWriter(new FileWriter(toFile));
		
		String line = null;
		while((line = reader.readLine()) != null) {
			writer.write(line + "\n");
		}
		reader.close();
		writer.close();
	}
	
	/**
	 * 拷贝文本文件
	 * @param fromFile 源文件
	 * @param toFile 目标文件
	 * @throws Exception
	 */
	public void copyFile(BufferedReader reader, BufferedWriter writer) throws Exception {
		String line = null;
		while((line = reader.readLine()) != null) {
			writer.write(line + "\n");
		}
		reader.close();
		writer.close();
	}
}
