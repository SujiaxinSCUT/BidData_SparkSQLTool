package com.querytool.sparksqltool.service;

import java.sql.ResultSet;
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
	
}
