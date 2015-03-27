package org.yuner.www.database;

import java.sql.*;

public class DBCon {

	private static final String url = "jdbc:mysql://192.168.52.142";
    private static final String user = "francis";
    private static final String password = "zgfailmr";

	public static Connection getConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}
	
		Connection con = null;
		try {
		    con = DriverManager.getConnection(url, user, password);
		    System.out.println("Success");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		if(con != null) {
			System.out.println("you have successfully taken control of the database, enjoy!");
		} else {
			System.out.println("failed to make connection!!!");
		}

		return con;
	}

}
