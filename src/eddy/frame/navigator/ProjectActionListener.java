package eddy.frame.navigator;


public interface ProjectActionListener {
	
	/**
	 * 新建项目
	 */
	public void newProject();
	
	/**
	 * 打开项目
	 */
	public void openProject();
	
	/**
	 * 添加目录
	 */
	public void addDir();

	/**
	 * 添加文件
	 */
	public void addFile();
	
	/**
	 * 新建JAVA类
	 */
	public void newClass();
	
	/**
	 * 新建Web应用模块
	 */
	public void newWebModel();
	
	/**
	 * 删除文件
	 */
	public void deleteFile();
	
	/**
	 * 新建文件
	 */
	public void newFile();

	/**
	 * 测试
	 */
	public void doTest();
}
