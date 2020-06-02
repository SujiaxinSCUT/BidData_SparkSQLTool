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

import com.querytool.sparksqltool.AppContext;
import com.querytool.sparksqltool.service.LoadSqlService;
import com.querytool.sparksqltool.service.QueryService;
import com.querytool.sparksqltool.utils.JdbcUtil;

public class Test {

	private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	
	public static void main(String[] args) throws SQLException {
		try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		Connection connection = DriverManager.getConnection("jdbc:hive2://bigdata31.depts.bingosoft.net:22231"
				, "user10", "pass@bingo10");
		String sql = "show tables";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rMetaData = rs.getMetaData();
//		List<String> list = new ArrayList<String>();
		int count = rMetaData.getColumnCount();
		while(rs.next()){
			for(int i=1;i<=count;i++) {
				System.out.print(rs.getString(i)+" ");
			}
			System.out.println();
		}
//		System.out.println(list);
//		DatabaseMetaData metaData = connection.getMetaData();
		
	}
}
