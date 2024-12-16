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

public class HomeAdmin {
	
	public Scene getScene() {
		VBox container = new VBox(20);
		container.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Welcome, ");
//		Label welcomeLabel = new Label("Welcome, " + UserSession.getInstance().getCurrUser().getUser_name() + "!");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		
		Button manageUsersButton = new Button("Manage Users");
		manageUsersButton.setPrefWidth(400);
		manageUsersButton.setPrefHeight(200);
		
        Button manageEventsButton = new Button("Manage Events");
        manageEventsButton.setPrefWidth(400);
        manageEventsButton.setPrefHeight(200);
        
        // Font
        manageUsersButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        manageEventsButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        // Set on Click
        manageUsersButton.setOnAction(event -> {
        	PageManager.getInstance().showViewUsers();
        });
        
        manageEventsButton.setOnAction(event -> {
        	PageManager.getInstance().showViewEvents();
        });
        
        GridPane gridContainer = new GridPane();
        gridContainer.setHgap(20);
        gridContainer.setVgap(20);
        gridContainer.setAlignment(Pos.CENTER);
        
        gridContainer.add(manageUsersButton, 0, 0); // Column 0, Row 0
        gridContainer.add(manageEventsButton, 1, 0); // Column 1, Row 0
        
        container.getChildren().addAll(welcomeLabel, gridContainer);
		
		return new Scene(container, 1600, 900);
	}
}
