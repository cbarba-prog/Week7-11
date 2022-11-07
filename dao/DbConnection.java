package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

public class DbConnection {
	private static String HOST = "localhost";
	private static String PASSWORD = "projects";
	private static int PORT = 3306;
	private static String SCHEMA = "projects";
	private static String USER = "projects";
	
	public static Connection getConnection() {
		String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA, USER, PASSWORD);
		System.out.println("Connecting with url=" + uri);
		
		try {
			Connection conn = DriverManager.getConnection(uri);
			System.out.println("Schema " + SCHEMA + " is connected.");
			return conn;
		} catch (SQLException e) {
			System.out.println("Connection not found at " + uri);
			throw new DbException(e + "Connection not found at uri.");
		}
		
		
	}
}
