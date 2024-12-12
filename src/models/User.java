package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class User {
	private String user_id;
	private String user_name;
	private String user_email;
	private String user_password;
	private String user_role;
	
	private static Connection connection;
	
	public User() {
		connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_role() {
		return this.user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	
	public static String generateID() {
        String prefix = "US";
        int nextNum = 1; 

        String query = "SELECT COUNT(*) AS user_count FROM users";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nextNum = rs.getInt("user_count") + 1; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%03d", prefix, nextNum);
    }
	
	public static User getUserById(String user_id) {
		String query = "SELECT * FROM users WHERE user_id = ?";
		User user = null;

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, user_id);

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
	
	
	
    
    public static String deleteEvent(String eventId) {
    	String query = "DELETE FROM events WHERE event_id = ?";
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, eventId);         
	        
	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "Event successfully deleted";
	        } else {
	            return "Failed to delete event";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error occurred";
	    }
    }
    
    public static  List<User> viewAllUsers(){
    	String query = "SELECT * FROM users";
    	List<User> users = new ArrayList<>();
    	User user = null;
    	
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
 	        try (ResultSet rs = stmt.executeQuery()) {
 	            while(rs.next()) {
 	            	user = new User();
	                user.setUser_id(rs.getString("user_id"));
	                user.setUser_name(rs.getString("user_name"));
	                user.setUser_email(rs.getString("user_email"));
	                user.setUser_role(rs.getString("user_role"));
	                user.setUser_password(rs.getString("user_password"));
 	                users.add(user);

 	            }
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
    	return users;
    }
    
    public static String deleteUser(String user_id) {
    	String query = "DELETE FROM events WHERE user_id = ?";
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, user_id);         
	        
	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return "User successfully deleted";
	        } else {
	            return "Failed to delete user";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error occurred";
	    }
    }
    
    
    
    public static List<User> getAllUser() {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	            	User user = new User();
	            	user.setUser_id(rs.getString("user_id"));
	                user.setUser_name(rs.getString("user_name"));
	                user.setUser_email(rs.getString("user_email"));
	                user.setUser_role(rs.getString("user_role"));
	                user.setUser_password(rs.getString("user_password"));
	                
	                users.add(user);
	            }
	        }
	        
	       
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public static User login(String name, String password) {
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
	
	public static String updateProfile(String email, String name, String oldPassword, String newPassword) {
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

	
	public static User getUserByName(String name) {
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

	
	public  static User getUserByEmail(String email) {
		String query = "SELECT * FROM users WHERE user_email = ?";
		User user = null;

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
	
	public static boolean checkRegisterInput(String email, String name, String password) {
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
	
	public static boolean checkChangeProfile(String email, String name, String oldPassword, String newPassword) {
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
    
    public static String validateRegister(String name, String password, String email, String role) {
    	if (name.isEmpty() || password.isEmpty() || email.isEmpty() || role.isEmpty() ) {
    		return "All field must be filled!";
    	}
    	
    	if (password.length() < 5) {
    		return "Password length must be at least 5 letters!";
    	}
    	
    	if (getUserByEmail(email) != null) {
    		return "Email must be unique!";
    	}
    	
    	if (getUserByName(name) != null) {
    		return "Username must be unique!";
    	}
    	
    	// TODO: Implement Unique Validation
    	return "";
    }
	
    public static void register(String name, String password, String email, String role) {
    	String currID = generateID();
        String query = "INSERT INTO users (user_id, user_name, user_password, user_email, user_role) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	stmt.setString(1, currID);  
            stmt.setString(2, name);     
            stmt.setString(3, password);   
            stmt.setString(4, email);      
            stmt.setString(5, role);       

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
    
    public static boolean validateLogin(String name, String password) {
    	if (name.isEmpty() || password.isEmpty()) {
    		return false;
    	}
    	
    	return true;
    }
	
}
