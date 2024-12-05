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
import models.Vendor;

public class AdminController {
	private Connection connection;

    public AdminController() {
    	this.connection = DatabaseConnection.getInstance().getConnection(); 
    }
    
    public List<Event> viewAllEvents() {
    	String query = "SELECT * FROM events";
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
    
    public Event viewEventDetails(String eventID) {
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
    
    public String deleteEvent(String eventId) {
    	String query = "DELETE FROM events WHERE event_id = ?";
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, eventId);         
	        
	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "Event successfully deleted";
	        } else {
	            return "Failed to delete event";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error occurred";
	    }
    }
    
    public List<User> viewAllUsers(){
    	String query = "SELECT * FROM users";
    	List<User> users = new ArrayList<>();
    	User user = null;
    	
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	            	user = new User();
	                user.setUser_id(rs.getString("user_id"));
	                user.setUser_name(rs.getString("user_name"));
	                user.setUser_email(rs.getString("user_email"));
	                user.setUser_role(rs.getString("user_role"));
	                user.setUser_password(rs.getString("user_password"));
 	                users.add(user);

 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
    	return users;
    }
    
    public String deleteUser(String user_id) {
    	String query = "DELETE FROM events WHERE user_id = ?";
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, user_id);         
	        
	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "User successfully deleted";
	        } else {
	            return "Failed to delete user";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error occurred";
	    }
    }
}
