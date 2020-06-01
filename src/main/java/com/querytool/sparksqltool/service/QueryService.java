package com.querytool.sparksqltool.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryService {

	private Connection connection;
	
	PreparedStatement ps = null;
	
	public QueryService(Connection conn) {
		this.connection = conn;
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return rs;
	}
}
