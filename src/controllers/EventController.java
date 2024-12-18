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

public class EventController {


    public EventController() {
    	
    }
	
	public void createEvent(String eventName, String date, String location, String description, String organizerId) {
   	 	Event.createEvent(eventName, date, location, description, organizerId);
	}
	
	public List<Event> getAllEvent(){
		return Event.getAllEvent();
	}
	
	public Event viewOrganizedEventDetails(String eventID) {
   		return Event.viewOrganizedEventDetails(eventID);
	}
   
	public void deleteEvent(String id) {
       Event.deleteEvent(id);
	}
	
	public List<Event> viewAcceptedEvents(String email){
		return Event.viewAcceptedEvents(email);
	}
}
