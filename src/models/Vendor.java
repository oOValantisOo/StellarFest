package models;

public class Vendor extends User{
	private String accepted_invitations;
	
	public String getAccepted_invitations() {
		return accepted_invitations;
	}

	public void setAccepted_invitations(String accepted_invitations) {
		this.accepted_invitations = accepted_invitations;
	}

	public Vendor() {
		super();
	}

}
