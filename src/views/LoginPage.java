package views;


import controllers.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import utils.UserSession;

public class LoginPage {
	private UserController controller;

	public LoginPage(UserController controller) {
		super();
		this.controller = controller;
	}
	
	public Scene getScene() {
		
		VBox container = new VBox(0);
		container.setAlignment(Pos.CENTER);
		
        VBox formBox = new VBox(10);
        formBox.setMaxWidth(300);
        
        Label header = new Label("Login");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Label emailLbl = new Label("Email");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        Label passwordLbl = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red");
        
        HBox buttonsContainer = new HBox(10);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (!controller.validateLogin(email, password)) {
            	messageLabel.setText("Username and password field must be filled!");
            	return;
            }
            
            User u = controller.login(email, password);
            
            if (u == null) {
            	messageLabel.setText("Username and password doesn't match any credentials!");
            	return;
            }
            
            //TODO: To be Removed success message
            UserSession.authenticate(u);
            messageLabel.setText("Match!" + UserSession.getInstance().getCurrUser().getUser_role());
            
            PageManager.getInstance().showHomePage(UserSession.getInstance().getCurrUser().getUser_role().trim());
        });
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> {
        	PageManager.getInstance().showRegisterPage();
        });
        
        buttonsContainer.getChildren().addAll(loginButton, registerButton);
        
        
        formBox.getChildren().addAll(header, emailLbl, emailField, passwordLbl, passwordField, buttonsContainer, messageLabel);
        container.getChildren().add(formBox);
        
        BorderPane bp = new BorderPane();
        bp.setCenter(container);
        
        return new Scene(bp, 300, 200);
	}
}
