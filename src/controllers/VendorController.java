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
import models.Vendor;

public class VendorController {
	private Connection connection;

	public VendorController() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}

	public String acceptInvitation(String eventId, String userId) {

		return Invitation.acceptInvitation(eventId, userId);
	}

	public List<Invitation> viewInvitations(String user_id) throws SQLException {
		return Invitation.getInvitations(user_id);
	}

	public List<Event> viewAcceptedEvents(String email) {
		return Event.viewAcceptedEvents(email);

	}

	public String manageVendor(String description, String product) {
		String valid = checkManageVendorInput(description, product);

		if (valid == "Success") {
			return Vendor.updateVendor(description, product);
		} else {
			return valid;
		}
	}

	public String checkManageVendorInput(String description, String product) {
		if (description == "" || product == "") {
			return "All fields must be filled!";
		}
		if (description.length() > 200) {
			return "Description length at max is 200 characters long!";
		}
		return "Success";
	}

	public Event viewAcceptedEventDetails(String eventId) {
		return Event.viewOrganizedEventDetails(eventId);
	}
}
