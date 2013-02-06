package eddy.generate.webmodel.mvc;

import eddy.generate.webmodel.WebModelGenerateAssistant;

public class ActionGeneratorFactory {
	public static ActionGenerator createActionGenerator(WebModelGenerateAssistant generateAssistant, String projectPath) {
		ActionGenerator actionGenerator = new ActionGenerator_Spring(generateAssistant, projectPath);
		return actionGenerator;
	}
}