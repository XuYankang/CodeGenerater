package eddy.generate.javaclass.ibatis;


public class IBatisGeneratorFactory {
	public static IBatisGenerator createIBatisGenerator() {
		return new IBatisGenerator_MyBatis();
	}
}
