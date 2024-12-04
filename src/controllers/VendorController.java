package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import database.DatabaseConnection;
import models.Event;

public class VendorController {
	private Connection connection;

    public VendorController() {
        this.connection = (Connection) DatabaseConnection.getInstance(); 
    }
    
    public String acceptInvitation(String email, String eventId) {
		
		String query = "UPDATE invitaton SET invitation_status = ? WHERE user_email = ? AND event_id = ?";
		
		 try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, "accepted"); 
		   	stmt.setString(2, email);
		   	stmt.setString(3, eventId);
		   	int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "Invitation accepted";
	        } else {
	            return "Invitation unable to accepted";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error occured";
	    }
    }
    
    public List<Event> viewAcceptedEvents(String email) {
    	
    }
    
    public void manageVendor(String description, String product) {
    	
    }
    
    public boolean checkManageVendorInput(String description, String product) {
    	
    }
}
