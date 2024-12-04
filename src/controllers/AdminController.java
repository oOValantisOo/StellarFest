package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import models.Event;
import models.Vendor;

public class AdminController {
	private Connection connection;

    public AdminController() {
        this.connection = (Connection) DatabaseConnection.getInstance(); 
    }
    
    public List<Event> viewAllEvents() {
    	String query = "SELECT * FROM event";
    	List<Event> events = new ArrayList<>();
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
    		
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	            	Event event = new Event();
 	            	event.setEvent_id(rs.getString("event_id"));
 	            	event.setEvent_name(rs.getString("event_name"));
 	            	event.setEvent_location(rs.getString("event_location"));
 	            	event.setEvent_date(rs.getString("event_date"));
 	            	event.setEvent_description(rs.getString("event_description"));
 	            	event.setOrganizer_id(rs.getString("organizer_id"));
 	                events.add(event);

 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
    	return events;
    	
    }
}
