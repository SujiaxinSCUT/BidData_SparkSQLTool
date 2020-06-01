package com.querytool.sparksqltool.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.querytool.sparksqltool.utils.JdbcUtil;

public class Test {

	private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	
	public static void main(String[] args) throws SQLException {
//		try {
//            Class.forName(DRIVER_NAME);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//		Connection connection = DriverManager.getConnection("jdbc:hive2://bigdata31.depts.bingosoft.net:22231"
//				, "user10", "pass@bingo10");
//		String sql = "select table_name from user10_db";
//		Statement stmt = connection.createStatement();
//		ResultSet rs = stmt.executeQuery(sql);
//		List<String> list = new ArrayList<String>();
//		while(rs.next()){
//			System.out.println(rs.getString("TABLE_NAME"));
//			String tblName = rs.getString("TABLE_NAME");
//			list.add(tblName);
//		}
//		System.out.println(list);
		System.out.println(JdbcUtil.connect("jdbc:hive2://bigdata31.depts.bingosoft.net:22231",
				"user10", "pass@bingo10"));

	}
}
