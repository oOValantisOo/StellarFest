package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

	private boolean checkCreateEventInput(String eventName, String eventDate, String eventLocation,
			String eventDescription) {
		if (eventName == null || eventName.trim().isEmpty()) {
			return false;
		}

		if (eventDate == null || eventDate.trim().isEmpty()) {
			return false;
		}

		try {
			LocalDate date = LocalDate.parse(eventDate);
			if (!date.isAfter(LocalDate.now())) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		if (eventLocation == null || eventLocation.trim().isEmpty() || eventLocation.length() < 5) {
			return false;
		}

		if (eventDescription == null || eventDescription.trim().isEmpty() || eventDescription.length() > 200) {
			return false;
		}

		return true;
	}

	public String createEvent(String eventName, String date, String location, String description, String organizerId) {
		if (!checkCreateEventInput(eventName, date, location, description)) {
			return "Invalid input: Ensure all fields are properly filled, the date is in the future, "
					+ "the location is at least 5 characters, and the description is under 200 characters.";
		}

		return Event.createEvent(eventName, date, location, description, organizerId);
	}

	public Event viewOrganizedEventDetails(String eventID) {
		return Event.viewOrganizedEventDetails(eventID);
	}

	public List<User> getGuests() throws SQLException {
		return Guest.getGuests();
	}

	public String addGuest(String event_id, String user_id) {
		return Guest.addGuest(event_id, user_id);
	}

	public List<User> getVendors() throws SQLException {
		return Vendor.getVendors();
	}

	public String sendInvitation(String eventId, String email) {
		User u = checkAddVendorInput(email);

		if (u == null)
			return "Selected guest/vendor doesn't exists";

		if (u.getUser_role().equals("Vendor"))
			return Vendor.addVendor(eventId, u.getUser_id());

		return Guest.addGuest(eventId, u.getUser_id());

	}

	private User checkAddVendorInput(String email) {
		return User.getUserByEmail(email);
	}

	public List<Event> viewOrganizedEvents(String organizer_id) {
		return Event.viewOrganizedEvents(organizer_id);
	}

	public String editEventName(String eventId, String event_name) {
		if (!checkEditEventNameInput(event_name))
			return "Name must be filled!";
		return Event.editEventName(eventId, event_name);
	}

	public User getUserById(String user_id) {
		return User.getUserById(user_id);
	}

	public List<User> getUninvitedVendor(String eventId) throws SQLException {
		return Vendor.getUninvitedVendors(eventId);
	}

	public List<User> getUninvitedGuests(String eventId) throws SQLException {
		return Guest.getUninvitedGuests(eventId);
	}

	private boolean checkEditEventNameInput(String eventName) {
		if (eventName.isEmpty())
			return false;
		return true;
	}
	
	public List<User> getVendorFromEvent(String eventId) throws SQLException {
		return Vendor.getVendorFromEvent(eventId);
	}
	
	public List<User> getGuestFromEvent(String eventId) throws SQLException {
    	return Guest.getGuestFromEvent(eventId);
    }

}
