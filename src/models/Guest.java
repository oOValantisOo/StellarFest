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
		
		String invitationId = Invitation.generateID();

		String query = "INSERT INTO invitations (invitation_id, event_id, user_id, invitation_status, invitation_role) VALUES(?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, invitationId);
			stmt.setString(2, event_id);
			stmt.setString(3, user_id);
			stmt.setString(4, "pending");
			stmt.setString(5, "Guest");

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return "Guest invitation sent successfully!";
			} else {
				return "Failed to send guest invitation.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "An error occurred while sending the guest invitation.";
		}
	}
	
	public static List<User> getGuests() throws SQLException {
        List<User> guests = new ArrayList<>();
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
    
    public static List<User> getUninvitedGuests(String eventId) throws SQLException {
		List<User> guests = new ArrayList<>();
		String query = "SELECT * " + "FROM users u " + "WHERE u.user_role LIKE 'Guest' AND "
				+ "      u.user_id NOT IN ( " + "          SELECT u.user_id " + "          FROM users u "
				+ "          JOIN invitations i ON u.user_id = i.user_id "
				+ "          JOIN events e ON e.event_id = i.event_id " + "          WHERE e.event_id LIKE ? "
				+ "      )";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, eventId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {

					User user = new User();
					user.setUser_id(resultSet.getString("user_id"));
					user.setUser_name(resultSet.getString("user_name"));
					user.setUser_role(resultSet.getString("user_role"));
					user.setUser_email(resultSet.getString("user_email"));

					guests.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("An error occurred while fetching uninvited vendors.");
		}

		return guests;
	}
    
    public static List<User> getGuestFromEvent(String eventId) throws SQLException {
        List<User> guests = new ArrayList<>();
        String query = "SELECT * " +
                       "FROM users u " +
                       "JOIN invitations i ON u.user_id = i.user_id " +
                       "JOIN events e ON e.event_id = i.event_id " +
                       "WHERE u.user_role LIKE 'Guest' AND e.event_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Guest guest = new Guest();
                    guest.setUser_id(rs.getString("user_id"));
                    guest.setUser_name(rs.getString("user_name"));
                    guest.setUser_email(rs.getString("user_email"));
                    guest.setUser_role(rs.getString("user_role"));
                    guests.add(guest);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("An error occurred while fetching guests for the event.");
        }

        return guests;
    }
	
}
