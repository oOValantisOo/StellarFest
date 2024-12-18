package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.PageManager;
import utils.UserSession;

public class HomeGuest {
	public Scene getScene() {
		VBox container = new VBox(20);
		container.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Welcome, " + UserSession.getInstance().getCurrUser().getUser_name() + "!");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		
        Button manageInvitations = new Button("View Invitations");
        manageInvitations.setPrefWidth(400);
        manageInvitations.setPrefHeight(200);
        
	     // Set on Click       
        manageInvitations.setOnAction(event -> {
        	PageManager.getInstance().showViewInvitations(UserSession.getInstance().getCurrUser().getUser_id());
        });
       

		// Font
        manageInvitations.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        GridPane gridContainer = new GridPane();
        gridContainer.setHgap(20);
        gridContainer.setVgap(20);
        gridContainer.setAlignment(Pos.CENTER);
        
        gridContainer.add(manageInvitations, 0 , 0);
        
        
        container.getChildren().addAll(welcomeLabel, gridContainer);
		
		return new Scene(container, 300, 200);
	}
}
