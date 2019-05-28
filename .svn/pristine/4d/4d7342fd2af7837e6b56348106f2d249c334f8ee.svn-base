package com.cbt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTaskHelper {

	private static String className = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://112.64.174.34:3309/purchase?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&autoReconnect=true";
	private static String user = "root";
	private static String password = "csgkr@123com";
	
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(className);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
