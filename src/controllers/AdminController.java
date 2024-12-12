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

	public AdminController() {
    	
    }
    
    public List<Event> viewAllEvents() {
    	return Event.getAllEvent();
    }
    
    public Event viewEventDetails(String eventID) {
       	return Event.viewOrganizedEventDetails(eventID);
    }
    
    public String deleteEvent(String eventId) {
    	return User.deleteEvent(eventId);
    }
    
    public List<User> viewAllUsers(){
    	return User.viewAllUsers();
    }
    
    public String deleteUser(String user_id) {
    	return User.deleteUser(user_id);
    }
}
