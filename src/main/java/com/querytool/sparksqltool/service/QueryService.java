package com.querytool.sparksqltool.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryService {
	
	static PreparedStatement ps = null;
	
	public static ResultSet executeQuery(String sql,Connection connection) throws SQLException {
		ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return rs;
	}
	
	public static void selectDatabase(String database,Connection connection) throws SQLException {
		ps = connection.prepareStatement("use "+database);
		ps.execute();
	}
	
}
