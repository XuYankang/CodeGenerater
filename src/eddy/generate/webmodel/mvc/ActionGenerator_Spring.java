package eddy.generate.webmodel.mvc;

import eddy.generate.util.FileCreator;
import eddy.generate.util.JavaClassUtil;
import eddy.generate.webmodel.WebModelGenerateAssistant;
import eddy.log.DebugLogger;

public class ActionGenerator_Spring implements ActionGenerator {
	private WebModelGenerateAssistant generateAssistant;
	private String projectPath;
	
	private String actionClassName;//Action类名称
	private String actionClassFullName;
	private String requestMapping;
	
	private String entityClassFullName;
	private String entityClassName;
	private String entityInstanceName;
	
	private String daoName;
	private String daoInstanceName;
	
	public ActionGenerator_Spring(WebModelGenerateAssistant generateAssistant, String projectPath) {
		this.generateAssistant = generateAssistant;
		this.projectPath = projectPath;
	}

	/**
	 * 生成Action
	 * @param generateAssistant
	 */
	public void generateAction() {
		try {
			StringBuffer fileBuf = new StringBuffer();
			
			actionClassFullName = generateAssistant.controllerNameField.getText();
			actionClassName = JavaClassUtil.getClassName(actionClassFullName);
			requestMapping = generateAssistant.requestMappingField.getText();
//			classInstanceName = JavaClassUtil.getClassInstanceName(classFullName);
			
			entityClassFullName = (String) generateAssistant.entityCombo.getSelectedItem();
			entityClassName = JavaClassUtil.getClassName(entityClassFullName);
			entityInstanceName = JavaClassUtil.getClassInstanceName(entityClassFullName);
			
			
			String daoFullName = generateAssistant.daoTextField.getText();
			daoName = JavaClassUtil.getClassName(daoFullName);
			daoInstanceName = JavaClassUtil.getClassInstanceName(daoFullName);
			
			String packageName = JavaClassUtil.getClassPackageName(actionClassFullName);
			if(!packageName.equals(""))
				fileBuf.append("package " + packageName + ";\n");
			
			fileBuf.append("import java.text.SimpleDateFormat;\n");//
			fileBuf.append("import com.eddy.util.EntityReflect;\n");
			fileBuf.append("import com.eddy.util.ExtLimit;\n");
			
			fileBuf.append("import org.apache.log4j.Logger;\n");
			fileBuf.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			fileBuf.append("import org.springframework.stereotype.Controller;\n");
			fileBuf.append("import org.springframework.web.bind.annotation.ExceptionHandler;\n");
			fileBuf.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
			fileBuf.append("import org.springframework.web.bind.annotation.RequestParam;\n");
			fileBuf.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
			
			fileBuf.append("import java.util.Date;\n");
			fileBuf.append("import java.util.List;\n");
			fileBuf.append("import net.sf.json.JSONObject;\n");
			
			fileBuf.append("import " + entityClassFullName + ";\n");
			fileBuf.append("import " + daoFullName + ";\n");
			fileBuf.append("\n");
			
			fileBuf.append("@Controller\n");//
			fileBuf.append("@RequestMapping(\"" + requestMapping + "\")\n");//
			fileBuf.append("public class " + actionClassName +" {\n");
			
			fileBuf.append("\nprivate Logger logger = Logger.getLogger(" + actionClassName + ".class);\n");
			
			fileBuf.append("\t@Autowired\n");
			fileBuf.append("\tprivate " + daoName + " " + daoInstanceName + ";\n");
			
			generateSearchMethod(fileBuf);
			generateSaveMethod(fileBuf);
			generateDeleteMethod(fileBuf);
			generateGetDetailInfoMethod(fileBuf);
			generateException(fileBuf);
			fileBuf.append("}");
			
			FileCreator.createJavaFile(actionClassFullName, projectPath + "/src", fileBuf.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			DebugLogger.getLogger().err(e);
		}
	}
	
	/**
	 * 生成查询方法
	 * @param fileStrBuf
	 */
	private void generateSearchMethod(StringBuffer fileStrBuf) {
		fileStrBuf.append("\n");
		fileStrBuf.append("\n\t@RequestMapping(value=\"search.action\")");
		fileStrBuf.append("\n\tpublic @ResponseBody String search(HttpServletRequest request, HttpServletResponse response, " + entityClassName + " " + entityInstanceName  + ") throws Exception {\n");
		
		fileStrBuf.append("\t\t");
		fileStrBuf.append("int count = " + daoInstanceName + ".selectLimitCount(" + entityInstanceName + ");");
		fileStrBuf.append("\n");
		
		fileStrBuf.append("\t\t");
		fileStrBuf.append("List list = " + daoInstanceName + ".selectByLimit(" + entityInstanceName + ");");
		fileStrBuf.append("\n");
		
		fileStrBuf.append("\t\t");
		fileStrBuf.append("JSONObject retObj = JSONGrid.toJSon(list, count);");
		fileStrBuf.append("\n");
		
		fileStrBuf.append("\t\t");
		fileStrBuf.append("return retObj.toString();");
		fileStrBuf.append("\n");
		
		fileStrBuf.append("\t}\n");
	}
	
	private void generateSaveMethod(StringBuffer fileStrBuf) {
		fileStrBuf.append("\n");
		fileStrBuf.append("\n\t@RequestMapping(value=\"save.action\")");
		fileStrBuf.append("\n\tpublic @ResponseBody String save(" + entityClassName + " " + entityInstanceName  + ") {\n");
		fileStrBuf.append("\t\tJSONObject obj = new JSONObject();\n");
		fileStrBuf.append("\t\tobj.put(\"success\",true);\n");
		
		fileStrBuf.append("\t\t");
		fileStrBuf.append("if(" + entityInstanceName + ".getId() == null) {\n");//FIXME 需要取隐藏属性，而不是指定的id
		fileStrBuf.append("\t\t\t" + daoInstanceName + ".insert(" + entityInstanceName + ");\n");
		fileStrBuf.append("\t\t} else { \n");
		fileStrBuf.append("\t\t\t" + daoInstanceName + ".updateById(" + entityInstanceName + ");\n");
		fileStrBuf.append("\t\t}\n");
		fileStrBuf.append("\t\tobj.put(\"result\",\"success\");\n");
		
		fileStrBuf.append("\t\treturn obj.toString();\n");
		fileStrBuf.append("\t}\n");
	}

	private void generateDeleteMethod(StringBuffer fileStrBuf) {
		fileStrBuf.append("\n");
		fileStrBuf.append("\n\t@RequestMapping(value=\"delete.action\")");
		fileStrBuf.append("\n\tpublic @ResponseBody String delete(@RequestParam(\"id\") int id) {\n");
		fileStrBuf.append("\t\tJSONObject obj = new JSONObject();\n");
		fileStrBuf.append("\t\tobj.put(\"success\",true);\n");
		fileStrBuf.append("\t\t" + daoInstanceName + ".deleteById(id);\n");
		fileStrBuf.append("\t\tobj.put(\"result\",\"success\");\n");
		fileStrBuf.append("\t\treturn obj.toString();\n");
		fileStrBuf.append("\t}\n");
	}
	
	private void generateGetDetailInfoMethod(StringBuffer fileStrBuf) {
		fileStrBuf.append("\n");
		fileStrBuf.append("\n\t@RequestMapping(value=\"getDetailInfo.action\")");
		fileStrBuf.append("\n\tpublic @ResponseBody String getDetailInfo(@RequestParam(\"id\") int id) {\n");
		fileStrBuf.append("\t\tJSONObject obj = new JSONObject();\n");
		fileStrBuf.append("\t\tobj.put(\"success\",true);\n");
		fileStrBuf.append("\t\t" + entityClassName + " " + entityInstanceName + " = (" + entityClassName + ") " + daoInstanceName + ".selectById(id);\n");
		fileStrBuf.append("\t\tJsonConfig config = new JsonConfig();\n");
		fileStrBuf.append("\t\tconfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor(\"yyyy-MM-dd\"));\n");
		fileStrBuf.append("\t\tJSONObject dataObj = JSONObject.fromObject(" + entityInstanceName + ", config);");
		fileStrBuf.append("\t\tJSONObject dataObj = JSONObject.fromObject(" + entityInstanceName + ", config);\n");
		fileStrBuf.append("\t\tobj.put(\"data\", dataObj);\n");
		fileStrBuf.append("\t\treturn obj.toString();\n");
		fileStrBuf.append("\t}\n");
	}
	
	private void generateException(StringBuffer fileStrBuf) {
		fileStrBuf.append("\n");
		fileStrBuf.append("\n\t@ExceptionHandler");
		fileStrBuf.append("\n\tpublic @ResponseBody String handle(Exception e) {");
		fileStrBuf.append("\n\t\tlogger.error(\"\", e);");
		fileStrBuf.append("\n\t\tJSONObject obj = new JSONObject();");
		fileStrBuf.append("\n\t\tobj.put(\"success\",true);");
		fileStrBuf.append("\n\t\tobj.put(\"result\",\"error\");");
		fileStrBuf.append("\n\t\tobj.put(\"info\",e.getMessage());");
		fileStrBuf.append("\n\t\treturn obj.toString();");
		fileStrBuf.append("\n\t}\n");
	}
	//FIXME 生成导出方法
	
}
