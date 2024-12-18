package views;

import controllers.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.User;
import utils.PageManager;

public class RegisterPage {
	private UserController controller;

	public RegisterPage(UserController controller) {
		super();
		this.controller = controller;
	}
	
	public Scene getScene() {
		VBox container = new VBox(0);
		container.setAlignment(Pos.CENTER);
		
        VBox formBox = new VBox(10);
        formBox.setMaxWidth(300);
        
        Label header = new Label("Register");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Label emailLbl = new Label("Email");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        Label usernameLbl = new Label("Username");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        
        Label passwordLbl = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        Label roleLbl = new Label("Role");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Event Organizer", "Vendor", "Guest");
        roleComboBox.setPromptText("Select a role");


        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red");
        
        HBox buttonsContainer = new HBox(10);

        Button regisButton = new Button("Register");
        regisButton.setOnAction(event -> {
            String name = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String role = roleComboBox.getValue();
            
            String error = controller.validateRegister(name, password, email, role);
            
            if (!error.isEmpty()) {
            	messageLabel.setText(error);
            	return;
            }
            
            controller.register(name, password, email, role);
            
            // TODO: to be removed or changed success message
            messageLabel.setText("Success!");
            
        });
        regisButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
        	PageManager.getInstance().showLoginPage();
        });
        
        buttonsContainer.getChildren().addAll(regisButton, loginButton);
        
        
        formBox.getChildren().addAll(header, emailLbl, emailField,  usernameLbl, usernameField, passwordLbl, passwordField, roleLbl, roleComboBox, buttonsContainer, messageLabel);
        container.getChildren().add(formBox);
        
        BorderPane bp = new BorderPane();
        bp.setCenter(container);
        
        return new Scene(bp, 800, 600);
	}
}
