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
import models.User;
import models.Vendor;

public class InvitationController {
	private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }
	
	private Connection connection;

    public InvitationController() {
    	this.connection = DatabaseConnection.getInstance().getConnection(); 
    }
	
	public String sendInvitation(String email) {
		
		return "Success";
	}
	
	public String acceptInvitation(String eventId) {
		String user_id = this.userId;
		
		String query = "UPDATE invitatons SET invitation_status = ? WHERE user_id = ? AND event_id = ?";
		
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
	
	public List<Invitation> getInvitations(String email) throws SQLException{
//		String query = "SELECT * FROM user WHERE user_email = ?";
//		User user = null;
//
//	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
//	        stmt.setString(1, email);
//
//	        try (ResultSet rs = stmt.executeQuery()) {
//	            if (rs.next()) {
//	            	user = new User();
//	                user.setUser_id(rs.getString("user_id"));
//	                user.setUser_name(rs.getString("user_name"));
//	                user.setUser_email(rs.getString("user_email"));
//	                user.setUser_role(rs.getString("user_role"));
//	                user.setUser_password(rs.getString("user_password"));
//	            }
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    String user_id = user.getUser_id();
//		Keknya ini bisa pake params
		
		String user_id = this.userId;
	    
	    String query = "SELECT * FROM invitations WHERE user_id = ?";
	    List<Invitation> invitations = new ArrayList<>();
	    
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	    	stmt.setString(1, user_id);
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
}

