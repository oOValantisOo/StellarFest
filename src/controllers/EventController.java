package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import models.Event;
import models.User;

public class EventController {
	private Connection connection;

    public EventController() {
    	this.connection = DatabaseConnection.getInstance().getConnection(); 
    }
	
	public void createEvent(String eventName, String date, String location, String description, String organizerId) {
   	 String query = "INSERT INTO events (event_name, event_date, event_location, event_description, organizer_id) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, eventName);     
            stmt.setString(2, date);   
            stmt.setString(3, location);      
            stmt.setString(4, description);    
            stmt.setString(5, organizerId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Event created successfully!");
            } else {
                System.out.println("Failed to create event.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
   }
	
	public List<Event> getAllEvent(){
		String query = "SELECT * FROM events";
        List<Event> events = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	            	Event event = new Event();
	            	event.setEvent_id(rs.getString("event_id"));
	            	event.setEvent_name(rs.getString("event_name"));
	            	event.setEvent_date(rs.getString("event_date"));
	            	event.setEvent_location(rs.getString("event_location"));
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

   
   public Event getEventDetails(String eventID) {
   	String query = "SELECT * FROM events WHERE event_id = ?";
	    Event event = null;

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, eventID);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                event = new Event();
	                event.setEvent_name(rs.getString("event_name"));
	                event.setEvent_date(rs.getString("event_date"));
	                event.setEvent_description(rs.getString("event_description"));
	                event.setEvent_id(rs.getString("event_id"));
	                event.setOrganizer_id(rs.getString("organizer_id"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return event;
   }
   
   public void deleteEvent(String id) {
       String query = "DELETE FROM events WHERE event_id = ?";
       try (PreparedStatement stmt = connection.prepareStatement(query)) {
       	stmt.setString(1, id);   

           int rowsAffected = stmt.executeUpdate();
           if (rowsAffected > 0) {
               System.out.println("Event deleted successfully!");
           } else {
               System.out.println("Failed to delete event.");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
}
