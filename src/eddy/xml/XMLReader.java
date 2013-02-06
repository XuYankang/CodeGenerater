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
	private XPath path;//XPath来获得所需要的字段
	private Document document = null;
	
	/**
	 * 构造XmlReader
	 * @param xmlData xml格式的字符串
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
	 * 构造XmlReader
	 * @param data xml字节数据
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
	 * 构造XmlReader
	 * @param file xml文件
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
	 * 由xpath表达式得到结点或属性的值<br>
	 * 输入xpath表达式，如：<br>
	 * 例1./configuration/DfkyBox/CommSet/CommName<br>
	 *     表示结点CommName<br>
	 * 例2. /sqlMapConfig/transactionManager/dataSource/property[1]/@value <br>
	 *     property[1]:表示结点dataSource下的第一个property结点，从1开始<br>
	 *     "@value":表示property结点的value属性
	 * @param nodePath 结点路径，如：/configuration/DfkyBox/CommSet/CommName
	 * @return xpath表达式的值
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
	 * 返回子结点列表，如果结点为空，则反回空
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
	 * 得到结点，不存在时返回空
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
	 * 得到结点的值
	 * @param tagName 结点名，用这个方法时要保证XML文件的结点名是唯一的，不然也许会得不到你想要的数据
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
