package com.querytool.sparksqltool.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadSqlService {

	public static List<String> getDatabasesOrTables(ResultSet rs) throws SQLException{
		List<String> list = new ArrayList<>();
		while(rs.next()) {
			list.add(rs.getString(1));
		}
		return list;
	}
	
	public static List<List<String>> loadTables(ResultSet rs) throws SQLException{
		List<List<String>> data = new ArrayList<>();
		List<String> colNames = new ArrayList<>();
		ResultSetMetaData metaData = rs.getMetaData();
		int colCount = metaData.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			String columnName = metaData.getColumnName(i + 1);
			colNames.add(columnName);
		}
		data.add(colNames);
		while(rs.next()) {
			List<String> row = new ArrayList<>();
			for (int i = 0; i < colCount; i++) {
				row.add(rs.getString(i+1));
			}
			data.add(row);
		}
		return data;
	}
	
}
