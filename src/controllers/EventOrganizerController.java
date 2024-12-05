package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import models.Event;
import models.Guest;
import models.Vendor;

public class EventOrganizerController {
	private Connection connection;

    public EventOrganizerController() {
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
    
    public Event viewOrganizedEventDetails(String eventID) {
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
    
    public List<Guest> getGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM guests";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	                Guest guest = new Guest();
 	                guest.setUser_id(rs.getString("user_id"));
 	                guest.setUser_name(rs.getString("user_name"));
 	                guest.setUser_email(rs.getString("user_email"));
 	                guest.setUser_password(rs.getString("user_password"));
 	                guest.setUser_role("user_role");
 	                guests.add(guest);
 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
        return guests;
    }
    
    public List<Vendor> getVendors() throws SQLException{
    	List<Vendor> vendors = new ArrayList<>();
    	String query = "SELECT * FROM vendors";
    	
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
    		
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	                Vendor vendor = new Vendor();
 	                vendor.setUser_id(rs.getString("user_id"));
 	                vendor.setUser_name(rs.getString("user_name"));
 	                vendor.setUser_email(rs.getString("user_email"));
 	                vendor.setUser_password(rs.getString("user_password"));
 	                vendor.setUser_role("user_role");
 	                vendors.add(vendor);
 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
        return vendors;
    }
    
    public List<Event> viewOrganizedEvents(String organizer_id){
    	String query = "SELECT * FROM events WHERE organizer_id = ?";
    	List<Event> organizedEvents = new ArrayList<>();
    	
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
    		stmt.setString(1, organizer_id);
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	                Event event = new Event();
 	                event.setEvent_date(rs.getString("event_date"));
 	                event.setEvent_description(rs.getString("event_description"));
 	                event.setEvent_id(rs.getString("event_id"));
 	                event.setEvent_location(rs.getString("event_location"));
 	                event.setEvent_name(rs.getString("event_name"));
 	                event.setOrganizer_id(organizer_id);
 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
        return organizedEvents;
    }
    
    public String editEventName(String eventId, String event_name) {
    	String query = "UPDATE events SET event_name = ? WHERE event_id = ?";
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
    		stmt.setString(1, event_name);
    		stmt.setString(2, eventId);
    		int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "Event successfully updated";
	        } else {
	            return "Failed to update event";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "An error occurred while updating the event";
	    }
    }
}


