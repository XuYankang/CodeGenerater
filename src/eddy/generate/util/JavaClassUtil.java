package eddy.generate.util;

public class JavaClassUtil {
	
	/**
	 * 得到类名称
	 * @param classFullName 类的全限定名称
	 * @return
	 */
	public static String getClassName(String classFullName) {
		String className = classFullName;
		int n = classFullName.lastIndexOf(".");
		if(n > 0) {
			className = className.substring(n + 1);
		}
		return className;
	}
	
	public static String getClassPackageName(String classFullName) {
		int n = classFullName.lastIndexOf(".");
		if(n > 0) 
			return classFullName.substring(0, n);
		return "";
	}
	
	/**
	 * 得到类的实例名称(默认为类名称开头小写)
	 * @param classFullName
	 * @return
	 */
	public static String getClassInstanceName(String classFullName) {
		String className = getClassName(classFullName);
		String a = className.charAt(0) + "";
		String b = a.toLowerCase();
		className = className.replaceFirst(a, b);
		return className;
	}
}
