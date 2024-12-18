package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class Vendor extends User {
	private String accepted_invitations;
	private static Connection connection;

	public String getAccepted_invitations() {
		return accepted_invitations;
	}

	public void setAccepted_invitations(String accepted_invitations) {
		this.accepted_invitations = accepted_invitations;
	}

	static {
		connection = DatabaseConnection.getInstance().getConnection();
	}

	public Vendor() {
		super();

	}

	public static List<User> getVendors() throws SQLException {
		List<User> vendors = new ArrayList<>();
		String query = "SELECT * FROM users WHERE user_role LIKE 'Vendor'";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Vendor vendor = new Vendor();
					vendor.setUser_id(rs.getString("user_id"));
					vendor.setUser_name(rs.getString("user_name"));
					vendor.setUser_email(rs.getString("user_email"));
					vendor.setUser_password(rs.getString("user_password"));
					vendor.setUser_role("user_role");
					vendors.add(vendor);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vendors;
	}

	public static List<User> getUninvitedVendors(String eventId) throws SQLException {
		List<User> vendors = new ArrayList<>();
		String query = "SELECT * " + "FROM users u " + "WHERE u.user_role LIKE 'Vendor' AND "
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
					user.setUser_email(resultSet.getString("user_email"));
					user.setUser_role(resultSet.getString("user_role"));

					vendors.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("An error occurred while fetching uninvited vendors.");
		}

		return vendors;
	}

	public static String addVendor(String eventId, String userId) {
		User user = User.getUserById(userId);

		if (user == null) {
			return "Vendor does not exist";
		}

		String invitationId = Invitation.generateID();

		String query = "INSERT INTO invitations (invitation_id, event_id, user_id, invitation_status, invitation_role) VALUES(?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, invitationId);
			stmt.setString(2, eventId);
			stmt.setString(3, userId);
			stmt.setString(4, "pending");
			stmt.setString(5, "Vendor");

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return "Vendor invitation sent successfully!";
			} else {
				return "Failed to send vendor invitation.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "An error occurred while sending the vendor invitation.";
		}
	}

	public static String updateVendor(String description, String product) {
		String query = "UPDATE vendors SET product_name = ?, product_description = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, product);
			stmt.setString(2, description);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return "Product successfully updated";
			} else {
				return "Failed to update account";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "An error occurred while updating the profile";
		}
	}
	
	public static List<User> getVendorFromEvent(String eventId) throws SQLException {
	    List<User> vendors = new ArrayList<>();
	    String query = "SELECT * " +
	                   "FROM users u " +
	                   "JOIN invitations i ON u.user_id = i.user_id " +
	                   "JOIN events e ON e.event_id = i.event_id " +
	                   "WHERE u.user_role LIKE 'Vendor' AND e.event_id = ?";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, eventId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Vendor vendor = new Vendor();
	                vendor.setUser_id(rs.getString("user_id"));
	                vendor.setUser_name(rs.getString("user_name"));
	                vendor.setUser_email(rs.getString("user_email"));
	                vendor.setUser_role(rs.getString("user_role"));
	                vendors.add(vendor);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SQLException("An error occurred while fetching vendors for the event.");
	    }

	    return vendors;
	}

}
