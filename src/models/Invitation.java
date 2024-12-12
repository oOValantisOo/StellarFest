package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class Invitation {
	private String invitation_id;
	private String event_id;
	private String user_id;
	private String invitation_status;
	private String invitation_role;

	private static Connection connection;

	public Invitation() {
		super();

		connection = DatabaseConnection.getInstance().getConnection();
	}

	public String getInvitation_id() {
		return invitation_id;
	}

	public void setInvitation_id(String invitation_id) {
		this.invitation_id = invitation_id;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getInvitation_status() {
		return invitation_status;
	}

	public void setInvitation_status(String invitation_status) {
		this.invitation_status = invitation_status;
	}

	public String getInvitation_role() {
		return invitation_role;
	}

	public void setInvitation_role(String invitation_role) {
		this.invitation_role = invitation_role;
	}

	public static String generateID() {
		String prefix = "IN";
		int nextNum = 1;

		String query = "SELECT COUNT(*) AS invitation_count FROM invitations";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				nextNum = rs.getInt("invitation_count") + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return String.format("%s%03d", prefix, nextNum);
	}

	public static String sendInvitation(String email, String eventID) {

		String query = "INSERT INTO invitations (invitation_id, event_id, user_id, invitation_role, invitation_status) VALUES (?, ?, ?, ?, ?)";

		User u = User.getUserByEmail(email);

		String invitation_id = Invitation.generateID();
		String user_id = u.getUser_id();
		String role = u.getUser_role();
		String status = "pending";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, invitation_id);
			stmt.setString(2, eventID);
			stmt.setString(3, user_id);
			stmt.setString(4, role);
			stmt.setString(5, status);

			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted > 0) {
				return "Invitation sent successfully to " + email;
			} else {
				return "Failed to send invitation to " + email;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error occurred while sending invitation: " + e.getMessage();
		}

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

	public static List<Invitation> getInvitations(String userId) throws SQLException {

		String query = "SELECT * FROM invitations WHERE user_id = ?";
		List<Invitation> invitations = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
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
