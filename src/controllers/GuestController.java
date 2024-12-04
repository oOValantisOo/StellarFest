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

public class GuestController {
	private Connection connection;
	private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GuestController() {
        this.connection = (Connection) DatabaseConnection.getInstance(); 
    }
    
    public String acceptInvitation(String eventId) {
		String user_id = this.userId;
		
		String query = "UPDATE invitaton SET invitation_status = ? WHERE user_id = ? AND event_id = ?";
		
		 try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, "accepted"); 
		   	stmt.setString(2, user_id);
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
        String user_id = this.userId; 
        
        String queryInvitation = "SELECT event_id FROM invitation WHERE user_id = ? AND invitation_status = ?";
        String queryEvent = "SELECT * FROM event WHERE event_id = ?";
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

}
