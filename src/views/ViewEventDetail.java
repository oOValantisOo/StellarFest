package views;

import controllers.EventOrganizerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Event;

public class ViewEventDetail {
	private EventOrganizerController controller;
	private String eventId;
	Event event;
	
	public ViewEventDetail(EventOrganizerController controller, String eventId) {
		this.controller = controller;
		this.eventId = eventId;
		
		this.event = this.controller.viewOrganizedEventDetails(eventId);
	}
	
	public Scene getScene() {
		VBox container = new VBox(12);
		container.setAlignment(Pos.CENTER_LEFT);
		container.setPrefWidth(800);
		container.setPadding(new Insets(10,400,10,400));
		
		// Back button
		Button back = new Button(" < ");
		
		Label eventId = new Label("Event ID : " + event.getEvent_id());
		
		Label organizerId = new Label("Organized By : " + event.getOrganizer_id());
		
		Label eventName = new Label(event.getEvent_name());
		eventName.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		
		HBox header = new HBox(50);
		header.getChildren().addAll(eventId, organizerId);
		
		Label eventDate = new Label(event.getEvent_date());
		Label eventLocation = new Label(event.getEvent_location());
		Label eventDescription = new Label(event.getEvent_description());
		
		
		container.getChildren().addAll(header,eventName,eventDate, eventLocation, eventDescription);
		
		BorderPane bp = new BorderPane();
		bp.setTop(back);
		bp.setCenter(container);
		
		return new Scene(bp, 1600, 900);
	}
}
