package com.querytool.sparksqltool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querytool.sparksqltool.service.LoadSqlService;
import com.querytool.sparksqltool.service.QueryService;
import com.querytool.sparksqltool.utils.JdbcUtil;

public class AppContext {

	private static AppContext instance = new AppContext();
	
	private App app;
	
	private Map<String,Connection> conns;
	
	private AppContext() {conns = new HashMap<>();} 
	
	public static AppContext instance() {
		return instance;
	}
	
	public void setApp(App app) {
		this.app = app;
	}
	
	public App getApp() {
		return this.app;
	}
	
	public String connect(String url,String username,String password) throws SQLException {
		Connection conn = JdbcUtil.connect(url, username, password);
		int index_pre = url.indexOf("//");
		int index_post = url.lastIndexOf(":");
		String name = url.substring(index_pre+1,index_post);
		conns.put(name, conn);
		return name;
	}
	
	public List<String> getDatabases(String connName) throws SQLException{
		List<String> databases = LoadSqlService.getDatabasesOrTables(
				QueryService.executeQuery("show databases", conns.get(connName))
		);
		return databases;
	}
	
	public List<String> getTables(String connName,String database) throws SQLException{
		Connection con = conns.get(connName);
		QueryService.selectDatabase(database, con);
		List<String> tables = LoadSqlService.getDatabasesOrTables(
				QueryService.executeQuery("show tables", con)
		);
		return tables;
	}
}
