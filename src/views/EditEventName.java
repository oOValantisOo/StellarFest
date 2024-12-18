package views;

import controllers.EventOrganizerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Event;
import utils.PageManager;

public class EditEventName {
    private EventOrganizerController controller;
    private Event event;

    public EditEventName(EventOrganizerController controller, String eventId) {
        super();
        this.controller = controller;
        this.event = this.controller.viewOrganizedEventDetails(eventId);
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button back = new Button(" < ");
        back.setPadding(new Insets(10));
        back.setOnAction(action -> PageManager.getInstance().goBack());

        VBox backButtonContainer = new VBox();
        backButtonContainer.setPadding(new Insets(10));
        backButtonContainer.setAlignment(Pos.TOP_LEFT);
        backButtonContainer.getChildren().add(back);
        root.setLeft(backButtonContainer);

        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(20));

        Label eventIdLabel = new Label("Event ID: " + event.getEvent_id());
        Label eventNameLabel = new Label("Event Name: " + event.getEvent_name());
        Label eventDateLabel = new Label("Event Date: " + event.getEvent_date());
        Label eventLocationLabel = new Label("Event Location: " + event.getEvent_location());
        Label eventDescriptionLabel = new Label("Event Description: " + event.getEvent_description());
        Label organizerIdLabel = new Label("Event Creator: " + event.getOrganizer_id());

        eventIdLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        eventNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        eventDateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        eventLocationLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        eventDescriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        organizerIdLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

        Label editEventNameLabel = new Label("Edit Event Name:");
        editEventNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Enter new event name");
        eventNameField.setPrefWidth(300);

        Label feedbackLabel = new Label("");
        feedbackLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        feedbackLabel.setStyle("-fx-text-fill: red;");

        Button submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        submitButton.setOnAction(e -> {
            String newEventName = eventNameField.getText().trim();

            String response = controller.editEventName(this.event.getEvent_id(), newEventName);
            if (response.equals("Event successfully updated")) {
                feedbackLabel.setText(response);
                feedbackLabel.setStyle("-fx-text-fill: green;");
                eventNameLabel.setText("Event Name: " + newEventName);
                eventNameField.clear();
            } else {
                feedbackLabel.setText(response);
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        });

        container.getChildren().addAll(
            eventIdLabel,
            eventNameLabel,
            eventDateLabel,
            eventLocationLabel,
            eventDescriptionLabel,
            organizerIdLabel,
            editEventNameLabel,
            eventNameField,
            submitButton,
            feedbackLabel
        );

        root.setCenter(container);
        return new Scene(root, 800, 600);
    }
}