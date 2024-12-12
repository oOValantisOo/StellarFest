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
import models.User;
import models.Vendor;

public class EventOrganizerController {

    public EventOrganizerController() {
    	
    }
    
    
    public void createEvent(String eventName, String date, String location, String description, String organizerId) {
    	 Event.createEvent(eventName, date, location, description, organizerId);
    }
    
    public Event viewOrganizedEventDetails(String eventID) {
    	return Event.viewOrganizedEventDetails(eventID);
    }
    
    public List<Guest> getGuests() throws SQLException {
        return Guest.getGuests();
    }
    
    public String addGuest(String event_id, String user_id) {
    	return Guest.addGuest(event_id, user_id);
	}
    
    public List<Vendor> getVendors() throws SQLException{
    	return Vendor.getVendors();
    }
    
    public String addVendor(String eventId, String userId) {
        return Vendor.addVendor(eventId, userId);
    }

    
    public List<Event> viewOrganizedEvents(String organizer_id){
    	return Event.viewOrganizedEvents(organizer_id);
    }
    
    public String editEventName(String eventId, String event_name) {
    	return Event.editEventName(eventId, event_name);
    }
    
	public User getUserById(String user_id) {
		return User.getUserById(user_id);
	}
}


