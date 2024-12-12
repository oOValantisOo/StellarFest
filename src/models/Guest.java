package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class Guest extends User{
	
	private static Connection connection;
	
	
	static {
		connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public Guest() {
		super();

	}
	
	public static String addGuest(String event_id, String user_id) {
		User user = User.getUserById(user_id);

		if (user == null) {
			return "User does not exist";
		}
		String query = "INSERT INTO invitations (event_id, user_id, invitation_status, invitation_role) VALUES(?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, event_id);
			stmt.setString(2, user_id);
			stmt.setString(3, "pending");
			stmt.setString(4, "Guest");

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return "Vendor invitation sent successfully!";
			} else {
				return "Failed to send vendor invitation.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "An error occurred while sending the guest invitation.";
		}
	}
	
	public static List<Guest> getGuests() throws SQLException {
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
	
	public static List<Invitation> viewInvitations(String userId){
    	List<Invitation> invitations = new ArrayList<>();
    	
    	String query = "SELECT * FROM invitations WHERE user_id = ? AND invitation_status = ?";
    	
    	 try (PreparedStatement stmt = connection.prepareStatement(query)) {
 	    	stmt.setString(1, userId);
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
    
    public static String acceptInvitation(String eventId, String userId) {
		
		String query = "UPDATE invitatons SET invitation_status = ? WHERE user_id = ? AND event_id = ?";
		
		 try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, "accepted"); 
		   	stmt.setString(2, userId);
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
    
    
    
    public static Event viewEventDetails(String eventId) {
    	String query = "SELECT FROM events WHERE event_id = ?";
    	
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
