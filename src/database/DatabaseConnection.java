package database;

import java.sql.*;

public class DatabaseConnection {
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "stellarfest";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	public ResultSet rs;
	public ResultSetMetaData rsm;
	
	private Connection con;
	private Statement st;
	private static DatabaseConnection connect;
	
	public static DatabaseConnection getInstance() {
		if(connect == null) {
			return new DatabaseConnection();
		}
		return connect;
	}
	
	private DatabaseConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
        return con;
    }
	
}
