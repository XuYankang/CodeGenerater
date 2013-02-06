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
	private XPath path;//XPath来获得所需要的字段
	private Document document = null;
	private String filePath;//XML配置文件路径
	
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
	
	public Document getDocument() {
		return document;
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
	
	public void updateNodeValue(String tagName, String value) {
		this.updateNodeValue(tagName, value, false);
	}
	
	/**
	 * 设置结点的值，当结点名为唯一时可以用这个方法
	 * @param tagName 结点名
	 * @param value 结点值
	 * @param isSave 是否保存
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
	 * 更新路径处结点的值,路径就是XPath表达式，可以用该方法修改结点的值以及结点的属性值等<br>
	 * 该方法不保存修改信息，如要保存需要调用saveConfiger()方法
	 * @param nodePath XPath表达式
	 * @param value 新值
	 */
	public void updateAbsoluteNodeValue(String nodePath, String value) {
		this.updateAbsoluteNodeValue(nodePath, value, false);
	}
	
	/**
	 * 更新路径处结点的值,路径就是XPath表达式，可以用该方法修改结点的值以及结点的属性值等
	 * @param nodePath XPath表达式
	 * @param value 新值
	 * @param isSave 是否保存
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
