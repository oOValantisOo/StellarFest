package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import models.User;

public class UserController {
	private Connection connection;

    public UserController() {
        this.connection = DatabaseConnection.getInstance().getConnection(); 
    }
    
    public String generateID() {
    	return "";
    }
    
    public String validateRegister(String name, String password, String email, String role) {
    	if (name.isEmpty() || password.isEmpty() || email.isEmpty() || role.isEmpty() ) {
    		return "All field must be filled!";
    	}
    	
    	if (password.length() < 5) {
    		return "Password length must be at least 5 letters!";
    	}
    	
    	// TODO: Implement Unique Validation
    	return "";
    }
	
    public void register(String name, String password, String email, String role) {
        String query = "INSERT INTO users (user_name, user_password, user_email, user_role) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);     
            stmt.setString(2, password);   
            stmt.setString(3, email);      
            stmt.setString(4, role);       

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean validateLogin(String name, String password) {
    	if (name.isEmpty() || password.isEmpty()) {
    		return false;
    	}
    	
    	return true;
    }

	
	public User login(String name, String password) {
		String query = "SELECT * FROM users WHERE user_email = ? AND user_password = ?";
		User user = null;
		
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, name);
	        stmt.setString(2, password);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setUser_id(rs.getString("user_id"));
	                user.setUser_name(rs.getString("user_name"));
	                user.setUser_email(rs.getString("user_email"));
	                user.setUser_role(rs.getString("user_role"));
	                user.setUser_password(rs.getString("user_password"));
	                
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return user;
	}
	
	public String updateProfile(String email, String name, String oldPassword, String newPassword) {
	    User user = getUserByName(name);
	    if (user == null) {
	        return "User not found";
	    }

	    if (!user.getUser_password().equals(oldPassword)) {
	        return "Invalid old password";
	    }

	    boolean valid = checkChangeProfile(email, name, oldPassword, newPassword);
	    if (!valid) {
	        return "Cannot update profile due to invalid inputs";
	    }

	    String query = "UPDATE user SET user_name = ?, user_email = ?, user_password = ? WHERE user_name = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, name);         
	        stmt.setString(2, email);         
	        stmt.setString(3, newPassword);  
	        stmt.setString(4, user.getUser_name()); 

	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "Account successfully updated";
	        } else {
	            return "Failed to update account";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "An error occurred while updating the profile";
	    }
	}

	
	public User getUserByName(String name) {
	    String query = "SELECT * FROM users WHERE user_name = ?";
	    User user = null;

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, name);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setUser_id(rs.getString("user_id"));
	                user.setUser_name(rs.getString("user_name"));
	                user.setUser_email(rs.getString("user_email"));
	                user.setUser_role(rs.getString("user_role"));
	                user.setUser_password(rs.getString("user_password"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return user;
	}

	
	public User getUserByEmail(String email) {
		String query = "SELECT * FROM users WHERE user_name = ?";
	    User user = getUserByName(email);

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, email);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setUser_id(rs.getString("user_id"));
	                user.setUser_name(rs.getString("user_name"));
	                user.setUser_email(rs.getString("user_email"));
	                user.setUser_role(rs.getString("user_role"));
	                user.setUser_password(rs.getString("user_password"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return user;
	}
	
	public boolean checkRegisterInput(String email, String name, String password) {
		if(email == "" || name == "" || password == "") {
			return false;
		} else if(getUserByName(name)!=null) {
			return false;
		} else if(getUserByEmail(email)!=null) {
			return false;
		} else if(password.length() < 5) {
			return false;
		}
			
		return true;
	}
	
	public boolean checkChangeProfile(String email, String name, String oldPassword, String newPassword) {
		if(email == "" || name == "" || newPassword == "") {
			return false;
		} else if(getUserByName(name)!=null) {
			return false;
		} else if(getUserByEmail(email)!=null) {
			return false;
		} else if(newPassword.length() < 5) {
			return false;
		}
		return true;
	}
}
