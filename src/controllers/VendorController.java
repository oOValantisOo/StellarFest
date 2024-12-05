package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import models.Event;
import models.Invitation;

public class VendorController {
	private Connection connection;

    public VendorController() {
    	this.connection = DatabaseConnection.getInstance().getConnection(); 
    }
    
    public String acceptInvitation(String email, String eventId) {
		
		String query = "UPDATE invitatons SET invitation_status = ? WHERE user_email = ? AND event_id = ?";
		
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
    
    public List<Invitation> viewInvitations(String user_id){
    	List<Invitation> invitations = new ArrayList<>();
    	
    	String query = "SELECT * FROM invitations WHERE user_id = ? AND invitation_status = ?";
    	
    	 try (PreparedStatement stmt = connection.prepareStatement(query)) {
 	    	stmt.setString(1, user_id);
 	    	stmt.setString(2, "pending");
  	        try (ResultSet rs = stmt.executeQuery()) {
  	            while(rs.next()) {
  	                Invitation invitation = new Invitation();
  	                invitation.setEvent_id(rs.getString("event_id"));
  	                invitation.setInvitation_id(rs.getString("invitation_id"));
  	                invitation.setInvitation_role(rs.getString("invitation_role"));
  	                invitation.setInvitation_status(rs.getString("invitation_status"));
  	                invitation.setUser_id(rs.getString("user_id"));
  	                invitations.add(invitation);
  	            }
  	        }
  	    } catch (SQLException e) {
  	        e.printStackTrace();
  	    }
 	    return invitations;
    }
    
    public List<Event> viewAcceptedEvents(String email, String user_id) {
        
        String queryInvitation = "SELECT event_id FROM invitations WHERE user_id = ? AND invitation_status = ?";
        String queryEvent = "SELECT * FROM events WHERE event_id = ?";
        List<Event> events = new ArrayList<>();
        
        try (PreparedStatement stmtInvitation = connection.prepareStatement(queryInvitation)) {
            stmtInvitation.setString(1, user_id);
            stmtInvitation.setString(2, "accepted");
            
            try (ResultSet rsInvitation = stmtInvitation.executeQuery()) {
                while (rsInvitation.next()) {
                    String event_id = rsInvitation.getString("event_id");
                    
                    try (PreparedStatement stmtEvent = connection.prepareStatement(queryEvent)) {
                        stmtEvent.setString(1, event_id);
                        
                        try (ResultSet rsEvent = stmtEvent.executeQuery()) {
                            if (rsEvent.next()) {
                                Event event = new Event();
                                event.setEvent_id(rsEvent.getString("event_id"));
                                event.setEvent_date(rsEvent.getString("event_date"));
                                event.setEvent_name(rsEvent.getString("event_name"));
                                event.setEvent_location(rsEvent.getString("event_location"));
                                event.setEvent_description(rsEvent.getString("event_description"));
                                event.setOrganizer_id("organizer_id");
                                events.add(event);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    
    public String manageVendor(String description, String product) {
    	String valid = checkManageVendorInput(description, product);
    	
    	if(valid == "Success") {
    		String query = "UPDATE vendors SET product_name = ?, product_description = ?";
    		 try (PreparedStatement stmt = connection.prepareStatement(query)) {
    		        stmt.setString(1, product);         
    		        stmt.setString(2, description);         

    		        int rowsAffected = stmt.executeUpdate();
    		        if (rowsAffected > 0) {
    		            return "Product successfully updated";
    		        } else {
    		            return "Failed to update account";
    		        }
    		    } catch (SQLException e) {
    		        e.printStackTrace();
    		        return "An error occurred while updating the profile";
    		    }
    	} else {
    		return valid;
    	}
    }
    
    public String checkManageVendorInput(String description, String product) {
    	if(description == "" || product == "") {
    		return "All fields must be filled!";
    	}
    	if(description.length() > 200) {
    		return "Description length at max is 200 characters long!";
    	}
    	return "Success";
    }
    
    public Event viewAcceptedEventDetails(String eventId) {
    	String query = "SELECT FROM event WHERE event_id = ?";
    	
    	Event event = null;

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, eventId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	            	event = new Event();
                    event.setEvent_id(rs.getString("event_id"));
                    event.setEvent_date(rs.getString("event_date"));
                    event.setEvent_name(rs.getString("event_name"));
                    event.setEvent_location(rs.getString("event_location"));
                    event.setEvent_description(rs.getString("event_description"));
                    event.setOrganizer_id("organizer_id");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return event;
    }
}
