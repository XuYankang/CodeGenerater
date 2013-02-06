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
 * �ı��ļ�������
 * @author Eddy
 *
 */
public class TextFileOperator {
	
	/**
	 * 
	 * @param toFile д����ļ�
	 * @param info ��Ϣ Ĭ����UTF-8��ʽ�洢
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
	 * @param toFile д����ļ�
	 * @param info ��Ϣ
	 * @param encoding ���뷽ʽ
	 * @throws IOException
	 */
	public void writeToFile(File toFile, String info, String encoding) throws IOException {
		FileOutputStream fos = new FileOutputStream(toFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, encoding); 
		osw.write(info);
		osw.close();
	}
	
	/**
	 * ��ȡ�ļ�
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
	 * �����ı��ļ�
	 * @param fromFile Դ�ļ�
	 * @param toFile Ŀ���ļ�
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
	 * �����ı��ļ�
	 * @param fromFile Դ�ļ�
	 * @param toFile Ŀ���ļ�
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
