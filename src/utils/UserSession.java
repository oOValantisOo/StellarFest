package utils;

import controllers.UserController;
import models.User;

public class UserSession {
	private static UserSession instance;
	private User currUser;
	
	private UserSession(User u) {
		currUser = u;
		
	}
	
	public static UserSession getInstance() throws Error {
		if (instance == null) {
			throw new Error("You need to be authenticated first!");
		}
		
		return instance;
	}
	
	public static void authenticate(User user) {
		instance = new UserSession(user);
	}

	public User getCurrUser() {
		return currUser;
	}

	public void setCurrUser(User currUser) {
		this.currUser = currUser;
	}
	
	public void logout() {
		currUser = null;
		PageManager.getInstance().showLoginPage();
	}
	
	
}
