package com.querytool.sparksqltool.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JdbcUtil {

	private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	
	private static Map<String,Connection> map = new HashMap<>();
	
	public static Connection connect(String url,String username,String password) throws SQLException {
		try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		Connection connection = DriverManager.getConnection("jdbc:hive2://"+url,username,password);
		return connection;
	}
	
}
