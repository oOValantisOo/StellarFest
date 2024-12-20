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
	private Connection connection;
	
    public InvitationController(String userId) {
    	this.userId = userId;
    	this.connection = DatabaseConnection.getInstance().getConnection(); 
    }
	
	public String sendInvitation(String email, String eventID) {
		
		return Invitation.sendInvitation(email, eventID);
		
	}
	
	public String acceptInvitation(String eventId) {
		return Invitation.acceptInvitation(eventId, userId);
	}
	
	public List<Invitation> getInvitations(String email){
		List<Invitation> lists = Invitation.getInvitations(userId);
		lists.removeIf(inv -> "Accepted".equals(inv.getInvitation_status()));
		return lists;
   }
	
	public List<Invitation> viewAcceptedEvents(String email){
		return Invitation.viewAcceptedEvents(userId);
	}
}

