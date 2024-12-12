package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class Event {
	private String event_id;
	private String event_name;
	private String event_date;
	private String event_location;
	private String event_description;
	private String organizer_id;
	private List<Vendor> vendors;
	private List<Guest> guests;
	private static Connection connection;

	public Event() {
		connection = DatabaseConnection.getInstance().getConnection();
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getEvent_date() {
		return event_date;
	}

	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}

	public String getEvent_location() {
		return event_location;
	}

	public void setEvent_location(String event_location) {
		this.event_location = event_location;
	}

	public String getEvent_description() {
		return event_description;
	}

	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}

	public String getOrganizer_id() {
		return organizer_id;
	}

	public void setOrganizer_id(String organizer_id) {
		this.organizer_id = organizer_id;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}

	public List<Vendor> getVendors() {
		return vendors;
	}

	public void setVendors(List<Vendor> vendors) {
		this.vendors = vendors;
	}

	public static void createEvent(String eventName, String date, String location, String description,
			String organizerId) {
		String query = "INSERT INTO events (event_name, event_date, event_location, event_description, organizer_id) VALUES(?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, eventName);
			stmt.setString(2, date);
			stmt.setString(3, location);
			stmt.setString(4, description);
			stmt.setString(5, organizerId);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Event created successfully!");
			} else {
				System.out.println("Failed to create event.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Event> getAllEvent() {
		String query = "SELECT * FROM events";
		List<Event> events = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(query)) {

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Event event = new Event();
					event.setEvent_id(rs.getString("event_id"));
					event.setEvent_name(rs.getString("event_name"));
					event.setEvent_date(rs.getString("event_date"));
					event.setEvent_location(rs.getString("event_location"));
					event.setEvent_description(rs.getString("event_description"));
					event.setOrganizer_id(rs.getString("organizer_id"));

					events.add(event);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return events;
	}
	
	public static List<Event> viewOrganizedEvents(String organizer_id){
    	String query = "SELECT * FROM events WHERE organizer_id = ?";
    	List<Event> organizedEvents = new ArrayList<>();
    	
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
    		stmt.setString(1, organizer_id);
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	                Event event = new Event();
 	                event.setEvent_date(rs.getString("event_date"));
 	                event.setEvent_description(rs.getString("event_description"));
 	                event.setEvent_id(rs.getString("event_id"));
 	                event.setEvent_location(rs.getString("event_location"));
 	                event.setEvent_name(rs.getString("event_name"));
 	                event.setOrganizer_id(organizer_id);
 	                organizedEvents.add(event);
 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
        return organizedEvents;
    }

	public static Event viewOrganizedEventDetails(String eventID) {
		String query = "SELECT * FROM events WHERE event_id = ?";
		Event event = null;

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, eventID);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					event = new Event();
					event.setEvent_name(rs.getString("event_name"));
					event.setEvent_date(rs.getString("event_date"));
					event.setEvent_description(rs.getString("event_description"));
					event.setEvent_id(rs.getString("event_id"));
					event.setOrganizer_id(rs.getString("organizer_id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return event;
	}

	public static void deleteEvent(String id) {
		String query = "DELETE FROM events WHERE event_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, id);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Event deleted successfully!");
			} else {
				System.out.println("Failed to delete event.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	

	public static String editEventName(String eventId, String event_name) {
		String query = "UPDATE events SET event_name = ? WHERE event_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, event_name);
			stmt.setString(2, eventId);
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return "Event successfully updated";
			} else {
				return "Failed to update event";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "An error occurred while updating the event";
		}
	}
	
	public static List<Event> viewAcceptedEvents(String email) {
	    String query = "SELECT e.event_id, e.event_date, e.event_name, e.event_location, "
	                 + "e.event_description, e.organizer_id "
	                 + "FROM invitations i "
	                 + "JOIN users u ON i.user_id = u.user_id "
	                 + "JOIN events e ON i.event_id = e.event_id "
	                 + "WHERE u.email = ? AND i.invitation_status = ?";

	    List<Event> events = new ArrayList<>();

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, email); 
	        stmt.setString(2, "accepted"); 
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Event event = new Event();
	                event.setEvent_id(rs.getString("event_id"));
	                event.setEvent_date(rs.getString("event_date"));
	                event.setEvent_name(rs.getString("event_name"));
	                event.setEvent_location(rs.getString("event_location"));
	                event.setEvent_description(rs.getString("event_description"));
	                event.setOrganizer_id(rs.getString("organizer_id"));
	                events.add(event);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return events;
	}

}
