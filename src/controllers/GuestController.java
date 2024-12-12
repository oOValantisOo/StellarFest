package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import models.Event;
import models.Guest;
import models.Invitation;

public class GuestController {
	private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GuestController() {
    
    }
    
    public List<Invitation> viewInvitations(){
    	return Guest.viewInvitations(userId);
    }
    
    public String acceptInvitation(String eventId) {
		return Guest.acceptInvitation(eventId, eventId);
	}
    
    public List<Event> viewAcceptedEvents(String email) {
        return Event.viewAcceptedEvents(email);
    }
    
}
