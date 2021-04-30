package com.qkl.ztysl.provider.dbhelper;

public class DbContextHolder {
	
	public final static String ORIDB = "sqlSessionFactory";
    public final static String ODSDB = "sqlSessionFactory2";
//    public final static String ENTERDB = "sqlSessionFactory3";
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDbType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDbType() {
		return (String) contextHolder.get();
	}

	public static void clearDbType() {
		contextHolder.remove();
	}
}