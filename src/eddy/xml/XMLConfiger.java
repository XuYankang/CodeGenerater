package eddy.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLConfiger {
	private DocumentBuilder builder = null;
	private XPath path;//XPath���������Ҫ���ֶ�
	private Document document = null;
	private String filePath;//XML�����ļ�·��
	
	@SuppressWarnings("deprecation")
	public XMLConfiger(String filepath) {
		File f = new File(filepath);
		URL url = null;
		try {
			url = f.toURL();
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		filePath = url.getPath();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		XPathFactory xpFactory = XPathFactory.newInstance();
		path = xpFactory.newXPath();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(filePath);
//			System.out.println("DocumentURI: " + document.getDocumentURI());
//			System.out.println("XmlVersion: " + document.getXmlVersion());
//			System.out.println("InputEncoding: " + document.getInputEncoding());
//			System.out.println("Doctype().getName: " + document.getDoctype().getName());
//			System.out.println("Doctype().getPublicId: " + document.getDoctype().getPublicId());
//			System.out.println("Doctype().getSystemId: " + document.getDoctype().getSystemId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��xpath���ʽ�õ��������Ե�ֵ<br>
	 * ����xpath���ʽ���磺<br>
	 * ��1./configuration/DfkyBox/CommSet/CommName<br>
	 *     ��ʾ���CommName<br>
	 * ��2. /sqlMapConfig/transactionManager/dataSource/property[1]/@value <br>
	 *     property[1]:��ʾ���dataSource�µĵ�һ��property��㣬��1��ʼ<br>
	 *     "@value":��ʾproperty����value����
	 * @param nodePath ���·�����磺/configuration/DfkyBox/CommSet/CommName
	 * @return xpath���ʽ��ֵ
	 */
	public String getNodeValue(String nodePath) {
		Node node = null;
		try {
			node = (Node)path.evaluate(nodePath, document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return node.getTextContent();
	}
	
	/**
	 * �����ӽ���б�������Ϊ�գ��򷴻ؿ�
	 * @param nodePath
	 * @return
	 * @throws Exception
	 */
	public NodeList getNodeList(String nodePath) throws Exception {
		NodeList nodeList = null;
		Node node = (Node)path.evaluate(nodePath, document, XPathConstants.NODE);
		if(node == null)
			return null;
		nodeList = node.getChildNodes();
		return nodeList;
	}
	
	public Document getDocument() {
		return document;
	}
	
	/**
	 * �õ���㣬������ʱ���ؿ�
	 * @param nodePath
	 * @return
	 * @throws Exception
	 */
	public Node getNode(String nodePath) throws Exception {
		Node node = (Node)path.evaluate(nodePath, document, XPathConstants.NODE);
		return node;
	}
	
	public Object evaluate(String xpath) throws Exception {
		return path.evaluate(xpath, document);
	}
	
	/**
	 * �õ�����ֵ
	 * @param tagName ����������������ʱҪ��֤XML�ļ��Ľ������Ψһ�ģ���ȻҲ���ò�������Ҫ������
	 * @return
	 */
	public String getTagValue(String tagName) {
		String value = "";
		NodeList nodelist = document.getElementsByTagNameNS("*", tagName);
		for(int i = 0; i < nodelist.getLength(); i++) {
			Element node = (Element) nodelist.item(i);
			value = node.getTextContent();
		}
		
		return value;
	}
	
	public void updateNodeValue(String tagName, String value) {
		this.updateNodeValue(tagName, value, false);
	}
	
	/**
	 * ���ý���ֵ���������ΪΨһʱ�������������
	 * @param tagName �����
	 * @param value ���ֵ
	 * @param isSave �Ƿ񱣴�
	 */
	public void updateNodeValue(String tagName, String value, boolean isSave) {
		NodeList nodelist = document.getElementsByTagNameNS("*", tagName);
		for(int i = 0; i < nodelist.getLength(); i++) {
			Element node = (Element) nodelist.item(i);
			node.setTextContent(value);
			if(isSave)
				saveConfiger();
		}
	}
	
	/**
	 * ����·��������ֵ,·������XPath���ʽ�������ø÷����޸Ľ���ֵ�Լ���������ֵ��<br>
	 * �÷����������޸���Ϣ����Ҫ������Ҫ����saveConfiger()����
	 * @param nodePath XPath���ʽ
	 * @param value ��ֵ
	 */
	public void updateAbsoluteNodeValue(String nodePath, String value) {
		this.updateAbsoluteNodeValue(nodePath, value, false);
	}
	
	/**
	 * ����·��������ֵ,·������XPath���ʽ�������ø÷����޸Ľ���ֵ�Լ���������ֵ��
	 * @param nodePath XPath���ʽ
	 * @param value ��ֵ
	 * @param isSave �Ƿ񱣴�
	 */
	public void updateAbsoluteNodeValue(String nodePath, String value, boolean isSave) {
		try {
			Node node = (Node)path.evaluate(nodePath, document, XPathConstants.NODE);
			node.setTextContent(value);
			
			if(isSave)
				saveConfiger();
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfiger() {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			if(document.getInputEncoding() != null)
				transformer.setOutputProperty(OutputKeys.ENCODING, document.getInputEncoding());
			if(document.getXmlEncoding() != null)
				transformer.setOutputProperty(OutputKeys.ENCODING, document.getXmlEncoding());
			if(document.getXmlVersion() != null)
				transformer.setOutputProperty(OutputKeys.VERSION, document.getXmlVersion());
			if(document.getDoctype() != null) {
				if(document.getDoctype().getPublicId() != null)
					transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId());
				if(document.getDoctype().getSystemId() != null)
					transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
			}
			PrintWriter pw = new PrintWriter(new FileOutputStream(filePath));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCommName() {
		String commName = "";
		try {
			commName = path.evaluate("/configuration/DfkyBox/CommSet/CommName", document);
			System.out.println(commName);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return commName;
	}
	
	public static void main(String[] args) {
		XMLConfiger configer  = new XMLConfiger("ibatis-sqlmap-oracle.xml");
		String text = "";
		
		text = configer.getNodeValue("/sqlMapConfig/transactionManager/@type");
		System.out.println(text);
		
		text = configer.getNodeValue("/sqlMapConfig/transactionManager/dataSource/@type");
		System.out.println(text);
		
		text = configer.getNodeValue("/sqlMapConfig/transactionManager/dataSource/property[1]/@name");
		System.out.println(text);
		
		text = configer.getNodeValue("/sqlMapConfig/transactionManager/dataSource/property[1]/@value");
		System.out.println(text);
		
		text = configer.getNodeValue("/sqlMapConfig/transactionManager/dataSource/property[2]/@value");
		System.out.println(text);
		configer.updateAbsoluteNodeValue("/sqlMapConfig/transactionManager/dataSource/property[2]/@value", "jdbc:oracle:thin:@192.168.1.6:1521:data", true);
	}
}
