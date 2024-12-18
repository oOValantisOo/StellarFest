package views;

import java.sql.SQLException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.PageManager;

public class HomeEventOrganizer {

    public HomeEventOrganizer() {
        super();
    }

    public Scene getScene() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, Event Organizer!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        Button viewOrganizedEventsButton = new Button("View Organized Events");
        Button createEventButton = new Button("Create Event");

        viewOrganizedEventsButton.setPrefSize(400, 100);
        createEventButton.setPrefSize(400, 100);

        viewOrganizedEventsButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        createEventButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

        viewOrganizedEventsButton.setOnAction(event -> {
            PageManager.getInstance().showViewOrganizedEvents();
        });


        createEventButton.setOnAction(event -> {
            // TODO: Implement create event functionality
        	try {
				PageManager.getInstance().showCreateEvent();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });

        GridPane gridContainer = new GridPane();
        gridContainer.setHgap(20);
        gridContainer.setVgap(10);
        gridContainer.setAlignment(Pos.CENTER);

        gridContainer.add(viewOrganizedEventsButton, 0, 0);
        gridContainer.add(createEventButton, 0, 1);

        container.getChildren().addAll(welcomeLabel, gridContainer);

        return new Scene(container, 800, 600);
    }
}