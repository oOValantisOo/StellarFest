package controllers;

import java.sql.Connection;

import database.DatabaseConnection;

public class AdminController {
	private Connection connection;

    public AdminController() {
        this.connection = (Connection) DatabaseConnection.getInstance(); 
    }
    
    public ResultSet viewAllEvents() {
    	String query = 
    }
}
