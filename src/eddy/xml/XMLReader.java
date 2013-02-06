package eddy.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XMLReader {
	private DocumentBuilder builder = null;
	private XPath path;//XPath���������Ҫ���ֶ�
	private Document document = null;
	
	/**
	 * ����XmlReader
	 * @param xmlData xml��ʽ���ַ���
	 */
	public XMLReader(String xmlData) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		XPathFactory xpFactory = XPathFactory.newInstance();
		path = xpFactory.newXPath();
		try {
			StringReader read = new StringReader(xmlData);
	        InputSource source = new InputSource(read);
			builder = factory.newDocumentBuilder();
			document = builder.parse(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����XmlReader
	 * @param data xml�ֽ�����
	 */
	public XMLReader(byte[] data) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		XPathFactory xpFactory = XPathFactory.newInstance();
		path = xpFactory.newXPath();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new ByteArrayInputStream(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����XmlReader
	 * @param file xml�ļ�
	 */
	public XMLReader(File file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setIgnoringElementContentWhitespace(true);
		XPathFactory xpFactory = XPathFactory.newInstance();
		
		path = xpFactory.newXPath();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(file);
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
	
	public String getXMLString() {
		
		
		
		return "";
	}
	
	public static void main(String[] args) {
	}
}
