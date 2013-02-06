package eddy.generate.util;

public class JavaClassUtil {
	
	/**
	 * �õ�������
	 * @param classFullName ���ȫ�޶�����
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
	 * �õ����ʵ������(Ĭ��Ϊ�����ƿ�ͷСд)
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
