package eddy.generate.webmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import resources.Resources;
import eddy.frame.docpane.TextFileOperator;
import eddy.generate.config.ClassConfigGenerator;

/**
 * JS模块类生成
 * @author User
 *
 */
public class JSGeneratorN {
	private WebModelGenerateAssistant generateAssistant;
	private String projectPath;
	private List<Element> fieldsList;
	
	public JSGeneratorN(WebModelGenerateAssistant generateAssistant, String projectPath) {
		this.generateAssistant = generateAssistant;
		this.projectPath = projectPath;
	}
	
	/**
	 * 生成JS模块
	 * @throws Exception
	 */
	public void generateJSFile() throws Exception {
		SAXReader saxReader =  new SAXReader();
		String selEntityId = (String) generateAssistant.entityCombo.getSelectedItem();
		String entityFilePath = ClassConfigGenerator.getEntityConfigPath(projectPath, selEntityId);
		Document doc = saxReader.read(new FileInputStream(new File(entityFilePath)));
		fieldsList = doc.getRootElement().element("Attributes").elements("filed");
		Map<String, Element> fieldsMap = new HashMap<String, Element>();
		for(int k = 0; k < fieldsList.size(); k++) {
			Element node = fieldsList.get(k);
			String fieldName = node.attribute("name").getStringValue();
			fieldsMap.put(fieldName.toLowerCase(), node);
		}
		
		//1. 读取模板文件
		InputStream ins = Resources.getResources().getResourceStream("web/JSModule.js.templete");
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
		StringBuffer fileBuf = new StringBuffer();
		String line = null;
		while((line = reader.readLine()) != null) {
			//2. 替换其中的关键字段
			line = line.replace("${ModuleComments}", "comments");
			line = line.replace("${ModuleClassFullName}", generateAssistant.controllerNameField.getText() + "Panel");
			line = line.replace("${ModuleClassHideID}", generateAssistant.primaryKeyHiddenField.getText());
			
			String mapping = generateAssistant.requestMappingField.getText();
			
			String actionpath = ".." + mapping + "getDetailInfo.action";
			line = line.replace("${ModuleActionGetDetailInfo}", actionpath);
			
			actionpath = ".." + mapping + "save.action";
			line = line.replace("${ModuleActionSave}", actionpath);
			
			actionpath = ".." + mapping + "search.action";
			line = line.replace("${ModuleActionSearch}", actionpath);
			
			actionpath = ".." + mapping + "delete.action";
			line = line.replace("${ModuleActionDelete}", actionpath);
			
			//生成表单Form
			StringBuffer editFormItemsBuffer = new StringBuffer();
			String[] fields = generateAssistant.editFormField.getText().split(",");
			for(int i = 0; i < fields.length; i++) {
				Element node = fieldsMap.get(fields[i].toLowerCase());
				if(node != null) {
					editFormItemsBuffer.append("\n\t\t\t\t{");
					String type = node.attribute("type").getStringValue();
					if(type.toLowerCase().endsWith("date")) {
						editFormItemsBuffer.append("xtype: 'datefield',format:'Y-m-d',");
					}
					else {
						editFormItemsBuffer.append("xtype: 'textfield',");
					}
					editFormItemsBuffer.append("name: '" + fields[i] + "', fieldLabel: '" + fields[i] + "',anchor : '95%'");
					String nullable = node.attribute("nullable").getStringValue();
					if(nullable != null && nullable.equals("false"))
						editFormItemsBuffer.append(", allowBlank : false");
					editFormItemsBuffer.append("}");
				}
				if(i != fields.length - 1)
					editFormItemsBuffer.append(",");
			}
			line = line.replace("${ModuleEditItems}", editFormItemsBuffer.toString());
			
			//${ModuleSearchItems} 查询属性列表
			String serachItems = getSearchItems(fieldsMap);
			line = line.replace("${ModuleSearchItems}", serachItems);
			
			//生成grid items
			StringBuffer gridCMItemsBuffer = new StringBuffer();
			fields = generateAssistant.gridField.getText().split(",");
			for(int i = 0; i < fields.length; i++) {
				if(fields[i].trim().equals(""))
					continue;
				if(i > 0)
					gridCMItemsBuffer.append("\t\t\t");
				gridCMItemsBuffer.append("{header:'" + fields[i] + "', dataIndex:'" + fields[i] + "', sortable:true}");
				if(i != fields.length - 1) {
					gridCMItemsBuffer.append(",");
					gridCMItemsBuffer.append("\n");
				}
			}
			line = line.replace("${ModuleGridColumnItems}", gridCMItemsBuffer.toString());
			
			//生成store fields
			StringBuffer storeFieldsBuffer = new StringBuffer();
			storeFieldsBuffer.append("\n");
			for(int j = 0; j < fieldsList.size(); j++) {
				Element node =  fieldsList.get(j);
				String fieldName = node.attribute("name").getStringValue();
				
				if(fieldName.trim().equals(""))
					continue;
				storeFieldsBuffer.append("\t\t\t\t\t{name: '" + fieldName + "'}");
				
				if(j != fieldsList.size() - 1) {
					storeFieldsBuffer.append(",");
					storeFieldsBuffer.append("\n");
				}
			}
			line = line.replace("${ModuleGridDataStoreFields}", storeFieldsBuffer.toString());
			
			/*
			 * ${ModuleGridSortInfo} 排序属性
			 * 	sortInfo: {
					field: 'menuOrder',
					direction: 'ASC'
				}
			 */
			fileBuf.append(line + "\n");
		}
		reader.close();
		
		//3. 写到文件
		String filePath = projectPath + "/WebRoot/modules/" + generateAssistant.jsFileNameField.getText();
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		TextFileOperator tw = new TextFileOperator();
		tw.writeToFile(file, fileBuf.toString());
	}
	
	private String getSearchItems(Map<String, Element> fieldsMap) {
		String[] fields = generateAssistant.searchFormField.getText().split(",");
		int r = fields.length / 3;
		int l = fields.length % 3;
		StringBuffer fileBuff = new StringBuffer();
		for(int i = 0; i < r; i++) {
			if(i > 0)
				fileBuff.append("\n\t\t\t\t");
			fileBuff.append("{");
			fileBuff.append("\n\t\t\t\t\tlayout: 'column',");
			fileBuff.append("\n\t\t\t\t\titems: [");
			
			for(int c = 0; c < 3; c++) {
				fileBuff.append("\n\t\t\t\t\t\t{");
				fileBuff.append("\n\t\t\t\t\t\t\tcolumnWidth: .33, layout: 'form',");
				fileBuff.append("\n\t\t\t\t\t\t\titems: [");
				
				Element node = fieldsMap.get(fields[i*3 + c].toLowerCase());
				if(node != null) {
					fileBuff.append("\n\t\t\t\t\t\t\t\t{");
					String type = node.attribute("type").getStringValue();
					if(type.toLowerCase().endsWith("date")) {
						fileBuff.append("xtype: 'datefield',format:'Y-m-d',");
					}
					else {
						fileBuff.append("xtype: 'textfield',");
					}
					
					fileBuff.append("name: '" + fields[i*3 + c] + "', fieldLabel: '" + fields[i*3 + c] + "',anchor : '95%'");
					fileBuff.append("}");
				}
				
				fileBuff.append("\n\t\t\t\t\t\t\t]");
				fileBuff.append("\n\t\t\t\t\t\t}");
				if(c != 2) {
					fileBuff.append(",");
				}
			}
			
			fileBuff.append("\n\t\t\t\t\t]");
			fileBuff.append("\n\t\t\t\t}");
			
			if(i != r-1) {
				fileBuff.append(",");
			}
		}
		
		if(l > 0) {
			fileBuff.append("\n\t\t\t,{");
			fileBuff.append("\n\t\t\t\tlayout: 'column',");
			fileBuff.append("\n\t\t\t\titems: [");
			
			for(int c = 0; c < l; c++) {
				fileBuff.append("\n\t\t\t\t\t{");
				fileBuff.append("\n\t\t\t\t\t\tcolumnWidth: .33, layout: 'form',");
				fileBuff.append("\n\t\t\t\t\t\titems: [");
				fileBuff.append("\n\t\t\t\t\t\t\t{ xtype: 'textfield', name: '" + fields[r*3 + c] + "', fieldLabel: '" + fields[r*3 + c] + "',anchor : '95%'}");
				fileBuff.append("\n\t\t\t\t\t\t]");
				fileBuff.append("\n\t\t\t\t\t}");
				if(c != l-1) {
					fileBuff.append(",");
				}
			}
			fileBuff.append("\n\t\t\t\t]");
			fileBuff.append("\n\t\t\t}");
		}
		
		return fileBuff.toString();
	}
}
