package com.querytool.sparksqltool.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JdbcUtil {

	private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	
	private static Map<String,Connection> map = new HashMap<>();
	
	public static String connect(String url,String username,String password) throws SQLException {
		try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
		Connection connection = DriverManager.getConnection(url,username,password);
		int index_pre = url.indexOf("//");
		int index_post = url.lastIndexOf(":");
		String name = url.substring(index_pre+2,index_post);
		map.put(name, connection);
		return name;
	}
	
}
